package org.openelis.gwt.widget;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
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
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Popup extends FocusPanel implements ClickHandler {

    DecoratorPanel dp = new DecoratorPanel();
    AbsolutePanel modalGlass;
    AbsolutePanel modalPanel;
    FocusPanel close = new FocusPanel();
    int active = -1;
    HorizontalPanel bp;
    HandlerRegistration keyHandler;
    public enum Type {WARN,ERROR,QUESTION};
    private int width = 400;
    private int height = -1;
    private PickupDragController dragController;
    private AbsolutePositionDropController dropController;
    Caption cap = new Caption();
    
    private class Caption extends AbsolutePanel implements HasAllMouseHandlers { 

    	public String name;

    	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
    		return addDomHandler(handler, MouseDownEvent.getType());
    	}

    	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
    		return addDomHandler(handler,MouseUpEvent.getType());
    	}

    	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
    		return addDomHandler(handler,MouseOutEvent.getType());
    	}

    	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
    		return addDomHandler(handler,MouseOverEvent.getType());
    	}

    	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
    		return addDomHandler(handler,MouseMoveEvent.getType());
    	}

    	public HandlerRegistration addMouseWheelHandler(
    			MouseWheelHandler handler) {
    		return addDomHandler(handler,MouseWheelEvent.getType());
    	}
    }
    
    public Popup(String caption, Widget content) {
    	VerticalPanel vp = new VerticalPanel();
    	cap.name = caption;
    	cap.setStyleName("ConfirmCaption");
    	HorizontalPanel hp = new HorizontalPanel();   	    
        Label winLabel = new Label();
        winLabel.setStyleName("ConfirmCaptionLabel");
        winLabel.setText(cap.name);
        cap.add(winLabel);
        cap.setWidth("100%");
        hp.add(cap);
        hp.setCellWidth(cap, "100%");
        close.addClickHandler(this);
        close.setStyleName("CloseButton");
        hp.add(close);
    	vp.add(hp);
    	vp.add(content);
    	vp.setCellVerticalAlignment(content, HasAlignment.ALIGN_MIDDLE);	
    	dp.add(vp);
    	dp.setStyleName("ConfirmWindow");
    	dp.setVisible(false);
    	setWidget(dp);
    }
    
    private void hide() {
    	dragController.unregisterDropController(dropController);
    	dragController.makeNotDraggable(this);
    	dragController = null;
    	dropController = null;
    	removeFromParent();
    	modalPanel.removeFromParent();
    	modalGlass.removeFromParent();
    	dp.setVisible(false);
    }
    
    public void show() {
    	setWidth(width+"px");
    	if(height > -1)
    		setHeight(height+"px");
        modalGlass = new AbsolutePanel();
        modalGlass.setStyleName("GlassPanel");
        modalGlass.setHeight(Window.getClientHeight()+"px");
        modalGlass.setWidth(Window.getClientWidth()+"px");
        RootPanel.get().add(modalGlass, 0, 0);
        DOM.setStyleAttribute(modalGlass.getElement(), "zIndex","1000");
        modalPanel = new AbsolutePanel();
        modalPanel.setStyleName("ModalPanel");
        modalPanel.setHeight(Window.getClientHeight()+"px");
        modalPanel.setWidth(Window.getClientWidth()+"px");
        modalPanel.add(this,Window.getClientWidth()/2 - 400/2,Window.getClientHeight()/2 - this.getOffsetHeight()/2);
        RootPanel.get().add(modalPanel,0,0); 
        DOM.setStyleAttribute(modalPanel.getElement(),"zIndex","1001");
        final Widget wid = this;
        DeferredCommand.addCommand(new Command() {
        	public void execute() {
               size();
        	}
        });
        //if(dragController == null) {
        	dragController = new PickupDragController(modalPanel,true);
        	dropController = new AbsolutePositionDropController(modalPanel);
        	dragController.registerDropController(dropController);
        	dragController.makeDraggable(this,cap);
        //}
    }
    
    private void size() {
    	dp.setVisible(true);
    	modalPanel.setWidgetPosition(this, Window.getClientWidth()/2 - this.getOffsetWidth()/2,Window.getClientHeight()/2 - this.getOffsetHeight()/2);
    	
    }
	
	public void onClick(ClickEvent event) {
		hide();
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}
