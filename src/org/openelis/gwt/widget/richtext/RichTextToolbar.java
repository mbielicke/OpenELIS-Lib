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

import org.openelis.gwt.widget.Button;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RichTextArea;


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
			else if (event.getSource() == undo)
				formatter.undo();
			else if (event.getSource() == redo)
				formatter.redo();
			else if (event.getSource() == richText)
				checkToggleStates();
		}

		public void onKeyUp(KeyUpEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_RIGHT || 
					event.getNativeKeyCode() == KeyCodes.KEY_LEFT || 
					event.getNativeKeyCode() == KeyCodes.KEY_UP ||
					event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
				checkToggleStates();
			}
		}

		private void checkToggleStates() {
			if(formatter.isBold() && !bold.isPressed())
				bold.setPressed(true);
			else if(!formatter.isBold() && bold.isPressed())
				bold.setPressed(false);
			if(formatter.isItalic() && !italic.isPressed())
				italic.setPressed(true);
			else if(!formatter.isItalic() && italic.isPressed())
				italic.setPressed(false);
			if(formatter.isUnderlined() && !underline.isPressed())
				underline.setPressed(true);
			else if(!formatter.isUnderlined() && underline.isPressed())
				underline.setPressed(false);
			if(formatter.isSubscript() && !subscript.isPressed())
				subscript.setPressed(true);
			else if(!formatter.isSubscript() && subscript.isPressed())
				subscript.setPressed(false);
			if(formatter.isSuperscript() && !superscript.isPressed())
				superscript.setPressed(true);
			else if(!formatter.isSuperscript() && superscript.isPressed())
				superscript.setPressed(false);
			if(formatter.isStrikethrough() && !strikethrough.isPressed())
				strikethrough.setPressed(true);
			else if(!formatter.isStrikethrough() && strikethrough.isPressed())
				strikethrough.setPressed(false);
		}

	}

	private EventListener listener = new EventListener();

	private RichTextArea richText;
	private RichTextArea.Formatter formatter;

	private Grid outer = new Grid(2,1);
	private HorizontalPanel topPanel = new HorizontalPanel();
	private HorizontalPanel bottomPanel = new HorizontalPanel();
	private Button bold;
	private Button italic;
	private Button underline;
	private Button subscript;
	private Button superscript;
	private Button strikethrough;
	private Button indent;
	private Button outdent;
	private Button justifyLeft;
	private Button justifyCenter;
	private Button justifyRight;
	private Button hr;
	private Button ol;
	private Button ul;
	private Button removeLink;
	private Button removeFormat;
	private Button undo;
	private Button redo;

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
		AbsolutePanel ap = new AbsolutePanel();
		ap.setStyleName("ButtonDivider");
		topPanel.add(ap);
		topPanel.add(subscript = createToggleButton("Subscript","Subscript"));
		topPanel.add(superscript = createToggleButton("Superscript","Superscript"));
		ap = new AbsolutePanel();
		ap.setStyleName("ButtonDivider");
		topPanel.add(ap);
		topPanel.add(justifyLeft = createButton("JustifyLeft","Justify Left"));
		topPanel.add(justifyCenter = createButton("JustifyCenter","Justify Center"));
		topPanel.add(justifyRight = createButton("JustifyRight","Justify Right"));
		ap = new AbsolutePanel();
		ap.setStyleName("ButtonDivider");
		topPanel.add(ap);
		topPanel.add(indent = createButton("Indent", "Indent"));
		topPanel.add(outdent = createButton("Outdent", "Outdent"));
		ap = new AbsolutePanel();
		ap.setStyleName("ButtonDivider");
		topPanel.add(ap);
		topPanel.add(ol = createButton("OL", "Ordered List"));
		topPanel.add(ul = createButton("UL", "Unordered List"));
		ap = new AbsolutePanel();
		ap.setStyleName("ButtonDivider");
		topPanel.add(ap);
		topPanel.add(strikethrough = createToggleButton("StrikeThrough","Strike Through"));
		topPanel.add(hr = createButton("HR", "Horizontal Line"));
		ap = new AbsolutePanel();
		ap.setStyleName("ButtonDivider");
		topPanel.add(ap);
		topPanel.add(removeFormat = createButton("RemoveFormat","Remove Format"));
		topPanel.add(redo = createButton("redo","Redo"));
		topPanel.add(undo = createButton("undo","Undo"));
		
		
		
	}
	
	private Button createButton(String img, String title) {
		Button ab = new Button();
		ab.setTitle(title);
		ab.setStyleName("ButtonPanelButton");
		AbsolutePanel ap = new AbsolutePanel();
		ap.setStyleName(img);
		ab.setWidget(ap);
		ab.addClickHandler(listener);
		ab.setEnabled(true);
		return ab;
	}

	private Button createToggleButton(String img, String title) {		
		Button ab = createButton(img,title);
		ab.setToggles(true);
		return ab;
	}

	public void enable(boolean enabled) {

	}

	public void reset() {
		bold.setPressed(false);
		italic.setPressed(false);
		superscript.setPressed(false);
		subscript.setPressed(false);
		strikethrough.setPressed(false);
		underline.setPressed(false);
		richText.setHTML("");
	}
}

