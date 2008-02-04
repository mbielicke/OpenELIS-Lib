package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.Label;
/**
 * This widget is used by the DragSelectWidget to 
 * drag items between the two selection boxes.
 * @author tschmidt
 *
 */
public class DragWidget extends Label {
    public boolean selected;
    public Object value;

    public DragWidget(String label, Object value, boolean selected) {
        super(label);
        setWordWrap(false);
        setStyleName("DragItem");
        this.value = value;
        this.selected = selected;
    }

    public DragWidget(DragWidget drag) {
        super(drag.getText());
        setWordWrap(false);
        setStyleName("DragItem");
        this.selected = drag.selected;
        this.value = drag.value;
    }
}
