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
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.SyncCallback;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.Form;
import org.openelis.gwt.common.LastPageException;
import org.openelis.gwt.common.Query;
import org.openelis.gwt.common.RPC;
import org.openelis.gwt.common.RPCDeleteException;
import org.openelis.gwt.common.Form.Status;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.KeyListManager;
import org.openelis.gwt.common.data.TableDataModel;
import org.openelis.gwt.common.data.TableDataRow;
import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;
import org.openelis.gwt.services.AppScreenFormServiceInt;
import org.openelis.gwt.services.AppScreenFormServiceIntAsync;
import org.openelis.gwt.widget.ButtonPanel;

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
public class AppScreenForm<ScreenRPC extends Form,QueryRPC extends RPC> extends AppScreen<ScreenRPC> implements SourcesCommandEvents, CommandListener {
   
    //public Key key;
    public AppScreenFormServiceIntAsync<ScreenRPC,QueryRPC> formService = (AppScreenFormServiceIntAsync<ScreenRPC,QueryRPC>)GWT.create(AppScreenFormServiceInt.class);
    public ServiceDefTarget target = (ServiceDefTarget)formService;
    public enum State {DEFAULT,DISPLAY,UPDATE,ADD,QUERY,BROWSE,DELETE}
    public State state = State.DEFAULT;
    protected CommandListenerCollection commandListeners;
    public enum Action {NEW_MODEL,REFRESH_PAGE,NEW_PAGE};
    public QueryRPC query;

    protected AsyncCallback<ScreenRPC> fetchCallback = new AsyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result) {
            form = result;
            loadScreen();
            setState(State.DISPLAY);
            window.setDone("Load Complete");
        }
        public void onFailure(Throwable caught){
            handleError(caught);
            window.setDone("Load Failed");
            setState(State.DEFAULT);
            form.entityKey = null;
        }
    };
    
    protected AsyncCallChain<ScreenRPC> fetchChain = new AsyncCallChain<ScreenRPC>(); 
    {
        fetchChain.add(fetchCallback);
    }
    
    protected SyncCallback<ScreenRPC> updateCallback= new SyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result){
            form = result;
            loadScreen();
            setState(State.UPDATE);
            window.setDone(consts.get("updateFields"));
        }
        public void onFailure(Throwable caught){
            handleError(caught);
            setState(State.DEFAULT);
        }            
    };
   
    protected SyncCallChain updateChain = new SyncCallChain();
    {
        updateChain.add(updateCallback);
        
    }
    
    protected SyncCallback<ScreenRPC> abortCallback = new SyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result){
            form = result;
            loadScreen();
            setState(State.DEFAULT);
            window.setDone(consts.get("updateAborted"));
           
        }
        public void onFailure(Throwable caught){
            handleError(caught);
            window.setDone(consts.get("updateAborted"));
        }
    };
   
    protected SyncCallChain abortChain = new SyncCallChain();
    { 
        abortChain.add(abortCallback);

    }
    
    protected SyncCallback<ScreenRPC> deleteCallback = new SyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result){
            form = result;
            if (form.status == Form.Status.invalid) {
                drawErrors();
            
                if(form.getErrors().size() > 0){
                    if(form.getErrors().size() > 1){
                        window.setMessagePopup((String[])form.getErrors().toArray(new String[form.getErrors().size()]), "ErrorPanel");
                        window.setError("(Error 1 of "+form.getErrors().size()+") "+(String)form.getErrors().get(0));
                    }else
                        window.setError((String)form.getErrors().get(0));
                }
            }else{
                commandListeners.fireCommand(Action.REFRESH_PAGE,null);
                strikeThru(false);
                setState(State.DEFAULT);
            }
         }
         public void onFailure(Throwable caught){
             handleError(caught);
         }
    };
    
    protected SyncCallChain deleteChain = new SyncCallChain();
    {
        deleteChain.add(deleteCallback);
    }

    protected SyncCallback<ScreenRPC> commitAddCallback = new SyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result){
            form = result;
            loadScreen();
            if(form.status == Form.Status.invalid){
                if(form.getErrors().size() == 0)
                    window.setError(consts.get("addingFailed"));
            }else {
                setState(State.DEFAULT);
                window.setDone(consts.get("addingComplete"));
            }
            
        }
        public void onFailure(Throwable caught){
            handleError(caught);
        } 
    };

    protected SyncCallChain commitAddChain = new SyncCallChain();
    {
        commitAddChain.add(commitAddCallback);
    }

    protected SyncCallback<ScreenRPC> commitUpdateCallback = new SyncCallback<ScreenRPC>() {
        public void onSuccess(ScreenRPC result){
            form = result;
            loadScreen();
            if(form.status == Form.Status.invalid){
                if(form.getErrors().size() == 0)
                    window.setError(consts.get("updateFailed"));
                
            } else {
                setState(State.DEFAULT);
                window.setDone(consts.get("updatingComplete"));
            }
        }
        public void onFailure(Throwable caught){
            handleError(caught);
        }
    };
    
    protected SyncCallChain commitUpdateChain = new SyncCallChain();
    {
        commitUpdateChain.add(commitUpdateCallback);
    }

    protected AsyncCallback<QueryRPC> commitQueryCallback = new AsyncCallback<QueryRPC>() {
        public void onSuccess(QueryRPC result){
                resetForm();
                load();
                setForm(State.DISPLAY);
                load();
                setState(State.DEFAULT);
                window.setDone(consts.get("queryingComplete"));
                commandListeners.fireCommand(Action.NEW_MODEL, result);
        }
        public void onFailure(Throwable caught){
            handleError(caught);
        }
    };
    
    protected AsyncCallChain<QueryRPC> commitQueryChain = new AsyncCallChain<QueryRPC>();
    {
        commitQueryChain.add(commitQueryCallback);
    }
    
    protected void handleError(Throwable caught) {
        if(caught instanceof RPCDeleteException)
            window.setError(caught.getMessage());
        else
            Window.alert(caught.getMessage());
        if(form.getErrors().size() > 0){
            if(form.getErrors().size() > 1){
                window.setMessagePopup((String[])form.getErrors().toArray(new String[form.getErrors().size()]), "ErrorPanel");
                window.setError("(Error 1 of "+form.getErrors().size()+") "+(String)form.getErrors().get(0));
            }else
                window.setError((String)form.getErrors().get(0));
        }  
    }
    
    protected void loadScreen() {
        //forms.put(form.key, form);
        load();
        if (form.status == Form.Status.invalid) {
            drawErrors();
        }
        if(form.getErrors() != null && form.getErrors().size() > 0){
            if(form.getErrors().size() > 1){
                window.setMessagePopup((String[])form.getErrors().toArray(new String[form.getErrors().size()]), "ErrorPanel");
                window.setError("(Error 1 of "+form.getErrors().size()+") "+(String)form.getErrors().get(0));
            }else
                window.setError((String)form.getErrors().get(0));
        }            
    }
    

    public AppScreenForm(String serviceClass) {
        super();              
        target.setServiceEntryPoint(target.getServiceEntryPoint()+"?service="+serviceClass);
        service = formService;
    }
    
    public AppScreenForm() {
        super();
    }
    
    public void afterDraw(boolean sucess) {
        super.afterDraw(sucess);
        setState(State.DEFAULT);
    }
    
    
    public void fetch() {
        fetch(form.entityKey,fetchChain);
    }
    
    public Request fetch(AsyncCallback<ScreenRPC> callback){
        return fetch(form.entityKey,callback);
    }
    
    public <T> Request fetch(T key, AsyncCallback<ScreenRPC> callback){
        window.setBusy("Loading...");
        resetForm();
        form.entityKey = key;
        return formService.fetch(form, callback);
    }
    
    /**
     * This method provides the default behavior for when the Query button from 
     * a the ButtonPanel is clicked.  It is called from the ButtonPanel Widget.
     */
    public void query() {
        form.entityKey = null;
        resetForm();
        load();
        window.setDone(consts.get("enterFieldsToQuery"));
        setForm(State.QUERY);
        //form = forms.get("query");
        //resetForm();
        //load();
        setState(State.QUERY);
        
    }
    
    /** 
     * This method provides the default behavior for a form when the Add button on a 
     * ButtonPanel is clicked.  It is called from the ButtonPanel Widget.  If overridden 
     * in the extending class be sure to call super.add(state).
     */
    public void add() {
        resetForm();
        form.entityKey = null;
        load();
        setState(State.ADD);
        window.setDone(consts.get("enterInformationPressCommit"));
    }

    public void update(){
        update(updateChain);
    }
    
    /**
     * This method provides the default behavior for when the Update button of a
     * ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public Request update(AsyncCallback<ScreenRPC> callback) {
        window.setBusy(consts.get("lockForUpdate"));
        resetForm();
        return formService.fetchForUpdate(form, callback);
    }
    
    public void delete() {
    	//strikethru all the input widgets
    	strikeThru(true);
    	
    	//set the state to delete
        setState(State.DELETE);

        //set the message to delete
        window.setDone(consts.get("deleteMessage"));           
    }
    
    public void commitDelete() {
        commitDelete(deleteChain);
    }
    
    public Request commitDelete(AsyncCallback<ScreenRPC> callback){
        window.setBusy(consts.get("deleting"));
    	return formService.commitDelete(form,callback); 
    }
    

    /**
     * This method provides the default behavior for a form when the Commit button
     * of a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void commit() {
        if (state == State.UPDATE) {
            submitForm();
            form.validate();
            if (form.status == Status.valid && validate()) {
                window.setBusy(consts.get("updating"));
                clearErrors();
                commitUpdate();
            } else {
                drawErrors();
                window.setBusy(consts.get("correctErrors"));
            }
        }
        if (state == State.ADD) {
            submitForm();
            form.validate();
            if (form.status == Status.valid && validate()) {
                window.setBusy(consts.get("adding"));
                clearErrors();
                commitAdd();
            } else {
                drawErrors();
                window.setError(consts.get("correctErrors"));
            }
        }
        if (state == State.QUERY) {
            ((Query)query).fields = getQueryFields();
            if(!((Query)query).fields.isEmpty()){
                if(!isQueryValid(((Query)query).fields)) {
                    drawErrors();
                    window.setError(consts.get("correctErrors"));
                    return;
                }
            }
            window.setBusy(consts.get("querying"));
            clearErrors();
            commitQuery();
        }
        if(state == State.DELETE){
            submitForm();
            commitDelete();
        }
        
    }
   
    public void commitUpdate() {
        commitUpdate(commitUpdateChain);
    }
    
    public Request commitUpdate(SyncCallback<ScreenRPC> callback) {
        return formService.commitUpdate(form, callback);
    }
    
    public void commitAdd() {
        commitAdd(commitAddChain);
    }
    
    public Request commitAdd(SyncCallback<ScreenRPC> callback) {
        form.key = null;
        return formService.commitAdd(form,callback); 
    }
    
    public Request commitQuery(AbstractField field) {
        ((Query)query).fields = new ArrayList<AbstractField>();
        ((Query)query).fields.add(field);
        return commitQuery();
    }
    
    public Request commitQuery() {
        return commitQuery(commitQueryChain);
    }
    
    public Request commitQuery(AsyncCallback<QueryRPC> callback) {    	
        window.setBusy(consts.get("querying"));
        ((Query)query).page = 0;
        return formService.commitQuery(query, callback); 
    }
    
    protected String messageText;
    
    protected AsyncCallback<QueryRPC> pageCallback = new AsyncCallback<QueryRPC>() {
        public void onSuccess(QueryRPC result){
            if(messageText == null){
                window.setDone(consts.get("queryingComplete"));
            }else{
                window.setDone(messageText);
            }
            commandListeners.fireCommand(Action.NEW_PAGE,result);
        }
                
        public void onFailure(Throwable caught){
            if(caught instanceof LastPageException){
                window.setError(caught.getMessage());
            }else
                Window.alert(caught.getMessage());
        }
    };
    
    public void getPage(final String messageText, final QueryRPC query, final AsyncCallback<QueryRPC> callback) {
        this.messageText = messageText;
    	//if(model.getPage() < 0){
    	//	window.setError(consts.get("beginningQueryException"));
    	//	callback.onFailure(new Exception("begining"));
    	//}else{
            window.setBusy(consts.get("querying"));
            final SyncCallChain chain = new SyncCallChain();
            chain.add(pageCallback);
            chain.add(callback);
            DeferredCommand.addCommand( new Command() {
                public void execute() {
                    formService.commitQuery(query, chain);                        

                }
            });
        //}
    }
    
    /**
     * This method provides the default behavior for a form when the Abort button 
     * on a ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void abort() {
        if (state == State.UPDATE) {
            clearErrors();   
            formService.abort(form, abortChain);
            setState(State.DISPLAY);
        }
        else if (state == State.ADD) {
            resetForm();
            load();
            clearErrors();
            setState(State.DEFAULT);
            window.setDone(consts.get("addAborted"));
        }
        else if (state == State.QUERY) {
        	clearErrors();
            setForm(State.DISPLAY);
            load();
            setState(State.DEFAULT);
            window.setDone(consts.get("queryAborted"));
        }
        else if(state == State.DELETE){
        	strikeThru(false);
        	setState(State.DISPLAY);
        	window.setDone(consts.get("deleteAborted"));
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
            window.setError(consts.get("mustCommitOrAbort"));
            return true;
        }
        return false;
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
    
    public void setState(State state){
        this.state = state;
        enable();
        if(commandListeners != null)
            commandListeners.fireCommand(state,this);
    }
    
    protected void enable() {
        Iterator wids = widgets.values().iterator();
        while (wids.hasNext()) {
            Widget wid = (Widget)wids.next();
            if(wid instanceof ScreenWidget)
            	if(((ScreenWidget)wid).enabledStates.contains(state))
            		((ScreenWidget)wid).enable(true);
            	else
            		((ScreenWidget)wid).enable(false);	
        }
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
            form.entityKey = ((Object[])obj)[0];
            final AsyncCallback call = ((AsyncCallback)((Object[])obj)[1]);
            resetForm();
            AsyncCallChain callChain = (AsyncCallChain)fetchChain.clone();
            callChain.add(call);
            fetch(callChain);
        }else if(action == KeyListManager.Action.GETPAGE)
            getPage(null,(QueryRPC)((Object[])obj)[0],(AsyncCallback)((Object[])obj)[1]);
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
    
    public ArrayList<AbstractField> getQueryFields() {
        ArrayList<AbstractField> qFields = new ArrayList<AbstractField>();
        for(ScreenWidget widget : widgets.values()) {
            if(widget instanceof ScreenInputWidget) {
                ScreenInputWidget input = (ScreenInputWidget)widget;
                input.submitQuery(qFields);
            }
        }
        return qFields;
    }
    
    public boolean isQueryValid(ArrayList<AbstractField> qFields) {
        for(AbstractField field : qFields) {
            if(!field.isValid())
                return false;
        }
        return true;
        
    }
}
