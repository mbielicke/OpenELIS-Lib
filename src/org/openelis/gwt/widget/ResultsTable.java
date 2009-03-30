package org.openelis.gwt.widget;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.Query;
import org.openelis.gwt.common.data.KeyListManager;
import org.openelis.gwt.common.data.TableDataModel;
import org.openelis.gwt.common.data.TableDataRow;
import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;
import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.screen.AppScreenForm;
import org.openelis.gwt.screen.ScreenAToZTable;
import org.openelis.gwt.screen.ScreenButtonPanel;
import org.openelis.gwt.screen.ScreenVertical;
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.widget.AppButton.ButtonState;
import org.openelis.gwt.widget.table.TableManager;
import org.openelis.gwt.widget.table.TableModel;
import org.openelis.gwt.widget.table.TableModelInt;
import org.openelis.gwt.widget.table.TableWidget;

public class ResultsTable extends Composite implements ClickListener, CommandListener, SourcesCommandEvents, ChangeListener, TableManager{
    
    private CommandListenerCollection commandListeners;
    public TableWidget table;
    
    private HorizontalPanel mainHP = new HorizontalPanel();
    private ScreenVertical alphabetButtonVP = new ScreenVertical();
    private VerticalPanel tablePanel = new VerticalPanel();
    protected ButtonPanel bpanel;
    protected AppButton selectedButton;
    protected boolean refreshedByLetter;
    public enum Action {NEXT_PAGE,PREVIOUS_PAGE,ROW_SELECTED};
    public ScreenAToZTable screenWidget;
    public boolean showNavPanel = true;
    public TableModelInt model;
    
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
        if(((TableModel)table.model).getManager() == null)
            table.model.setManager(this);
        table.addClickListener(this);
        model = table.model;
        tablePanel.add(table);
    }

    public void setButtonPanel(Widget wid) {
        alphabetButtonVP.add(wid);
        if(wid instanceof ScreenButtonPanel){
            bpanel = (ButtonPanel)((ScreenWidget)wid).getWidget();
            bpanel.addCommandListener(this);
        }
    }
    
    public void onClick(Widget sender) {
        if(table.view.table.isAttached()){
            if(sender instanceof AppScreen){
                if(table.focused && !DOM.isOrHasChild(table.view.getElement(), ((AppScreen)sender).clickTarget)){
                    table.focused = false;
                }
                return;
            }
            if(sender == table.view.nextNav){
                commandListeners.fireCommand(Action.NEXT_PAGE,table.model.getData());
                refreshedByLetter = true;
                return;
            }
            if(sender == table.view.prevNav){
                commandListeners.fireCommand(Action.PREVIOUS_PAGE,table.model.getData());
                refreshedByLetter = true;
                return;
            }
        }
    }
    
    public void performCommand(Enum action, Object obj) {
        if(action == AppScreenForm.Action.NEW_MODEL) {
            table.model.load(((Query)obj).results);
            table.activeCell = -1;
            table.activeRow = -1;
            table.view.setScrollHeight((table.model.getData().size()*table.cellHeight)+(table.model.getData().size()*table.cellSpacing)+table.cellSpacing);
            if(showNavPanel){
                table.view.setNavPanel(((Query)obj).page, ((Query)obj).page+1, false);
                table.view.prevNav.addClickListener(this);
                table.view.nextNav.addClickListener(this);
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
        else if(action == AppScreenForm.Action.NEW_PAGE){
            table.model.load(((Query)obj).results);
            table.view.setScrollHeight((table.model.getData().size()*table.cellHeight)+(table.model.getData().size()*table.cellSpacing)+table.cellSpacing);
            table.activeCell = -1;
            table.activeRow = -1;
            if(showNavPanel){
                table.view.setNavPanel(((Query)obj).page, ((Query)obj).page+1, false);
                table.view.prevNav.addClickListener(this);
                table.view.nextNav.addClickListener(this);
            }
            table.model.refresh();
            table.focused = true;
            table.enabled(true);
        }
        else if(action == KeyListManager.Action.SELECTION){   
            
            if(table.activeRow > -1){
                table.model.unselectRow(table.activeRow);
            }
            
            int select = ((Integer)obj).intValue();
            for(int i = 0; i < table.model.shownRows(); i++){
                if(table.modelIndexList[i] == select){
                    table.activeRow = i;
                }
            }
            table.model.selectRow(table.modelIndexList[table.activeRow]);
        }
        else if(action == KeyListManager.Action.UNSELECT){
            if(table.activeRow > -1)
                table.model.unselectRow(-1);
        }
        if(obj != null && obj instanceof AppButton && bpanel != null && DOM.isOrHasChild(bpanel.getElement(), ((AppButton)obj).getElement())){
            if(selectedButton != null){
                selectedButton.changeState(ButtonState.UNPRESSED);
            }
            selectedButton = (AppButton)obj;
            ((AppButton)obj).changeState(ButtonState.PRESSED);
            refreshedByLetter = true;
        
           
        }
        
    }

    public void addCommandListener(CommandListener listener) {
       if(commandListeners == null){
           commandListeners = new CommandListenerCollection();
       }
       commandListeners.add(listener);
    }

    public void removeCommandListener(CommandListener listener) {
        if(commandListeners != null)
            commandListeners.remove(listener);
        
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
    
    public boolean canPerformCommand(Enum action, Object obj) {
        return true;
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
        commandListeners.fireCommand(Action.ROW_SELECTED,new Integer(row));
        return false;
    }

}
