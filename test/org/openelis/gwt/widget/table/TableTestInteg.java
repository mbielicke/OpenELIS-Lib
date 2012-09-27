package org.openelis.gwt.widget.table;

import java.util.ArrayList;

import org.openelis.gwt.widget.IntegerHelper;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.IntegrationTest;
import org.openelis.gwt.widget.table.Column;
import org.openelis.gwt.widget.table.Row;
import org.openelis.gwt.widget.table.Table;
import org.openelis.gwt.widget.table.TextBoxCell;

import com.google.gwt.event.dom.client.KeyCodes;

public class TableTestInteg extends IntegrationTest {
	
	Table test;
	
	@Override
	public String getModuleName() {
		return "org.openelis.gwt.Widget";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		super.gwtSetUp();
		
		TextBox<Integer> integer = new TextBox<Integer>();
		integer.setHelper(new IntegerHelper());
		
		test = new Table.Builder(10)
		                .column(new Column.Builder(100)
		                                  .renderer(new TextBoxCell(new TextBox<String>()))
		                                  .build())
		                .column(new Column.Builder(100)
		                                  .renderer(new TextBoxCell(integer))
		                                  .build())
		                .column(new Column.Builder(100)
		                                  .renderer(new TextBoxCell(new TextBox<String>()))
		                                  .build())
		                .build();
		
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
	
	public void testException() {
		TextBox<Integer> editor;
		testAddModel();
		
		// Edit cell and cause an invalid numeric exception
		clickCell(test, 0, 1);
		editor = (TextBox<Integer>)test.getColumnWidget(1);
		editor.setText("fdsdf");
		blur(editor);
		assertFalse(test.isEditing());
		assertTrue(test.hasExceptions());
		assertTrue(test.hasExceptions(0, 1));
		assertEquals("fdsdf",test.view.flexTable.getText(0, 1));
		assertEquals("fdsdf",test.getValueAt(0, 1));
		assertTrue(test.view.flexTable.getCellFormatter().getStyleName(0, 1).contains("InputError"));
		
		//Edit cell to a valid integer and ensure exception is cleared
		clickCell(test,0,1);
		editor.setText("4");
		blur(editor);
		assertFalse(test.isEditing());
		assertFalse(test.hasExceptions());
		assertFalse(test.hasExceptions(0, 1));
		assertEquals("4",test.view.flexTable.getText(0, 1));
		assertEquals(new Integer(4), test.getValueAt(0, 1));
		assertFalse(test.view.flexTable.getCellFormatter().getStyleName(0, 1).contains("InputError"));

	}
	
	
	
}
