package org.openelis.test.client.textbox;

import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.IntegerHelper;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.logging.client.HasWidgetsLogHandler;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;


public class TextBoxTestViewImpl extends Screen implements TextBoxTestView {
	
	TextBox           test;
	TextBox<String>   mask,pattern,value;
	TextBox<Integer>  maxlength;
	CheckBox          enabled,required,query;
	Dropdown<String>  field,tCase,alignment,logLevel;
	VerticalPanel     log;
	Logger            logger;
	Button            clearLog;
	
	public TextBoxTestViewImpl() {
		super((ScreenDefInt)GWT.create(TextBoxTestDef.class));
		initialize();
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
		ArrayList<Item<String>> model = new ArrayList<Item<String>>();
		model.add(new Item<String>("String","String"));
		model.add(new Item<String>("Integer","Integer"));
		model.add(new Item<String>("Double","Double"));
		model.add(new Item<String>("Date","Date"));
		field.setModel(model);
		field.setValue("String");
		
		tCase = (Dropdown<String>)def.getWidget("case");
		tCase.setEnabled(true);
		model = new ArrayList<Item<String>>();
		model.add(new Item<String>("mixed","Mixed"));
		model.add(new Item<String>("upper","UPPER"));
		model.add(new Item<String>("lower","lower"));
		tCase.setModel(model);
		tCase.setValue("mixed");
		tCase.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setCase(TextBox.Case.valueOf(event.getValue().toUpperCase()));
			}
		});
		
		alignment = (Dropdown<String>)def.getWidget("alignment");
		alignment.setEnabled(true);
		model = new ArrayList<Item<String>>();
		model.add(new Item<String>("left","left"));
		model.add(new Item<String>("center","center"));
		model.add(new Item<String>("right","right"));
		alignment.setModel(model);
		alignment.setValue("left");
		alignment.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setTextAlignment(TextAlignment.valueOf(event.getValue().toUpperCase()));
			}
		});
		
		logLevel = (Dropdown<String>)def.getWidget("logLevel");
		logLevel.setEnabled(true);
		model = new ArrayList<Item<String>>();
		model.add(new Item<String>("SEVERE","Severe"));
		model.add(new Item<String>("WARNING","Warning"));
		model.add(new Item<String>("INFO","Info"));
		model.add(new Item<String>("FINE","Fine"));
		model.add(new Item<String>("FINER","Finer"));
		model.add(new Item<String>("FINEST","Finest"));
		logLevel.setModel(model);
		
		logLevel.addValueChangeHandler(new ValueChangeHandler<String>() {
		    @Override
			public void onValueChange(ValueChangeEvent<String> event) {
		    	logger.setLevel(Level.parse(event.getValue()));
			}
		});
		
		log = (VerticalPanel)def.getWidget("logPanel");
		
		logger = Logger.getLogger("TestTextBox");
		logger.addHandler(new HasWidgetsLogHandler(log));
		test.setLogger(logger);
		
		clearLog = (Button)def.getWidget("clearLog");
		clearLog.setEnabled(true);
		clearLog.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				log.clear();
			}
		});
	}

	@Override
	public void setField(String value) {
		field.setValue(value);
	}

	@Override
	public void setCase(String value) {
		tCase.setValue(value);
	}

	@Override
	public void setEnabled(String value) {
		enabled.setValue(value);
	}

	@Override
	public void setAlignment(String value) {
		alignment.setValue(value);
	}

	@Override
	public void setQueryMode(String value) {
		query.setValue(value);
	}

	@Override
	public void setMask(String value) {
		mask.setValue(value);
	}

	@Override
	public void setPattern(String value) {
		pattern.setValue(value);
	}

	@Override
	public void setRequired(String value) {
		required.setValue(value);
	}

	@Override
	public void setValue(String value) {
		this.value.setValue(value);
	}

	@Override
	public void setMaxLength(Integer value) {
		maxlength.setValue(value);
	}

	@Override
	public String getField() {
		return field.getValue();
	}

	@Override
	public String getCase() {
		return tCase.getValue();
	}

	@Override
	public String getEnabled() {
		return enabled.getValue();
	}

	@Override
	public String getAlignment() {
		return alignment.getValue();
	}

	@Override
	public String getQueryMode() {
		return query.getValue();
	}

	@Override
	public String getMask() {
		return mask.getValue();
	}

	@Override
	public String getPattern() {
		return pattern.getValue();
	}

	@Override
	public String getRequired() {
		return required.getValue();
	}

	@Override
	public String getValue() {
		return value.getValue();
	}

	@Override
	public Integer getMaxLength(Integer value) {
		return maxlength.getValue();
	}
	
}
