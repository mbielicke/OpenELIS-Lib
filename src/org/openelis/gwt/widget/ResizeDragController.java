package org.openelis.gwt.widget;


	import org.openelis.gwt.screen.ViewPanel;

import com.google.gwt.user.client.ui.AbsolutePanel;
	import com.google.gwt.user.client.ui.Widget;

	import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
	


	public final class ResizeDragController extends AbstractDragController {
		WindowInt window;
		int startX, startY;

	  public ResizeDragController(AbsolutePanel boundaryPanel,WindowInt window) {
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
		//((ViewPanel)window.content).setHeight((window.content.getOffsetHeight()+deltaY));
		//((ViewPanel)window.content).setWidth((window.content.getOffsetWidth()+deltaX));
		startX = context.mouseX;
		startY = context.mouseY;
		
	}


	}

