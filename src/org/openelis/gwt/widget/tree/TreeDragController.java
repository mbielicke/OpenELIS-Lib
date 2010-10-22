package org.openelis.gwt.widget.tree;

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
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * This classes is used by the Table widget to enable and handle dragging of
 * table rows
 * 
 * @author tschmidt
 * 
 */
public class TreeDragController extends PickupDragController implements
                                                             HasBeforeDragStartHandlers<DragItem>,
                                                             HasDragStartHandlers<DragItem> {

    /**
     * Widget displayed when a row is being dragged;
     */
    protected Widget          proxy;

    /**
     * The Table widget that this controller is registered to.
     */
    protected Tree           tree;

    /**
     * Widgets used to display the drag
     */
    protected AbsolutePanel   proxyContainer, dropIndicator;
    protected DecoratorPanel  dragContainer;
    protected HorizontalPanel hp;
    protected Label           proxyLabel;

    /**
     * Used to fire events to registered handlers
     */
    private HandlerManager    handlerManager;

    /**
     * Constructor that takes the Table used by this controller and a panel used
     * to contain the drag. The boundaryPanel is usually set to RootPanel.get().
     * 
     * @param table
     * @param boundaryPanel
     */
    public TreeDragController(Tree tree, AbsolutePanel boundaryPanel) {
        super(boundaryPanel, false);
        this.tree = tree;
        setBehaviorDragProxy(true);

        /* Drag will not start until mouse moved 5 pixels */
        setBehaviorDragStartSensitivity(5);
    }

    /**
     * This method is overridden from PickupDragController so that
     * BeforeDragStartEvent can be fired. If the user cancels the event the Drag
     * will be aborted.
     */
    @Override
    public void previewDragStart() throws VetoDragException {
        BeforeDragStartEvent<DragItem> event;

        /* Select row before drag start and cancel drag if not selectable */
        if ( !tree.selectNodeAt(tree.view.lastRow))
            throw new VetoDragException();

        /* Set the index of row being dragged into the DragItem */
        ((DragItem)context.draggable).setIndex(tree.view.lastRow);

        /* Notify the user that a Drag is to be started */
        event = BeforeDragStartEvent.fire(this, (DragItem)context.draggable);

        /* Check if user canceled event */
        if (event != null && event.isCancelled())
            throw new VetoDragException();

        /* Set Proxy from event if not null */
        if (event != null && event.getProxy() != null)
            proxy = event.getProxy();

        super.previewDragStart();
    }

    /**
     * This method is overridden from PickupDragController to make the Drag
     * widget start and drag from the cursor instead of the left side of the
     * table row.
     */
    @Override
    public void dragMove() {
        context.desiredDraggableX = context.mouseX;
        context.desiredDraggableY = context.mouseY + 16;
        super.dragMove();
    }

    /**
     * This method is overridden to remove the default drag style form the
     * dragged widget and also to fire the DragStartEvent to the user.
     */
    @Override
    public void dragStart() {
        super.dragStart();
        ((DragItem)context.draggable).removeStyleName("dragdrop-dragging");

        DragStartEvent.fire(this, (DragItem)context.draggable);
    }

    /**
     * This method is overridden to use a custom drag proxy instead of the
     * default
     */
    @Override
    protected Widget newDragProxy(DragContext context) {
        DragItem item;

        /**
         * Create Drag container first time used
         */
        if (dragContainer == null) {
            dragContainer = new DecoratorPanel();
            hp = new HorizontalPanel();
            dropIndicator = new AbsolutePanel();
            dropIndicator.setStyleName("DragStatus NoDrop");
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
        if (proxy != null) {
            proxyContainer.add(proxy);
        } else {
            /* Default proxy if not set by user */
            item = (DragItem)context.draggable;
            proxyLabel.setText(tree.getValueAt(item.getIndex(), 0).toString());
            proxyContainer.add(proxyLabel);
        }

        return dragContainer;
    }

    /**
     * Sets the proxy to be used when dragging a row with this container
     * 
     * @param proxy
     */
    public void setProxy(Widget proxy) {
        this.proxy = proxy;
    }

    /**
     * Sets the indicator image depending id the row can be dropped or not.
     */
    public void setDropIndicator(boolean drop) {
        if (drop)
            dropIndicator.setStyleName("DragStatus Drop");
        else
            dropIndicator.setStyleName("DragStatus NoDrop");
    }

    // ******* Handler code ***************

    /**
     * Method to register a beforeStartHandler to this controller
     */
    public HandlerRegistration addBeforeDragStartHandler(BeforeDragStartHandler<DragItem> handler) {
        return addHandler(handler, BeforeDragStartEvent.getType());
    }

    /**
     * Method to register a StartHAndler to this controller
     */
    public HandlerRegistration addStartHandler(DragStartHandler<DragItem> handler) {
        return addHandler(handler, DragStartEvent.getType());
    }

    protected final <H extends EventHandler> HandlerRegistration addHandler(final H handler,
                                                                            GwtEvent.Type<H> type) {
        return ensureHandlers().addHandler(type, handler);
    }

    /**
     * Ensures the existence of the handler manager.
     * 
     * @return the handler manager
     * */
    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = new HandlerManager(this) : handlerManager;
    }

    HandlerManager getHandlerManager() {
        return handlerManager;
    }

    public void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }

    public int getHandlerCount(GwtEvent.Type<?> type) {
        if (handlerManager == null)
            return 0;
        return handlerManager.getHandlerCount(type);

    }

}
