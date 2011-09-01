package org.openelis.test.client.dropdown;

import java.util.ArrayList;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.table.Row;
import org.openelis.gwt.widget.table.Table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class DropdownTestViewImpl extends Screen implements DropdownTestView {

	Dropdown         test;
	Dropdown<String> field, tCase;
	TextBox<String>  value;
	TextBox<Integer> visibleItems;
	CheckBox         enabled, required, query;
	Table            testModel;
	Button           setModel,addRow,removeRow;

	public DropdownTestViewImpl() {
		super((ScreenDefInt)GWT.create(DropdownTestDef.class));
		initialize();
	}
	
	public void initialize() {
		ArrayList<Row> model;
		ArrayList<Item<String>> items;
		
		test = (Dropdown)def.getWidget("test");
		test.setEnabled(true);
		test.addValueChangeHandler(new ValueChangeHandler() {
			public void onValueChange(ValueChangeEvent event) {
				if(event.getValue() != null)
					value.setValue(event.getValue().toString());
			}
		});
		
		value = (TextBox)def.getWidget("value");
		value.setEnabled(false);
		
		visibleItems = (TextBox<Integer>)def.getWidget("visibleItems");
		visibleItems.setEnabled(true);
		visibleItems.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			public void onValueChange(ValueChangeEvent<Integer> event) {
				test.setVisibleItemCount(event.getValue());
			}
		});
		visibleItems.setValue(10);
		
		enabled = (CheckBox)def.getWidget("enabled");
		enabled.setEnabled(true);
		enabled.setValue("Y");
		enabled.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setEnabled("Y".equals(event.getValue()));
			}
		});
		
		required = (CheckBox)def.getWidget("required");
		required.setEnabled(true);
		required.addValueChangeHandler(new ValueChangeHandler<String>()  {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setRequired("Y".equals(event.getValue()));
			}
		});
		
		query = (CheckBox)def.getWidget("query");
		query.setEnabled(true);
		query.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setQueryMode("Y".equals(event.getValue()));
			}
		});
		
		field = (Dropdown<String>)def.getWidget("field");
		field.setEnabled(true);
		items = new ArrayList<Item<String>>();
		items.add(new Item<String>("String","String"));
		items.add(new Item<String>("Integer","Integer"));
		items.add(new Item<String>("Double","Double"));
		items.add(new Item<String>("Date","Date"));
		field.setModel(items);
		field.setValue("String");
		
		tCase = (Dropdown<String>)def.getWidget("case");
		tCase.setEnabled(true);
		items = new ArrayList<Item<String>>();
		items.add(new Item<String>("mixed","Mixed"));
		items.add(new Item<String>("upper","UPPER"));
		items.add(new Item<String>("lower","lower"));
		tCase.setModel(items);
		tCase.setValue("mixed");
		tCase.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setCase(TextBox.Case.valueOf(event.getValue().toUpperCase()));
			}
		});
		
		testModel = (Table)def.getWidget("model");
		testModel.setEnabled(true);
		
		model = new ArrayList<Row>();
		model.add(new Row("1","Row 1"));
		model.add(new Row("2","Row 2"));
		model.add(new Row("3","Row 3"));
		model.add(new Row("4","Row 4"));
		model.add(new Row("5","Row 5"));
		model.add(new Row("6","Row 6"));
		model.add(new Row("7","Row 7"));
		model.add(new Row("8","Row 8"));
		model.add(new Row("9","Row 9"));
		model.add(new Row("10","Row 10"));
		model.add(new Row("11","Row 11"));
		model.add(new Row("12","Row 12"));
		model.add(new Row("13","Row 13"));
		model.add(new Row("14","Row 14"));
		model.add(new Row("15","Row 15"));
		
		testModel.setModel(model);
		
		setTestItems();
		
		setModel = (Button)def.getWidget("setModel");
		setModel.setEnabled(true);
		setModel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setTestItems();
			}
		});
			
		addRow = (Button)def.getWidget("addRow");
		addRow.setEnabled(true);
		addRow.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				testModel.addRow();
			}
		});
		
		removeRow = (Button)def.getWidget("removeRow");
		removeRow.setEnabled(true);
		removeRow.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				testModel.removeRowAt(testModel.getSelectedRow());
			}
		});
		
	}
	
	private void setTestItems() {
		ArrayList<Item> model;
		
		model = new ArrayList<Item>();
		
		if(testModel.getRowCount() == 0) {
			test.setModel(null);
			return;
		}
		
		for(int i = 0; i < testModel.getRowCount(); i++) 
			model.add(new Item(testModel.getRowAt(i).getCell(0),testModel.getRowAt(i).getCell(1)));
		
		test.setModel(model);	
	}
	
	
}
