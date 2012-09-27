package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.Arrays;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.table.Column;
import org.openelis.gwt.widget.table.Table;

public class DropdownTest extends IntegrationTest {
	
	ArrayList<Item<Integer>> model;
	Dropdown<Integer> test;
	
	@Override
	public String getModuleName() {
		return "org.openelis.gwt.Widget";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		super.gwtSetUp();
		model = new ArrayList<Item<Integer>>();
		model.add(new Item<Integer>(null,""));
		model.add(new Item<Integer>(1,"Alabama"));
		model.add(new Item<Integer>(2,"Alaska"));
		model.add(new Item<Integer>(3,"Arizona"));
		model.add(new Item<Integer>(4,"Arkansas"));
		model.add(new Item<Integer>(5,"California"));
		model.add(new Item<Integer>(6,"Colorado"));
		model.add(new Item<Integer>(7,"Connecticut"));
		model.add(new Item<Integer>(8,"Delaware"));
		model.add(new Item<Integer>(9,"Florida"));
		model.add(new Item<Integer>(10,"Georgia"));
		model.add(new Item<Integer>(11,"Hawaii"));
		model.add(new Item<Integer>(12,"Idaho"));
		model.add(new Item<Integer>(13,"Illinois"));
		model.add(new Item<Integer>(14,"Indiana"));
		model.add(new Item<Integer>(15,"Iowa"));
		model.add(new Item<Integer>(16,"Kansas"));
		model.add(new Item<Integer>(17,"Kentucky"));
		model.add(new Item<Integer>(18,"Louisiana"));
		model.add(new Item<Integer>(19,"Maine"));
		model.add(new Item<Integer>(20,"Maryland"));
		model.add(new Item<Integer>(21,"Massachusetts"));
		model.add(new Item<Integer>(22,"Michigan"));
		model.add(new Item<Integer>(23,"Minnesota"));
		model.add(new Item<Integer>(24,"Mississippi"));
		model.add(new Item<Integer>(25,"Missouri"));
		model.add(new Item<Integer>(26,"Montana"));
		model.add(new Item<Integer>(27,"Nebraska"));
		model.add(new Item<Integer>(28,"Nevada"));
		model.add(new Item<Integer>(29,"New Hampshire"));
		model.add(new Item<Integer>(30,"New Jersey"));
		model.add(new Item<Integer>(31,"New Mexico"));
		model.add(new Item<Integer>(32,"New York"));
		model.add(new Item<Integer>(33,"North Carolina"));
		model.add(new Item<Integer>(34,"North Dakota"));
		model.add(new Item<Integer>(35,"Ohio"));
		model.add(new Item<Integer>(36,"Oklahoma"));
		model.add(new Item<Integer>(37,"Oregon"));
		model.add(new Item<Integer>(38,"Pennsylvania"));
		model.add(new Item<Integer>(39,"Rhode Island"));
		model.add(new Item<Integer>(40,"South Carolina"));
		model.add(new Item<Integer>(41,"South Dakota"));
		model.add(new Item<Integer>(42,"Tennessee"));
		model.add(new Item<Integer>(43,"Texas"));
		model.add(new Item<Integer>(44,"Utah"));
		model.add(new Item<Integer>(45,"Vermont"));
		model.add(new Item<Integer>(46,"Virginia"));
		model.add(new Item<Integer>(47,"Washington"));
		model.add(new Item<Integer>(48,"West Virginia"));
		model.add(new Item<Integer>(49,"Wisconsin"));
		model.add(new Item<Integer>(50,"Wyoming"));
		
		test = new Dropdown<Integer>();
		Table table = new Table.Builder(10).column(new Column.Builder(100).build()).build();
		test.setPopupContext(table);
		test.setModel(model);
		test.setEnabled(true);
	}
	
	public void testSimple() {
		assertTrue(true);
	}
	
	public void testNull() {
		test.setValue(null);
		assertNull(test.getValue());
	}
	
	public void testRequired() {
		test.setRequired(true);
		blur(test);
		assertTrue(test.hasExceptions());
		testSelection();
		assertFalse(test.hasExceptions());
	}
	
	public void testSelection() {
		click(test.button);
		//clickCell(test.getPopupContext(),1,0);
		assertFalse(test.popup.isShowing());
		blur(test);
		assertEquals(new Integer(1),test.getValue());
	}
	
	public void testUnselection() {
		testSelection();
		click(test.button);
		assertTrue(test.popup.isShowing());
		//clickCell(test.getPopupContext(),0,0);
		assertFalse(test.popup.isShowing());
		blur(test);
		assertEquals(null,test.getValue());
	}
	
	public void testKey() {
		pressKey(test,'i');
		assertEquals("Idaho",test.getDisplay());
		pressKey(test,'o');
		assertEquals("Iowa",test.getDisplay());
		blur(test);
		assertEquals(new Integer(15),test.getValue());
	}
	
	public void testUpDown() {
		pressKey(test,KeyCodes.KEY_DOWN);
		assertEquals("",test.getDisplay());
		pressKey(test,KeyCodes.KEY_DOWN);
		assertEquals("Alabama",test.getDisplay());
		pressKey(test,KeyCodes.KEY_DOWN);
		assertEquals("Alaska",test.getDisplay());
		pressKey(test,KeyCodes.KEY_UP);
		assertEquals("Alabama",test.getDisplay());
		blur(test);
		assertEquals(new Integer(1),test.getValue());
	}
	
	public void testBackSpace() {
		pressKey(test,'i');
		assertEquals("Idaho",test.getDisplay());
		pressKey(test,'o');
		assertEquals("Iowa",test.getDisplay());
		pressKey(test,'w');
		assertEquals("Iowa",test.getDisplay());
		pressKey(test,KeyCodes.KEY_BACKSPACE);
		assertEquals("",test.getDisplay());
		pressKey(test,'i');
		assertEquals("Idaho",test.getDisplay());
	}
	
	public void testMultSelect() {
		//test.setMultiSelect(true);
		click(test.button);
		//clickCell(test.getPopupContext(),1,0);
		assertTrue(test.popup.isShowing());
		//clickCell(test.getPopupContext(),15,1);
		assertTrue(test.popup.isShowing());
		test.popup.hide();
		assertEquals("Alabama, Iowa",test.getDisplay());
		blur(test);
		//assertEquals(Arrays.asList(new Integer[] {1,15}),test.getValues());
		click(test.button);
		click(test.checkAll);
		click(test.close);
		blur(test);
		//assertEquals(51,test.getValues().size());
		click(test.button);
		click(test.uncheckAll);
		click(test.close);
		blur(test);
		assertNull(test.getValue());
	}
	
	public void testQuery() {
		test.setQueryMode(true);
		click(test.button);
		//clickCell(test.getPopupContext(), 15, 0);
		//clickCell(test.getPopupContext(), 1,1);
		click(test.close);
		blur(test);
		assertEquals("1 | 15",((QueryData)test.getQuery()).getQuery());
		click(test.button);
		click(test.uncheckAll);
		click(test.close);
		blur(test);
		assertNull(test.getQuery());
	}

}
