package org.openelis.test.client.dropdown;

import java.util.ArrayList;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.table.Row;
import org.openelis.gwt.widget.table.Table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;

public class DropdownScreen extends Screen {

	Dropdown          test;
	Dropdown<String>  field;
	TextBox<String>   value,css;
	TextBox<Integer>  visibleItems,maxDisplay;
	CheckBox          enabled, required, query,multi;
	Table             testModel;
	Button            setModel,addRow,removeRow,setValue,getQuery;
	

	public DropdownScreen() {
		super((ScreenDefInt)GWT.create(DropdownDef.class));
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				postConstructor();
			}
		});
	}
	
	private void postConstructor() {
		initialize();	
		initializeDropdowns();
	}
	
	public void initialize() {
		ArrayList<Row> model;
		
		test = (Dropdown)def.getWidget("test");
		test.setEnabled(true);
		test.addValueChangeHandler(new ValueChangeHandler() {
			public void onValueChange(ValueChangeEvent event) {
				if(event.getValue() == null)
					value.setValue("");
				else if(test.isMultSelect()) {
					value.setValue(test.getValues().toString());
				}else if(event.getValue() != null)
						value.setValue(event.getValue().toString());
			}
		});
				
		value = (TextBox)def.getWidget("value");
		value.setEnabled(true);
		
		setValue = (Button)def.getWidget("setValue");
		setValue.setEnabled(true);
		setValue.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ArrayList list = new ArrayList();
				String val = value.getValue();
				String[] vals;
				
				if(!test.isMultSelect()) {
					test.setValue(val);
					return;
				}

				if(val == null || val.equals("") || val.equals("[]"))
					test.setValues(null);
				
				if(val.startsWith("["))
					val = val.substring(1,val.length()-1);
				
				vals = val.split(",");
				
				for(String v : vals)
					list.add(v);
				
				test.setValue(list);	
					
			}
		});
		
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
		
		multi = (CheckBox)def.getWidget("multi");
		multi.setEnabled(true);
		multi.addValueChangeHandler(new ValueChangeHandler<String>()  {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setMultiSelect("Y".equals(event.getValue()));
			}
		});
		
		query = (CheckBox)def.getWidget("query");
		query.setEnabled(true);
		query.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setQueryMode("Y".equals(event.getValue()));
			}
		});
		
		getQuery = (Button)def.getWidget("getQuery");
		getQuery.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				QueryData qd;
				
				qd = (QueryData)test.getQuery();
				
				Window.alert(qd != null ? qd.getQuery() : "null");
			}
		});
		
		maxDisplay = (TextBox<Integer>)def.getWidget("maxDisplay");
		maxDisplay.setEnabled(true);
		maxDisplay.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			public void onValueChange(ValueChangeEvent<Integer> event) {
				test.setMaxDisplay(event.getValue());
			}
		});
		maxDisplay.setValue(3);
		
		field = (Dropdown<String>)def.getWidget("field");
		field.setEnabled(true);

		css = (TextBox<String>)def.getWidget("css");
		css.setEnabled(true);
		css.setValue(test.getStyleName());
		css.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setStyleName(event.getValue());
			}
		});
				
		testModel = (Table)def.getWidget("model");
		testModel.setEnabled(true);
		
		model = new ArrayList<Row>();
		model.add(new Row("1","Alabama","Y"));
		model.add(new Row("2","Alaska","Y"));
		model.add(new Row("3","Arizona","Y"));
		model.add(new Row("4","Arkansas","Y"));
		model.add(new Row("5","California","Y"));
		model.add(new Row("6","Colorado","Y"));
		model.add(new Row("7","Connecticut","Y"));
		model.add(new Row("8","Delaware","Y"));
		model.add(new Row("9","Florida","Y"));
		model.add(new Row("10","Georgia","Y"));
		model.add(new Row("11","Hawaii","Y"));
		model.add(new Row("12","Idaho","Y"));
		model.add(new Row("13","Illinois","Y"));
		model.add(new Row("14","Indiana","Y"));
		model.add(new Row("15","Iowa","Y"));
		model.add(new Row("16","Kansas","Y"));
		model.add(new Row("17","Kentucky","Y"));
		model.add(new Row("18","Louisiana","Y"));
		model.add(new Row("19","Maine","Y"));
		model.add(new Row("20","Maryland","Y"));
		model.add(new Row("21","Massachusetts","Y"));
		model.add(new Row("22","Michigan","Y"));
		model.add(new Row("23","Minnesota","Y"));
		model.add(new Row("24","Mississippi","Y"));
		model.add(new Row("25","Missouri","Y"));
		model.add(new Row("26","Montana","Y"));
		model.add(new Row("27","Nebraska","Y"));
		model.add(new Row("28","Nevada","Y"));
		model.add(new Row("29","New Hampshire","Y"));
		model.add(new Row("30","New Jersey","Y"));
		model.add(new Row("31","New Mexico","Y"));
		model.add(new Row("32","New York","Y"));
		model.add(new Row("33","North Carolina","Y"));
		model.add(new Row("34","North Dakota","Y"));
		model.add(new Row("35","Ohio","Y"));
		model.add(new Row("36","Oklahoma","Y"));
		model.add(new Row("37","Oregon","Y"));
		model.add(new Row("38","Pennsylvania","Y"));
		model.add(new Row("39","Rhode Island","Y"));
		model.add(new Row("40","South Carolina","Y"));
		model.add(new Row("41","South Dakota","Y"));
		model.add(new Row("42","Tennessee","Y"));
		model.add(new Row("43","Texas","Y"));
		model.add(new Row("44","Utah","Y"));
		model.add(new Row("45","Vermont","Y"));
		model.add(new Row("46","Virginia","Y"));
		model.add(new Row("47","Washington","Y"));
		model.add(new Row("48","West Virginia","Y"));
		model.add(new Row("49","Wisconsin","Y"));
		model.add(new Row("50","Wyoming","Y"));
		
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
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				((CollapsePanel)def.getWidget("collapsePanel")).open();
			}
		});
				
	}
	
	private void setTestItems() {
		ArrayList<Item> model;
		Item item;
		
		model = new ArrayList<Item>();
		
		if(testModel.getRowCount() == 0) {
			test.setModel(null);
			return;
		}
		
		for(int i = 0; i < testModel.getRowCount(); i++) { 
			 item = new Item(testModel.getRowAt(i).getCell(0),testModel.getRowAt(i).getCell(1));
			 item.setEnabled("Y".equals(testModel.getRowAt(i).getCell(2)));
			 model.add(item);
		}
		
		
		test.setModel(model);	
	}
	
	private void initializeDropdowns() {
		ArrayList<Item<String>> items;
		
		items = new ArrayList<Item<String>>();
		items.add(new Item<String>("String","String"));
		items.add(new Item<String>("Integer","Integer"));
		items.add(new Item<String>("Double","Double"));
		items.add(new Item<String>("Date","Date"));
		field.setModel(items);
		field.setValue("String");		
	}
	
	
}
