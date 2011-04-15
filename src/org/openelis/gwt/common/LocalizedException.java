package org.openelis.gwt.common;

import org.openelis.gwt.screen.Screen;

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
		String m;

		try {
		    m = Screen.consts.get(key);
        } catch (Throwable any) {
            m = null;
        }
		if (m != null) {
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					m = m.replaceFirst("\\{" + i + "\\}", params[i]);
				}
			}
		} else {
			m = key;
		}
		return m;
	}

	public Object clone() {
		return new LocalizedException(key, params);
	}

	public boolean equals(Object obj) {
		if (obj instanceof LocalizedException)
			return ((LocalizedException) obj).key.equals(key);
		return false;
	}
}
