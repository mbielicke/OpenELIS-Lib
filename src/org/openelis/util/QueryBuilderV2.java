package org.openelis.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.openelis.gwt.common.Meta;
import org.openelis.gwt.common.MetaMap;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.QueryFieldUtil;

public class QueryBuilderV2 {
	private String selectStatement = "";
    private HashMap<String, Meta> fromTables = new HashMap<String, Meta>();
	private ArrayList whereOperands = new ArrayList();
	private String orderByStatement = "";
    private MetaMap meta;
	
    
    public void setMeta(MetaMap meta){
        this.meta = meta;
    }
    
    public static String getQuery(QueryFieldUtil field, String fieldName) {
        if (field.getParameter() == null || field.getParameter().size() == 0)
            return "";
        
        StringBuffer sb = new StringBuffer();
        sb.append(" and (");
        
        sb.append(getQueryNoOperand(field, fieldName));
        
        sb.append(")");
        
        return sb.toString();
    }
    
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
            if (comp.startsWith("!")) {
                sb.append("not ");
                comp = comp.substring(1);
            }
            if (comp.equals("~"))
                comp = "like ";
            String param = (String)fieldParamIt.next();
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
    
    public String getSelectStatement(){
    	return selectStatement;
    }
    
    
    public void addWhere(ArrayList<QueryData> fields) throws Exception{
    	//fieldsFromRPC = fields;
//    	where clause
    	for (QueryData field : fields){
            boolean columnFound = meta.hasColumn(field.key);

            if(!columnFound)
                throw new Exception("column not found [" + field.key + "]");    	
            
            QueryFieldUtil qField = new QueryFieldUtil();
            qField.parse(field.query);
            String whereClause = getQueryNoOperand(qField, field.key);
            if(!"".equals(whereClause)){
                whereOperands.add(whereClause);
		
				//add the table name to the from hash map
			//	addTable(meta);
            }
        }
    }
    
    /**
     * Adds a single where statement from a string.
     * @param whereStatement
     */
    public void addWhere(String whereStatement){
    	whereOperands.add(whereStatement);
    }
    
    public void clearWhereClause(){
        whereOperands.clear();
    }
    
    public void setQueryParams(Query query, ArrayList<QueryData> fields){
        for (QueryData field : fields) {//int i = 0; i < keys.length; i++) {
        	QueryFieldUtil qField = new QueryFieldUtil();
        	qField.parse(field.query);
        	
            if(field.type == QueryData.Type.DOUBLE)
                setDoubleParameters(qField, field.key, query);
			else if(field.type == QueryData.Type.STRING) 	
			    setStringParameters(qField, field.key, query);
			else if(field.type== QueryData.Type.INTEGER) 
			    setIntegerParameters(qField, field.key, query);
            else if(field.type == QueryData.Type.DATE)
                setDateParameters(qField, field.key, query);
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
    
    public void setOrderBy(String orderBy){
    	this.orderByStatement = orderBy;
    }
    
    public String getOrderBy(){
        if(!"".equals(orderByStatement))
            return " ORDER BY "+orderByStatement;
        else 
            return "";
    }
    
    public String getWhereClause(){
        String returnString = "";
        if(whereOperands.size() > 0){
            returnString = " WHERE ";
            for(int j=0; j < whereOperands.size(); j++)
                if(j>0)
                    returnString += " and ("+whereOperands.get(j)+") ";
                else
                    returnString += "("+whereOperands.get(j)+") ";
        }
        return returnString;
    }
    
    public String getFromClause(String where){
        return meta.buildFrom(where);
    }
    
    public String getSelectClause(){
        return "SELECT "+selectStatement+" FROM ";
    }
    
    /**
     * Checks to see if the table exists in the from statement.  It will return true if it does.
     * @param tableName
     * @return
     */
    public boolean hasTable(String tableName){
    	return fromTables.containsKey(tableName);
    }
    
    public static void setStringParameters(QueryFieldUtil field,
    		String fieldName,
    		Query query) {
    	if(field.getParameter() == null)
    		return;

    	String paramName = getParamName(fieldName);

    	Iterator fieldParamIt = field.getParameter().iterator();
    	int i = 0;
    	while (fieldParamIt.hasNext()) {
    		String param = (String)fieldParamIt.next();
    		if (param.indexOf("..") > -1) {
    			String[] bparams = param.split("..");
    			query.setParameter(paramName + i + "0", bparams[0]);
    			query.setParameter(paramName + i + "1", bparams[1]);
    		} else if (param.indexOf(",") > -1) {
    			String[] params = param.split(",");
    			for (int j = 0; j < params.length; j++) {
    				query.setParameter(paramName + i + j, params[j]);
    			}
    		} else
    			query.setParameter(paramName + i, param);
    		i++;
    	}
    }



    public static void setIntegerParameters(QueryFieldUtil field,
    		String fieldName,
    		Query query) {
    	if(field.getParameter() == null)
    		return;

    	String paramName = getParamName(fieldName);
    	Iterator fieldParamIt = field.getParameter().iterator();
    	int i = 0;
    	while (fieldParamIt.hasNext()) {
    		String param = (String)fieldParamIt.next();

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
    		i++;
    	}
    }

    public static void setDoubleParameters(QueryFieldUtil field,
    		String fieldName,
    		Query query) {
    	if(field.getParameter() == null)
    		return;

    	String paramName = getParamName(fieldName);
    	Iterator fieldParamIt = field.getParameter().iterator();
    	int i = 0;
    	while (fieldParamIt.hasNext()) {
    		String param = (String)fieldParamIt.next();

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
    		i++;
    	}
    }
    
    private static String getParamName(String name){
        while(name.indexOf(".") > -1){
            name = name.substring(0,name.indexOf(".")) + name.substring(name.indexOf(".")+1,name.length());
        }
        return name;
    }

    public static void setDateParameters(QueryFieldUtil field,
    		String fieldName,
    		Query query) {
    	String paramName = getParamName(fieldName);
    	Iterator fieldParamIt = field.getParameter().iterator();
    	int i = 0;
    	while (fieldParamIt.hasNext()) {
    		String param = (String)fieldParamIt.next();
    		if (param.indexOf("..") > -1) {
    			String[] bparams = param.split("..");
    			Date date = new Date(bparams[0]);
    			query.setParameter(paramName + i + "0", date, TemporalType.DATE);
    			date = new Date(bparams[1]);
    			query.setParameter(paramName + i + "1", date, TemporalType.DATE);
    		} else if (param.indexOf(",") > -1) {
    			String[] params = param.split(",");
    			for (int j = 0; j < params.length; j++) {
    				Date date = new Date(params[j]);
    				query.setParameter(paramName + i + j,
    						date,
    						TemporalType.DATE);
    			}
    		} else {
    			Date date = new Date(param);
    			query.setParameter(paramName + i, date, TemporalType.DATE);
    		}
    		i++;
    	}
    }
}
