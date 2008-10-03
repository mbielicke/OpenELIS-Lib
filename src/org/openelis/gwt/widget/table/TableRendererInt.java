package org.openelis.gwt.widget.table;

public interface TableRendererInt {
    
    public void createRow(int i);
    
    public void load(int pos);
    
    public void scrollLoad(int scrollPos);
    
    public void setCellDisplay(int row, int col);
    
    public void setCellEditor(int row, int col);
    

}
