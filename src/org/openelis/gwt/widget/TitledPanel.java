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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
/*
 * Copyright 2007 Google Inc.
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
package org.openelis.gwt.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

/**
 * A panel that wraps its contents in a border with a title that appears
 * in the upper left corner of the border.  This is an implementation
 * of the fieldset HTML element.
 */
public class TitledPanel extends SimplePanel {
  /**
   * Implementation class for TitledPanel.
   */
  public static class TitledPanelImpl {
	  
	 public void TitledPanel(Node node){
		 
	 }
    /**
     * Set the title of a fieldset element.
     */
    public void setTitle(Element fieldset, Element legend, Widget title) {
      if (title != null) {
        DOM.setInnerHTML(legend, DOM.getInnerHTML(title.getElement()));
        DOM.insertChild(fieldset, legend, 0);
      } else if (DOM.getParent(legend) != null) {
        DOM.removeChild(fieldset, legend);
      }
    }
  }

  /**
   * Implementation class for TitledPanel that handles Mozilla rendering
   * issues.
   */
  public static class TitledPanelImplMozilla extends TitledPanelImpl {
    public void setTitle(Element fieldset, Element legend, Widget title) {
      DOM.setStyleAttribute(fieldset, "display", "none");
      super.setTitle(fieldset, legend, title);
      DOM.setStyleAttribute(fieldset, "display", "");
    }
  }
  
  /**
   * The implementation instance.
   */
  private static TitledPanelImpl impl = (TitledPanelImpl) GWT.create(TitledPanelImpl.class);
  
  /**
   * The legend used as the title.
   */
  private Element legend;
  
  /**
   * The title.
   */
  private Widget title;
  
  /**
   * Constructor.
   * 
   * @param title the title to display
   */
  public TitledPanel(Widget title) {
    super(DOM.createElement("fieldset"));
    legend = DOM.createElement("legend");
    DOM.appendChild(getElement(), legend);
    setTitle(title);
  }
  
  /**
   * Constructor.
   * 
   * @param title the title to display
   */
  public TitledPanel() {
	  super(DOM.createElement("fieldset"));
	    legend = DOM.createElement("legend");
	    DOM.appendChild(getElement(), legend);
  }
  
  /**
   * Constructor.
   * 
   * @param title the title to display
   * @param w the widget to add to the panel
   */
  public TitledPanel(Widget title, Widget w) {
    this(title);
    setWidget(w);
  }
  
  /**
   * Get the current title.
   * 
   * @return the title of the panel
   */
  public Widget getTitleWidget() {
	    return this.title;
	  }
  
  /**
   * Set the title in the border.  Pass in null or an empty string
   * to remove the title completely, leaving just a box.
   * 
   * @param title the new title
   */
  public void setTitle(Widget title) {
    this.title = title;
    impl.setTitle(getElement(),legend, title);
  }
}
