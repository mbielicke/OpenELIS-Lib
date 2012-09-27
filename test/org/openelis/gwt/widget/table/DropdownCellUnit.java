package org.openelis.gwt.widget.table;

import java.util.ArrayList;

import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.IntegerHelper;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.StringHelper;

import com.google.gwt.junit.client.GWTTestCase;

public class DropdownCellUnit extends GWTTestCase {
	
	DropdownCell test;
	Dropdown<Integer> intEditor;
	Dropdown<String> stringEditor;
	
	
	@Override
	public String getModuleName() {
		return "org.openelis.gwt.Widget";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		ArrayList<Item<String>>  smodel;
		ArrayList<Item<Integer>> imodel;
		
		intEditor = new Dropdown<Integer>();
		intEditor.setPopupContext(new Table.Builder(10).column(new Column.Builder(100).build()).build());
		imodel = new ArrayList<Item<Integer>>();
		imodel.add(new Item<Integer>(null,""));
		imodel.add(new Item<Integer>(1,"Item 1"));
		imodel.add(new Item<Integer>(2,"Item 2"));
		imodel.add(new Item<Integer>(3,"Item 3"));
		
		intEditor.setModel(imodel);
		intEditor.setHelper(new IntegerHelper());
		
		stringEditor = new Dropdown<String>();
		stringEditor.setPopupContext(new Table.Builder(10).column(new Column.Builder(100).build()).build());
		smodel = new ArrayList<Item<String>>();
		smodel.add(new Item<String>(null,""));
		smodel.add(new Item<String>("1","Item 1"));
		smodel.add(new Item<String>("2","Item 2"));
		smodel.add(new Item<String>("3","Item 3"));
		
		stringEditor.setModel(smodel);
		stringEditor.setHelper(new StringHelper());
		
	}
	
	public void testDisplay() {
		test = new DropdownCell(intEditor);
		assertEquals("",test.display(null));
		assertEquals("Item 1",test.display(1));
		assertEquals("1",test.display("1"));
		assertEquals("asdasd",test.display("asdasd"));
		assertEquals("4",test.display(4));
		
		test = new DropdownCell(stringEditor);
		assertEquals("",test.display(null));
		assertEquals("Item 1",test.display("1"));
		assertEquals("1",test.display(1));
		assertEquals("asdasd",test.display("asdasd"));
		assertEquals("4",test.display("4"));
	}
	
	public void testValidate() {
		test = new DropdownCell(intEditor);
		assertTrue(test.validate(5).isEmpty());
		assertFalse(test.validate("5").isEmpty());
		
		test = new DropdownCell(stringEditor);
		assertTrue(test.validate("5").isEmpty());
		assertFalse(test.validate(5).isEmpty());
	}

}
