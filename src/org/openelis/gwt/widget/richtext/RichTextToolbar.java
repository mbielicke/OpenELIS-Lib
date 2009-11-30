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

import org.openelis.gwt.widget.AppButton;
import org.openelis.gwt.widget.AppButton.ButtonState;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
	private class EventListener implements ClickHandler, KeyUpHandler {

		public void onClick(ClickEvent event) { 
			if (event.getSource() == bold) 
				formatter.toggleBold();
			else if (event.getSource() == italic) 
				formatter.toggleItalic();
			else if (event.getSource() == underline) 
				formatter.toggleUnderline();
			else if (event.getSource() == subscript) 
				formatter.toggleSubscript();
			else if (event.getSource() == superscript) 
				formatter.toggleSuperscript();
			else if (event.getSource() == strikethrough) 
				formatter.toggleStrikethrough();
			else if (event.getSource() == indent)
				formatter.rightIndent();
			else if (event.getSource() == outdent) 
				formatter.leftIndent();
			else if (event.getSource() == justifyLeft) 
				formatter.setJustification(RichTextArea.Justification.LEFT);
			else if(event.getSource() == justifyCenter)
				formatter.setJustification(RichTextArea.Justification.CENTER);
			else if (event.getSource() == justifyRight)
				formatter.setJustification(RichTextArea.Justification.RIGHT);
			else if (event.getSource() == removeLink)
				formatter.removeLink();
			else if (event.getSource() == hr)
				formatter.insertHorizontalRule();
			else if (event.getSource() == ol)
				formatter.insertOrderedList();
			else if (event.getSource() == ul)
				formatter.insertUnorderedList();
			else if (event.getSource() == removeFormat)
				formatter.removeFormat();
			else if (event.getSource() == richText)
				checkToggleStates();
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
			if(formatter.isBold() && bold.getState() != ButtonState.PRESSED)
				bold.setState(ButtonState.PRESSED);
			else if(!formatter.isBold() && bold.getState() == ButtonState.PRESSED)
				bold.setState(ButtonState.UNPRESSED);
			if(formatter.isItalic() && italic.getState() != ButtonState.PRESSED)
				italic.setState(ButtonState.PRESSED);
			else if(!formatter.isItalic() && italic.getState() == ButtonState.PRESSED)
				italic.setState(ButtonState.UNPRESSED);
			if(formatter.isUnderlined() && underline.getState() != ButtonState.PRESSED)
				underline.setState(ButtonState.PRESSED);
			else if(!formatter.isUnderlined() && underline.getState() == ButtonState.PRESSED)
				underline.setState(ButtonState.UNPRESSED);
			if(formatter.isSubscript() && subscript.getState() != ButtonState.PRESSED)
				subscript.setState(ButtonState.PRESSED);
			else if(!formatter.isSubscript() && subscript.getState() == ButtonState.PRESSED)
				subscript.setState(ButtonState.UNPRESSED);
			if(formatter.isSuperscript() && superscript.getState() != ButtonState.PRESSED)
				superscript.setState(ButtonState.PRESSED);
			else if(!formatter.isSuperscript() && superscript.getState() == ButtonState.PRESSED)
				superscript.setState(ButtonState.UNPRESSED);
			if(formatter.isStrikethrough() && strikethrough.getState() != ButtonState.PRESSED)
				strikethrough.setState(ButtonState.PRESSED);
			else if(!formatter.isStrikethrough() && strikethrough.getState() == ButtonState.PRESSED)
				strikethrough.setState(ButtonState.UNPRESSED);
		}

	}

	private EventListener listener = new EventListener();

	private RichTextArea richText;
	private RichTextArea.Formatter formatter;

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
	private AppButton removeLink;
	private AppButton removeFormat;

	/**
	 * Creates a new toolbar that drives the given rich text area.
	 * 
	 * @param richText the rich text area to be controlled
	 */
	public RichTextToolbar(RichTextArea richText) {

		this.richText = richText;
		this.formatter = richText.getFormatter();

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

		topPanel.add(bold = createToggleButton("Bold", "Bold"));
		topPanel.add(italic = createToggleButton("Italic", "Italic"));
		topPanel.add(underline = createToggleButton("Underline","Underline"));
		topPanel.add(subscript = createToggleButton("Subscript","Subscript"));
		topPanel.add(superscript = createToggleButton("Superscript","Superscript"));
		topPanel.add(justifyLeft = createButton("JustifyLeft","Justify Left"));
		topPanel.add(justifyCenter = createButton("JustifyCenter","JustifyCenter"));
		topPanel.add(justifyRight = createButton("JustifyRight","JustifyRight"));

		topPanel.add(strikethrough = createToggleButton("StrikeThrough","Strike Through"));
		topPanel.add(indent = createButton("Indent", "Indent"));
		topPanel.add(outdent = createButton("Outdent", "Outdent"));
		topPanel.add(hr = createButton("HR", "HR"));
		topPanel.add(ol = createButton("OL", "OL"));
		topPanel.add(ul = createButton("UL", "UL"));
		topPanel.add(removeFormat = createButton("RemoveFormat","Remove Format"));
		
	}
	
	private AppButton createButton(String img, String action) {
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
		AppButton ab = createButton(img,action);
		ab.setToggle(true);
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

