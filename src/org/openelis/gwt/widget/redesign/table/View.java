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

import org.openelis.gwt.widget.ScrollBar;
import org.openelis.gwt.widget.redesign.table.Table.Scrolling;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
    public View(Table table) {
        this.table = table;
        
        outer = new HorizontalPanel();
        initWidget(outer);
        
        draw();
    }
    
    public void draw() {
        /*
         * This panel will be used only if the table contains a header.  This 
         * is used to glue the Header and Cells table together because ScrollPanel 
         * extends SimplePanel and can only have one widget.
         */
        VerticalPanel vp = null;
        
        //******** Setl layout of view ***************
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
        
    }
    
    public void renderView(int startIndex) {
        if(firstIndex < 0 || (Math.abs(startIndex - firstIndex) > table.getVisibleRows() / 4))
            renderViewBySetAll(startIndex);
        else
            renderViewByDeleteAdd(startIndex);
    }
    
    private void renderViewByDeleteAdd(int startIndex) {
        
    }
    
    private void renderViewBySetAll(int startIndex) {
        
    }
    

}
