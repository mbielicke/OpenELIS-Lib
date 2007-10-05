package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.Command;
/**
 * This interface is used by classes to pass Commands 
 * to a MenuBar widget.
 * @author tschmidt
 *
 */
public interface MenuCommands {
	
	public Command getCommand(String cmd);

}
