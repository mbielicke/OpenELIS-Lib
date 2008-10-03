package org.openelis.gwt.widget.tree;

import com.google.gwt.user.client.ui.Widget;

public interface TreeViewInt {
    
    public enum VerticalScroll {NEVER,ALWAYS,NEEDED};

    public void setHeight(int height);
    
    public void setWidth(String width);
    
    public void setCell(Widget widget, int row, int col);
    
    public void setTitle(String title);
    
    public void setScrollHeight(int height);
}
