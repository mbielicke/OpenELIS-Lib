package org.openelis.gwt.widget.web;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LinkButton extends FocusPanel {
	DecoratorPanel dp,pdp;
	AbsolutePanel ap;
	PopupPanel pop;
	Timer timer;
	
	public LinkButton(String icon, String label, String popup, int width, int height) {
		VerticalPanel vp = new VerticalPanel();
		//dp = new DecoratorPanel();
		//dp.setStyleName("ConfirmWindow");
		//dp.setSize(width+"px",height+"px");
		ap = new AbsolutePanel();
		ap.setSize(width+"px", height+"px");
		ap.setStyleName(icon);
		//dp.add(ap);
		vp.add(ap);
		if(label != null) {
			Label lb = new Label(label);
			lb.setStyleName("menuLabel");
			vp.add(lb);
			vp.setCellHorizontalAlignment(vp.getWidget(1), HasAlignment.ALIGN_CENTER);
		}
		if (popup != null) {
			timer = new Timer() {
				public void run() {
					pop.hide();
				}
			};
			
			Label lb = new Label(popup);
			lb.setStyleName("ScreenLabel");
			
			pdp = new DecoratorPanel();
	        pdp.setStyleName("ErrorWindow");
	        pdp.add(lb);
	        pdp.setVisible(true);

	        pop = new PopupPanel();
	        pop.setWidget(pdp);
	        pop.setStyleName("LinkPop");
	        
			addMouseOverHandler(new MouseOverHandler() {
				public void onMouseOver(MouseOverEvent event) {
					if(pop.isShowing())
						return;
					pop.setPopupPosition(getAbsoluteLeft()+getOffsetWidth(), getAbsoluteTop()-10);
					pop.show();
			        timer.schedule(10000); 
					
				}
			});
			addMouseOutHandler(new MouseOutHandler() {
				public void onMouseOut(MouseOutEvent event) {
					timer.cancel();
					pop.hide();
				}
			});
			
			addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					timer.cancel();
					pop.hide();
				}
			});
		}
		setWidget(vp);
		setWidth(width+"px");
	}
	
	@Override
	public void setSize(String width, String height) {
		ap.setSize(width, height);
		setWidth(width);
	}

}
