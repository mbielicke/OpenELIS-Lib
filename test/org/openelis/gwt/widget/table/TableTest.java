package org.openelis.gwt.widget.table;

import java.util.ArrayList;

import org.openelis.gwt.widget.IntegerHelper;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.UnitTest;
import org.openelis.gwt.widget.table.Column;
import org.openelis.gwt.widget.table.Row;
import org.openelis.gwt.widget.table.Table;
import org.openelis.gwt.widget.table.TextBoxCell;

import com.google.gwt.event.dom.client.KeyCodes;

public class TableTest extends UnitTest {
	
	Table test;
	
	@Override
	public String getModuleName() {
		return "org.openelis.gwt.Widget";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		super.gwtSetUp();
		
		test = new Table();
		Column col = test.addColumn();
		col.setWidth(100);
		TextBox<String> textbox = new TextBox<String>();
		col.setCellRenderer(new TextBoxCell<String>(textbox));
		TextBox<Integer> integer = new TextBox<Integer>();
		integer.setHelper(new IntegerHelper());
		col = test.addColumn();
		col.setWidth(100);
		col.setCellRenderer(new TextBoxCell<Integer>(integer));
		textbox = new TextBox<String>();
		col = test.addColumn();
		col.setWidth(100);
		col.setCellRenderer(new TextBoxCell<String>(textbox));
		test.setVisibleRows(10);
		test.setEnabled(true);
	}
	
	public void testSimple() {
		assertTrue(true);
	}
	
	/**
	 * Add a Simple model and call table to confirm correct values
	 * are returned by getValueAt() methods
	 */
	public void testAddModel() {
		ArrayList<Row> model = new ArrayList<Row>();
		model.add(new Row("Alpha",5,"Beta"));
		model.add(new Row("Gamma",7,"Delta"));
		model.add(new Row("Epsilon",8,"Rho"));
		test.setModel(model);
		
		assertEquals(3,test.getRowCount());
		assertEquals("Alpha", test.getValueAt(0, 0));
		assertEquals(new Integer(7),test.getValueAt(1, 1));
		
	}
	
	/**
	 * Edit a cell and ensure and the cell finishes editing and the
	 * correct value is in the model;
	 */
	public void testCellEdit() {
		testAddModel();
		clickCell(test,0,0);
		
		assertTrue(test.isAnyRowSelected());
		assertEquals(0,test.getSelectedRow());
		assertTrue(test.isEditing());
		
		TextBox<String> editor = (TextBox<String>)test.getColumnWidget(0);
		editor.setText("Omega");
		
		blur(editor);
		
		assertFalse(test.isEditing());
		assertEquals("Omega",test.getValueAt(0, 0));
	}
	
	/**
	 * Test the use of Tab and directional keys to enusre correct navigation
	 * through a table
	 */
	public void testKeys() {
		testAddModel();
		
		// Set focus to table and hit enter. 0,0 should be editing
		test.setFocus(true);
		pressKey(test,KeyCodes.KEY_ENTER);
		assertTrue(test.isAnyRowSelected());
		assertEquals(0,test.getSelectedRow());
		assertTrue(test.isEditing());
		assertEquals(0,test.editingCol);
		
		// Press Tab and ensure 0,1 is editing
		pressKey(test,KeyCodes.KEY_TAB);
		assertEquals(0,test.editingRow);
		assertEquals(1,test.editingCol);
		
		// Press Tab and ensure 0,2 is editing
		pressKey(test,KeyCodes.KEY_TAB);
		assertEquals(0,test.editingRow);
		assertEquals(2,test.editingCol);
		
		// Press Tab and ensure 1,0 is editing
		pressKey(test,KeyCodes.KEY_TAB);
		assertEquals(1,test.editingRow);
		assertEquals(0,test.editingCol);
		
		// Press Arrow Down and endure 2,0 is editing
		pressKey(test,KeyCodes.KEY_DOWN);
		assertEquals(2,test.editingRow);
		assertEquals(0,test.editingCol);
		
		// Press Arrow Right and ensure 2,0 is still editing
		pressKey(test,KeyCodes.KEY_RIGHT);
		assertEquals(2,test.editingRow);
		assertEquals(0,test.editingCol);
		
		// Press Arrow Up and ensure 1,0 is editing
		pressKey(test,KeyCodes.KEY_UP);
		assertEquals(1,test.editingRow);
		assertEquals(0,test.editingCol);
		
		// Press Arrow Left and and ensure 1,0 is still editing
		pressKey(test,KeyCodes.KEY_LEFT);
		assertEquals(1,test.editingRow);
		assertEquals(0,test.editingCol);
		
	}
	
	
	
}
