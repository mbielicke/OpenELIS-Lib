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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.DataSorterInt;
import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.data.NumberObject;
import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.widget.MenuItem;
import org.openelis.gwt.widget.MenuPanel;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.table.TableHeaderMenuBar.ListenContainer;

import java.util.ArrayList;

public class TableHeaderMenuBarWebkitImpl extends TableHeaderMenuBar {
    
    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseUp(Widget sender, int x, int y) {
        
            if (resizing) {
                DOM.releaseCapture(sender.getElement());
                columns.get(tableCol1).setCurrentWidth( columns.get(tableCol1).getCurrentWidth() + (sender.getAbsoluteLeft() - startx));
                columns.get(tableCol2).setCurrentWidth( columns.get(tableCol2).getCurrentWidth() - (sender.getAbsoluteLeft() - startx));
                RootPanel.get().remove(sender);
                resizing = false;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        int adj1 = -2;
                        int adj2 = 3;
                        
                        for(int i = 0; i < columns.size(); i++){
                            if( i > 0 && i < columns.size() - 1){
                                panel.setCellWidth(panel.getWidget(i*2),(columns.get(i).getCurrentWidth()+adj1)+"px");
                                ((ListenContainer)panel.getWidget(i*2)).getWidget(0).setWidth((columns.get(i).getCurrentWidth()+adj1 -16)+"px");
                            }else{
                                panel.setCellWidth(panel.getWidget(i*2),(columns.get(i).getCurrentWidth()+adj2)+"px");
                                ((ListenContainer)panel.getWidget(i*2)).getWidget(0).setWidth((columns.get(i).getCurrentWidth()+adj2 - 16)+"px");
                            }   
                        }
                        for (int j = 0; j < controller.view.table.getRowCount(); j++) {
                            for (int i = 0; i < columns.size(); i++) {
                                controller.view.table.getFlexCellFormatter().setWidth(j, i, (columns.get(i).getCurrentWidth()) +  "px");
                                ((TableCellWidget)controller.view.table.getWidget(j,i)).setCellWidth(columns.get(i).getCurrentWidth());
                                ((SimplePanel)controller.view.table.getWidget(j, i)).setWidth((columns.get(i).getCurrentWidth()) + "px");
                                //((SimplePanel)view.table.getWidget(j,i)).getWidget().setWidth(curColWidth[i]+"px");
                            }
                        }
                    }
                });
            }
        
    }
    
    public void sizeHeader() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                int adj1 = -2;
                int adj2 = 3;
                int width = 0;
                for(TableColumnInt column : columns)
                    width += column.getCurrentWidth();
                int displayWidth = width + (columns.size()*4) - (columns.size() -1);
                setWidth(displayWidth+"px");  
                for(int i = 0; i < columns.size(); i++){
                    if( i > 0 && i < columns.size() - 1){
                        panel.setCellWidth(panel.getWidget(i*2),(columns.get(i).getCurrentWidth()+adj1)+"px");
                        ((ListenContainer)panel.getWidget(i*2)).getWidget(0).setWidth((columns.get(i).getCurrentWidth()+adj1 -16)+"px");
                    }else{
                        panel.setCellWidth(panel.getWidget(i*2),(columns.get(i).getCurrentWidth()+adj2)+"px");
                        ((ListenContainer)panel.getWidget(i*2)).getWidget(0).setWidth((columns.get(i).getCurrentWidth()+adj2 - 16)+"px");
                    }
                }
                setWidth(displayWidth+"px");
                controller.view.cellView.setScrollWidth(displayWidth+"px");
            }
        });
        
    }
    

}
