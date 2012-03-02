package org.openelis.test.client.textarea;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.TextArea;
import org.openelis.gwt.widget.TextBox;
import org.openelis.test.client.Application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;


public class TextareaScreen extends Screen {
	
	TextArea           test;
	TextBox<String>    value,css;
	CheckBox           enabled,required;
	Button             setValue;
	
	public TextareaScreen() {
		super((ScreenDefInt)GWT.create(TextareaDef.class));
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				postConstruct();
			}
		});
		
	}
	
	public void postConstruct() {
		initialize();
	}
	
	public void initialize() {
		test = (TextArea)def.getWidget("test");
		test.setEnabled(true);
		test.addValueChangeHandler(new ValueChangeHandler() {
			public void onValueChange(ValueChangeEvent event) {
				Application.logger().info(log("value changed - "+event.getValue()));
				if(event.getValue() != null)
					value.setValue(test.getValue().toString());
				else
					value.setValue("NULL");
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
