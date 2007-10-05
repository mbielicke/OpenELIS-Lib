package org.openelis.util;

import java.util.Date;
import java.util.Iterator;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.openelis.gwt.common.OptionItem;
import org.openelis.gwt.common.QueryCheckField;
import org.openelis.gwt.common.QueryDateField;
import org.openelis.gwt.common.QueryField;
import org.openelis.gwt.common.QueryNumberField;
import org.openelis.gwt.common.QueryOptionField;
import org.openelis.gwt.common.QueryStringField;

public class QueryBuilder {

    public static String getQuery(QueryField field, String fieldName) {
        if (field.getParameter().size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        Iterator fieldCompIt = field.getComparator().iterator();
        Iterator fieldParamIt = field.getParameter().iterator();
        Iterator fieldLogicalIt = field.getLogical().iterator();
        int i = 0;
        sb.append(" and (");
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
                    sb.append(":" + fieldName + i + j);
                }
                sb.append(") ");
            } else if (comp.startsWith("between")) {
                sb.append("between :" + fieldName
                          + i
                          + "0 and :"
                          + fieldName
                          + i
                          + "1 ");
            } else
                sb.append(comp + " :" + fieldName + i + " ");
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
        sb.append(")");
        return sb.toString();
    }

    public static String getQuery(QueryOptionField field, String fieldName) {
        if (field.getSelections().size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        sb.append(" and (" + fieldName + " in (");
        for (int i = 0; i < field.getSelections().size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(":" + fieldName + i);
        }
        sb.append(")) ");
        return sb.toString();
    }
    
    public static String getQuery(QueryCheckField field, String fieldName) {
        StringBuffer sb = new StringBuffer();
        sb.append(" and (" + fieldName + " =  :" + fieldName);
        sb.append(") ");
        return sb.toString();
    }    

    public static void setParameters(QueryStringField field,
                                     String fieldName,
                                     Query query) {
        System.out.println("In QUERY STRING FIELD");
        Iterator fieldParamIt = field.getParameter().iterator();
        int i = 0;
        while (fieldParamIt.hasNext()) {
            String param = (String)fieldParamIt.next();
            if (param.indexOf("..") > -1) {
                String[] bparams = param.split("..");
                query.setParameter(fieldName + i + "0", bparams[0]);
                query.setParameter(fieldName + i + "1", bparams[1]);
            } else if (param.indexOf(",") > -1) {
                String[] params = param.split(",");
                for (int j = 0; j < params.length; j++) {
                    query.setParameter(fieldName + i + j, params[j]);
                }
            } else
                query.setParameter(fieldName + i, param);
            i++;
        }
    }

    public static void setParameters(QueryNumberField field,
                                     String fieldName,
                                     Query query) {
        System.out.println("IN QUERY NUMBER FIELD");
        Iterator fieldParamIt = field.getParameter().iterator();
        int i = 0;
        while (fieldParamIt.hasNext()) {
            String param = (String)fieldParamIt.next();
            System.out.println("param = " + param);
            if (param.indexOf("..") > -1) {
                String param1 = param.substring(0, param.indexOf(".."));
                String param2 = param.substring(param.indexOf("..") + 2,
                                                param.length());
                System.out.println("0 : " + param1);
                System.out.println("1 : " + param2);
                if (field.getType().equals("integer")) {
                    query.setParameter(fieldName + i + "0",
                                       new Integer(param1.trim()));
                    query.setParameter(fieldName + i + "1",
                                       new Integer(param2.trim()));
                } else {
                    query.setParameter(fieldName + i + "0",
                                       new Double(param1.trim()));
                    query.setParameter(fieldName + i + "1",
                                       new Double(param2.trim()));
                }
            } else if (param.indexOf(",") > -1) {
                String[] params = param.split(",");
                for (int j = 0; j < params.length; j++) {
                    if (field.getType().equals("integer"))
                        query.setParameter(fieldName + i + j,
                                           new Integer(params[j].trim()));
                    else
                        query.setParameter(fieldName + i + j,
                                           new Double(params[j].trim()));
                }
            } else {
                if (field.getType().equals("integer"))
                    query.setParameter(fieldName + i, new Integer(param.trim()));
                else
                    query.setParameter(fieldName + i, new Double(param.trim()));
            }
            i++;
        }
    }

    public static void setParameters(QueryDateField field,
                                     String fieldName,
                                     Query query) {
        Iterator fieldParamIt = field.getParameter().iterator();
        int i = 0;
        while (fieldParamIt.hasNext()) {
            String param = (String)fieldParamIt.next();
            if (param.indexOf("..") > -1) {
                String[] bparams = param.split("..");
                Date date = new Date(bparams[0]);
                query.setParameter(fieldName + i + "0", date, TemporalType.DATE);
                date = new Date(bparams[1]);
                query.setParameter(fieldName + i + "1", date, TemporalType.DATE);
            } else if (param.indexOf(",") > -1) {
                String[] params = param.split(",");
                for (int j = 0; j < params.length; j++) {
                    Date date = new Date(params[j]);
                    query.setParameter(fieldName + i + j,
                                       date,
                                       TemporalType.DATE);
                }
            } else {
                Date date = new Date(param);
                query.setParameter(fieldName + i, date, TemporalType.DATE);
            }
            i++;
        }
    }

    public static void setParameters(QueryOptionField field,
                                     String fieldName,
                                     Query query) {
        Iterator paramsIt = field.getSelections().iterator();
        int i = 0;
        while (paramsIt.hasNext()) {
            OptionItem param = (OptionItem)paramsIt.next();
            if (field.getType().equals("string"))
                query.setParameter(fieldName + i, param.akey);
            else if (field.getType().equals("integer"))
                query.setParameter(fieldName + i,
                                   new Integer(param.akey.trim()));
            else if (field.getType().equals("double"))
                query.setParameter(fieldName + i, new Double(param.akey.trim()));
            i++;
        }
    }
    
    public static void setParameters(QueryCheckField field,
                                     String fieldName,
                                     Query query) {
        String param = "N";
        if(((Boolean)field.getValue()).booleanValue())
            param = "Y";
        query.setParameter(fieldName, param);
    }

}
