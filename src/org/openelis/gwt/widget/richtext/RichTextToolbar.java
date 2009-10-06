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

import org.openelis.gwt.common.data.deprecated.StringObject;
import org.openelis.gwt.screen.ScreenDef;
import org.openelis.gwt.screen.UIUtil;
import org.openelis.gwt.widget.AppButton;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Label;
import org.openelis.gwt.widget.MenuItem;
import org.openelis.gwt.widget.MenuPanel;
import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableRow;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.xml.client.XMLParser;


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
  private class EventListener implements ClickHandler, SelectionHandler<TableRow> {

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
  
  private MenuPanel fontsMenu;
  private MenuPanel fontSizeMenu;
    
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
     "<menuItem key=\"FontTimes\" style=\"TopMenuRowContainer\" label=\"Times New Roman\" icon=\"\" description=\"\" enabled=\"true\" />"+  
     "<menuItem key=\"FontArial\" style=\"TopMenuRowContainer\" label=\"Arial\" icon=\"\" description=\"\" enabled=\"true\" />"+
     "<menuItem key=\"FontCourier\" style=\"TopMenuRowContainer\" label=\"Courier New\" icon=\"\" description=\"\" enabled=\"true\"/>"+  
     "<menuItem key=\"FontGeorgia\" style=\"TopMenuRowContainer\" label=\"Georgia\" icon=\"\" description=\"\" enabled=\"true\"/>"+ 
     "<menuItem key=\"FontTrebuchet\" style=\"TopMenuRowContainer\" label=\"Trebuchet\" icon=\"\" description=\"\" enabled=\"true\" />"+  
     "<menuItem key=\"FontVerdana\" style=\"TopMenuRowContainer\" label=\"Verdana\" icon=\"\" description=\"\" enabled=\"true\" />"+  
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
     "<menuItem key=\"FontXXSmall\" style=\"TopMenuRowContainer\" label=\"XX Small\" icon=\"\" description=\"\" enabled=\"true\" />"+  
     "<menuItem key=\"FontXSmall\" style=\"TopMenuRowContainer\" label=\"X Small\" icon=\"\" description=\"\" enabled=\"true\" />"+
     "<menuItem key=\"FontSmall\" style=\"TopMenuRowContainer\" label=\"Small\" icon=\"\" description=\"\" enabled=\"true\" />"+  
     "<menuItem key=\"FontMedium\" style=\"TopMenuRowContainer\" label=\"Medium\" icon=\"\" description=\"\" enabled=\"true\" />"+ 
     "<menuItem key=\"FontLarge\" style=\"TopMenuRowContainer\" label=\"Large\" icon=\"\" description=\"\" enabled=\"true\" />"+  
     "<menuItem key=\"FontXLarge\" style=\"TopMenuRowContainer\" label=\"X Large\" icon=\"\" description=\"\" enabled=\"true\" />"+
     "<menuItem key=\"FontXXLarge\" style=\"TopMenuRowContainer\" label=\"XX Large\" icon=\"\" description=\"\" enabled=\"true\"/>"+
   "</menuPanel>" +
  "</menuItem>"+
"</menuPanel>"; 

  /**
   * Creates a new toolbar that drives the given rich text area.
   * 
   * @param richText the rich text area to be controlled
   */
  public RichTextToolbar(RichTextArea richText) {

	this.richText = richText;
    this.basic = richText.getFormatter();
    this.extended = richText.getFormatter();

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
      topPanel.add(removeFormat = createPushButton("RemoveFormat",
          "Remove Format"));
    }
    
    
    if (basic != null) {
    	ScreenDef def = new ScreenDef();
        topPanel.add(UIUtil.createWidget(XMLParser.parse(fontSizeMenuXMl).getDocumentElement(),def));
        topPanel.add(UIUtil.createWidget(XMLParser.parse(fontsMenuXMl).getDocumentElement(),def));
        ((MenuItem)def.getWidget("FontTimes")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		basic.setFontName("Times New Roman");
        		fontLabel.setText("Times New Roman");
        	}
        });
        ((MenuItem)def.getWidget("FontArial")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		basic.setFontName("Arial");
        		fontLabel.setText("Arial");
        	}
        });
        ((MenuItem)def.getWidget("FontCourier")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		basic.setFontName("Courier New");
        		fontLabel.setText("Courier New");
        	}
        });
        ((MenuItem)def.getWidget("FontGeorgia")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		basic.setFontName("Georgia");
        		fontLabel.setText("Georgia");
        	}
        });
        ((MenuItem)def.getWidget("FontTrebuchet")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		basic.setFontName("Trebuchet");
        		fontLabel.setText("Trebuchet");
        	}
        });
        ((MenuItem)def.getWidget("FontVerdana")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		basic.setFontName("Verdana");
        		fontLabel.setText("Verdana");
        	}
        });
        ((MenuItem)def.getWidget("FontXXSmall")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		 basic.setFontSize(fontSizesConstants[0]);
        		 sizeLabel.setText("XX Small");
        	}
        });
        ((MenuItem)def.getWidget("FontXSmall")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
       		 basic.setFontSize(fontSizesConstants[1]);
       		 sizeLabel.setText("X Small");
        	}
       });
        ((MenuItem)def.getWidget("FontSmall")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
       		 basic.setFontSize(fontSizesConstants[2]);
       		sizeLabel.setText("Small");
        	}
       });
        ((MenuItem)def.getWidget("FontMedium")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
       		 basic.setFontSize(fontSizesConstants[3]);
       		sizeLabel.setText("Medium");
        	}
       });
        ((MenuItem)def.getWidget("FontLarge")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
       		 basic.setFontSize(fontSizesConstants[4]);
       		sizeLabel.setText("Large");
       	}
       });
        ((MenuItem)def.getWidget("FontXLarge")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
       		 basic.setFontSize(fontSizesConstants[5]);
       		sizeLabel.setText("X Large");
       	}
       });
        ((MenuItem)def.getWidget("FontXXLarge")).addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
       		 basic.setFontSize(fontSizesConstants[6]);
       		sizeLabel.setText("XX Large");
       	}
       });
        fontLabel = (Label)def.getWidget("fontLabel");
        sizeLabel = (Label)def.getWidget("sizeLabel");
      // We only use these listeners for updating status, so don't hook them up
      // unless at least basic editing is supported.
      richText.addClickHandler(listener);
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
      AbsolutePanel ap = new AbsolutePanel();
      ap.setStyleName(img);
      ab.setWidget(ap);
      ab.addClickHandler(listener);
      return ab;
  }

  private AppButton createToggleButton(String img, String action) {
    AppButton ab = new AppButton();
    ab.setAction(action);
    AbsolutePanel ap = new AbsolutePanel();
    ap.setStyleName(img);
    ab.setWidget(ap);
    ab.addClickHandler(listener);
    ab.setToggle(true);
    return ab;
  }
  
  public void enable(boolean enabled) {
	  
  }
}

