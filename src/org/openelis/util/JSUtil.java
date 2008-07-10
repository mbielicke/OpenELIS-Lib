/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
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
