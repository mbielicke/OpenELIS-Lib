package org.openelis.gwt.widget;

import java.util.Date;

import org.junit.Test;
import org.openelis.gwt.common.Datetime;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.junit.client.GWTTestCase;

public class TextBoxUnit extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "org.openelis.gwt.Widget";
	}

	/** 
	 * Test to ensure all properties are se correctly switching between QueryMode and 
	 * edit mode.
	 */
	@Test
	public void testSetQueryMode() {
		TextBox<String> test = new TextBox<String>();
		test.setEnabled(true);
		//Test no mask or maxLength
		test.setQueryMode(true);
		assertEquals(-1,test.textbox.getMaxLength());
		test.setQueryMode(false);
		assertEquals(-1,test.textbox.getMaxLength());
	}
	
	public void testSetQueryModeMaxLength() {
		TextBox<String> test = new TextBox<String>();
		test.setEnabled(true);
		test.setMaxLength(11);
		test.setQueryMode(true);
		assertEquals(255,test.textbox.getMaxLength());
		test.setQueryMode(false);
		assertEquals(11,test.textbox.getMaxLength());
	}
		
	public void testSetQueryModeMask() {	
		TextBox<String> test;
		
		test = new TextBox<String>();
		test.setEnabled(true);
		test.setMask("999-99-9999");
		test.setQueryMode(true);
		assertFalse(test.textbox.enforceMask);
		assertEquals(-1,test.textbox.getMaxLength());
		test.setQueryMode(false);
		assertTrue(test.textbox.enforceMask);
		assertEquals(-1,test.textbox.getMaxLength());
	}
	
	public void testSetValue() {
		TextBox<String> test = new TextBox<String>();
		test.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				assertEquals("new value",event.getValue());
			}
		});
		test.setValue("new value",true);
		assertEquals("new value",test.getText());
	}
	
	public void testSetValueNoFire() {
		TextBox<String> test = new TextBox<String>();
		test.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				fail("Should not have fired ValueChange");
			}
		});
		test.setValue("new value",false);
		assertEquals("new value",test.getText());
	}
	
	public void testSetValueNull() {
		TextBox<String> test = new TextBox<String>();
		test.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				fail("Should not have fired ValueChange");
			}
		});
		test.setValue(null,true);
		assertEquals("",test.getText());
	}
	
	public void testSetValueSame() {
		TextBox<String> test = new TextBox<String>();
		test.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				fail("Should not have fired ValueChange");
			}
		});
		test.setValue("new value",false);
		assertEquals("new value",test.getText());
		test.setValue("new value",true);
		assertEquals("new value",test.getText());
	}
	
	public void testSetValueClear() {
		TextBox<String> test = new TextBox<String>();
		test.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				assertNull(event.getValue());
			}
		});
		test.setValue("new value",false);
		assertEquals("new value",test.getText());
		test.setValue(null,true);
		assertEquals("",test.getText());
	}
	
	public void testSetFromInput() {
		TextBox<String> test = new TextBox<String>();
		test.setEnabled(true);
		test.setRequired(true);
		test.finishEditing();
		assertTrue(test.hasExceptions());
		assertNull(test.getValue());
		
		test.setText("something");
		test.finishEditing();
		assertFalse(test.hasExceptions());
		assertEquals("something",test.getValue());
	}
	
	public void testSetFromInputInteger() {
		TextBox<Integer> test = new TextBox<Integer>();
		test.setEnabled(true);
		test.setHelper(new IntegerHelper());
		test.setText("error");
		test.finishEditing();
		assertTrue(test.hasExceptions());
		assertNull(test.getValue());
		
		test.setText("4");
		test.finishEditing();
		assertFalse(test.hasExceptions());
		assertEquals(new Integer(4),test.getValue());
	}
	
	public void testSetFromInputDouble() {
		TextBox<Double> test = new TextBox<Double>();
		test.setEnabled(true);
		test.setHelper(new DoubleHelper());
		test.setText("error");
		test.finishEditing();
		assertTrue(test.hasExceptions());
		assertNull(test.getValue());
		
		test.setText("4.0");
		test.finishEditing();
		assertFalse(test.hasExceptions());
		assertEquals(new Double(4),test.getValue());
	}
	
	public void testSetFromInputDate() {
		TextBox<Datetime> test = new TextBox<Datetime>();
		test.setEnabled(true);
		DateHelper helper = new DateHelper();
		helper.setBegin(Datetime.YEAR);
		helper.setEnd(Datetime.DAY);
		helper.setPattern("yyyy-MM-dd");
		test.setHelper(helper);
		test.setText("error");
		test.finishEditing();
		assertTrue(test.hasExceptions());
		assertNull(test.getValue());
		
		test.setText("2012-03-20");
		test.finishEditing();
		assertFalse(test.hasExceptions());
		assertEquals(Datetime.getInstance(Datetime.YEAR, Datetime.DAY, new Date(112,2,20)),test.getValue());
	}
	
	public void testSetMask() {
		TextBox<String> test = new TextBox<String>();
		test.setMask("999-99-9999");
		assertNotNull(test.textbox.keyDown);
		assertTrue(test.textbox.enforceMask);
		assertEquals("999-99-9999",test.textbox.mask);
		assertEquals("   -  -    ",test.textbox.picture);
		
		test.setMask("XXX-XX-XXXX");
		assertNotNull(test.textbox.keyDown);
		assertTrue(test.textbox.enforceMask);
		assertEquals("XXX-XX-XXXX",test.textbox.mask);
		assertEquals("   -  -    ",test.textbox.picture);
		
		test.setMask(null);
		assertNull(test.textbox.keyDown);
		assertFalse(test.textbox.enforceMask);
		assertNull(test.textbox.mask);
		assertNull(test.textbox.picture);
	}
	
	boolean onValueCalled;
	public void testBlur() {
		TextBox<String> test = new TextBox<String>();
		test.setEnabled(true);
		
		onValueCalled = false;
		test.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
		 	public void onValueChange(ValueChangeEvent<String> event) {
				onValueCalled = true;
			}
		});
		
		test.setText("this my text");
		
		BlurEvent.fireNativeEvent(Document.get().createBlurEvent(), test);
		
		assertTrue(onValueCalled);
	}
	

}
