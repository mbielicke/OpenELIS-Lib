package org.openelis.gwt.common;

import org.openelis.gwt.screen.Screen;

public class LocalizedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String key;
	private String[] params;
	
	public LocalizedException() {
		super();
	}
	
	public LocalizedException(String key) {
		super();
		this.key = key;
	}
	
	public LocalizedException(String key, String... params) {
		this(key);
		this.params = params;		
	}
	
	public String getKey() {
	    return key;
	}
	
	public String[] getParams() {
	    return params;
	}

	@Override
	public String getMessage() {
		String message = Screen.consts.get(key);
		if(params != null) {
			for(int i = 0; i < params.length; i++) {
				message = message.replaceFirst("\\{"+i+"\\}", params[i]);
			}
		}
		return message;
	}
	
	
	

}
