package org.openelis.gwt.widget;

import com.google.gwt.core.client.JavaScriptObject;

import org.openelis.gwt.common.Form;

public class Scriptlet extends JavaScriptObject {
    
    protected Scriptlet() {}
    
    public native final void execute(Form form) /*-{
        for(i in this.fields){
            this.fields[i] = form.@org.openelis.gwt.common.Form::getFieldValue(Ljava/lang/String;)(i);
        }
        this.run();
        for(i in this.fields) {
           form.@org.openelis.gwt.common.Form::setFieldValue(Ljava/lang/String;Ljava/lang/Object;)(i,this.fields[i]);
        }
    }-*/;
    
    public native final Object getValue(String param) /*-{
        return rpc.@org.openelis.gwt.common.Form::getFieldValue(Ljava/lang/String;)(param);
    }-*/;
    
    public native final void execute(ScriptRPC rpc) /*-{
        this.rpc = rpc;
        this.run();
    }-*/;
    
    public native static final Scriptlet eval(String script) /*-{
        return eval(script);
    }-*/;

}
