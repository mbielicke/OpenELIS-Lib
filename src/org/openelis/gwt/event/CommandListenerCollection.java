package org.openelis.gwt.event;

import com.google.gwt.user.client.Window;

import java.util.ArrayList;

public class CommandListenerCollection extends ArrayList<CommandListener> {
    
    private static final long serialVersionUID = 1L;

    public void fireCommand(Enum action, Object obj) {
        for(CommandListener listener : this) {
            if(listener.canPerformCommand(action, obj))
                listener.performCommand(action, obj);
        }
    }
}
