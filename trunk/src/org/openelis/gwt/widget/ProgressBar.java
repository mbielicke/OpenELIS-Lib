package org.openelis.gwt.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProgressBar extends Composite {
	
	AbsolutePanel prog = new AbsolutePanel();
	Label label = new Label();
	Label pct = new Label();
	
	public ProgressBar() {
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("200px");
		AbsolutePanel cont = new AbsolutePanel();
		cont.setSize("175px","20px");
		cont.setStyleName("ProgressBarOuter");
		cont.add(prog,0,0);
		cont.add(pct,80,0);
		vp.add(cont);
		vp.setCellHorizontalAlignment(cont, HasAlignment.ALIGN_CENTER);
		pct.setStyleName("ProgressBarPct");
		DOM.setStyleAttribute(pct.getElement(), "zIndex", "100");
		prog.setHeight("100%");
		prog.setWidth("0%");
		prog.setStyleName("ProgressBar");
		label.setStyleName("ProgressBarMessage");
		vp.add(label);
		DecoratorPanel dp = new DecoratorPanel();
		dp.setStyleName("ErrorWindow");
		dp.add(vp);
		initWidget(dp);
	}
	
	public void setProgress(int percent) {
		prog.setWidth(percent+"%");
		pct.setText(percent+"%");
	}
	
	public void setMessage(String message) {
		label.setText(message);
	}
	

}
