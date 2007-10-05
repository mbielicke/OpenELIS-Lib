package org.openelis.util;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JSUtil {

    public Object executeScript(String script, Object[] args, Object[] params) {
        try {
            Context cx = Context.enter();
            Scriptable scope = cx.initStandardObjects();
            for (int i = 0; i < params.length; i++) {
                Object wrappedParam = Context.javaToJS(params[i], scope);
                ScriptableObject.putProperty(scope, "param" + i, wrappedParam);
            }
            cx.evaluateString(scope, script, "<cmd>", 1, null);
            Object executeObj = scope.get("execute", scope);
            Function execute = (Function)executeObj;
            return execute.call(cx, scope, scope, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
