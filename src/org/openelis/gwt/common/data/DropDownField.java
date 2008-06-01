package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

import java.util.ArrayList;

public class DropDownField extends AbstractField {

    private static final long serialVersionUID = 1L;
    
    private ArrayList<DataSet> selections = new ArrayList<DataSet>();
    
    public static final String TAG_NAME = "rpc-dropdown";
    
    public DropDownField() {
        
    }
    
    public DropDownField(Node node) {
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
        if (node.getFirstChild() != null) {
            String dflt = node.getFirstChild().getNodeValue();
            setValue(dflt);
        }
    }
    
    public void setValue(Object val) {
        if(val instanceof ArrayList){
            selections = (ArrayList)val;
            return;
        }
        selections = new ArrayList<DataSet>();
        if(val instanceof Integer)
            add((Integer)val);
        else if(val instanceof Double)
            add((Double)val);
        else if(val instanceof String)
            add((String)val);
        else if(val instanceof DataSet)
            add((DataSet)val);
    }

    public Object getValue() {
        if(selections.size() == 1)
            return ((DataSet)selections.get(0)).getKey().getValue();
        else if(selections.size() > 1)
            return selections;
        else
            return null;       
    }
    
    public Object getTextValue(){
    	if(selections.size() == 1)
            return ((DataSet)selections.get(0)).getObject(0).getValue();
        else if(selections.size() > 1)
            return selections;
        else
            return null;
    }
    
    public ArrayList getSelections() {
        return selections;
    }

    public void add(Integer key) {
        DataSet set = new DataSet();
        NumberObject no = new NumberObject(NumberObject.INTEGER);
        no.setValue(key);
        set.setKey(no);
        add(set);
    }
    
    public void add(Double key){
        DataSet set = new DataSet();
        NumberObject no = new NumberObject(NumberObject.DOUBLE);
        no.setValue(key);
        set.setKey(no);
        add(set);
    }
    
    public void add(String key){
        DataSet set = new DataSet();
        StringObject so = new StringObject();
        so.setValue(key);
        set.setKey(so);
        add(set);
    }
    
    public void add(DataSet set) {
        selections.add(set);
    }
    
    public void remove(DataSet set){
        selections.remove(set);
    }
    
    public void clear() {
        selections = new ArrayList<DataSet>();
    } 
    
    public Object getInstance() {
        DropDownField obj = new DropDownField();
        obj.setRequired(required);
        obj.setValue(selections);
        return obj;
    }
    
    public Object getInstance(Node node){
        return new DropDownField(node);
    }
    
    public void validate() {
    	if (required) {
            //if there are no selections or there is one selection but it is "" then it is empty and we need to throw an error
            if (selections.size() == 0 || (selections.size() == 1 && ((StringObject)selections.get(0).getObject(0)).getValue().equals(""))) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        valid = true;
    }
}
