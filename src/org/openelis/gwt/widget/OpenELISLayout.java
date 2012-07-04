package org.openelis.gwt.widget;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class OpenELISLayout extends Composite {
	
	protected DockLayoutPanel dock;
	protected CollapsePanel collapse;
	protected Widget content;

	public OpenELISLayout() {
		
		dock = new DockLayoutPanel(Unit.PX);
		collapse = new CollapsePanel(false);
		collapse.setStyleName("LeftSidePanel");
		dock.addWest(collapse,10);
		initWidget(dock);
		
		collapse.addResizeHandler(new ResizeHandler() {
			
			@Override
			public void onResize(ResizeEvent event) {
				dock.setWidgetSize(collapse, event.getWidth()+10);
				content.setWidth((content.getOffsetWidth()+event.getWidth()+10)+"px");
				//dock.setPixelSize((width+event.getWidth()+10),height);
			}
		});
	}
	
	public void addLeft(Widget widget) {
		collapse.setContent(widget);
	}
	
	public void addContent(Widget content) {
		this.content = content;
		dock.add(content);
	}
	/*
	public void setWidth(int width) {
		this.width = width;
		dock.setPixelSize(width,height);
	}
	
	public void setHeight(int height) {
		this.height = height;
		dock.setPixelSize(width, height);
	}
	*/
	
}
