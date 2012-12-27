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
import org.openelis.gwt.event.DropEnterEvent.DropPosition;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
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
	
	public DropPosition dropPos;


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
		if(scroll != null){
			scroll.cancel();
			scroll = null;
		}	
		if(!validDrop)
			throw new VetoDragException();
		if(handlerManager.getHandlerCount(BeforeDropEvent.getType()) > 0) {
			BeforeDropEvent<TreeRow> event = new BeforeDropEvent<TreeRow>((TreeRow)context.draggable, tree.renderer.rows.get(targetRow));
			handlerManager.fireEvent(event);
			if(event != null && event.isCancelled()){
				positioner.removeFromParent();
				positioner = null;
				for(TreeRow trow : tree.renderer.rows )
					trow.removeStyleName("DropOnRow");
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
		tree.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).removeStyleName("DropOnRow");
		if(tree.dragController == context.dragController){
			/*
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
	        */
			if(dropPos == DropPosition.ON) {
				tree.deleteRow(modelIndex);
				tree.addChildItem(targetItem,item);
			}else if(dropPos == DropPosition.ABOVE) {
				if(targetItem.depth > 0){
					tree.deleteRow(modelIndex);
					tree.addChildItem(targetItem.parent,item,targetItem.childIndex);
				}else
					tree.moveRow(dropRow.dragIndex, tree.renderer.getRows().get(targetRow).modelIndex);
			}else {
				if(targetItem.depth > 0){
					tree.deleteRow(modelIndex);
					tree.addChildItem(targetItem.parent,item,targetItem.childIndex+1);
				}else
					tree.moveRow(dropRow.dragIndex, tree.renderer.getRows().get(targetRow).modelIndex+1);
			}
			
		}else {   
			if(tree.numRows() == 0){
				tree.addRow(item);
			}else if(tree.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).modelIndex == tree.rows.size()-1 && dropPos == DropPosition.BELOW ){
				tree.addRow(item);
			}else if(dropPos == DropPosition.ON)
				tree.addChildItem(targetItem,item);
			else if(targetRow < 0)
				tree.addRow(0,item);
			else if(targetItem.depth > 0){
				if(dropPos == DropPosition.ABOVE)
					tree.addChildItem(targetItem.parent,item,targetItem.childIndex);
				else
					tree.addChildItem(targetItem.parent,item,targetItem.childIndex+1);
			}else{
				if(dropPos == DropPosition.ABOVE)
					tree.addRow(tree.renderer.getRows().get(targetRow).modelIndex,item);
				else
					tree.addRow(tree.renderer.getRows().get(targetRow+1).modelIndex, item);
			}
		}
		dropping = false;
		dropRow = null;
		for(TreeRow trow : tree.renderer.rows )
			trow.removeStyleName("DropOnRow");
		super.onDrop(context);
		if(handlerManager.getHandlerCount(DropEvent.getType()) > 0)
			handlerManager.fireEvent(new DropEvent<TreeRow>(dropRow, tree.getRow(targetRow)));
		tree.select(item);
	}

	@Override
	public void onLeave(DragContext context) {
		if(positioner != null) {
			positioner.removeFromParent();
			positioner = null;
		}
		for(TreeRow trow : tree.renderer.rows )
			trow.removeStyleName("DropOnRow");
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
		
		targetRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(
				context.mouseX, context.mouseY), LocationWidgetComparator.BOTTOM_HALF_COMPARATOR);
		if(targetRow >= tree.view.table.getRowCount() || context.mouseY < tree.renderer.rows.get(targetRow).getAbsoluteTop())
			targetRow--;
		if(targetRow < 0)
			targetRow = 0;
		
		int rowTop = tree.renderer.rows.get(targetRow).getAbsoluteTop();
		int rowHght =  tree.renderer.rows.get(targetRow).getOffsetHeight();
		int rowBot = rowTop + rowHght;
		
		Location tableLocation = new WidgetLocation(tree, context.boundaryPanel);
		validDrop = true;
		if(tree.numRows() > 0){
			int diff = context.mouseY - rowTop;
			double perc = ((double)diff)/((double)rowHght);
			if(perc <= 0.33)
				dropPos = DropPosition.ABOVE;
			else if(perc > 0.33 && perc < 0.66)
				dropPos = DropPosition.ON;
			else 
				dropPos = DropPosition.BELOW;
			if(handlerManager.getHandlerCount(DropEnterEvent.getType()) > 0){
				DropEnterEvent<TreeRow> event = new DropEnterEvent<TreeRow>((TreeRow)context.draggable, tree.renderer.rows.get(targetRow == -1 ? 0 : targetRow),dropPos);
				handlerManager.fireEvent(event);
				if(event != null && event.isCancelled())
					validDrop = false;
			}
			if(validDrop) {
				TreeRow row = tree.renderer.getRows().get(targetRow == -1 ? 0 : targetRow);
				Location widgetLocation = new WidgetLocation(row, context.boundaryPanel);
				for(TreeRow trow : tree.renderer.rows )
					trow.removeStyleName("DropOnRow");
				if(dropPos == DropPosition.BELOW) {
					context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop()
							+ (targetRow == -1 ? 0 : row.getOffsetHeight()));
				}else if(dropPos == DropPosition.ON) {
					positioner.removeFromParent();
					tree.renderer.rows.get(targetRow).addStyleName("DropOnRow");
				}else{
					context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop());
				}
				((TreeDragController)context.dragController).dropIndicator.setStyleName("DragStatus Drop");
			}else{
				((TreeDragController)context.dragController).dropIndicator.setStyleName("DragStatus NoDrop");
				positioner.removeFromParent();
				for(TreeRow trow : tree.renderer.rows )
					trow.removeStyleName("DropOnRow");
			}
		}else{
			Location headerLocation = new WidgetLocation(tree.view.header,context.boundaryPanel);
			context.boundaryPanel.add(positioner,tableLocation.getLeft(),headerLocation.getTop()+tree.view.header.getOffsetHeight());
		}
		
		if((targetRow == 0  && context.mouseY < rowTop) || (targetRow == tree.maxRows -1 && context.mouseY > rowBot))
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
		TreeRow dRow = null;
		if(targetRow > -1)
			dRow = tree.renderer.getRows().get(targetRow);
		else
			dRow = tree.renderer.getRows().get(0);
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
					scroll.schedule(150);
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
					scroll.schedule(150);
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
	
	private HandlerManager handlerManager = new HandlerManager(this);

	/*
	protected final <H extends EventHandler> HandlerRegistration addHandler(
			final H handler, GwtEvent.Type<H> type) {
		return ensureHandlers().addHandler(type, handler);
	}

	/**
	 * Ensures the existence of the handler manager.
	 * 
	 * @return the handler manager
	 * */
	/*
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
    */
	public HandlerRegistration addBeforeDropHandler(
			BeforeDropHandler<TreeRow> handler) {
		return handlerManager.addHandler(BeforeDropEvent.getType(),handler);
	}

	public HandlerRegistration addDropHandler(DropHandler<TreeRow> handler) {
		return handlerManager.addHandler(DropEvent.getType(),handler);
	}

	public HandlerRegistration addDropEnterHandler(
			DropEnterHandler<TreeRow> handler) {
		return handlerManager.addHandler(DropEnterEvent.getType(),handler);
	}
	
	

}
