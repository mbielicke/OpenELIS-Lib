/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.widget.redesign.table;

import org.openelis.gwt.widget.TextBox;

import com.google.gwt.user.client.ui.Widget;

/**
 * This is a logical class used to describe a column in a Table
 * @author tschmidt
 *
 */
public class Column {

    /**
     * Reference to the Table containing this column
     */
    protected Table  table;
    /**
     * Editor widget used for this column
     */
    protected CellEditor editor = new LabelCell<String>();
    
    protected CellRenderer renderer = (CellRenderer)editor;
    /**
     * name used to reference column and label for the Header 
     */
    protected String name, label;
    /**
     * width - default and current width
     * minWidth - Minimum allowed width of the column
     */
    protected int    width, minWidth;
    
    protected boolean enabled, resizable, filterable = true;

    /**
     * Creates a default column that exist outside a table has 
     * a TextBox<String> has the editor.
     */
    public Column() {
        this(null, "", "");
    }

    /**
     * Creates a column for the Table passed and defaults the editor
     * to a TextBox<String>
     */
    public Column(Table table, String name, String label) {
        this.table = table;
        this.name = name;
        this.label = label;
        enabled = true;
        resizable = true;
        width = 15;
        minWidth = 15;
    }

    /**
     * Returns the Editor currently being used by this Column
     */
    public CellEditor getCellEditor() {
        return editor;
    }

    /**
     * Sets the Editor widget to be used by this Column. This method will also
     * set Cell Renderer if the passed editor also implements the CellRenderer interface.
     * If you need a different Cell Renderer make sure to call setCellEditor first before
     * calling setCellRenderer.
     * @param editor
     */
    public void setCellEditor(CellEditor editor) {
        this.editor = editor;
        if(editor instanceof CellRenderer)
            renderer = (CellRenderer)editor;
    }

    /**
     * Returns the Table that this Column is used in.
     * @return
     */
    public Table getTable() {
        return table;
    }

    /**
     * Sets the Table that this Column is used in.
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * Returns the name of set to this Column.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name to be used by this Column
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the string used as the header for this Column
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the string to be used as the header for this Column
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the width being used by this Column.
     * @return
     */
    public int getWidth() {
        
        int totalWidth,lastColumn;
       
        if(table == null)
            return minWidth;
        
        lastColumn = table.getColumnCount() - 1;
        if(lastColumn >= 0 && table.getColumnAt(lastColumn) == this) {
            totalWidth = table.getXForColumn(lastColumn);
            if(totalWidth + width < table.getWidthWithoutScrollbar())
                return table.getWidthWithoutScrollbar() - totalWidth;
        }
        
        return width;
    }

    /**
     * Sets the width to be used by this Column
     * @param width
     */
    public void setWidth(int width) {
        this.width = Math.max(width,minWidth);
        table.resize();
    }

    /**
     * Returns the Minimum width to be used by this Column.
     * @return
     */
    public int getMinWidth() {
        return minWidth;
    }

    /**
     * Sets the minimum width to be used by this Column.
     * @param minWidth
     */
    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    /**
     * Method used to check if this Column is enabled for editing.
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Method used to enable/disable this Column for editing.
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
//     * Method used to determine if this Column is allowed to be resized.
     * @return
     */
    public boolean isResizable() {
        return resizable;
    }

    public boolean isFilterable() {
        return filterable;
    }

    public void setFilterable(boolean filterable) {
        this.filterable = filterable;
    }

    /**
     * Method used to allow/disallow this Column to be resized. 
     * @param resizable
     */
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }
    
    public CellRenderer getCellRenderer() {
        return renderer;
    }
    
    public void setCellRenderer(CellRenderer renderer) {
        this.renderer = renderer;
    }

}
