package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.ui.Image;
/**
 * This class is deprecated and should be deleted
 * @author tschmidt
 *
 */
public class DragImage extends Image {
    public boolean selected;
    public Object value;

    public DragImage(String url, Object value, boolean selected) {
        super(url);
        setStyleName("DragItem");
        this.value = value;
        this.selected = selected;
    }

    public DragImage(DragImage drag) {
        super(drag.getUrl());
        setStyleName("DragItem");
        this.selected = drag.selected;
        this.value = drag.value;
    }
}
