package org.openelis.gwt.event;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.EventListener;

public interface CommandListener extends EventListener {
    
    public void performCommand(Enum action, Object obj);

}
