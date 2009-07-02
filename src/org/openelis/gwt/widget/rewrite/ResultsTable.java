package org.openelis.gwt.widget.rewrite;

import java.util.ArrayList;

import org.openelis.gwt.common.rewrite.Query;
import org.openelis.gwt.event.ActionEvent;
import org.openelis.gwt.event.ActionHandler;
import org.openelis.gwt.event.HasActionHandlers;
import org.openelis.gwt.screen.rewrite.Screen;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.rewrite.AppButton.ButtonState;
import org.openelis.gwt.widget.table.rewrite.TableDataRow;
import org.openelis.gwt.widget.table.rewrite.TableManager;
import org.openelis.gwt.widget.table.rewrite.TableWidget;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResultsTable extends Composite implements ClickListener, HasActionHandlers<ResultsTable.Action>, ChangeListener, TableManager{
    
    public TableWidget table;
    
    private HorizontalPanel mainHP = new HorizontalPanel();
    private VerticalPanel alphabetButtonVP = new VerticalPanel();
    private VerticalPanel tablePanel = new VerticalPanel();
    protected ButtonPanel bpanel;
    protected AppButton selectedButton;
    protected boolean refreshedByLetter;
    public enum Action {NEXT_PAGE,PREVIOUS_PAGE,ROW_SELECTED};
    public boolean showNavPanel = true;
    private ClickListener navListener = this;
    
    public ActionHandler<Screen.Action> screenActions = new ActionHandler<Screen.Action>() {

		public void onAction(ActionEvent<Screen.Action> event) {
	        if(event.getAction() == Screen.Action.NEW_MODEL) {
	            table.load(((Query)event.getData()).model);
	            table.activeCell = -1;
	            table.activeRow = -1;
	            table.view.setScrollHeight((table.getData().size()*table.cellHeight)+(table.getData().size()*table.cellSpacing)+table.cellSpacing);
	            if(showNavPanel){
	                table.view.setNavPanel(((Query)event.getData()).page, ((Query)event.getData()).page+1, false);
	                table.view.prevNav.addClickListener(navListener);
	                table.view.nextNav.addClickListener(navListener);
	            }
	            if(!refreshedByLetter){
	                if(selectedButton != null){
	                    selectedButton.changeState(ButtonState.UNPRESSED);
	                }
	            }else
	                refreshedByLetter = false;
	            table.enabled(true);
	            table.focused = true;
	        }
	        else if(event.getAction() == Screen.Action.NEW_PAGE){
	            table.load((ArrayList<TableDataRow>)event.getData());
	            table.view.setScrollHeight((table.getData().size()*table.cellHeight)+(table.getData().size()*table.cellSpacing)+table.cellSpacing);
	            table.activeCell = -1;
	            table.activeRow = -1;
	            if(showNavPanel){
	                table.view.setNavPanel(((Query)event.getData()).page, ((Query)event.getData()).page+1, false);
	                table.view.prevNav.addClickListener(navListener);
	                table.view.nextNav.addClickListener(navListener);
	            }
	            table.refresh();
	            table.focused = true;
	            table.enabled(true);
	        }
		}
    };
    
    public ActionHandler<KeyListManager.Action> keyListActions = new ActionHandler<KeyListManager.Action>() {

		public void onAction(ActionEvent<KeyListManager.Action> event) {
			 if(event.getAction() == KeyListManager.Action.SELECTION){  
		            if(table.activeRow > -1){
		                table.unselect(table.activeRow);
		            }
		            
		            int select = ((Integer)event.getData()).intValue();
		            for(int i = 0; i < table.shownRows(); i++){
		                if(table.modelIndexList[i] == select){
		                    table.activeRow = i;
		                }
		            }
		            table.selectRow(table.modelIndexList[table.activeRow]);
		        }
		        else if(event.getAction() == KeyListManager.Action.UNSELECT){
		            if(table.activeRow > -1)
		                table.unselect(-1);
		        }
		}
    	
    };
    
    public ResultsTable() {
        mainHP.setHeight("100%");
        mainHP.setSpacing(0);
        tablePanel.setSpacing(1);
        mainHP.add(alphabetButtonVP);
        mainHP.add(tablePanel);
        initWidget(mainHP);     
    }
    
    public void setTable(TableWidget table) {
        this.table = table;
        if(table.getManager() == null)
            table.setManager(this);
        tablePanel.add(table);
    }

    public void setButtonPanel(Widget wid) {
        alphabetButtonVP.add(wid);
        if(wid instanceof ButtonPanel){
            bpanel = (ButtonPanel)wid;
            bpanel.addActionHandler(new ActionHandler<ButtonPanel.Action>() {
				public void onAction(ActionEvent<ButtonPanel.Action> event) {
		            if(selectedButton != null){
		                selectedButton.changeState(ButtonState.UNPRESSED);
		            }
		            selectedButton = (AppButton)event.getData();
		            ((AppButton)event.getData()).changeState(ButtonState.PRESSED);
		            refreshedByLetter = true;
				}
            	
            });
        }
    }
    
    public void onClick(Widget sender) {
        if(table.view.table.isAttached()){
            if(sender == table.view.nextNav){
            	ActionEvent.fire(this, Action.NEXT_PAGE, table.getData());
                refreshedByLetter = true;
                return;
            }
            if(sender == table.view.prevNav){
                ActionEvent.fire(this,Action.PREVIOUS_PAGE,table.getData());
                refreshedByLetter = true;
                return;
            }
        }
    }
    
    public void onChange(Widget sender) {

        if(sender instanceof CollapsePanel) {
            if(((CollapsePanel)sender).isOpen){
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        if(table.view.header != null)
                            table.view.header.sizeHeader();
                    }
                });
            }
        }
    }

    public boolean canAdd(TableWidget widget, TableDataRow set, int row) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canAutoAdd(TableWidget widget, TableDataRow addRow) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canDelete(TableWidget widget, TableDataRow set, int row) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canEdit(TableWidget widget, TableDataRow set, int row, int col) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canSelect(TableWidget widget, TableDataRow set, int row) {
        ActionEvent.fire(this,Action.ROW_SELECTED,new Integer(row));
        return false;
    }

	public HandlerRegistration addActionHandler(ActionHandler<Action> handler) {
		return addHandler(handler,ActionEvent.getType());
	}

}
