package org.openelis.gwt.screen;

//import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.IForm;
import org.openelis.gwt.common.LastPageException;
import org.openelis.gwt.common.RPCDeleteException;
import org.openelis.gwt.common.IForm.Status;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataModelWidget;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.NumberObject;
import org.openelis.gwt.services.AppScreenFormServiceIntAsync;
import org.openelis.gwt.widget.ButtonPanel;
import org.openelis.gwt.widget.FormInt;

import java.util.Iterator;
/**
 * ScreenForm extends Screen to include functionality for integrating 
 * the ButtonPanel widget and default logic for standard forms that accept
 * input and also perform queries
 * @author tschmidt
 *
 */
public class AppScreenForm extends AppScreen implements FormInt, ChangeListener, SourcesChangeEvents {
    
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
    protected ChangeListenerCollection changeListeners;
    private boolean busy = false;

    public AppScreenForm(AppScreenFormServiceIntAsync service) {
        super(service);
        formService = service;
    }
    
    public AppScreenForm() {
        super();
        modelWidget.addChangeListener(this);
    }
    
    public void afterDraw(boolean sucess) {
        super.afterDraw(sucess);
        if(bpanel != null){
            bpanel.addChangeListener(this);
            addChangeListener(bpanel);
        }
        changeState(State.DEFAULT);
        enable(false);
    }
    
    public void fetch(){
       	if(busy)
    		return;
    	busy = true;
        window.setStatus(consts.get("loading"), "spinnerIcon");

    	
        formService.fetch(key, (FormRPC)forms.get("display"), new AsyncCallback(){
           public void onSuccess(Object result){
               rpc = (FormRPC)result;
               forms.put(rpc.key, rpc);
               load();
               afterFetch(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterFetch(false);
           }
        });
    }
    
    public void afterFetch(boolean success){
       if(!success)
            key = null;
    	busy = false;
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
        doReset();
        window.setStatus(consts.get("enterFieldsToQuery"),"");
        setForm(true);
        rpc = (FormRPC)forms.get("query");
        doReset();
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
        doReset();
        enable(true);
        changeState(State.ADD);
        window.setStatus(consts.get("enterInformationPressCommit"),"");
    }

    /**
     * This method provides the default behavior for when the Update button of a
     * ButtonPanel is clicked.  It is called from the ButtonPanel widget.
     */
    public void update() {
        window.setStatus(consts.get("lockForUpdate"),"spinnerIcon");
        formService.fetchForUpdate(key, (FormRPC)forms.get("display"), new AsyncCallback() {
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
        });
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
    
    public void commitDelete(){
    	if(busy)
    		return;
    	busy = true;
        window.setStatus(consts.get("deleting"),"spinnerIcon");
    	formService.commitDelete(key, (FormRPC)forms.get("display"), new AsyncCallback() {
            public void onSuccess(Object result){
                rpc = (FormRPC)result;
                forms.put(rpc.key, rpc);
                load();
                if (rpc.status == Status.invalid) {
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
         });
    }
    
    public void afterCommitDelete(boolean success){
    	busy = false;
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
            rpc.operation = IForm.UPDATE;
            if (rpc.validate() & validate()) {
                window.setStatus(consts.get("updating"),"spinnerIcon");
                clearErrors();
                commitUpdate();
            } else {
                drawErrors();
                window.setStatus(consts.get("correctErrors"),"ErrorPanel");
            }
        }
        if (state == State.ADD) {
            rpc.operation = IForm.UPDATE;
            if (rpc.validate() & validate()) {
                window.setStatus(consts.get("adding"),"spinnerIcon");
                clearErrors();
                commitAdd();
            } else {
                drawErrors();
                window.setStatus(consts.get("correctErrors"),"ErrorPanel");
            }
        }
        if (state == State.QUERY) { 
            rpc.operation = IForm.QUERY;
            if (rpc.validate() & validate()) {
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
    	if(busy)
    		return;
    	busy = true;
    	
        formService.commitUpdate(rpc, (FormRPC)forms.get("display"),new AsyncCallback() {
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
        });
    }
    
    public void afterCommitUpdate(boolean success) {
    	busy = false;
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
    
    public void commitAdd() {
    	if(busy)
    		return;
    	busy = true;
        formService.commitAdd(rpc, (FormRPC)forms.get("display"), new AsyncCallback() {
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
        });
    }
    
    public void afterCommitAdd(boolean success) {
    	busy = false;
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
    
    public void commitQuery(FormRPC rpcQuery) {
    	if(busy)
    		return;
    	busy = true;
    	
        window.setStatus(consts.get("querying"),"spinnerIcon");
        formService.commitQuery(rpcQuery, modelWidget.getModel(), new AsyncCallback() {
           public void onSuccess(Object result){
               modelWidget.setModel((DataModel)result);
               afterCommitQuery(true);
               modelWidget.select(0);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterCommitQuery(false);
           }
        });
    }
    
    public void getPage(final boolean selectItem, final String messageText) {
    	if(modelWidget.getPage() < 0){
    		window.setStatus(consts.get("beginningQueryException"),"ErrorPanel");
    		modelWidget.getModel().setPage(0);
    		modelWidget.getModel().select(modelWidget.getSelectedIndex()+1);
    	}else{
            window.setStatus(consts.get("querying"),"spinnerIcon");
    	    formService.commitQuery(null, modelWidget.getModel(), new AsyncCallback() {
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
    }
    
    public void afterCommitQuery(boolean success) {
    	busy = false;
        if(success){
        	doReset();
        	setForm(false);
            load((FormRPC)forms.get("display"));
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
            rpc.operation = IForm.CANCEL;
            clearErrors();
            doReset();
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
            doReset();
            clearErrors();
            enable(false);
            window.setStatus(consts.get("addAborted"),"");
            changeState(State.DEFAULT);
            afterAbort(true);
        }
        if (state == State.QUERY) {
        	clearErrors();
            setForm(false);
            load((FormRPC)forms.get("display"));
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
        if(state == State.ADD ||
           state == State.QUERY ||
           state == State.UPDATE){
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
    	//clear the message, the action method can set it to something else
    	if(message != null)
    		window.setStatus("","");
    	
        if(sender == modelWidget){
            if(modelWidget.event == DataModelWidget.SELECTION){
                key = (DataSet)((DataSet)modelWidget.getSelected()).getInstance();
                fetch();
            }
            if(modelWidget.event == DataModelWidget.GETPAGE)
                getPage(true, null);
        }
        if(sender == bpanel){
            if (bpanel.buttonClicked.action.equals("query")) {
                query();
            }
            else if (bpanel.buttonClicked.action.equals("next")) {
                next();
            }
            else if (bpanel.buttonClicked.action.equals("prev")) {
                prev();
            }
            else if (bpanel.buttonClicked.action.equals("add")) {
                add();
            }
            else if (bpanel.buttonClicked.action.equals("update")) {
                update();
            }
            else if (bpanel.buttonClicked.action.equals("delete")) {
                delete();
            }
            else if (bpanel.buttonClicked.action.equals("commit")) {
                commit();
            }
            else if (bpanel.buttonClicked.action.equals("abort")) {
                abort();
            }
            else if (bpanel.buttonClicked.action.equals("reload")) {
                reload();
            }
            else if (bpanel.buttonClicked.action.equals("select")) {
                select();
            }
        }
    }

    public void addChangeListener(ChangeListener listener) {
        if(changeListeners == null){
            changeListeners = new ChangeListenerCollection();
        }
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if(changeListeners != null){
            changeListeners.remove(listener);
        }       
    }
    
    public void changeState(State state){
        this.state = state;
        if(changeListeners != null)
            changeListeners.fireChange(this);
    }

	public ButtonPanel getBpanel() {
		return bpanel;
	}

	public void setBpanel(ButtonPanel bpanel) {
		this.bpanel = bpanel;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}
}