package org.openelis.gwt.common;

public class LocalizedException extends Exception implements Cloneable {

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
		return null;
	}
	
	public Object clone() {
		return new LocalizedException(key,params);
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof LocalizedException) 
			return ((LocalizedException)obj).key.equals(key);
		return false;
	}
	

}
