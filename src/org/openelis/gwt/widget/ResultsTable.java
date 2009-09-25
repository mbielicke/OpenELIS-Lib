package org.openelis.gwt.widget;

import org.openelis.gwt.common.data.Query;
import org.openelis.gwt.event.ActionEvent;
import org.openelis.gwt.event.ActionHandler;
import org.openelis.gwt.event.HasActionHandlers;
import org.openelis.gwt.widget.AppButton.ButtonState;
import org.openelis.gwt.widget.deprecated.ButtonPanel;
import org.openelis.gwt.widget.table.TableRow;
import org.openelis.gwt.widget.table.TableWidget;
import org.openelis.gwt.widget.table.event.BeforeCellEditedEvent;
import org.openelis.gwt.widget.table.event.BeforeCellEditedHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResultsTable extends Composite implements ClickHandler, HasActionHandlers<ResultsTable.Action>, BeforeSelectionHandler<TableRow>, BeforeCellEditedHandler {
    
    public TableWidget table;
    
    private HorizontalPanel mainHP = new HorizontalPanel();
    private VerticalPanel alphabetButtonVP = new VerticalPanel();
    private VerticalPanel tablePanel = new VerticalPanel();
    protected ButtonPanel bpanel;
    protected AppButton selectedButton;
    protected boolean refreshedByLetter;
    public enum Action {NEXT_PAGE,PREVIOUS_PAGE,ROW_SELECTED};
    public boolean showNavPanel = true;
    private ClickHandler navListener;
    public Widget nextPage;
    public Widget prevPage;
    

    
    public void select(int select) {
		table.unselect(-1);
		for(int i = 0; i < table.shownRows(); i++){
			if(table.modelIndexList[i] == select){
				table.activeRow = i;
			}
		}
        table.selectRow(select);
    }
    
    public void unselect() {
		table.unselect(-1);
    }
    
    public ResultsTable() {
        mainHP.setHeight("100%");
        mainHP.setSpacing(0);
        tablePanel.setSpacing(1);
        mainHP.add(alphabetButtonVP);
        mainHP.add(tablePanel);
        initWidget(mainHP);     
    }
    
    public void setQuery(Query query) {
        table.load(query.getModel());
        table.activeCell = -1;
        table.activeRow = -1;
        table.view.setScrollHeight((table.getData().size()*table.cellHeight)+(table.getData().size()*table.cellSpacing)+table.cellSpacing);
        if(showNavPanel){
            table.view.setNavPanel(query.page, query.page+1, false);
            table.view.prevNav.addClickHandler(navListener);
            table.view.nextNav.addClickHandler(navListener);
        	nextPage = table.view.nextNav;
        	prevPage = table.view.prevNav;
        }
        if(!refreshedByLetter){
            if(selectedButton != null){
                selectedButton.changeState(ButtonState.UNPRESSED);
            }
        }else
            refreshedByLetter = false;
        table.enable(true);
        
        table.focused = true;
    }
    
    public void setTable(TableWidget table) {
        this.table = table;
        tablePanel.add(table);
        table.addBeforeSelectionHandler(this);
        table.addBeforeCellEditedHandler(this);
    }

    public void setButtonGroup(ButtonGroup bg) {
        alphabetButtonVP.add(bg);
        
        bg.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                refreshedByLetter = true;
			}
        });
    }
    
    public void onClick(ClickEvent event) {
        if(table.view.table.isAttached()){
            if(event.getSource() == table.view.nextNav){
            	ActionEvent.fire(this, Action.NEXT_PAGE, table.getData());
                refreshedByLetter = true;
                return;
            }
            if(event.getSource() == table.view.prevNav){
                ActionEvent.fire(this,Action.PREVIOUS_PAGE,table.getData());
                refreshedByLetter = true;
                return;
            }
        }
    }
    
    public void addPageHandler(ClickHandler handler) {
    	navListener = handler;
    }

	public HandlerRegistration addActionHandler(ActionHandler<Action> handler) {
		return addHandler(handler,ActionEvent.getType());
	}

	public void onBeforeSelection(BeforeSelectionEvent<TableRow> event) {
		ActionEvent.fire(this,Action.ROW_SELECTED,event.getItem().index);
		event.cancel();
	}

	public void onBeforeCellEdited(BeforeCellEditedEvent event) {
		event.cancel();
	}

}
