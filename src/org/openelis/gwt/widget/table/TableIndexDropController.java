package org.openelis.gwt.widget.table;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.event.DropManager;

/**
* Allows one or more table rows to be dropped into an existing table.
*/
public final class TableIndexDropController extends AbstractPositioningDropController {

 private static final String CSS_DROP_POSITIONER = "DropPositioner";

 private TableWidget table;

 public boolean dropping;
 
 private Widget positioner;
 
 public DropManager manager;
 
 private IndexedPanel flexTableRowsAsIndexPanel = new IndexedPanel() {

   public Widget getWidget(int index) {
     return table.renderer.getRows().get(index);
   }

   public int getWidgetCount() {
     return table.view.table.getRowCount();
   }

   public int getWidgetIndex(Widget child) {
     throw new UnsupportedOperationException();
   }

   public boolean remove(int index) {
     throw new UnsupportedOperationException();
   }
 };

 private int targetRow;
 private int targetPosition = -1;

 public TableIndexDropController(TableWidget table) {
   super(table);
   this.table = table;
 }

 @Override
 public void onPreviewDrop(DragContext context) throws VetoDragException {
    if(manager != null)
        manager.previewDrop(context);
    super.onPreviewDrop(context);
 }
 

 @Override
 public void onEnter(DragContext context) {
     super.onEnter(context);
     positioner = newPositioner(context); 
     if(manager != null)
         manager.onEnter(context);
 }
 
 @Override
 public void onDrop(DragContext context) {
     TableRow drop = null;
     if(manager != null)
         drop = (TableRow)manager.getDropWidget(context);
     else
         drop = (TableRow)context.draggable;
     DataSet<Object> row = drop.dragRow;
     int modelIndex = drop.dragModelIndex;
     if(table.model.numRows() == 0 || table.model.numRows() -1 == table.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).modelIndex)
         table.model.addRow(row);
     else 
         table.model.addRow(table.renderer.getRows().get(targetRow+1).modelIndex, row);
     if(table.dragController == context.dragController){
         if(modelIndex > table.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).modelIndex)
             modelIndex++;
         table.model.deleteRow(modelIndex);
     }    
     dropping = false;
     super.onDrop(context);
     if(manager != null)
         manager.dropEnded(context);
 }

 @Override
 public void onLeave(DragContext context) {
     positioner.removeFromParent();
     positioner = null;
     super.onLeave(context);
     if(manager != null)
         manager.onLeave(context);
 }
 
 
 @Override
 public void onMove(DragContext context) {
   super.onMove(context);
   if(scroll != null){
       scroll.cancel();
       scroll = null;
   }
   targetRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(
       context.mouseX, context.mouseY), LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;
   Location tableLocation = new WidgetLocation(table, context.boundaryPanel);
   if(table.model.numRows() > 0){
       TableRow row = table.renderer.getRows().get(targetRow == -1 ? 0 : targetRow);
       Location widgetLocation = new WidgetLocation(row, context.boundaryPanel);
       context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop()
                                 + (targetRow == -1 ? 0 : row.getOffsetHeight()));
   }else{
       Location headerLocation = new WidgetLocation(table.view.header,context.boundaryPanel);
       context.boundaryPanel.add(positioner,tableLocation.getLeft(),headerLocation.getTop()+table.view.header.getOffsetHeight());
   }
   if(targetRow == 0 || targetRow == table.maxRows -1)
       checkScroll(targetRow);
 }
 
 public Timer scroll;

 public boolean checkScroll(final int targetRow) {
     if(table.model.numRows() < table.maxRows)
         return false;
     TableRow dRow = table.renderer.getRows().get(targetRow);
     if((dRow.index == 0 && dRow.modelIndex > 0) || 
        (dRow.index == table.maxRows -1 && dRow.modelIndex < table.model.shownRows() -1)){
         if(dRow.index == table.maxRows -1){
             if(dRow.modelIndex < table.model.shownRows() -1){
                 
                 scroll = new Timer() {
                     public void run() {
                         table.view.setScrollPosition(table.view.scrollBar.getScrollPosition()+10);
                         checkScroll(targetRow);
                         //table.view.setScrollPosition(table.view.scrollBar.getScrollPosition()+10);
                     }
                 };
                 scroll.schedule(500);
                 return true;
             }
         }
         if(dRow.index == 0){
             if(dRow.modelIndex > 0){
             
             scroll = new Timer() {
                 public void run() {
                     table.view.setScrollPosition(table.view.scrollBar.getScrollPosition()-10);
                     checkScroll(targetRow);
                    //table.view.setScrollPosition(table.view.scrollBar.getScrollPosition()-10);
                 }
             };
             scroll.schedule(500);
             return true;
         }
     }  
   }
   if(scroll != null) {
       scroll.cancel();
       scroll = null;
   }
   return false;  
 }
 
 Widget newPositioner(DragContext context) {
     Widget p = new SimplePanel();
     p.addStyleName(CSS_DROP_POSITIONER);
     p.setPixelSize(table.getOffsetWidth(), 1);
     return p;
   }

}
