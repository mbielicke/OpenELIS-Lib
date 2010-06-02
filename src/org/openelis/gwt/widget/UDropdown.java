package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableRow;
import org.openelis.gwt.widget.table.TableWidget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;

public class UDropdown<T> extends TextBox<T> implements KeyDownHandler{

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
            	showPopup();
            }
        });
        
        addDomHandler(this,KeyDownEvent.getType());
       
        hp.add(textbox);
        hp.add(button);
        
        initWidget(hp);
        setStyleName("AutoDropDown");
        
    }
    
    public void showPopup() {
        if(popup == null){
            popup = new PopupPanel();
            popup.setStyleName("DropdownPopup");
            popup.setWidget(table);
            popup.setPreviewingAllNativeEvents(false);
            //popup.
            //table.sinkEvents(Event.ONKEYUP & Event.ONKEYDOWN);
        }
        popup.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop()+getOffsetHeight());
        popup.show();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        button.enable(enabled);
        table.enable(enabled);
        super.setEnabled(enabled);
    }
    
    public void setPopupContext(TableWidget table) {
        this.table = table;
    }
    
    public void setModel(ArrayList<TableDataRow> model) {
        assert table != null;
        if(table.getMaxRows() > model.size())
        	table.setMaxRows(model.size());
        table.view.setHeight(table.getMaxRows()*21);
        table.load(model);
        table.addSelectionHandler(new SelectionHandler<TableRow>() {
        	public void onSelection(SelectionEvent<TableRow> event) {
        		setDisplay();
        		popup.hide();
        	}
        });

    }
   
    public void setSelectedIndex(int index){
        table.selectRow(index);
        setDisplay();
    }
    
    public int getSelectedIndex() {
        return table.getSelectedRow();
    }
    
    public void setValues(T... values) {
        
    }
        
    public TableDataRow getSelectedRow() {
        return null;
    }
    
    public ArrayList<TableDataRow> getSelectedRows() {
        return null;
    }
    
    private void setDisplay() {
    	textbox.setText(table.getSelection().getCells().get(0).toString());
    }    

	public void onKeyDown(KeyDownEvent event) {
//        if(!popup.isShowing())
  //          return;
        event.stopPropagation();
        //if(event.getNativeKeyCode() == KeyCodes.KEY_CTRL)
          //  ctrlKey = true;
        //if(event.getNativeKeyCode() == KeyCodes.KEY_SHIFT)
          //  shiftKey = true;
        if (KeyCodes.KEY_DOWN == event.getNativeKeyCode()) {
            if (table.getSelectedRow() >= 0 && table.getSelectedRow() < table.numRows() - 1) {
            	final int row = findNextActive(table.getSelectedRow());
            	//if(!isRowDrawn(row)){
            		//view.setScrollPosition(view.top+(cellHeight*(row-selectedRow)));
            	//}
                table.selectRow(row);
                setDisplay();
            }else if(table.getSelectedRow() < 0){
            	table.selectRow(0);
            	setDisplay();
            }
        }
        if (KeyCodes.KEY_UP == event.getNativeKeyCode()) {
            if (table.getSelectedRow() > 0) {
                final int row = findPrevActive(table.getSelectedRow());
            	//if(!isRowDrawn(row)){
            		//view.setScrollPosition(view.top-(cellHeight*(selectedRow-row)));
            	//}
                table.selectRow(row);
                setDisplay();
            }
        }
        if (KeyCodes.KEY_ENTER == event.getNativeKeyCode() || KeyCodes.KEY_TAB == event.getNativeKeyCode()) {
        	if(table.getSelectedRow() > -1){
                //itemSelected = true;
                //SelectionEvent.fire(this, renderer.rows.get(tableIndex(selectedRow)));
        		if(popup == null || !popup.isShowing())
        			showPopup();
        		else
        			popup.hide();
            }
        }
        if (KeyCodes.KEY_ESCAPE == event.getNativeKeyCode()){
            popup.hide();
        }
		
	}
	
    private int findNextActive(int current) {
        int next = current + 1;
        while(next < table.numRows() && !table.isEnabled(next))
            next++;
        if(next < table.numRows())
            return next;
        return findNextActive(next);
    }
    
    private int findPrevActive(int current) {
        int prev = current - 1;
        while(prev > -1 && !table.isEnabled(prev))
            prev--;
        if(prev >  -1)
            return prev;
        return findPrevActive(1);
    }

	public void onKeyUp(KeyUpEvent event) {
        //if(event.getNativeKeyCode() == KeyCodes.KEY_CTRL)
          //  ctrlKey = false;
        //if(event.getNativeKeyCode() == KeyCodes.KEY_SHIFT)
          //  shiftKey = false;
	}


}
