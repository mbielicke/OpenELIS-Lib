package org.openelis.gwt.widget.tree;

import java.util.HashMap;

import org.openelis.gwt.widget.HandlesEvents;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;


public class TreeDragController extends PickupDragController  {
        
    private HandlesEvents events = new HandlesEvents();
    
    private boolean enabled;
   
    
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
    	((TreeRow)context.draggable).setDragValues();
        super.previewDragStart();
    }
    
    @Override
    public void previewDragEnd() throws VetoDragException {
        super.previewDragEnd();
    }
    
    @Override
    public void dragStart() {
        context.draggable.addStyleName("disabled");
        super.dragStart();
    }
   
    @Override
    public void dragMove() {
        context.desiredDraggableX = context.mouseX;
        context.desiredDraggableY = context.mouseY;
        super.dragMove();
      }
    
    @Override
    public void dragEnd() {
        context.draggable.removeStyleName("TreeHighlighted");
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
