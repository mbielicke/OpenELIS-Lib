package org.openelis.gwt.widget.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


import org.junit.Ignore;
import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.DateHelper;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.IntegerHelper;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.Label;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.calendar.Calendar;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class TableTestUnit extends GWTTestCase {
	
	Table test;
	
	@Override
	public String getModuleName() {
		return "org.openelis.gwt.Widget";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		super.gwtSetUp();
		
		TextBox<Integer> tbInt = new TextBox<Integer>();
		tbInt.setHelper(new IntegerHelper());
		
		test = new Table.Builder(10)
		                .column(new Column.Builder(100)
		                                  .renderer(new TextBoxCell(tbInt))
		                                  .build())
		                .column(new Column.Builder(100)
		                                  .renderer(new TextBoxCell(new TextBox<String>()))
		                                  .build())
		                .column(new Column.Builder(100)
		                                  .renderer(new TextBoxCell(new TextBox<String>()))
		                                  .build())
		                .build();
		
		test.view.onAttach();
	}
	
	public void testSimple() {
		
	}
	
	@Ignore
	public void testSetModel() {
		test.setModel(null);
		assertEquals(0,test.view.flexTable.getRowCount());
		
		ArrayList<Row> model = new ArrayList<Row>();
		for(int i = 0; i < 10; i++)
			model.add(new Row(null,null,null));
		test.setModel(model);
		
		assertEquals(10,test.view.flexTable.getRowCount());
		
		for(int i = 0; i < 10; i++)
			model.add(new Row(null,null,null));
		
		test.setModel(model);
		
		assertEquals(10,test.view.flexTable.getRowCount());
		assertEquals(0,test.view.firstVisibleRow);
	
		for(int i = 0; i < 15; i++)
			model.remove(0);
		
		test.setModel(model);
		
		assertEquals(5,test.view.flexTable.getRowCount());
		
		test.setModel(null);
		
		assertEquals(0,test.view.flexTable.getRowCount());
			
	}
	
	public void testApplyFilters() {
		ArrayList<Row> model = getModel();
		
		test.getColumnAt(0).setFilterable(true);
		test.getColumnAt(1).setFilterable(true);
		test.getColumnAt(2).setFilterable(true);
		
		test.setModel(model);
		
		ArrayList<Filter> filters = new ArrayList<Filter>();
		filters.add(test.new UniqueFilter());
		filters.get(0).setColumn(0);
		filters.add(test.new UniqueFilter());
		filters.get(1).setColumn(1);
		
		test.applyFilters(filters);
		
		assertEquals(5,test.view.flexTable.getRowCount());
		
		filters.get(0).getChoices(model).get(0).setSelected(true);
		
		test.applyFilters(filters);
		
		assertEquals(1,test.view.flexTable.getRowCount());
		assertEquals(1,test.getValueAt(0,0));
		
		filters.get(0).getChoices(model).get(0).setSelected(false);
		
		test.applyFilters(filters);
		
		assertEquals(5,test.view.flexTable.getRowCount());
		
		filters.get(1).getChoices(model).get(1).setSelected(true);
		filters.get(1).getChoices(model).get(3).setSelected(true);
		
		test.applyFilters(filters);
		
		assertEquals(2,test.view.flexTable.getRowCount());
		assertEquals("B",test.getValueAt(0,1));
		assertEquals("D",test.getValueAt(1,1));
		
		filters.get(0).getChoices(model).get(1).setSelected(true);
		
		test.applyFilters(filters);
		
		assertEquals(1,test.view.flexTable.getRowCount());
		assertEquals("B",test.getValueAt(0,1));
		
		filters.get(0).unselectAll();
		filters.get(0).getChoices(model).get(4).setSelected(true);
		
		test.applyFilters(filters);
		
		assertEquals(0,test.view.flexTable.getRowCount());
		
		test.applyFilters(null);
		
		assertEquals(5,test.view.flexTable.getRowCount());
		
	}
	
	public void testSort() {
		test.getColumnAt(0).setSortable(true);
		test.getColumnAt(1).setSortable(true);
		test.getColumnAt(2).setSortable(true);
		
		test.setModel(getModel());
		
		test.applySort(0,Table.SORT_DESCENDING,null);
		
		assertEquals(5,test.getValueAt(0,0));
		assertEquals(1,test.getValueAt(4,0));
		
		test.applySort(1,Table.SORT_ASCENDING,null);
		
		assertEquals("A", test.getValueAt(0, 1));
		assertEquals("E",test.getValueAt(4,1));
		
	}
	
	public void testIndexing() {
		test.getColumnAt(0).setSortable(true);
		test.getColumnAt(1).setSortable(true);
		test.getColumnAt(2).setSortable(true);
		
		test.setModel(getModel());
		
		test.applySort(0,Table.SORT_DESCENDING,null);
		
		assertEquals(4,test.convertViewIndexToModel(0));
	    assertEquals(0,test.convertModelIndexToView(4));
		
	    test.removeRowAt(2);
	    
	    assertEquals(3,test.convertViewIndexToModel(0));
	    assertEquals(0,test.convertModelIndexToView(3));
	    
	    test.addRowAt(2, new Row(3,"3",null));
	    
	    assertEquals(4,test.convertViewIndexToModel(0));
	    assertEquals(0,test.convertModelIndexToView(4));
	    
	    test.applySort(0, Table.SORT_ASCENDING,null);
	    
	    assertEquals(0,test.convertViewIndexToModel(0));
	    assertEquals(0,test.convertModelIndexToView(0));
	}
	
	public void testGetRowCount() {
		assertEquals(0,test.getRowCount());
		
		test.setModel(getModel());
		
		assertEquals(5,test.getRowCount());
		
		ArrayList<Filter> filters = new ArrayList<Filter>();
		filters.add(test.new UniqueFilter());
		filters.get(0).setColumn(0);
		filters.get(0).getChoices(test.getModel()).get(0).setSelected(true);
		
		test.applyFilters(filters);
		
		assertEquals(1,test.getRowCount());
		
	}
	
	public void testIsMultipleRowsSelected() {
		test.setModel(getModel());
		test.setAllowMultipleSelection(true);
		test.selectRowAt(0);
		test.selectRowAt(1);
		assertEquals(true,test.isMultipleRowsSelected());
	}
	
	public void testAddColumns() {
		test.setModel(getModel());
		
		test.addColumn();
		
		assertEquals(4, test.getColumnCount());
		assertEquals(4, test.view.flexTable.getCellCount(0));
		assertNull(test.getValueAt(0, 3));
		
		
		test.addColumnAt(1);
		
		assertEquals(5,test.getColumnCount());
		assertEquals(5,test.view.flexTable.getCellCount(0));
		assertEquals("A",test.getValueAt(0, 2));
		assertEquals(null,test.getValueAt(0,1));
	}
	
	public void testRemoveColumns() {
		test.setModel(getModel());
		
		test.removeColumnAt(0);
		
		assertEquals(2, test.getColumnCount());
		assertEquals(2, test.getModel().get(0).getCells().size());
		assertEquals(2, test.view.flexTable.getCellCount(0));
		assertEquals("A",test.getValueAt(0, 0));
	}
	
	public void testAddRows() {
		test.setModel(getModel());
		
		test.addRow();
		
		assertEquals(6,test.view.flexTable.getRowCount());
		assertEquals(6,test.getRowCount());
		
		test.addRowAt(2);
		
		assertEquals(7,test.view.flexTable.getRowCount());
		assertEquals(7,test.getRowCount());
		assertEquals("C",test.getValueAt(3,1));
		
	}
	
	public void testRemoveRows() {
		test.setModel(getModel());
		
		test.removeRowAt(2);
		
		assertEquals(4,test.getRowCount());
		assertEquals(4,test.view.flexTable.getRowCount());
		assertEquals("D",test.getValueAt(2, 1));
		
		test.removeAllRows();
		assertEquals(0,test.getRowCount());
		assertEquals(0,test.view.flexTable.getRowCount());
	}
	
	public void testSelection() {
		test.setModel(getModel());
		
		test.selectRowAt(0);
		
		assertEquals(0,test.getSelectedRow());
		assertTrue(Arrays.deepEquals(new Integer[]{0},test.getSelectedRows()));
		assertTrue(test.isAnyRowSelected());
		assertTrue(test.isRowSelected(0));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(0).contains("Selection"));
		
		test.selectRowAt(1);
		assertEquals(1,test.getSelectedRow());
		assertTrue(Arrays.deepEquals(new Integer[]{1},test.getSelectedRows()));
		assertTrue(test.isAnyRowSelected());
		assertTrue(test.isRowSelected(1));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(1).contains("Selection"));
		assertFalse(test.view.flexTable.getRowFormatter().getStyleName(0).contains("Selection"));
		
		test.setAllowMultipleSelection(true);
		test.selectRowAt(1);
		test.selectRowAt(2);
		
		assertTrue(Arrays.deepEquals(new Integer[]{1,2},test.getSelectedRows()));
		assertTrue(test.isMultipleRowsSelected());
		assertTrue(test.isAnyRowSelected());
		assertTrue(test.isRowSelected(1));
		assertTrue(test.isRowSelected(2));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(1).contains("Selection"));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(2).contains("Selection"));
		
		test.selectRowAt(3);
		
		assertEquals(3,test.getSelectedRow());
		assertTrue(Arrays.deepEquals(new Integer[]{3},test.getSelectedRows()));
		assertFalse(test.isMultipleRowsSelected());
		assertTrue(test.isAnyRowSelected());
		assertTrue(test.isRowSelected(3));
		assertFalse(test.isRowSelected(2));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(3).contains("Selection"));
		assertFalse(test.view.flexTable.getRowFormatter().getStyleName(1).contains("Selection"));
		assertFalse(test.view.flexTable.getRowFormatter().getStyleName(2).contains("Selection"));
	
		test.unselectAll();
		assertFalse(test.isMultipleRowsSelected());
		assertFalse(test.isAnyRowSelected());
		assertEquals(-1,test.getSelectedRow());
		assertTrue(Arrays.deepEquals(new Integer[]{},test.getSelectedRows()));
		assertFalse(test.view.flexTable.getRowFormatter().getStyleName(3).contains("Selection"));
		
		test.selectAll();
		assertTrue(test.isMultipleRowsSelected());
		assertTrue(test.isAnyRowSelected());
		assertEquals(0,test.getSelectedRow());
		assertTrue(Arrays.deepEquals(new Integer[]{0,1,2,3,4},test.getSelectedRows()));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(0).contains("Selection"));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(1).contains("Selection"));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(2).contains("Selection"));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(3).contains("Selection"));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(4).contains("Selection"));
		
		test.unselectRowAt(2);
		assertTrue(test.isMultipleRowsSelected());
		assertTrue(test.isAnyRowSelected());
		assertEquals(0,test.getSelectedRow());
		assertTrue(Arrays.deepEquals(new Integer[]{0,1,3,4},test.getSelectedRows()));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(0).contains("Selection"));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(1).contains("Selection"));
		assertFalse(test.view.flexTable.getRowFormatter().getStyleName(2).contains("Selection"));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(3).contains("Selection"));
		assertTrue(test.view.flexTable.getRowFormatter().getStyleName(4).contains("Selection"));
	}
	
	public void testSetValueAt() {
		test.setModel(getModel());
		
		test.setValueAt(0,0,6);
		
		assertEquals(6,test.getValueAt(0,0));
		assertEquals("6",test.view.flexTable.getText(0, 0));
		
    	test.setValueAt(0, 0, "fsdfsdf");
		
    	assertEquals("fsdfsdf",test.getValueAt(0, 0));
    	assertEquals("fsdfsdf",test.view.flexTable.getText(0, 0));
    	assertTrue(test.hasExceptions(0,0));
    	assertTrue(test.view.flexTable.getCellFormatter().getStyleName(0, 0).contains("InputError"));
    	
    	test.setValueAt(0,0,5);
    	assertEquals(5,test.getValueAt(0,0));
    	assertEquals("5",test.view.flexTable.getText(0, 0));
    	assertFalse(test.hasExceptions(0,0));
    	assertFalse(test.view.flexTable.getCellFormatter().getStyleName(0, 0).contains("InputError"));
    	
	}
	
	public void testSetRowAt() {
		test.setModel(getModel());
		
		test.setRowAt(2,new Row(7,"F",null));
		
		assertEquals(5,test.getRowCount());
		assertEquals(7,test.getValueAt(2,0));
		assertEquals("F",test.view.flexTable.getText(2, 1));
	}
	
	public void testStartEditing() {
		
		test.setModel(getModel());
		
		assertFalse(test.startEditing(0, 0));
		assertFalse(test.isAnyRowSelected());
		
		test.setEnabled(true);
		
		assertTrue(test.startEditing(0,0));
		assertEquals(0,test.getSelectedRow());
		assertEquals(test.getColumnWidget(0),((AbsolutePanel)test.view.flexTable.getWidget(0, 0)).getWidget(0));
		
	}
	
	public void testFinishEditing() {
		test.setModel(getModel());
		
		test.setEnabled(true);
		
		test.startEditing(0,0);
		assertTrue(test.isEditing());
		((TextBox)test.getColumnWidget(0)).setText("10");
		test.finishEditing();
		assertFalse(test.isEditing());
		assertEquals(10,test.getValueAt(0,0));
		
		test.startEditing(0,0);
		assertTrue(test.isEditing());
		
		((TextBox)test.getColumnWidget(0)).setText("ASDA");
		test.finishEditing();
		assertFalse(test.isEditing());
		assertEquals("ASDA",test.getValueAt(0, 0));
		assertTrue(test.hasExceptions(0, 0));
		assertTrue(test.view.flexTable.getCellFormatter().getStyleName(0,0).contains("InputError"));

	}
	
	public void testScrollToVisible() {
		test.setModel(getModel());
		
		for(int i = 0; i < 15; i++)
			test.addRowAt(0);
		
		assertEquals(20,test.getRowCount());
		
		assertTrue(test.scrollToVisible(15));

		// Calling renderview expliculty for test only
		test.renderView(-1, -1);
		
     	assertEquals(6,test.view.firstVisibleRow);
	    assertEquals("A",test.view.flexTable.getText(9, 1));

	}
	
	public void testScrollBy() {
		test.setModel(getModel());
		
		for(int i = 0; i < 15; i++)
			test.addRowAt(0);
		
		test.scrollBy(6);
		
		// Calling renderview expliculty for test only
		test.renderView(-1, -1);
		
     	assertEquals(6,test.view.firstVisibleRow);
	    assertEquals("A",test.view.flexTable.getText(9, 1));
	}
	
	public void testResize() {
		test.getColumnAt(2).setWidth(200);
		assertEquals(400,test.view.flexTable.getOffsetWidth());
	}
	
	public void testRefreshCell() {
		test.setModel(getModel());
		
		test.getModel().get(0).cells.set(0,10);
		test.refreshCell(0, 0);
		assertEquals("10",test.view.flexTable.getText(0,0));
	}
	
	public void testQueryMode() {
		test.setModel(getModel());
		test.setEnabled(true);
		test.setQueryMode(true);
		
		assertEquals(1,test.getRowCount());
		assertEquals(1,test.view.flexTable.getRowCount());
		assertNull(test.getValueAt(0, 0));
		
		test.setQueryMode(false);
		
		assertEquals(0,test.getRowCount());
		assertEquals(0,test.view.flexTable.getRowCount());
		assertNull(test.getModel());
	}
	
	public void testGetQuery() {
		test.setModel(getModel());
		test.setEnabled(true);
		test.setQueryMode(true);
		
		test.startEditing(0, 0);
		((TextBox)test.getColumnWidget(0)).setText("> 1");
		test.finishEditing();
		
		QueryData[] qds = (QueryData[])test.getQuery();
		
		assertNotNull(qds);
		assertEquals(1,qds.length);
		assertEquals(QueryData.Type.INTEGER, qds[0].getType());
		assertEquals("> 1",qds[0].getQuery());
		
	}
	
	public void testAddException() {
		test.setModel(getModel());
		test.setEnabled(true);
		test.addException(0, 0, new LocalizedException("test"));
		
		assertTrue(test.hasExceptions());
		assertTrue(test.hasExceptions(0, 0));
		assertTrue(test.view.flexTable.getCellFormatter().getStyleName(0, 0).contains("InputError"));
	}
	
	public void testClearException() {
		testAddException();
		
		test.clearEndUserExceptions(0, 0);
		
		assertFalse(test.hasExceptions());
		assertFalse(test.hasExceptions(0,0));
		assertFalse(test.view.flexTable.getCellFormatter().getStyleName(0,0).contains("InputError"));
	}
	
	public void testValidate() {
		test.setModel(getModel());
		test.getColumnAt(2).setRequired(true);
		assertTrue(test.hasExceptions());
		assertTrue(test.view.flexTable.getCellFormatter().getStyleName(0,2).contains("InputError"));
	}
	
	public void testTimeCell() {
		test = new Table.Builder(10)
        .column(new Column.Builder(100)
                          .renderer(new TimeCell())
                          .build()).build();
		test.view.onAttach();
		
		test.addRow();
		test.setEnabled(true);
		
		test.startEditing(0, 0);
		((TextBox)test.getColumnWidget(0)).setText("10");
		test.finishEditing();
		assertFalse(test.isEditing());
		assertEquals(10.0,((Double)test.getValueAt(0,0)).doubleValue());
		assertEquals("10:0",test.view.flexTable.getText(0, 0));
		
		test.startEditing(0,0);
		((TextBox)test.getColumnWidget(0)).setText("AA");
		test.finishEditing();
		assertFalse(test.isEditing());
		assertEquals("AA",(String)test.getValueAt(0,0));
		assertEquals("AA",test.view.flexTable.getText(0,0));
		assertTrue(test.hasExceptions());
		assertTrue(test.view.flexTable.getCellFormatter().getStyleName(0, 0).contains("InputError"));
		
		test.startEditing(0,0);
		((TextBox)test.getColumnWidget(0)).setText("");
		test.finishEditing();
		assertFalse(test.isEditing());
		assertNull(test.getValueAt(0,0));
		assertEquals("",test.view.flexTable.getText(0,0));
		assertFalse(test.hasExceptions());
		assertFalse(test.view.flexTable.getCellFormatter().getStyleName(0, 0).contains("InputError"));
	}
	
	public void testCalendarCell() {
		Calendar cal = new Calendar();
		
		DateHelper helper = new DateHelper();
		helper.setBegin(Datetime.YEAR);
		helper.setEnd(Datetime.DAY);
		
		cal.setHelper(helper);
		
		test = new Table.Builder(10)
        .column(new Column.Builder(100)
                          .renderer(new CalendarCell(cal))
                          .build()).build();
		test.view.onAttach();
		
		test.addRow();
		test.setEnabled(true);
		
		test.startEditing(0, 0);
		((Calendar)test.getColumnWidget(0)).setText("2012-05-14");
		test.finishEditing();
		assertFalse(test.isEditing());
		assertEquals(Datetime.getInstance(Datetime.YEAR,Datetime.DAY,new Date("2012/05/14")),((Datetime)test.getValueAt(0,0)));
		assertEquals("2012-05-14",test.view.flexTable.getText(0, 0));
		
		test.startEditing(0,0);
		((Calendar)test.getColumnWidget(0)).setText("AA");
		test.finishEditing();
		assertFalse(test.isEditing());
		assertEquals("AA",(String)test.getValueAt(0,0));
		assertEquals("AA",test.view.flexTable.getText(0,0));
		assertTrue(test.hasExceptions());
		assertTrue(test.view.flexTable.getCellFormatter().getStyleName(0, 0).contains("InputError"));
		
		test.startEditing(0,0);
		((Calendar)test.getColumnWidget(0)).setText("");
		test.finishEditing();
		assertFalse(test.isEditing());
		assertNull(test.getValueAt(0,0));
		assertEquals("",test.view.flexTable.getText(0,0));
		assertFalse(test.hasExceptions());
		assertFalse(test.view.flexTable.getCellFormatter().getStyleName(0, 0).contains("InputError"));
	}
	
	public void testDropdownCell() {
		Dropdown<Integer> drop = new Dropdown<Integer>();
		
		Table popup = new Table.Builder(10).column(new Column.Builder(100).renderer(new LabelCell(new Label<String>())).build()).build();
		
		drop.setPopupContext(popup);
		
		drop.setHelper(new IntegerHelper());
		
		ArrayList<Item<Integer>> model = new ArrayList<Item<Integer>>();
		model.add(new Item<Integer>(null,""));
		model.add(new Item<Integer>(1,"Item 1"));
		model.add(new Item<Integer>(2,"Item 2"));
		model.add(new Item<Integer>(3,"Item 3"));
		
		drop.setModel(model);
		
		test = new Table.Builder(10)
        .column(new Column.Builder(100)
                          .renderer(new DropdownCell(drop))
                          .build()).build();
		test.view.onAttach();
		
		test.addRow();
		test.setEnabled(true);
		
		test.setValueAt(0,0,"sdf");
		assertEquals("sdf",test.view.flexTable.getText(0, 0));
		assertTrue(test.hasExceptions());
		assertTrue(test.view.flexTable.getCellFormatter().getStyleName(0,0).contains("InputError"));
		
		test.setValueAt(0,0,1);
		assertEquals("Item 1", test.view.flexTable.getText(0,0));
		assertFalse(test.hasExceptions());
		assertFalse(test.view.flexTable.getCellFormatter().getStyleName(0,0).contains("InputError"));
	}
	
	public ArrayList<Row> getModel() {
		ArrayList<Row> model = new ArrayList<Row>();
		
		model.add(new Row(1,"A",null));
		model.add(new Row(2,"B",null));
		model.add(new Row(3,"C",null));
		model.add(new Row(4,"D",null));
		model.add(new Row(5,"E",null));
		
		return model;
		
	}
	

	

}
