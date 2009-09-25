package org.openelis.gwt.screen;

import org.openelis.gwt.common.LastPageException;
import org.openelis.gwt.common.RPC;
import org.openelis.gwt.common.data.Query;
import org.openelis.gwt.widget.ResultsTable;
import org.openelis.gwt.widget.table.TableRow;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.Window;

public class ScreenNavigator<T extends Query<? extends RPC>> implements BeforeSelectionHandler<TableRow>, ClickHandler {
	
	public boolean selectItem = true;
	public boolean selectLast;
	public T query;
	public ResultsTable resultsTable;
	public int selection;
	private Screen screen;
	
	
	public ScreenNavigator(Screen screen) {
		this.screen = screen;
		if(screen.def.getWidgets().containsKey("azTable")){
			resultsTable = (ResultsTable)screen.def.getWidget("azTable");
			resultsTable.table.addBeforeSelectionHandler(this);
			resultsTable.addPageHandler(this);
		}
	}
	
	public void setQuery(T query) {
		this.query = query;
		if(resultsTable != null)
			resultsTable.setQuery(query);
		if(query.getResults().size() == 0)
			return;
		try {
            if (selectItem) {
                if (selectLast)
                    select(query.getModel().size() - 1);
                else
                    select(0);
            }else{
            	if(resultsTable != null)
            		resultsTable.unselect();
            }
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
    public void select(int selection) throws Exception {
        if (selection > query.getModel().size() - 1) {
            selectItem = true;
            setPage(query.page + 1);
        } else if (selection < 0) {
            selectItem = true;
            selectLast = true;
            setPage(query.page - 1);
        } else if(selection > -1 && selection < query.getModel().size()) {
        	getSelection(query.getResults().get(selection));
        	this.selection = selection; 
        	if(resultsTable != null)
        		resultsTable.select(selection);
        }
    }
    
    public void setPage(int page) {
        if(page < 0)
            return;
        int currPage = query.page;
        query.page = page;
        try{
        	screen.window.setBusy(screen.consts.get("querying"));
        	query = screen.service.call("query", query);
        	loadPage(query);
        	if(resultsTable != null)
        		resultsTable.setQuery(query);
            selectItem = true;
            selectLast = false;
        }catch(Throwable caught) {
            if(caught instanceof LastPageException){
                screen.window.setError(caught.getMessage());
            }else
                Window.alert(caught.getMessage());
            query.page = currPage;
        }                        
    }

	public void onBeforeSelection(BeforeSelectionEvent<TableRow> event) {
		try {
			select(event.getItem().index);	
		}catch(Exception e) {
			e.printStackTrace();
		}
		event.cancel();
	}
	
	public void next() {
		try {
			select(selection+1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void previous() {
		try {
			select(selection-1);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void onClick(ClickEvent event) {
		if(resultsTable != null){
			if(resultsTable.isAttached()){
				if(event.getSource() == resultsTable.nextPage){
					selectItem = false;
					setPage(query.page+1);
					return;
				}
				if(event.getSource() == resultsTable.prevPage){
					selectItem = false;
					setPage(query.page-1);
					return;
				}
			}
		}
	}
	
	public void loadPage(T query) {
		
	}
	
	public void getSelection(RPC rpc) {
		
	}
}
