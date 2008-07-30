/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.common.data;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;


    public class DataModelWidget extends Composite implements SourcesCommandEvents {

    public enum Action {SELECTION,REFRESH,GETPAGE,ADD,DELETE,FETCH}
    
    public Action action;
    
    private CommandListenerCollection commandListeners;
    
    private DataModel model = new DataModel();
    
    private int candidate = 0;
    
    public Request lastRequest;
    
    public void addCommandListener(CommandListener listener) {
        if (commandListeners == null)
            commandListeners = new CommandListenerCollection();
        commandListeners.add(listener);
    }

    public void removeCommandListener(CommandListener listener) {
        if (commandListeners != null)
            commandListeners.remove(listener);
    }
    
    private void fireCommand(Action action,Object obj) {
        if(commandListeners != null)
        	commandListeners.fireCommand(action,obj);
    }
    
    public void setModel(DataModel model){
        this.model = model;
        candidate = 0;
        fireCommand(Action.REFRESH,model);
    }
    
    public DataModel getModel() {
        return model;
    }
    
    public void next() {
        select(++candidate);
    }
    
    public void previous() {
        select(--candidate);
    }
    
    public void select(final int selection) throws IndexOutOfBoundsException {
        if(lastRequest != null && lastRequest.isPending()){
            lastRequest.cancel();
        }
        if(selection > model.size() - 1){
            setPage(model.getPage()+1);
        }else if(selection < 0){
            if(model.getPage() > 0){
                model.selecttLast(true);
                setPage(model.getPage()-1);
	        }
        }else{
            AsyncCallback callback = new AsyncCallback() {
                public void onSuccess(Object result){
                    model.select(selection);
                    candidate = selection;
                    fireCommand(Action.SELECTION,new Integer(selection));
                }
            
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
            
                }
            
            };
            fireCommand(Action.FETCH,new Object[] {model.get(selection).getInstance(),callback});
        }
    }
    
    public void add(DataSet set){
        model.add(set);
        fireCommand(Action.ADD,null);
    }
    
    public DataSet getSelected() {
        return model.getSelected();
    }
    
    public void delete(int index){
        model.delete(index);
        fireCommand(Action.DELETE,new Integer(index));
    }
    
    public int getSelectedIndex() {
        return model.getSelectedIndex();
    }
    
    public int getPage(){
        return model.getPage();
    }
    
    public void setPage(int page) {    	
        model.setPage(page);
        fireCommand(Action.GETPAGE,new Integer(page));
    }

}
