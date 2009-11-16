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

import java.util.ArrayList;

import org.openelis.gwt.event.ActionEvent;
import org.openelis.gwt.widget.table.event.SortEvent;
import org.openelis.gwt.event.ActionHandler;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.MenuItem;
import org.openelis.gwt.widget.MenuPanel;
import org.openelis.gwt.widget.QueryFieldUtil;
import org.openelis.gwt.widget.TextBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;

public class TableHeaderBar extends Composite implements MouseMoveHandler, 
															 MouseDownHandler, 
															 MouseUpHandler,  
															 CloseHandler<PopupPanel>, 
															 ActionHandler<MenuItem.Action>,
															 ClickHandler {
    
    public static String headerStyle = "Header";
    public static String headerCellStyle = "HeaderCell";
    public ArrayList<Label> hLabels = new ArrayList<Label>();
    public ArrayList<MenuItem> hMenus = new ArrayList<MenuItem>();
    public ArrayList<HorizontalPanel> headers = new ArrayList<HorizontalPanel>();
    protected ArrayList<TableColumn> columns;
    protected boolean resizing;
    protected int startx;
    protected int resizeColumn1 = -1;
    protected int tableCol1 = -1;
    protected TableWidget controller;
    public boolean doFilter;
    public boolean doQuery;
    private FlexTable bar = new FlexTable(); 
    
    public TableHeaderBar() {
    	initWidget(bar);
    }
    
    public class BarContainer extends AbsolutePanel implements HasMouseDownHandlers,
    														   HasMouseUpHandlers, 
    														   HasMouseMoveHandlers {

    	public BarContainer() {
    		sinkEvents(Event.MOUSEEVENTS);
    	}
    	
		public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
			return addDomHandler(handler,MouseDownEvent.getType());
		}

		public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
			return addDomHandler(handler, MouseUpEvent.getType());
		}

		public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
			return addDomHandler(handler, MouseMoveEvent.getType());
		}

    }
    
    public class ListenContainer extends HorizontalPanel implements HasMouseOverHandlers,
    																HasMouseOutHandlers,
    																MouseOverHandler,
    																MouseOutHandler,
    																CloseHandler<PopupPanel> {
        

        public void onMouseOver(MouseOverEvent event) {
            getWidget(2).setStyleName("HeaderDropdownButton");
        }

        /**
         * Catches mouses Events for resizing columns.
         */
        public void onMouseOut(MouseOutEvent event) {
            if(!((MenuItem)getWidget(2)).popShowing)
                getWidget(2).removeStyleName("HeaderDropdownButton");
        }

        public void onClose(CloseEvent<PopupPanel> event) {
            getWidget(2).removeStyleName("HeaderDropdownButton");
        }

		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());
		}

		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return addDomHandler(handler, MouseOutEvent.getType());
		}
    }
    
    
    public void init(TableWidget controller) {
        
        setStyleName("topHeaderBar");
        this.controller = controller;
        this.columns = controller.columns;
        bar.setCellSpacing(0);
        bar.setCellPadding(0);
        for(TableColumn column : columns) {
        	ListenContainer header = new ListenContainer();
        	header.setSpacing(0);
        	headers.add(header);
            Label headerLabel = new Label(column.getHeader());
            hLabels.add(headerLabel);

            DOM.setStyleAttribute(header.getElement(), "overflow", "hidden");
            DOM.setStyleAttribute(header.getElement(), "overflowX", "hidden");
            headerLabel.addStyleName("HeaderLabel");
            DOM.setStyleAttribute(headerLabel.getElement(),"overflowX","hidden");
            DOM.setStyleAttribute(headerLabel.getElement(), "overflow", "hidden");

            
            headerLabel.setWordWrap(false);
            header.add(headerLabel);
            header.setCellWidth(headerLabel, "100%");
            header.add(headerLabel);
            if(column.getSortable() || column.getFilterable()){
                AbsolutePanel wid = new AbsolutePanel();
                wid.setHeight("18px");
                wid.setWidth("16px");
                MenuItem menuItem = new MenuItem(wid);
                menuItem.menuItemsPanel = new MenuPanel("vertical");
                menuItem.menuItemsPanel.setStyleName("topHeaderContainer");
                menuItem.addActionHandler(this);
                header.add(menuItem);
                header.setCellHorizontalAlignment(menuItem, HasAlignment.ALIGN_RIGHT);
                header.addMouseOverHandler(header);
                header.addMouseOutHandler(header);
                header.setCellVerticalAlignment(menuItem, HasAlignment.ALIGN_TOP);
                menuItem.pop.addCloseHandler(header);
                menuItem.enable(true);
                hMenus.add(menuItem);
            }

        	BarContainer barc = new BarContainer(); 
        	barc.addMouseDownHandler(this);
        	barc.addMouseUpHandler(this);
        	barc.addMouseMoveHandler(this);
        	AbsolutePanel ap3 = new AbsolutePanel();
        	ap3.addStyleName("HeaderBarPad");
        	barc.add(ap3);
        	header.add(barc);

            bar.setWidget(0,columns.indexOf(column),header);
            bar.getCellFormatter().setHeight(0, columns.indexOf(column), "18px");
           	bar.getCellFormatter().setStyleName(0,columns.indexOf(column), "Header");

            
        }
        sizeHeader();
    }
    
    public void setHeaders(String[] headers) {
        for(int i = 0; i < headers.length; i++){
            hLabels.get(i).setText(headers[i]);
        }
    }
    

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseDown(MouseDownEvent event) {
    	Widget sender = (Widget)event.getSource();
    	// TODO Auto-generated method stub
    	resizing = true;
    	startx = sender.getAbsoluteLeft();
    	resizeColumn1 = headers.indexOf(sender.getParent());
    	tableCol1 = resizeColumn1;
    	if(columns.get(tableCol1).getFixedWidth()){
    		resizing = false;
    		resizeColumn1 = -1;
    		tableCol1 = -1;
    		return;
    	}  
    	FocusPanel bar = new FocusPanel();
    	bar.addMouseUpHandler(this);
    	bar.addMouseDownHandler(this);
    	bar.addMouseMoveHandler(this);
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
    public void onMouseMove(MouseMoveEvent event) {
    	Widget sender = (Widget)event.getSource();
        if(resizing) {
            int colA =  columns.get(tableCol1).getCurrentWidth() + (sender.getAbsoluteLeft() - startx);
            int pad = 5;
            if(columns.get(tableCol1).sortable || columns.get(tableCol1).filterable)
            	pad = 23;
            if((event.getX() < 0 && (colA-pad) <= 16)) 
            	return;
            DOM.setStyleAttribute(sender.getElement(),"left",(DOM.getAbsoluteLeft(sender.getElement())+(event.getX()))+"px");
        }
    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseUp(MouseUpEvent event) {
        	Widget sender = (Widget)event.getSource();
            if (resizing) {
                DOM.releaseCapture(sender.getElement());
                columns.get(tableCol1).setCurrentWidth( columns.get(tableCol1).getCurrentWidth() + (sender.getAbsoluteLeft() - startx));
                RootPanel.get().remove(sender);
                resizing = false;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                    	sizeHeader();
                        for (int j = 0; j < controller.view.table.getRowCount(); j++) {
                            for (int i = 0; i < columns.size(); i++) {
                                controller.view.table.getFlexCellFormatter().setWidth(j, i, (columns.get(i).getCurrentWidth()) +  "px");
                                if(!(controller.columns.get(i).getColumnWidget() instanceof CheckBox))
                                	controller.view.table.getWidget(j, i).setWidth((columns.get(i).getCurrentWidth()) + "px");
                            }
                        }
                    }
                });
            }
        
    }
    
    public void sizeHeader() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
            	int scrollWidth = 0;
            	for(int i = 0; i <  headers.size(); i++) {
            		scrollWidth += columns.get(i).getCurrentWidth();
            		HorizontalPanel header = headers.get(i);
            		header.setWidth((columns.get(i).currentWidth+2)+"px");
            		int pad = 5;
                    if(columns.get(i).sortable || columns.get(i).filterable)
                    	pad = 23;
            		if(columns.get(i).currentWidth - pad < 15){
            			hLabels.get(i).setWidth("15px");
            		}else{
            			hLabels.get(i).setWidth((columns.get(i).currentWidth-pad)+"px");
            		}
            	}
            	controller.view.cellView.setScrollWidth((scrollWidth+(controller.columns.size()*3))+"px");
            }
        });
        
    }



    public void onAction(ActionEvent<MenuItem.Action> event) {
        if(event.getAction() == MenuItem.Action.OPENING) {         
            final int index = hMenus.indexOf((MenuItem)event.getData());
            final TableColumn col = (TableColumn)controller.columns.get(index);
            if(col.filterDisplayed) 
                ((MenuItem)event.getSource()).menuItemsPanel.clear();
            if(col.getSortable()) {
                MenuItem item = new MenuItem("Ascending",new Label("Ascending"),"");
                item.setStyleName("topHeaderRowContainer");
                ((MenuItem)event.getData()).menuItemsPanel.add(item);
                item.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        controller.sort(index, SortEvent.SortDirection.ASCENDING);
                    }
                });
                item.enable(true);
                item = new MenuItem("Descending",new Label("Descending"),"");
                item.setStyleName("topHeaderRowContainer");
                ((MenuItem)event.getData()).menuItemsPanel.add(item);
                item.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        controller.sort(index, SortEvent.SortDirection.DESCENDING);
                    }
                });
                item.enable(true);
                if(col.filterable){
                    ((MenuItem)event.getData()).menuItemsPanel.add(new HTML("<hr/>"));
                }
            }
            if (col.filterable) {
                MenuItem filterMenu = new MenuItem("",new Label("Filters"),"");
                filterMenu.popPosition = MenuItem.PopPosition.BESIDE;
                filterMenu.menuItemsPanel = new MenuPanel("vertical");
                filterMenu.menuItemsPanel.setStyleName("topHeaderContainer");
                ArrayList<Filter> filters = col.getFilters();
                for (Filter filter : filters) {
                    String filtered = "Unchecked";
                    if(filter.filtered)
                        filtered = "Checked";
                    MenuItem item = new MenuItem(filtered,filter.display,"");
                    item.addClickHandler(this);
                    item.setStyleName("topHeaderRowContainer");
                    //item.addMouseListener((MouseListener)ClassFactory.forName("HoverListener"));
                    filterMenu.menuItemsPanel.add(item);
                    item.args = new Object[] {filter,index};
                } 
                filterMenu.pop.addCloseHandler(this);
                ((MenuItem)event.getData()).menuItemsPanel.add(filterMenu);
            }
            if(col.queryable) {
                final TextBox entryText = new TextBox();
                String checkState = "Unchecked";
                if(col.query != null){
                	checkState = "Checked";
                	entryText.setText(col.query.queryString);
                }	 
                final MenuItem item = new MenuItem(checkState,entryText,"");

                ((MenuItem)event.getData()).menuItemsPanel.add(item);
                ((MenuItem)event.getData()).pop.addCloseHandler(this);
              	item.unsinkEvents(Event.ONCLICK);
              	final TableColumn column = col;
                entryText.addKeyUpHandler(new KeyUpHandler() {
                	
                    public void onKeyUp(KeyUpEvent event) {
                        if(((TextBox)event.getSource()).getText().length() > 0){
                            doQuery = true;
                            item.iconPanel.setStyleName("Checked");
                            //col.query = ((TextBox)event.getSource()).getText();
                        }else{
                            doQuery = false;
                            doFilter = true;
                            item.iconPanel.setStyleName("Unchecked");
                            col.query = null;
                        }
                        if(event.getNativeKeyCode() == KeyboardHandler.KEY_ENTER){
                        	if(doQuery) {
                        		query(col,entryText.getText());
                        	}else{
                        		col.query = null;
                        		applyQueryFilter();
                        	}
                        	 if(item.parent != null && item.parent.getParent() instanceof PopupPanel)
                                 ((PopupPanel)item.parent.getParent()).hide();
                        		
                        }
                        
                    }
                    
                });
              
 
            }
            col.filterDisplayed = true;
        }
        
    }

    public void onClose(CloseEvent<PopupPanel> event) {
    	
    }

    public void onClick(ClickEvent event) {
    	Widget sender = (Widget)event.getSource();
        if(sender instanceof MenuItem) {
            doFilter = true;
            Filter filter = (Filter)((MenuItem)sender).args[0];
            TableColumn col = (TableColumn)controller.columns.get((((Integer)((MenuItem)sender).args[1])));
            filter.filtered = !filter.filtered;
            if(filter.filtered)
                ((MenuItem)sender).iconPanel.setStyleName("Checked");
            else
                ((MenuItem)sender).iconPanel.setStyleName("Unchecked");
            ArrayList<Filter> filters = col.getFilterList();
            if(filter.obj == null){
                for (Filter filt : filters) {
                    if (filt.filtered) {
                        filt.filtered = false;
                    }
                }
                for(int i = 1; i < ((MenuItem)sender).parent.menuItems.size(); i++)
                    ((MenuItem)sender).parent.menuItems.get(i).iconPanel.setStyleName("Unchecked");
                filters.get(0).filtered = true;
                ((MenuItem)sender).parent.menuItems.get(0).iconPanel.setStyleName("Checked");
            }else if(filter.filtered){
                    filters.get(0).filtered = false;
                    ((MenuItem)sender).parent.menuItems.get(0).iconPanel.setStyleName("Unchecked");
            }else {
            	boolean setAll = true;
                for(Filter filt : filters) {
                    if(filt.filtered){
                        setAll = false;
                        break;
                    }
                }
                if(setAll){
                	filters.get(0).filtered = true;
                	((MenuItem)sender).parent.menuItems.get(0).iconPanel.setStyleName("Checked");
                }
            }
            col.setFilter(filters);
            applyQueryFilter();
            //((MenuItem)sender).onMouseLeave(sender);
        }
        
    }
    
    private void applyQueryFilter() {
        for(TableDataRow row : controller.getData())
        	row.shown = true;
        for(TableColumn column : controller.columns){
            if(((TableColumn)column).query != null )
                ((TableColumn)column).applyQueryFilter();
            else
                column.applyFilter();
        }
        controller.refresh();
    }
    
    public void query(TableColumn col, String query) {
    	QueryFieldUtil qField = new QueryFieldUtil();
    	qField.parse(query);
    	col.query = qField;
    	applyQueryFilter();
    }
    
    public static native String getUserAgent() /*-{
        return navigator.userAgent.toLowerCase();
     }-*/;
    

}
