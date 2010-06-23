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

public class ScrollBar extends ScrollPanel implements HasScrollBarHandlers {

    private AbsolutePanel ap;

    private boolean       horizontal;

    public ScrollBar() {
        final ScrollBar source = this;
        ap = new AbsolutePanel();
        DOM.setStyleAttribute(getElement(), "overflowX", "hidden");
        ap.setWidth("15px");
        ap.setHeight("15px");
        setWidth("18px");
        setHeight("18px");
        setStyleName("TableVertScroll");
        setWidget(ap);
        addScrollHandler(new ScrollHandler() {
            public void onScroll(ScrollEvent event) {
                ScrollBarEvent.fire(source);
            }
        });
    }

    public ScrollBar(boolean horizontal) {
        this();
        this.horizontal = horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public void adjust(int viewSize, int scrollSize) {

        if (horizontal) {
            setWidth(Util.addUnits(viewSize));
            ap.setWidth(Util.addUnits(scrollSize));
        } else {
            setHeight(Util.addUnits(viewSize));
            ap.setHeight(Util.addUnits(scrollSize));
        }
    }


    public void scrollTo(int position) {
        setScrollPosition(position);
    }

    public HandlerRegistration addScrollBarHandler(ScrollBarHandler handler) {
        return addHandler(handler, ScrollBarEvent.getType());
    }

    
    public int getScrollPosition() {
        if(horizontal)
            return getHorizontalScrollPosition();
     
        return super.getScrollPosition();
    }

}
