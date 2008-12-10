package org.openelis.persistence;

import java.io.Serializable;

public interface Message extends Serializable {
    
    public String getHandler();
    
}
