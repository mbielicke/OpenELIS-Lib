package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

import java.util.Iterator;

public class QueryNumberField extends QueryField {

    private static final long serialVersionUID = 1L;
    private String type;
    private Double max;
    private Double min;

    public void validate() {
        Iterator paramIt = parameter.iterator();
        while (paramIt.hasNext()) {
            String param = (String)paramIt.next();
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
        Iterator paramIt = parameter.iterator();
        while (paramIt.hasNext()) {
            String param = (String)paramIt.next();
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

    public Object getInstance() {
        QueryNumberField obj = new QueryNumberField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setType(type);
        obj.setRequired(required);
        obj.setValue(value);
        return obj;
    }

    public Object getInstance(Node field) {
        QueryNumberField number = new QueryNumberField();
        number.setKey(field.getAttributes().getNamedItem("key").getNodeValue());
        number.setType(field.getAttributes()
                            .getNamedItem("type")
                            .getNodeValue());
        if (field.getAttributes().getNamedItem("max") != null)
            number.setMax(new Double(field.getAttributes()
                                          .getNamedItem("max")
                                          .getNodeValue()));
        if (field.getAttributes().getNamedItem("min") != null)
            number.setMin(new Double(field.getAttributes()
                                          .getNamedItem("min")
                                          .getNodeValue()));
        return number;
    }
}
