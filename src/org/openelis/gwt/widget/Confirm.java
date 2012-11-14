package org.openelis.gwt.widget;

import org.openelis.gwt.constants.Constants;
import org.openelis.gwt.resources.ConfirmCSS;
import org.openelis.gwt.resources.OpenELISResources;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class will display a modal window with a message and buttons for 
 * the user to confirm
 * @author tschmidt
 *
 */
public class Confirm extends Window implements HasSelectionHandlers<Integer>, ClickHandler, NativePreviewHandler {

    int active = -1;
    HorizontalPanel bp;
    HandlerRegistration keyHandler;
    public enum Type {WARN,ERROR,QUESTION,BUSY};
    private int width = 400,height = -1,top = -1, left = -1;
    private PickupDragController dragController;
    private AbsolutePositionDropController dropController;
    protected AbsolutePanel modalGlass,modalPanel;
    ConfirmCSS css; 
        
    public Confirm(Type type, String caption, String message, String... buttons) {
    	super();
    	
    	css = OpenELISResources.INSTANCE.confirm();
    	css.ensureInjected();
    	
    	
    	VerticalPanel vp = new VerticalPanel();
    	AbsolutePanel ap = new AbsolutePanel();
    	HorizontalPanel hp = new HorizontalPanel();
    	
    	switch(type) {
    		case WARN : {
    			ap.setStyleName(css.largeWarnIcon());
    			if(caption == null || caption.equals(""))
    				label.setText(Constants.get().warning());
    			break;
    		}
    		case ERROR : {
        		ap.setStyleName(css.largeErrorIcon());
        		if(caption == null || caption.equals(""))
        			label.setText(Constants.get().error());
        		break;
        	}
    		case QUESTION : {
        		ap.setStyleName(css.largeQuestionIcon());
        		if(caption == null || caption.equals(""))
        			label.setText(Constants.get().question());
        		break;
        	}
    		case BUSY : {
    			ap.setStyleName(css.spinnerIcon());
    			if(caption == null || caption.equals(""))
    				label.setText(Constants.get().busy());
    			break;
    		}
    	}
   	    
    	hp.add(ap);
    	hp.setCellVerticalAlignment(ap, HasAlignment.ALIGN_MIDDLE);
    	Label<String> lb = new Label<String>();
    	lb.setText(message);
    	lb.setWordWrap(true);
    	lb.setStyleName("Form ScreenLabel");
    	hp.add(lb);
    	vp.add(hp);
    	if(buttons != null && buttons[0] != null ){
    		createButtons(buttons);
    		vp.add(bp);
    		vp.setCellHorizontalAlignment(bp, HasAlignment.ALIGN_CENTER);
    	}
    	setContent(vp);
    }
    
    public void hide() {
    	dragController.unregisterDropController(dropController);
    	dragController.makeNotDraggable(this);
    	dragController = null;
    	dropController = null;
    	removeFromParent();
    	unlockWindow();
    	keyHandler.removeHandler();
    	setVisible(false);
    }
    
    public void show(int left, int top) {
    	this.left = left;
    	this.top = top;
    	show();
    }
    
    public void show() {
    	setWidth(width+"px");
    	if(height > -1)
    		setHeight(height+"px");
        modalGlass = new AbsolutePanel();
        modalGlass.setStyleName(css.GlassPanel());
        modalGlass.setHeight(com.google.gwt.user.client.Window.getClientHeight()+"px");
        modalGlass.setWidth(com.google.gwt.user.client.Window.getClientWidth()+"px");
        RootPanel.get().add(modalGlass, 0, 0);
        DOM.setStyleAttribute(modalGlass.getElement(), "zIndex","1000");
        modalPanel = new AbsolutePanel();
        modalPanel.setStyleName(css.ModalPanel());
        modalPanel.setHeight(com.google.gwt.user.client.Window.getClientHeight()+"px");
        modalPanel.setWidth(com.google.gwt.user.client.Window.getClientWidth()+"px");
        modalPanel.add(this, left > -1 ? left : com.google.gwt.user.client.Window.getClientWidth()/2 - 400/2,
        		             top > -1 ? top : com.google.gwt.user.client.Window.getClientHeight()/2 - this.getOffsetHeight()/2);
        RootPanel.get().add(modalPanel,0,0); 
        DOM.setStyleAttribute(modalPanel.getElement(),"zIndex","1001");

        keyHandler = Event.addNativePreviewHandler(this);
    	
    		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {			
    			public void execute() {
    				size();
    				if(bp != null ){
    					if(active > -1){
    						((Button)bp.getWidget(active)).setFocus(false);
    					}
    					((Button)bp.getWidget(0)).setFocus(true);
    					active = 0;
    				}
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
    	setVisible(true);
    	if(bp != null) {
    		if(bp.getOffsetWidth() > width)
    			setWidth((bp.getOffsetWidth()+50)+"px");
    	}
    	modalPanel.setWidgetPosition(this, left > -1 ? left : com.google.gwt.user.client.Window.getClientWidth()/2 - this.getOffsetWidth()/2,
    									   top > -1 ? top :  com.google.gwt.user.client.Window.getClientHeight()/2 - this.getOffsetHeight()/2);
    	
    }
    
    private Widget createButtons(String[] buttons) {
    	bp = new HorizontalPanel();
    	for(int i = 0; i < buttons.length; i++) {
    		Button ab = new Button("",buttons[i]);
    		ab.setAction(String.valueOf(i));
    		ab.setEnabled(true);
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
		int clicked;
		
		clicked = new Integer(((Button)event.getSource()).getAction()).intValue();
		SelectionEvent.fire(this,new Integer(((Button)event.getSource()).getAction()));
		hide();
        if(active > -1)
           	((Button)bp.getWidget(active)).setFocus(true);
        active = -1;
        ((Button)bp.getWidget(clicked)).setFocus(false);
           
		
	}

	public void onPreviewNativeEvent(NativePreviewEvent event) {
		if(event.getTypeInt() == Event.ONKEYDOWN){
			if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB){
				((Button)bp.getWidget(active)).setFocus(false);
				active++;
				if(active == bp.getWidgetCount())
					active = 0;
				((Button)bp.getWidget(active)).setFocus(true);
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
