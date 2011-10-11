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

import java.util.ArrayList;

import org.openelis.gwt.widget.table.TableView.VerticalScroll;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupTable extends TableWidget implements CloseHandler<PopupPanel> , HasCloseHandlers<PopupPanel> {
    
    protected boolean showingOptions;
	
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
        
        public boolean isShowing() {
            return showing;
        }
    };
    
    public final DropPopup popup = new DropPopup(true);
    
    public PopupTable(){
    
    }
    
    public PopupTable(ArrayList<TableColumn> columns,int maxRows,String width, String title, boolean showHeader, VerticalScroll showScroll) {
        super();
        for(TableColumn column : columns) {
            column.setTableWidget(this);
        }
        this.columns = columns;
        this.maxRows = maxRows;
        this.title = title;
        this.showHeader = showHeader;
        renderer = new TableRenderer(this);
        view = new TableView(this, showScroll);
        view.setWidth(width);
        view.setHeight(maxRows*cellHeight);
        keyboardHandler = new TableKeyboardHandler(this);
        setWidget(view);
        popup.addStyleName("AutoCompletePopup");
        popup.setWidget(view);
        popup.addCloseHandler(this);
        addDomHandler(keyboardHandler,KeyDownEvent.getType());
        addDomHandler(keyboardHandler,KeyUpEvent.getType());
    }
 
    public void showTable() {
    	showingOptions = true;
        popup.setPopupPosition(this.getAbsoluteLeft(), this
                                      .getAbsoluteTop()
                                      + this.getOffsetHeight() - 1);
        popup.show();
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                if(view.header != null)
                   view.header.sizeHeader();
                scrollToSelection();
            }
        });
        focused = true;
        
    }
    
    public void showTable(Widget wid) {
    	showingOptions = true;
        popup.setPopupPosition(wid.getAbsoluteLeft(), wid
                                      .getAbsoluteTop()
                                      + wid.getOffsetHeight() - 1);
        popup.show();
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                if(view.header != null)
                   view.header.sizeHeader();
                scrollToSelection();
            }
        });
        focused = true;
    }
    
    public void hideTable() {
    	showingOptions = false;
        popup.hide();
    }

	public void onClose(CloseEvent<PopupPanel> event) {
		showingOptions = false;
	}

	public HandlerRegistration addCloseHandler(CloseHandler<PopupPanel> handler) {
		return addHandler(handler,CloseEvent.getType());
	}
    
}
