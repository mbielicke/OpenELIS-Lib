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
package org.openelis.gwt.widget;

import org.openelis.gwt.event.ActionEvent;
import org.openelis.gwt.event.ActionHandler;
import org.openelis.gwt.event.HasActionHandlers;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class MenuItem extends SimplePanel implements MouseOutHandler, MouseOverHandler, ClickHandler, CloseHandler<PopupPanel>, HasClickHandlers, HasMouseOverHandlers, HasMouseOutHandlers, HasActionHandlers<MenuItem.Action> {
    
    public MenuPanel parent;
    public boolean cursorOn;
    public Node popupNode;
    public PopupPanel pop = new PopupPanel(true,false);
    public boolean popClosed;
    public boolean popShowing;
    public MenuPanel child;
    public MenuPanel menuItemsPanel;
    public Widget wid;
    public enum PopPosition {BELOW,BESIDE};
    public PopPosition popPosition = PopPosition.BELOW;
    public enum Action {OPENING,CLOSING};
    public AbsolutePanel iconPanel = new AbsolutePanel();
    private AbsolutePanel openIcon = new AbsolutePanel();
    public String objClass;
    public Object[] args;
    public String key;
    public String label;
    public String icon;
    public String labelText;
    public String description;
    private boolean enabled;

    
    public MenuItem() {
    	
    }
    
    public MenuItem clone() {
    	return new MenuItem(icon,labelText,description);
    }
    
    public MenuItem(String icon, String labelText, String description) {
        init(icon,labelText,description);
        label = labelText;
    }
    
    public void init(String icon, String labelText, String description) {
    	this.icon = icon;
    	this.labelText = labelText;
    	this.description = description;
        Label label = new Label(labelText);
        label.setStyleName("topMenuItemTitle");
        label.addStyleName("locked");
        setWidget(create(icon,label,description));
        addClickHandler(this);
        addMouseOverHandler(this);
        addMouseOutHandler(this);
        this.label = labelText;
    }
    
    public MenuItem(String icon, Widget wid, String description) {
        init(icon,wid,description);
    }
    
    public void init(String icon, Widget wid, String description) {
        setWidget(create(icon,wid,description));
        addClickHandler(this);
        addMouseOverHandler(this);
        addMouseOutHandler(this);
    }
    
    public MenuItem(Widget wid) {
        init(wid);
    }
    
    public void init(Widget wid) {
        setWidget(wid);
        addClickHandler(this);
        addMouseOverHandler(this);
        addMouseOutHandler(this);
    }
    
    public Widget getWidget() {
        return super.getWidget();
    }
    
    public Widget create(String icon, Widget wid, String descriptionText) {
        FlexTable table = new FlexTable();
        table.setStyleName("TopMenuRowContainer");
        iconPanel.setStyleName(icon);
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(wid);
        hp.setCellWidth(wid,"100%");
        hp.add(openIcon);
        hp.setWidth("100%");
        VerticalPanel textPanel = new VerticalPanel();
        //Label label = new Label(labelText);
        wid.setStyleName("topMenuItemTitle");
        wid.addStyleName("locked");
        
        Label description;
        if("EXCEPTION".equals(descriptionText))
            description = new Label();
        else
            description = new Label(descriptionText);
        
        description.setStyleName("topMenuItemDesc");
        table.setWidget(0,0,iconPanel);
        textPanel.add(hp);
        textPanel.setCellWidth(hp, "100%");
        textPanel.add(description);
        textPanel.setWidth("100%");
        table.setWidget(0,1,textPanel);
        table.getFlexCellFormatter().setStyleName(0,0,"topMenuIcon");
        table.getFlexCellFormatter().setStyleName(0,1,"topMenuItemMiddle");
        return table;
    }
    
    public static Widget createDefault(String icon, Widget wid, String descriptionText) {
        FlexTable table = new FlexTable();
        table.setStyleName("TopMenuRowContainer");
        AbsolutePanel iconPanel = new AbsolutePanel();
        iconPanel.setStyleName(icon);
        VerticalPanel textPanel = new VerticalPanel();
        //Label label = new Label(labelText);
        wid.setStyleName("topMenuItemTitle");
        wid.addStyleName("locked");
        
        Label description;
        if("EXCEPTION".equals(descriptionText))
        	description = new Label();
        else
        	description = new Label(descriptionText);
        
        description.setStyleName("topMenuItemDesc");
        table.setWidget(0,0,iconPanel);
        textPanel.add(wid);
        textPanel.add(description);
        table.setWidget(0,1,textPanel);
        table.getFlexCellFormatter().setStyleName(0,0,"topMenuIcon");
        table.getFlexCellFormatter().setStyleName(0,1,"topMenuItemMiddle");
        return table;
    }
    
    public static Widget createTableHeader(String icon, Widget wid) {
        HorizontalPanel hp = new HorizontalPanel();
        //hp.setStyleName("TopHeaderRowContainer");
        hp.add(wid);
        wid.setStyleName("HeaderLabel");
        hp.setCellWidth(wid,"100%");
        AbsolutePanel ap = new AbsolutePanel();
        hp.add(ap);
        hp.setCellWidth(ap,"16px");
        ap.setHeight("18px");
        ap.setWidth("16px");
        hp.setCellHorizontalAlignment(ap, HasAlignment.ALIGN_RIGHT);
        hp.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_CENTER);
        return hp;
    }

    public void setMenuPopup(MenuPanel panel, PopPosition position) {
    	addPopPanel(panel);
    	popPosition = position;
    	if(position == PopPosition.BESIDE)
    		openIcon.setStyleName("MenuArrow");
    }
    
    public void addPopPanel(MenuPanel panel) {
        menuItemsPanel = panel;
    }
    
    public void onMouseOver(MouseOverEvent event) {
        if(parent != null)
            parent.itemEnter(this);
        cursorOn = true;
        addStyleName("Hover");
    }

    public void onMouseOut(MouseOutEvent event) {
        if(parent != null)
            parent.itemLeave(this);
        cursorOn = false;
        removeStyleName("Hover");
        
    }

    public void onClick(ClickEvent event) {
        if(menuItemsPanel != null){
            if(popClosed)
                popClosed = false;
            else
                createPopup();
            
            MouseOverEvent.fireNativeEvent(Document.get().createMouseOverEvent(0,
            																   event.getNativeEvent().getScreenX(), 
            																   event.getNativeEvent().getScreenY(), 
            																   event.getNativeEvent().getClientX(), 
            																   event.getNativeEvent().getClientY(), 
            																   event.getNativeEvent().getCtrlKey(), 
            																   event.getNativeEvent().getAltKey(), 
            																   event.getNativeEvent().getShiftKey(), 
            																   event.getNativeEvent().getMetaKey(), 
            																   event.getNativeEvent().getButton(), 
            																   ((Widget)event.getSource()).getElement()),this);
            return;
        }
        if(parent != null && parent.getParent() instanceof PopupPanel)
            ((PopupPanel)parent.getParent()).hide();
        MouseOutEvent.fireNativeEvent(Document.get().createMouseOutEvent(0,
				   event.getNativeEvent().getScreenX(), 
				   event.getNativeEvent().getScreenY(), 
				   event.getNativeEvent().getClientX(), 
				   event.getNativeEvent().getClientY(), 
				   event.getNativeEvent().getCtrlKey(), 
				   event.getNativeEvent().getAltKey(), 
				   event.getNativeEvent().getShiftKey(), 
				   event.getNativeEvent().getMetaKey(), 
				   event.getNativeEvent().getButton(), 
				   ((Widget)event.getSource()).getElement()),this);
    }

    public void onClose(CloseEvent<PopupPanel> event) {
    	ActionEvent.fire(this, Action.CLOSING, this);
        if(cursorOn)
            popClosed = true;
        if(DOM.getElementProperty(((Widget)event.getSource()).getElement(), "closeAll").equals("true")){
                if(parent != null && parent.getParent() instanceof PopupPanel)
                    ((PopupPanel)parent.getParent()).hide();
                
        }
        if(parent != null) {
            parent.activeItem = null;
            parent.active = false;
        }
        removeStyleName("Selected");
        popShowing = false;
    }
    
    public void createPopup(){
        if(parent != null)
            parent.activeItem = this;
        if(menuItemsPanel == null)
            return;
        ActionEvent.fire(this, Action.OPENING, this);
        if(pop.getWidget() == null) {
            //pop = new PopupPanel(true,false);
            //ScreenMenuPanel mp = (ScreenMenuPanel)ScreenWidget.loadWidget(popupNode, screen);
            //use the menuItemsPanel so you can disable certain rows on the fly
            pop.setWidget(menuItemsPanel);
        
            pop.addCloseHandler(this);
            pop.setStyleName("");
 
        }
        
        //to be able to use the widget on a screen we always need to set the position
        if(popPosition == PopPosition.BELOW)
            pop.setPopupPosition(getAbsoluteLeft()+8, 
                                 getAbsoluteTop()+getOffsetHeight());
        else
            pop.setPopupPosition(getAbsoluteLeft()+getOffsetWidth(),
                                 getAbsoluteTop());
        
        pop.show();
        popShowing = true;
        child = ((MenuPanel)pop.getWidget());
        DOM.setElementProperty(pop.getElement(),"closeAll", "true");
        ((MenuPanel)pop.getWidget()).active = true;
        DeferredCommand.addCommand(new Command() {
            public void execute(){
                ((MenuPanel)pop.getWidget()).setSize(pop.getPopupTop());
            }
        });
        addStyleName("Selected");
    }
    
    public void closePopup() {
        if(pop != null){
            DOM.setElementProperty(pop.getElement(),"closeAll", "false");
            pop.hide();
            popShowing = false;
        }
    }
    
    public void enable(boolean enabled){
    	this.enabled = enabled;
        if(enabled){
        	sinkEvents(Event.ONCLICK);
        	sinkEvents(Event.ONMOUSEOUT);
        	sinkEvents(Event.ONMOUSEOVER);
        	getWidget().removeStyleName("disabled");
        }else{
        	unsinkEvents(Event.ONCLICK);
        	unsinkEvents(Event.MOUSEEVENTS);
        	unsinkEvents(Event.ONMOUSEOVER);
            getWidget().addStyleName("disabled");
        }
        
    }
    
    public boolean isEnabled() {
    	return enabled;
    }

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addHandler(handler,ClickEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addHandler(handler,MouseOverEvent.getType());
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addHandler(handler,MouseOutEvent.getType());
	}

	public HandlerRegistration addActionHandler(ActionHandler<Action> handler) {
		return addHandler(handler,ActionEvent.getType());
	}

}
