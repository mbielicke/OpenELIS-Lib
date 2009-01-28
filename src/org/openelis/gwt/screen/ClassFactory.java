/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.screen;

import java.util.HashMap;

public class ClassFactory {
    
    public static interface Factory {
        public Object newInstance(Object[] args);
    }
    
    private static HashMap<String,Factory> classFactories = new HashMap<String,Factory>();
    
    private static HashMap<String,Class> classes = new HashMap<String,Class>();

    public static void addClassFactory(String[] classKeys, Factory factory){
        for(int i = 0; i < classKeys.length;  i++){
            classFactories.put(classKeys[i], factory);
        }
    }
    
    public static void addClass(String[] classKeys, Class classs){
        for(int i = 0; i < classKeys.length;  i++){
            classes.put(classKeys[i], classs);
        }
    }

    public static Object forName(String name, Object[] args) {
        Factory factory = (Factory)classFactories.get(name);

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
    
    public static Class getClass(String name) {
        return classes.get(name);
    }
    
    public static Enum getEnum(String name) {
        String classs = name.substring(0,name.lastIndexOf("."));
        String enumValue = name.substring(name.lastIndexOf(".")+1);
        return Enum.valueOf(classes.get(classs), enumValue);
    }
    
}
