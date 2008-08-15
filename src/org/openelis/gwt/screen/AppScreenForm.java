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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SyncCallback;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.LastPageException;
import org.openelis.gwt.common.RPCDeleteException;
import org.openelis.gwt.common.FormRPC.Status;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.KeyListManager;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;
import org.openelis.gwt.services.AppScreenFormServiceIntAsync;
import org.openelis.gwt.widget.ButtonPanel;
import org.openelis.gwt.widget.FormInt;

import java.util.ArrayList;
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
    
    protected DataSet key;
    public AppScreenFormServiceIntAsync formService;
    public State state = State.DEFAULT;
    protected CommandListenerCollection commandListeners;
    public enum Action {NEW_MODEL,REFRESH_PAGE,NEW_PAGE};

    protected AsyncCallback fetchCallback = new AsyncCallback() {
        public void onSuccess(Object result) {
            loadScreen((FormRPC)result);
            window.setStatus("Load Complete", "");
            changeState(State.DISPLAY);
            clearStatus(5000);
        }
        public void onFailure(Throwable caught){
            handleError(caught);
            window.setStatus("Load Failed", "");
            changeState(State.DEFAULT);
            key = null;
        }
    };
    
    protected AsyncCallback afterFetch = new AsyncCallback() {
        public void onSuccess(Object result){
            
        }
        public void onFailure(Throwable caught){
            
        }
    };
    
    protected AsyncCallChain fetchChain = new AsyncCallChain(); 
    {
        fetchChain.add(fetchCallback);
    }
    
    protected AsyncCallback updateCallback= new AsyncCallback() {
        public void onSuccess(Object result){
            loadScreen((FormRPC)result);
            enable(true);
            window.setStatus(consts.get("updateFields"),"");
            changeState(State.UPDATE);
            clearStatus(5000);
        }
        public void onFailure(Throwable caught){
            handleError(caught);
            changeState(State.DISPLAY);
        }            
    };
   
    protected AsyncCallback afterUpdate = new AsyncCallback() {
        public void onSuccess(Object result){
            
        }
        public void onFailure(Throwable caught){
            
        }
    };
    
    protected AsyncCallChain updateChain = new AsyncCallChain();
    {
        updateChain.add(updateCallback);
        
    }
    
    protected AsyncCallback abortCallback = new AsyncCallback() {
        public void onSuccess(Object result){
            loadScreen((FormRPC)result);
            window.setStatus(consts.get("updateAborted"), "");
            clearStatus(5000);
        }
        public void onFailure(Throwable caught){
            handleError(caught);
            window.setStatus(consts.get("updateAborted"), "");
        }
    };
   
    protected AsyncCallback afterAbort = new AsyncCallback() {
        public void onSuccess(Object result){
            
        }
        public void onFailure(Throwable caught){
            
        }
    };
    
    protected AsyncCallChain abortChain = new AsyncCallChain();
    {   
        abortChain.add(abortCallback);
    }
    
    protected AsyncCallback deleteCallback = new AsyncCallback() {
        public void onSuccess(Object result){
            commandListeners.fireCommand(Action.REFRESH_PAGE,null);
            strikeThru(false);
            changeState(State.DEFAULT);
         }
         public void onFailure(Throwable caught){
             handleError(caught);
         }
    };
    
    protected AsyncCallback afterDelete = new AsyncCallback() {
        public void onSuccess(Object result){
            
        }
        public void onFailure(Throwable caught){
            
        }
    };
    
    protected AsyncCallChain deleteChain = new AsyncCallChain();
    {
        deleteChain.add(deleteCallback);
    }

    protected AsyncCallback commitAddCallback = new AsyncCallback() {
        public void onSuccess(Object result){
            loadScreen((FormRPC)result);
            enable(false);
            changeState(State.DEFAULT);
            window.setStatus(consts.get("addingComplete"),"");
            clearStatus(5000);
        }
        public void onFailure(Throwable caught){
            handleError(caught);
        } 
    };

    protected AsyncCallback afterCommitAdd = new AsyncCallback() {
        public void onSuccess(Object result){
            
        }
        public void onFailure(Throwable caught){
            
        }
    };
    
    protected AsyncCallChain commitAddChain = new AsyncCallChain();
    {
         commitAddChain.add(commitAddCallback);
    }

    protected AsyncCallback commitUpdateCallback = new AsyncCallback() {
        public void onSuccess(Object result){
            loadScreen((FormRPC)result);
            enable(false);
            changeState(State.DISPLAY);
            window.setStatus(consts.get("updatingComplete"),"");
            clearStatus(5000);
        }
        public void onFailure(Throwable caught){
            handleError(caught);
        }
    };
    
    protected AsyncCallback afterCommitUpdate = new AsyncCallback() {
        public void onSuccess(Object result){
            
        }
        public void onFailure(Throwable caught){
            
        }
    };
    
    protected AsyncCallChain commitUpdateChain = new AsyncCallChain();
    {
        commitUpdateChain.add(commitUpdateCallback);
    }

    protected AsyncCallback commitQueryCallback = new AsyncCallback() {
        public void onSuccess(Object result){
            try {
            commandListeners.fireCommand(Action.NEW_MODEL, result);
            resetRPC();
            load();
            setForm(false);
            rpc = (FormRPC)forms.get("display");
            load();
            enable(false);
            window.setStatus(consts.get("queryingComplete"),"");
            }catch(Exception e){
                Window.alert(e.getMessage());
            }
        }
        public void onFailure(Throwable caught){
            handleError(caught);
        }
    };
    
    protected AsyncCallback afterCommitQuery = new AsyncCallback() {
        public void onSuccess(Object result){
            
        }
        public void onFailure(Throwable caught){
            
        }
    };
    
    protected AsyncCallChain commitQueryChain = new AsyncCallChain();
    {
        commitQueryChain.add(commitQueryCallback);
    
    }
    
    protected void handleError(Throwable caught) {
        if(caught instanceof RPCDeleteException)
            window.setStatus(caught.getMessage(),"ErrorPanel");
        else
            Window.alert(caught.getMessage());
        if(rpc.getErrors().size() > 0){
            if(rpc.getErrors().size() > 1){
                window.setMessagePopup((String[])rpc.getErrors().toArray(new String[rpc.getErrors().size()]), "ErrorPanel");
                window.setStatus("(Error 1 of "+rpc.getErrors().size()+") "+(String)rpc.getErrors().get(0), "ErrorPanel");
            }else
                window.setStatus((String)rpc.getErrors().get(0),"ErrorPanel");
        }            
    }
    
    protected void loadScreen(FormRPC rpc) {
        this.rpc = rpc;
        forms.put(rpc.key, rpc);
        load();
        if (rpc.status == FormRPC.Status.invalid) {
            drawErrors();
        }
    }
    
    protected void clearStatus(int delay) {
       Timer timer = new Timer() {
            public void run() {
                window.setStatus("", "");
            }
        };
        timer.schedule(delay);
    }

    public AppScreenForm(AppScreenFormServiceIntAsync service) {
        super(service);
        formService = service;
    }
    
    public AppScreenForm() {
        super();
    }
    
    public void afterDraw(boolean sucess) {
        super.afterDraw(sucess);
        changeState(State.DEFAULT);
        enable(false);
        fetchChain.add(afterFetch);
        updateChain.add(afterUpdate);
        abortChain.add(afterAbort);
        deleteChain.add(afterDelete);
        commitUpdateChain.add(afterCommitUpdate);
        commitAddChain.add(afterCommitAdd);
        commitQueryChain.add(afterCommitQuery);
        
    }
    
    
    public void fetch() {
        fetch((FormRPC)forms.get("display"),fetchChain);
    }
    
    public Request fetch(AsyncCallback callback){
        return fetch((FormRPC)forms.get("display"),callback);
    }
        
    public Request fetch(FormRPC rpc, AsyncCallback callback){
        return fetch(key, rpc, callback);
    }
    
    public Request fetch(DataSet key, FormRPC rpc, AsyncCallback callback){
        window.setStatus("Loading...", "spinnerIcon");
        return formService.fetch(key, rpc, callback);
    }
    
    /**
     * This method provides the default behavior for when the Query button from 
     * a the ButtonPanel is clicked.  It is called from the ButtonPanel Widget.
     */
    public void query() {
        resetRPC();
        load();
        window.setStatus(consts.get("enterFieldsToQuery"),"");
        setForm(true);
        rpc = (FormRPC)forms.get("query");
        resetRPC();
        load();
        enable(true); 
        changeState(State.QUERY);
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

    public void update(){
        update(updateChain);
    }
    
    /**
     * This method provides the default behavior for when the Update button of a
     * ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public Request update(AsyncCallback callback) {
        window.setStatus(consts.get("lockForUpdate"),"spinnerIcon");
        return formService.fetchForUpdate(key, (FormRPC)forms.get("display"), callback);
    }
    
    public void delete() {
    	//strikethru all the input widgets
    	strikeThru(true);
    	
    	//set the state to delete
        changeState(State.DELETE);

        //set the message to delete
        window.setStatus(consts.get("deleteMessage"),"");           
    }
    
    public void commitDelete() {
        commitDelete(deleteChain);
    }
    
    public Request commitDelete(AsyncCallback callback){
        window.setStatus(consts.get("deleting"),"spinnerIcon");
    	return formService.commitDelete(key, (FormRPC)forms.get("display"),callback); 
    }
    

    /**
     * This method provides the default behavior for a form when the Commit button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void commit() {
        super.doSubmit();
        if (state == State.UPDATE) {
            rpc.validate();
            if (rpc.status == Status.valid && validate()) {
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
            if (rpc.status == Status.valid && validate()) {
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
            if (rpc.status == Status.valid && validate()) {
           		window.setStatus(consts.get("querying"),"spinnerIcon");
           		clearErrors();
           		commitQuery(rpc);
            } else {
                drawErrors();
                window.setStatus(consts.get("correctErrors"),"ErrorPanel");
            }
        }
        if(state == State.DELETE){
            commitDelete();
        }
        
    }
   
    public void commitUpdate() {
        commitUpdate(commitUpdateChain);
    }
    
    public Request commitUpdate(AsyncCallback callback) {
        return formService.commitUpdate(rpc, (FormRPC)forms.get("display"), callback);
    }
    
    public void commitAdd() {
        commitAdd(commitAddChain);
    }
    
    public Request commitAdd(AsyncCallback callback) {
        return formService.commitAdd(rpc, (FormRPC)forms.get("display"),callback); 
    }
    
    public Request commitQuery(FormRPC rpcQuery) {
        return commitQuery(rpcQuery,commitQueryChain);
    }
    
    public Request commitQuery(FormRPC rpcQuery, AsyncCallback callback) {    	
        window.setStatus(consts.get("querying"),"spinnerIcon");
        return formService.commitQuery(rpcQuery, null, callback); 
    }
    
    public void getPage(final String messageText, final DataModel model) {
    	if(model.getPage() < 0){
    		window.setStatus(consts.get("beginningQueryException"),"ErrorPanel");
    		model.setPage(0);
    	}else{
            window.setStatus(consts.get("querying"),"spinnerIcon");
            DeferredCommand.addCommand( new Command() {
                public void execute() {
                    formService.commitQuery(null, model, new SyncCallback() {                        
                        public void onSuccess(Object result){
                            commandListeners.fireCommand(Action.NEW_PAGE, result);
                            if(messageText == null){
                                window.setStatus(consts.get("queryingComplete"),"");
                            }else{
                                window.setStatus(messageText, "");
                            }
                        }
                                
                        public void onFailure(Throwable caught){
                            if(caught instanceof LastPageException){
                                window.setStatus(caught.getMessage(),"ErrorPanel");
                            }else
                                Window.alert(caught.getMessage());
                        }
                    });
                }
            });
        }
    }
    
    /**
     * This method provides the default behavior for a form when the Abort button 
     * on a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void abort() {
        if (state == State.UPDATE) {
            clearErrors();   
            formService.abort(key, (FormRPC)forms.get("display"), abortChain);
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

    /** 
     * This method provides the default logic for a form when the Reload button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void reload() {
        fetch();
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
            window = null;
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
    
    public boolean canPerformCommand(Enum action, Object obj) {
        return (action == KeyListManager.Action.FETCH) ||
               (action == KeyListManager.Action.GETPAGE) ||
               (action == ButtonPanel.Action.QUERY) ||
               (action == ButtonPanel.Action.ABORT) ||
               (action == ButtonPanel.Action.UPDATE) ||
               (action == ButtonPanel.Action.DELETE) ||
               (action == ButtonPanel.Action.COMMIT) ||
               (action == ButtonPanel.Action.ADD) ||
               (action == ButtonPanel.Action.RELOAD) ||
               (action == ButtonPanel.Action.SELECT);
    }
    
    public void performCommand(Enum action, Object obj) {
        if(action == KeyListManager.Action.FETCH){
            key = (DataSet)((Object[])obj)[0];
            final AsyncCallback call = ((AsyncCallback)((Object[])obj)[1]);
            resetRPC();
            AsyncCallChain callChain = (AsyncCallChain)fetchChain.clone();
            callChain.add(call);
            fetch(callChain);
        }else if(action == KeyListManager.Action.GETPAGE)
            getPage(null,(DataModel)obj);
        else if (action == ButtonPanel.Action.QUERY) {
            query();
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

    public void next() {
        // TODO Auto-generated method stub
        
    }

    public void prev() {
        // TODO Auto-generated method stub
        
    }
}
