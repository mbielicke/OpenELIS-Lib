package org.openelis.util;

import org.apache.bsf.BSFManager;

public class ScriptletUtil {

    public static boolean validateScript(String script) throws Exception {
        BSFManager manager = new BSFManager();
        return true;
    }
    
    public static void setScriptObjects(BSFManager manager, String script) throws Exception {
        if(script.indexOf("Objects[") > -1){
            int start = script.indexOf("Objects[")+8;
            String objects = script.substring(start,script.indexOf("]",start+1));
            String[] classes = objects.split(";");
            for(String instance : classes) {
                String[] pair = instance.split(":");
                manager.registerBean(pair[0], Class.forName(pair[1]).newInstance());
            }
        }
    }
}
