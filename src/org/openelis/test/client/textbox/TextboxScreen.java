package org.openelis.test.client.textbox;

import java.util.ArrayList;
import java.util.Date;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.DateHelper;
import org.openelis.gwt.widget.DoubleHelper;
import org.openelis.gwt.widget.IntegerHelper;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.Selection;
import org.openelis.gwt.widget.StringHelper;
import org.openelis.gwt.widget.TextBox;
import org.openelis.test.client.Application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;


public class TextboxScreen extends Screen {
	
	TextBox            test;
	TextBox<String>    mask,pattern,value,css,testString;
	TextBox<Integer>   maxlength,testInteger;
	TextBox<Double>    testDouble;
	TextBox<Datetime>  testDatetime;
	CheckBox           enabled,required,query;
	Selection<String>  field,tCase,alignment,logLevel;
	Selection<Integer> begin,end;
	Button             setValue,getQuery;
	
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
		testString = (TextBox<String>)def.getWidget("test");
		testString.setEnabled(true);
		testString.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				Application.logger().info(log("value changed - "+event.getValue()));
				if(event.getValue() != null)
					value.setValue(testString.getValue());
				else
					value.setValue("NULL");
			}			
		});
			
		test = testString;
		
		test.addDomHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode() >=112 && event.getNativeKeyCode() <= 124) {
					event.preventDefault();
					event.stopPropagation();
				}
				
			}
		},KeyUpEvent.getType());
		
		mask = (TextBox<String>)def.getWidget("mask");
		mask.setEnabled(true);
		mask.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				Application.logger().info(log("mask changed - "+event.getValue()));
				test.setMask(event.getValue());
			}
		});
		
		pattern = (TextBox<String>)def.getWidget("pattern");
		pattern.setEnabled(true);
		pattern.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				Application.logger().info(log("pattern changed - "+event.getValue()));
				test.getHelper().setPattern(event.getValue());
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
				if(field.getValue().equals("String"))
					test.setValue(value.getValue());
				else if(field.getValue().equals("Integer"))
					test.setValue(Integer.valueOf(value.getValue()));
				else if(field.getValue().equals("Double"))
					test.setValue(Double.valueOf(value.getValue()));
				else if(field.getValue().equals("Date"))
					test.setValue(Datetime.getInstance(Datetime.YEAR, Datetime.DAY, new Date(value.getValue())));
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
		
		field = (Selection<String>)def.getWidget("field");
		field.setEnabled(true);
		field.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				if("String".equals(event.getValue())){
					test = testString;
				}else if("Integer".equals(event.getValue())) {
					IntegerHelper helper;
					
					if(testInteger == null) {
						testInteger = new TextBox<Integer>();
						helper = new IntegerHelper();						
						testInteger.setHelper(helper);
						
						testInteger.addValueChangeHandler(new ValueChangeHandler<Integer>() {
							public void onValueChange(ValueChangeEvent<Integer> event) {
								Application.logger().info(log("value changed - "+event.getValue()));
								if(event.getValue() != null)
									value.setValue(event.getValue().toString());
								else
									value.setValue("NULL");
							}
						});
					}
					
					test = testInteger;					
				}else if("Double".equals(event.getValue())) {
					DoubleHelper helper;
					
					if(testDouble == null) {	
						testDouble = new TextBox<Double>();
						helper = new DoubleHelper();
						testDouble.setHelper(helper);
						
						testDouble.addValueChangeHandler(new ValueChangeHandler<Double>() {
							public void onValueChange(ValueChangeEvent<Double> event) {
								Application.logger().info(log("value changed - "+event.getValue()));
								if(event.getValue() != null)
									value.setValue(event.getValue().toString());
								else
									value.setValue("NULL");
							}
						});
					}
					
					test = testDouble;
				} else if("Date".equals(event.getValue())) {
					DateHelper helper;
					
					if(testDatetime == null) {
						testDatetime = new TextBox<Datetime>();
						
						helper = new DateHelper();
						helper.setBegin(begin.getValue().byteValue());
						helper.setEnd(end.getValue().byteValue());

						begin.setEnabled(true);
						end.setEnabled(true);
					
						if(begin.getValue() > Datetime.DAY) {
							mask.setValue("99:99");
							pattern.setValue("HH:mm");
						} else if (end.getValue() < Datetime.HOUR){
							mask.setValue("9999-99-99");
							pattern.setValue("yyyy-MM-dd");
						} else {
							mask.setValue("9999-99-99 99:99");
							pattern.setValue("yyyy-MM-dd HH:mm");
						}
			    	
						testDatetime.setHelper(helper);
						
						testDatetime.addValueChangeHandler(new ValueChangeHandler<Datetime>() {
							public void onValueChange(ValueChangeEvent<Datetime> event) {
								Application.logger().info(log("value changed - "+event.getValue()));
								if(event.getValue() != null)
									value.setValue(event.getValue().toString());
								else
									value.setValue("NULL");
							}
						});
					}

					test = testDatetime;
				}
				
				if(!"Date".equals(event.getValue())) {
					begin.setEnabled(false);
					end.setEnabled(false);
				}
				
		
				setAttributes();
				((FlexTable)def.getWidget("displayTable")).setWidget(1,1, test);
				test.setValue(null);
				value.setValue(null);
			}
		});
		
		
		
		tCase = (Selection<String>)def.getWidget("case");
		tCase.setEnabled(true);
		tCase.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				Application.logger().info("case changed - "+event.getValue());
				test.setCase(TextBox.Case.valueOf(event.getValue().toUpperCase()));
			}
		});
		
		alignment = (Selection<String>)def.getWidget("alignment");
		alignment.setEnabled(true);

		alignment.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				Application.logger().info(log("alignment changed - "+event.getValue()));
				test.setTextAlignment(TextAlignment.valueOf(event.getValue().toUpperCase()));
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
		
		begin = (Selection<Integer>)def.getWidget("begin");
		begin.setEnabled(false);
		begin.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			public void onValueChange(ValueChangeEvent<Integer> event) {
				((DateHelper)test.getHelper()).setBegin(event.getValue().byteValue());
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
		    	((DateHelper)test.getHelper()).setPattern(pattern.getValue());
		    	test.setMask(mask.getValue());
		    	
			}
		});
		
		end = (Selection<Integer>)def.getWidget("end");
		end.setEnabled(false);
		end.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			public void onValueChange(ValueChangeEvent<Integer> event) {
				((DateHelper)test.getHelper()).setEnd(event.getValue().byteValue());
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
		    	((DateHelper)test.getHelper()).setPattern(pattern.getValue());
		    	test.setMask(mask.getValue());
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

	}
	
	private void setAttributes() {
		test.setCase(TextBox.Case.valueOf(tCase.getValue().toUpperCase()));
		test.setEnabled("Y".equals(enabled.getValue()));
		test.setTextAlignment(TextAlignment.valueOf(alignment.getValue().toUpperCase()));
		test.setQueryMode("Y".equals(query.getValue()));
		test.setMask(mask.getValue());
		test.getHelper().setPattern(pattern.getValue());
		test.setRequired("Y".equals(required.getValue()));
		if(maxlength.getValue() != null)
			test.setMaxLength(maxlength.getValue());
		if(css.getValue() != null)
			test.setStyleName(css.getValue());
	}
	
    private String log(String message) {
    	try {
    		return getClass().getName() + " - " +message;
    	}catch(Exception e) {
    		return message;
    	}
    }

}
