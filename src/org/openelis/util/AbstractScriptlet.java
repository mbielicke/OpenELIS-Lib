package org.openelis.util;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.HashMap;

public class AbstractScriptlet {
    
    protected Context ctx;
    protected Scriptable scope;
    
    protected AbstractScriptlet() throws Exception{
        ctx = Context.enter();
        scope = ctx.initStandardObjects();
    }
    
    protected AbstractScriptlet(HashMap<String,Object> objs) throws Exception{
        this();
        setClasses(objs);
    }
    
    protected AbstractScriptlet(HashMap<String,Object> objs, String script) throws Exception {
        this(objs);
        setScript(script);
    }
    
    protected AbstractScriptlet(String script) throws Exception {
        this();
        setScript(script);
    }
    
    protected void setClasses(HashMap<String,Object> objs) throws Exception {
       for(String key : objs.keySet()) 
           ScriptableObject.putProperty(scope,key,ctx.javaToJS(objs.get(key),scope));
    }
    
    protected void setScript(String script) throws Exception {
        ctx.evaluateString(scope,script,null,-1,null);
    }
    
    protected Object call(String method,Object[] args) throws Exception {
        return ((Function)scope.get(method, scope)).call(ctx,scope,scope,args);
    }

}
