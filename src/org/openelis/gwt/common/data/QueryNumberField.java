package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

public class QueryNumberField extends QueryField {

    private static final long serialVersionUID = 1L;
    private String type;
    private Double max;
    private Double min;
    public static final String TAG_NAME = "rpc-queryNumber";
    
    public QueryNumberField() {
        
    }
    
    public QueryNumberField(Node node){
        setKey(node.getAttributes().getNamedItem("key").getNodeValue());
        setType(node.getAttributes()
                            .getNamedItem("type")
                            .getNodeValue());
        if (node.getAttributes().getNamedItem("max") != null)
            setMax(new Double(node.getAttributes()
                                          .getNamedItem("max")
                                          .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            setMin(new Double(node.getAttributes()
                                          .getNamedItem("min")
                                          .getNodeValue()));
    }

    public void validate() {
        for(String param : parameter){
            if (type.equals("integer")) {
                try {
                    Integer.parseInt(param);
                } catch (Exception e) {
                    addError("Param is not a number");
                    valid = false;
                    return;
                }
            } else {
                try {
                    Double.parseDouble(param);
                } catch (Exception e) {
                    addError("Param is not a number");
                    valid = false;
                    return;
                }
            }
        }
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        if (value == null)
            return true;
        for (String param : parameter) {
            double pVal = Double.parseDouble(param);
            if (max != null && pVal > max.doubleValue()) {
                addError("Field exceeded maximum value");
                return false;
            }
            if (min != null && pVal < min.doubleValue()) {
                addError("Field is below minimum value");
                return false;
            }
        }
        return true;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setMin(Object min) {
        this.min = (Double)min;
    }

    public void setMax(Object max) {
        this.max = (Double)max;
    }

    public QueryNumberField getInstance() {
        QueryNumberField obj = new QueryNumberField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setType(type);
        obj.setRequired(required);
        obj.setValue(value);
        return obj;
    }

    public QueryNumberField getInstance(Node node) {
        return new QueryNumberField(node);
    }
}
