package org.openelis.gwt.widget.tree;

import java.util.HashMap;

import org.openelis.gwt.event.BeforeDragStartEvent;
import org.openelis.gwt.event.BeforeDragStartHandler;
import org.openelis.gwt.event.DragManager;
import org.openelis.gwt.event.DragStartEvent;
import org.openelis.gwt.event.DragStartHandler;
import org.openelis.gwt.event.HasBeforeDragStartHandlers;
import org.openelis.gwt.event.HasDragStartHandlers;
import org.openelis.gwt.widget.table.TableRow;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetArea;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;


public class TreeDragController extends PickupDragController implements HasBeforeDragStartHandlers<TreeRow>, HasDragStartHandlers<TreeRow> {
        
	public DragManager manager;
	private String PRIVATE_CSS_PROXY = "dragdrop-proxy";
	private boolean enabled;
	public Widget proxy;
	public String dragText;
	public AbsolutePanel dropIndicator;
	private HashMap<Widget, SavedWidgetInfo> savedWidgetInfoMap;
	
    private static class SavedWidgetInfo {

        /**
         * The initial draggable index for indexed panel parents.
         */
        int initialDraggableIndex;

        /**
         * Initial draggable CSS margin.
         */
        String initialDraggableMargin;

        /**
         * Initial draggable parent widget.
         */
        Widget initialDraggableParent;

        /**
         * Initial location for absolute panel parents.
         */
        Location initialDraggableParentLocation;
      }
    
    public TreeDragController(AbsolutePanel boundaryPanel){
        super(boundaryPanel,false);
        setBehaviorDragProxy(true);
        setBehaviorDragStartSensitivity(5);
    }
    
    @Override
    public void previewDragStart() throws VetoDragException {
		if(getHandlerCount(BeforeDragStartEvent.getType()) > 0 ){
			BeforeDragStartEvent<TreeRow> event = BeforeDragStartEvent.fire(this, (TreeRow)context.draggable);
			if(event != null && event.isCancelled()){
				throw new VetoDragException();
			}
			if(event != null)
				proxy = event.getProxy();
		}
		if(enabled)
			((TreeRow)context.draggable).setDragValues();
		else
			throw new VetoDragException();
        super.previewDragStart();
    }
    
    @Override
    public void previewDragEnd() throws VetoDragException {
        super.previewDragEnd();
    }
    
    @Override
    public void dragStart() {
        ((TreeRow)context.draggable).dragItem.enabled = false;
        ((TreeRow)context.draggable).addStyleName(TreeView.disabledStyle);
        super.dragStart();
        ((TreeRow)context.draggable).removeStyleName("dragdrop-dragging");
		if(getHandlerCount(DragStartEvent.getType()) > 0){
			DragStartEvent.fire(this, (TreeRow)context.draggable);
		}
    }
   
    @Override
    public void dragMove() {
        context.desiredDraggableX = context.mouseX;
        context.desiredDraggableY = context.mouseY;
        super.dragMove();
      }
    
    @Override
    public void dragEnd() {
        //context.draggable.removeStyleName("TreeHighlighted");
        ((TreeRow)context.draggable).dragItem.enabled = true;
		if(((TreeRow)context.draggable).controller.isRowDrawn(((TreeRow)context.draggable).dragModelIndex))
			((TreeRow)context.draggable).controller.renderer.loadRow(((TreeRow)context.draggable).dragModelIndex);
        proxy = null;
        super.dragEnd();
    }
    
	protected Widget newDragProxy(DragContext context) {
	    AbsolutePanel ap, container;
	    WidgetArea draggableArea, widgetArea;
        DecoratorPanel dp;
        HorizontalPanel hp;
        
	    container = new AbsolutePanel();
		container.getElement().getStyle().setProperty("overflow", "visible");

		draggableArea = new WidgetArea(context.draggable, null);
		for (Widget widget : context.selectedWidgets) {
			widgetArea = new WidgetArea(widget, null);
			dp = new DecoratorPanel();
			
			dp.setStyleName("ErrorWindow");
			hp = new HorizontalPanel();
			
			dropIndicator = new AbsolutePanel();
			dropIndicator.setStyleName("DragStatus NoDrop");
			hp.add(dropIndicator);
			ap = new AbsolutePanel();
			hp.add(ap);
			
			if(proxy != null){
				ap.add(proxy);
				ap.setWidth("100%");
			}else{
				proxy = ((TreeRow)context.draggable).getDragProxy();
				ap.add(proxy);
			}
			hp.setStyleName("DragProxy");
			dp.add(hp);
			container.add(dp, widgetArea.getLeft() - draggableArea.getLeft(), widgetArea.getTop()
					- draggableArea.getTop());
		}

		return container;
	}
    
    
    /**
     * Save the selected widgets' current location in case they much
     * be restored due to a cancelled drop.
     * @see #restoreSelectedWidgetsLocation()
     */
    protected void saveSelectedWidgetsLocationAndStyle() {
      savedWidgetInfoMap = new HashMap<Widget, SavedWidgetInfo>();
      for (Widget widget : context.selectedWidgets) {
        SavedWidgetInfo info = new SavedWidgetInfo();
        info.initialDraggableParent = widget.getParent();

        //info.initialDraggableIndex = (() info.initialDraggableParent).getWidgetIndex(widget);
        info.initialDraggableMargin = DOM.getStyleAttribute(widget.getElement(), "margin");
        widget.getElement().getStyle().setProperty("margin", "0px");
        savedWidgetInfoMap.put(widget, info);
      }
    }
    
    public void setEnable(boolean enable) {
        enabled = enable;
    }
    
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
    
	public HandlerRegistration addBeforeStartHandler(
			BeforeDragStartHandler<TreeRow> handler) {
		return addHandler(handler,BeforeDragStartEvent.getType());
	}

	public HandlerRegistration addStartHandler(
			DragStartHandler<TreeRow> handler) {
		return addHandler(handler,DragStartEvent.getType());
	}

}
