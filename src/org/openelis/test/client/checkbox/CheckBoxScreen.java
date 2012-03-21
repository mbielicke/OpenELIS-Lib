package org.openelis.test.client.checkbox;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.TextBox;
import org.openelis.test.client.Application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;

public class CheckBoxScreen extends Screen {

    protected CheckBox          test;
	protected TextBox<String>   css,value;
	protected CheckBox          enabled,query;
	protected Button            getQuery,setValue;
	
	public CheckBoxScreen() {
		super((ScreenDefInt)GWT.create(CheckBoxDef.class));
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				postConstructor();
			}
		});
	}
	
	protected void postConstructor()  {
		initialize();
	}
	
	private void initialize() {
		test = (CheckBox)def.getWidget("test");
		test.setEnabled(true);
		test.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				value.setValue(event.getValue());
			}
		});
						
		enabled = (CheckBox)def.getWidget("enabled");
		enabled.setEnabled(true);
		enabled.setValue("Y");
		enabled.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setEnabled("Y".equals(event.getValue()));
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
		
		value = (TextBox<String>)def.getWidget("value");
		value.setEnabled(true);
		
		setValue = (Button)def.getWidget("setValue");
		setValue.setEnabled(true);
		setValue.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Application.logger().info(log("value being set to - "+value.getValue()));
				test.setValue(value.getValue());
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
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				((CollapsePanel)def.getWidget("collapsePanel")).open();
			}
		});
		
	}
	
    private String log(String message) {
    	try {
    		return getClass().getName() + " - " +message;
    	}catch(Exception e) {
    		return message;
    	}
    }
	
	
}
