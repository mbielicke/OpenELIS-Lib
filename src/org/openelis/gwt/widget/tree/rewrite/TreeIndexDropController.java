package org.openelis.gwt.widget.tree.rewrite;

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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Allows one or more table rows to be dropped into an existing table.
 */
public class TreeIndexDropController extends AbstractPositioningDropController {

	private static final String CSS_DROP_POSITIONER = "DropPositioner";

	private TreeWidget tree;

	public boolean dropping;

	public boolean validDrop;

	private Widget positioner;
	
	public TreeRow dropRow;


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

	private int targetRow;
	private int targetPosition = -1;

	public TreeIndexDropController(TreeWidget tree) {
		super(tree);
		this.tree = tree;
	}

	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
		if(!validDrop)
			throw new VetoDragException();
		super.onPreviewDrop(context);
	}


	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);
		positioner = newPositioner(context); 
	}

	@Override
	public void onDrop(DragContext context) {
		if(dropRow == null)
			dropRow = (TreeRow)context.draggable;
		TreeDataItem item = dropRow.dragItem;
		int modelIndex = dropRow.dragModelIndex;
		if(tree.dragController == context.dragController){
			if(modelIndex < tree.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).modelIndex)
				targetRow--;
			//tree.unlink(modelIndex);
		}   
		boolean advanceScroll = false;
		TreeDataItem targetItem = tree.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).item;
		if(tree.numRows() == 0 || (tree.numRows() -1 != 0 && tree.numRows() -1 == tree.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).modelIndex && targetItem.depth == 0)){
			tree.addRow(item);
			advanceScroll = true;
		}else if(targetItem.open && targetItem.mightHaveChildren() && targetRow > -1)
			targetItem.addItem(0,item);
		else if(targetRow < 0)
			tree.addRow(0,item);
		else if(targetItem.depth > 0)
			targetItem.parent.addItem(targetItem.childIndex+1, item);
		else
			tree.addRow(tree.renderer.getRows().get(targetRow+1).modelIndex, item);
		if(tree.dragController == context.dragController){
			//if(modelIndex > tree.renderer.getRows().get(targetRow == -1 ? 0 : targetRow).modelIndex)
				//  modelIndex++;
			//tree.model.deleteRow(modelIndex);
		}    
		if(advanceScroll){
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					tree.view.setScrollPosition(tree.view.scrollBar.getScrollPosition()+20);
				}
			});
		}
		dropping = false;
		super.onDrop(context);

	}

	@Override
	public void onLeave(DragContext context) {
		positioner.removeFromParent();
		positioner = null;
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
				context.mouseX, context.mouseY), LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;
		Location tableLocation = new WidgetLocation(tree, context.boundaryPanel);
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
			TreeRow row = tree.renderer.getRows().get(targetRow == -1 ? 0 : targetRow);
			Location widgetLocation = new WidgetLocation(row, context.boundaryPanel);
			context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop()
					+ (targetRow == -1 ? 0 : row.getOffsetHeight()));
		}else{
			Location headerLocation = new WidgetLocation(tree.view.header,context.boundaryPanel);
			context.boundaryPanel.add(positioner,tableLocation.getLeft(),headerLocation.getTop()+tree.view.header.getOffsetHeight());
		}
		validDrop = true;
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
		return p;
	}

}
