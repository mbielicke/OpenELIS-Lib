package org.openelis.test.client;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Label;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class IndexViewImpl extends Screen implements IndexView {
	
	Label<String> textbox;
	Label<String> dropdown;
	Label<String> table;
	
	IndexView.Presenter presenter;
	
	public IndexViewImpl() {
		super((ScreenDefInt)GWT.create(IndexScreenDef.class));
		initialize();
	}
	
	private void initialize() {
		textbox = (Label<String>)def.getWidget("textbox");
		textbox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				presenter.goToTextBoxTest();
			}
		});
		
		dropdown = (Label<String>)def.getWidget("dropdown");
		dropdown.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				presenter.goToDropdownTest();
			}
		});
		
		table = (Label<String>)def.getWidget("table");
		table.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.goToTableTest();
			}
		});
	}
	
	public void setPresenter(IndexView.Presenter presenter) {
		this.presenter = presenter;
	}

}
