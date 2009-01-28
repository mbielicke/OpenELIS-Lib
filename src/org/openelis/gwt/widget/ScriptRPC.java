package org.openelis.gwt.widget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;

import org.openelis.gwt.common.Form;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.NumberField;

public class ScriptRPC extends JavaScriptObject {
    
    protected ScriptRPC() {}
    
    public final static ScriptRPC create(Form form) {
        StringBuffer sb = new StringBuffer();
        sb.append("({");
        for(AbstractField field : form.getFieldMap().values()){
            if(sb.length() > 2){
                sb.append(",");
            }
            sb.append("'"+field.key+"' : ");
            if(!(field instanceof NumberField)) 
                sb.append("'");
            sb.append(field.getValue());
            if(!(field instanceof NumberField))
                sb.append("'");
        }
        sb.append("})");
        Window.alert(sb.toString());
        return eval(sb.toString());
    }
    
    public final static ScriptRPC createArray(Form form) {
        StringBuffer sb = new StringBuffer();
        sb.append("( new function() {this.fields = new Array();");
        int i = 0; 
        for(AbstractField field : form.getFieldMap().values()){ 
            sb.append("this.fields[\""+field.key+"\"] = ");
            if(!(field instanceof NumberField)) 
                sb.append("'");
            sb.append(field.getValue());
            if(!(field instanceof NumberField))
                sb.append("'");
            sb.append(";");
        }
        sb.append("})");
        Window.alert(sb.toString());
        return eval(sb.toString());
    }
    
    private final static native ScriptRPC eval(String form) /*-{
        return eval(form);
    }-*/;
    
    public final native Object getFieldValue(String key) /*-{
        return this.fields[key];
    }-*/;
    
    public final native void setFieldValue(String key, Object value) /*-{
        this.fields[key] = value;
    }-*/;
    
    public final native double getNumberValue(String key) /*-{
        return this.fields[key];
    }-*/;
    
    public final void pullOutForm(Form form) {
        for(String key : form.getFieldMap().keySet()){
            if(form.getField(key) instanceof NumberField) 
                form.setFieldValue(key, getNumberValue(key));
            else
                form.setFieldValue(key, getFieldValue(key));
        }
    }
}
