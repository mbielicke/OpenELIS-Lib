package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
/**
 * PasswordWidget combines 3 GWT PasswordTextBoxes for changing a user's
 * password.
 * @author tschmidt
 *
 */
public class PasswordWidget extends Composite {
    private PasswordTextBox oldPass = new PasswordTextBox();
    private PasswordTextBox newPass = new PasswordTextBox();
    private PasswordTextBox confirmPass = new PasswordTextBox();
    private Label oldError = new Label("");
    private Label newError = new Label("");
    private Label confirmError = new Label("");
    private FlexTable outer = new FlexTable();

    public PasswordWidget() {
        initWidget(outer);
        outer.setText(0, 0, "Old Password:");
        outer.setWidget(0, 1, oldPass);
        outer.setWidget(1, 1, oldError);
        outer.getRowFormatter().setVisible(1, false);
        outer.setText(2, 0, "New Password:");
        outer.setWidget(2, 1, newPass);
        outer.setWidget(3, 1, newError);
        outer.getRowFormatter().setVisible(3, false);
        outer.setText(4, 0, "Confirm Password:");
        outer.setWidget(4, 1, confirmPass);
        outer.setWidget(5, 1, confirmError);
        outer.getRowFormatter().setVisible(5, false);
    }

    public String getOldPass() {
        return oldPass.getText();
    }

    public String getNewPass() {
        return newPass.getText();
    }

    public String getConfirmPass() {
        return confirmPass.getText();
    }

    public void setOldError(String error) {
        oldError.setText(error);
        outer.getRowFormatter().setVisible(1, true);
    }

    public void setNewError(String error) {
        newError.setText(error);
        outer.getRowFormatter().setVisible(3, true);
    }

    public void setConfirmError(String error) {
        confirmError.setText(error);
        outer.getRowFormatter().setVisible(5, true);
    }

    public void clearErrors(String error) {
        oldError.setText("");
        newError.setText("");
        confirmError.setText("");
        outer.getRowFormatter().setVisible(1, false);
        outer.getRowFormatter().setVisible(3, false);
        outer.getRowFormatter().setVisible(5, false);
    }
}
