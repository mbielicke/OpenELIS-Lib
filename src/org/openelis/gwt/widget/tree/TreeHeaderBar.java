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

public class TreeHeaderBar extends Composite implements MouseMoveHandler, 
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
    protected ArrayList<TreeColumn> columns;
    protected boolean resizing;
    protected int startx;
    protected int resizeColumn1 = -1;
    protected int tableCol1 = -1;
    protected int resizeColumn2 = -1;
    protected int tableCol2 = -1;
    protected TreeWidget controller;
    public boolean doFilter;
    public boolean doQuery;
    private FlexTable bar = new FlexTable(); 
    
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
    
    
    public void init(TreeWidget controller) {
        
        setStyleName("topHeaderBar");
        this.controller = controller;
        this.columns = controller.headers;
        bar.setCellSpacing(1);
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
            
            BarContainer barc = new BarContainer(); 
            barc.addMouseDownHandler(this);
             barc.addMouseUpHandler(this);
             barc.addMouseMoveHandler(this);
            AbsolutePanel ap3 = new AbsolutePanel();
            ap3.addStyleName("HeaderBarPad");
            barc.add(ap3);
            header.add(barc);
            header.setCellWidth(bar, "3px");
            
            headerLabel.setWordWrap(false);
            header.add(headerLabel);
            header.setCellWidth(headerLabel, "100%");
            header.add(headerLabel);
            MenuItem menuItem = null;
            if(column.getSortable() || column.getFilterable()){
                AbsolutePanel wid = new AbsolutePanel();
                wid.setHeight("18px");
                wid.setWidth("16px");
                menuItem = new MenuItem(wid);
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
            }
            hMenus.add(menuItem);
            bar.setWidget(0,columns.indexOf(column),header);
            bar.getCellFormatter().setHeight(0, columns.indexOf(column), "20px");
            
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
            int colB =  columns.get(tableCol2).getCurrentWidth() - (sender.getAbsoluteLeft() - startx);
            if((event.getX() < 0 && colA <= 16) || (event.getX() > 0 && colB <= 16)) 
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
                columns.get(tableCol2).setCurrentWidth( columns.get(tableCol2).getCurrentWidth() - (sender.getAbsoluteLeft() - startx));
                RootPanel.get().remove(sender);
                resizing = false;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                    	for(int i = 0; i <  headers.size(); i++) {
                    		HorizontalPanel header = headers.get(i);
                    		header.setWidth(columns.get(i).currentWidth+"px");
                    		hLabels.get(i).setWidth((columns.get(i).currentWidth-20)+"px");
                    	}
                        for (int j = 0; j < controller.view.table.getRowCount(); j++) {
                            for (int i = 0; i < columns.size(); i++) {
                                controller.view.table.getFlexCellFormatter().setWidth(j, i, (columns.get(i).getCurrentWidth()) +  "px");
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
            	for(int i = 0; i <  headers.size(); i++) {
            		HorizontalPanel header = headers.get(i);
            		header.setWidth(columns.get(i).currentWidth+"px");
            		hLabels.get(i).setWidth((columns.get(i).currentWidth-23)+"px");
            	}
            }
        });
        
    }



    public void onAction(ActionEvent<MenuItem.Action> event) {
        if(event.getAction() == MenuItem.Action.OPENING) {         
            final int index = hMenus.indexOf((MenuItem)event.getData());
            final TreeColumn col = (TreeColumn)controller.headers.get(index);
            ((MenuItem)event.getSource()).menuItemsPanel.clear();
            if(col.getSortable()) {
                MenuItem item = new MenuItem("",new Label("Sort Up"),"");
                item.setStyleName("topHeaderRowContainer");
                ((MenuItem)event.getData()).menuItemsPanel.add(item);
                item.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        controller.sort(index, TreeSorterInt.SortDirection.UP);
                    }
                });
                item = new MenuItem("",new Label("Sort Down"),"");
                item.setStyleName("topHeaderRowContainer");
                ((MenuItem)event.getData()).menuItemsPanel.add(item);
                item.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        controller.sort(index, TreeSorterInt.SortDirection.DOWN);
                    }
                });
            }
        }
    }

    public void onClose(CloseEvent<PopupPanel> event) {
    	
    }

    public void onClick(ClickEvent event) {

    }
    

}
