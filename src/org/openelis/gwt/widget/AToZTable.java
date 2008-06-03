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
import org.openelis.gwt.common.data.DataModelWidget;
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
                                              ClickListener, ChangeListener {
    
    private HorizontalPanel mainHP = new HorizontalPanel();
    private ScreenVertical alphabetButtonVP = new ScreenVertical();
    private VerticalPanel tablePanel = new VerticalPanel();
    protected DataModel dm;
    protected DataModelWidget modelWidget;
    protected ButtonPanel bpanel;
    protected AppButton selectedButton;
    protected boolean locked;
    protected boolean refreshedByLetter;
    
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
            bpanel.addChangeListener(this);
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
                modelWidget.getModel().selecttLast(false);
                modelWidget.setPage(modelWidget.getPage()+1);
                refreshedByLetter = true;
                return;
            }
            if(!locked && sender == view.prevNav){
                modelWidget.getModel().selecttLast(false);
                modelWidget.setPage(modelWidget.getPage()-1);
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
        if(sender instanceof DataModelWidget){
            if(((DataModelWidget)sender).action == DataModelWidget.Action.REFRESH) {
                modelWidget = (DataModelWidget)sender;
                dm = ((DataModelWidget)sender).getModel();
                view.setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellSpacing)+cellSpacing);
                view.setNavPanel(dm.getPage(), dm.getPage()+1, false);
                scrollLoad(0);
               // DOM.addEventPreview(this);
                if(!refreshedByLetter){
                    if(selectedButton != null){
                        selectedButton.changeState(ButtonState.UNPRESSED);
                    }
                }else{
                    refreshedByLetter = false;
                }
                active = true;
            }
            if(((DataModelWidget)sender).action == DataModelWidget.Action.SELECTION){
                if(selectedRow > -1){
                    view.table.getRowFormatter().removeStyleName(selectedRow,TableView.selectedStyle);
                }
                selectedRow = modelWidget.getSelectedIndex() - start;
                view.table.getRowFormatter().addStyleName(selectedRow,TableView.selectedStyle);
                active = true;
            }
            return;
        }
        if(sender instanceof AppScreenForm) {
            State state = ((AppScreenForm)sender).state;
            if(bpanel != null){
                if(state == State.ADD){
                    bpanel.setPanelState(ButtonPanelState.LOCKED);
                    locked = true;
                    unselect(selectedRow);
                }else if(state == State.DELETE || state == State.QUERY) {
                    bpanel.setPanelState(ButtonPanelState.LOCKED);
                    locked = true;
                    unselect(selectedRow);
                }else if(state == State.UPDATE) {
                    bpanel.setPanelState(ButtonPanelState.LOCKED);
                    locked = true;
                }else if(state == State.DEFAULT || state == State.DISPLAY || state == State.BROWSE){
                    bpanel.setPanelState(ButtonPanelState.ENABLED);
                    locked = false;
                }
            }
            return;
        }
        if(sender == bpanel){
            if(selectedButton != null){
                selectedButton.changeState(ButtonState.UNPRESSED);
            }
            selectedButton = bpanel.buttonClicked;
            refreshedByLetter = true;
        }
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
    public void onCellClicked(SourcesTableEvents sender, int row, int col){
        if(!locked)
            active = true;
        if(selectedRow == row || locked){
            return;
        }
        if(selectedRow > -1){
            view.table.getRowFormatter().removeStyleName(selectedRow,TableView.selectedStyle);
        }
        selectedRow = row;
        view.table.getRowFormatter().addStyleName(selectedRow,TableView.selectedStyle);
        modelWidget.select(start+row);
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

}
