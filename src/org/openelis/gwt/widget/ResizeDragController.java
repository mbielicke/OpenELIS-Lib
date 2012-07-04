package org.openelis.gwt.widget;


	import com.google.gwt.user.client.ui.AbsolutePanel;
	import com.google.gwt.user.client.ui.Widget;

	import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
	


	public final class ResizeDragController extends AbstractDragController {
		Window window;
		int startX, startY;

	  public ResizeDragController(AbsolutePanel boundaryPanel,Window window) {
	    super(boundaryPanel);
	    this.window = window;
	  }
	  
	  
	  
		@Override
		public void dragStart() {
			super.dragStart();
			startX = context.mouseX;
			startY = context.mouseY;
			
		}

	@Override
	public void dragMove() {
		int deltaX = context.mouseX - startX;
		int deltaY = context.mouseY - startY;
		window.content.setHeight((window.content.getOffsetHeight()+deltaY)+"px");
		window.content.setWidth((window.content.getOffsetWidth()+deltaX)+"px");
		startX = context.mouseX;
		startY = context.mouseY;
		
	}


	}

