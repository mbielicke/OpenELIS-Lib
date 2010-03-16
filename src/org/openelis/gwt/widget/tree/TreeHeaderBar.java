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

import java.util.ArrayList;

import org.openelis.gwt.event.ActionEvent;
import org.openelis.gwt.event.ActionHandler;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.MenuItem;
import org.openelis.gwt.widget.MenuPanel;
import org.openelis.gwt.widget.table.event.SortEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class TreeHeaderBar extends Composite implements MouseMoveHandler, 
															 MouseDownHandler, 
															 MouseUpHandler,  
															 CloseHandler<PopupPanel>, 
															 ActionHandler<MenuItem.Action>,
															 ClickHandler,
															 HasMouseOutHandlers {
    
    public static String headerStyle = "Header";
    public static String headerCellStyle = "HeaderCell";
    public ArrayList<Label> hLabels = new ArrayList<Label>();
    public ArrayList<MenuItem> hMenus = new ArrayList<MenuItem>();
    public ArrayList<HorizontalPanel> headers = new ArrayList<HorizontalPanel>();
    protected ArrayList<TreeColumn> columns;
    protected boolean resizing;
    protected int startx;
    protected int resizeColumn1 = -1;
    protected int tableCol1 = -1;
    protected TreeWidget controller;
    public boolean doFilter;
    public boolean doQuery;
    private FlexTable bar = new FlexTable(); 
    private TreeHeaderBar source = this;
	protected MenuItem menuItem = null;
	protected PopupPanel pop = new PopupPanel(true);
    
    public TreeHeaderBar() {
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
    																HasClickHandlers,
    																ClickHandler,
    																CloseHandler<PopupPanel> {


    	public void onMouseOver(MouseOverEvent event) {
    		int index = headers.indexOf(this);
    		if(hMenus.get(index) == null){
    			pop.hide();
    			menuItem = null;
    			return;
    		}
    		if(menuItem == hMenus.get(index))
    			return;
    		menuItem = hMenus.get(index);
    		int left = this.getAbsoluteLeft()+this.getOffsetWidth()-19;
    		if(left > controller.view.cellView.getAbsoluteLeft()+controller.view.cellView.getOffsetWidth())
    			left = controller.view.cellView.getAbsoluteLeft()+controller.view.cellView.getOffsetWidth()-19;
    		pop.addCloseHandler(new CloseHandler<PopupPanel>() {
    			public void onClose(CloseEvent<PopupPanel> event) {
    				menuItem = null;
    			}
    		});
    		pop.setWidget(menuItem);
    		pop.setPopupPosition(left, this.getAbsoluteTop());
    		pop.show();
    	}

    	/**
    	 * Catches mouses Events for resizing columns.
    	 */
    	public void onMouseOut(MouseOutEvent event) {
    		//Window.alert(""+event.getRelativeY(getElement()));
    		if(pop.isShowing() && !menuItem.popShowing && ((event.getRelativeX(((Widget)event.getSource()).getElement()) <= 0 || event.getRelativeX(((Widget)event.getSource()).getElement()) >= getOffsetWidth())
    				|| (event.getRelativeY(((Widget)event.getSource()).getElement()) <= 0 || event.getRelativeY(((Widget)event.getSource()).getElement()) >= getOffsetHeight()))){
    			//RootPanel.get().remove(menuItem);
    			pop.hide();
    			menuItem = null;
    		}
    	}

    	public void onClose(CloseEvent<PopupPanel> event) {
    		if(menuItem != null) {
    			//RootPanel.get().remove(menuItem);
    			pop.hide();
    			menuItem = null;
    		}
    	}

    	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
    		return addDomHandler(handler, MouseOverEvent.getType());
    	}

    	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
    		return addDomHandler(handler, MouseOutEvent.getType());
    	}

    	public HandlerRegistration addClickHandler(ClickHandler handler){
    		return addDomHandler(handler,ClickEvent.getType());
    	}

    	public void onClick(ClickEvent event) {
    		//menuItem.onClick(event);

    	}
    }
    
    
    public void init(TreeWidget controller) {
        
        setStyleName("topHeaderBar");
        this.controller = controller;
        this.columns = controller.headers;
        bar.setCellSpacing(0);
        bar.setCellPadding(0);
        for(TreeColumn column : columns) {
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
            MenuItem menuItem = null;
            if(column.getSortable()){
                AbsolutePanel wid = new AbsolutePanel();
                wid.setHeight("18px");
                wid.setWidth("16px");
                menuItem = new MenuItem(wid);
                menuItem.menuItemsPanel = new MenuPanel("vertical");
                menuItem.menuItemsPanel.setStyleName("topHeaderContainer");
                menuItem.addActionHandler(this);
                menuItem.addMouseOutHandler(header);
                header.addMouseOverHandler(header);
                header.addMouseOutHandler(header);
                menuItem.pop.addCloseHandler(header);
                menuItem.enable(true);
                menuItem.setStyleName("HeaderDropdownButton");
            }
            hMenus.add(menuItem);
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
        addMouseOutHandler(new MouseOutHandler() {
        	public void onMouseOut(MouseOutEvent event) {
        		if(pop.isShowing()){
        			if((event.getRelativeX(getElement()) <= 0 || event.getRelativeX(getElement()) >= getOffsetWidth())
        			    || (event.getRelativeY(getElement()) <= 0 || event.getRelativeY(getElement()) >= getOffsetHeight())){
        				pop.hide();
        				menuItem = null;
        			}
        		}
        	}
        });
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
    	if(pop.isShowing()){
    		pop.hide();
    		menuItem =  null;
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
    	DOM.setStyleAttribute(bar.getElement(),"zIndex", "1000");
           
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
            	pad = 5;
            if((event.getX() < 0 && (colA - pad) <= columns.get(tableCol1).getMinWidth())) 
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
           
            int colWidth =  columns.get(tableCol1).getCurrentWidth() + (sender.getAbsoluteLeft() - startx);
            int scrollWidth = 0;
            for(int i = 0; i < headers.size(); i++) {
            	if(tableCol1 != i)
            		scrollWidth += columns.get(i).getCurrentWidth();
            }
            if(scrollWidth + colWidth < controller.getTreeWidth())
            	colWidth = controller.getTreeWidth() - scrollWidth;
            columns.get(tableCol1).setCurrentWidth(colWidth);
            resizing = false;
            RootPanel.get().remove(sender);
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                	sizeHeader();
                    for (int j = 0; j < controller.view.table.getRowCount(); j++) {
                        for (int i = 0; i < columns.size(); i++) {
                            controller.view.table.getFlexCellFormatter().setWidth(j, i, (columns.get(i).getCurrentWidth()) +  "px");
                            if(!(controller.columns.get(controller.getRow(j).leafType).get(i).getColumnWidget() instanceof CheckBox))
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
                    if(columns.get(i).sortable)
                    	pad = 5;
            		if(columns.get(i).currentWidth - pad < columns.get(i).getMinWidth()){
            			hLabels.get(i).setWidth(columns.get(i).getMinWidth()+"px");
            		}else{
            			hLabels.get(i).setWidth((columns.get(i).currentWidth-pad)+"px");
            		}
            	}
            	controller.view.cellView.setScrollWidth((scrollWidth+(columns.size()*3))+"px");
            }
        });
        
    }



    public void onAction(ActionEvent<MenuItem.Action> event) {
        if(event.getAction() == MenuItem.Action.OPENING) {         
            final int index = hMenus.indexOf((MenuItem)event.getData());
            final TreeColumn col = (TreeColumn)controller.headers.get(index);
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
            }
        }
    }

    public void onClose(CloseEvent<PopupPanel> event) {
    	
    }

    public void onClick(ClickEvent event) {

    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler,MouseOutEvent.getType());
	}
    

}
