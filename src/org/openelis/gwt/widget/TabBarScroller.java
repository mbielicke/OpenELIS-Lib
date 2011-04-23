package org.openelis.gwt.widget;

import org.openelis.gwt.common.Util;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.VisibleEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;

public class TabBarScroller extends Composite implements ClickHandler, MouseDownHandler, MouseUpHandler {

	private AbsolutePanel ap;
	private Grid hp;
	private IconContainer leftArrow;
	private IconContainer rightArrow;
	private int intWidth = 0;
	private Timer timer;
	private TabBar bar;

	public TabBarScroller(TabBar bar) {
		this.bar = bar;
		hp = new Grid(1,3);
		leftArrow = new IconContainer("MoveLeft");
		rightArrow = new IconContainer("MoveRight");
		leftArrow.addClickHandler(this);
		rightArrow.addClickHandler(this); 
		leftArrow.addMouseDownHandler(this);
		rightArrow.addMouseDownHandler(this);
		leftArrow.addMouseUpHandler(this);
		rightArrow.addMouseUpHandler(this);
		ap = new AbsolutePanel();
		ap.setHeight("20px");
		DOM.setStyleAttribute(ap.getElement(), "overflow", "hidden");
		ap.add(bar);
		hp.setWidget(0,0,leftArrow);
		hp.setWidget(0,1,ap);
		hp.setWidget(0,2,rightArrow);
		hp.getCellFormatter().setVisible(0,0,false);
		hp.getCellFormatter().setVisible(0,2,false);
		hp.setCellPadding(0);
		hp.setCellSpacing(0);
		initWidget(hp);
		addVisibleHandler(new VisibleEvent.Handler() {			
			public void onVisibleOrInvisible(VisibleEvent event) {
				if(event.isVisible())
					checkScroll();
			}
		});
	}

	public void onClick(ClickEvent event) {
		int pos = 0;
		if(event.getSource() == leftArrow) {
			if(ap.getWidgetLeft(bar) < 0){ 
				pos = ap.getWidgetLeft(bar)+15;
				if(pos > 0)
					pos = 0;
				ap.setWidgetPosition(bar,pos, 0);
			}
		}
		if(event.getSource() == rightArrow){
			if(ap.getWidgetLeft(bar) > -(bar.getOffsetWidth()-(ap.getOffsetWidth()+18))){
				pos = ap.getWidgetLeft(bar)-15;
				if(ap.getWidgetLeft(bar) < -(bar.getOffsetWidth()-(ap.getOffsetWidth()+18)))
					pos = -(bar.getOffsetWidth()-(ap.getOffsetWidth()+3));	
				ap.setWidgetPosition(bar,pos, 0);
			}else{
				ap.setWidgetPosition(bar, -(bar.getOffsetWidth()-(ap.getOffsetWidth()+3)), 0);
			}
		}  
		checkScroll();
	}

	public void onMouseDown(MouseDownEvent event) {
		if(event.getSource() == leftArrow){
			timer = new Timer() {
				public void run() {
					if(ap.getWidgetLeft(bar) < 0){ 
						int pos = ap.getWidgetLeft(bar)+15;
						if(pos > 0)
							pos = 0;
						ap.setWidgetPosition(bar,pos, 0);
					}
					checkScroll();
				}
			};
		}else {
			timer = new Timer() {
				public void run() {
					if(ap.getWidgetLeft(bar) > -(bar.getOffsetWidth()-(ap.getOffsetWidth()+18))){
						int pos = ap.getWidgetLeft(bar)-15;
						if(ap.getWidgetLeft(bar) < -(bar.getOffsetWidth()-(ap.getOffsetWidth()+18)))
							pos = -(bar.getOffsetWidth()-(ap.getOffsetWidth()+3));	
						ap.setWidgetPosition(bar,pos, 0);
					}else{
						ap.setWidgetPosition(bar, -(bar.getOffsetWidth()-(ap.getOffsetWidth()+3)), 0);
					}
					checkScroll();
				}	    			
			};
		}
		timer.scheduleRepeating(100);
	}

	public void onMouseUp(MouseUpEvent event) {
		if(timer != null)
			timer.cancel();
		timer = null;
	}

	public void checkScroll() {
		if(bar.getOffsetWidth() > intWidth){
			ap.setWidth((intWidth-36)+"px");
			hp.getCellFormatter().setVisible(0, 0, true);
			hp.getCellFormatter().setVisible(0, 2, true);
			  if(ap.getWidgetLeft(bar) >= 0){
				  if(leftArrow.getStyleName().indexOf("inactive") == -1 )
					  leftArrow.addStyleName("inactive");
			  }else
				  leftArrow.removeStyleName("inactive");
			  if(ap.getWidgetLeft(bar) <= -(bar.getOffsetWidth()-(ap.getOffsetWidth()+3))){
				  if(rightArrow.getStyleName().indexOf("inactive") == -1)
					  rightArrow.addStyleName("inactive");
			  }else
				  rightArrow.removeStyleName("inactive");
		}else{
			ap.setWidth(intWidth+"px");
			hp.getCellFormatter().setVisible(0, 0, false);
			hp.getCellFormatter().setVisible(0, 2, false);
		}
	}

	public void setWidth(String width) {
		intWidth = Util.stripUnits(width);
		ap.setWidth(width+"px");
	}
	
	  public void scrollToSelected() {
		  Widget wid = (Widget)bar.getTab(bar.getSelectedTab());
		  int left = wid.getAbsoluteLeft();
		  int width = wid.getOffsetWidth();
		  int barLeft = ap.getAbsoluteLeft();
		  int barWidth = ap.getOffsetWidth();
		  if(left+width > barLeft+barWidth) {
			  ap.setWidgetPosition(bar,ap.getWidgetLeft(bar)-((left+width)-(barLeft+barWidth)) , 0);
		  }else if(left < barLeft){
			  ap.setWidgetPosition(bar,ap.getWidgetLeft(bar)+(barLeft-left) , 0);
		  }
		  checkScroll();
	  }
	  
	  
}
