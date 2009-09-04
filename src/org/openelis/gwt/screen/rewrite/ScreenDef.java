package org.openelis.gwt.screen.rewrite;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;

public class  ScreenDef  {
	
	protected String loadURL;
	protected String xmlDef;
	protected Document xml;
	protected ScreenPanel panel;
	protected HashMap<String,Widget> widgets;
	private boolean keepDoc;
	public String name;
	
	public class ScreenPanel extends AbsolutePanel implements HasClickHandlers {
		
		public void addShortcutHandler(UIUtil.ShortcutHandler handler) {
			addDomHandler(handler,KeyPressEvent.getType());
		}

		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return addDomHandler(handler, ClickEvent.getType());
		}

	}

	public ScreenDef() {
		xmlDef = null;
		xml = null;
		loadURL = null;
		keepDoc = false;
		widgets = new HashMap<String,Widget>();
		panel = new ScreenPanel();
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
	    xmlDef = xml;
	}
	
	public String getXMLString() {
		return xmlDef;
	}
	
	public String getName(){
	    return name;
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
	
	public ScreenPanel getPanel() {
		return panel;
	}
}
