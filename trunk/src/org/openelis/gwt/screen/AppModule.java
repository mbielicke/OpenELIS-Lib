package org.openelis.gwt.screen;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MouseListener;

public interface AppModule extends EntryPoint, ClickListener, MouseListener {
    
    public String getModuleName();

}
