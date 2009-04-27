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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;

public class MenuItem extends SimplePanel implements MouseListener, ClickListener, PopupListener, SourcesClickEvents, SourcesMouseEvents, SourcesCommandEvents {
    
    public MenuPanel parent;
    public boolean cursorOn;
    public Node popupNode;
    public PopupPanel pop = new PopupPanel(true,false);
    public boolean popClosed;
    public boolean popShowing;
    public MouseListenerCollection mouseListeners = new MouseListenerCollection();
    public ClickListenerCollection clickListeners = new ClickListenerCollection();
    public CommandListenerCollection commandListeners = new CommandListenerCollection();
    public MenuPanel child;
    public MenuPanel menuItemsPanel;
    public Widget wid;
    public enum PopPosition {BELOW,SIDE};
    public PopPosition popPosition = PopPosition.BELOW;
    public enum Action {OPENING,CLOSING};
    public AbsolutePanel iconPanel = new AbsolutePanel();
    public String objClass;
    public Object[] args;
    public String key;
    public String label;
    
    @Override
    public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEUP:
            case Event.ONMOUSEMOVE:
            case Event.ONMOUSEOVER:
            case Event.ONMOUSEOUT:
                if (mouseListeners != null) {
                    mouseListeners.fireMouseEvent(this, event);
                }
                break;
            case Event.ONCLICK:
                if (clickListeners != null) {
                    clickListeners.fireClick(this);
                }
                break;
        }
        super.onBrowserEvent(event);
    }
    
    public MenuItem() {
        
    }
    
    public MenuItem(String icon, String labelText, String description) {
        init(icon,labelText,description);
        label = labelText;
    }
    
    public void init(String icon, String labelText, String description) {
        Label label = new Label(labelText);
        label.setStyleName("topMenuItemTitle");
        label.addStyleName("locked");
        setWidget(create(icon,label,description));
        enable(true);
        this.label = labelText;
    }
    
    public MenuItem(String icon, Widget wid, String description) {
        init(icon,wid,description);
    }
    
    public void init(String icon, Widget wid, String description) {
        setWidget(create(icon,wid,description));
        enable(true);
    }
    
    public MenuItem(Widget wid) {
        init(wid);
    }
    
    public void init(Widget wid) {
        setWidget(wid);
        enable(true);
    }
    
    public Widget getWidget() {
        return super.getWidget();
    }
    
    public Widget create(String icon, Widget wid, String descriptionText) {
        FlexTable table = new FlexTable();
        table.setStyleName("TopMenuRowContainer");
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

    public void addPopPanel(MenuPanel panel) {
        menuItemsPanel = panel;
    }
    
    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        if(parent != null)
            parent.itemEnter(this);
        cursorOn = true;
        addStyleName("Hover");
    }

    public void onMouseLeave(Widget sender) {
        if(parent != null)
            parent.itemLeave(this);
        cursorOn = false;
        removeStyleName("Hover");
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onClick(Widget sender) {
        if(menuItemsPanel != null){
            if(popClosed)
                popClosed = false;
            else
                createPopup();
            mouseListeners.fireMouseEnter(sender);
            return;
        }
        if(parent != null && parent.getParent() instanceof PopupPanel)
            ((PopupPanel)parent.getParent()).hide();
        mouseListeners.fireMouseLeave(sender);
    }

    public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
        commandListeners.fireCommand(Action.CLOSING, this);
        if(cursorOn)
            popClosed = true;
        if(DOM.getElementProperty(sender.getElement(), "closeAll").equals("true")){
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
        commandListeners.fireCommand(Action.OPENING, this);
        if(pop.getWidget() == null) {
            //pop = new PopupPanel(true,false);
            //ScreenMenuPanel mp = (ScreenMenuPanel)ScreenWidget.loadWidget(popupNode, screen);
            //use the menuItemsPanel so you can disable certain rows on the fly
            pop.setWidget(menuItemsPanel);
        
            pop.addPopupListener(this);
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
        if(enabled){
           // removeClickListener(this);
            addClickListener(this);
            sinkEvents(Event.ONCLICK);
            //removeMouseListener(this);
            addMouseListener(this);
            sinkEvents(Event.MOUSEEVENTS);
            getWidget().removeStyleName("disabled");
        }else{
           // removeClickListener(this);
           // removeMouseListener(this);
            unsinkEvents(Event.ONCLICK);
            unsinkEvents(Event.MOUSEEVENTS);
            getWidget().addStyleName("disabled");
        }
    }

    public void addClickListener(ClickListener listener) {
        if(clickListeners == null)
            clickListeners = new ClickListenerCollection();
        clickListeners.add(listener);
        
    }

    public void removeClickListener(ClickListener listener) {
        if(clickListeners != null)
            clickListeners.remove(listener);
    }

    public void addMouseListener(MouseListener listener) {
        if(mouseListeners == null)
            mouseListeners = new MouseListenerCollection();
        mouseListeners.add(listener);
    }

    public void removeMouseListener(MouseListener listener) {
        if(mouseListeners != null)
            mouseListeners.remove(listener);
        
    }


    public void addCommandListener(CommandListener listener) {
        if(commandListeners == null){
            commandListeners = new CommandListenerCollection();
        }
        commandListeners.add(listener);
    }


    public void removeCommandListener(CommandListener listener) {
        if(commandListeners != null)
            commandListeners.remove(listener);
    }
}
