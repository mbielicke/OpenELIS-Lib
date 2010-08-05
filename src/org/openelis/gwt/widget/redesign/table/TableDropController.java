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

    /**
     * String representing CSS class used for positioner
     */
	protected static final String CSS_DROP_POSITIONER = "DropPositioner";

	/**
	 * Table that this controller is setup for
	 */
	protected Table table;

	/**
	 * Boolean used to determine if the drag is over a valid drop area
	 */
	protected boolean validDrop;

	/**
	 * Widget used to show position of current drop
	 */
	protected Widget positioner;
	
	/**
	 * Enumeration used to specify if the drop is above or below the targetRow
	 */
	protected DropPosition dropPos;
	
	/**
	 * HandlerManager added to this class to be able to fire events
	 */
	protected HandlerManager handlerManager;

	/**
	 * Indexes used to determine the drop row
	 */
	protected int targetRow,targetModelIndex;
	
	/**
	 * Timers used to setup scrolling of table when dragged
	 */
	protected Timer scroll,startScroll;
	
	/**
	 * Number of rows and direction to scroll
	 */
	protected int scrollRows;
	
	/**
	 * Registration of NativeEventPreview so that we can remove when not needed
	 */
	protected HandlerRegistration scrollEndHandler;

	/**
	 * Constructor that takes the Table widget to be used by this controller 
	 * @param tbl
	 */
	public TableDropController(Table tbl) {
	    /* Pass the Drop area to the base class */
		super(tbl.view.flexTable);
		this.table = tbl;
		
		/* Timer used to determine if the user has paused in the correct area to start 
		 * a scroll of the table
		 */
		startScroll = new Timer() {
		    public void run() {
		        checkScroll(targetRow);
		    }
		};
		
		/*
		 * Timer used to keep scrolling the table until the user lets up the mouse button or the 
		 * user drags back into the table and drop target changes
		 */
		scroll = new Timer() {
		    public void run() {
	            table.scrollBy(scrollRows);
                checkScroll(targetRow);
		    }
		    
		};
		
	    /*
	     * Set up the positioner widget used to let the user know where the drop will occurr 
	     */
	    positioner = new AbsolutePanel();
	    positioner.addStyleName(CSS_DROP_POSITIONER);
	    DOM.setStyleAttribute(positioner.getElement(), "zIndex", "1000");
	}
	
	/**
	 * Method overridden to fire a BeforeDropEvent to notify the user of the drop and allow 
	 * them the chance to cancel 
	 */
	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
	    BeforeDropEvent<DragItem> event;
	    
	    scroll.cancel();
	    
		if(!validDrop) 
			throw new VetoDragException();
		
		event = BeforeDropEvent.fire(this, (DragItem)context.draggable, table.getModel().get(targetRow));
		if(event != null && event.isCancelled()){
			positioner.removeFromParent();
			((TableDragController)context.dragController).setDropIndicator(false);
			throw new VetoDragException();
		}
		
		super.onPreviewDrop(context);
	}

	/**
	 * Method Overridden to fire the DropEvent and let the event handler to the drop.
	 */
	@Override
	public void onDrop(DragContext context) {
	    DragItem dragItem;
	    
	    dragItem = (DragItem)context.draggable;
	    
        super.onDrop(context);
        DropEvent.fire(this, dragItem);
	}

	/**
	 * Method overridden to cancel startScroll, remove postioner and set the dragIndicator to no drop 
	 * when the user drags outside of this drop area. 
	 * 
	 */
	@Override
	public void onLeave(DragContext context) {
	    startScroll.cancel();
	    positioner.removeFromParent();
		((TableDragController)context.dragController).setDropIndicator(false);
		super.onLeave(context);
	}


	/**
	 * Method overridden to determine the current drop row that the mouse is currently 
	 * dragged over.
	 */
	@Override
	public void onMove(DragContext context) {
	    int adjY,rowTop,posY;
	    DropEnterEvent<DragItem> event;
	    
		super.onMove(context);
		
		/*
		 * Cancel startScroll since user moved the mouse
		 */
		startScroll.cancel();
		
		/*
		 * mouseY is based on overall window postion, we need to adjust it from 
		 * the top of the flexTable
		 */
		adjY = context.mouseY - table.view.flexTable.getAbsoluteTop();
		
		/*
		 * Calculate the physical row and model indexes
		 */
		targetRow = adjY / table.view.rowHeight;
		targetModelIndex = table.view.firstVisibleRow + targetRow;
	
		/*
		 * Start with assumption of a valid drop
		 */
		validDrop = true;
		if(table.getRowCount() > 0){
		    
			rowTop = targetRow * table.view.rowHeight;

			if(adjY < (rowTop + (table.view.rowHeight/ 2)))
				dropPos = DropPosition.ABOVE;
			else 
				dropPos = DropPosition.BELOW;

			event = DropEnterEvent.fire(this, (DragItem)context.draggable, table.getModel().get(targetRow),dropPos);
			if(event != null && event.isCancelled())
				validDrop = false;

			if(validDrop) {
			    posY = rowTop + table.view.flexTable.getAbsoluteTop();
				if(dropPos == DropPosition.BELOW) 
				    posY += table.view.rowHeight;
				positioner.setPixelSize(table.getWidthWithoutScrollbar(), 1);
				context.boundaryPanel.add(positioner, table.view.flexTable.getAbsoluteLeft(), posY);
				((TableDragController)context.dragController).setDropIndicator(true);
			}else{
				((TableDragController)context.dragController).setDropIndicator(false);
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

	/**
	 * Method to keep scrolling on drag if needed
	 * @param targetRow
	 * @return
	 */
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
	
	/**
	 * Returns the currently index of the targeted row
	 * @return
	 */
	public int getDropIndex() {
	    return targetModelIndex;
	}
	
	/**
	 * Returns if the drop is above or below the target row
	 * @return
	 */
	public DropPosition getDropPosition() {
	    return dropPos;
	}

	/**
	 * Method to stop the table from scrolling when a drag ocurrs above or below the
	 * table.
	 */
	protected void stopScroll() {
	    scroll.cancel();
	}
	
	/**
	 * When the scroll is on and the drag goes outside the drop area then the native preview
	 * is the only way to catch the mouseup and stop the table from scrolling.  This preview 
	 * is set when the scroll starts and is removed by the mouse up of the preview itself so 
	 * it is only in force when needed.
	 */
    public void onPreviewNativeEvent(NativePreviewEvent event) {
        if(event.getTypeInt() == Event.ONMOUSEUP) {
            scroll.cancel();
            scrollEndHandler.removeHandler();
        }
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

}
