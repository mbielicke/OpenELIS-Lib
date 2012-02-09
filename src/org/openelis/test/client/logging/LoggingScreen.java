package org.openelis.test.client.logging;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Item;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.logging.client.HasWidgetsLogHandler;
import com.google.gwt.logging.client.HtmlLogFormatter;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This screen will give the users a chance to view the logs output by the Application in
 * a window in the browser.  They can also change the log level.
 *
 */
public class LoggingScreen extends Screen {
	
	protected Dropdown<String> logLevel;
	protected Button           clearLog;
	protected CheckBox         remoteSwitch;
	protected HasWidgets	   logPanel;
	
	protected Logger           logger = Logger.getLogger("TestWidgets");
		
	/**
	 * No arg-constructor
	 */
	public LoggingScreen() {
		super((ScreenDefInt)GWT.create(LoggingDef.class));
		
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            public void execute() {
                postConstructor();
            }
        }); 
	}
	
	/**
	 * Method called from scheduled command to initialize the screen
	 */
	protected void postConstructor() {
		initialize();
		initializeDropdowns();
		
		logPanel = new VerticalPanel();
		
		HasWidgetsLogHandler handler = new HasWidgetsLogHandler(logPanel);
		HtmlLogFormatter formatter = new HtmlLogFormatter(true) {
			@Override
			protected String getHtmlPrefix(LogRecord event) {
			    StringBuilder prefix = new StringBuilder();
			    prefix.append("<span>");
			    prefix.append("<code>");
			    return prefix.toString();
			}
		};
		
		logger.addHandler(handler);
				
		((ScrollPanel)def.getWidget("logContainer")).setWidget((IsWidget)logPanel);
	}
	
	/**
	 * Method to initialize widgets used in the screen.
	 */
	private void initialize() {
		logLevel = (Dropdown<String>)def.getWidget("logLevel");
		logLevel.setEnabled(true);
		
		logLevel.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				logger.setLevel(Level.parse((event.getValue())));
			}
		});
		
		clearLog = (Button)def.getWidget("clearLog");
		clearLog.setEnabled(true);
		
		clearLog.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				logPanel.clear();
			}
		});
	}
	
	/**
	 * Method initializes dropdown model and sets value to the current log level of
	 * Application.logger
	 */
	private void initializeDropdowns() {
		ArrayList<Item<String>> model;
		
		model = new ArrayList<Item<String>>();
		
		model.add(new Item<String>(Level.SEVERE.toString(),"Severe"));
		model.add(new Item<String>(Level.WARNING.toString(),"Warning"));
		model.add(new Item<String>(Level.INFO.toString(),"Info"));
		model.add(new Item<String>(Level.CONFIG.toString(),"Config"));
		model.add(new Item<String>(Level.FINE.toString(),"Fine"));
		model.add(new Item<String>(Level.FINER.toString(),"Finer"));
		model.add(new Item<String>(Level.FINEST.toString(),"Finest"));
		model.add(new Item<String>(Level.ALL.toString(),"All"));
		
		logLevel.setModel(model);
		
		while(logger.getLevel() == null) 
			logger = logger.getParent();
		
		logLevel.setValue(logger.getLevel().toString());
	}

}
