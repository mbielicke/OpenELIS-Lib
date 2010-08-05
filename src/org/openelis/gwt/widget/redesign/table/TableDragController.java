package org.openelis.gwt.widget.redesign.table;

import org.openelis.gwt.event.BeforeDragStartEvent;
import org.openelis.gwt.event.BeforeDragStartHandler;
import org.openelis.gwt.event.DragStartEvent;
import org.openelis.gwt.event.DragStartHandler;
import org.openelis.gwt.event.HasBeforeDragStartHandlers;
import org.openelis.gwt.event.HasDragStartHandlers;
import org.openelis.gwt.widget.DragItem;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class TableDragController extends PickupDragController implements HasBeforeDragStartHandlers<DragItem>, HasDragStartHandlers<DragItem> {

	protected String PRIVATE_CSS_PROXY = "dragdrop-proxy";
	protected Widget proxy;
	protected Table table;
    protected AbsolutePanel proxyContainer,dropIndicator;
    protected DecoratorPanel dragContainer;
    protected HorizontalPanel hp;
    protected Label proxyLabel;

	public TableDragController(Table table,AbsolutePanel boundaryPanel){
		super(boundaryPanel,false);
		this.table = table;
		setBehaviorDragProxy(true);
		setBehaviorDragStartSensitivity(5);
	}

	@Override
	public void previewDragStart() throws VetoDragException {
	    BeforeDragStartEvent<DragItem> event;
	    
	    ((DragItem)context.draggable).setIndex(table.view.lastRow);
	    event = BeforeDragStartEvent.fire(this, (DragItem)context.draggable);
	    if(event != null && event.isCancelled())
	        throw new VetoDragException();
	    
	    if(event != null && event.getProxy() != null)
	        proxy = event.getProxy();

	    super.previewDragStart();
	}

	public void dragMove() {
		context.desiredDraggableX = context.mouseX;
		context.desiredDraggableY = context.mouseY;
		super.dragMove();
	}

	@Override
	public void dragStart() {

	    super.dragStart();
		((DragItem)context.draggable).removeStyleName("dragdrop-dragging");

		DragStartEvent.fire(this, (DragItem)context.draggable);
	}

	protected Widget newDragProxy(DragContext context) {
	    DragItem item;

	    if(dragContainer == null) {
            dragContainer = new DecoratorPanel();
			hp = new HorizontalPanel();
			dropIndicator = new AbsolutePanel();
			dropIndicator.setStyleName("NoDrop");
			hp.add(dropIndicator);
			proxyContainer = new AbsolutePanel();
			proxyContainer.setWidth("100%");
			hp.add(proxyContainer);
			hp.setStyleName("DragProxy");
			dragContainer.add(hp);
			proxyLabel = new Label();
			proxyLabel.setStyleName("ScreenLabel");
		}

	    proxyContainer.clear();
	    if(proxy != null){
            proxyContainer.add(proxy);
        }else{
            item = (DragItem)context.draggable;
            proxyLabel.setText(table.getValueAt(item.getIndex(),0).toString());
            proxyContainer.add(proxyLabel);
        }
		
	    return dragContainer;
	}
	
	public void setProxy(Widget proxy) {
	    this.proxy = proxy;
	}
	
	public AbsolutePanel getDropIndicator() {
	    return dropIndicator;
	}

	//******* Handler code ***************
	
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
			BeforeDragStartHandler<DragItem> handler) {
		return addHandler(handler,BeforeDragStartEvent.getType());
	}

	public HandlerRegistration addStartHandler(
			DragStartHandler<DragItem> handler) {
		return addHandler(handler,DragStartEvent.getType());
	}

}
