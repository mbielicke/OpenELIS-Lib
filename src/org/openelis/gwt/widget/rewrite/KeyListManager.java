/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.widget.rewrite;

import org.openelis.gwt.common.rewrite.Query;
import org.openelis.gwt.event.ActionEvent;
import org.openelis.gwt.event.ActionHandler;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.HasActionHandlers;
import org.openelis.gwt.screen.rewrite.Screen;
import org.openelis.gwt.widget.rewrite.ButtonPanel;
import org.openelis.gwt.widget.table.rewrite.TableDataRow;
import org.openelis.gwt.widget.HandlesEvents;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * KeyListManager is used by extending instances of AppScreenForm to manage primary key results from 
 * a query.  Navigational commands on the screen such as next, previous or clicking on a row in the 
 * AtoZ table will consult the KeyListManager for the correct key to fetch next.
 * @author tschmidt
 *
 */
public class KeyListManager<T extends Query<? extends Object>> extends HandlesEvents implements HasActionHandlers<KeyListManager.Action> {
	
	final KeyListManager keyManager = this;
	
	public ActionHandler<Screen.Action> screenActions = new ActionHandler<Screen.Action>() {
		 
		public void onAction(ActionEvent<Screen.Action> event) {
			if (event.getAction() == Screen.Action.NEW_PAGE) {
				 //setModel((DataModel)obj); if(selectItem){ if(selectLast)
				 //select(getList().size()-1); else select(0); } selectItem = false;
				 //selectLast = false;
			} else if (event.getAction() == Screen.Action.NEW_MODEL) {
				setQuery((T)event.getData());
				select(0);	
			} else if (event.getAction() == Screen.Action.REFRESH_PAGE) {
				setPage(getPage());
				ActionEvent.fire(keyManager,Action.GETPAGE,query);
			}
		}
	};
	
	public ActionHandler<ButtonPanel.Action> buttonActions = new ActionHandler<ButtonPanel.Action>() {

		public void onAction(ActionEvent<ButtonPanel.Action> event) {
	        if (event.getAction() == ButtonPanel.Action.NEXT) {
	            next();
	        } else if (event.getAction() == ButtonPanel.Action.PREVIOUS) {
	            previous();
	        }
		}
		
	};
	
	public ActionHandler<ResultsTable.Action> resultsActions = new ActionHandler<ResultsTable.Action>() {

		public void onAction(ActionEvent<ResultsTable.Action> event) {
			if (event.getAction() == ResultsTable.Action.NEXT_PAGE) {
	            selectLast = false;
	            setPage(getPage() + 1);
	        } else if (event.getAction() == ResultsTable.Action.PREVIOUS_PAGE) {
	            selectLast = false;
	            setPage(getPage() - 1);
	        } else if (event.getAction() == ResultsTable.Action.ROW_SELECTED){
	            select(((Integer)event.getData()).intValue());
	        }
			
		}
		
	};
	
	public enum Action {
        SELECTION, REFRESH, GETPAGE, ADD, DELETE, FETCH, UNSELECT
    }

    public Action action;

    private CommandListenerCollection commandListeners;

    /**
     * The model to store the list of keys from a query
     */
    //private TableDataModel<TableDataRow<Key>> list = new TableDataModel<TableDataRow<Key>>();
     private T query;
    /**
     * The current index for the key being attempted to fetch.
     * If fetch is successful then the candidate will become the 
     * cursor in the list.
     */
    private int candidate = 0;
    private int selected = -1;

    private boolean selectItem;
    /**
     * Flag used to tell the KeyListManager to select the last item in
     * the list.  Used when navigating with previous.
     */
    private boolean selectLast;

    
    public void setQuery(T query) {
        this.query = query;
        candidate = 0;
    }

    /**
     * Increments the index in the list by one
     *
     */
    public void next() {
        select(++candidate);
    }
    
    /**
     * Decrements the index in the list by one
     *
     */
    public void previous() {
        select(--candidate);
    }

    /**
     * Selects the passed index in the list of keys and broadcasts the Fetch command.
     * If the callback comes back successfully then the candidate key becomes the selected
     * index in the list and the Selection command is fired.  if not then the candidate key 
     * is rolled back.
     * @param selection
     * @throws IndexOutOfBoundsException
     */
    public void select(final int selection) throws IndexOutOfBoundsException {
        if (selection > query.model.size() - 1) {
            selectItem = true;
            setPage(query.page + 1);
        } else if (selection < 0) {
            // if(list.getPage() > 0){
            selectItem = true;
            selectLast = true;
            setPage(query.page - 1);
            // }else
            // candidate = 0;
        } else if(selection > -1 && selection < query.model.size()) {
            AsyncCallback callback = new AsyncCallback() {
                public void onSuccess(Object result) {
                    //query.results.select(selection);
                    candidate = selection;
                    selected = selection;
                    ActionEvent.fire(keyManager,Action.SELECTION, new Integer(selection));
                }

                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());

                }

            };
            ActionEvent.fire(this,Action.FETCH, new Object[] {query.model.get(selection).key, callback});
        }
    }

    /**
     * Adds a key to the list that is being managed.
     * @param set
     */
    public void add(TableDataRow row) {
        query.model.add(row);
        ActionEvent.fire(this,Action.ADD, null);
    }

    /**
     * Returns the currently selected key in the list.
     * @return
     */
    public Object getSelected() {
        return query.model.get(selected).key;
    }

    /**
     * Removes the key from t
     * @param index
     */
    public void delete(int index) {
        query.model.remove(index);
        ActionEvent.fire(this,Action.DELETE, new Integer(index));
    }

    public int getSelectedIndex() {
        return selected;
    }

    public int getPage() {
        return query.page;
    }

    public void setPage(int page) {
        if(page < 0)
            return;
        final int currPage = query.page;
        query.page = page;
        AsyncCallback callback = new AsyncCallback<T>() {
            public void onSuccess(T result) {
                setQuery(result);
                if (selectItem) {
                    if (selectLast)
                        select(result.model.size() - 1);
                    else
                        select(0);
                }
                selectItem = false;
                selectLast = false;

            }

            public void onFailure(Throwable caught) {
                query.page = currPage;
                candidate = selected;
            }

        };
        ActionEvent.fire(this,Action.GETPAGE, new Object[] {query, callback});
    }

    public void unselect() {
        if(query != null && query.results != null)
            selected = -1;
        ActionEvent.fire(this,Action.UNSELECT, null);
    }

	public HandlerRegistration addActionHandler(ActionHandler<Action> handler) {
		return addHandler(handler,ActionEvent.getType());
	}

}
