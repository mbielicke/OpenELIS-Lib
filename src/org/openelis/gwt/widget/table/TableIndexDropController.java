package org.openelis.gwt.widget.table;

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

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
* Allows one or more table rows to be dropped into an existing table.
*/
public final class TableIndexDropController extends AbstractPositioningDropController implements HasBeforeDropHandlers<TableRow>, HasDropHandlers<TableRow>,HasDropEnterHandlers<TableRow> {

	private static final String CSS_DROP_POSITIONER = "DropPositioner";

	private TableWidget table;

	public boolean dropping;

	private boolean validDrop;

	private Widget positioner;
	
	private DropPosition dropPos;

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
		super(table.view.cellView);
		this.table = table;
	}

	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
		if(scroll != null) {
			scroll.cancel();
			scroll = null;
		}
		if(!validDrop) 
			throw new VetoDragException();
		if(getHandlerCount(BeforeDropEvent.getType()) > 0) {
			BeforeDropEvent event = BeforeDropEvent.fire(this, (TableRow)context.draggable, table.getRow(targetRow));
			if(event != null && event.isCancelled()){
				positioner.removeFromParent();
				positioner = null;
				((TableDragController)context.dragController).dropIndicator.setStyleName("DragStatus NoDrop");
				throw new VetoDragException();
			}
		}
		super.onPreviewDrop(context);
	}


	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);
		positioner = newPositioner(context); 
		//((TableDragController)context.dragController).dropIndicator.setStyleName("DragStatus Drop");
	}

	@Override
	public void onDrop(DragContext context) {
		TableRow drop = null;
		drop = (TableRow)context.draggable;
		TableDataRow row = drop.dragRow;
		int modelIndex = drop.dragModelIndex;
		//if(table.numRows() == 0 || table.numRows() -1 == table.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).modelIndex)
		//    table.addRow(row);
		//else 
		//    table.addRow(table.renderer.getRows().get(targetRow+1).modelIndex, row);

		//if(table.dragController == context.dragController){
		//    if(modelIndex > table.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).modelIndex)
		//        modelIndex++;
		//    table.deleteRow(modelIndex);
		//}
		if(table.dragController == context.dragController){	
			if(dropPos == DropPosition.ABOVE)
				table.moveRow(drop.dragModelIndex, table.renderer.getRows().get(targetRow).modelIndex);
			else
				table.moveRow(drop.dragModelIndex, table.renderer.getRows().get(targetRow).modelIndex+1);
			//table.MoveRow(drop.dragIndex, table.renderer.getRows().get(targetRow+1).modelIndex);
		}else {
			if(table.numRows() == 0)
				table.addRow(row);
			else if(table.numRows() -1 == table.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).modelIndex && dropPos == DropPosition.BELOW)
				table.addRow(row);
			else{
				if(dropPos == DropPosition.ABOVE)
					table.addRow(table.renderer.getRows().get(targetRow).modelIndex, row);
				else
					table.addRow(table.renderer.getRows().get(targetRow+1).modelIndex,row);
			}
		}
		dropping = false;
		super.onDrop(context);
		if(getHandlerCount(DropEvent.getType()) > 0)
			DropEvent.fire(this, drop, table.getRow(targetRow));
	}

	@Override
	public void onLeave(DragContext context) {
		if(positioner != null) {
			positioner.removeFromParent();
			positioner = null;
		}
		((TableDragController)context.dragController).dropIndicator.setStyleName("DragStatus NoDrop");
		super.onLeave(context);
	}


	@Override
	public void onMove(DragContext context) {
		super.onMove(context);
		if(scroll != null){
			scroll.cancel();
			scroll = null;
		}
		targetRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(
				context.mouseX, context.mouseY), LocationWidgetComparator.BOTTOM_HALF_COMPARATOR);
		if(targetRow >= table.view.table.getRowCount() || context.mouseY < table.renderer.rows.get(targetRow).getAbsoluteTop())
			targetRow--;
		if(targetRow < 0)
			targetRow = 0;
		
		Location tableLocation = new WidgetLocation(table, context.boundaryPanel);
		validDrop = true;
		if(table.numRows() > 0){
			int rowTop = table.renderer.rows.get(targetRow).getAbsoluteTop();
			int rowHght =  table.renderer.rows.get(targetRow).getOffsetHeight();
			int rowBot = rowTop + rowHght;
			int diff = context.mouseY - rowTop;
			double perc = ((double)diff)/((double)rowHght);
			if(perc <= 0.5)
				dropPos = DropPosition.ABOVE;
			else 
				dropPos = DropPosition.BELOW;
			if(getHandlerCount(DropEnterEvent.getType()) > 0){
				DropEnterEvent event = DropEnterEvent.fire(this, (TableRow)context.draggable, table.renderer.rows.get(targetRow == -1 ? 0 : targetRow),dropPos);
				if(event != null && event.isCancelled())
					validDrop = false;
			}
			if(validDrop) {
				TableRow row = table.renderer.getRows().get(targetRow == -1 ? 0 : targetRow);
				Location widgetLocation = new WidgetLocation(row, RootPanel.get());
				if(dropPos == DropPosition.BELOW) {
					context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop()
							+ (targetRow == -1 ? 0 : row.getOffsetHeight()));
				}else{
					context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop());
				}
				((TableDragController)context.dragController).dropIndicator.setStyleName("DragStatus Drop");
			}else{
				((TableDragController)context.dragController).dropIndicator.setStyleName("DragStatus NoDrop");
				positioner.removeFromParent();
			}
			if((targetRow == 0 && context.mouseY < rowTop+3)|| (targetRow == table.maxRows -1 && context.mouseY > rowBot-3))
				checkScroll(targetRow);
		}else{
			Location headerLocation = new WidgetLocation(table.view.header,RootPanel.get());
			RootPanel.get().add(positioner,tableLocation.getLeft(),headerLocation.getTop()+table.view.header.getOffsetHeight());
		}

	}

	public Timer scroll;

	public boolean checkScroll(final int targetRow) {
		if(table.numRows() < table.maxRows)
			return false;
		TableRow dRow = table.renderer.getRows().get(targetRow);
		if((dRow.index == 0 && dRow.modelIndex > 0) || 
				(dRow.index == table.maxRows -1 && dRow.modelIndex < table.shownRows() -1)){
			if(dRow.index == table.maxRows -1){
				if(dRow.modelIndex < table.shownRows() -1){

					scroll = new Timer() {
						public void run() {
							table.view.setScrollPosition(table.view.scrollBar.getScrollPosition()+20);
							checkScroll(targetRow);
						}
					};
					scroll.schedule(200);
					return true;
				}
			}
			if(dRow.index == 0){
				if(dRow.modelIndex > 0){

					scroll = new Timer() {
						public void run() {
							table.view.setScrollPosition(table.view.scrollBar.getScrollPosition()-20);
							checkScroll(targetRow);
						}
					};
					scroll.schedule(200);
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
		Widget p = new AbsolutePanel();
		p.addStyleName(CSS_DROP_POSITIONER);
		p.setPixelSize(table.getOffsetWidth(), 1);
		DOM.setStyleAttribute(p.getElement(), "zIndex", "1000");
		return p;
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

	public HandlerRegistration addBeforeDropHandler(
			BeforeDropHandler<TableRow> handler) {
		return addHandler(handler,BeforeDropEvent.getType());
	}

	public HandlerRegistration addDropHandler(DropHandler<TableRow> handler) {
		return addHandler(handler,DropEvent.getType());
	}

	public HandlerRegistration addDropEnterHandler(
			DropEnterHandler<TableRow> handler) {
		return addHandler(handler, DropEnterEvent.getType());
	}

}
