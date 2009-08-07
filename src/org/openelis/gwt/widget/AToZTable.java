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
package org.openelis.gwt.widget;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.KeyListManager;
import org.openelis.gwt.common.data.TableDataModel;
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
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableKeyboardHandlerInt;
import org.openelis.gwt.widget.table.TableModel;
import org.openelis.gwt.widget.table.TableMouseHandlerInt;
import org.openelis.gwt.widget.table.TableRenderer;
import org.openelis.gwt.widget.table.TableView;
import org.openelis.gwt.widget.table.TableWidget;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;
import org.openelis.gwt.widget.table.event.TableWidgetListener;

import java.util.ArrayList;

@Deprecated
public class AToZTable extends TableWidget implements
                                              ClickListener, ChangeListener, CommandListener, TableKeyboardHandlerInt, TableMouseHandlerInt, SourcesCommandEvents {
    
    private HorizontalPanel mainHP = new HorizontalPanel();
    private ScreenVertical alphabetButtonVP = new ScreenVertical();
    private VerticalPanel tablePanel = new VerticalPanel();
    protected TableDataModel dm;
    protected ButtonPanel bpanel;
    protected AppButton selectedButton;
    protected boolean refreshedByLetter;
    private CommandListenerCollection commandListeners;
    public enum Action {NEXT_PAGE,PREVIOUS_PAGE,ROW_SELECTED};
    public ScreenAToZTable screenWidget;
    public boolean showNavPanel = true;
    public boolean locked;

    public AToZTable() {
        super();
    }
    
    public AToZTable(ArrayList<TableColumnInt> columns, int maxRows, String width, String title, boolean showHeader, VerticalScroll showScroll) {
        super();
        init(columns,maxRows,width,title,showHeader,showScroll);
    }
    
    public void init(ArrayList<TableColumnInt> columns, int maxRows, String width, String title, boolean showHeader, VerticalScroll showScroll) {
        for(TableColumnInt column : columns) {
            column.setTableWidget(this);
        }
        this.columns = columns;
        this.maxRows = maxRows;
        this.title = title;
        this.showHeader = showHeader;
        renderer = new TableRenderer((TableWidget)this);
        model = new TableModel(this);
        view = new TableView(this,showScroll);
        view.setWidth("auto");
        view.setHeight((maxRows*cellHeight+(maxRows*cellSpacing)+(maxRows*2)+cellSpacing));
        keyboardHandler = this;
        mouseHandler = this;
        addTableWidgetListener((TableWidgetListener)renderer);
        mainHP.setHeight("100%");
        tablePanel.add(view);
        mainHP.setSpacing(0);
        tablePanel.setSpacing(1);
        mainHP.add(alphabetButtonVP);
        mainHP.add(tablePanel);
        setWidget(mainHP);     
    }
    
    public void setButtonPanel(Widget wid) {
        alphabetButtonVP.add(wid);
        if(wid instanceof ScreenButtonPanel){
            bpanel = (ButtonPanel)((ScreenWidget)wid).getWidget();
            bpanel.addCommandListener(this);
        }
    }
    
    public void onClick(Widget sender) {
        if(view.table.isAttached()){
            if(sender instanceof AppScreen){
                if(focused && !DOM.isOrHasChild(view.getElement(), ((AppScreen)sender).clickTarget)){
                    focused = false;
                }
                return;
            }
            if(sender == view.nextNav){
                commandListeners.fireCommand(Action.NEXT_PAGE,dm);
                refreshedByLetter = true;
                return;
            }
            if(sender == view.prevNav){
                commandListeners.fireCommand(Action.PREVIOUS_PAGE,dm);
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
                        if(view.header != null)
                            view.header.sizeHeader();
                    }
                });
            }
        }
    }
    
    public void setTableWidth(String width) {
        view.setWidth(width);
    }
    public void onCellClicked(SourcesTableEvents sender, final int row, int col){
        if(locked)
            return;
        focused = true;
        if(activeRow == row){
            super.onCellClicked(sender, row, col);
            return;
        }
        if(model.canSelect(modelIndexList[row])){
            commandListeners.fireCommand(Action.ROW_SELECTED,new Integer(modelIndexList[row]));
            super.onCellClicked(sender, row, col);
        }
    }

    public void onKeyDown(Widget sender, char code, int modifiers) {
        if(!focused)
            return;
        boolean shift = modifiers == KeyboardListener.KEY_SHIFT;
        if (KeyboardListener.KEY_DOWN == code) {
            if (activeRow >= 0 && activeRow < view.table.getRowCount() - 1) {
                if(activeRow < view.table.getRowCount() -1){
                    final int row = activeRow + 1;
                    final int col = activeCell;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            onCellClicked(view.table, row, col);
                        }
                    });
                }else{
                    view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
                    final int col = activeCell;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            onCellClicked(view.table, maxRows-1, col);
                        }
                    });
                }
            }
        }
        if (KeyboardListener.KEY_UP == code) {
            if (activeRow >= 0 && activeRow != 0) {
                final int row = activeRow - 1;
                final int col = activeCell;
                //unselect(selected);
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        onCellClicked(view.table, row, col);
                    }
                });
            }else{
                view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()-cellHeight);
                final int col = activeCell;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        onCellClicked(view.table, 0, col);
                    }
                });
            }
        }
        
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }
    
    public boolean canPerformCommand(Enum action, Object obj) {
        return (action == AppScreenForm.Action.NEW_MODEL) ||
               (action == AppScreenForm.Action.NEW_PAGE) ||
               (action == KeyListManager.Action.SELECTION) ||                
               (action == KeyListManager.Action.UNSELECT) ||
               (action.getDeclaringClass() == AppScreenForm.State.class) ||
               (obj instanceof AppButton && bpanel != null && DOM.isOrHasChild(bpanel.getElement(), ((AppButton)obj).getElement()));
    }

    public void performCommand(Enum action, Object obj) {
        if(action == AppScreenForm.Action.NEW_MODEL) {
            model.load((TableDataModel)obj);
            view.setScrollHeight((model.getData().size()*cellHeight)+(model.getData().size()*cellSpacing)+cellSpacing);
            if(showNavPanel)
                view.setNavPanel(model.getData().getPage(), model.getData().getPage()+1, false);
            //model.refresh();
           // DOM.addEventPreview(this);
            if(!refreshedByLetter){
                if(selectedButton != null){
                    selectedButton.changeState(ButtonState.UNPRESSED);
                }
            }else
                refreshedByLetter = false;
            enabled(true);
            focused = true;
        }
        else if(action == AppScreenForm.Action.NEW_PAGE){
            model.load((TableDataModel)obj);
            view.setScrollHeight((model.getData().size()*cellHeight)+(model.getData().size()*cellSpacing)+cellSpacing);
            if(showNavPanel)
                view.setNavPanel(model.getData().getPage(), model.getData().getPage()+1, false);
            model.refresh();
            focused = true;
            enabled(true);
        }
        else if(action == KeyListManager.Action.SELECTION){   
            
            if(activeRow > -1){
                model.unselectRow(activeRow);
                //if(activeRow < view.table.getRowCount())
                  //  view.table.getRowFormatter().removeStyleName(activeRow,TableView.selectedStyle);
            }
            
            int select = ((Integer)obj).intValue();
            for(int i = 0; i < model.shownRows(); i++){
                if(modelIndexList[i] == select){
                    activeRow = i;
                }
            }
            model.selectRow(modelIndexList[activeRow]);
            //view.table.getRowFormatter().addStyleName(activeRow,TableView.selectedStyle);
            //focused = true;
        }
        else if(action == KeyListManager.Action.UNSELECT){
            if(activeRow > -1)
                model.unselectRow(-1);
            /*if(activeRow > -1){
                if(activeRow < view.table.getRowCount())
                    view.table.getRowFormatter().removeStyleName(activeRow,TableView.selectedStyle);
                activeRow = -1;
            }
            */
        }
        if(obj != null && obj instanceof AppButton && DOM.isOrHasChild(bpanel.getElement(), ((AppButton)obj).getElement())){
            if(selectedButton != null){
                selectedButton.changeState(ButtonState.UNPRESSED);
            }
            selectedButton = (AppButton)obj;
            ((AppButton)obj).changeState(ButtonState.PRESSED);
            refreshedByLetter = true;
        
           
        }
        
        if(action == AppScreenForm.State.UPDATE)
            locked = true;
        else if(action.getDeclaringClass() == AppScreenForm. State.class)
            locked = false;
        
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
    
    public void setMaxRows(int rows){
        maxRows = rows;
        view.setHeight((rows*cellHeight+(rows*cellSpacing)+cellSpacing)); 
    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseLeave(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

	public void onMouseOver(MouseOverEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onMouseOut(MouseOutEvent event) {
		// TODO Auto-generated method stub
		
	}

}
