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
package org.openelis.gwt.common.data;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;
import org.openelis.gwt.screen.AppScreenForm;
import org.openelis.gwt.widget.AToZTable;
import org.openelis.gwt.widget.ButtonPanel;
import org.openelis.gwt.widget.FormInt;


    public class KeyListManager implements SourcesCommandEvents, CommandListener {

    public enum Action {SELECTION,REFRESH,GETPAGE,ADD,DELETE,FETCH,UNSELECT}
    
    public Action action;
    
    private CommandListenerCollection commandListeners;
    
    private DataModel list = new DataModel();
    
    private int candidate = 0;
        
    private boolean selectItem;
    private boolean selectLast;

    
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
    
    public void setModel(DataModel list){
        this.list = list;
        candidate = 0;
        //fireCommand(Action.REFRESH,list);
    }
    
    public DataModel getList() {
        return list;
    }
    
    public void next() {
        select(++candidate);
    }
    
    public void previous() {
        select(--candidate);
    }
    
    public void select(final int selection) throws IndexOutOfBoundsException {
        if(selection > list.size() - 1){
            selectItem = true;
            setPage(list.getPage()+1);
        }else if(selection < 0){
            //if(list.getPage() > 0){
                selectItem = true;
                selectLast = true;
                setPage(list.getPage()-1);
            //}else
              //  candidate = 0;
        }else{
            AsyncCallback callback = new AsyncCallback() {
                public void onSuccess(Object result){
                    list.select(selection);
                    candidate = selection;
                    fireCommand(Action.SELECTION,new Integer(selection));
                }
            
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
            
                }
            
            };
            fireCommand(Action.FETCH,new Object[] {list.get(selection).getInstance(),callback});
        }
    }
    
    public void add(DataSet set){
        list.add(set);
        fireCommand(Action.ADD,null);
    }
    
    public DataSet getSelected() {
        return list.getSelected();
    }
    
    public void delete(int index){
        list.remove(index);
        fireCommand(Action.DELETE,new Integer(index));
    }
    
    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }
    
    public int getPage(){
        return list.getPage();
    }
    
    public void setPage(int page) {
        final int currPage = list.getPage();
        list.setPage(page);
        AsyncCallback callback = new AsyncCallback() {
            public void onSuccess(Object result) {
                setModel((DataModel)result);
                if(selectItem){
                    if(selectLast)
                        select(getList().size()-1);
                    else
                        select(0);
                }                
                selectItem = false;
                selectLast = false;

            }
            
            public void onFailure(Throwable caught){
                list.setPage(currPage);
                candidate = list.getSelectedIndex();
            }

        };
        fireCommand(Action.GETPAGE,new Object[] {list,callback});
    }
    
    public void unselect() {
        list.select(-1);
        fireCommand(Action.UNSELECT,null);
    }
    
    public boolean canPerformCommand(Enum action, Object obj){
        return (action == ButtonPanel.Action.NEXT) ||
               (action == ButtonPanel.Action.PREVIOUS) ||
               (action == AppScreenForm.Action.NEW_PAGE) ||
               (action == AppScreenForm.Action.NEW_MODEL) ||
               (action == AToZTable.Action.NEXT_PAGE) ||
               (action == AToZTable.Action.PREVIOUS_PAGE) ||
               (action == AppScreenForm.Action.REFRESH_PAGE) ||
               (action == AToZTable.Action.ROW_SELECTED) ||
               (action == FormInt.State.ADD);
    }

    public void performCommand(Enum action, Object obj) {
        if(action == ButtonPanel.Action.NEXT){
            next();
        }else if(action == ButtonPanel.Action.PREVIOUS){
            previous();
        }else if(action == AppScreenForm.Action.NEW_PAGE){
          /*  setModel((DataModel)obj);
            if(selectItem){
                if(selectLast)
                    select(getList().size()-1);
                else
                    select(0);
            }                
            selectItem = false;
            selectLast = false;
            */
        }else if(action == AppScreenForm.Action.NEW_MODEL){
            setModel((DataModel)obj);
            select(0);
        }else if(action == AToZTable.Action.NEXT_PAGE){
            selectLast = false;
            setPage(getPage()+1);
        }else if(action == AToZTable.Action.PREVIOUS_PAGE){
            selectLast = false;
            setPage(getPage()-1);
        }else if(action == AppScreenForm.Action.REFRESH_PAGE){
            fireCommand(Action.GETPAGE,list);
        }else if(action == AToZTable.Action.ROW_SELECTED){
            select(((Integer)obj).intValue());
        }else if(action == FormInt.State.ADD){
            unselect();
        }
    }

    public void onBrowserEvent(Event event) {
        // TODO Auto-generated method stub
        
    }

}
