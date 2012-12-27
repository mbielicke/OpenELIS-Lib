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
package org.openelis.gwt.widget.tree;

import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class represents the View of the table widget. It contains the logic for
 * displaying and styling a table on the screen.
 * 
 * @author tschmidt
 * 
 */
public class TreeView extends Composite implements ScrollHandler, MouseWheelHandler {
    
    public boolean loaded;
    public enum VerticalScroll {NEVER,ALWAYS,NEEDED};
    
    public class CellView extends ScrollPanel implements HasMouseWheelHandlers {

        private AbsolutePanel ap = new AbsolutePanel();
        
        public CellView() {
            super.setWidget(ap);
        }
        
        public void setScrollWidth(String width){
            ap.setWidth(width);
        }
        
        public void setWidget(final Widget wid){
            ap.clear();
            ap.add(wid);
        }
        
        public void setHeight(String height) {
            super.setHeight(height);
            ap.setHeight(height);
        }

		public HandlerRegistration addMouseWheelHandler(
				MouseWheelHandler handler) {
			return addDomHandler(handler,MouseWheelEvent.getType());
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
    public TreeHeaderBar header = null;
    public FlexTable rows = new FlexTable();
    public int left = 0;
    public int top = 0;
    public String[] headers;
    public Label[] hLabels;
    private String title = "";
    public static String widgetStyle = "TreeWidget";
    public static String cellStyle = "TreeCell";
    public static String rowStyle = "TreeRow";
    public static String headerStyle = "Header";
    public static String headerCellStyle = "HeaderCell";
    public static String tableStyle = "Tree";
    public static String selectedStyle = "Selected";
    public static String navLinks = "NavLinks";
    public static String disabledStyle = "Disabled";
    protected HorizontalPanel navPanel = new HorizontalPanel();
    private VerticalPanel vp = new VerticalPanel(); 
    public String width;
    public int height;
    public TreeWidget controller = null;
    public HTML prevNav;
    public HTML nextNav;
    public VerticalScroll showScroll;

    
    public TreeView(TreeWidget controller, VerticalScroll showScroll) {
        this.controller = controller;
        this.showScroll = showScroll;
        initWidget(vp);
        if(controller.title != null && !controller.title.equals("")){
            titleLabel.setText(controller.title);
            titlePanel.add(titleLabel);
            titlePanel.addStyleName("TitlePanel");
        }
        if(controller.showHeader){
            header = new TreeHeaderBar();
            header.init(controller);
            headerView.add(header);
        }
        cellView.setWidget(table);
        DOM.setStyleAttribute(headerView.getElement(), "overflow", "hidden");
        cellView.addScrollHandler(this);
        AbsolutePanel tspacer = new AbsolutePanel();
        tspacer.setStyleName("TableSpacer");
        if(controller.title != null && !controller.title.equals("")){
        	vp.add(titlePanel);
            vp.setCellWidth(titlePanel, "100%");
        }
        if(controller.showRows) {
        	if(header != null)
        		ft.setWidget(0,1,headerView);
            ft.setWidget(1,0,rows);
            ft.getFlexCellFormatter().setVerticalAlignment(1, 0, HasAlignment.ALIGN_TOP);
            ft.setWidget(1,1,cellView);
            ft.setWidget(1, 2, scrollBar);
            ft.getFlexCellFormatter().setHorizontalAlignment(1, 2, HasHorizontalAlignment.ALIGN_LEFT);
            ft.getFlexCellFormatter().setVerticalAlignment(1,2,HasAlignment.ALIGN_TOP);
            //if(headers != null)
              //  ft.getFlexCellFormatter().addStyleName(0,2, "tableheader");
        }else{
        	if(header != null){
        		ft.setWidget(0,0,headerView);
                ft.setWidget(1,0,cellView);
                if(showScroll != VerticalScroll.NEVER){
                    ft.setWidget(1,1,scrollBar);
                    ft.getFlexCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
                    ft.getFlexCellFormatter().setVerticalAlignment(1,1,HasAlignment.ALIGN_TOP);
                //    if(showScroll == VerticalScroll.ALWAYS)
                  //      ft.getFlexCellFormatter().addStyleName(0, 1, "tableheader");
                }
                ft.getFlexCellFormatter().setVerticalAlignment(1,0,HasAlignment.ALIGN_TOP);
 
            }else{
                ft.setWidget(0,0,cellView);
                if(showScroll != VerticalScroll.NEVER){
                    ft.setWidget(0, 1, scrollBar);
                    ft.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
                    ft.getFlexCellFormatter().setVerticalAlignment(0,1,HasAlignment.ALIGN_TOP);
                }
            }
        }
        
        ft.getFlexCellFormatter().addStyleName(0, 0, "tableheader");
        
        vp.add(ft);
        ft.setCellPadding(0);
        ft.setCellSpacing(0);
        table.setCellSpacing(0);
        table.addStyleName(tableStyle);
        DOM.setStyleAttribute(table.getElement(), "background", "white");
        cellView.setWidget(table);
        ft.setCellSpacing(0);
        scrollBar.setWidth("18px");
        scrollBar.setStyleName("TableVertScroll");
        scrollBar.addScrollHandler(this);
        AbsolutePanel ap = new AbsolutePanel();
        DOM.setStyleAttribute(scrollBar.getElement(), "overflowX", "hidden");
        if(showScroll == VerticalScroll.NEEDED)
            DOM.setStyleAttribute(scrollBar.getElement(), "display", "none");
        DOM.setStyleAttribute(cellView.getElement(),"overflowY","hidden");
        scrollBar.setWidget(ap);
        cellView.addMouseWheelHandler(this);
        table.addClickHandler(controller);
        scrollBar.setAlwaysShowScrollBars(true);
        DeferredCommand.addCommand(new Command() {
           public void execute() {
               titlePanel.setWidth("100%");
           }
        });
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
        if(!width.equals("auto"))
        	cellView.setWidth(width);
        else{

        }
        headerView.setWidth(width);
        titlePanel.setWidth(width);
        titleLabel.setWidth(width);
        vp.setCellWidth(titlePanel, width);
        rows.setWidth("25px");
        if(controller.title != null && !controller.title.equals("")){
        	titleLabel.setWidth(width);
        	titlePanel.setWidth(width);
        }
    }

    public void setCell(Widget widget, int row, int col) {
        table.setWidget(row, col, widget);
        if (widget instanceof FocusWidget)
            ((FocusWidget)widget).setFocus(true);
    }

    public void setTitle(String title) {
        this.title = title;
        titleLabel.setText(title);
    }

    public void setScrollPosition(int scrollPos) {
        scrollBar.setScrollPosition(scrollPos);
        onScroll(null);
    }
    
	public void onScroll(ScrollEvent event) {
        if(event == null || event.getSource() == scrollBar ) {
            if(top != scrollBar.getScrollPosition()){
                controller.renderer.scrollLoad(scrollBar.getScrollPosition());
                top = scrollBar.getScrollPosition();
            }
            return;
        }
        if(event.getSource() == cellView){
            if(left != cellView.getHorizontalScrollPosition()){
                headerView.setWidgetPosition(header, -cellView.getHorizontalScrollPosition(), 0);
                left = cellView.getHorizontalScrollPosition();
            }
        }		
	}
    
    public void setScrollHeight(int scrollHeight) {
        try {
            scrollBar.getWidget().setHeight(scrollHeight+"px");
            if(showScroll == showScroll.NEEDED){
                if(scrollHeight > (this.height+1)){
                    DOM.setStyleAttribute(scrollBar.getElement(), "display", "block");
                    if(header != null)
                        ft.getFlexCellFormatter().addStyleName(0, 1, "Header");
                }else{ 
                    DOM.setStyleAttribute(scrollBar.getElement(),"display","none");
                    if(header != null && ft.isCellPresent(0, 1))
                        ft.getFlexCellFormatter().removeStyleName(0,1,"Header");
                }
            }
        }catch(Exception e){
            Window.alert("set scroll height"+e.getMessage());
        }
    }

	public void onMouseWheel(MouseWheelEvent event) {
        int pos = scrollBar.getScrollPosition();
        int delta = event.getDeltaY();
        if(delta < 0 && delta > - 18)
            delta = -18;
        if(delta > 0 && delta < 18)
            delta = 18;
        scrollBar.setScrollPosition(pos + delta);
		
	}
    
}
