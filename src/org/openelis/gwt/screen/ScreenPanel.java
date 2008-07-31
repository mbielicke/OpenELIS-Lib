package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.Panel;

import org.openelis.gwt.common.data.AbstractField;

public interface ScreenPanel {
    
    public void load(AbstractField field);
    
    public void clear();
    
    public Panel getPanel();
    
    public void submit(AbstractField field);

}
