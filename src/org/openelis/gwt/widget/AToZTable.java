/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.widget;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.KeyListManager;
import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;
import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.screen.AppScreenForm;
import org.openelis.gwt.screen.ClassFactory;
import org.openelis.gwt.screen.ScreenButtonPanel;
import org.openelis.gwt.screen.ScreenLabel;
import org.openelis.gwt.screen.ScreenVertical;
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.widget.AppButton.ButtonState;
import org.openelis.gwt.widget.ButtonPanel.ButtonPanelState;
import org.openelis.gwt.widget.FormInt.State;
import org.openelis.gwt.widget.table.TableController;
import org.openelis.gwt.widget.table.TableView;

public class AToZTable extends TableController implements
                                              ClickListener, ChangeListener, CommandListener, SourcesCommandEvents {
    
    private HorizontalPanel mainHP = new HorizontalPanel();
    private ScreenVertical alphabetButtonVP = new ScreenVertical();
    private VerticalPanel tablePanel = new VerticalPanel();
    protected DataModel dm;
    protected ButtonPanel bpanel;
    protected AppButton selectedButton;
    protected boolean locked;
    protected boolean refreshedByLetter;
    private CommandListenerCollection commandListeners;
    public enum Action {NEXT_PAGE,PREVIOUS_PAGE,ROW_SELECTED};
    
    public AToZTable() { 
        mainHP.setHeight("100%");
        view = new TableView();
        view.setTableListener(this);
        tablePanel.add(view);
        mainHP.setSpacing(0);
        tablePanel.setSpacing(1);
        mainHP.add(alphabetButtonVP);
        mainHP.add(tablePanel);
        initWidget(mainHP);     
    }
    
    public void setButtonPanel(Widget wid) {
        alphabetButtonVP.add(wid);
        if(wid instanceof ScreenButtonPanel){
            bpanel = (ButtonPanel)((ScreenWidget)wid).getWidget();
            bpanel.addCommandListener(this);
        }
    }
    
    @Override
    public void onClick(Widget sender) {
        if(view.table.isAttached()){
            if(sender instanceof AppScreen){
                if(active && !DOM.isOrHasChild(view.getElement(), ((AppScreen)sender).clickTarget)){
                    active = false;
                }
                return;
            }
            if(!locked && sender == view.nextNav){
                commandListeners.fireCommand(Action.NEXT_PAGE,dm);
                refreshedByLetter = true;
                return;
            }
            if(!locked && sender == view.prevNav){
                commandListeners.fireCommand(Action.PREVIOUS_PAGE,dm);
                refreshedByLetter = true;
                return;
            }
        }
    }

    @Override
    public void scrollLoad(int scrollPos) {
        try{
            int rowsPer = maxRows;
            if(maxRows > dm.size())
                rowsPer = dm.size();
            start = (scrollPos)/(cellHeight);
            if(start+rowsPer > dm.size())
                start = start - ((start+rowsPer) - dm.size());
            if(view.table.getRowCount() < rowsPer){
                for(int i = view.table.getRowCount(); i < rowsPer; i++){
                    createRow(i);
                }
            }else if(view.table.getRowCount() > rowsPer){
                for(int i = view.table.getRowCount() -1; i >= rowsPer; i--)
                    view.table.removeRow(i);
            }
            for(int i = 0; i < rowsPer; i++){
                loadRow(i);
            }
        }catch(Exception e){
            Window.alert("scrollLoad "+e.getMessage());
        }

    }
    private void loadRow(int index){
        for(int i = 0; i < curColWidth.length; i++){
            ScreenLabel label = (ScreenLabel)view.table.getWidget(index,i);
            label.label.setText(dm.get(start+index).getObject(i).getValue().toString());
            label.setUserObject(dm.get(start+index).getKey().getValue());
        }
        view.table.getRowFormatter().removeStyleName(index, TableView.selectedStyle);
    }
    
    private void createRow(int index){
        for(int i = 0; i < curColWidth.length; i++){
            ScreenLabel label = new ScreenLabel("   ",null);
            view.table.setWidget(index, i, label);
            label.label.setWordWrap(false);
            DOM.setStyleAttribute(label.getElement(), "overflowX", "hidden");
            label.addMouseListener((MouseListener)ClassFactory.forName("HoverListener"));
            label.setWidth(curColWidth[i]+"px");
            view.table.getFlexCellFormatter().setWidth(index, i, curColWidth[i] + "px");
            view.table.getFlexCellFormatter().setHeight(index, i, cellHeight+"px");
            view.table.getFlexCellFormatter().addStyleName(index,
                                                           i,
                                                           TableView.cellStyle);
        }
        view.table.getRowFormatter().addStyleName(index, TableView.rowStyle);
        if(index % 2 == 1){
            DOM.setStyleAttribute(view.table.getRowFormatter().getElement(index), "background", "#f8f8f9");
        }
        if(showRows){
            Label rowNum = new Label(String.valueOf(index+1));
            view.rows.setWidget(index,0,rowNum);
            view.rows.getFlexCellFormatter().setStyleName(index, 0, "RowNum");
            view.rows.getFlexCellFormatter().setHeight(index,0,cellHeight+"px");
        }
    }    

    public void onChange(Widget sender) {

        if(sender instanceof CollapsePanel) {
            if(((CollapsePanel)sender).isOpen){
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        sizeTable();
                    }
                });
            }
        }
    }
    
    public void setTableWidth(String width) {
        view.setWidth(width);
    }
    public void onCellClicked(SourcesTableEvents sender, final int row, int col){
        if(!locked)
            active = true;
        if(selectedRow == row || locked){
            return;
        }
        commandListeners.fireCommand(Action.ROW_SELECTED,new Integer(start+row));
    }

    public void onKeyDown(Widget sender, char code, int modifiers) {
        if(!active)
            return;
        boolean shift = modifiers == KeyboardListener.KEY_SHIFT;
        if (KeyboardListener.KEY_DOWN == code) {
            if (selectedRow >= 0 && selectedRow < view.table.getRowCount() - 1) {
                if(selectedRow < view.table.getRowCount() -1){
                    final int row = selectedRow + 1;
                    final int col = selectedCell;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            onCellClicked(view.table, row, col);
                        }
                    });
                }else{
                    view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
                    final int col = selectedCell;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            onCellClicked(view.table, maxRows-1, col);
                        }
                    });
                }
            }
        }
        if (KeyboardListener.KEY_UP == code) {
            if (selectedRow >= 0 && selectedRow != 0) {
                final int row = selectedRow - 1;
                final int col = selectedCell;
                //unselect(selected);
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        onCellClicked(view.table, row, col);
                    }
                });
            }else{
                view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()-cellHeight);
                final int col = selectedCell;
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
               (obj instanceof AppButton && DOM.isOrHasChild(bpanel.getElement(), ((AppButton)obj).getElement()));
    }

    public void performCommand(Enum action, Object obj) {
        if(action == AppScreenForm.Action.NEW_MODEL) {
            dm = (DataModel)obj;
            view.setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellSpacing)+cellSpacing);
            view.setNavPanel(dm.getPage(), dm.getPage()+1, false);
            scrollLoad(0);
           // DOM.addEventPreview(this);
            if(!refreshedByLetter){
                if(selectedButton != null){
                    selectedButton.changeState(ButtonState.UNPRESSED);
                }
            }else
                refreshedByLetter = false;
            active = true;
        }
        else if(action == AppScreenForm.Action.NEW_PAGE){
            dm = (DataModel)obj;
            view.setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellSpacing)+cellSpacing);
            view.setNavPanel(dm.getPage(), dm.getPage()+1, false);
            scrollLoad(0);
            active = true;
        }
        else if(action == KeyListManager.Action.SELECTION){                
            if(selectedRow > -1){
                if(selectedRow < view.table.getRowCount())
                    view.table.getRowFormatter().removeStyleName(selectedRow,TableView.selectedStyle);
            }
            selectedRow = ((Integer)obj).intValue() - start;
            view.table.getRowFormatter().addStyleName(selectedRow,TableView.selectedStyle);
            active = true;
        }
        else if(action == KeyListManager.Action.UNSELECT){
            if(selectedRow > -1){
                if(selectedRow < view.table.getRowCount())
                    view.table.getRowFormatter().removeStyleName(selectedRow,TableView.selectedStyle);
                selectedRow = -1;
            }
        }
        /*
        else if(action.getDeclaringClass() == State.class) {
            if(bpanel != null){
                if(action == State.ADD){
                    bpanel.setPanelState(ButtonPanelState.LOCKED);
                    locked = true;
                    unselect(selectedRow);
                }else if(action == State.DELETE || action == State.QUERY) {
                    bpanel.setPanelState(ButtonPanelState.LOCKED);
                    locked = true;
                    unselect(selectedRow);
                }else if(action == State.UPDATE) {
                    bpanel.setPanelState(ButtonPanelState.LOCKED);
                    locked = true;
                }else if(action == State.DEFAULT || action == State.DISPLAY || action == State.BROWSE){
                    //bpanel.setPanelState(ButtonPanelState.ENABLED);
                    //locked = false;
                }
            }
            return;
        }
        */
        if(obj != null && obj instanceof AppButton && DOM.isOrHasChild(bpanel.getElement(), ((AppButton)obj).getElement())){
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
    
    public void setMaxRows(int rows){
        maxRows = rows;
        view.setHeight((rows*cellHeight+(rows*cellSpacing)+cellSpacing)); 
    }

}
