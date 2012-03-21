package org.openelis.test.client.editbox;

import java.util.ArrayList;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.EditBox;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.TextBase;
import org.openelis.gwt.widget.TextBox;
import org.openelis.test.client.Application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;


public class EditBoxScreen extends Screen {
	
	EditBox            test;
	TextBox<String>    value,css;
	TextBox<Integer>   maxlength;
	Dropdown<String>  tCase;
	CheckBox           enabled,required,query;
	Button             setValue,getQuery;
	
	public EditBoxScreen() {
		super((ScreenDefInt)GWT.create(EditBoxDef.class));
		
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
		test = (EditBox)def.getWidget("test");
		test.setEnabled(true);
		test.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				Application.logger().info(log("value changed - "+event.getValue()));
				if(event.getValue() != null)
					value.setValue(test.getValue());
				else
					value.setValue(null);
			}			
		});
					
		maxlength = (TextBox<Integer>)def.getWidget("maxlength");
		maxlength.setEnabled(true);
		maxlength.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			public void onValueChange(ValueChangeEvent<Integer> event) {
				Application.logger().info(log("maxLength changed - "+event.getValue()));
				test.setMaxLength(event.getValue());
			}
		});
		
		value = (TextBox)def.getWidget("value");
		value.setEnabled(true);
		
		setValue = (Button)def.getWidget("setValue");
		setValue.setEnabled(true);
		setValue.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Application.logger().info(log("value being set to - "+value.getValue()));
				test.setValue(value.getValue());
			}
		});
		
		enabled = (CheckBox)def.getWidget("enabled");
		enabled.setEnabled(true);
		enabled.setValue("Y");
		enabled.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				Application.logger().info(log("enabled changed - "+event.getValue()));
				test.setEnabled("Y".equals(event.getValue()));
			}
		});
		
		required = (CheckBox)def.getWidget("required");
		required.setEnabled(true);
		required.addValueChangeHandler(new ValueChangeHandler<String>()  {
			public void onValueChange(ValueChangeEvent<String> event) {
				Application.logger().info(log("required changed - "+event.getValue()));
				test.setRequired("Y".equals(event.getValue()));
			}
		});
		
		query = (CheckBox)def.getWidget("query");
		query.setEnabled(true);
		query.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				Application.logger().info(log("query changed - "+event.getValue()));
				test.setQueryMode("Y".equals(event.getValue()));
				getQuery.setEnabled("Y".equals(event.getValue()));
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
		
		css = (TextBox<String>)def.getWidget("css");
		css.setEnabled(true);
		css.setValue(test.getStyleName());
		css.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setStyleName(event.getValue());
			}
		});
		
		tCase = (Dropdown<String>)def.getWidget("case");
		tCase.setEnabled(true);
		tCase.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				Application.logger().info("case changed - "+event.getValue());
				test.setCase(TextBase.Case.valueOf(event.getValue().toUpperCase()));
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
		model.add(new Item<String>("mixed","Mixed"));
		model.add(new Item<String>("upper","UPPER"));
		model.add(new Item<String>("lower","lower"));
		tCase.setModel(model);
		tCase.setValue("mixed");

	}
	
    private String log(String message) {
    	try {
    		return getClass().getName() + " - " +message;
    	}catch(Exception e) {
    		return message;
    	}
    }

}
