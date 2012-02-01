package org.openelis.test.client.calendar;

import java.util.ArrayList;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.DateHelper;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.calendar.Calendar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;


public class CalendarScreen extends Screen {
	
	Calendar          test;
	TextBox<String>   mask,pattern,value;
	CheckBox          enabled,required,query;
	Dropdown<Integer> begin,end;
	Dropdown<String>  alignment,logLevel;
	
	public CalendarScreen() {
		super((ScreenDefInt)GWT.create(CalendarDef.class));
		
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
		test = (Calendar)def.getWidget("test");
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
		mask.setValue("9999-99-99");
		mask.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.getHelper().setMask(event.getValue());
			}
		});
		
		
		pattern = (TextBox<String>)def.getWidget("pattern");
		pattern.setEnabled(true);
		pattern.setValue("yyyy-MM-dd");
		pattern.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				((DateHelper)test.getHelper()).setPattern(event.getValue());
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
		
		begin = (Dropdown<Integer>)def.getWidget("begin");
		begin.setEnabled(true);
		begin.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			public void onValueChange(ValueChangeEvent<Integer> event) {
				test.setBegin(event.getValue().byteValue());
		    	if(event.getValue() > Datetime.DAY) {
		    		mask.setValue("99:99");
		    		pattern.setValue("HH:mm");
		    	} else if (end.getValue() < Datetime.HOUR){
		    		mask.setValue("9999-99-99");
		    		pattern.setValue("yyyy-MM-dd");
		    	} else {
		    		mask.setValue("9999-99-99 99:99");
		    		pattern.setValue("yyyy-MM-dd HH:mm");
		    	}
			}
		});
		
		end = (Dropdown<Integer>)def.getWidget("end");
		end.setEnabled(true);
		end.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			public void onValueChange(ValueChangeEvent<Integer> event) {
				test.setEnd(event.getValue().byteValue());
		    	if(begin.getValue() > Datetime.DAY) {
		    		mask.setValue("99:99");
		    		pattern.setValue("HH:mm");
		    	} else if (event.getValue() < Datetime.HOUR){
		    		mask.setValue("9999-99-99");
		    		pattern.setValue("yyyy-MM-dd");
		    	} else {
		    		mask.setValue("9999-99-99 99:99");
		    		pattern.setValue("yyyy-MM-dd HH:mm");
		    	}
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
		ArrayList<Item<Integer>> imodel;
		
		imodel = new ArrayList<Item<Integer>>();
		imodel.add(new Item<Integer>(0,"Year"));
		imodel.add(new Item<Integer>(1,"Month"));
		imodel.add(new Item<Integer>(2,"Day"));
		imodel.add(new Item<Integer>(3,"Hour"));
		imodel.add(new Item<Integer>(4,"Minute"));
		
		begin.setModel(imodel);
		begin.setValue(0);
		
		end.setModel(imodel);
		end.setValue(2);
				
		model = new ArrayList<Item<String>>();
		model.add(new Item<String>("left","left"));
		model.add(new Item<String>("center","center"));
		model.add(new Item<String>("right","right"));
		alignment.setModel(model);
		alignment.setValue("left");

	}


}
