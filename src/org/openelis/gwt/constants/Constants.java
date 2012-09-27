package org.openelis.gwt.constants;



import com.google.gwt.core.client.GWT;

public class Constants {

	private static LibraryConstants consts;
	
	public static LibraryConstants get() {
		if(consts == null)
			consts = GWT.create(LibraryConstants.class);
		
		return consts;
	}
	
	private Constants(){}
}
