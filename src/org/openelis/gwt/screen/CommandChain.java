package org.openelis.gwt.screen;

import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;

public class CommandChain implements CommandListener {
    
    private CommandListenerCollection listeners = new CommandListenerCollection();

    public void performCommand(Enum action, Object obj) {
        listeners.fireCommand(action, obj);
    }
    
    public boolean canPerformCommand(Enum action, Object obj){
        return true;
    }
    
    public void addCommand(SourcesCommandEvents source){
        if(source instanceof SourcesCommandEvents)
            ((SourcesCommandEvents)source).addCommandListener(this);
        if(source instanceof CommandListener) {
            listeners.add((CommandListener)source);
        }
    }

}
