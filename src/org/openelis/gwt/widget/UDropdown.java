package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableWidget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;

public class UDropdown<T> extends TextBox<T> {

    protected HorizontalPanel  hp;
    protected AppButton        button;
    protected TableWidget      table;
    protected PopupPanel       popup;

    public UDropdown() {
    }

    @Override
    public void init() {
        hp = new HorizontalPanel();
        textbox = new com.google.gwt.user.client.ui.TextBox();
        button = new AppButton("AutoDropdownButton");
        
        textbox.setStyleName("TextboxUnselected");
        
        final UDropdown<T> source = this;
        
        textbox.addFocusHandler(new FocusHandler() {
            public void onFocus(FocusEvent event) {
                FocusEvent.fireNativeEvent(Document.get().createFocusEvent(),source);
            }
        });
        
        textbox.addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent event) {
                BlurEvent.fireNativeEvent(Document.get().createBlurEvent(), source);
            }
        });
        
        textbox.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                
            }
        });
        
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if(popup == null){
                    popup = new PopupPanel();
                    popup.add(table);
                }
                popup.setPopupPosition(source.getAbsoluteLeft(), source.getAbsoluteTop()+source.getOffsetHeight());
                popup.show();
            }
        });
       
        hp.add(textbox);
        hp.add(button);
        
        initWidget(hp);
        setStyleName("AutoDropDown");
        
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        button.enable(enabled);
        super.setEnabled(enabled);
    }
    
    public void setPopupContext(TableWidget table) {
        this.table = table;
    }
    
    public void setModel(ArrayList<TableDataRow> model) {
        //code this
    }
   
    public void setSelectedIndex(int index){
        
    }
    
    public int getSelectedIndex() {
        return -1;
    }
    
    public void setValues(T... values) {
        
    }
        
    public TableDataRow getSelectedRow() {
        return null;
    }
    
    public ArrayList<TableDataRow> getSelectedRows() {
        return null;
    }
    
 


}
