package org.openelis.test.client.button;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;

public class ButtonScreen extends Screen {

	protected Button            test;
	protected TextBox<String>   text,icon,css;
	protected CheckBox          enabled,wrap,pressed,locked,toggles;
	
	public ButtonScreen() {
		super((ScreenDefInt)GWT.create(ButtonDef.class));
		
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
		test = (Button)def.getWidget("test");
		test.setEnabled(true);
		test.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.alert("Clicked");
			}
		});
		
		text = (TextBox<String>)def.getWidget("text");
		text.setEnabled(true);
		text.setValue("Button");
		text.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setText(event.getValue());
			}
		});
		
		icon = (TextBox<String>)def.getWidget("icon");
		icon.setEnabled(true);
		icon.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setIcon(event.getValue());
			}
		});
		
		wrap = (CheckBox)def.getWidget("wrap");
		wrap.setEnabled(true);
		wrap.setValue("Y");
		wrap.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setWrap("Y".equals(event.getValue()));
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
		
		pressed = (CheckBox)def.getWidget("pressed");
		pressed.setEnabled(true);
		pressed.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setPressed("Y".equals(event.getValue()));
			}
		});
		
		locked = (CheckBox)def.getWidget("locked");
		locked.setEnabled(true);
		locked.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				if("Y".equals(event.getValue()))
					test.lock();
				else
					test.unlock();
			}
		});
		
		toggles = (CheckBox)def.getWidget("toggles");
		toggles.setEnabled(true);
		toggles.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setToggles("Y".equals(event.getValue()));
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
	
	
}
