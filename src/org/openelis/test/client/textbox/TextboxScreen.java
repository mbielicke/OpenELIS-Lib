package org.openelis.test.client.textbox;

import java.util.ArrayList;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.IntegerHelper;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;


public class TextboxScreen extends Screen {
	
	TextBox           test;
	TextBox<String>   mask,pattern,value;
	TextBox<Integer>  maxlength;
	CheckBox          enabled,required,query;
	Dropdown<String>  field,tCase,alignment,logLevel;
	
	public TextboxScreen() {
		super((ScreenDefInt)GWT.create(TextboxDef.class));
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				postConstruct();
			}
		});
		
	}
	
	public void postConstruct() {
		initialize();
		initializeDropdowns();
	}
	
	public void initialize() {
		test = (TextBox)def.getWidget("test");
		test.setEnabled(true);
		test.addValueChangeHandler(new ValueChangeHandler() {

			public void onValueChange(ValueChangeEvent event) {
				if(event.getValue() != null)
					value.setValue(test.getValue().toString());
				else
					value.setValue("NULL");
			}			
		});
			
		
		mask = (TextBox<String>)def.getWidget("mask");
		mask.setEnabled(true);
		mask.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.getHelper().setMask(event.getValue());
			}
		});
		
		pattern = (TextBox<String>)def.getWidget("pattern");
		pattern.setEnabled(true);
		pattern.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				((IntegerHelper)test.getHelper()).setPattern(event.getValue());
			}
		});
		
		maxlength = (TextBox<Integer>)def.getWidget("maxlength");
		maxlength.setEnabled(true);
		maxlength.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			public void onValueChange(ValueChangeEvent<Integer> event) {
				test.setMaxLength(event.getValue());
			}
		});
		
		value = (TextBox)def.getWidget("value");
		value.setEnabled(false);
		
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
		
		tCase = (Dropdown<String>)def.getWidget("case");
		tCase.setEnabled(true);
		tCase.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setCase(TextBox.Case.valueOf(event.getValue().toUpperCase()));
			}
		});
		
		alignment = (Dropdown<String>)def.getWidget("alignment");
		alignment.setEnabled(true);

		alignment.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setTextAlignment(TextAlignment.valueOf(event.getValue().toUpperCase()));
			}
		});
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				((CollapsePanel)def.getWidget("collapsePanel")).open();
			}
		});
		
	}
	
	private void initializeDropdowns() {
		ArrayList<Item<String>> model;
		
		model = new ArrayList<Item<String>>();
		model.add(new Item<String>("String","String"));
		model.add(new Item<String>("Integer","Integer"));
		model.add(new Item<String>("Double","Double"));
		model.add(new Item<String>("Date","Date"));
		field.setModel(model);
		field.setValue("String");
		
		model = new ArrayList<Item<String>>();
		model.add(new Item<String>("mixed","Mixed"));
		model.add(new Item<String>("upper","UPPER"));
		model.add(new Item<String>("lower","lower"));
		tCase.setModel(model);
		tCase.setValue("mixed");
		
		model = new ArrayList<Item<String>>();
		model.add(new Item<String>("left","left"));
		model.add(new Item<String>("center","center"));
		model.add(new Item<String>("right","right"));
		alignment.setModel(model);
		alignment.setValue("left");

	}


}
