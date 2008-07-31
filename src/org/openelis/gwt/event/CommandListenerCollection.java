package org.openelis.gwt.event;

import com.google.gwt.http.client.Request;

import java.util.ArrayList;

public class CommandListenerCollection extends ArrayList<CommandListener> {
    
    private static final long serialVersionUID = 1L;

    public void fireCommand(Enum action, Object obj) {
        for(CommandListener listener : this) {
            listener.performCommand(action, obj);
        }
    }
}
