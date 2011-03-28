package org.openelis.gwt.widget.web;

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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

public class LinkButton extends FocusPanel {
	DecoratorPanel dp,pdp;
	PopupPanel pop;
	Timer timer;
	
	public LinkButton(String icon, String label, String popup, int width, int height) {
		VerticalPanel vp = new VerticalPanel();
		dp = new DecoratorPanel();
		dp.setStyleName("ConfirmWindow");
		dp.setSize(width+"px",height+"px");
		AbsolutePanel ap = new AbsolutePanel();
		ap.setStyleName(icon);
		dp.add(ap);
		vp.add(dp);
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
			lb.setStyleName("menuLabel");
			
	        pdp.setStyleName("ErrorWindow");
	        pdp.add(lb);
	        pdp.setVisible(true);

	        pop.setWidget(pdp);
	        pop.setStyleName("");
	        
			addMouseOverHandler(new MouseOverHandler() {
				public void onMouseOver(MouseOverEvent event) {
					final int x = event.getClientX();
					final int y = event.getClientY();
			        pop.setPopupPositionAndShow(new PositionCallback() {
			            public void setPosition(int offsetWidth, int offsetHeight) {
			                int offset = x;
			                if(x+offsetWidth > Window.getClientWidth())  
			                    offset -= x + offsetWidth - Window.getClientWidth() - 10;

			                pop.setPopupPosition(offset,y+10);
			            }
			        });

			        timer.schedule(5000); 
					
				}
			});
			addMouseOutHandler(new MouseOutHandler() {
				public void onMouseOut(MouseOutEvent event) {
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
		dp.setSize(width, height);
		setWidth(width);
	}
	

}
