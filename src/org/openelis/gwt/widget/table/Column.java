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
import java.util.Iterator;

import org.openelis.gwt.widget.Label;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is a logical class used to describe a column in a Table
 * 
 * @author tschmidt
 * 
 */
public class Column implements ColumnInt, IsWidget, HasWidgets.ForIsWidget {

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
    protected CellEditor   editor;

    /**
     * Render widget used for this column
     */
	protected CellRenderer renderer = new LabelCell(new Label<String>());
    
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
    protected boolean      enabled, resizable, isFiltered, isSorted, isSortable, isFilterable, required, display=true;


    protected String style;
    
    public static class Builder {
    	
    	String name,label,style;
    	int width,minWidth = 15;
    	boolean enabled = true,
    	        resizable = true,
    	        isSortable, isFilterable, required,display = true;
    	CellRenderer renderer;
    	
    	public Builder(int width) {
    		this.width = width;
    	}
    	
    	public Builder minWidth(int minWidth) {
    		this.minWidth = minWidth;
    		return this;
    	}
    	
    	public Builder name(String name) {
    		this.name = name;
    		return this;
    	}
    	
    	public Builder label(String label) {
    		this.label = label;
    		return this;
    	}
    	
    	public Builder style(String style) {
    		this.style = style;
    		return this;
    	}
    	
    	public Builder enabled(boolean enabled) {
    		this.enabled = enabled;
    		return this;
    	}
    	
    	public Builder resizable(boolean resizable) {
    		this.resizable = resizable;
    		return this;
    	}
    	
    	public Builder isSortable(boolean sortable) {
    		this.isSortable = sortable;
    		return this;
    	}
    	
    	public Builder isFilterable(boolean filterable) {
    		this.isFilterable = filterable;
    		return this;
    	}
    	
    	public Builder required(boolean required) {
    		this.required = required;
    		return this;
    	}
    	
    	public Builder renderer(CellRenderer renderer) {
    		this.renderer = renderer;
    		return this;
    	}
    	
    	public Builder display(boolean display) {
    		this.display = display;
    		return this;
    	}
    	
    	public Column build() {
    		return new Column(this);
    	}
    }
    
    public Column(Builder builder) {
        name = builder.name;
        label = builder.label;
        enabled = builder.enabled;
        resizable = builder.resizable;
        width = builder.width;
        minWidth = builder.minWidth;
        style = builder.style;
        isSortable = builder.isSortable;
        isFilterable = builder.isFilterable;
        required = builder.required;
        this.display = builder.display;
        if(builder.renderer != null)
        	setCellRenderer(builder.renderer);
        //setVisible(false);
    }
    
    public Column() {
    	
    }
    
	public CellEditor getCellEditor() {
        return editor;
    }

    /**
     * Returns the Editor currently being used by this Column
     */
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
	public void setCellEditor(CellEditor editor) {
        this.editor = editor;
    }
    
    
	public CellRenderer getCellRenderer() {
        return renderer;
    }

	/**
     * Method will return the currently set Renderer for this column
     * @return
     */
	public CellRenderer getCellRenderer(int row) {
        return renderer;
    }

    /**
     * Method will set the current renderer for this column
     * @param renderer
     */
	@UiChild(limit=1,tagname="renderer")
	public void setCellRenderer(CellRenderer renderer) {
        this.renderer = renderer;
        if (renderer instanceof CellEditor) {
            editor = (CellEditor)renderer;
            editor.setColumn(this);
        }
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
    public void setTable(Table table) {
        this.table = table;
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
    public void setName(String name) {
        this.name = name;
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
    public void setLabel(String label) {
        this.label = label;
        if(table != null && table.view != null && table.view.header != null)
        	table.view.header.layout();
    }

    public void setStyle(String style) {
    	this.style = style;
    	if(table != null)
    		table.layout();
    }
    
    public String getStyle() {
    	return style;
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
    public void setWidth(int width) {
        this.width = Math.max(width, minWidth);
        if(table != null)
        	table.resize();
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
    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
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
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Method used to allow/disallow this Column to be resized.
     * 
     * @param resizable
     */
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
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
    public void setFiltered(boolean isFiltered) {
        this.isFiltered = isFiltered;
    }

    /**
     * Method used to determine if this Column can be filtered
     * @return
     */
    public boolean isFilterable() {
        return isFilterable;
    }
    
    public void setFilterable(boolean filterable) {
        isFilterable = filterable;
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
    public void setFilter(Filter filter) {
        this.filter = filter;
        isFiltered = false;
        isSortable = true;
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
    public void setSortable(boolean isSortable) {
        this.isSortable = isSortable;
    }

    /**
     * Method used to set the sortable flag for this column
     * @param filterable
     */
    @SuppressWarnings("rawtypes")
	public void setSort(Comparator sort) {
        this.sort = sort;
        isSorted = false;
        isSortable = true;
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
    public void setRequired(boolean required) {
        this.required = required;
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
    
    public boolean isDisplayed() {
    	return display;
    }
    
    public void setDisplay(boolean display) {
    	this.display = display;
    	table.layout();
    }

	@Override
	public void finishEditing() {
		table.finishEditing();
	}

	
	@Override
	public void add(IsWidget w) {
		assert w instanceof CellRenderer;

		setCellRenderer((CellRenderer)w);
	}
	
	

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Widget> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Widget w) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean remove(IsWidget w) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return new SimplePanel();
	}

	@Override
	public void add(Widget w) {
		// TODO Auto-generated method stub
		
	}
	

}
