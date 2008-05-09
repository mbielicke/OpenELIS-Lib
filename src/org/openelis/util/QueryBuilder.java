package org.openelis.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.CollectionField;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.DropDownField;
import org.openelis.gwt.common.data.NumberObject;
import org.openelis.gwt.common.data.OptionItem;
import org.openelis.gwt.common.data.QueryCheckField;
import org.openelis.gwt.common.data.QueryDateField;
import org.openelis.gwt.common.data.QueryField;
import org.openelis.gwt.common.data.QueryNumberField;
import org.openelis.gwt.common.data.QueryOptionField;
import org.openelis.gwt.common.data.QueryStringField;
import org.openelis.gwt.common.data.StringObject;

public class QueryBuilder {
	
	private String selectStatement = "";
	private HashMap<String, Meta> fromTables = new HashMap<String, Meta>();
	private ArrayList orderedFromTableKeys = new ArrayList();
	private HashMap<String, AbstractField> fieldsFromRPC = new HashMap<String, AbstractField>();
	private ArrayList whereOperands = new ArrayList();
	private String orderByStatement = "";
	private ArrayList metaList = new ArrayList();
	

    public static String getQuery(QueryField field, String fieldName) {
        if (field.getParameter() == null || field.getParameter().size() == 0)
            return "";
        
        StringBuffer sb = new StringBuffer();
        sb.append(" and (");
        
        sb.append(getQueryNoOperand(field, fieldName));
        
        sb.append(")");
        
        return sb.toString();
    }
    
    public static String getQueryNoOperand(QueryField field, String fieldName) {

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

    //TODO get rid of soon
    public static String getQuery(QueryOptionField field, String fieldName) {
        if (field.getSelections().size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        sb.append(" and (");
        
        getQueryNoOperand(field, fieldName);
        
        sb.append(") ");
        
        return sb.toString();
    }
    
    public static String getQueryNoOperand(QueryOptionField field, String fieldName) {
    	if (field.getSelections().size() == 0)
            return "";
        String paramName = getParamName(fieldName);
        StringBuffer sb = new StringBuffer();
        sb.append(fieldName + " in (");
        for (int i = 0; i < field.getSelections().size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(":" + paramName + i);
        }
        sb.append(") ");
        return sb.toString();
    }
    
    //TODO could get rid of this after new dropdownfield is implemented
    public static String getQuery(CollectionField field, String fieldName) {
        ArrayList list = (ArrayList) field.getValue();
        if (list.size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        sb.append(" and (");
       
        getQueryNoOperand(field, fieldName);
        
        sb.append(") ");
        
        return sb.toString();
    }
    
    public static String getQueryNoOperand(CollectionField field, String fieldName) {
        ArrayList list = (ArrayList) field.getValue();
        if (list.size() == 0)
            return "";
        String paramName = getParamName(fieldName);
        StringBuffer sb = new StringBuffer();
        sb.append(fieldName + " in (");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(":" + paramName + i);
        }
        sb.append(") ");
        return sb.toString();
    }
    
    public static String getQuery(DropDownField field, String fieldName) {
    	//this should always be an arraylist of datasets
        ArrayList list = (ArrayList) field.getSelections();
        if (list.size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        
        if (list.size() == 0)
            return "";

        sb.append(" and (");
       
        getQueryNoOperand(field, fieldName);
        
        sb.append(") ");

        return sb.toString();
    }
    
    public static String getQueryNoOperand(DropDownField field, String fieldName) {
    	//this should always be an arraylist of datasets
        ArrayList list = (ArrayList) field.getSelections();
        if (list.size() == 0)
            return "";
        String paramName = getParamName(fieldName);

        StringBuffer sb = new StringBuffer();
        sb.append(fieldName + " in (");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(":" + paramName + i);
        }
        sb.append(") ");

        return sb.toString();
    }
    
    public static String getQuery(QueryCheckField field, String fieldName) {
        if(field.getValue() == null)
            return "";
        
        StringBuffer sb = new StringBuffer();
 
        sb.append(" and (");
        
        getQueryNoOperand(field, fieldName);
        
        sb.append(") ");
        
        return sb.toString();
    }   
    
    public static String getQueryNoOperand(QueryCheckField field, String fieldName) {
        if(field.getValue() == null)
            return "";
        
        StringBuffer sb = new StringBuffer();
        String paramName = getParamName(fieldName);
        sb.append(fieldName + " =  :" + paramName);

        return sb.toString();
    }   

    public static void setParameters(QueryStringField field,
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

    public static void setParameters(QueryNumberField field,
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

                if (field.getType().equals("integer")) {
                    query.setParameter(paramName + i + "0",
                                       new Integer(param1.trim()));
                    query.setParameter(paramName + i + "1",
                                       new Integer(param2.trim()));
                } else {
                    query.setParameter(paramName + i + "0",
                                       new Double(param1.trim()));
                    query.setParameter(paramName + i + "1",
                                       new Double(param2.trim()));
                }
            } else if (param.indexOf(",") > -1) {
                String[] params = param.split(",");
                for (int j = 0; j < params.length; j++) {
                    if (field.getType().equals("integer"))
                        query.setParameter(paramName + i + j,
                                           new Integer(params[j].trim()));
                    else
                        query.setParameter(paramName + i + j,
                                           new Double(params[j].trim()));
                }
            } else {
                if (field.getType().equals("integer"))
                    query.setParameter(paramName + i, new Integer(param.trim()));
                else
                    query.setParameter(paramName + i, new Double(param.trim()));
            }
            i++;
        }
    }

    public static void setParameters(QueryDateField field,
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

    //TODO get rid of soon
    public static void setParameters(QueryOptionField field,
                                     String fieldName,
                                     Query query) {
        try {
        String paramName = getParamName(fieldName);
        Iterator paramsIt = field.getSelections().iterator();
        int i = 0;
        while (paramsIt.hasNext()) {
            OptionItem param = (OptionItem)paramsIt.next();
            if (field.getType().equals("string") || field.getType().equals("alpha"))
                query.setParameter(paramName + i, param.akey);
            else if (field.getType().equals("integer"))
                query.setParameter(paramName + i,
                                   new Integer(param.akey.trim()));
            else if (field.getType().equals("double"))
                query.setParameter(paramName + i, new Double(param.akey.trim()));
            i++;
        }
        }catch(Exception e){
            System.out.println("field: "+fieldName);
            e.printStackTrace();
        }
    }
    
    public static void setParameters(CollectionField field,
            						 String fieldName,
            						 Query query) {
    	ArrayList list = (ArrayList) field.getValue();
		String paramName = getParamName(fieldName);
		for(int i = 0;i<list.size();i++){
			if (field.getType().equals("string")){
				String param = (String)list.get(i); 
				query.setParameter(paramName + i, param.trim());
			}else if (field.getType().equals("integer")){
				Integer param = (Integer)list.get(i);
				query.setParameter(paramName + i,
									new Integer(param));
			}
		}	
    }
    
    public static void setParameters(DropDownField field, String fieldName, Query query) {
    	ArrayList list = (ArrayList) field.getSelections();
    	String paramName = getParamName(fieldName);
    
    	for(int i = 0;i<list.size();i++){
    		Object o = ((DataSet)list.get(i)).getKey();
    		if(o instanceof NumberObject){
				NumberObject number = (NumberObject)o;
				if(number.getType() == NumberObject.INTEGER){
					Integer param = (Integer)number.getValue();
					query.setParameter(paramName + i, param);	
				}else if(number.getType() == NumberObject.DOUBLE){
					Double param = (Double)number.getValue();
					query.setParameter(paramName + i, param);	
				}
			}else if(o instanceof StringObject){
				String param = (String)((StringObject)o).getValue(); 
				query.setParameter(paramName + i, param.trim());
			}
    	}	
    }
    
    public static void setParameters(QueryCheckField field,
                                     String fieldName,
                                     Query query) {
        
        if(field.getValue() == null)
            return;
        
        String paramName = getParamName(fieldName);
       
        query.setParameter(paramName, (String)field.getValue());
    }
    
    private static String getParamName(String name){
        while(name.indexOf(".") > -1){
            name = name.substring(0,name.indexOf(".")) + name.substring(name.indexOf(".")+1,name.length());
        }
        return name;
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
    
    /**
     * Creates the where statement from a hashmap of Query fields.  This method will throw an exception
     * if the column value doesnt match the database.
     * @param fields
     * @throws Exception
     */
    public void addWhere(HashMap fields) throws Exception{
    	fieldsFromRPC = fields;
//    	where clause
    	Object[] keys = (Object[]) ((Set)fields.keySet()).toArray();	
    	for (int i = 0; i < keys.length; i++) {
        	Object o = fields.get((String)keys[i]); 

        	if(fields.containsKey((String)keys[i])){
        		String key = (String)keys[i];

                //iterate through all the metas to validate
                boolean columnFound = false;
                Meta meta = null;
                int j=0;
                while(j < metaList.size() && !columnFound) {
                	meta = (Meta) metaList.get(j);
                	if(meta.hasColumn(key))
                		columnFound = true;
                	j++;
                }

                
                if(!columnFound)
                	throw new Exception("column not found [" + key + "]");    	

//        		set the where param            	            	
            	String whereClause = "";
            	if(o != null){
					if(o instanceof QueryNumberField)
						whereClause = getQueryNoOperand((QueryNumberField)o, key);
					else if(o instanceof QueryStringField)
						whereClause = getQueryNoOperand((QueryStringField)o, key);
					else if(o instanceof DropDownField)
						whereClause = getQueryNoOperand((DropDownField)o, key);	
                    else if(o instanceof QueryCheckField)
                        whereClause = getQueryNoOperand((QueryCheckField)o, key);
            	}
        	
				if(!"".equals(whereClause)){
					whereOperands.add(whereClause);
			
					//add the table name to the from hash map
					addTable(meta);
				}
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
    
    public String[] getWhere(){
    	return null;
    	//TODO do this
    }
    
    /**
     * Sets the values of the parameters in the query and then returns it.
     * @param query
     * @return
     */
    public void setQueryParams(Query query){
    	Object[] keys = (Object[]) ((Set)fieldsFromRPC.keySet()).toArray();
        for (int i = 0; i < keys.length; i++) {
        	Object o = fieldsFromRPC.get((String)keys[i]);    
        	if(fieldsFromRPC.containsKey((String)keys[i])){
				if(o instanceof QueryNumberField) 
					setParameters((QueryNumberField)o, (String)keys[i], query);
				else if(o instanceof QueryStringField) 	
					setParameters((QueryStringField)o, (String)keys[i], query);
				else if(o instanceof DropDownField) 
					setParameters((DropDownField)o, (String)keys[i], query);
                else if(o instanceof QueryCheckField)
                    setParameters((QueryCheckField)o, (String)keys[i], query);
        	}
        }
    }
    
    /**
     * This method adds a table to the from statement in the query.
     * @param meta
     */
    public void addTable(Meta meta){
    	boolean addTableToOrderedArray = false;
    	addTableToOrderedArray = !fromTables.containsKey(meta.getTable());
    	
    	fromTables.put(meta.getTable(), meta);
    	
    	if(addTableToOrderedArray)
    		orderedFromTableKeys.add(meta.getTable());
    }
    
    public String[] getTables(){
    	return null;
    	//TODO do this
    }
    
    /**
     * Takes the different pieces of the query and put thems together.  It will return the query as a string.
     * @return
     */
    public String getEJBQL(){
    	StringBuffer query = new StringBuffer();
    	query.append("SELECT ").append(selectStatement).append(" FROM ");
    	
    	//from tables
    	int fromTablesCount = 0;

        for (int i = 0; i < orderedFromTableKeys.size(); i++) {
        	String tableName = (String)orderedFromTableKeys.get(i);
    		Meta addTableMeta = fromTables.get(tableName); 
    		
    		if(addTableMeta.includeInFrom()){
	    		
    			//we use left join on all the tables so we dont care about null values.
    			//This will also let us use a collection in the query without the IN keyword
    			if(fromTablesCount>0)
	    			query.append(" LEFT JOIN ").append(addTableMeta.getEntity()).append(' ').append(addTableMeta.getTable());
	    		else
	    			query.append(' ').append(addTableMeta.getEntity()).append(' ').append(addTableMeta.getTable());
	    		
	    		fromTablesCount++;
    		}
    	}
        
        //where clause
        if(whereOperands.size() > 0){
        	query.append(" WHERE ");
        	for(int j=0; j < whereOperands.size(); j++)
        		if(j>0)
        			query.append(" and (").append(whereOperands.get(j)).append(") ");
        		else
        			query.append("(").append(whereOperands.get(j)).append(") ");
        }
        
        //order by
        if(!"".equals(orderByStatement))
        	query.append(" ORDER BY ").append(orderByStatement);
        
    	return query.toString();
    }
    
    public void setOrderBy(String orderBy){
    	this.orderByStatement = orderBy;
    }
    
    public String getOrderBy(){
    	return orderByStatement;
    }

    /**
     * Adds a single meta class to the builder.
     * @param meta
     */
    public void addMeta(Meta meta){
    	metaList.add(meta);
    }
    
    /**
     * Adds multple meta classes to the builder from an array.
     * @param metas
     */
    public void addMeta(Meta[] metas){
    	for(int i=0; i<metas.length; i++)
    		metaList.add(metas[i]);    	
    }
    
    /**
     * Checks to see if the table exists in the from statement.  It will return true if it does.
     * @param tableName
     * @return
     */
    public boolean hasTable(String tableName){
    	return fromTables.containsKey(tableName);
    }
}
