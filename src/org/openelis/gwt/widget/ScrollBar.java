/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.widget;

import org.openelis.gwt.common.Util;
import org.openelis.gwt.event.HasScrollBarHandlers;
import org.openelis.gwt.event.ScrollBarEvent;
import org.openelis.gwt.event.ScrollBarHandler;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * This class is used to draw a scroll bar by itself that can be used to scroll other widgets
 * Specifically used in the Table and Tree View classes
 * @author tschmidt
 *
 */
public class ScrollBar extends ScrollPanel implements HasScrollBarHandlers {

	/**
	 * Widget used to cause the ScrollPanel to scroll the desired amount
	 */
    private AbsolutePanel ap;
    /**
     * This is the maximum amount the ScrollBar should be able to scroll
     */
    protected int scrollMax;
    /**
     * Flag to display the ScrollBar Horizontally
     */
    private boolean       horizontal;

    /**
     * No-arg constructor
     */
    public ScrollBar() {
        final ScrollBar source = this;
        ap = new AbsolutePanel();
        DOM.setStyleAttribute(getElement(), "overflowX", "hidden");
        ap.setWidth("18px");
        ap.setHeight("0px");
        setWidth("18px");
        setHeight("0px");
        
        setAlwaysShowScrollBars(true);
        setWidget(ap);
        addScrollHandler(new ScrollHandler() {
            public void onScroll(ScrollEvent event) {
                ScrollBarEvent.fire(source,getVerticalScrollPosition());
            }
        });
        scrollMax = -1;
    }

    /**
     * Constructor the sets the ScrollBar into Horizontal layout
     * @param horizontal
     */
    public ScrollBar(boolean horizontal) {
        this();
        this.horizontal = horizontal;
    }

    /**
     * Method to set the ScrollBar into Horizontal layout
     * @param horizontal
     */
    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    /**
     * This method will adjust the amount the ScrollBar is allowed to scroll
     * @param scrollMax
     */
    public void adjustScrollMax(int scrollMax) {
        if(scrollMax == this.scrollMax)
            return;
        
        this.scrollMax = scrollMax;
        if (horizontal) {
            ap.setWidth(Util.addUnits(scrollMax));
        } else {
            ap.setHeight(Util.addUnits(Math.max(scrollMax,15)));
        }
    }

    /**
     * Moves the ScrollBar to the passed position
     * @param position
     */
    public void scrollTo(int position) {
        setVerticalScrollPosition(position);
    }

    /**
     * Adds a handler to this scrollbar
     */
    public HandlerRegistration addScrollBarHandler(ScrollBarHandler handler) {
        return addHandler(handler, ScrollBarEvent.getType());
    }

    /**
     * Gets the current ScrollPostion of this ScrollBar
     */
    public int getScrollBarPosition() {
        if(horizontal)
            return getHorizontalScrollPosition();
     
        return getVerticalScrollPosition();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        DOM.setStyleAttribute(getElement(), "overflowX", "hidden");
    }
}
