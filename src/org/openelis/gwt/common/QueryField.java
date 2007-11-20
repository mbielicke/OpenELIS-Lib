package org.openelis.gwt.common;

import java.util.ArrayList;

public class QueryField extends AbstractField {

    /**
     * @gwt.typeArgs <java.lang.String>
     */
    protected ArrayList comparator;
    protected String value = "";
    /**
     * @gwt.typeArgs <java.lang.String>
     */
    protected ArrayList parameter;
    /**
     * @gwt.typeArgs <java.lang.String>
     */
    protected ArrayList logical;

    /**
     * @gwt.typeArgs <java.lang.String>
     */
    private ArrayList operators = new ArrayList();
    {
        operators.add("!");
        operators.add("~");
        operators.add("=");
        operators.add("(");
        operators.add(">");
        operators.add("<");
        operators.add("&");
        operators.add("|");
    }

    public boolean isValid() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        this.value = (String)val;
        parse((String)val);
    }
    
    public void parse(String value) {
        comparator = new ArrayList();
        parameter = new ArrayList();
        logical = new ArrayList();
        while (value != null && value.length() > 0) {
            String pos = value.substring(0, 1);
            String comp = "";
            while (operators.contains(pos)) {
                comp += pos;
                value = value.substring(1);
                pos = value.substring(0, 1);
            }
            if (comp.equals("")) {
                comp = "=";
            }
            comparator.add(comp);
            String param = "";
            while (!operators.contains(pos) && value.length() > 0) {
                if (!pos.equals(")"))
                    param += pos;
                value = value.substring(1);
                if (value.length() > 0)
                    pos = value.substring(0, 1);
            }
            if (param.indexOf("..") > -1) {
                comp = "between ";
                comparator.set(comparator.size() - 1, comp);
            }
            if (param.indexOf("*") > -1 || param.indexOf("?") > -1) {
                comp = "like ";
                comparator.set(comparator.size() - 1, comp);
                param = param.replace('*', '%');
                int index = 0;
                while(param.indexOf("_",index) > -1){
                    index = param.indexOf("_");
                    
                    param = param.substring(0,index) + "\\" + param.substring(index+1);
                    index++;
                }
                //param = param.replace("_","\\_");
                param = param.replace('?', '_');
            }
            parameter.add(param);
            if (value.length() > 0) {
                if (pos.equals("|"))
                    logical.add("|");
                else
                    logical.add("&");
                value = value.substring(1);
                while (value.charAt(0) == ' ')
                    value = value.substring(1);
            }
        }
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        if (value == null)
            return "";
        return value;
    }

    public ArrayList getComparator() {
        return comparator;
    }

    public ArrayList getParameter() {
        return parameter;
    }

    public ArrayList getLogical() {
        return logical;
    }

    public Object getInstance() {
        // TODO Auto-generated method stub
        QueryField field = new QueryField();
        field.setRequired(required);
        field.setValue(value);
        return field;
    }

    public String toString() {
        if (value == null)
            return "";
        else
            return value;
    }

}
