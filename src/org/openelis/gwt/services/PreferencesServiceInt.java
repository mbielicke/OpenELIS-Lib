package org.openelis.gwt.services;

import com.google.gwt.user.client.rpc.RemoteService;

import org.openelis.gwt.common.Preferences;

public interface PreferencesServiceInt extends RemoteService {
    
    public Preferences getPreferences(String key);
    
    public void storePreferences(Preferences prefs);

}
