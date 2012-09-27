package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;

public interface AppStatusInt {
		
	public void setMessagePopup(ArrayList<LocalizedException> exceptions, String style);
	
	public void clearMessagePopup(String style);
	
	public void setStatus(String text, String style);
	
	public void lockWindow();
	
	public void unlockWindow();
	
	public void setBusy();
	
	public void setBusy(String message);
	
	public void clearStatus();
	
	public void setDone(String message);
	
	public void setError(String message);	

}
