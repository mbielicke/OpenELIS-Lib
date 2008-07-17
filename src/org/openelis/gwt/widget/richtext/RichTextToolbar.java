/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openelis.gwt.widget.richtext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.widget.AutoCompleteDropdown;

/**
 * A sample toolbar for use with {@link RichTextArea}. It provides a simple UI
 * for all rich text formatting, dynamically displayed only for the available
 * functionality.
 */
public class RichTextToolbar extends Composite {

  /**
   * This {@link ImageBundle} is used for all the button icons. Using an image
   * bundle allows all of these images to be packed into a single image, which
   * saves a lot of HTTP requests, drastically improving startup time.
   */
  public interface Images extends ImageBundle {

    AbstractImagePrototype bold();

    AbstractImagePrototype createLink();

    AbstractImagePrototype hr();

    AbstractImagePrototype indent();

    AbstractImagePrototype insertImage();

    AbstractImagePrototype italic();

    AbstractImagePrototype justifyCenter();

    AbstractImagePrototype justifyLeft();

    AbstractImagePrototype justifyRight();

    AbstractImagePrototype ol();

    AbstractImagePrototype outdent();

    AbstractImagePrototype removeFormat();

    AbstractImagePrototype removeLink();

    AbstractImagePrototype strikeThrough();

    AbstractImagePrototype subscript();

    AbstractImagePrototype superscript();

    AbstractImagePrototype ul();

    AbstractImagePrototype underline();
  }


  /**
   * We use an inner EventListener class to avoid exposing event methods on the
   * RichTextToolbar itself.
   */
  private class EventListener implements ClickListener, ChangeListener,
      KeyboardListener {

    public void onChange(Widget sender) {
      if (sender == backColors) {
        basic.setBackColor((String)backColors.getSelectedValue());
        backColors.setSelectedIndex(0);
      } else if (sender == foreColors) {
        basic.setForeColor((String)foreColors.getSelectedValue());
        foreColors.setSelectedIndex(0);
      } else if (sender == fonts) {
        basic.setFontName((String)fonts.getSelectedValue());
        fonts.setSelectedIndex(0);
      } else if (sender == fontSizes) {
        basic.setFontSize(fontSizesConstants[Integer.parseInt((String)fontSizes.getSelectedValue())]);
        fontSizes.setSelectedIndex(0);
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

  private Images images = (Images) GWT.create(Images.class);
  private EventListener listener = new EventListener();

  private RichTextArea richText;
  private RichTextArea.BasicFormatter basic;
  private RichTextArea.ExtendedFormatter extended;

  private VerticalPanel outer = new VerticalPanel();
  private HorizontalPanel topPanel = new HorizontalPanel();
  private HorizontalPanel bottomPanel = new HorizontalPanel();
  private ToggleButton bold;
  private ToggleButton italic;
  private ToggleButton underline;
  private ToggleButton subscript;
  private ToggleButton superscript;
  private ToggleButton strikethrough;
  private PushButton indent;
  private PushButton outdent;
  private PushButton justifyLeft;
  private PushButton justifyCenter;
  private PushButton justifyRight;
  private PushButton hr;
  private PushButton ol;
  private PushButton ul;
  private PushButton insertImage;
  private PushButton createLink;
  private PushButton removeLink;
  private PushButton removeFormat;

  private AutoCompleteDropdown backColors;
  private AutoCompleteDropdown foreColors;
  private AutoCompleteDropdown fonts;
  private AutoCompleteDropdown fontSizes;

  /**
   * Creates a new toolbar that drives the given rich text area.
   * 
   * @param richText the rich text area to be controlled
   */
  public RichTextToolbar(RichTextArea richText) {
    this.richText = richText;
    this.basic = richText.getBasicFormatter();
    this.extended = richText.getExtendedFormatter();

    outer.add(topPanel);
    outer.add(bottomPanel);
    topPanel.setWidth("100%");
    bottomPanel.setWidth("100%");

    initWidget(outer);
    setStyleName("gwt-RichTextToolbar");
    richText.addStyleName("hasRichTextToolbar");

    if (basic != null) {
      topPanel.add(bold = createToggleButton(images.bold(), "Bold"));
      topPanel.add(italic = createToggleButton(images.italic(), "Italic"));
      topPanel.add(underline = createToggleButton(images.underline(),
          "Underline"));
      topPanel.add(subscript = createToggleButton(images.subscript(),
          "Subscript"));
      topPanel.add(superscript = createToggleButton(images.superscript(),
          "Superscript"));
      topPanel.add(justifyLeft = createPushButton(images.justifyLeft(),
          "Justify Left"));
      topPanel.add(justifyCenter = createPushButton(images.justifyCenter(),
          "JustifyCenter"));
      topPanel.add(justifyRight = createPushButton(images.justifyRight(),
          "JustifyRight"));
    }

    if (extended != null) {
      topPanel.add(strikethrough = createToggleButton(images.strikeThrough(),
          "Strike Through"));
      topPanel.add(indent = createPushButton(images.indent(), "Indent"));
      topPanel.add(outdent = createPushButton(images.outdent(), "Outdent"));
      topPanel.add(hr = createPushButton(images.hr(), "HR"));
      topPanel.add(ol = createPushButton(images.ol(), "OL"));
      topPanel.add(ul = createPushButton(images.ul(), "UL"));
      topPanel.add(insertImage = createPushButton(images.insertImage(),
          "Insert Image"));
      topPanel.add(createLink = createPushButton(images.createLink(),
          "Create Link"));
      topPanel.add(removeLink = createPushButton(images.removeLink(),
          "Remove Link"));
      topPanel.add(removeFormat = createPushButton(images.removeFormat(),
          "Remove Format"));
    }

    if (basic != null) {
      bottomPanel.add(backColors = createColorList("Background"));
      bottomPanel.add(foreColors = createColorList("Foreground"));
      bottomPanel.add(fonts = createFontList());
      bottomPanel.add(fontSizes = createFontSizes());

      // We only use these listeners for updating status, so don't hook them up
      // unless at least basic editing is supported.
      richText.addKeyboardListener(listener);
      richText.addClickListener(listener);
    }
  }

  private AutoCompleteDropdown createColorList(String caption) {
    AutoCompleteDropdown lb = new AutoCompleteDropdown();
    lb.cat = null;
    lb.addChangeListener(listener);
    DataModel model = new DataModel();
    model.add(new StringObject(caption),new StringObject(""));
    model.add(new StringObject("White"), new StringObject("white"));
    model.add(new StringObject("Black"), new StringObject("black"));
    model.add(new StringObject("Red"), new StringObject("red"));
    model.add(new StringObject("Green"),new StringObject("green"));
    model.add(new StringObject("Yellow"), new StringObject("yellow"));
    model.add(new StringObject("Blue"), new StringObject("blue"));
    lb.setModel(model);
    return lb;
  }

  private AutoCompleteDropdown createFontList() {
    AutoCompleteDropdown lb = new AutoCompleteDropdown();
    lb.addChangeListener(listener);
    DataModel model = new DataModel();
    model.add(new StringObject("Font"),new StringObject(""));
    model.add(new StringObject("Normal"), new StringObject(""));
    model.add(new StringObject("Times New Roman"), new StringObject("Times New Roman"));
    model.add(new StringObject("Arial"), new StringObject("Arial"));
    model.add(new StringObject("Courier New"), new StringObject("Courier New"));
    model.add(new StringObject("Georgia"), new StringObject("Georgia"));
    model.add(new StringObject("Trebuchet"), new StringObject("Trebuchet"));
    model.add(new StringObject("Verdana"), new StringObject("Verdana"));
    lb.setModel(model);
    return lb;
  }

  private AutoCompleteDropdown createFontSizes() {
    AutoCompleteDropdown lb = new AutoCompleteDropdown();
    lb.addChangeListener(listener);
    DataModel model = new DataModel();
    model.add(new StringObject("Size"),new StringObject("0"));
    model.add(new StringObject("XX Small"),new StringObject("1"));
    model.add(new StringObject("X Small"),new StringObject("2"));
    model.add(new StringObject("Small"),new StringObject("3"));
    model.add(new StringObject("Medium"),new StringObject("4"));
    model.add(new StringObject("Large"),new StringObject("5"));
    model.add(new StringObject("X Large"),new StringObject("6"));
    model.add(new StringObject("XX Large"),new StringObject("7"));
    lb.setModel(model);
    return lb;
  }

  private PushButton createPushButton(AbstractImagePrototype img, String tip) {
    PushButton pb = new PushButton(img.createImage());
    pb.addClickListener(listener);
    pb.setTitle(tip);
    return pb;
  }

  private ToggleButton createToggleButton(AbstractImagePrototype img, String tip) {
    ToggleButton tb = new ToggleButton(img.createImage());
    tb.addClickListener(listener);
    tb.setTitle(tip);
    return tb;
  }

  /**
   * Updates the status of all the stateful buttons.
   */
  private void updateStatus() {
    if (basic != null) {
      bold.setDown(basic.isBold());
      italic.setDown(basic.isItalic());
      underline.setDown(basic.isUnderlined());
      subscript.setDown(basic.isSubscript());
      superscript.setDown(basic.isSuperscript());
    }

    if (extended != null) {
      strikethrough.setDown(extended.isStrikethrough());
    }
  }
}

