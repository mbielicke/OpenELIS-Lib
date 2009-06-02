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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.common.data.TableDataModel;
import org.openelis.gwt.common.data.TableDataRow;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenLabel;
import org.openelis.gwt.screen.ScreenMenuItem;
import org.openelis.gwt.screen.ScreenMenuPanel;
import org.openelis.gwt.widget.AppButton;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.MenuItem;
import org.openelis.gwt.widget.MenuPanel;

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
  private class EventListener implements ClickListener, ChangeListener,
      KeyboardListener {

    public void onChange(Widget sender) {
        
      if (sender == backColors) {
        basic.setBackColor((String)backColors.model.getSelection().cells[0].getValue());
        backColors.model.selectRow(0);
      } else if (sender == foreColors) {
        basic.setForeColor((String)foreColors.model.getSelection().cells[0].getValue());
        foreColors.model.selectRow(0);
      } else if (sender == fonts) {
        basic.setFontName((String)fonts.model.getSelection().cells[0].getValue());
        fonts.model.selectRow(0);
      } else if (sender == fontSizes) {
        basic.setFontSize(fontSizesConstants[Integer.parseInt((String)fontSizes.model.getSelection().cells[0].getValue())]);
        fontSizes.model.selectRow(0);
      } 
      
      
    }

    public void onClick(Widget sender) {
      if (sender == bold) {
        basic.toggleBold();
      } else if (sender == italic) {
        basic.toggleItalic();
      } else if (sender == underline) {
        basic.toggleUnderline();
      } else if (sender == subscript) {
        basic.toggleSubscript();
      } else if (sender == superscript) {
        basic.toggleSuperscript();
      } else if (sender == strikethrough) {
        extended.toggleStrikethrough();
      } else if (sender == indent) {
        extended.rightIndent();
      } else if (sender == outdent) {
        extended.leftIndent();
      } else if (sender == justifyLeft) {
        basic.setJustification(RichTextArea.Justification.LEFT);
      } else if (sender == justifyCenter) {
        basic.setJustification(RichTextArea.Justification.CENTER);
      } else if (sender == justifyRight) {
        basic.setJustification(RichTextArea.Justification.RIGHT);
      } else if (sender == insertImage) {
        String url = Window.prompt("Enter an image URL:", "http://");
        if (url != null) {
          extended.insertImage(url);
        }
      } else if (sender == createLink) {
        String url = Window.prompt("Enter a link URL:", "http://");
        if (url != null) {
          extended.createLink(url);
        }
      } else if (sender == removeLink) {
        extended.removeLink();
      } else if (sender == hr) {
        extended.insertHorizontalRule();
      } else if (sender == ol) {
        extended.insertOrderedList();
      } else if (sender == ul) {
        extended.insertUnorderedList();
      } else if (sender == removeFormat) {
        extended.removeFormat();
      } else if (sender == richText) {
        // We use the RichTextArea's onKeyUp event to update the toolbar status.
        // This will catch any cases where the user moves the cursur using the
        // keyboard, or uses one of the browser's built-in keyboard shortcuts.
        updateStatus();
      } else if (sender instanceof MenuItem) {
          String[] args = ((String)((StringObject)((MenuItem)sender).args[0]).getValue()).split(":");
          if(args[0].equals("font")){
              basic.setFontName(args[1]);
              fontLabel.label.setText(((MenuItem)sender).label);
          } else {
              basic.setFontSize(fontSizesConstants[Integer.parseInt(args[1])]);
              sizeLabel.label.setText(((MenuItem)sender).label);
          }
      }
    }

    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
      if (sender == richText) {
        // We use the RichTextArea's onKeyUp event to update the toolbar status.
        // This will catch any cases where the user moves the cursur using the
        // keyboard, or uses one of the browser's built-in keyboard shortcuts.
        updateStatus();
      }
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
  private RichTextArea.BasicFormatter basic;
  private RichTextArea.ExtendedFormatter extended;

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

  private Dropdown backColors;
  private Dropdown foreColors;
  private Dropdown fonts;
  private Dropdown fontSizes;
  
  private ScreenLabel fontLabel;
  private ScreenLabel sizeLabel;
  
  private ScreenMenuPanel fontsMenu;
  private ScreenMenuPanel fontSizeMenu;
  
  private ScreenBase screen;
  
  private static String fontsMenuXMl =  "<menuPanel key=\"fontsMenu\" layout=\"vertical\" style=\"topBarItemHolder\">"+
  "<menuItem>" +
  "<menuDisplay>"+
      "<appButton action=\"option\" style=\"fonttype\">"+
          "<HorizontalPanel>"+
              "<AbsolutePanel style=\"FontSize\"/>"+
              "<label key=\"fontLabel\">Times New Roman</label>" +
              "<AbsolutePanel style=\"OptionsButtonImage\"/>" + 
          "</HorizontalPanel>" +
      "</appButton>" +
  "</menuDisplay>" +
  "<menuPanel style=\"topMenuContainer\" layout=\"vertical\" position=\"below\">" +
     "<menuItem key=\"FontTimes\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"Times New Roman\" icon=\"\" description=\"\" enabled=\"true\" args=\"font:Times New Roman\"/>"+  
     "<menuItem key=\"FontArial\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"Arial\" icon=\"\" description=\"\" enabled=\"true\" args=\"font:Arial\"/>"+
     "<menuItem key=\"FontCourier\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"Courier New\" icon=\"\" description=\"\" enabled=\"true\" args=\"font:Courier New\"/>"+  
     "<menuItem key=\"FontGeorgia\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"Georgia\" icon=\"\" description=\"\" enabled=\"true\" args=\"font:Georgia\"/>"+ 
     "<menuItem key=\"FontTrebuchet\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"Trebuchet\" icon=\"\" description=\"\" enabled=\"true\" args=\"font:Trebuchet\"/>"+  
     "<menuItem key=\"FontVerdana\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"Verdana\" icon=\"\" description=\"\" enabled=\"true\" args=\"font:Verdana\"/>"+  
   "</menuPanel>" +
  "</menuItem>"+
"</menuPanel>"; 
  
  private static String fontSizeMenuXMl =  "<menuPanel key=\"fontSizeMenu\" layout=\"vertical\" style=\"topBarItemHolder\">"+
  "<menuItem>" +
  "<menuDisplay>"+
      "<appButton action=\"option\" style=\"fontsize\">"+
          "<HorizontalPanel>"+
              "<AbsolutePanel style=\"FontSize\"/>"+
              "<label key=\"sizeLabel\">Medium</label>"+
              "<AbsolutePanel style=\"OptionsButtonImage\"/>" +
          "</HorizontalPanel>" +
      "</appButton>" +
  "</menuDisplay>" +
  "<menuPanel style=\"topMenuContainer\" layout=\"vertical\" position=\"below\">" +
     "<menuItem key=\"FontXXSmall\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"XX Small\" icon=\"\" description=\"\" enabled=\"true\" args=\"size:0\"/>"+  
     "<menuItem key=\"FontXSmall\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"X Small\" icon=\"\" description=\"\" enabled=\"true\" args=\"size:1\"/>"+
     "<menuItem key=\"FontSmall\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"Small\" icon=\"\" description=\"\" enabled=\"true\" args=\"size:2\"/>"+  
     "<menuItem key=\"FontMedium\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"Medium\" icon=\"\" description=\"\" enabled=\"true\" args=\"size:3\"/>"+ 
     "<menuItem key=\"FontLarge\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"Large\" icon=\"\" description=\"\" enabled=\"true\" args=\"size:4\"/>"+  
     "<menuItem key=\"FontXLarge\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"X Large\" icon=\"\" description=\"\" enabled=\"true\" args=\"size:5\"/>"+
     "<menuItem key=\"FontXXLarge\" style=\"TopMenuRowContainer\" hover=\"Hover\" label=\"XX Large\" icon=\"\" description=\"\" enabled=\"true\" args=\"size:6\"/>"+
   "</menuPanel>" +
  "</menuItem>"+
"</menuPanel>"; 

  /**
   * Creates a new toolbar that drives the given rich text area.
   * 
   * @param richText the rich text area to be controlled
   */
  public RichTextToolbar(RichTextArea richText, ScreenBase screen) {
    this.screen = screen;
    this.richText = richText;
    this.basic = richText.getBasicFormatter();
    this.extended = richText.getExtendedFormatter();

    outer.setWidget(0,0,topPanel);
    outer.setWidget(1,0,bottomPanel);
    topPanel.setWidth("100%");
    //topPanel.setc
    bottomPanel.setWidth("100%");
    //outer.setHeight("75px");
    initWidget(outer);
    outer.setCellPadding(0);
    outer.setCellSpacing(0);
    //setc
    setStyleName("gwt-RichTextToolbar");
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
      /*topPanel.add(insertImage = createPushButton("InsertImage",
          "Insert Image"));
      topPanel.add(createLink = createPushButton("CreateLink",
          "Create Link"));
      topPanel.add(removeLink = createPushButton("RemoveLink",
          "Remove Link"));
          */
      topPanel.add(removeFormat = createPushButton("RemoveFormat",
          "Remove Format"));
    }
    
    
    if (basic != null) {
      //bottomPanel.add(backColors = createColorList("Background"));
      //bottomPanel.add(foreColors = createColorList("Foreground"));
      //bottomPanel.add(fonts = createFontList());
      //bottomPanel.add(fontSizes = createFontSizes());
 /*     
        MenuPanel fontsPanel = new MenuPanel();
        fontsPanel.init("vertical");
        fontsPanel.menuItems.add(new MenuItem("","Times New Roman",""));
        fontsPanel.menuItems.add(new MenuItem("","Arial",""));
        fontsPanel.menuItems.add(new MenuItem("","Courier New",""));
        fontsPanel.menuItems.add(new MenuItem("","Georgia",""));
        fontsPanel.menuItems.add(new MenuItem("","Trebuchet",""));
        fontsPanel.menuItems.add(new MenuItem("","Verdana",""));
        AbsolutePanel fntImg = new AbsolutePanel();
        fntImg.add(new Label("Fonts"));
        fntImg.setStyleName("FontsImage");
        fontsPanel.add(fntImg);
        MenuItem fontsItem = new MenuItem(fntImg);
        fontsItem.menuItemsPanel = fontsPanel;
        fontsItem.enable(true);
        topPanel.add(fontsItem);
        
        MenuPanel fontSizePanel = new MenuPanel();
        fontSizePanel.init("vertical");
        fontSizePanel.menuItems.add(new MenuItem("","XX Small",""));
        fontSizePanel.menuItems.add(new MenuItem("","X Small",""));
        fontSizePanel.menuItems.add(new MenuItem("","Small",""));
        fontSizePanel.menuItems.add(new MenuItem("","Medium",""));
        fontSizePanel.menuItems.add(new MenuItem("","Large",""));
        fontSizePanel.menuItems.add(new MenuItem("","X Large",""));
        fontSizePanel.menuItems.add(new MenuItem("","XX Large",""));
        AbsolutePanel fntSizeImg = new AbsolutePanel();
        fntSizeImg.add(new Label("Font Size"));
        fntSizeImg.setStyleName("FontSizeImge");
        fontSizePanel.add(fntSizeImg);
        MenuItem fontSizeItem = new MenuItem(fntSizeImg);
        fontSizeItem.menuItemsPanel = fontSizePanel;
        topPanel.add(fontSizeItem);
        fontSizeItem.enable(true);
   */
        topPanel.add(new ScreenMenuPanel(XMLParser.parse(fontSizeMenuXMl).getDocumentElement(),screen));
        topPanel.add(new ScreenMenuPanel(XMLParser.parse(fontsMenuXMl).getDocumentElement(),screen));
        ((ScreenMenuItem)screen.widgets.get("FontTimes")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontArial")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontCourier")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontGeorgia")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontTrebuchet")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontVerdana")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontXXSmall")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontXSmall")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontSmall")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontMedium")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontLarge")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontXLarge")).item.addClickListener(listener);
        ((ScreenMenuItem)screen.widgets.get("FontXXLarge")).item.addClickListener(listener);
        fontLabel = (ScreenLabel)screen.widgets.get("fontLabel");
        sizeLabel = (ScreenLabel)screen.widgets.get("sizeLabel");
      // We only use these listeners for updating status, so don't hook them up
      // unless at least basic editing is supported.
      richText.addKeyboardListener(listener);
      richText.addClickListener(listener);
    }
  }

  private Dropdown createColorList(String caption) {
    Dropdown lb = new Dropdown();
    lb.addChangeListener(listener);
    TableDataModel<TableDataRow<String>> model = new TableDataModel<TableDataRow<String>>();
    
    model.add(new TableDataRow<String>("",new StringObject(caption)));
    model.add(new TableDataRow<String>("white", new StringObject("White")));
    model.add(new TableDataRow<String>("black", new StringObject("Black")));
    model.add(new TableDataRow<String>("red",new StringObject("Red")));
    model.add(new TableDataRow<String>("green",new StringObject("Green")));
    model.add(new TableDataRow<String>("yellow",new StringObject("Yellow")));
    model.add(new TableDataRow<String>("blue",new StringObject("Blue")));
    
    lb.setModel(model);
    lb.setSelection("");
    return lb;
  }

  private Dropdown createFontList() {
    Dropdown lb = new Dropdown();
    lb.addChangeListener(listener);
    TableDataModel<TableDataRow<String>> model = new TableDataModel<TableDataRow<String>>();
    
    model.add(new TableDataRow<String>("",new StringObject("Font")));
    model.add(new TableDataRow<String>("Times New Roman", new StringObject("Times New Roman")));
    model.add(new TableDataRow<String>("Arial", new StringObject("Arial")));
    model.add(new TableDataRow<String>("Courier New", new StringObject("Courier New")));
    model.add(new TableDataRow<String>("Georgia", new StringObject("Georgia")));
    model.add(new TableDataRow<String>("Trebuchet", new StringObject("Trebuchet")));
    model.add(new TableDataRow<String>("Verdana", new StringObject("Verdana")));
    
    lb.setModel(model);
    lb.setSelection("");
    return lb;
  }

  private Dropdown createFontSizes() {
    Dropdown lb = new Dropdown();
    lb.addChangeListener(listener);
    TableDataModel<TableDataRow<String>> model = new TableDataModel<TableDataRow<String>>();
    
    model.add(new TableDataRow<String>("0",new StringObject("Size")));
    model.add(new TableDataRow<String>("1",new StringObject("XX Small")));
    model.add(new TableDataRow<String>("2",new StringObject("X Small")));
    model.add(new TableDataRow<String>("3",new StringObject("Small")));
    model.add(new TableDataRow<String>("4",new StringObject("Medium")));
    model.add(new TableDataRow<String>("5",new StringObject("Large")));
    model.add(new TableDataRow<String>("6",new StringObject("X Large")));
    model.add(new TableDataRow<String>("7",new StringObject("XX Large")));
    
    lb.setModel(model);
    lb.setSelection("0");
    return lb;
  }

  private AppButton createPushButton(String img, String action) {
      AppButton ab = new AppButton();
      ab.action = action;
      AbsolutePanel ap = new AbsolutePanel();
      ap.setStyleName(img);
      ab.setWidget(ap);
      ab.addClickListener(listener);
      /*
    PushButton pb = new PushButton(img.createImage());
    pb.addClickListener(listener);
    pb.setTitle(tip);
    */
    return ab;
  }

  private AppButton createToggleButton(String img, String action) {
    AppButton ab = new AppButton();
    ab.action = action;
    AbsolutePanel ap = new AbsolutePanel();
    ap.setStyleName(img);
    ab.setWidget(ap);
    ab.toggle = true;
    ab.addClickListener(listener);
      /*ToggleButton tb = new ToggleButton(img.createImage());
    tb.addClickListener(listener);
    tb.setTitle(tip);*/
    return ab;
  }

  /**
   * Updates the status of all the stateful buttons.
   */
  private void updateStatus() {
    if (basic != null) {
      //bold.setDown(basic.isBold());
      //italic.setDown(basic.isItalic());
      //underline.setDown(basic.isUnderlined());
      //subscript.setDown(basic.isSubscript());
      //superscript.setDown(basic.isSuperscript());
    }

    if (extended != null) {
      //strikethrough.setDown(extended.isStrikethrough());
    }
  }
  
  public void enable(boolean enabled) {
      //backColors.enabled(enabled);
      //foreColors.enabled(enabled);
      //fonts.enabled(enabled);
      //fontSizes.enabled(enabled);
  }
}

