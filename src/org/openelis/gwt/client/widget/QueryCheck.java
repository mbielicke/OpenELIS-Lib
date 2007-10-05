package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
/**
 * Query is supposed to be a 3 state checkbox to use in forms 
 * that query by checkboxes.  Needs work.
 * @author tschmidt
 *
 */
public class QueryCheck extends CheckBox implements ClickListener {

    private String value = null;

    public QueryCheck() {
        addClickListener(this);
        addStyleName("disabled");
    }

    public void onClick(Widget sender) {
        // TODO Auto-generated method stub
        if (value == null) {
            value = "N";
            setChecked(false);
            removeStyleName("disabled");
        } else if (value.equals("N")) {
            value = "Y";
            setChecked(true);
        } else if (value.equals("Y")) {
            value = null;
            setChecked(false);
            addStyleName("disabled");
        }

    }

}
