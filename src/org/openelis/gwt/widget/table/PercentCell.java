package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.PercentBar;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLTable;

/**
 * This class will display a PercentBar for the value passed into the the Table cell. 
 *
 */
public class PercentCell implements CellRenderer<Double> {
	
	/**
	 * This is the widget used to show Percent in the cell
	 */
	private PercentBar editor;
	
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
	public String display(Double value) {
		return NumberFormat.getFormat("###0.0").format(value)+"%";
	}

	/**
	 * Gets the HTML to display the Percentage from the editor and sets it into the cell
	 */
	@Override
	public void render(HTMLTable table, int row, int col, Double value) {
		editor.setPercent(value);
		table.setHTML(row,col,DOM.getInnerHTML(editor.getElement()));
	}

	/**
	 * Blanks out the table cell since this is a display only cell and cannot accept Query input
	 */
	@Override
	public void renderQuery(HTMLTable table, int row, int col, QueryData qd) {
		table.setText(row,col,"");
	}

}
