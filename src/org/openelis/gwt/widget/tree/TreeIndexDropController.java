package org.openelis.gwt.widget.tree;

import org.openelis.gwt.event.BeforeDropEvent;
import org.openelis.gwt.event.BeforeDropHandler;
import org.openelis.gwt.event.DropEnterEvent;
import org.openelis.gwt.event.DropEnterHandler;
import org.openelis.gwt.event.DropEvent;
import org.openelis.gwt.event.DropHandler;
import org.openelis.gwt.event.HasBeforeDropHandlers;
import org.openelis.gwt.event.HasDropEnterHandlers;
import org.openelis.gwt.event.HasDropHandlers;
import org.openelis.gwt.widget.table.TableDragController;

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
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Allows one or more table rows to be dropped into an existing table.
 */
public class TreeIndexDropController extends AbstractPositioningDropController implements HasBeforeDropHandlers<TreeRow>, HasDropHandlers<TreeRow>, HasDropEnterHandlers<TreeRow> {

	private static final String CSS_DROP_POSITIONER = "DropPositioner";

	private TreeWidget tree;

	public boolean dropping;

	public boolean validDrop;

	private Widget positioner;
	
	public TreeRow dropRow;


	private IndexedPanel flexTableRowsAsIndexPanel = new IndexedPanel() {

		public Widget getWidget(int index) {
			return tree.renderer.getRows().get(index);
		}

		public int getWidgetCount() {
			return tree.view.table.getRowCount();
		}

		public int getWidgetIndex(Widget child) {
			throw new UnsupportedOperationException();
		}

		public boolean remove(int index) {
			throw new UnsupportedOperationException();
		}
	};

	public int targetRow;
	private int targetPosition = -1;

	public TreeIndexDropController(TreeWidget tree) {
		super(tree);
		this.tree = tree;
	}

	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
		if(!validDrop)
			throw new VetoDragException();
		if(getHandlerCount(BeforeDropEvent.getType()) > 0) {
			BeforeDropEvent event = BeforeDropEvent.fire(this, (TreeRow)context.draggable, tree.getRow(targetRow));
			if(event != null && event.isCancelled()){
				positioner.removeFromParent();
				positioner = null;
				((TreeDragController)context.dragController).dropIndicator.setStyleName("DragStatus NoDrop");
				throw new VetoDragException();
			}
		}
		super.onPreviewDrop(context);
	}


	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);
		positioner = newPositioner(context); 
		//((TreeDragController)context.dragController).dropIndicator.setStyleName("DragStatus Drop");
	}

	@Override
	public void onDrop(DragContext context) {
		if(dropRow == null)
			dropRow = (TreeRow)context.draggable;
		TreeDataItem item = dropRow.dragItem;
		int modelIndex = dropRow.dragModelIndex;
		TreeDataItem targetItem = tree.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).item;
		if(tree.dragController == context.dragController){
			if(targetItem.open && targetItem.mightHaveChildren() && targetRow > -1){
				tree.deleteRow(modelIndex);
				tree.addChildItem(targetItem,item,0);
			}else if(targetItem.depth > 0){
				tree.deleteRow(modelIndex);
				tree.addChildItem(targetItem.parent,item,targetItem.childIndex+1);				
			}else if(targetRow < 0)
	     		tree.moveRow(dropRow.dragIndex, 0);
	     	 else
	     		tree.moveRow(dropRow.dragIndex, tree.renderer.getRows().get(targetRow).modelIndex+1);
		}else {   
			if(tree.numRows() == 0 || (tree.numRows() -1 != 0 && tree.numRows() -1 == tree.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).modelIndex && targetItem.depth == 0)){
				tree.addRow(item);
			}else if(targetItem.open && targetItem.mightHaveChildren() && targetRow > -1)
				tree.addChildItem(targetItem,item,0);
			else if(targetRow < 0)
				tree.addRow(0,item);
			else if(targetItem.depth > 0)
				tree.addChildItem(targetItem.parent,item,targetItem.childIndex+1);
			else
				tree.addRow(tree.renderer.getRows().get(targetRow+1).modelIndex, item);
		}
		dropping = false;
		dropRow = null;
		super.onDrop(context);
		if(getHandlerCount(DropEvent.getType()) > 0)
			DropEvent.fire(this, dropRow, tree.getRow(targetRow));

	}

	@Override
	public void onLeave(DragContext context) {
		positioner.removeFromParent();
		positioner = null;
		((TreeDragController)context.dragController).dropIndicator.setStyleName("DragStatus NoDrop");
		super.onLeave(context);
	}


	@Override
	public void onMove(DragContext context) {
		super.onMove(context);
		if(open != null){
			open.cancel();
			open = null;
		}
		if(scroll != null){
			scroll.cancel();
			scroll = null;
		}
		
		int newRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(
				context.mouseX, context.mouseY), LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;
		if(newRow == targetRow)
			return;
		targetRow = newRow;
		Location tableLocation = new WidgetLocation(tree, context.boundaryPanel);
		validDrop = true;
		if(tree.numRows() > 0){
			if(tree.dragController == context.dragController){
				int checkIndex = targetRow;
				if(checkIndex < 0) 
					checkIndex = 0;
				if(checkIndex >= tree.maxRows)
					checkIndex = tree.maxRows - 1;
				if(((TreeRow)context.draggable).dragModelIndex == tree.modelIndexList[checkIndex]){
					positioner.removeFromParent();
					validDrop = false;
					return;
				}
			}
			if(getHandlerCount(DropEnterEvent.getType()) > 0){
				DropEnterEvent event = DropEnterEvent.fire(this, (TreeRow)context.draggable, tree.getRow(targetRow));
				if(event != null && event.isCancelled())
					validDrop = false;
			}
			if(validDrop) {
				TreeRow row = tree.renderer.getRows().get(targetRow == -1 ? 0 : targetRow);
				Location widgetLocation = new WidgetLocation(row, context.boundaryPanel);
				context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop()
						+ (targetRow == -1 ? 0 : row.getOffsetHeight()));
				((TreeDragController)context.dragController).dropIndicator.setStyleName("DragStatus Drop");
			}else{
				((TreeDragController)context.dragController).dropIndicator.setStyleName("DragStatus NoDrop");
				positioner.removeFromParent();
			}
		}else{
			Location headerLocation = new WidgetLocation(tree.view.header,context.boundaryPanel);
			context.boundaryPanel.add(positioner,tableLocation.getLeft(),headerLocation.getTop()+tree.view.header.getOffsetHeight());
		}
		
		if(targetRow == 0 || targetRow == tree.maxRows -1)
			checkScroll(targetRow);
		if(targetRow > -1 && targetRow < tree.maxRows){
			final TreeRow targetItem = tree.renderer.getRows().get(targetRow);
			final DragContext ctx = context;
			if(targetItem.item.mightHaveChildren() && !targetItem.item.open){
				open = new Timer() {
					public void run() {
						targetItem.item.toggle();
						if(ctx.dragController == tree.dragController) {
							if(((TreeRow)ctx.draggable).dragModelIndex > targetItem.modelIndex) {
								((TreeRow)ctx.draggable).dragItem  = tree.getRow(((TreeRow)ctx.draggable).dragModelIndex+targetItem.item.getItems().size());
								((TreeRow)ctx.draggable).dragModelIndex = ((TreeRow)ctx.draggable).dragModelIndex+targetItem.item.getItems().size();
							}
						}
					}
				};
				open.schedule(1000);
			}
		}
	}
	
	public Timer open;
	public Timer scroll;

	public boolean checkScroll(final int targetRow) {
		if(tree.numRows() < tree.maxRows)
			return false;
		TreeRow dRow = tree.renderer.getRows().get(targetRow);
		if((dRow.index == 0 && dRow.modelIndex > 0) || 
				(dRow.index == tree.maxRows -1 && dRow.modelIndex < tree.shownRows() -1)){
			if(dRow.index == tree.maxRows -1){
				if(dRow.modelIndex < tree.shownRows() -1){
					scroll = new Timer() {
						public void run() {
							tree.view.setScrollPosition(tree.view.scrollBar.getScrollPosition()+20);
							checkScroll(targetRow);
						}
					};
					scroll.schedule(350);
					return true;
				}
			}
			if(dRow.index == 0){
				if(dRow.modelIndex > 0){

					scroll = new Timer() {
						public void run() {
							tree.view.setScrollPosition(tree.view.scrollBar.getScrollPosition()-20);
							checkScroll(targetRow);
						}
					};
					scroll.schedule(350);
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

	private Widget newPositioner(DragContext context) {
		Widget p = new SimplePanel();
		p.addStyleName(CSS_DROP_POSITIONER);
		p.setPixelSize(tree.getOffsetWidth(), 1);
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
			BeforeDropHandler<TreeRow> handler) {
		return addHandler(handler,BeforeDropEvent.getType());
	}

	public HandlerRegistration addDropHandler(DropHandler<TreeRow> handler) {
		return addHandler(handler,DropEvent.getType());
	}

	public HandlerRegistration addDropEnterHandler(
			DropEnterHandler<TreeRow> handler) {
		return addHandler(handler,DropEnterEvent.getType());
	}
	

}
