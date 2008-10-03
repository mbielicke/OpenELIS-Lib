package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class AutoCompleteListener implements
                                 ClickListener,
                                 MouseListener,
                                 KeyboardListener {
    
    private AutoComplete widget;
   
    
    public AutoCompleteListener(AutoComplete widget){
        this.widget = widget;
    }

    public void onClick(Widget sender) {
        if(sender == widget.focusPanel){
            if(widget.activeRow < 0)
                widget.showTable(0);
            else
                widget.showTable(widget.modelIndexList[widget.activeRow]);
        }

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

    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub

    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub

    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        if (!widget.textBox.isReadOnly()) {
            if (keyCode == KEY_DOWN || keyCode == KEY_UP ||  keyCode == KEY_TAB 
                    || keyCode == KEY_LEFT || keyCode == KEY_RIGHT || keyCode == KEY_ALT || 
                    keyCode == KEY_CTRL || keyCode == KEY_SHIFT || keyCode == KEY_ESCAPE)
                return;
            if(keyCode == KEY_ENTER && !widget.popup.isShowing() && !widget.itemSelected && widget.focused){//!widget.popup.getWidget().isAttached()){
                if(widget.activeRow < 0)
                    widget.showTable(0);
                else
                    widget.showTable(widget.modelIndexList[widget.activeRow]);
                return;
            }
            if(keyCode == KEY_ENTER && widget.itemSelected){
                widget.itemSelected = false;
                return;
            }

            String text = widget.textBox.getText();
/*            if (widget.model.getData().multiSelect) {
                if (text.length() == 0) {
                    widget.model.clearSelections();
                    widget.choicesPopup.hide();
                } else {
                    if (text.length() < widget.textBoxDefault.length()) {
                        widget.textBox.setText(widget.textBoxDefault);
                        widget.choicesPopup.hide();
                    }
                }
            } else */if (text.length() > 0 && !text.endsWith("*")) {
                widget.setDelay(text, 350);
            } else if(text.length() == 0) {
                widget.model.clear();
            } else {
                widget.hideTable();
            }
        }
    }

}
