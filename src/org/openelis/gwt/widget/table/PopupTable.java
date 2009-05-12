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
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesPopupEvents;
import com.google.gwt.user.client.ui.Widget;

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
        if(model.numRows() < maxRows){
            view.setHeight((model.numRows()*cellHeight+(model.numRows()*cellSpacing)+(model.numRows()*2)+cellSpacing));
        }
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
    
    public void showTable(final int active,Widget wid) {
        popup.setPopupPosition(wid.getAbsoluteLeft(), wid
                                      .getAbsoluteTop()
                                      + wid.getOffsetHeight() - 1);
        popup.show();
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                if(view.header != null)
                   view.header.sizeHeader();
            }
        });
        focused = true;
        if(model.numRows() < maxRows){
            view.setHeight((model.numRows()*cellHeight+(model.numRows()*cellSpacing)+(model.numRows()*2)+cellSpacing));
        }
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
