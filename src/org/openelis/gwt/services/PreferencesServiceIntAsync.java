package org.openelis.gwt.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openelis.gwt.common.Preferences;

public interface PreferencesServiceIntAsync {
    
    public void getPreferences(String key, AsyncCallback callback);
    
    public void storePreferences(Preferences prefs, AsyncCallback callback);

}
