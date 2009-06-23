package org.openelis.gwt.screen.rewrite;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;

public class ScreenDef {
	
	protected String loadURL;
	protected String xmlDef;
	protected Document xml;
	protected Widget panel;
	protected HashMap<String,Widget> widgets;
	private boolean keepDoc;
	public String name;

	public ScreenDef() {
		xmlDef = null;
		xml = null;
		loadURL = null;
		keepDoc = false;
		widgets = new HashMap<String,Widget>();
	}
	
	public void keepDocument(boolean keep) {
		keepDoc = keep;
	}
	
	public void setLoadURL(String url) {
		loadURL = url;
	}
	
	public String getLoadURL() {
		return loadURL;
	}
	
	public void setDocument(Document doc) {
		if(keepDoc)
			xml = doc;
	}
	
	public Document getDocument() {
		return xml;
	}
	
	public void setXMLString(String xml) {
		if(keepDoc)
			xmlDef = xml;
	}
	
	public String getXMLString() {
		return xmlDef;
	}
	
	public void setWidgets(HashMap<String,Widget> widgets) {
		this.widgets = widgets;
	}
	
	public HashMap<String,Widget> getWidgets() {
		return widgets;
	}
	
	public Widget getWidget(String key) {
		return widgets.get(key);
	}
	
	public void setWidget(Widget widget, String key) {
		widgets.put(key, widget);
	}
}
