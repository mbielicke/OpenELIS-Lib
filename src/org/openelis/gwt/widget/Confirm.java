package org.openelis.gwt.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;

public class Confirm extends Composite implements HasSelectionHandlers<Integer>, ClickHandler, NativePreviewHandler {

    DecoratorPanel dp = new DecoratorPanel();
    AbsolutePanel modalGlass;
    AbsolutePanel modalPanel;
    int active = -1;
    HorizontalPanel bp;
    HandlerRegistration keyHandler;
    public enum Type {WARN,ERROR,QUESTION};
    
    public Confirm(Type type, String message, String... buttons) {
    	VerticalPanel vp = new VerticalPanel();
    	AbsolutePanel ap = new AbsolutePanel();
    	HorizontalPanel hp = new HorizontalPanel();
    	if(type == Type.WARN)
    		ap.setStyleName("warnIcon");
    	else if(type == Type.ERROR)
    		ap.setStyleName("errorIcon");
    	else if(type == Type.QUESTION)
    		ap.setStyleName("questionIcon");
    	hp.add(ap);
    	hp.setCellVerticalAlignment(ap, HasAlignment.ALIGN_MIDDLE);
    	Label lb = new Label();
    	lb.setText(message);
    	lb.setWordWrap(true);
    	lb.setStyleName("noteBodyText");
    	hp.add(lb);
    	vp.add(hp);
    	createButtons(buttons);
    	vp.add(bp);
    	vp.setCellHorizontalAlignment(bp, HasAlignment.ALIGN_CENTER);
    	dp.add(vp);
    	dp.setStyleName("ErrorWindow");
    	dp.setVisible(false);
    	initWidget(dp);
    }
    
    private void hide() {
    	removeFromParent();
    	modalPanel.removeFromParent();
    	modalGlass.removeFromParent();
    	keyHandler.removeHandler();
    }
    
    public void show() {
    	setWidth("400px");
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
    }
    
    private void size() {
    	dp.setVisible(true);
    	if(bp.getOffsetWidth() > 400)
    		setWidth((bp.getOffsetWidth()+50)+"px");
    	modalPanel.setWidgetPosition(this, Window.getClientWidth()/2 - this.getOffsetWidth()/2,Window.getClientHeight()/2 - this.getOffsetHeight()/2);
    	
    }
    
    private Widget createButtons(String[] buttons) {
    	bp = new HorizontalPanel();
    	bp.setStyleName("ButtonPanelContainer");
    	bp.setStyleName("Form");
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
		if(event.getTypeInt() == Event.ONKEYUP){
			if(event.getNativeEvent().getKeyCode() == KeyboardHandler.KEY_TAB){
				((AppButton)bp.getWidget(active)).blur();
				active++;
				if(active == bp.getWidgetCount())
					active = 0;
				((AppButton)bp.getWidget(active)).setFocus();
			}
			if(event.getNativeEvent().getKeyCode() == KeyboardHandler.KEY_ENTER) {
				SelectionEvent.fire(this, active);
				hide();
			}
		}
		if(event.getTypeInt() != Event.ONCLICK)
			event.cancel();
	}
    
}
