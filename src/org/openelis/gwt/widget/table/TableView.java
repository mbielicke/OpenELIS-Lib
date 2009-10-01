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
package org.openelis.gwt.widget.table;

import org.openelis.gwt.screen.UIUtil;
import org.openelis.gwt.widget.deprecated.IconContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.XMLParser;

/**
 * This class represents the View of the table widget. It contains the logic for
 * displaying and styling a table on the screen.
 * 
 * @author tschmidt
 * 
 */
public class TableView extends Composite implements ScrollHandler, MouseWheelHandler {
    
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
    public TableHeaderMenuBar header = null;
    public FlexTable rows = new FlexTable();
    public int left = 0;
    public int top = 0;
    public String[] headers;
    public Label[] hLabels;
    private String title = "";
    public static String widgetStyle = "TableWidget";
    public static String cellStyle = "TableCell";
    public static String dropdownCellStyle = "dropdowncell";
    public static String rowStyle = "TableRow";
    public static String headerStyle = "Header";
    public static String headerCellStyle = "HeaderCell";
    public static String tableStyle = "Table";
    public static String selectedStyle = "Selected";
    public static String navLinks = "NavLinks";
    public static String disabledStyle = "Disabled";
    protected HorizontalPanel navPanel = new HorizontalPanel();
    private VerticalPanel vp = new VerticalPanel(); 
    public String width;
    public int height;
    public TableWidget controller = null;
    public HTML prevNav;
    public HTML nextNav;
    public VerticalScroll showScroll;

    
    public TableView(TableWidget control, VerticalScroll showScroll) {
        this.controller = control;
        this.showScroll = showScroll;
        initWidget(vp);
        if(controller.title != null && !controller.title.equals("")){
            titleLabel.setText(controller.title);
            titlePanel.add(titleLabel);
            titlePanel.addStyleName("TitlePanel");
            if(controller.addIcon) {
            	IconContainer add = (IconContainer)UIUtil.createWidget(XMLParser.parse("<icon style='AddRowIcon'/>").getDocumentElement());
            	add.addClickHandler(new ClickHandler() {
            		public void onClick(ClickEvent event) {
            			controller.addRow();
            		}
            	});
            	titlePanel.add(add);
            	titlePanel.setCellWidth(add, "16px");
            }
            if(controller.deleteIcon) {
            	IconContainer delete = (IconContainer)UIUtil.createWidget(XMLParser.parse("<icon style='DeleteRowIcon'/>").getDocumentElement());
            	delete.addClickHandler(new ClickHandler() {
            		public void onClick(ClickEvent event) {
            			if(controller.activeRow > -1) {
            				controller.deleteRow(controller.activeRow);
            			}
            		}
            	});
            	titlePanel.add(delete);
            	titlePanel.setCellWidth(delete, "16px");
            }
        }
        if(controller.showHeader){
            header = GWT.create(TableHeaderMenuBar.class);
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
            //    ft.getFlexCellFormatter().addStyleName(0,2, "tableheader");
        }else{
            if(header != null){
                ft.setWidget(0,0,headerView);
                ft.setWidget(1,0,cellView);
                if(showScroll != VerticalScroll.NEVER){
                    ft.setWidget(1,1,scrollBar);
                    ft.getFlexCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
                    ft.getFlexCellFormatter().setVerticalAlignment(1,1,HasAlignment.ALIGN_TOP);
              //      if(showScroll == VerticalScroll.ALWAYS)
                //        ft.getFlexCellFormatter().addStyleName(0, 1, "tableheader");
                }
                ft.getFlexCellFormatter().setVerticalAlignment(1,0,HasAlignment.ALIGN_TOP);
                ft.getFlexCellFormatter().addStyleName(0, 0, "tableheader");
 
            }else{
                ft.setWidget(0,0,cellView);
                if(showScroll != VerticalScroll.NEVER){
                    ft.setWidget(0, 1, scrollBar);
                    ft.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
                    ft.getFlexCellFormatter().setVerticalAlignment(0,1,HasAlignment.ALIGN_TOP);
                }
            }
           
        }
        vp.add(ft);
        ft.setCellPadding(0);
        ft.setCellSpacing(0);
        table.setCellSpacing(0);
        table.setCellPadding(0);
        table.addStyleName(tableStyle);
        cellView.setWidget(table);
        ft.setCellSpacing(0);
        scrollBar.setWidth("16px");
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
        if(headers != null)
        	headerView.setHeight("20px");

    }

    public void setWidth(String width) {
        this.width = width.trim();
        cellView.setWidth(width);
        if(headers != null)
        	headerView.setWidth(width);
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
        prevNav.addClickHandler(controller);
        
        nextNav = new HTML("");
        nextNav.addStyleName("nextNavIndex");
        nextNav.addClickHandler(controller);
        
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
                    nav.addClickHandler(controller);
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

    public void setScrollPosition(int scrollPos) {
        scrollBar.setScrollPosition(scrollPos);
        onScroll(null);
    }
    
    public void setScrollHeight(int scrollHeight) {
        try {
            scrollBar.getWidget().setHeight(scrollHeight+"px");
            if(showScroll == showScroll.NEEDED){
                if(scrollHeight > (this.height+1) && controller.numRows() > controller.maxRows){
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

	public void onScroll(ScrollEvent event) {
        if(event == null || event.getSource() == scrollBar ) {
            if(top != scrollBar.getScrollPosition()){
                controller.renderer.scrollLoad(scrollBar.getScrollPosition());
                top = scrollBar.getScrollPosition();
            }
        }
        if(event.getSource() == cellView){
            if(left != cellView.getHorizontalScrollPosition()){
                headerView.setWidgetPosition(header, -cellView.getHorizontalScrollPosition(), 0);
                left = cellView.getHorizontalScrollPosition();
            }
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
