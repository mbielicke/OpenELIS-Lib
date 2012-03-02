package org.openelis.test.client.main;

import java.util.logging.Level;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Browser;
import org.openelis.gwt.widget.MenuItem;
import org.openelis.test.client.Application;
import org.openelis.test.client.button.ButtonScreen;
import org.openelis.test.client.calendar.CalendarScreen;
import org.openelis.test.client.dropdown.DropdownScreen;
import org.openelis.test.client.logging.LoggingScreen;
import org.openelis.test.client.selection.SelectionScreen;
import org.openelis.test.client.table.TableScreen;
import org.openelis.test.client.textbox.TextboxScreen;
import org.openelis.test.client.tree.TreeScreen;
import org.openelis.test.client.autocomplete.AutoCompleteScreen;
import org.openelis.test.client.textarea.TextareaScreen;
import org.openelis.test.client.checkbox.CheckBoxScreen;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;


public class TestWidgetsScreen extends Screen {

    public static Browser          browser;
         
    public TestWidgetsScreen() throws Exception {
        
        super((ScreenDefInt)GWT.create(TestWidgetsDef.class));
        
        // resize browser will move the collapse handle to the middle
        browser = (Browser)def.getWidget("browser");        
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				browser.resize();
			}
		});
        
        initialize();
    }
    
    protected void initialize() {
    	addCommand("textbox",  new Command() {
			public void execute() {
				try {
					browser.addScreen(new TextboxScreen());
				}catch(Exception e) {
                    Window.alert(e.getMessage());
                    Application.logger().log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
    	
    	addCommand("dropdown",new Command() {
			public void execute() {
				try {
					browser.addScreen(new DropdownScreen());
				}catch(Exception e) {
                    Window.alert(e.getMessage());
                    Application.logger().log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
    	
    	addCommand("table",new Command() {
			public void execute() {
				try {
					browser.addScreen(new TableScreen());
				}catch(Exception e) {
                    Window.alert(e.getMessage());
                    Application.logger().log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
    	
    	addCommand("tree",new Command() {
			public void execute() {
				try {
					browser.addScreen(new TreeScreen());
				}catch(Exception e) {
                    Window.alert(e.getMessage());
                    Application.logger().log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
    	
    	addCommand("calendar",new Command() {
    		public void execute() {
    			try {
    				browser.addScreen(new CalendarScreen());
    			}catch(Exception e) {
    				Window.alert(e.getMessage());
    				Application.logger().log(Level.SEVERE,e.getMessage(),e);
    			}
    		}
    	});
    	
    	addCommand("selection",new Command() {
    		public void execute() {
    			try {
    				browser.addScreen(new SelectionScreen());
    			}catch(Exception e) {
    				Window.alert(e.getMessage());
    				Application.logger().log(Level.SEVERE,e.getMessage(),e);
    			}
    		}
    	});
    	
    	addCommand("button", new Command() {
    		public void execute() {
    			try {
    				browser.addScreen(new ButtonScreen());
    			}catch(Exception e) {
    				Window.alert(e.getMessage());
    				Application.logger().log(Level.SEVERE,e.getMessage(),e);
    			}
    		}
    	});
    	
    	addCommand("autoComplete", new Command() {
    		public void execute() {
    			try {
    				browser.addScreen(new AutoCompleteScreen());
    			}catch(Exception e) {
    				Window.alert(e.getMessage());
    				Application.logger().log(Level.SEVERE,e.getMessage(),e);
    			}
    		}
    	});
    	
    	addCommand("textarea", new Command() {
    		public void execute() {
    			try {
    				browser.addScreen(new TextareaScreen());
    			}catch(Exception e) {
    				Window.alert(e.getMessage());
    				Application.logger().log(Level.SEVERE,e.getMessage(),e);
    			}
    		}
    	});

    	addCommand("checkbox", new Command() {
    		public void execute() {
    			try {
    				browser.addScreen(new CheckBoxScreen());
    			}catch(Exception e) {
    				Window.alert(e.getMessage());
    				Application.logger().log(Level.SEVERE,e.getMessage(),e);
    			}
    		}
    	});

    	addCommand("logs",new Command() {
    		public void execute() {
    			try {
    				browser.addScreen(new LoggingScreen());
    			}catch(Exception e) {
    				Window.alert(e.getMessage());
    				Application.logger().log(Level.SEVERE,e.getMessage(),e);
    			}
    		}
    	});
    	
    }
    
    /**
     * Returns the browser associated with this application.
     */
    public static Browser getBrowser() {
        return browser;
    }


    /**
     * register a command handler
     */
    private void addCommand(String screenName, Command cmd) {
        MenuItem item;

        item = ((MenuItem)def.getWidget(screenName));
        if(item != null) {
       		item.setEnabled(true);        
       		item.addCommand(cmd);
       	}
    }
}
