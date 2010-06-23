/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.widget.redesign.table;

import org.openelis.gwt.common.Util;
import org.openelis.gwt.event.ScrollBarEvent;
import org.openelis.gwt.event.ScrollBarHandler;
import org.openelis.gwt.widget.HasHelper;
import org.openelis.gwt.widget.ScrollBar;
import org.openelis.gwt.widget.WidgetHelper;
import org.openelis.gwt.widget.redesign.table.Table.Scrolling;

import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class View extends Composite {
    /**
     * Reference to Table this View is used in
     */
    protected Table           table;
    /**
     * Table used to draw Table cells
     */
    protected FlexTable       cells;
    /**
     * Table used to draw Header cells for the table
     */
    protected FlexTable       header;
    /**
     * Scrollable area that contains cells and possibly header for horizontal scroll.
     */
    protected ScrollPanel     scrollView;
    /**
     * Vertical ScrollBar
     */
    protected ScrollBar       scrollBar;
    /**
     * Panel to hold Scrollable view area and ScrollBar together.
     */
    protected HorizontalPanel outer;
    
    protected int firstIndex;
    
    /**
     * Constructor that takes a reference to the table that will use this view 
     * @param table
     */
    public View(Table tbl) {
        this.table = tbl;
        
        outer = new HorizontalPanel();
        initWidget(outer);
        
        addDomHandler(new MouseWheelHandler() {
            public void onMouseWheel(MouseWheelEvent event) {
                int pos, delta, height;

                pos = scrollBar.getScrollPosition();
                delta = event.getDeltaY();
                height = table.getRowHeight();

                if (delta < 0 && delta > - height)
                    delta = -height;
                if (delta > 0 && delta < height)
                    delta = height;
                scrollBar.setScrollPosition(pos + delta);
            }
        }, MouseWheelEvent.getType());
        
        draw();
    }
    
    public void draw() {
        /*
         * This panel will be used only if the table contains a header.  This 
         * is used to glue the Header and Cells table together because ScrollPanel 
         * extends SimplePanel and can only have one widget.
         */
        VerticalPanel vp = null;
        
        //******** Set layout of view ***************
        outer.clear();
        scrollView = new ScrollPanel();
        cells      = new FlexTable();
        
        if(table.hasHeader) {
            vp     = new VerticalPanel();
            header = new FlexTable();
            cells  = new FlexTable();
            vp.add(header);
            vp.add(cells);
            vp.setSpacing(0);
            scrollView.setWidget(vp);
        }else
            scrollView.setWidget(cells);
        
        outer.add(scrollView);
        
        
        
        if(table.getVerticalScroll() != Scrolling.NEVER) {
            scrollBar = new ScrollBar();
            scrollBar.addScrollBarHandler(new ScrollBarHandler() {
                public void onScroll(ScrollBarEvent event) {
                    int newFirstIndex;
                    
                    newFirstIndex = calcFirstIndex(scrollBar.getScrollPosition());
                    
                    renderView(newFirstIndex);
                }
            });
            adjustScrollBar();
            outer.add(scrollBar);
        }
        
        //******* Create Header table if needed **************
        if(table.hasHeader)
            createHeader();
        
        //******* Create number of Rows needed to display ****
        
        createRows();
    }
    
    /**
     * Method will create the Header table to be displayed by this view
     * using information found in the Column list in the Table
     */
    private void createHeader() {
        
    }
    
    /**
     * Will create the the necessary visible rows for the cells table 
     * depending on what is needed at the time.  If model.size() < visibleRows
     * then the number of rows created will equal model.size() else the number 
     * visibleRows will be created for the cells table.
     */
    private void createRows() {
        int rowsToCreate, rowsPresent;
        
        if(table.getModel().size() < table.getVisibleRows())
            rowsToCreate = table.getModel().size();
        else
            rowsToCreate = table.getVisibleRows();
        
        rowsPresent = cells.getRowCount();
        
        if(rowsPresent < rowsToCreate){
            for(int i = rowsPresent; i < rowsToCreate; i++) {
                cells.insertRow(i);
            }
        }else {
            for(int i = rowsPresent; i > rowsToCreate; i--) {
                cells.removeRow(0);
            }
        }
        
        firstIndex = -1;
        
        renderView(0);
    }
    
    protected void renderView(int startIndex) {
        if(firstIndex < 0 || (Math.abs(startIndex - firstIndex) > table.getVisibleRows() / 4))
            renderViewBySetAll(startIndex);
        else
            renderViewByDeleteAdd(startIndex);
    }
    
    private void renderViewByDeleteAdd(int startIndex) {
        int rowsToScroll, newFirst = 0, modelStart, cellsStart;
        
        /*
         * Delete and add Rows from either Top or Bottom depending on direction of scroll. 
         * Also calc and set indexes needed to render the new rows.
         */
        if(startIndex > firstIndex) {
            rowsToScroll = startIndex - firstIndex;
            newFirst = firstIndex + rowsToScroll;
            for(int i = 0; i < rowsToScroll; i++) {
                cells.removeRow(0);
                cells.insertRow(cells.getRowCount());
            }
            modelStart = newFirst + table.getVisibleRows() - rowsToScroll;
            cellsStart = table.getVisibleRows() - rowsToScroll;
        }else{
            rowsToScroll = firstIndex - startIndex;
            newFirst = firstIndex - rowsToScroll;
            for(int i = 0; i < rowsToScroll; i++) {
                cells.removeRow(cells.getRowCount() -1);
                cells.insertRow(0);
            }
            modelStart = newFirst;
            cellsStart = 0;            
        }
        
        /*
         * Render the new rows with data from the model. 
         */
        for(int i = 0; i < rowsToScroll; i++) {
            renderRow(cellsStart+i,modelStart+i);
        }
            
        
        firstIndex = newFirst;
    }
    
    private void renderViewBySetAll(int startIndex) {
        for(int i = 0; i < cells.getRowCount(); i++) {
            renderRow(i,startIndex+i);
        }
        firstIndex = startIndex;
    }
    
    
    protected void renderRow(int row) {
        int cellsIndex;
        
        cellsIndex = getCellsIndex(row);
        /*
         * If table has not reached srollable length yet make sure that a row is present for the index
         * passed such as when a row is added. 
         */
        if(cellsIndex == cells.getRowCount())
            cells.insertRow(row);
        
        renderRow(cellsIndex,row);
    }
    
    @SuppressWarnings("unchecked")
    private void renderRow(int cellsIndex, int modelIndex) {
        for(int col = 0; col < table.getColumnCount(); col++) {
            renderCell(cellsIndex,col,modelIndex);
        }
    }
    
    protected void renderCell(int row, int col) {
        renderCell(getCellsIndex(row), col, row);
    }
    
    @SuppressWarnings("unchecked")
    private void renderCell(int cellsIndex, int col, int modelIndex) {
        Column column;
        WidgetHelper helper;
        Label display;
        
        column = table.columnAt(col);
        helper = ((HasHelper)column.getEditor()).getHelper();
        
        display = new Label(helper.format(table.getValueAt(modelIndex, col)));
        display.setWordWrap(false);
        display.setWidth(Util.addUnits(column.getWidth()));
        
        cells.setWidget(cellsIndex,col,display);
        
    }
    
    @SuppressWarnings("unchecked")
    public void switchToEditor(int row, int col) {
        int cellsIndex;
        Widget editor;
        
        cellsIndex = getCellsIndex(row);
        
        editor     = table.columnAt(col).getEditor();
        ((HasValue)editor).setValue(table.getValueAt(row, col));
        
        cells.setWidget(cellsIndex, col, editor);
    }
    
    public Object swtichToDisplay(int row, int col) {
        int cellsIndex;
        Object value;
        HasValue editor; 
        
        cellsIndex = getCellsIndex(row);
        
        editor = (HasValue)cells.getWidget(cellsIndex, col);
        value = editor.getValue();
        
        renderCell(cellsIndex,col,row);
        
        return value;
    }
    
    private int getCellsIndex(int modelIndex) {
        return modelIndex - firstIndex;
    }
    
    private int calcFirstIndex(int scrollPos) {
        return scrollPos / table.getRowHeight();
    }
    
    public void adjustScrollBar() {
        int height;
        
        height = table.getRowHeight();
        
        scrollBar.adjust(table.getVisibleRows() * height, table.getModel().size() * height);
    }
    

}
