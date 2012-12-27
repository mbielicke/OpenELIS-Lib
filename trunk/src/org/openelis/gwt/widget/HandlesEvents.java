package org.openelis.gwt.widget;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class HandlesEvents {

	  private HandlerManager handlerManager;
	  
	  protected final <H extends EventHandler> HandlerRegistration addHandler(
		      final H handler, GwtEvent.Type<H> type) {
		    return ensureHandlers().addHandler(type, handler);
		  }
	  
	  /**
	   * Ensures the existence of the handler manager.
	   * 
	   * @return the handler manager
	   * */
	  HandlerManager ensureHandlers() {
	    return handlerManager == null ? handlerManager = new HandlerManager(this)
	        : handlerManager;
	  }

	  HandlerManager getHandlerManager() {
	    return handlerManager;
	  }
	  
	  public void fireEvent(GwtEvent<?> event) {
	    if (handlerManager != null) {
	      handlerManager.fireEvent(event);
	    }
	  }
	  
	  public int getHandlerCount(GwtEvent.Type<?> type){
		  if(handlerManager == null)
			  return 0;
		  return handlerManager.getHandlerCount(type);
			  
	  }
}
