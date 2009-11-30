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
package org.openelis.gwt.widget.richtext;

import java.util.ArrayList;

import org.openelis.gwt.widget.AppButton;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Label;
import org.openelis.gwt.widget.AppButton.ButtonState;
import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableRow;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;


/**
 * A sample toolbar for use with {@link RichTextArea}. It provides a simple UI
 * for all rich text formatting, dynamically displayed only for the available
 * functionality.
 */
public class RichTextToolbar extends Composite {
  /**
   * We use an inner EventListener class to avoid exposing event methods on the
   * RichTextToolbar itself.
   */
  private class EventListener implements ClickHandler, SelectionHandler<TableRow>,KeyUpHandler {

    public void onSelection(SelectionEvent<TableRow> event) {
        
      if (event.getSource() == backColors) {
        basic.setBackColor((String)event.getSelectedItem().row.key);
      } else if (event.getSource() == foreColors) {
        basic.setForeColor((String)event.getSelectedItem().row.key);
      } else if (event.getSource() == fonts) {
        basic.setFontName((String)event.getSelectedItem().row.key);
      } else if (event.getSource() == fontSizes){
        basic.setFontSize(fontSizesConstants[Integer.parseInt((String)event.getSelectedItem().row.key)]);
      } 
    }

    public void onClick(ClickEvent event) {
      if (event.getSource() == bold) {
        basic.toggleBold();
      } else if (event.getSource() == italic) {
        basic.toggleItalic();
      } else if (event.getSource() == underline) {
        basic.toggleUnderline();
      } else if (event.getSource() == subscript) {
        basic.toggleSubscript();
      } else if (event.getSource() == superscript) {
        basic.toggleSuperscript();
      } else if (event.getSource() == strikethrough) {
        extended.toggleStrikethrough();
      } else if (event.getSource() == indent) {
        extended.rightIndent();
      } else if (event.getSource() == outdent) {
        extended.leftIndent();
      } else if (event.getSource() == justifyLeft) {
        basic.setJustification(RichTextArea.Justification.LEFT);
      } else if(event.getSource() == justifyCenter) {
        basic.setJustification(RichTextArea.Justification.CENTER);
      } else if (event.getSource() == justifyRight) {
        basic.setJustification(RichTextArea.Justification.RIGHT);
      } else if (event.getSource() == insertImage) {
        String url = Window.prompt("Enter an image URL:", "http://");
        if (url != null) {
          extended.insertImage(url);
        }
      } else if (event.getSource() == createLink) {
        String url = Window.prompt("Enter a link URL:", "http://");
        if (url != null) {
          extended.createLink(url);
        }
      } else if (event.getSource() == removeLink) {
        extended.removeLink();
      } else if (event.getSource() == hr) {
        extended.insertHorizontalRule();
      } else if (event.getSource() == ol) {
        extended.insertOrderedList();
      } else if (event.getSource() == ul) {
        extended.insertUnorderedList();
      } else if (event.getSource() == removeFormat) {
        extended.removeFormat();
      } 
      else if (event.getSource() == richText) {
    	  checkToggleStates();
      }
    }

	public void onKeyUp(KeyUpEvent event) {
		if(event.getNativeKeyCode() == KeyboardHandler.KEY_RIGHT || 
		   event.getNativeKeyCode() == KeyboardHandler.KEY_LEFT || 
		   event.getNativeKeyCode() == KeyboardHandler.KEY_UP ||
		   event.getNativeKeyCode() == KeyboardHandler.KEY_DOWN) {
			checkToggleStates();
		}
		
	}
	
	private void checkToggleStates() {
  	  if(basic.isBold() && bold.getState() != ButtonState.PRESSED)
		  bold.setState(ButtonState.PRESSED);
	  else if(!basic.isBold() && bold.getState() == ButtonState.PRESSED)
		  bold.setState(ButtonState.UNPRESSED);
	  if(basic.isItalic() && italic.getState() != ButtonState.PRESSED)
		  italic.setState(ButtonState.PRESSED);
	  else if(!basic.isItalic() && italic.getState() == ButtonState.PRESSED)
		  italic.setState(ButtonState.UNPRESSED);
	  if(basic.isUnderlined() && underline.getState() != ButtonState.PRESSED)
		  underline.setState(ButtonState.PRESSED);
	  else if(!basic.isUnderlined() && underline.getState() == ButtonState.PRESSED)
		  underline.setState(ButtonState.UNPRESSED);
	  if(basic.isSubscript() && subscript.getState() != ButtonState.PRESSED)
		  subscript.setState(ButtonState.PRESSED);
	  else if(!basic.isSubscript() && subscript.getState() == ButtonState.PRESSED)
		  subscript.setState(ButtonState.UNPRESSED);
	  if(basic.isSuperscript() && superscript.getState() != ButtonState.PRESSED)
		  superscript.setState(ButtonState.PRESSED);
	  else if(!basic.isSuperscript() && superscript.getState() == ButtonState.PRESSED)
		  superscript.setState(ButtonState.UNPRESSED);
	  if(basic.isStrikethrough() && strikethrough.getState() != ButtonState.PRESSED)
		  strikethrough.setState(ButtonState.PRESSED);
	  else if(!basic.isStrikethrough() && strikethrough.getState() == ButtonState.PRESSED)
		  strikethrough.setState(ButtonState.UNPRESSED);
	}

  }

  private static final RichTextArea.FontSize[] fontSizesConstants = new RichTextArea.FontSize[] {
      RichTextArea.FontSize.XX_SMALL, RichTextArea.FontSize.X_SMALL,
      RichTextArea.FontSize.SMALL, RichTextArea.FontSize.MEDIUM,
      RichTextArea.FontSize.LARGE, RichTextArea.FontSize.X_LARGE,
      RichTextArea.FontSize.XX_LARGE};

  //private Images images = (Images) GWT.create(Images.class);
  private EventListener listener = new EventListener();

  private RichTextArea richText;
  private RichTextArea.Formatter basic;
  private RichTextArea.Formatter extended;

  private Grid outer = new Grid(2,1);
  private HorizontalPanel topPanel = new HorizontalPanel();
  private HorizontalPanel bottomPanel = new HorizontalPanel();
  private AppButton bold;
  private AppButton italic;
  private AppButton underline;
  private AppButton subscript;
  private AppButton superscript;
  private AppButton strikethrough;
  private AppButton indent;
  private AppButton outdent;
  private AppButton justifyLeft;
  private AppButton justifyCenter;
  private AppButton justifyRight;
  private AppButton hr;
  private AppButton ol;
  private AppButton ul;
  private AppButton insertImage;
  private AppButton createLink;
  private AppButton removeLink;
  private AppButton removeFormat;

  private Dropdown<String> backColors;
  private Dropdown<String> foreColors;
  private Dropdown<String> fonts;
  private Dropdown<String> fontSizes;
  
  private Label<String> fontLabel;
  private Label<String> sizeLabel;

  /**
   * Creates a new toolbar that drives the given rich text area.
   * 
   * @param richText the rich text area to be controlled
   */
  public RichTextToolbar(RichTextArea richText) {

	this.richText = richText;
    this.basic = richText.getFormatter();
    this.extended = richText.getFormatter();
    richText.addClickHandler(listener);
    richText.addKeyUpHandler(listener);
    outer.setWidget(0,0,topPanel);
    outer.setWidget(1,0,bottomPanel);
    topPanel.setWidth("100%");
    topPanel.setStyleName("ButtonPanelContainer");
    bottomPanel.setWidth("100%");
    initWidget(outer);
    outer.setCellPadding(0);
    outer.setCellSpacing(0);
    setStyleName("gwt-RichTextToolbar");
    setWidth("100%");
    richText.addStyleName("hasRichTextToolbar");

    if (basic != null) {
      topPanel.add(bold = createToggleButton("Bold", "Bold"));
      topPanel.add(italic = createToggleButton("Italic", "Italic"));
      topPanel.add(underline = createToggleButton("Underline",
          "Underline"));
      topPanel.add(subscript = createToggleButton("Subscript",
          "Subscript"));
      topPanel.add(superscript = createToggleButton("Superscript",
          "Superscript"));
      topPanel.add(justifyLeft = createPushButton("JustifyLeft",
          "Justify Left"));
      topPanel.add(justifyCenter = createPushButton("JustifyCenter",
          "JustifyCenter"));
      topPanel.add(justifyRight = createPushButton("JustifyRight",
          "JustifyRight"));
    }

    if (extended != null) {
      topPanel.add(strikethrough = createToggleButton("StrikeThrough",
          "Strike Through"));
      topPanel.add(indent = createPushButton("Indent", "Indent"));
      topPanel.add(outdent = createPushButton("Outdent", "Outdent"));
      topPanel.add(hr = createPushButton("HR", "HR"));
      topPanel.add(ol = createPushButton("OL", "OL"));
      topPanel.add(ul = createPushButton("UL", "UL"));
      topPanel.add(removeFormat = createPushButton("RemoveFormat","Remove Format"));
    }
    
  }

  private Dropdown createColorList(String caption) {
    Dropdown<String> lb = new Dropdown<String>();
    lb.addSelectionHandler(listener);
    ArrayList<TableDataRow> model = new ArrayList<TableDataRow>();
    
    model.add(new TableDataRow("",caption));
    model.add(new TableDataRow("white", "White"));
    model.add(new TableDataRow("black", "Black"));
    model.add(new TableDataRow("red","Red"));
    model.add(new TableDataRow("green","Green"));
    model.add(new TableDataRow("yellow","Yellow"));
    model.add(new TableDataRow("blue","Blue"));
    
    lb.setModel(model);
    lb.setSelection("");
    return lb;
  }

  private Dropdown createFontList() {
    Dropdown lb = new Dropdown();
    lb.addSelectionHandler(listener);
    ArrayList<TableDataRow> model = new ArrayList<TableDataRow>();
    
    model.add(new TableDataRow("","Font"));
    model.add(new TableDataRow("Times New Roman", "Times New Roman"));
    model.add(new TableDataRow("Arial", "Arial"));
    model.add(new TableDataRow("Courier New", "Courier New"));
    model.add(new TableDataRow("Georgia", "Georgia"));
    model.add(new TableDataRow("Trebuchet", "Trebuchet"));
    model.add(new TableDataRow("Verdana", "Verdana"));
    
    lb.setModel(model);
    lb.setSelection("");
    return lb;
  }

  private Dropdown createFontSizes() {
    Dropdown lb = new Dropdown();
    lb.addSelectionHandler(listener);
    ArrayList<TableDataRow> model = new ArrayList<TableDataRow>();
    
    model.add(new TableDataRow("0","Size"));
    model.add(new TableDataRow("1","XX Small"));
    model.add(new TableDataRow("2","X Small"));
    model.add(new TableDataRow("3","Small"));
    model.add(new TableDataRow("4","Medium"));
    model.add(new TableDataRow("5","Large"));
    model.add(new TableDataRow("6","X Large"));
    model.add(new TableDataRow("7","XX Large"));
    
    lb.setModel(model);
    lb.setSelection("0");
    return lb;
  }

  private AppButton createPushButton(String img, String action) {
      AppButton ab = new AppButton();
      ab.setAction(action);
      ab.setStyleName("Button");
      AbsolutePanel ap = new AbsolutePanel();
      ap.setStyleName(img);
      ab.setWidget(ap);
      ab.addClickHandler(listener);
      ab.enable(true);
      return ab;
  }

  private AppButton createToggleButton(String img, String action) {
    AppButton ab = new AppButton();
    ab.setAction(action);
    ab.setStyleName("Button");
    AbsolutePanel ap = new AbsolutePanel();
    ap.setStyleName(img);
    ab.setWidget(ap);
    ab.addClickHandler(listener);
    ab.setToggle(true);
    ab.enable(true);
    return ab;
  }
  
  public void enable(boolean enabled) {
	  
  }
  
  public void reset() {
	bold.setState(ButtonState.UNPRESSED);
	italic.setState(ButtonState.UNPRESSED);
	superscript.setState(ButtonState.UNPRESSED);
	subscript.setState(ButtonState.UNPRESSED);
	strikethrough.setState(ButtonState.UNPRESSED);
	underline.setState(ButtonState.UNPRESSED);
	richText.setHTML("");
  }
}

