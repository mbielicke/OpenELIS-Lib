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
package org.openelis.gwt.widget.table.rewrite;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.rewrite.Filter;

import java.util.ArrayList;

public class TableHeader extends FlexTable implements TableHeaderInt, TableListener, MouseListener{
    
    public static String headerStyle = "Header";
    public static String headerCellStyle = "HeaderCell";
    public ArrayList<Label> hLabels = new ArrayList<Label>();
    private ArrayList<TableColumn> columns;
    private boolean resizing;
    private int startx;
    protected int resizeColumn1 = -1;
    protected int tableCol1 = -1;
    protected int resizeColumn2 = -1;
    protected int tableCol2 = -1;
    private TableWidget controller;
    
    
    public TableHeader(TableWidget controller) {
        this.controller = controller;
        this.columns = controller.columns;
        setCellSpacing(0);
        int j = 0;
        for(TableColumn column : columns) {
            Label headerLabel = new Label(column.getHeader());
            hLabels.add(headerLabel);
            if (hLabels.size() > 1) {
                FocusPanel bar = new FocusPanel() {
                    public void onBrowserEvent(Event event) {
                        //controller.event = event;
                        super.onBrowserEvent(event);
                    }
                };
                bar.addMouseListener(this);
                HorizontalPanel hpBar = new HorizontalPanel();
                AbsolutePanel ap1 = new AbsolutePanel();
                ap1.addStyleName("HeaderBarPad");
                AbsolutePanel ap2 = new AbsolutePanel();
                ap2.addStyleName("HeaderBar");
                AbsolutePanel ap3 = new AbsolutePanel();
                ap3.addStyleName("HeaderBarPad");
                hpBar.add(ap1);
                hpBar.add(ap2);
                hpBar.add(ap3);
                bar.add(hpBar);
                setWidget(0, j, bar);
                getFlexCellFormatter().addStyleName(0,j,headerCellStyle);
                getFlexCellFormatter().setWidth(0, j, "3px");
                j++;
            }
            SimplePanel hp0 = new SimplePanel();
            DOM.setStyleAttribute(hp0.getElement(), "overflow", "hidden");
            DOM.setStyleAttribute(hp0.getElement(), "overflowX", "hidden");
            HorizontalPanel hp = new HorizontalPanel();
            headerLabel.addStyleName("HeaderLabel");
            Image img = new Image("Images/unapply.png");
            img.setHeight("10px");
            img.setWidth("10px");
            DOM.setStyleAttribute(headerLabel.getElement(),"overflowX","hidden");
            img.addStyleName("HeaderIMG");
            if (!column.getSortable() && !column.getFilterable())
                img.addStyleName("hide");
            hp.add(headerLabel);
            headerLabel.setWordWrap(false);
            hp.add(img);
            hp.setCellHorizontalAlignment(headerLabel,
                                          HasHorizontalAlignment.ALIGN_RIGHT);
            hp.setCellHorizontalAlignment(img,
                                          HasHorizontalAlignment.ALIGN_LEFT);
            hp0.setWidget(hp);
            DOM.setElementProperty(hp0.getElement(),"align","center");              
            setWidget(0, j, hp0);
            getFlexCellFormatter().addStyleName(0, j, headerCellStyle);
            j++;
           
        }
        setStyleName(headerStyle);
        setHeight("18px");
        addTableListener(this);
        sizeHeader();
    }
    
    public void setHeaders(String[] headers) {
        for(int i = 0; i < headers.length; i++){
            hLabels.get(i).setText(headers[i]);
        }
    }
    
    /**
     * This method will make available the sort and filter options for the
     * column in the table.
     * 
     * @param row
     * @param col
     * @param colFilters
     */
    private void showMenu(int row, int col, Filter[] colFilters) {
        HeaderMenu menu = new HeaderMenu(col / 2,
                                         columns.get(col / 2).getSortable(),
                                         colFilters,
                                         controller);
        int left = DOM.getAbsoluteLeft(getFlexCellFormatter()
                                                  .getElement(row, col));
        int top = DOM.getAbsoluteTop(getFlexCellFormatter()
                                                .getElement(row, col)) +  18; 
                                                //DOM.getIntAttribute(getFlexCellFormatter()
                                                  //                                                      .getElement(row,
                                                    //                                                                col),
                                                      //                                       "offsetHeight");
        menu.setPopupPosition(left, top);
        int width = DOM.getIntAttribute(getFlexCellFormatter()
                                                   .getElement(row, col),
                                        "offsetWidth");
        if (width < 150)
            width = 150;
        menu.setWidth(width + "px");
        menu.show();
    }

    public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
        if (columns.get(cell/2).getSortable() || columns.get(cell/2).getFilterable()) {
            Filter[] colFilters = null;
            if (controller.columns.get(cell/2).getFilterable())
                colFilters = controller.columns.get(cell/2).getFilter();
            showMenu(row, cell, colFilters);
        }   
    }
    
    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        resizing = true;
        startx = sender.getAbsoluteLeft();
            for (int i = 0; i < getCellCount(0); i++) {
                if (sender == getWidget(0, i)) {
                    resizeColumn1 = i - 1;
                    tableCol1 = resizeColumn1 / 2;
                    if(columns.get(tableCol1).getFixedWidth() && columns.get(tableCol1+1).getFixedWidth()){
                        resizing = false;
                        resizeColumn1 = -1;
                        tableCol1 = -1;
                        tableCol2 = -1;
                        return;
                    }  
                    if(columns.get(tableCol1).getFixedWidth()){
                        tableCol1 = -1;
                        int j = tableCol1 -1; 
                        while(tableCol1 < 0 && j > -1){
                            if(!columns.get(tableCol1).getFixedWidth())
                                tableCol1 = j;
                            j--;
                        }
                    }
                    if(tableCol1 < 0){
                        int j = tableCol1 +1;
                        while(tableCol1 < 0 && j < columns.size()){
                            if(!columns.get(j).getFixedWidth())
                                tableCol1 = j;
                            j++;
                        }
                    }
                }
            }
            if(tableCol1 < 0){
                resizing = false;
                resizeColumn1 = -1;
                tableCol1 = -1;
                tableCol2 = -1;
                return;
            }
            tableCol2 = -1;
            int i = tableCol1 +1;
            while(tableCol2 < 0 && i < columns.size()){
                if(!columns.get(i).getFixedWidth())
                    tableCol2 = i;
                i++;
            }
            if(tableCol2 < 0){
                i = 0;
                while(tableCol2 < 0 && i < tableCol1){
                    if(!columns.get(i).getFixedWidth())
                        tableCol2 = i;
                    i++;
                }
            }
            if(tableCol2 < 0){
                resizing = false;
                resizeColumn1 = -1;
                tableCol1 = -1;
                tableCol2 = -1;
                return;
            }
            FocusPanel bar = new FocusPanel();
            bar.addMouseListener(this);
            bar.setHeight((controller.view.table.getOffsetHeight()+17)+"px");
            bar.setWidth("1px");
            DOM.setStyleAttribute(bar.getElement(), "background", "red");
            DOM.setStyleAttribute(bar.getElement(), "position", "absolute");
            DOM.setStyleAttribute(bar.getElement(),"left",sender.getAbsoluteLeft()+"px");
            DOM.setStyleAttribute(bar.getElement(),"top",sender.getAbsoluteTop()+"px");
            RootPanel.get().add(bar);   
            DOM.setCapture(bar.getElement());
    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub
    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseLeave(Widget sender) {

    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseMove(Widget sender, int x, int y) {
        if(resizing) {
            int colA =  columns.get(tableCol1).getCurrentWidth() + (sender.getAbsoluteLeft() - startx);
            int colB =  columns.get(tableCol2).getCurrentWidth() - (sender.getAbsoluteLeft() - startx);
            if((x < 0 && colA <= 16) || (x > 0 && colB <= 16)) 
                 return;
            DOM.setStyleAttribute(sender.getElement(),"left",(DOM.getAbsoluteLeft(sender.getElement())+(x))+"px");
        }
    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseUp(Widget sender, int x, int y) {
            if (resizing) {
                DOM.releaseCapture(sender.getElement());
                columns.get(tableCol1).setCurrentWidth( columns.get(tableCol1).getCurrentWidth() + (sender.getAbsoluteLeft() - startx));
                columns.get(tableCol2).setCurrentWidth( columns.get(tableCol2).getCurrentWidth() - (sender.getAbsoluteLeft() - startx));
                RootPanel.get().remove(sender);
                resizing = false;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        int adj1 = 4;
                        int adj2 = 1;
                        /*if(getAgent().indexOf("safari") > -1){
                            adj1 = 6;
                            adj2 = 2;
                        }
                        */
                        for(int i = 0; i < columns.size(); i++){
                            if( i > 0 && i < columns.size() - 1){
                                getFlexCellFormatter().setWidth(0, i*2,(columns.get(i).getCurrentWidth()-adj1)+"px");
                                getWidget(0,i*2).setWidth((columns.get(i).getCurrentWidth()-adj1)+"px");
                            }else{
                                getFlexCellFormatter().setWidth(0, i*2,(columns.get(i).getCurrentWidth()-adj2)+"px");
                                getWidget(0,i*2).setWidth((columns.get(i).getCurrentWidth()-adj2)+"px");
                            }   
                        }
                        for (int j = 0; j < controller.view.table.getRowCount(); j++) {
                            for (int i = 0; i < columns.size(); i++) {
                                controller.view.table.getFlexCellFormatter().setWidth(j, i, columns.get(i).getCurrentWidth() +  "px");
                                //((TableCellWidget)controller.view.table.getWidget(j,i)).setCellWidth(columns.get(i).getCurrentWidth());
                                ((SimplePanel)controller.view.table.getWidget(j, i)).setWidth((columns.get(i).getCurrentWidth()) + "px");
                                //((SimplePanel)view.table.getWidget(j,i)).getWidget().setWidth(curColWidth[i]+"px");
                            }
                        }
                    }
                });
            }
    }
    
    public void sizeHeader() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                int adj1 = 4;
                int adj2 = 1;
                //if(getAgent().indexOf("safari") > -1){
                //    adj1 = 6;
                //    adj2 = 2;
                //}
                int width = 0;
                for(TableColumn column : columns)
                    width += column.getCurrentWidth();
                int displayWidth = width + (columns.size()*4) - (columns.size() -1);
                setWidth(displayWidth+"px");  
                for(int i = 0; i < columns.size(); i++){
                    if( i > 0 && i < columns.size() - 1){
                        getFlexCellFormatter().setWidth(0, i*2,(columns.get(i).getCurrentWidth()-adj1)+"px");
                        getWidget(0,i*2).setWidth((columns.get(i).getCurrentWidth()-adj1)+"px");
                    }else{
                        getFlexCellFormatter().setWidth(0, i*2,(columns.get(i).getCurrentWidth()-adj2)+"px");
                        getWidget(0,i*2).setWidth((columns.get(i).getCurrentWidth()-adj2)+"px");
                    }
                }
                setWidth(displayWidth+"px");
                controller.view.cellView.setScrollWidth(displayWidth+"px");
            }
        });
    }
    

}
