package org.openelis.test.client.logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.Selection;
import org.openelis.test.client.Application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.logging.client.SimpleRemoteLogHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * This screen will give the users a chance to view the logs output by the Application in
 * a window in the browser.  They can also change the log level.
 *
 */
public class LoggingScreen extends Screen {
	
	protected Selection<String> logLevel;
	protected Button           clearLog;
	protected CheckBox         remoteSwitch;
	protected HasWidgets	   logPanel;
	
	protected static SimpleRemoteLogHandler remoteLogger;
		
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
		
		logPanel = Application.getLogPanel();
				
		((ScrollPanel)def.getWidget("logContainer")).setWidget((IsWidget)logPanel);
	}
	
	/**
	 * Method to initialize widgets used in the screen.
	 */
	private void initialize() {
		logLevel = (Selection<String>)def.getWidget("logLevel");
		logLevel.setEnabled(true);
		
		logLevel.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				Application.logger().setLevel(Level.parse((event.getValue())));
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
		
		remoteSwitch = (CheckBox)def.getWidget("remoteAll");
		remoteSwitch.setEnabled(true);
		
		remoteSwitch.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				if("Y".equals(event.getValue())) {
					if(remoteLogger == null)
						remoteLogger = new SimpleRemoteLogHandler();
					
					Application.logger().addHandler(remoteLogger);
				} else {
					Application.logger().removeHandler(remoteLogger);
				}
			}
		});
		
		if(remoteLogger != null && Arrays.asList(Application.logger().getHandlers()).contains(remoteLogger))
			remoteSwitch.setValue("Y");
	}
	
	/**
	 * Method initializes dropdown model and sets value to the current log level of
	 * Application.logger
	 */
	private void initializeDropdowns() {
		ArrayList<Item<String>> model;
		Logger logger;
		
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
		
		logger = Application.logger();
		while(Application.logger().getLevel() == null) 
			logger = logger.getParent();
		
		logLevel.setValue(logger.getLevel().toString());
	}

}
