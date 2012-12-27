package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.event.HasNavigationSelectionHandlers;

public interface NavigationWidget<I> extends HasNavigationSelectionHandlers { 
										  
	public void enable(boolean enabled);
	
	public void load(ArrayList<I> model);

	public void unselect(int selection);
	
	public void navSelect(int selection);
	
}
