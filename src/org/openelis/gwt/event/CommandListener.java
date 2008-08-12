package org.openelis.gwt.event;

public interface CommandListener {
    
    public void performCommand(Enum action, Object obj);
    
    public boolean canPerformCommand(Enum action, Object obj);

}
