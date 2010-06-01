package org.openelis.gwt.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class UDropdown<T> extends TextBox<T> {

    protected HorizontalPanel  hp;
    protected IconContainer    icon;
    protected DropdownListener listener;
    protected DropdownTable    table;

    public UDropdown() {
    }

    @Override
    public void init() {
        hp = new HorizontalPanel();
        icon = new IconContainer();
        setStyleName("AutoDropDown");
        icon.setStyleName("AutoDropDownButton");
        textbox.setStyleName("TextboxUnselected");
        
        icon.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                // TODO Auto-generated method stub
                
            }
            
        });
        
        addDomHandler(new KeyUpHandler() {

            public void onKeyUp(KeyUpEvent event) {
                // TODO Auto-generated method stub
                
            }
            
        }, KeyUpEvent.getType());

        hp.add(this);
        hp.add(icon);
        initWidget(hp);
    }

    private class DropdownListener implements ClickHandler, KeyUpHandler {

        public void onClick(ClickEvent event) {
            if ( !isEnabled())
                return;
            if (event.getSource() == icon) {
                if (table.getSelectedRow() < 0) {
                    if (table.getSelections().size() > 0)
                        table.selectRow((Integer)table.getSelectedRows()[0]);
                }
                table.showTable();
            }

        }

        public void onKeyUp(KeyUpEvent event) {
            if ( !isEnabled())
                return;
            int keyCode = event.getNativeKeyCode();
            if (keyCode == KeyCodes.KEY_DOWN  || keyCode == KeyCodes.KEY_UP    ||
                keyCode == KeyCodes.KEY_TAB   || keyCode == KeyCodes.KEY_LEFT  ||
                keyCode == KeyCodes.KEY_RIGHT || keyCode == KeyCodes.KEY_ALT   ||
                keyCode == KeyCodes.KEY_CTRL  || keyCode == KeyCodes.KEY_SHIFT ||
                keyCode == KeyCodes.KEY_ESCAPE)
                return;
            if (keyCode == KeyCodes.KEY_ENTER && !table.popup.isShowing() && !table.itemSelected) {
                if (table.getSelectedRow() < 0) {
                    if (table.getSelections().size() > 0)
                        table.selectRow((Integer)table.getSelectedRows()[0]);
                }
                table.showTable();
                return;
            }
            if (keyCode == KeyCodes.KEY_ENTER && table.itemSelected) {
                table.itemSelected = false;
                return;
            }
            String text = getText();
            if (text.length() > 0 && !text.endsWith("*")) {
                table.setDelay(text, 1);
            } else if (text.length() == 0) {
                table.selectRow(0);
                table.scrollToSelection();
            } else {
                table.hideTable();
            }
        }
    }

}
