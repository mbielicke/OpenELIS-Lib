package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.resources.OpenELISResources;
import org.openelis.gwt.resources.TabPanelCSS;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class DeckPanel extends com.google.gwt.user.client.ui.DeckPanel {
	
	private ArrayList<String> keyTabList = new  ArrayList<String>();
	private String width;	
	private String height;
	
	protected TabPanelCSS css;
	
	public void add(Widget wid, String tab) {
		keyTabList.add(tab);
		ScrollPanel scroll = new ScrollPanel();
		scroll.setWidget(wid);
		super.add(scroll);
		scroll.setWidth(width);
		scroll.setHeight(height);
		setCSS(OpenELISResources.INSTANCE.tabpanel());
	}
	
	@Override
	public void setWidth(String width) {
		this.width = width;
		//super.setWidth(width);
	}
	
	@Override
	public void setHeight(String height) {
		this.height = height;
		//super.setHeight(height);
	}
	
	public String getNextTabWidget() {
		return keyTabList.get(getVisibleWidget()).split(",")[0];
	}
	
	public String getPrevTabWidget() {
		return keyTabList.get(getVisibleWidget()).split(",")[1];
	}
	
	public void setCSS(TabPanelCSS css) {
		css.ensureInjected();
		this.css = css;
		setStyleName(css.TabPanelBottom());
	}

}
