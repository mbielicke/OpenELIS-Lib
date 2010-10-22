package org.openelis.gwt.widget.web;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MenuButton extends FocusPanel {
	
	public MenuButton(String icon, String label) {
		VerticalPanel vp = new VerticalPanel();
		DecoratorPanel dp = new DecoratorPanel();
		dp.setStyleName("ConfirmWindow");
		dp.setSize("100px","100px");
		AbsolutePanel ap = new AbsolutePanel();
		ap.setStyleName(icon);
		dp.add(ap);
		vp.add(dp);
		Label lb = new Label(label);
		lb.setStyleName("webLabel");
		vp.add(lb);
		vp.setCellHorizontalAlignment(vp.getWidget(1), HasAlignment.ALIGN_CENTER);
		setWidget(vp);
	}
	

}
