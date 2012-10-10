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
	
	AbsolutePanel prog = new AbsolutePanel(),cont;
	Label label = new Label();
	Label pct = new Label();
	
	protected ProgressCSS css;
	
	public ProgressBar() {
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("200px");
		cont = new AbsolutePanel();
		cont.setSize("175px","20px");

		cont.add(prog,0,0);
		cont.add(pct,80,0);
		vp.add(cont);
		vp.setCellHorizontalAlignment(cont, HasAlignment.ALIGN_CENTER);
		
		DOM.setStyleAttribute(pct.getElement(), "zIndex", "100");
		prog.setHeight("100%");
		prog.setWidth("0%");

		vp.add(label);
		DecoratorPanel dp = new DecoratorPanel();
		//dp.setStyleName(css.ErrorWindow());
		dp.add(vp);
		initWidget(dp);
		
		setCSS(OpenELISResources.INSTANCE.progress());
	}
	
	public void setProgress(int percent) {
		prog.setWidth(percent+"%");
		pct.setText(percent+"%");
	}
	
	public void setMessage(String message) {
		label.setText(message);
	}
	
	public void setCSS(ProgressCSS css) {
		css.ensureInjected();
		this.css = css;
		cont.setStyleName(css.ProgressBarOuter());
		pct.setStyleName(css.ProgressBarPct());
		prog.setStyleName(css.ProgressBar());
		label.setStyleName(css.ProgressBarMessage());
		
	}
	

}
