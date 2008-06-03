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
