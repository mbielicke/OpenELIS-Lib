package org.openelis.gwt.widget;

import com.google.gwt.core.client.JavaScriptObject;

public class Scriptlet<T extends Scriptlet> extends JavaScriptObject {
    
    protected Scriptlet() {}
    
    public native static final <T extends Scriptlet> T eval(String script) /*-{
        return eval(script);
    }-*/;

}
