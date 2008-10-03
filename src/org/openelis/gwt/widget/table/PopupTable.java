package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesPopupEvents;

import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;
import org.openelis.gwt.widget.table.event.TableWidgetListener;

import java.util.ArrayList;

public class PopupTable extends TableWidget implements PopupListener, SourcesPopupEvents {
    
    
    public class DropPopup extends PopupPanel {
        
        public boolean showing;
        
        public DropPopup(boolean auto) {
            super(auto);
        }
        
        public void show() {
            super.show();
            showing = true;
        }
        
        public void hide() {
            super.hide();
            showing = false;
        }
        
        public void hide(boolean auto) {
            super.hide(auto);
            showing = false;
        }
        
        public boolean onKeyDownPreview(char key, int modifiers) {
            // TODO Auto-generated method stub
            if(isShowing()){
                keyboardHandler.onKeyDown(this, key, modifiers);      
                return true;
            }else
                return true;
        }
        
        @Override
        public boolean onKeyUpPreview(char key, int modifiers) {
            if(isShowing()){
                keyboardHandler.onKeyUp(this,key,modifiers);
                return true;
            }else
                return true;
        }
        
        public boolean isShowing() {
            return showing;
        }
    };
    
    public final DropPopup popup = new DropPopup(true);
    
    public PopupTable(){
        
    }
    
    public PopupTable(ArrayList<TableColumnInt> columns,int maxRows,String width, String title, boolean showHeader, VerticalScroll showScroll) {
        super();
        for(TableColumnInt column : columns) {
            column.setTableWidget(this);
        }
        this.columns = columns;
        this.maxRows = maxRows;
        this.title = title;
        this.showHeader = showHeader;
        renderer = new TableRenderer(this);
        model = new TableModel(this);
        view = new TableView(this, showScroll);
        view.setWidth(width);
        view.setHeight((maxRows*cellHeight+(maxRows*cellSpacing)+(maxRows*2)+cellSpacing));
        keyboardHandler = new TableKeyboardHandler(this);
        mouseHandler = new TableMouseHandler(this);
        addTableWidgetListener((TableWidgetListener)renderer);
        
        setWidget(view);
        
        popup.addStyleName("AutoCompletePopup");
        popup.setWidget(view);
        popup.addPopupListener(this);
    }
 
    public void showTable(final int active) {
        popup.setPopupPosition(this.getAbsoluteLeft(), this
                                      .getAbsoluteTop()
                                      + this.getOffsetHeight() - 1);
        popup.show();
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                if(view.header != null)
                   view.header.sizeHeader();
            }
        });
        focused = true;
        view.scrollBar.setScrollPosition((active*cellHeight));
        renderer.load(view.scrollBar.getScrollPosition());
        activeRow = -1;
        
        for(int i = 0; i < view.table.getRowCount(); i++){
            if(modelIndexList[i] == active)
                activeRow = i;
            }
        if(view.table.getRowCount() > 0)
            model.selectRow(active);
    }
    
    public void hideTable() {
        popup.hide();
    }

    public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
            
    }

    public void addPopupListener(PopupListener listener) {
        popup.addPopupListener(listener);
        
    }

    public void removePopupListener(PopupListener listener) {
        popup.removePopupListener(listener);
        
    }
    
}
