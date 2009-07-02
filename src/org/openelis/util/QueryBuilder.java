/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.util;

import org.openelis.gwt.common.Meta;
import org.openelis.gwt.common.MetaMap;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DropDownField;
import org.openelis.gwt.common.data.QueryCheckField;
import org.openelis.gwt.common.data.QueryDateField;
import org.openelis.gwt.common.data.QueryDoubleField;
import org.openelis.gwt.common.data.QueryField;
import org.openelis.gwt.common.data.QueryIntegerField;
import org.openelis.gwt.common.data.QueryStringField;
import org.openelis.gwt.common.data.TableDataRow;
import org.openelis.gwt.common.rewrite.QueryData;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.persistence.Query;
import javax.persistence.TemporalType;

public class QueryBuilder {
	
	private String selectStatement = "";
    private HashMap<String, Meta> fromTables = new HashMap<String, Meta>();
	private ArrayList orderedFromTableKeys = new ArrayList();
	private ArrayList<AbstractField> fieldsFromRPC = new ArrayList<AbstractField>();
	private ArrayList whereOperands = new ArrayList();
	private String orderByStatement = "";
    private MetaMap meta;
	
    
    public void setMeta(MetaMap meta){
        this.meta = meta;
    }
    
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
    
    public static String getQuery(DropDownField field, String fieldName) {
    	//this should always be an arraylist of datasets
        ArrayList list = (ArrayList) field.getValue();
        if (list == null || list.size() == 0)
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
        ArrayList list = (ArrayList) field.getValue();
        if (list == null || list.size() == 0)
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


    
    public static void setParameters(QueryIntegerField field,
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
    
    public static void setParameters(QueryDoubleField field,
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
    
    public static void setParameters(DropDownField field, String fieldName, Query query) {
    	ArrayList list = (ArrayList) field.getValue();
        if(list == null)
            return;
    	String paramName = getParamName(fieldName);
    
    	for(int i = 0;i<list.size();i++){
    		Object o = ((TableDataRow)list.get(i)).key;
            query.setParameter(paramName+i, o);
            /*
    		if(o instanceof NumberObject){
				NumberObject number = (NumberObject)o;
				if(number.getType() == NumberObject.Type.INTEGER){
					Integer param = number.getIntegerValue();
					query.setParameter(paramName + i, param);	
				}else if(number.getType() == NumberObject.Type.DOUBLE){
					Double param = (Double)number.getValue();
					query.setParameter(paramName + i, param);	
				}
			}else if(o instanceof StringObject){
				String param = (String)((StringObject)o).getValue(); 
				query.setParameter(paramName + i, param.trim());
			}
            */
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
    public void addWhere(ArrayList<AbstractField> fields) throws Exception{
    	fieldsFromRPC = fields;
//    	where clause
    	for (AbstractField field : fields){
       		String key = (String)field.getKey();
            if(field.getValue() != null) {
                boolean columnFound = meta.hasColumn(key);

                if(!columnFound)
                    throw new Exception("column not found [" + key + "]");    	

//   		set the where param            	            	
                String whereClause = "";
                if(field instanceof QueryStringField)
                    whereClause = getQueryNoOperand((QueryStringField)field, key);
                else if(field instanceof DropDownField)
                    whereClause = getQueryNoOperand((DropDownField)field, key);	
                else if(field instanceof QueryCheckField)
                    whereClause = getQueryNoOperand((QueryCheckField)field, key);
                else if(field instanceof QueryDateField)
                    whereClause = getQueryNoOperand((QueryDateField)field, key);            
                else if(field instanceof QueryIntegerField)
                    whereClause = getQueryNoOperand((QueryIntegerField)field, key);      
                else if(field instanceof QueryDoubleField)
                    whereClause = getQueryNoOperand((QueryDoubleField)field, key);      
                if(!"".equals(whereClause)){
                    whereOperands.add(whereClause);
		
				//add the table name to the from hash map
				addTable(meta);
                }
            }
        }
    }
    
    public void addNewWhere(ArrayList<QueryData> fields) throws Exception{
    	//fieldsFromRPC = fields;
//    	where clause
    	for (QueryData field : fields){
            boolean columnFound = meta.hasColumn(field.key);

            if(!columnFound)
                throw new Exception("column not found [" + field.key + "]");    	
            
            QueryField qField = new QueryField();
            qField.parse(field.query);
            String whereClause = getQueryNoOperand(qField, field.key);
            if(!"".equals(whereClause)){
                whereOperands.add(whereClause);
		
				//add the table name to the from hash map
				addTable(meta);
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
    
    /**
     * Sets the values of the parameters in the query and then returns it.
     * @param query
     * @return
     */
    public void setQueryParams(Query query){
        for (AbstractField field : fieldsFromRPC) {//int i = 0; i < keys.length; i++) {
            if(field instanceof QueryIntegerField)
                setParameters((QueryIntegerField)field, field.key, query);
            else if(field instanceof QueryDoubleField)
                setParameters((QueryDoubleField)field, field.key, query);
			else if(field instanceof QueryStringField) 	
			    setParameters((QueryStringField)field, field.key, query);
			else if(field instanceof DropDownField) 
			    setParameters((DropDownField)field, field.key, query);
            else if(field instanceof QueryCheckField)
                setParameters((QueryCheckField)field, field.key, query);
            else if(field instanceof QueryDateField)
                setParameters((QueryDateField)field, field.key, query);
        }
    }
    
    public void setNewQueryParams(Query query, ArrayList<QueryData> fields){
        for (QueryData field : fields) {//int i = 0; i < keys.length; i++) {
        	QueryField qField = new QueryField();
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
    
    public static void setStringParameters(QueryField field,
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
    		System.out.println("#####"+paramName + " --- "+param);
    		i++;
    	}
    }



    public static void setIntegerParameters(QueryField field,
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

    public static void setDoubleParameters(QueryField field,
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

    public static void setDateParameters(QueryField field,
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
