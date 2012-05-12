package org.openelis.gwt.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.FIELD})
public @interface UIWidget {
    String value();
    
}
