package org.openelis.util;

import org.openelis.gwt.common.DatetimeRPC;
import org.openelis.gwt.common.Form;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.CheckField;
import org.openelis.gwt.common.data.DateField;
import org.openelis.gwt.common.data.DoubleField;
import org.openelis.gwt.common.data.DropDownField;
import org.openelis.gwt.common.data.IntegerField;
import org.openelis.gwt.common.data.StringField;
import org.openelis.gwt.common.data.TableField;
import org.openelis.gwt.common.data.TreeField;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Date;

public class FormInitUtil {
    
    public static void setForm(Form form, Node node){
        NodeList fieldList = node.getChildNodes();
        for (int j = 0; j < fieldList.getLength(); j++) {
            if (fieldList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                String key = fieldList.item(j).getAttributes().getNamedItem("key").getNodeValue();
                if(form.fields.containsKey(key)){
                    AbstractField field = form.fields.get(key);
                    if(field instanceof StringField)
                        FormInitUtil.setAttributes(fieldList.item(j),(StringField)field);
                    else if(field instanceof DateField)
                        FormInitUtil.setAttributes(fieldList.item(j),(DateField)field);
                    else if(field instanceof CheckField)
                        FormInitUtil.setAttributes(fieldList.item(j),(CheckField)field);
                    else if(field instanceof DropDownField)
                        FormInitUtil.setAttributes(fieldList.item(j),(DropDownField)field);
                    else if(field instanceof DoubleField)
                        FormInitUtil.setAttributes(fieldList.item(j),(DoubleField)field);
                    else if(field instanceof IntegerField)
                        FormInitUtil.setAttributes(fieldList.item(j),(IntegerField)field);
                    else if(field instanceof TableField)
                        FormInitUtil.setAttributes(fieldList.item(j),(TableField)field);
                    else if(field instanceof TreeField)
                        FormInitUtil.setAttributes(fieldList.item(j),(TreeField)field);
                }
            }
        }
        form.key = node.getAttributes().getNamedItem("key").getNodeValue();
        if(node.getAttributes().getNamedItem("load") != null){
            if(node.getAttributes().getNamedItem("load").getNodeValue().equals("true"))
                form.load = true;
        }
    }
    
    public static void setAttributes(Node node, StringField field) {
        if (node.getAttributes().getNamedItem("key") != null)
            field.setKey(node.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("max") != null)
            field.setMax(new Integer(node.getAttributes()
                                           .getNamedItem("max")
                                           .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            field.setMin(new Integer(node.getAttributes()
                                           .getNamedItem("min")
                                           .getNodeValue()));
        if (node.getAttributes().getNamedItem("required") != null)
            field.setRequired(new Boolean(node.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
        if (node.getAttributes().getNamedItem("reset") != null)
            field.setAllowReset(new Boolean(node.getAttributes()
                                                .getNamedItem("reset")
                                                .getNodeValue()).booleanValue());
        
        if (node.hasChildNodes()) {
            field.setValue(node.getFirstChild().getNodeValue());
        }
    }
    
    public static void setAttributes(Node node, CheckField field){        
        if (node.getAttributes().getNamedItem("key") != null)
            field.setKey(node.getAttributes()
                             .getNamedItem("key")
                             .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            field.setRequired(new Boolean(node.getAttributes()
                                              .getNamedItem("required")
                                              .getNodeValue()).booleanValue());
        if (node.hasChildNodes()) {
            field.setValue(node.getFirstChild().getNodeValue());
        }
    }
    
    public static void setAttributes(Node node, DateField field) {
        if (node.getAttributes().getNamedItem("key") != null)
            field.setKey(node.getAttributes()
                             .getNamedItem("key")
                             .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            field.setRequired(new Boolean(node.getAttributes()
                                              .getNamedItem("required")
                                              .getNodeValue()).booleanValue());
        if (node.getAttributes().getNamedItem("begin") != null)
            field.setBegin(Byte.parseByte(node.getAttributes()
                                              .getNamedItem("begin")
                                              .getNodeValue()));
        if (node.getAttributes().getNamedItem("end") != null)
            field.setEnd(Byte.parseByte(node.getAttributes()
                                            .getNamedItem("end")
                                            .getNodeValue()));
        if (node.getAttributes().getNamedItem("max") != null)
            field.setMax(new Integer(node.getAttributes()
                                         .getNamedItem("max")
                                         .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            field.setMin(new Integer(node.getAttributes()
                                         .getNamedItem("min")
                                         .getNodeValue()));
        if (node.hasChildNodes()) {
            String def = node.getFirstChild().getNodeValue();
            Date dat = null;
            if (def.equals("current"))
                dat = new Date();
            else
                dat = new Date(def);
            field.setValue(DatetimeRPC.getInstance(field.getBegin(),
                                                  field.getEnd(),
                                                  dat));
        }
        if(node.getAttributes().getNamedItem("pattern") != null){
            field.setFormat(node.getAttributes().getNamedItem("pattern").getNodeValue());
        }   
    }
    
    public static void setAttributes(Node node, DoubleField field) {
        if (node.getAttributes().getNamedItem("key") != null)
            field.setKey(node.getAttributes().getNamedItem("key").getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            field.setRequired(new Boolean(node.getAttributes()
                                        .getNamedItem("required")
                                        .getNodeValue()).booleanValue());
        if (node.getAttributes().getNamedItem("max") != null)
            field.setMax(new Double(node.getAttributes()
                                  .getNamedItem("max")
                                  .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            field.setMin(new Double(node.getAttributes()
                                  .getNamedItem("min")
                                  .getNodeValue()));
        if (node.getAttributes().getNamedItem("reset") != null)
            field.setAllowReset(new Boolean(node.getAttributes()
                                          .getNamedItem("reset")
                                          .getNodeValue()).booleanValue());
        if (node.hasChildNodes()) {
            field.setValue(new Integer(node.getFirstChild().getNodeValue()));
        }
        if (node.getAttributes().getNamedItem("pattern") != null) {
            field.setFormat(node.getAttributes()
                          .getNamedItem("pattern")
                          .getNodeValue());
        }
    }
    
    public static void setAttributes(Node node, DropDownField field) {
        if (node.getAttributes().getNamedItem("key") != null)
            field.setKey(node.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            field.setRequired(new Boolean(node.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
    }
    
    public static void setAttributes(Node node, IntegerField field) {
        if (node.getAttributes().getNamedItem("key") != null)
            field.setKey(node.getAttributes().getNamedItem("key").getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            field.setRequired(new Boolean(node.getAttributes()
                                        .getNamedItem("required")
                                        .getNodeValue()).booleanValue());
        if (node.getAttributes().getNamedItem("max") != null)
            field.setMax(new Integer(node.getAttributes()
                                   .getNamedItem("max")
                                   .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            field.setMin(new Integer(node.getAttributes()
                                   .getNamedItem("min")
                                   .getNodeValue()));
        if (node.getAttributes().getNamedItem("reset") != null)
            field.setAllowReset(new Boolean(node.getAttributes()
                                          .getNamedItem("reset")
                                          .getNodeValue()).booleanValue());
        if (node.hasChildNodes()) {
            field.setValue(new Integer(node.getFirstChild().getNodeValue()));
        }
        if (node.getAttributes().getNamedItem("pattern") != null) {
            field.setFormat(node.getAttributes()
                          .getNamedItem("pattern")
                          .getNodeValue());
        }
    }
    
    public static void setAttributes(Node node, TableField field) {
        field.setKey(node.getAttributes().getNamedItem("key").getNodeValue());
    }
    
    public static void setAttributes(Node node, TreeField field) {
        field.setKey(node.getAttributes().getNamedItem("key").getNodeValue());
    }

}
