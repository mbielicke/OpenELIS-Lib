package org.openelis.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.openelis.gwt.common.Meta;
import org.openelis.gwt.common.MetaMap;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.QueryFieldUtil;

import sun.security.action.GetLongAction;

public class QueryBuilderV2 {
	private String selectStatement = "";
    private HashMap<String, Meta> fromTables = new HashMap<String, Meta>();
	private ArrayList<WhereClause> whereOperands = new ArrayList<WhereClause>();
	private String orderByStatement = "";
    private MetaMap meta;
	
    /**
     * This method sets the Meta to be used to compare and validate field keys returned from the 
     * client for query data.
     */
    public void setMeta(MetaMap meta){
        this.meta = meta;
    }
    
    /**
     * Possibly creates a standard SQL statement, but not sure of the purpose of this method
     * 
     */
    private static String getQuery(QueryFieldUtil field, String fieldName) {
        if (field.getParameter() == null || field.getParameter().size() == 0)
            return "";
        
        StringBuffer sb = new StringBuffer();
        sb.append(" and (");
        
        sb.append(getQueryNoOperand(field, fieldName));
        
        sb.append(")");
        
        return sb.toString();
    }
    
    /**
     * Builds entrys into the where clause list using the passed QueryFieldUtil and using the fieldName passed.
     * @param field
     * @param fieldName
     * @return
     */
    public static String getQueryNoOperand(QueryFieldUtil field, String fieldName) {

    	if (field.getParameter() == null || field.getParameter().size() == 0)
            return "";
        String paramName = getParamName(fieldName);
        StringBuffer sb = new StringBuffer();
        Iterator fieldCompIt = field.getComparator().iterator();
        Iterator fieldParamIt = field.getParameter().iterator();
        Iterator fieldLogicalIt = field.getLogical().iterator();
        int i = 0;
        while (fieldCompIt.hasNext()) {
            sb.append(fieldName + " ");
            String comp = (String)fieldCompIt.next();
            String param = (String)fieldParamIt.next();
            if(param.equals("NULL")){
            	if(comp.startsWith("!"))
            		sb.append("IS NOT NULL ");
            	else
            		sb.append("IS NULL ");
            }else {
            	if (comp.startsWith("!")) {
            		sb.append("<> ");
            		comp = comp.substring(1);
            	}
            	if (comp.equals("~"))
            		comp = "like ";

            	if (comp.endsWith("(")) {
            		String[] list = param.split(",");
            		sb.append("in (");
            		for (int j = 0; j < list.length; j++) {
            			if (j > 0)
            				sb.append(",");
            			sb.append(":" + paramName + i + j);
            		}
            		sb.append(") ");
            	} else if (comp.startsWith("between")) {
            		sb.append("between :" + paramName
            				+ i
            				+ "0 and :"
            				+ paramName
            				+ i
            				+ "1 ");
            	} else
            		sb.append(comp + " :" + paramName + i + " ");
            }
            if (fieldLogicalIt.hasNext()) {
                String logical = (String)fieldLogicalIt.next();
                if (logical.equals("|"))
                    logical = "or ";
                else
                    logical = "and ";
                sb.append(logical + " ");
            }
            i++;
        }
        return sb.toString();
    }
    

    /**
     * Sets the select statement that will be used in the query. 
     * Leave off the word select as it will be added automatically.
     * @param selectStatment
     */
    public void setSelect(String selectStatment){
    	this.selectStatement = selectStatment;
    }
    
    /**
     * Resturns the currently constructed Select statement used in the Query
     * @return
     */
    public String getSelectStatement(){
    	return selectStatement;
    }
    
    /**
     * Use constructWhere(ArrayList<QueryData> fields);
     * @param fields
     * @throws Exception
     */
    @Deprecated public void addWhere(ArrayList<QueryData> fields) throws Exception {
    	constructWhere(fields);
    }
    
    /**
     * Constructs a sql "where" clause based on QueryData list returned from the client.
     * @param fields
     * @throws Exception
     */
    public void constructWhere(ArrayList<QueryData> fields) throws Exception{
    	WhereClause whereClause;
    	for (QueryData field : fields){
            boolean columnFound = meta.hasColumn(field.getKey());

            if(!columnFound)
                throw new Exception("column not found [" + field.getKey() + "]");    	
            
            QueryFieldUtil qField = new QueryFieldUtil();
            qField.parse(field.getQuery());
            String where = getQueryNoOperand(qField, field.getKey());
            if(!"".equals(where)){
            	whereClause = new WhereClause();
            	whereClause.clause = where;
            	whereClause.logical = field.getLogical();
                whereOperands.add(whereClause);		
            }
        }
    }
    
    /**
     * Adds a single where statement from a string.
     * @param whereStatement
     */
    public void addWhere(String whereStatement){
    	addWhere(whereStatement,QueryData.Logical.AND);
    }
    
    public void addWhere(String whereStatement,QueryData.Logical logical) {
    	WhereClause whereClause;
    	
    	whereClause = new WhereClause();
    	whereClause.clause = whereStatement;
    	whereClause.logical = logical;
    	whereOperands.add(whereClause);
    }
    
    /**
     * Clears the current where clause list.
     */
    public void clearWhereClause(){
        whereOperands.clear();
    }
    
    /**
     * Sets the query values in the Query object passed into the method using the list of QueryData also passed into the 
     * method 
     * @param query
     * @param fields
     */
    public static void setQueryParams(Query query, ArrayList<QueryData> fields){
        for (QueryData field : fields) {//int i = 0; i < keys.length; i++) {
        	QueryFieldUtil qField = new QueryFieldUtil();
        	try {
        	    qField.parse(field.getQuery());
        	}catch(Exception e){}
        	
        	switch(field.getType()) {
        		case DOUBLE :
        			setDoubleParameters(qField, field.getKey(), query);
        			break;
        		case STRING : 	
        			setStringParameters(qField, field.getKey(), query);
        			break;
        		case INTEGER : 
        			setIntegerParameters(qField, field.getKey(), query);
        			break;
        		case DATE :
        			setDateParameters(qField, field.getKey(), query);
        	}
        }
    }
    
    /**
     * This method adds a table to the from statement in the query.
     * @param meta
     */
    /*
    public void addTable(Meta meta){
    	boolean addTableToOrderedArray = false;
    	addTableToOrderedArray = !fromTables.containsKey(meta.getEntity());
    	
    	fromTables.put(meta.getEntity(), meta);
    	
    	if(addTableToOrderedArray)
    		orderedFromTableKeys.add(meta.getEntity());
    }
    
    public String[] getTables(){
    	return null;
    	//TODO do this
    }
    */
    /**
     * Takes the different pieces of the query and put thems together.  It will return the query as a string.
     * @return
     */
    public String getEJBQL(){
    	StringBuffer query = new StringBuffer();
        
        String where = getWhereClause();
    	query.append(getSelectClause());
    	
    	//from tables
        query.append(getFromClause(where));
        
        //where clause
        query.append(where);
        
        //order by
        query.append(getOrderBy());
        
    	return query.toString();
    }
    
    /**
     * Adds the passes "order by" sql fragment to the current Query
     * @param orderBy
     */
    public void setOrderBy(String orderBy){
    	this.orderByStatement = orderBy;
    }
    
    /**
     * returns the current order by clause set in the Query.
     * @return
     */
    public String getOrderBy(){
        if(!"".equals(orderByStatement))
            return " ORDER BY "+orderByStatement;
        else 
            return "";
    }
    
    /**
     * Returns the sql "where" section that is set for the current query.
     * @return
     */
    public String getWhereClause(){
        StringBuffer returnString = new StringBuffer();
        if(whereOperands.size() > 0){
            returnString.append(" WHERE ");
            for(int j = 0; j < whereOperands.size(); j++) {
            	if(whereOperands.get(j).logical == QueryData.Logical.OR)
            		continue;
                if(j>0)
                    returnString.append(" and ("+whereOperands.get(j).clause+") ");
                else
                    returnString.append("(("+whereOperands.get(j).clause+")");
            }
            returnString.append(")");
            for(int j = 0; j < whereOperands.size(); j++) {
            	if(whereOperands.get(j).logical == QueryData.Logical.AND)
            		continue;
            	if(returnString.length() > 7) 
            		returnString.append(" or ("+whereOperands.get(j).clause+") ");
            	else
            		returnString.append("("+whereOperands.get(j).clause+")");
            }
        }
        return returnString.toString();
    }
    
    /**
     * returns the sql "from" portion that is set for the current query.
     * @param where
     * @return
     */
    public String getFromClause(String where){
        return meta.buildFrom(where);
    }
    
    /**
     * returns the sql "select" portion of the currently set Query
     * @return
     */
    public String getSelectClause(){
        return "SELECT "+selectStatement+" FROM ";
    }
    
    /**
     * Checks to see if the table exists in the from statement.  It will return true if it does.
     * @param tableName
     * @return
     */
    private boolean hasTable(String tableName){
    	return fromTables.containsKey(tableName);
    }
    
    /**
     * Pulls string parameters out of the passed QueryFieldUtil and sets them to the correct params in the
     * query.
     * @param field
     * @param fieldName
     * @param query
     */
    private static void setStringParameters(QueryFieldUtil field,
    		String fieldName,
    		Query query) {
    	if(field.getParameter() == null)
    		return;

    	String paramName = getParamName(fieldName);

    	Iterator fieldParamIt = field.getParameter().iterator();
    	int i = 0;
    	while (fieldParamIt.hasNext()) {
    		String param = (String)fieldParamIt.next();
    		if(param.indexOf("NULL") < 0) {
    			if (param.indexOf("..") > -1) {
    				String[] bparams = param.split("\\.\\.");
    				query.setParameter(paramName + i + "0", bparams[0]);
    				query.setParameter(paramName + i + "1", bparams[1]);
    			} else if (field.getComparator().get(i).startsWith("(") && param.indexOf(",") > -1) {
    				String[] params = param.split(",");
    				for (int j = 0; j < params.length; j++) {
    					query.setParameter(paramName + i + j, params[j]);
    				}
    			} else
    				query.setParameter(paramName + i, param);
    		}
    		i++;
    	}
    }


    /**
     * Pulls Integer parameters out of the passed QueryFieldUtil and sets them to the correct params in the
     * query.
     * @param field
     * @param fieldName
     * @param query
     */
    private static void setIntegerParameters(QueryFieldUtil field,
    		String fieldName,
    		Query query) {
    	if(field.getParameter() == null)
    		return;

    	String paramName = getParamName(fieldName);
    	Iterator fieldParamIt = field.getParameter().iterator();
    	int i = 0;
    	while (fieldParamIt.hasNext()) {
    		String param = (String)fieldParamIt.next();
    		if(param.indexOf("NULL") < 0) {
    			if (param.indexOf("..") > -1) {
    				String param1 = param.substring(0, param.indexOf(".."));
    				String param2 = param.substring(param.indexOf("..") + 2,
    						param.length());

    				query.setParameter(paramName + i + "0",
    						new Integer(param1.trim()));
    				query.setParameter(paramName + i + "1",
    						new Integer(param2.trim()));

    			} else if (param.indexOf(",") > -1) {
    				String[] params = param.split(",");
    				for (int j = 0; j < params.length; j++) {
    					query.setParameter(paramName + i + j,
    							new Integer(params[j].trim()));

    				}
    			} else {
    				query.setParameter(paramName + i, new Integer(param.trim()));
    			}
    		}
    		i++;
    	}
    }
   
    /**
    * Pulls double parameters out of the passed QueryFieldUtil and sets them to the correct params in the
    * query.
    */
    private static void setDoubleParameters(QueryFieldUtil field,
    		String fieldName,
    		Query query) {
    	if(field.getParameter() == null)
    		return;

    	String paramName = getParamName(fieldName);
    	Iterator fieldParamIt = field.getParameter().iterator();
    	int i = 0;
    	while (fieldParamIt.hasNext()) {
    		String param = (String)fieldParamIt.next();
    		if(param.indexOf("NULL") < 0) {
    			if (param.indexOf("..") > -1) {
    				String param1 = param.substring(0, param.indexOf(".."));
    				String param2 = param.substring(param.indexOf("..") + 2,
    						param.length());

    				query.setParameter(paramName + i + "0",
    						new Double(param1.trim()));
    				query.setParameter(paramName + i + "1",
    						new Double(param2.trim()));
    			} else if (param.indexOf(",") > -1) {
    				String[] params = param.split(",");
    				for (int j = 0; j < params.length; j++) {
    					query.setParameter(paramName + i + j,
    							new Double(params[j].trim()));
    				}
    			} else {
    				query.setParameter(paramName + i, new Double(param.trim()));
    			}
    		}
    		i++;
    	}
    }
    /**
     * 
     * @param name
     * @return
     */
    private static String getParamName(String name){
        while(name.indexOf(".") > -1){
            name = name.substring(0,name.indexOf(".")) + name.substring(name.indexOf(".")+1,name.length());
        }
        return name;
    }

    /**
     * Pulls Date parameters out of the passed QueryFieldUtil and sets them to the correct params in the
     * query.
     * @param field
     * @param fieldName
     * @param query
     */
    private static void setDateParameters(QueryFieldUtil field,
    		String fieldName,
    		Query query) {
    	String paramName = getParamName(fieldName);
    	Iterator fieldParamIt = field.getParameter().iterator();
    	TemporalType dateType = TemporalType.DATE; 
    	
    	int i = 0;
    	while (fieldParamIt.hasNext()) {
    		String param = (String)fieldParamIt.next();
    		if(param.indexOf("NULL") < 0) {
    			if(param.indexOf(":") > -1)
    				dateType = TemporalType.TIMESTAMP;
    			if (param.indexOf("..") > -1) {
    				String[] bparams = param.split("\\.\\.");
    				Date date = new Date(bparams[0].replaceAll("-","/"));
    				query.setParameter(paramName + i + "0", date, dateType);
    				date = new Date(bparams[1].replaceAll("-","/"));
    				query.setParameter(paramName + i + "1", date, dateType);
    			} else if (param.indexOf(",") > -1) {
    				String[] params = param.split(",");
    				for (int j = 0; j < params.length; j++) {
    					Date date = new Date(params[j].replaceAll("-","/"));
    					query.setParameter(paramName + i + j,
    							date,
    							dateType);
    				}
    			} else {
    				Date date;
    				if(param.indexOf("-") < 0 && param.indexOf("/") < 0){
    					dateType = TemporalType.TIMESTAMP;
						String[] time = param.split(":");
						if(time.length == 3)
							date = new Date(0,11,31,Integer.parseInt(time[0]),Integer.parseInt(time[1]),Integer.parseInt(time[2])); 	
						else
							date = new Date(0,11,31,Integer.parseInt(time[0]),Integer.parseInt(time[1]));
    				}else{
    					date = new Date(param.replaceAll("-","/"));
    				}
    				query.setParameter(paramName + i, date, dateType);
    			}
    		}
    		i++;
    	}
    }
    
    public static String securityParser(String clause) {
    	StringBuffer qc;
    	
    	qc = new StringBuffer("(");
    	for(char c : clause.toCharArray()){
    		switch(c) {
    			case ':' :
    				qc.append(" in (");
    				break;
    			case ';' :
    				qc.append(")");
    				break;
    			case '&' :
    				qc.append(" and ");
    				break;
    			case '|' :
    				qc.append(" or ");
    			    break;
    			default :
    				qc.append(c);
    		}
    	}
    	qc.append(")");
    	
    	return qc.toString();
    		
    }
    
    private class WhereClause {
    	protected String clause;
    	protected QueryData.Logical logical;
    }
}
