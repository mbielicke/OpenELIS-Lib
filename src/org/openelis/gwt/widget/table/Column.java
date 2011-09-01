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
package org.openelis.gwt.widget.table;

import java.util.Comparator;

import org.openelis.gwt.widget.Label;

/**
 * This is a logical class used to describe a column in a Table
 * 
 * @author tschmidt
 * 
 */
public class Column {

    /**
     * Reference to the Table containing this column
     */
    protected Table        table;
    
    /**
     * Filter used for this column
     */
    protected Filter       filter;
    
    /**
     * Comparator implementation to use when sorting this column
     */
    @SuppressWarnings("rawtypes")
	protected Comparator   sort;
    
    /**
     * Editor widget used for this column
     */
    @SuppressWarnings({ "rawtypes" })
    protected CellEditor   editor;

    /**
     * Render widget used for this column
     */
    @SuppressWarnings("rawtypes")
	protected CellRenderer renderer = new LabelCell<String>(new Label<String>());
    
    /**
     * name used to reference column and label for the Header
     */
    protected String       name, label;

    /**
     * width - default and current width minWidth - Minimum allowed width of the
     * column
     */
    protected int          width, minWidth;

    /**
     * Boolean flags used by column
     */
    protected boolean      enabled, resizable, isFiltered, isSorted, isSortable, isFilterable, required;


    /**
     * Creates a default column that exist outside a table has a TextBox<String>
     * has the editor.
     */
    public Column() {
        this(null, "", "");
    }

    /**
     * Creates a column for the Table passed and defaults the editor to a
     * TextBox<String>
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
    
    @SuppressWarnings("rawtypes")
	public CellEditor getCellEditor() {
        return editor;
    }

    /**
     * Returns the Editor currently being used by this Column
     */
    @SuppressWarnings("rawtypes")
	public CellEditor getCellEditor(int row) {
        return editor;
    }

    /**
     * Sets the Editor widget to be used by this Column. This method will also
     * set Cell Renderer if the passed editor also implements the CellRenderer
     * interface. If you need a different Cell Renderer make sure to call
     * setCellEditor first before calling setCellRenderer.
     * 
     * @param editor
     */
    @SuppressWarnings("rawtypes")
	public Column setCellEditor(CellEditor editor) {
        this.editor = editor;
        return this;
    }
    
    
    @SuppressWarnings("rawtypes")
	public CellRenderer getCellRenderer() {
        return renderer;
    }
    /**
     * Method will return the currently set Renderer for this column
     * @return
     */
    @SuppressWarnings("rawtypes")
	public CellRenderer getCellRenderer(int row) {
        return renderer;
    }

    /**
     * Method will set the current renderer for this column
     * @param renderer
     */
    @SuppressWarnings("rawtypes")
	public Column setCellRenderer(CellRenderer renderer) {
        this.renderer = renderer;
        if (renderer instanceof CellEditor)
            editor = (CellEditor)renderer;
        return this;
    }

    /**
     * Returns the Table that this Column is used in.
     * 
     * @return
     */
    public Table getTable() {
        return table;
    }

    /**
     * Sets the Table that this Column is used in.
     * 
     * @param table
     */
    public Column setTable(Table table) {
        this.table = table;
        return this;
    }

    /**
     * Returns the name of set to this Column.
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name to be used by this Column
     * 
     * @param name
     */
    public Column setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns the string used as the header for this Column
     * 
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the string to be used as the header for this Column
     * 
     * @param label
     */
    public Column setLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * Returns the width being used by this Column.
     * 
     * @return
     */
    public int getWidth() {

        int totalWidth, lastColumn;

        if (table == null)
            return minWidth;

        /*
         * If this is the last column calculate its width if the overall width 
         * will be less then the set width of the table
         */
        lastColumn = table.getColumnCount() - 1;
        if (lastColumn >= 0 && table.getColumnAt(lastColumn) == this) {
            totalWidth = table.getXForColumn(lastColumn);
            if (totalWidth + width < table.getWidthWithoutScrollbar())
                return table.getWidthWithoutScrollbar() - totalWidth;
        }
     
        return width;
    }

    /**
     * Sets the width to be used by this Column
     * 
     * @param width
     */
    public Column setWidth(int width) {
        this.width = Math.max(width, minWidth);
        table.resize();
        return this;
    }

    /**
     * Returns the Minimum width to be used by this Column.
     * 
     * @return
     */
    public int getMinWidth() {
        return minWidth;
    }

    /**
     * Sets the minimum width to be used by this Column.
     * 
     * @param minWidth
     */
    public Column setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        return this;
    }

    /**
     * Method used to check if this Column is enabled for editing.
     * 
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Method used to enable/disable this Column for editing.
     * 
     * @param enabled
     */
    public Column setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    
    /**
     * Method used to allow/disallow this Column to be resized.
     * 
     * @param resizable
     */
    public Column setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    /**
     * Method used to determine if this Column is allowed to be resized.
     * 
     * @return
     */
    public boolean isResizable() {
        return resizable;
    }
    
    /**
     * Method used to set the flag indicating that this column is currently
     * being filtered.
     * @param isFiltered
     */
    public Column setFiltered(boolean isFiltered) {
        this.isFiltered = isFiltered;
        return this;
    }

    /**
     * Method used to determine if this Column can be filtered
     * @return
     */
    public boolean isFilterable() {
        return isFilterable;
    }
    
    public Column setFilterable(boolean filterable) {
        isFilterable = filterable;
        return this;
    }
    
    /**
     * Method used to determine if this column currently has a filter applied 
     * @return
     */
    public boolean isFiltered() {
        return isFiltered;
    }
    
    /**
     * Sets the filter to be used when filtering this column
     * @param filter
     */
    public Column setFilter(Filter filter) {
        this.filter = filter;
        isFiltered = false;
        isSortable = true;
        return this;
    }
    
    /**
     * Returns the Filter to be used for this column
     * @return
     */
    public Filter getFilter() {
        return filter;
    }
    
    /**
     * Method used to determine if this Column can be sorted
     * @return
     */
    public boolean isSortable() {
        return isSortable;
    }
    
    /**
     * Method used to set the flag if this column is allowed to be sorted
     * @param isSortable
     */
    public Column setSortable(boolean isSortable) {
        this.isSortable = isSortable;
        return this;
    }

    /**
     * Method used to set the sortable flag for this column
     * @param filterable
     */
    @SuppressWarnings("rawtypes")
	public Column setSort(Comparator sort) {
        this.sort = sort;
        isSorted = false;
        isSortable = true;
        return this;
    }
    
    /**
     * Method used to determine if this column currently sorted 
     * @return
     */
    public boolean isSorted() {
        return isSorted;
    }
    
    /**
     * Method used to set the required flag for this column
     * @param required
     */
    public Column setRequired(boolean required) {
        this.required = required;
        return this;
    }
    
    /**
     * Method to determine if this column is required to have a value set in each row
     * @return
     */
    public boolean isRequired() {
        return required;
    }

    public boolean hasEditor() {
        return editor != null;
    }
}
