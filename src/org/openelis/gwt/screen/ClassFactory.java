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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.screen;

import java.util.HashMap;

public class ClassFactory {
    
    public static interface Factory {
        public Object newInstance(Object[] args);
    }

    private static HashMap<String,Factory> classes = new HashMap<String,Factory>();

    public static void addClass(String[] classKeys, Factory factory){
        for(int i = 0; i < classKeys.length;  i++){
            classes.put(classKeys[i], factory);
        }
    }

    public static Object forName(String name, Object[] args) {
        Factory factory = (Factory)classes.get(name);

        if (factory == null) { 
            throw new IllegalArgumentException("Don't know how to create a " + name);
        }
        Object obj =  factory.newInstance(args);
        if(obj == null) {
            if(args != null){
                for(int i = 0; i < args.length; i++){
                    if(i == 0)
                        name += "(";
                    else if(i > 0)
                        name += ",";
                    name += args[i].getClass().getName();
                }
                name += ")";
            }
            throw new IllegalArgumentException("Don't know how to create a " +name);
        }
        return obj;
    } 
    
    public static Object forName(String name) {
        return forName(name,null);
    }

    
}
