package org.openelis.gwt.widget.table;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetArea;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.event.DragManager;

import java.util.HashMap;


public class TableDragController extends PickupDragController {
    
    public DragManager manager;
    private static final String PRIVATE_CSS_PROXY = "dragdrop-proxy";
    private boolean enabled;
    private Widget proxy;
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
    
    public TableDragController(AbsolutePanel boundaryPanel){
        super(boundaryPanel,false);
        setBehaviorDragProxy(true);
        setBehaviorDragStartSensitivity(5);
    }
    
    @Override
    public void previewDragStart() throws VetoDragException {
        ((TableRow)context.draggable).setDragValues();
        super.previewDragStart();
        
    }
    
    @Override
    public void previewDragEnd() throws VetoDragException {
        super.previewDragEnd();
    }
    
    public void dragMove() {
        context.desiredDraggableX = context.mouseX;
        context.desiredDraggableY = context.mouseY;
        super.dragMove();
      }
    
    @Override
    public void dragStart() {
        context.draggable.addStyleName("disabled");
        super.dragStart();
    }
    
    protected Widget newDragProxy(DragContext context) {
        AbsolutePanel container = new AbsolutePanel();
        container.getElement().getStyle().setProperty("overflow", "visible");

        WidgetArea draggableArea = new WidgetArea(context.draggable, null);
        for (Widget widget : context.selectedWidgets) {
          WidgetArea widgetArea = new WidgetArea(widget, null);
          Widget proxy = new SimplePanel();
          proxy.setPixelSize(widget.getOffsetWidth(), widget.getOffsetHeight());
          proxy.addStyleName(PRIVATE_CSS_PROXY);
          container.add(proxy, widgetArea.getLeft() - draggableArea.getLeft(), widgetArea.getTop()
              - draggableArea.getTop());
        }
        this.proxy = container;
        return container;
      }
    
    @Override
    public void dragEnd() {
        context.draggable.removeStyleName("TableHighlighted");
        context.draggable.removeStyleName("disabled");
        super.dragEnd();
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

}
