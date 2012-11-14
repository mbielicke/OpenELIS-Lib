package org.openelis.test.client.autocomplete;

import java.util.ArrayList;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.event.GetMatchesEvent;
import org.openelis.gwt.event.GetMatchesHandler;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.AutoComplete;
import org.openelis.gwt.widget.AutoCompleteValue;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.TextBase;
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

public class AutoCompleteScreen extends Screen {

	AutoComplete      test;
	Dropdown<String> tCase;
	TextBox<String>   value,css;
	TextBox<Integer>  visibleItems;
	CheckBox          enabled, required, query;
	Table             testModel;
	Button            setModel,addRow,removeRow,setValue,getQuery;
	ArrayList<Row>    model;
	

	public AutoCompleteScreen() {
		super((ScreenDefInt)GWT.create(AutoCompleteDef.class));
		
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
		
		
		test = (AutoComplete)def.getWidget("test");
		test.setEnabled(true);
		test.addValueChangeHandler(new ValueChangeHandler<AutoCompleteValue>() {
			public void onValueChange(ValueChangeEvent<AutoCompleteValue> event) {
				if(event.getValue() != null)
					value.setValue(event.getValue().getId()+","+event.getValue().getDisplay());
			}
		});
		
		test.addGetMatchesHandler(new GetMatchesHandler() {
			public void onGetMatches(GetMatchesEvent event) {
				ArrayList<Item<Integer>> matches;
				
				matches = new ArrayList<Item<Integer>>();
				
				for(Row row : model) 
					if(((String)row.getCell(1)).toUpperCase().startsWith(event.getMatch().toUpperCase()))
						matches.add(new Item<Integer>(Integer.valueOf((String)row.getCell(0)),(String)row.getCell(1)));
						
				
				test.showAutoMatches(matches);
				
			}
		});
		
		value = (TextBox)def.getWidget("value");
		value.setEnabled(true);
		
		setValue = (Button)def.getWidget("setValue");
		setValue.setEnabled(true);
		setValue.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String[] vals;
				
				vals = value.getValue().split(",");
				
				test.setValue(Integer.valueOf(vals[0]),vals[1]);
			}
		});
		
		visibleItems = (TextBox<Integer>)def.getWidget("visibleItems");
		visibleItems.setEnabled(true);
		visibleItems.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			public void onValueChange(ValueChangeEvent<Integer> event) {
				test.setVisibleItems(event.getValue());
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
		
		getQuery = (Button)def.getWidget("getQuery");
		getQuery.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				QueryData qd;
				
				qd = (QueryData)test.getQuery();
				
				Window.alert(qd != null ? qd.getQuery() : "null");
			}
		});
		
		tCase = (Dropdown<String>)def.getWidget("case");
		tCase.setEnabled(true);

		tCase.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setCase(TextBase.Case.valueOf(event.getValue().toUpperCase()));
			}
		});
		
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
		model.add(new Row("1","Alabama"));
		model.add(new Row("2","Alaska"));
		model.add(new Row("3","Arizona"));
		model.add(new Row("4","Arkansas"));
		model.add(new Row("5","California"));
		model.add(new Row("6","Colorado"));
		model.add(new Row("7","Connecticut"));
		model.add(new Row("8","Delaware"));
		model.add(new Row("9","Florida"));
		model.add(new Row("10","Georgia"));
		model.add(new Row("11","Hawaii"));
		model.add(new Row("12","Idaho"));
		model.add(new Row("13","Illinois"));
		model.add(new Row("14","Indiana"));
		model.add(new Row("15","Iowa"));
		model.add(new Row("16","Kansas"));
		model.add(new Row("17","Kentucky"));
		model.add(new Row("18","Louisiana"));
		model.add(new Row("19","Maine"));
		model.add(new Row("20","Maryland"));
		model.add(new Row("21","Massachusetts"));
		model.add(new Row("22","Michigan"));
		model.add(new Row("23","Minnesota"));
		model.add(new Row("24","Mississippi"));
		model.add(new Row("25","Missouri"));
		model.add(new Row("26","Montana"));
		model.add(new Row("27","Nebraska"));
		model.add(new Row("28","Nevada"));
		model.add(new Row("29","New Hampshire"));
		model.add(new Row("30","New Jersey"));
		model.add(new Row("31","New Mexico"));
		model.add(new Row("32","New York"));
		model.add(new Row("33","North Carolina"));
		model.add(new Row("34","North Dakota"));
		model.add(new Row("35","Ohio"));
		model.add(new Row("36","Oklahoma"));
		model.add(new Row("37","Oregon"));
		model.add(new Row("38","Pennsylvania"));
		model.add(new Row("39","Rhode Island"));
		model.add(new Row("40","South Carolina"));
		model.add(new Row("41","South Dakota"));
		model.add(new Row("42","Tennessee"));
		model.add(new Row("43","Texas"));
		model.add(new Row("44","Utah"));
		model.add(new Row("45","Vermont"));
		model.add(new Row("46","Virginia"));
		model.add(new Row("47","Washington"));
		model.add(new Row("48","West Virginia"));
		model.add(new Row("49","Wisconsin"));
		model.add(new Row("50","Wyoming"));
		
		
		testModel.setModel(model);
		
		//setTestItems();
		
		setModel = (Button)def.getWidget("setModel");
		setModel.setEnabled(true);
		setModel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//setTestItems();
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
	
	
	private void initializeDropdowns() {
		ArrayList<Item<String>> items;
		
		items = new ArrayList<Item<String>>();
		items.add(new Item<String>("mixed","Mixed"));
		items.add(new Item<String>("upper","UPPER"));
		items.add(new Item<String>("lower","lower"));
		tCase.setModel(items);
		tCase.setValue("mixed");
	}
	
	
}
