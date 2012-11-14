package org.openelis.gwt.widget.table;

import java.util.ArrayList;
import java.util.Iterator;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.PercentBar;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class will display a PercentBar for the value passed into the the Table cell. 
 *
 */
public class PercentCell implements CellRenderer,IsWidget,HasWidgets.ForIsWidget {
	
	/**
	 * This is the widget used to show Percent in the cell
	 */
	private PercentBar editor;
	
	public PercentCell() {
		
	}
	/**
	 * Constructor that takes the editor as an argument
	 * @param editor
	 */
	public PercentCell(PercentBar editor) {
		this.editor = editor;
	}

	/**
	 * Returns the value of the percentage formatted as a string
	 */
	@Override
	public String display(Object value) {
		return NumberFormat.getFormat("###0.0").format((Double)value)+"%";
	}

	/**
	 * Gets the HTML to display the Percentage from the editor and sets it into the cell
	 */
	@Override
	public void render(HTMLTable table, int row, int col, Object value) {
		editor.setPercent((Double)value);
		table.setHTML(row,col,DOM.getInnerHTML(editor.getElement()));
	}

	/**
	 * Blanks out the table cell since this is a display only cell and cannot accept Query input
	 */
	@Override
	public void renderQuery(HTMLTable table, int row, int col, QueryData qd) {
		table.setText(row,col,"");
	}

	@Override
	public ArrayList<LocalizedException> validate(Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(Widget w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Widget> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Widget w) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void add(IsWidget w) {
		assert w instanceof PercentBar;
		
		this.editor = editor;
	}

	@Override
	public boolean remove(IsWidget w) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}

}
