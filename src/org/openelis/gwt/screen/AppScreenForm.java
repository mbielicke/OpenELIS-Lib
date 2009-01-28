/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.screen;

//import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.Form;
import org.openelis.gwt.common.LastPageException;
import org.openelis.gwt.common.RPC;
import org.openelis.gwt.common.RPCDeleteException;
import org.openelis.gwt.common.Form.Status;
import org.openelis.gwt.common.data.Data;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.KeyListManager;
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
public class AppScreenForm<ScreenRPC extends RPC,QModel extends DataModel<? extends DataSet>,Display extends Form> extends AppScreen<ScreenRPC> implements FormInt, SourcesCommandEvents, CommandListener {
   
    public DataSet key;
    public AppScreenFormServiceIntAsync<ScreenRPC,QModel> formService;
    public State state = State.DEFAULT;
    protected CommandListenerCollection commandListeners;
    public enum Action {NEW_MODEL,REFRESH_PAGE,NEW_PAGE};

    protected AsyncCallback<? extends Data> fetchCallback = new AsyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result) {
            rpc = result;
            loadScreen(rpc.form);
            window.setStatus("Load Complete", "");
            changeState(State.DISPLAY);
        }
        public void onFailure(Throwable caught){
            handleError(caught);
            window.setStatus("Load Failed", "");
            changeState(State.DEFAULT);
            key = null;
        }
    };
    
    protected AsyncCallChain<? extends Data> fetchChain = new AsyncCallChain<Data>(); 
    {
        fetchChain.add(fetchCallback);
    }
    
    protected AsyncCallback<? extends Data> updateCallback= new AsyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result){
            rpc = result;
            loadScreen(rpc.form);
            window.setStatus(consts.get("updateFields"),"");
            changeState(State.UPDATE);
        }
        public void onFailure(Throwable caught){
            handleError(caught);
            enable(false);
            changeState(State.DISPLAY);
        }            
    };
   
    protected AsyncCallChain<? extends Data> updateChain = new AsyncCallChain<Data>();
    {
        updateChain.add(updateCallback);
        
    }
    
    protected AsyncCallback<? extends Data> abortCallback = new AsyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result){
            rpc = result;
            loadScreen(rpc.form);
            window.setStatus(consts.get("updateAborted"), "");
           
        }
        public void onFailure(Throwable caught){
            handleError(caught);
            window.setStatus(consts.get("updateAborted"), "");
        }
    };
   
    protected AsyncCallChain<? extends Data> abortChain = new AsyncCallChain<Data>();
    { 
        abortChain.add(abortCallback);

    }
    
    protected AsyncCallback<? extends Data> deleteCallback = new AsyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result){
            rpc = result;
            Form delete = rpc.form;
            if (delete.status == Form.Status.invalid) {
                drawErrors();
            
                if(delete.getErrors().size() > 0){
                    if(delete.getErrors().size() > 1){
                        window.setMessagePopup((String[])delete.getErrors().toArray(new String[delete.getErrors().size()]), "ErrorPanel");
                        window.setStatus("(Error 1 of "+delete.getErrors().size()+") "+(String)delete.getErrors().get(0), "ErrorPanel");
                    }else
                        window.setStatus((String)delete.getErrors().get(0),"ErrorPanel");
                }
            }else{
                commandListeners.fireCommand(Action.REFRESH_PAGE,null);
                strikeThru(false);
                changeState(State.DEFAULT);
            }
         }
         public void onFailure(Throwable caught){
             handleError(caught);
         }
    };
    
    protected AsyncCallChain<? extends Data> deleteChain = new AsyncCallChain<Data>();
    {
        deleteChain.add(deleteCallback);
    }

    protected AsyncCallback<? extends Data> commitAddCallback = new AsyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result){
            rpc = result;
            loadScreen(rpc.form);
            if(form.status == Form.Status.invalid){
                if(form.getErrors().size() == 0)
                    window.setStatus(consts.get("addingFailed"),"ErrorPanel");
            }else {
                enable(false);
                changeState(State.DEFAULT);
                window.setStatus(consts.get("addingComplete"),"");
            }
            
        }
        public void onFailure(Throwable caught){
            handleError(caught);
        } 
    };

    protected AsyncCallChain<? extends Data> commitAddChain = new AsyncCallChain<Data>();
    {
        commitAddChain.add(commitAddCallback);
    }

    protected AsyncCallback<? extends Data> commitUpdateCallback = new AsyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result){
            rpc = result;
            loadScreen(rpc.form);
            if(form.status == Form.Status.invalid){
                if(form.getErrors().size() == 0)
                    window.setStatus(consts.get("updateFailed"),"ErrorPanel");
                
            } else {
                enable(false);
                changeState(State.DISPLAY);
                window.setStatus(consts.get("updatingComplete"),"");
                clearStatus(5000);
            }
        }
        public void onFailure(Throwable caught){
            handleError(caught);
        }
    };
    
    protected AsyncCallChain<? extends Data> commitUpdateChain = new AsyncCallChain<Data>();
    {
        commitUpdateChain.add(commitUpdateCallback);
    }

    protected AsyncCallback<QModel> commitQueryCallback = new AsyncCallback<QModel>() {
        public void onSuccess(QModel result){
            try {
                resetForm();
                load();
                setForm(FormInt.State.DISPLAY);
                form = rpc.form;
                load();
                enable(false);
                window.setStatus(consts.get("queryingComplete"),"");
                changeState(State.DEFAULT);
                commandListeners.fireCommand(Action.NEW_MODEL, result);
            }catch(Exception e){
                Window.alert(e.getMessage());
            }
        }
        public void onFailure(Throwable caught){
            handleError(caught);
        }
    };
    
    protected AsyncCallChain<? extends Data> commitQueryChain = new AsyncCallChain<Data>();
    {
        commitQueryChain.add(commitQueryCallback);
    }
    
    protected void handleError(Throwable caught) {
        if(caught instanceof RPCDeleteException)
            window.setStatus(caught.getMessage(),"ErrorPanel");
        else
            Window.alert(caught.getMessage());
        if(form.getErrors().size() > 0){
            if(form.getErrors().size() > 1){
                window.setMessagePopup((String[])form.getErrors().toArray(new String[form.getErrors().size()]), "ErrorPanel");
                window.setStatus("(Error 1 of "+form.getErrors().size()+") "+(String)form.getErrors().get(0), "ErrorPanel");
            }else
                window.setStatus((String)form.getErrors().get(0),"ErrorPanel");
        }  
    }
    
    protected void loadScreen(Form form) {
        this.form = form;
        forms.put(form.key, form);
        load();
        if (form.status == Form.Status.invalid) {
            drawErrors();
        }
        if(form.getErrors().size() > 0){
            if(form.getErrors().size() > 1){
                window.setMessagePopup((String[])form.getErrors().toArray(new String[form.getErrors().size()]), "ErrorPanel");
                window.setStatus("(Error 1 of "+form.getErrors().size()+") "+(String)form.getErrors().get(0), "ErrorPanel");
            }else
                window.setStatus((String)form.getErrors().get(0),"ErrorPanel");
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
    }
    
    
    public void fetch() {
        fetch(key,fetchChain);
    }
    
    public Request fetch(AsyncCallback<? extends Data> callback){
        return fetch(key,callback);
    }
    
    public Request fetch(DataSet key, AsyncCallback<? extends Data> callback){
        window.setStatus("Loading...", "spinnerIcon");
        rpc.key = key;
        return formService.fetch(rpc, callback);
    }
    
    /**
     * This method provides the default behavior for when the Query button from 
     * a the ButtonPanel is clicked.  It is called from the ButtonPanel Widget.
     */
    public void query() {
        key = null;
        resetForm();
        load();
        window.setStatus(consts.get("enterFieldsToQuery"),"");
        setForm(FormInt.State.QUERY);
        form = forms.get("query");
        resetForm();
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
        resetForm();
        key = null;
        load();
        enable(true);
        changeState(State.ADD);
        window.setStatus(consts.get("enterInformationPressCommit"),"");
    }

    public void update(){
        enable(true);
        update(updateChain);
    }
    
    /**
     * This method provides the default behavior for when the Update button of a
     * ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public Request update(AsyncCallback<? extends Data> callback) {
        window.setStatus(consts.get("lockForUpdate"),"spinnerIcon");
        resetForm();
        return formService.fetchForUpdate(rpc, callback);
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
    
    public Request commitDelete(AsyncCallback<? extends Data> callback){
        window.setStatus(consts.get("deleting"),"spinnerIcon");
    	return formService.commitDelete(rpc,callback); 
    }
    

    /**
     * This method provides the default behavior for a form when the Commit button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void commit() {
        submitForm();
        if (state == State.UPDATE) {
            form.validate();
            if (form.status == Status.valid && validate()) {
                window.setStatus(consts.get("updating"),"spinnerIcon");
                clearErrors();
                commitUpdate();
            } else {
                drawErrors();
                window.setStatus(consts.get("correctErrors"),"ErrorPanel");
            }
        }
        if (state == State.ADD) {
            form.validate();
            if (form.status == Status.valid && validate()) {
                window.setStatus(consts.get("adding"),"spinnerIcon");
                clearErrors();
                commitAdd();
            } else {
                drawErrors();
                window.setStatus(consts.get("correctErrors"),"ErrorPanel");
            }
        }
        if (state == State.QUERY) {
            form.validate();
            if (form.status == Status.valid && validate()) {
           		window.setStatus(consts.get("querying"),"spinnerIcon");
           		clearErrors();
           		commitQuery(form);
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
    
    public Request commitUpdate(AsyncCallback<? extends Data> callback) {
        return formService.commitUpdate(rpc, callback);
    }
    
    public void commitAdd() {
        commitAdd(commitAddChain);
    }
    
    public Request commitAdd(AsyncCallback<? extends Data> callback) {
        rpc.key = null;
        return formService.commitAdd(rpc,callback); 
    }
    
    public Request commitQuery(Form form) {
        return commitQuery(form,commitQueryChain);
    }
    
    public Request commitQuery(Form form, AsyncCallback<? extends Data> callback) {    	
        window.setStatus(consts.get("querying"),"spinnerIcon");
        return formService.commitQuery(form, null, callback); 
    }
    
    protected String messageText;
    
    protected AsyncCallback<? extends Data> pageCallback = new AsyncCallback<DataModel>() {
        public void onSuccess(DataModel result){
            if(messageText == null){
                window.setStatus(consts.get("queryingComplete"),"");
            }else{
                window.setStatus(messageText, "");
            }
            commandListeners.fireCommand(Action.NEW_PAGE,result);
        }
                
        public void onFailure(Throwable caught){
            if(caught instanceof LastPageException){
                window.setStatus(caught.getMessage(),"ErrorPanel");
            }else
                Window.alert(caught.getMessage());
        }
    };
    
    public void getPage(final String messageText, final QModel model, final AsyncCallback<? extends Data> callback) {
        this.messageText = messageText;
    	if(model.getPage() < 0){
    		window.setStatus(consts.get("beginningQueryException"),"ErrorPanel");
    		callback.onFailure(new Exception("begining"));
    	}else{
            window.setStatus(consts.get("querying"),"spinnerIcon");
            final SyncCallChain chain = new SyncCallChain();
            chain.add(pageCallback);
            chain.add(callback);
            DeferredCommand.addCommand( new Command() {
                public void execute() {
                    formService.commitQuery(null, model, chain);                        

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
            formService.abort(rpc, abortChain);
            enable(false);
            changeState(State.DISPLAY);
        }
        if (state == State.ADD) {
            resetForm();
            load();
            clearErrors();
            enable(false);
            window.setStatus(consts.get("addAborted"),"");
            changeState(State.DEFAULT);
        }
        if (state == State.QUERY) {
        	clearErrors();
            setForm(FormInt.State.DISPLAY);
            this.form = forms.get("display");
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
    
    public void setForm(State state){
        Iterator widIt = widgets.values().iterator();
        while(widIt.hasNext()){
            ScreenWidget wid = (ScreenWidget)widIt.next();
            if(wid instanceof ScreenInputWidget){
                ((ScreenInputWidget)wid).setForm(state);
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
               (action == ButtonPanel.Action.SELECT) ||
               (action.getDeclaringClass().equals(State.class));
    }
    
    public void performCommand(Enum action, Object obj) {
        if(action == KeyListManager.Action.FETCH){
            key = (DataSet)((Object[])obj)[0];
            final AsyncCallback call = ((AsyncCallback)((Object[])obj)[1]);
            resetForm();
            AsyncCallChain callChain = (AsyncCallChain)fetchChain.clone();
            callChain.add(call);
            fetch(callChain);
        }else if(action == KeyListManager.Action.GETPAGE)
            getPage(null,(QModel)((Object[])obj)[0],(AsyncCallback)((Object[])obj)[1]);
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
