package org.openelis.gwt.widget.redesign.table;

import org.openelis.gwt.event.BeforeDropEvent;
import org.openelis.gwt.event.BeforeDropHandler;
import org.openelis.gwt.event.DropEnterEvent;
import org.openelis.gwt.event.DropEnterHandler;
import org.openelis.gwt.event.DropEvent;
import org.openelis.gwt.event.DropHandler;
import org.openelis.gwt.event.HasBeforeDropHandlers;
import org.openelis.gwt.event.HasDropEnterHandlers;
import org.openelis.gwt.event.HasDropHandlers;
import org.openelis.gwt.event.DropEnterEvent.DropPosition;
import org.openelis.gwt.widget.DragItem;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
* Allows one or more table rows to be dropped into an existing table.
*/
public final class TableDropController extends SimpleDropController implements HasBeforeDropHandlers<DragItem>, HasDropHandlers<DragItem>,HasDropEnterHandlers<DragItem>, NativePreviewHandler {

	protected static final String CSS_DROP_POSITIONER = "DropPositioner";

	protected Table table;

	protected boolean dropping;

	protected boolean validDrop;

	protected Widget positioner;
	
	protected DropPosition dropPos;
	
	protected HandlerManager handlerManager;

	protected int targetRow;
	protected int targetModelIndex;
	
	protected Timer scroll,startScroll;
	protected int scrollRows;
	protected boolean scrolling;
	protected HandlerRegistration scrollEndHandler;

	public TableDropController(Table tbl) {
		super(tbl.view.flexTable);
		this.table = tbl;
		
		startScroll = new Timer() {
		    public void run() {
		        checkScroll(targetRow);
		    }
		};
		
		scroll = new Timer() {
		    
		    public void run() {
	            table.scrollBy(scrollRows);
                checkScroll(targetRow);
		    }
		    
		};
		
	   positioner = new AbsolutePanel();
	   positioner.addStyleName(CSS_DROP_POSITIONER);
	   DOM.setStyleAttribute(positioner.getElement(), "zIndex", "1000");
	}
	
	

	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
	    BeforeDropEvent<DragItem> event;
	    
	    scroll.cancel();
	    
		if(!validDrop) 
			throw new VetoDragException();
		
		event = BeforeDropEvent.fire(this, (DragItem)context.draggable, table.getModel().get(targetRow));
		if(event != null && event.isCancelled()){
			positioner.removeFromParent();
			((TableDragController)context.dragController).dropIndicator.setStyleName("DragStatus NoDrop");
			throw new VetoDragException();
		}
		
		super.onPreviewDrop(context);
	}


	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);
		positioner = newPositioner(context);
	}

	@Override
	public void onDrop(DragContext context) {
	    DragItem dragItem;
	    
	    dragItem = (DragItem)context.draggable;
	    
	    dropping = false;
        super.onDrop(context);
        DropEvent.fire(this, dragItem);
	}

	@Override
	public void onLeave(DragContext context) {
	    startScroll.cancel();
	    positioner.removeFromParent();
		((TableDragController)context.dragController).dropIndicator.setStyleName("DragStatus NoDrop");
		super.onLeave(context);
	}


	@Override
	public void onMove(DragContext context) {
	    int adjY,rowTop,rowBot,diff;
	    double perc;
	    DropEnterEvent<DragItem> event;
	    
		super.onMove(context);
		
		startScroll.cancel();
		//scroll.cancel();
		
		adjY = context.mouseY - table.view.flexTable.getAbsoluteTop();
		
		targetRow = adjY / table.view.rowHeight;
		targetModelIndex = table.view.firstVisibleRow + targetRow;
	
		validDrop = true;
		if(table.getRowCount() > 0){
			rowTop = targetRow * table.view.rowHeight;
			rowBot = rowTop + table.view.rowHeight;
			diff = adjY - rowTop;
			perc = ((double)diff)/((double)table.view.rowHeight);

			if(perc <= 0.5)
				dropPos = DropPosition.ABOVE;
			else 
				dropPos = DropPosition.BELOW;

			event = DropEnterEvent.fire(this, (DragItem)context.draggable, table.getModel().get(targetRow == -1 ? 0 : targetRow),dropPos);
			if(event != null && event.isCancelled())
				validDrop = false;

			if(validDrop) {
				if(dropPos == DropPosition.BELOW) {
					context.boundaryPanel.add(positioner, table.view.flexTable.getAbsoluteLeft(), rowTop + table.view.flexTable.getAbsoluteTop()
							+ (targetRow == -1 ? 0 : table.view.rowHeight));
				}else{
					context.boundaryPanel.add(positioner, table.view.flexTable.getAbsoluteLeft(), rowTop + table.view.flexTable.getAbsoluteTop());
				}
				((TableDragController)context.dragController).dropIndicator.setStyleName("DragStatus Drop");
			}else{
				((TableDragController)context.dragController).dropIndicator.setStyleName("DragStatus NoDrop");
				positioner.removeFromParent();
			}
			
			if((targetRow == 0 && dropPos == DropPosition.ABOVE) || (targetRow == table.getVisibleRows() -1 && dropPos == DropPosition.BELOW))
			    startScroll.schedule(500);
			else
			    scroll.cancel();
		}else{
			RootPanel.get().add(positioner,table.view.flexTable.getAbsoluteLeft(),table.view.flexTable.getAbsoluteTop());
		}

	}

	public boolean checkScroll(final int targetRow) {
		if(table.getRowCount() < table.getVisibleRows())
			return false;
		
		scrollRows = 0;
		
		if(targetRow <= 0 && targetModelIndex > 0) 
		   scrollRows = -1;
		else if(targetRow >= table.getVisibleRows() -1 && targetModelIndex < table.getRowCount() -1)
		   scrollRows = 1;
		
		if(scrollRows != 0) {
		    scrollEndHandler = Event.addNativePreviewHandler(this);
			scroll.schedule(200);
            return true;
		}
		
		scroll.cancel();
		
		return false;  
	}
	
	public int getDropIndex() {
	    return targetModelIndex;
	}
	
	public DropPosition getDropPosition() {
	    return dropPos;
	}

	Widget newPositioner(DragContext context) {
	    positioner.setPixelSize(table.getWidthWithoutScrollbar(), 1);
	    return positioner;
	}

	protected void stopScroll() {
	    scroll.cancel();
	}


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

	public HandlerRegistration addBeforeDropHandler(
			BeforeDropHandler<DragItem> handler) {
		return addHandler(handler,BeforeDropEvent.getType());
	}

	public HandlerRegistration addDropHandler(DropHandler<DragItem> handler) {
		return addHandler(handler,DropEvent.getType());
	}

	public HandlerRegistration addDropEnterHandler(
			DropEnterHandler<DragItem> handler) {
		return addHandler(handler, DropEnterEvent.getType());
	}



    public void onPreviewNativeEvent(NativePreviewEvent event) {
        if(event.getTypeInt() == Event.ONMOUSEUP) {
            scroll.cancel();
            scrollEndHandler.removeHandler();
        }
    }

}
