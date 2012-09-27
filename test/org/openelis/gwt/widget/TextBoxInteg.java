package org.openelis.gwt.widget;

import java.util.Date;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.data.QueryData;

public class TextBoxInteg extends IntegrationTest {

	
	@Override
	public String getModuleName() {
		return "org.openelis.gwt.Widget";
	}
	
	public void testSimple() {
		assertTrue(true);
	}
	
	public void testCase() {
		TextBox<String> test = new TextBox<String>();
		test.setEnabled(true);
		
		test.setCase(TextBase.Case.MIXED);
		test.setText("dREfddRE");
		blur(test);
		assertEquals("dREfddRE",test.getText());
		
		test.setCase(TextBase.Case.LOWER);
		test.setText("dREfddRE");
		blur(test);
		assertEquals("drefddre",test.getText());
		
		test.setCase(TextBase.Case.UPPER);
		test.setText("dREfddRE");
		blur(test);
		assertEquals("DREFDDRE",test.getText());
		
	}
	
	public void testNull() {
		TextBox<Integer> testInt = new TextBox<Integer>();
		testInt.setHelper(new IntegerHelper());
		TextBox<Double>  testDoub = new TextBox<Double>();
		testDoub.setHelper(new DoubleHelper());
		TextBox<String>  testString = new TextBox<String>();
		TextBox<Datetime> testDate = new TextBox<Datetime>();
		testDate.setHelper(new DateHelper());
		
		testInt.setValue(null);
		assertNull(testInt.getValue());
		
		testDoub.setValue(null);
		assertNull(testDoub.getValue());
		
		testString.setValue(null);
		assertNull(testString.getValue());
		
		testDate.setValue(null);
		assertNull(testDate.getValue());
		
	}
	
	public void testRequired() {
		TextBox<Integer> testInt = new TextBox<Integer>();
		TextBox<Double>  testDoub = new TextBox<Double>();
		TextBox<String>  testString = new TextBox<String>();
		TextBox<Datetime> testDate = new TextBox<Datetime>();
		
		testInt.setRequired(true);
		testInt.setEnabled(true);
		testInt.setText("");
		blur(testInt);
		assertTrue(testInt.getValidateExceptions().size() > 0);
		
		testDoub.setRequired(true);
		testDoub.setEnabled(true);
		testDoub.setText("");
		blur(testDoub);
		assertTrue(testDoub.getValidateExceptions().size() > 0);
		
		testString.setRequired(true);
		testString.setEnabled(true);
		testString.setText("");
		blur(testString);
		assertTrue(testString.getValidateExceptions().size() > 0);
		
		testDate.setRequired(true);
		testDate.setEnabled(true);
		testDate.setText("");
		blur(testDate);
		assertTrue(testDoub.getValidateExceptions().size() > 0);
		
	}
	
	public void testInteger() {
		TextBox<Integer> testInt = new TextBox<Integer>();
		testInt.setHelper(new IntegerHelper());
		testInt.setEnabled(true);
		
		testInt.setText("4");
		blur(testInt);
		assertEquals(new Integer(4), testInt.getValue());
		
		testInt.setText("4.0");
		blur(testInt);
		assertTrue(testInt.hasExceptions());
		
		testInt.setText("4");
		blur(testInt);
		assertFalse(testInt.hasExceptions());
		
		testInt.setText("fd");
		blur(testInt);
		assertTrue(testInt.hasExceptions());
		
		testInt.setText("");
		blur(testInt);
		assertNull(testInt.getValue());
		assertFalse(testInt.hasExceptions());
	}
	
	public void testDouble() {
		TextBox<Double> test = new TextBox<Double>();
		test.setHelper(new DoubleHelper());
		test.setEnabled(true);
		
		test.setText("4");
		blur(test);
		assertEquals(new Double(4.0), test.getValue());
		
		test.setText("4.0");
		blur(test);
		assertEquals(new Double(4.0), test.getValue());
				
		test.setText("fd");
		blur(test);
		assertTrue(test.hasExceptions());
		
		test.setText("4.0");
		blur(test);
		assertFalse(test.hasExceptions());
		
		test.setText("");
		blur(test);
		assertNull(test.getValue());
		assertFalse(test.hasExceptions());
	}
	
	public void testDate() {
		TextBox<Datetime> test = new TextBox<Datetime>();
		DateHelper helper = new DateHelper();
		helper.setBegin(Datetime.YEAR);
		helper.setEnd(Datetime.DAY);
		test.setHelper(helper);
		test.setEnabled(true);

		Datetime valid = Datetime.getInstance(Datetime.YEAR,Datetime.MINUTE, new Date(112,2,12));
		test.setMask("9999-99-99");
		
		test.setText("2012-03-12");
		blur(test);
		assertEquals(valid, test.getValue());
		
		test.setText("djkjkjsd");
		blur(test);
		assertTrue(test.hasExceptions());
				
		test.setText("");
		blur(test);
		assertFalse(test.hasExceptions());
		assertNull(test.getValue());
		
		pressKey(test.textbox,'2');
		assertEquals("2",test.getText());
		
		pressKey(test.textbox,'0');
		assertEquals("20",test.getText());
		
		pressKey(test.textbox,'1');
		assertEquals("201",test.getText());
		
		pressKey(test.textbox,'2');
		assertEquals("2012",test.getText());
		
		pressKey(test.textbox,'f');
		assertEquals("2012",test.getText());
		
		pressKey(test.textbox,'0');
		assertEquals("2012-0",test.getText());
		
		pressKey(test.textbox,'3');
		assertEquals("2012-03",test.getText());
		
		pressKey(test.textbox,'1');
		assertEquals("2012-03-1",test.getText());
		
		pressKey(test.textbox,'2');
		assertEquals("2012-03-12",test.getText());
		
		pressKey(test.textbox,'3');
		assertEquals("2012-03-12",test.getText());
		
		blur(test);
		assertEquals(valid,test.getValue());
		
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("2012-03-1 ", test.getText());
		
		setCursorPos(test.textbox,9);
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("2012-03-  ",test.getText());
		
		setCursorPos(test.textbox,8);
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("2012-03-  ",test.getText());
		
		setCursorPos(test.textbox,7);
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("2012-0 -  ",test.getText());

		setCursorPos(test.textbox,6);
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("2012-  -  ",test.getText());

		setCursorPos(test.textbox,5);
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("2012-  -  ",test.getText());

		setCursorPos(test.textbox,4);
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("201 -  -  ",test.getText());

		setCursorPos(test.textbox,3);
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("20  -  -  ",test.getText());

		setCursorPos(test.textbox,2);
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("2   -  -  ",test.getText());
		
		setCursorPos(test.textbox,1);
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("",test.textbox.getText());
		test.setText("");
	}
	
	public void testMask() {
		TextBox<String> test;
		
		test = new TextBox<String>();
		test.setEnabled(true);
		test.setMask("9-9");
				
		pressKey(test.textbox,'3');
		assertEquals("3",test.getText());
		
		pressKey(test.textbox,'r');
		assertEquals("3",test.getText());
		
		pressKey(test.textbox,'3');
		assertEquals("3-3",test.getText());	
		
		pressKey(test.textbox,'3');
		assertEquals("3-3",test.getText());
		
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("3- ", test.getText());
		
		setCursorPos(test.textbox,2);
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("3- ",test.getText());

		setCursorPos(test.textbox,1);
		pressKey(test.textbox,KeyCodes.KEY_BACKSPACE);
		assertEquals("",test.textbox.getText());
		
		blur(test);
		
		assertEquals(null, test.getValue());
		
	}
	
	public void testQuery() {
		TextBox<String> test = new TextBox<String>();
		test.setEnabled(true);
		test.setQueryMode(true);
		
		test.setText("a* | b*");
		blur(test);
		assertEquals("a* | b*",((QueryData)test.getQuery()).getQuery());
		assertFalse(test.hasExceptions());
		test.setText("a* |");
		blur(test);
		assertTrue(test.hasExceptions());
	}
	
	public void testQueryInt() {
		TextBox<Integer> test = new TextBox<Integer>();
		IntegerHelper helper = new IntegerHelper();
		test.setHelper(helper);
		test.setEnabled(true);
		test.setQueryMode(true);
		
		test.setText(">4 | <10");
		blur(test);
		assertEquals(">4 | <10",((QueryData)test.getQuery()).getQuery());
		assertFalse(test.hasExceptions());
		test.setText("frrf");
		blur(test);
		assertTrue(test.hasExceptions());
	}
	
	public void testQueryDouble() {
		TextBox<Double> test = new TextBox<Double>();
		DoubleHelper helper = new DoubleHelper();
		test.setHelper(helper);
		test.setEnabled(true);
		test.setQueryMode(true);
		
		test.setText(">4.0 | <10.0");
		blur(test);
		assertEquals(">4.0 | <10.0",((QueryData)test.getQuery()).getQuery());
		assertFalse(test.hasExceptions());
		test.setText("sdfsdf");
		blur(test);
		assertTrue(test.hasExceptions());
	}
	
	public void testQueryDate() {
		TextBox<Datetime> test = new TextBox<Datetime>();
		DateHelper helper = new DateHelper();
		helper.setBegin(Datetime.YEAR);
		helper.setEnd(Datetime.DAY);
		test.setHelper(helper);
		test.setEnabled(true);
		test.setMask("9999-99-99");
		test.setQueryMode(true);
		
		test.setText(">2012-03-01");
		blur(test);
		assertEquals(">2012-03-01",((QueryData)test.getQuery()).getQuery());
		assertFalse(test.hasExceptions());
		test.setText(">2012-fd-01");
		blur(test);
		assertTrue(test.hasExceptions());
		
	}
	

}
