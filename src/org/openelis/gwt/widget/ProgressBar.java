package org.openelis.gwt.widget;

import org.openelis.gwt.resources.OpenELISResources;
import org.openelis.gwt.resources.ProgressCSS;

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
	
	protected ProgressCSS css;
	
	public ProgressBar() {
		css = OpenELISResources.INSTANCE.progress();
		css.ensureInjected();
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("200px");
		AbsolutePanel cont = new AbsolutePanel();
		cont.setSize("175px","20px");
		cont.setStyleName(css.ProgressBarOuter());
		cont.add(prog,0,0);
		cont.add(pct,80,0);
		vp.add(cont);
		vp.setCellHorizontalAlignment(cont, HasAlignment.ALIGN_CENTER);
		pct.setStyleName(css.ProgressBarPct());
		DOM.setStyleAttribute(pct.getElement(), "zIndex", "100");
		prog.setHeight("100%");
		prog.setWidth("0%");
		prog.setStyleName(css.ProgressBar());
		label.setStyleName(css.ProgressBarMessage());
		vp.add(label);
		DecoratorPanel dp = new DecoratorPanel();
		//dp.setStyleName(css.ErrorWindow());
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
