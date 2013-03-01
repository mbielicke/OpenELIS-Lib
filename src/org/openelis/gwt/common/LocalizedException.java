package org.openelis.gwt.common;

import javax.ejb.ApplicationException;

import org.openelis.gwt.screen.Screen;

@ApplicationException
public class LocalizedException extends Exception implements Cloneable {

    private static final long serialVersionUID = 1L;

    private String            key;
    private String[]          params;

    public LocalizedException() {
        super();
    }

    public LocalizedException(String key) {
        super();
        this.key = key;
    }

    public LocalizedException(String key, Object... params) {
        this(key);
        if (params != null && params.length > 0) {
            this.params = new String[params.length];
            for (int i = 0; i < params.length; i++ )
                if (params[i] != null)
                    this.params[i] = params[i].toString();
                else
                    this.params[i] = "";
        } else {
            this.params = null;
        }
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
            m = "";
        } catch (Throwable any) {
            m = null;
        }
        if (m != null) {
            if (params != null) {
                for (int i = 0; i < params.length; i++ ) {
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
            return ((LocalizedException)obj).key.equals(key);
        return false;
    }
}
