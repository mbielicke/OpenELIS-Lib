/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.widget.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.MouseWheelListenerCollection;
import com.google.gwt.user.client.ui.MouseWheelVelocity;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class represents the View of the table widget. It contains the logic for
 * displaying and styling a table on the screen.
 * 
 * @author tschmidt
 * 
 */
public class TableView extends Composite implements ScrollListener, MouseWheelListener, KeyboardListener {
    
    public boolean loaded;
    public class CellView extends ScrollPanel implements SourcesMouseWheelEvents {

        private AbsolutePanel ap = new AbsolutePanel();
        
        public CellView() {
            super.setWidget(ap);
            sinkEvents(Event.ONMOUSEWHEEL);
        }
        
        public void onBrowserEvent(Event event) {
            // TODO Auto-generated method stub
            if(DOM.eventGetType(event) == event.ONMOUSEWHEEL){
                listeners.fireMouseWheelEvent(this, event);
                DOM.eventCancelBubble(event, true);
                DOM.eventPreventDefault(event);
            }
            super.onBrowserEvent(event);
        }
        
        private MouseWheelListenerCollection listeners;
        
        public void addMouseWheelListener(MouseWheelListener listener) {
            if(listeners == null){
                listeners = new MouseWheelListenerCollection();
            }
            listeners.add(listener);
        }

        public void removeMouseWheelListener(MouseWheelListener listener) {
            if(listeners != null){
                listeners.remove(listener);
            }    
        }
        
        public void setScrollWidth(String width){
            ap.setWidth(width);
        }
        
        public void setWidget(Widget wid){
            ap.clear();
            ap.add(wid);
        }
        
        public void setHeight(String height) {
            super.setHeight(height);
            ap.setHeight(height);
        }
        
    }
    public CellView cellView = new CellView();
    public ScrollPanel scrollBar = new ScrollPanel();
    public AbsolutePanel headerView = new AbsolutePanel();
    public AbsolutePanel rowsView = new AbsolutePanel();
    private AbsolutePanel statView = new AbsolutePanel();
    private FlexTable ft = new FlexTable();
    public final HorizontalPanel titlePanel = new HorizontalPanel();
    private final Label titleLabel = new Label();
    public FlexTable table = new FlexTable();
    public FlexTable header = new FlexTable();
    public FlexTable rows = new FlexTable();
    private int left = 0;
    private int top = 0;
    public String[] headers;
    public Label[] hLabels;
    private String title = "";
    public static String widgetStyle = "TableWidget";
    public static String cellStyle = "TableCell";
    public static String rowStyle = "TableRow";
    public static String headerStyle = "Header";
    public static String headerCellStyle = "HeaderCell";
    public static String tableStyle = "Table";
    public static String selectedStyle = "Selected";
    public static String navLinks = "NavLinks";
    protected HorizontalPanel navPanel = new HorizontalPanel();
    private VerticalPanel vp = new VerticalPanel(); 
    public String width;
    public int height;
    public TableController controller = null;
    public HTML prevNav;
    public HTML nextNav;
    
    public TableView() {
        initWidget(vp);
    }
    
    public void initTable(TableController controller) {
        this.controller = controller;
        if(title != null && !title.equals("")){
            titleLabel.setText(title);
            titlePanel.add(titleLabel);
            titlePanel.addStyleName("TitlePanel");
        }
        if(headers != null){
            header.setCellSpacing(0);
            int j = 0;
            for (int i = 0; i < headers.length; i++) {
                if (i > 0) {
                    FocusPanel bar = new FocusPanel();
                    bar.addMouseListener(controller);
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
                    header.setWidget(0, j, bar);
                    header.getFlexCellFormatter().addStyleName(0,
                                                               j,
                                                               headerCellStyle);
                    header.getFlexCellFormatter().setWidth(0, j, "3px");
                    j++;
                }
                SimplePanel hp0 = new SimplePanel();
                DOM.setStyleAttribute(hp0.getElement(), "overflow", "hidden");
                DOM.setStyleAttribute(hp0.getElement(), "overflowX", "hidden");
                HorizontalPanel hp = new HorizontalPanel();
                hLabels[i].addStyleName("HeaderLabel");
                Image img = new Image("Images/unapply.png");
                img.setHeight("10px");
                img.setWidth("10px");
                DOM.setStyleAttribute(hLabels[i].getElement(),"overflowX","hidden");
                img.addStyleName("HeaderIMG");
                if ((controller.sortable == null || !controller.sortable[i]) && 
                    (controller.filterable == null || !controller.filterable[i]))
                    img.addStyleName("hide");
                hp.add(hLabels[i]);
                hLabels[i].setWordWrap(false);
                hp.add(img);
                hp.setCellHorizontalAlignment(hLabels[i],
                                              HasHorizontalAlignment.ALIGN_RIGHT);
                hp.setCellHorizontalAlignment(img,
                                              HasHorizontalAlignment.ALIGN_LEFT);
                hp0.setWidget(hp);
                DOM.setElementProperty(hp0.getElement(),"align","center");              
                header.setWidget(0, j, hp0);
                header.getFlexCellFormatter().addStyleName(0,
                                                           j,
                                                           headerCellStyle);
                j++;
               
            }
            header.setStyleName(headerStyle);
            header.addTableListener(controller);
        }
        //DOM.setStyleAttribute(statView.getElement(), "overflow", "hidden");
        headerView.add(header);
        cellView.setWidget(table);
        DOM.setStyleAttribute(headerView.getElement(), "overflow", "hidden");
        cellView.addScrollListener(this);
        AbsolutePanel tspacer = new AbsolutePanel();
        tspacer.setStyleName("TableSpacer");
        if(title != null && !title.equals("")){
           // HorizontalPanel hp = new HorizontalPanel();
            //hp.add(titlePanel);
            //hp.add(new HTML("<td width="))
        	vp.add(titlePanel);
        }else{    
            
        }
        if(controller.showRows) {
        	if(headers != null)
        		ft.setWidget(0,1,headerView);
            ft.setWidget(1,0,rows);
            ft.getFlexCellFormatter().setVerticalAlignment(1, 0, HasAlignment.ALIGN_TOP);
            ft.setWidget(1,1,cellView);
            ft.setWidget(0, 2, scrollBar);
            ft.getFlexCellFormatter().setRowSpan(0, 2, 2);
            ft.getFlexCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT);
            ft.getFlexCellFormatter().setVerticalAlignment(0,2,HasAlignment.ALIGN_TOP);
        }else{
        	if(headers != null){
        		ft.setWidget(0,0,headerView);
                 ft.setWidget(0,1,scrollBar);
                ft.setWidget(1,0,cellView);
                ft.getFlexCellFormatter().setRowSpan(0, 1, 2);
                ft.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
                ft.getFlexCellFormatter().setVerticalAlignment(0,1,HasAlignment.ALIGN_BOTTOM);
            }else{
                ft.setWidget(0,0,cellView);
                ft.setWidget(0, 1, scrollBar);
                ft.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
                ft.getFlexCellFormatter().setVerticalAlignment(0,0,HasAlignment.ALIGN_TOP);
            }
        }
        vp.add(ft);
        ft.setCellPadding(0);
        ft.setCellSpacing(0);
        table.setCellSpacing(1);
        table.addStyleName(tableStyle);
        //table.sinkEvents(Event.KEYEVENTS);
        cellView.setWidget(table);
        ft.setCellSpacing(0);
        scrollBar.setWidth("18px");
        scrollBar.addScrollListener(this);
        AbsolutePanel ap = new AbsolutePanel();
        DOM.setStyleAttribute(scrollBar.getElement(), "overflowX", "hidden");
        DOM.setStyleAttribute(scrollBar.getElement(), "display", "none");
        DOM.setStyleAttribute(cellView.getElement(),"overflowY","hidden");
        scrollBar.setWidget(ap);
        cellView.addMouseWheelListener(this);
    }
    
    
    public void setHeight(int height) {
        if(!width.equals("auto")){
            cellView.setHeight(height+18+"px");
            this.height = height+18;
        }else{
            cellView.setHeight(height+"px");
            this.height = height;
        }
        rowsView.setHeight(height+"px");
        scrollBar.setHeight(height+"px");
        headerView.setHeight("18px");

    }

    public void setWidth(String width) {
        this.width = width.trim();
        cellView.setWidth(width);
        titlePanel.setWidth(width);
        headerView.setWidth(width);
        rows.setWidth("25px");
    }

    public void setTableListener(TableListener listener) {
        table.addTableListener(listener);
        controller = (TableController)listener;
    }

    public void setCell(Widget widget, int row, int col) {
        table.setWidget(row, col, widget);
        if (widget instanceof FocusWidget)
            ((FocusWidget)widget).setFocus(true);
    }
/*
    public void reset() {
        table = new FlexTable();
        table.setCellSpacing(1);
        table.addStyleName(tableStyle);
        table.addTableListener(controller);
        if(controller.showRows){
            rows = new FlexTable();
            ft.setWidget(1,0,rows);
            rows.setCellSpacing(1);
        }
        cellView.setWidget(table);
    }
  */  
    public void setTable(){
        cellView.clear();
        cellView.setWidget(table);
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
        if(hLabels == null){
            hLabels = new Label[headers.length];
            for(int i = 0; i < headers.length; i++){
                hLabels[i] = new Label(headers[i]);
            }
        }else{
            for(int i = 0; i < headers.length; i++){
                hLabels[i].setText(headers[i]);
            }
        }
    }

    public void setTitle(String title) {
        this.title = title;
        titleLabel.setText(title);
    }

    public void setNavPanel(int curIndex, int pages, boolean showIndex) {
        vp.remove(navPanel);
        navPanel = new HorizontalPanel();
        navPanel.setSpacing(3);
        navPanel.addStyleName(navLinks);
        navPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        DOM.setAttribute(navPanel.getElement(), "align", "center");
        FocusPanel leftButtonPanel = new FocusPanel();
        FocusPanel rightButtonPanel = new FocusPanel();

        prevNav = new HTML("");
        prevNav.addStyleName("prevNavIndex");
        prevNav.addClickListener(controller);
        
        nextNav = new HTML("");
        nextNav.addStyleName("nextNavIndex");
        nextNav.addClickListener(controller);
        
        leftButtonPanel.add(prevNav);
        rightButtonPanel.add(nextNav);
        navPanel.add(leftButtonPanel);
        navPanel.add(rightButtonPanel);                       
                        
        if (curIndex > 0) {            
            leftButtonPanel.removeStyleName("disabled");
        }
        else{          
        	leftButtonPanel.setStyleName("disabled");
        }
        if(showIndex){
            for (int i = 1; i <= pages; i++) {
                final int index = i - 1;
                HTML nav = null;
                if (index != curIndex) {
                    nav = new HTML("<a class='navIndex' value='" + index
                                   + "'>"
                                   + i
                                   + "</a>");
                    nav.addClickListener(controller);
                } else {
                    nav = new HTML("" + i);
                    nav.setStyleName("current");
                }
                navPanel.add(nav);
            }
        }

        navPanel.add(nextNav);    
        
        vp.add(navPanel);
    }

    public void onScroll(Widget sender, int scrollLeft, final int scrollTop) {
    	if(sender == scrollBar ) {
    		if(top != scrollTop){
    			controller.scrollLoad(scrollTop);
    			top = scrollTop;
    		}
    	}
    	if(sender == cellView){
    		if(left != scrollLeft){
    			headerView.setWidgetPosition(header, -scrollLeft, 0);
    			left = scrollLeft;
    		}
    	}
    }
    
    public void setScrollHeight(int height) {
        try {
            scrollBar.getWidget().setHeight(height+"px");
            if(!controller.showScrolls){
                if(height > (this.height+controller.cellSpacing))
                    DOM.setStyleAttribute(scrollBar.getElement(), "display", "block");
                else 
                    DOM.setStyleAttribute(scrollBar.getElement(),"display","none");
            }
        }catch(Exception e){
            Window.alert("set scroll height"+e.getMessage());
        }
    }

    public void onMouseWheel(Widget sender, MouseWheelVelocity velocity) {
        int pos = scrollBar.getScrollPosition();
        int delta = velocity.getDeltaY();
        if(delta < 0 && delta > - controller.cellHeight)
            delta = -controller.cellHeight;
        if(delta > 0 && delta < 18)
            delta = controller.cellHeight;
        scrollBar.setScrollPosition(pos + delta);
    }

    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }
    
}