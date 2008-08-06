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
package org.openelis.gwt.screen;

//import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.LastPageException;
import org.openelis.gwt.common.RPCDeleteException;
import org.openelis.gwt.common.FormRPC.Status;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataModelWidget;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;
import org.openelis.gwt.services.AppScreenFormServiceIntAsync;
import org.openelis.gwt.widget.ButtonPanel;
import org.openelis.gwt.widget.FormInt;

import java.util.EnumSet;
import java.util.Iterator;
/**
 * ScreenForm extends Screen to include functionality for integrating 
 * the ButtonPanel widget and default logic for standard forms that accept
 * input and also perform queries
 * @author tschmidt
 *
 */
public class AppScreenForm extends AppScreen implements FormInt, SourcesCommandEvents, CommandListener {
    
    /**
     * Reference to the ButtonPanel that is defined on this
     * Screen.  This needs to be set by the Extending class 
     * after the screen is drawn.
     */
    private ButtonPanel bpanel = null;
    /**
     * Reference to the Label that will display the Form Messages to
     * the User.  If this Screen is set in a ScreenWindow this 
     * field will be set by that class.  If not, then the Extending class
     * must set this field after the Screen is drawn.
     */
    public Label message = new Label();
    
    public DataModelWidget modelWidget = new DataModelWidget();
    protected DataSet key;
    public AppScreenFormServiceIntAsync formService;
    public State state = State.DEFAULT;
    protected CommandListenerCollection commandListeners;

    public AppScreenForm(AppScreenFormServiceIntAsync service) {
        super(service);
        formService = service;
    }
    
    public AppScreenForm() {
        super();
        modelWidget.addCommandListener(this);
    }
    
    public void afterDraw(boolean sucess) {
        super.afterDraw(sucess);
        if(bpanel != null){
            bpanel.addCommandListener(this);
            addCommandListener(bpanel);
        }
        changeState(State.DEFAULT);
        enable(false);
    }
    
    public void fetch() {
        AsyncCallback callback = new AsyncCallback() {
            public void onSuccess(Object result){
                rpc = (FormRPC)result;
                forms.put("display",(FormRPC)result);
                load();
                afterFetch(true);
            }
            
            public void onFailure(Throwable caught){
                afterFetch(false);
            }
        };
        fetch(callback);
    }
    
    public Request fetch(AsyncCallback callback){
        return fetch((FormRPC)forms.get("display"), callback);
    }
    
    public Request fetch(FormRPC rpc, AsyncCallback callback){
        return fetch(key, rpc, callback);
    }
    
    public Request fetch(DataSet key, FormRPC rpc, AsyncCallback callback){
        return formService.fetch(key, rpc, callback);
    }
    
    public void afterFetch(boolean success){
       if(!success)
            key = null;
    	window.setStatus("", "");
        changeState(State.DISPLAY);
    	if(success)
    		changeState(State.DISPLAY);
    	else{
    		changeState(State.DEFAULT);
    		key = null;
    	}
    }
    
    /**
     * This method provides the default behavior for when the Query button from 
     * a the ButtonPanel is clicked.  It is called from the ButtonPanel Widget.
     */
    public void query() {
        resetRPC();
        window.setStatus(consts.get("enterFieldsToQuery"),"");
        setForm(true);
        rpc = (FormRPC)forms.get("query");
        resetRPC();
        load();
        enable(true); 
        changeState(State.QUERY);
    }

    /**
     * This stub can be overridden to handle the default behavior for when the
     * Next button on a ButtonPanel is clicked.  It is called from the ButtonPanel
     * Widget.
     */
    public void next() {
        // TODO Auto-generated method stub
        modelWidget.next();
        
    }
    
    /**
     * This stub can be overridden to handle the default behavior for when the
     * Previous button on a ButtonPanel is clicked.  It is called from the ButtonPanel
     * Widget.
     */
    public void prev() {
        // TODO Auto-generated method stub
        modelWidget.previous();
    }

    /** 
     * This method provides the default behavior for a form when the Add button on a 
     * ButtonPanel is clicked.  It is called from the ButtonPanel Widget.  If overridden 
     * in the extending class be sure to call super.add(state).
     */
    public void add() {
        resetRPC();
        key = null;
        load();
        enable(true);
        changeState(State.ADD);
        window.setStatus(consts.get("enterInformationPressCommit"),"");
    }
    
    public void update() {
        AsyncCallback callback = new AsyncCallback() {
            public void onSuccess(Object result){
                rpc = (FormRPC)result;
                forms.put(rpc.key, rpc);
                load();
                afterUpdate(true);
            }
            public void onFailure(Throwable caught){
                Window.alert(caught.getMessage());
                afterUpdate(false);
            }
         };
         update(callback);
    }

    /**
     * This method provides the default behavior for when the Update button of a
     * ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public Request update(AsyncCallback callback) {
        window.setStatus(consts.get("lockForUpdate"),"spinnerIcon");
        return formService.fetchForUpdate(key, (FormRPC)forms.get("display"), callback);
    }
    
    public void afterUpdate(boolean success){
        if(success){
            enable(true);
            window.setStatus(consts.get("updateFields"),"");
            changeState(State.UPDATE);
        }else{
            window.setStatus("", "");
            changeState(State.DISPLAY);
        }
    }

    /** 
     * This stub can be overridden by the extending class to handle the behavior 
     * for when the Delete button on a ButtonPanel is clicked.  It is called from 
     * the ButtonPanel widget.
     */
    public void delete() {
    	//strikethru all the input widgets
    	strikeThru(true);
    	
    	//set the state to delete
        changeState(State.DELETE);

        //set the message to delete
        window.setStatus(consts.get("deleteMessage"),"");           
    }
    
    public Request commitDelete(AsyncCallback callback){
        window.setStatus(consts.get("deleting"),"spinnerIcon");
    	return formService.commitDelete(key, (FormRPC)forms.get("display"),callback); 
    }
    
    public void afterCommitDelete(boolean success){
    	if(success){
            getPage(false,consts.get("deleteComplete"));        
    		strikeThru(false);
            changeState(State.DEFAULT);
        }else{
    		if(rpc.getErrors().size() > 0){
    			if(rpc.getErrors().size() > 1){
    				window.setMessagePopup((String[])rpc.getErrors().toArray(new String[rpc.getErrors().size()]), "ErrorPanel");
    				window.setStatus("(Error 1 of "+rpc.getErrors().size()+") "+(String)rpc.getErrors().get(0), "ErrorPanel");
    			}else
    				window.setStatus((String)rpc.getErrors().get(0),"ErrorPanel");
    		}    		
        }
    }

    /**
     * This method provides the default behavior for a form when the Commit button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void commit() {
        super.doSubmit();
        if (state == State.UPDATE) {
            rpc.validate();
            validate();
            if (rpc.status == Status.valid) {
                window.setStatus(consts.get("updating"),"spinnerIcon");
                clearErrors();
                commitUpdate();
            } else {
                drawErrors();
                window.setStatus(consts.get("correctErrors"),"ErrorPanel");
            }
        }
        if (state == State.ADD) {
            rpc.validate();
            validate();
            if (rpc.status == Status.valid) {
                window.setStatus(consts.get("adding"),"spinnerIcon");
                clearErrors();
                commitAdd();
            } else {
                drawErrors();
                window.setStatus(consts.get("correctErrors"),"ErrorPanel");
            }
        }
        if (state == State.QUERY) {
            rpc.validate();
            validate();
            if (rpc.status == Status.valid) {
           		window.setStatus(consts.get("querying"),"spinnerIcon");
           		clearErrors();
           		commitQuery(rpc);
            } else {
                drawErrors();
                window.setStatus(consts.get("correctErrors"),"ErrorPanel");
            }
        }
        if(state == State.DELETE){
            AsyncCallback callback = new AsyncCallback() {
                public void onSuccess(Object result){
                    rpc = (FormRPC)result;
                    forms.put(rpc.key, rpc);
                    load();
                    if (rpc.status == FormRPC.Status.invalid) {
                       drawErrors();
                      afterCommitDelete(false);
                    } else {
                        afterCommitDelete(true);
                    }
                }
                public void onFailure(Throwable caught){
                    if(caught instanceof RPCDeleteException)
                        window.setStatus(caught.getMessage(),"ErrorPanel");
                    else
                        Window.alert(caught.getMessage());
                    afterCommitDelete(false);
                }
             };
            commitDelete(callback);
        }
        
    }
    
    public Request commitUpdate() {
        SyncCallback callback = new SyncCallback() {
            public void onSuccess(Object result){
                rpc = (FormRPC)result;
                forms.put(rpc.key, rpc);
                load();
                if (rpc.status == Status.invalid) {
                   drawErrors();
                   afterCommitUpdate(false);
                } else {
                   afterCommitUpdate(true);
                }
            }
            public void onFailure(Throwable caught){
                Window.alert(caught.getMessage());
                afterCommitUpdate(false);
            }
         };
         return commitUpdate(callback);
    }
    
    public Request commitUpdate(AsyncCallback callback) {
        return formService.commitUpdate(rpc, (FormRPC)forms.get("display"), callback);
    }
    
    public void afterCommitUpdate(boolean success) {
        if(success){
            enable(false);
            changeState(State.DISPLAY);
            window.setStatus(consts.get("updatingComplete"),"");
        }else{
        	if(rpc.getErrors().size() > 0){
        		if(rpc.getErrors().size() > 1){
        			window.setMessagePopup((String[])rpc.getErrors().toArray(new String[rpc.getErrors().size()]), "ErrorPanel");
            		window.setStatus("(Error 1 of "+rpc.getErrors().size()+") "+(String)rpc.getErrors().get(0), "ErrorPanel");
            	}else
            		window.setStatus((String)rpc.getErrors().get(0),"ErrorPanel");
        	}else{
        		window.setStatus(consts.get("updateFailed"),"ErrorPanel");
        	}
        }
    }
    
    public Request commitAdd() {
        SyncCallback callback = new SyncCallback() {
            public void onSuccess(Object result){
                rpc = (FormRPC)result;
                forms.put(rpc.key,rpc);
                load();
                if (rpc.status == Status.invalid) {
                   drawErrors();
                    afterCommitAdd(false);
                } else {
                    afterCommitAdd(true);
                }
            }
            public void onFailure(Throwable caught){
                   Window.alert(caught.getMessage());
                   afterCommitAdd(false);
            }
         };
         return commitAdd(callback);
    }
    
    public Request commitAdd(AsyncCallback callback) {
        return formService.commitAdd(rpc, (FormRPC)forms.get("display"),callback); 
    }
    
    public void afterCommitAdd(boolean success) {
        if(success){
            enable(false);
            changeState(State.DEFAULT);
            window.setStatus(consts.get("addingComplete"),"");
        }else{
        	if(rpc.getErrors().size() > 0){
        		if(rpc.getErrors().size() > 1){
        			window.setMessagePopup((String[])rpc.getErrors().toArray(new String[rpc.getErrors().size()]), "ErrorPanel");
            		window.setStatus("(Error 1 of "+rpc.getErrors().size()+") "+(String)rpc.getErrors().get(0), "ErrorPanel");
            	}else
            		window.setStatus((String)rpc.getErrors().get(0),"ErrorPanel");
        	}else{
        		window.setStatus(consts.get("addingFailed"),"ErrorPanel");
        	}
        }
    }
    
    public Request commitQuery(FormRPC rpcQuery) {
        AsyncCallback callback = new AsyncCallback() {
            public void onSuccess(Object result){
                modelWidget.setModel((DataModel)result);
                afterCommitQuery(true);
                modelWidget.select(0);
            }
            public void onFailure(Throwable caught){
                Window.alert(caught.getMessage());
                afterCommitQuery(false);
            }
         };
         return commitQuery(rpcQuery,callback);
    }
    
    public Request commitQuery(FormRPC rpcQuery, AsyncCallback callback) {    	
        window.setStatus(consts.get("querying"),"spinnerIcon");
        return formService.commitQuery(rpcQuery, modelWidget.getModel(),callback); 
    }
    
    public void getPage(final boolean selectItem, final String messageText) {
    	if(modelWidget.getPage() < 0){
    		window.setStatus(consts.get("beginningQueryException"),"ErrorPanel");
    		modelWidget.getModel().setPage(0);
    		modelWidget.getModel().select(modelWidget.getSelectedIndex()+1);
    	}else{
            window.setStatus(consts.get("querying"),"spinnerIcon");
            DeferredCommand.addCommand( new Command() {
                
                public void execute() {
    	    formService.commitQuery(null, modelWidget.getModel(), new SyncCallback() {
    	        public void onSuccess(Object result){
    	            modelWidget.setModel((DataModel)result);
    	            if(selectItem){
    	                if(modelWidget.getModel().isSelectLast())
    	                    modelWidget.select(modelWidget.getModel().size()-1);
    	                else
    	                    modelWidget.select(0);
    	            }                       
    	            if(messageText == null){
    	                window.setStatus(consts.get("queryingComplete"),"");
    	            }else{
    	                message.setText(messageText);
    	            }
    	        }
                        
    	        public void onFailure(Throwable caught){
    	            
    	            if(caught instanceof LastPageException){
    	                modelWidget.getModel().setPage(modelWidget.getPage()-1);
    	                if(modelWidget.getSelectedIndex() == modelWidget.getModel().size()){
    	                    modelWidget.getModel().select(modelWidget.getSelectedIndex()-1);
    	                    window.setStatus(consts.get("endingQueryException"),"ErrorPanel");             		   
    	                }else
    	                    window.setStatus(caught.getMessage(),"ErrorPanel");
    	            }else
    	                Window.alert(caught.getMessage());
    	        }
    	        });
                }
            });
        }
    }
    
    public void afterCommitQuery(boolean success) {
        if(success){
        	resetRPC();
            load();
        	setForm(false);
            this.rpc = (FormRPC)forms.get("display");
            load();
            enable(false);
            if(modelWidget.getSelectedIndex() > -1)
            	changeState(State.DISPLAY);
            else
            	changeState(State.DEFAULT);
            window.setStatus(consts.get("queryingComplete"),"");
        }else{
            window.setStatus(consts.get("correctErrors"),"ErrorPanel");
        }
    }
    /**
     * This method provides the default behavior for a form when the Abort button 
     * on a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void abort() {
        if (state == State.UPDATE) {
            clearErrors();   
            formService.abort(key, (FormRPC)forms.get("display"), new AsyncCallback() {
               public void onSuccess(Object result){
                   rpc = (FormRPC)result;
                   forms.put(rpc.key, rpc);
                   load();
                   afterAbort(true);
               }
               public void onFailure(Throwable caught){
                   Window.alert(caught.getMessage());
                   afterAbort(false);
               }
            });
            enable(false);
            changeState(State.DISPLAY);
        }
        if (state == State.ADD) {
            resetRPC();
            load();
            clearErrors();
            enable(false);
            window.setStatus(consts.get("addAborted"),"");
            changeState(State.DEFAULT);
            afterAbort(true);
        }
        if (state == State.QUERY) {
        	clearErrors();
            setForm(false);
            this.rpc = (FormRPC)forms.get("display");
            load();
            enable(false);
            window.setStatus(consts.get("queryAborted"),"");
            
            changeState(State.DEFAULT);
        }
        if(state == State.DELETE){
        	strikeThru(false);
        	window.setStatus(consts.get("deleteAborted"),"");
        	changeState(State.DISPLAY);
        }
    }

    public void afterAbort(boolean success) {
        window.setStatus(consts.get("updateAborted"), "");
    }
    /** 
     * This method provides the default logic for a form when the Reload button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void reload() {
        fetch(null);
    }
    
    public void select() {
        // TODO Auto-generated method stub
        
    }
    /**
     * This method is called when the ScreenForm is displayed as part of a ScreenWindow widget.  
     * It will return true if the button panel is one of the three update modes and false when 
     * it is any of the display only field.
     */
    public boolean hasChanges() {
        // TODO Auto-generated method stub
        if(EnumSet.of(State.ADD,State.QUERY,State.UPDATE).contains(state)){
            window.setStatus(consts.get("mustCommitOrAbort"),"ErrorPanel");
            return true;
        }
        return false;
    }
    
    public void onDetach() {
        if(!keep){
            bpanel = null;
            message = null;      
            window = null;
            modelWidget = null;            
        }
        super.onDetach();
    }
    
    public void setForm(boolean mode){
        Iterator widIt = widgets.values().iterator();
        while(widIt.hasNext()){
            ScreenWidget wid = (ScreenWidget)widIt.next();
            if(wid instanceof ScreenInputWidget){
                ((ScreenInputWidget)wid).setForm(mode);
            }
        }
        
    }

    public void onChange(Widget sender) {

    }

    public void addCommandListener(CommandListener listener) {
        if(commandListeners == null){
            commandListeners = new CommandListenerCollection();
        }
        commandListeners.add(listener);
    }

    public void removeCommandListener(CommandListener listener) {
        if(commandListeners != null){
            commandListeners.remove(listener);
        }       
    }
    
    public void changeState(State state){
        this.state = state;
        if(commandListeners != null)
            commandListeners.fireCommand(state,this);
    }

	public ButtonPanel getBpanel() {
		return bpanel;
	}

	public void setBpanel(ButtonPanel bpanel) {
		this.bpanel = bpanel;
	}

    public void performCommand(Enum action, Object obj) {
        if(action == DataModelWidget.Action.FETCH){
            key = (DataSet)((Object[])obj)[0];
            final AsyncCallback call = ((AsyncCallback)((Object[])obj)[1]);
            resetRPC();
            AsyncCallback callback = new AsyncCallback() {
                public void onSuccess(Object result){
                    rpc = (FormRPC)result;
                    forms.put("display",(FormRPC)result);
                    load();
                    call.onSuccess(result);
                    afterFetch(true);
                }
                    
                public void onFailure(Throwable caught){
                    call.onFailure(caught);
                    afterFetch(false);
                }
            };
            modelWidget.lastRequest = fetch(callback);
        }else if(action == DataModelWidget.Action.GETPAGE)
            getPage(true, null);
        else if (action == ButtonPanel.Action.QUERY) {
            query();
        }
        else if (action == ButtonPanel.Action.NEXT) {
            next();
        }
        else if (action == ButtonPanel.Action.PREVIOUS) {
            prev();
        }
        else if (action == ButtonPanel.Action.ADD) {
            add();
        }
        else if (action == ButtonPanel.Action.UPDATE) {
            update();
        }
        else if (action == ButtonPanel.Action.DELETE) {
            delete();
        }
        else if (action == ButtonPanel.Action.COMMIT) {
            commit();
        }
        else if (action == ButtonPanel.Action.ABORT) {
            abort();
        }
        else if (action == ButtonPanel.Action.RELOAD) {
            reload();
        }
        else if (action == ButtonPanel.Action.SELECT) {
            select();
        }
    }
}
