package org.openelis.gwt.widget;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
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
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Confirm extends FocusPanel implements HasSelectionHandlers<Integer>, ClickHandler, NativePreviewHandler {

    DecoratorPanel dp = new DecoratorPanel();
    AbsolutePanel modalGlass;
    AbsolutePanel modalPanel;
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
    
    public Confirm(Type type, String caption, String message, String... buttons) {
    	VerticalPanel vp = new VerticalPanel();
    	cap.name = caption;
    	cap.setStyleName("ConfirmCaption");
    	AbsolutePanel ap = new AbsolutePanel();
    	HorizontalPanel hp = new HorizontalPanel();
    	if(type == Type.WARN){
    		ap.setStyleName("warnIcon");
    		if(caption == null || caption.equals(""))
    			cap.name = "Warning";
    	}else if(type == Type.ERROR){
    		ap.setStyleName("errorIcon");
    		if(caption == null || caption.equals(""))
    			cap.name = "Error";
    	}else if(type == Type.QUESTION){
    		ap.setStyleName("questionIcon");
    		if(caption == null || caption.equals(""))
    			cap.name = "Question";
    	}
   	    
        Label winLabel = new Label();
        winLabel.setStyleName("ConfirmCaptionLabel");
        winLabel.setText(cap.name);
        cap.add(winLabel);
        cap.setWidth("100%");
    	vp.add(cap);
    	hp.add(ap);
    	hp.setCellVerticalAlignment(ap, HasAlignment.ALIGN_MIDDLE);
    	Label lb = new Label();
    	lb.setText(message);
    	lb.setWordWrap(true);
    	lb.setStyleName("Form ScreenLabel");
    	hp.add(lb);
    	vp.add(hp);
    	createButtons(buttons);
    	vp.add(bp);
    	vp.setCellHorizontalAlignment(bp, HasAlignment.ALIGN_CENTER);
    	dp.add(vp);
    	dp.setStyleName("ErrorWindow");
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
    	keyHandler.removeHandler();
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
        if(active > -1){
        	((AppButton)bp.getWidget(active)).blur();
        }
        ((AppButton)bp.getWidget(0)).setFocus();
        active = 0;
        keyHandler = Event.addNativePreviewHandler(this);
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
    	if(bp.getOffsetWidth() > width)
    		setWidth((bp.getOffsetWidth()+50)+"px");
    	modalPanel.setWidgetPosition(this, Window.getClientWidth()/2 - this.getOffsetWidth()/2,Window.getClientHeight()/2 - this.getOffsetHeight()/2);
    	
    }
    
    private Widget createButtons(String[] buttons) {
    	bp = new HorizontalPanel();
    	for(int i = 0; i < buttons.length; i++) {
    		AppButton ab = new AppButton();
    		ab.action = String.valueOf(i);
    		Label bl = new Label(buttons[i]);
    		bl.setStyleName("ScreenLabel");
    		ab.setWidget(bl);
    		ab.setStyleName("Button");
    		ab.enable(true);
    		bp.add(ab);
    		ab.addClickHandler(this);
    	}
    	return bp;
    
    }

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Integer> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}
	
	public void onClick(ClickEvent event) {
		SelectionEvent.fire(this,new Integer(((AppButton)event.getSource()).action));
		hide();
	}

	public void onPreviewNativeEvent(NativePreviewEvent event) {
		if(event.getTypeInt() == Event.ONKEYDOWN){
			if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB){
				((AppButton)bp.getWidget(active)).blur();
				active++;
				if(active == bp.getWidgetCount())
					active = 0;
				((AppButton)bp.getWidget(active)).setFocus();
			}
			if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				SelectionEvent.fire(this, active);
				hide();
			}
		}
		//if(event.getTypeInt() != Event.ONCLICK)
			//event.cancel();
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
