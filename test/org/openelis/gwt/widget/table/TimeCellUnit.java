package org.openelis.gwt.widget.table;

import java.util.Date;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.junit.client.GWTTestCase;

public class TimeCellUnit extends GWTTestCase {
	
	TimeCell test;
	
	@Override
	public String getModuleName() {
		return "org.openelis.gwt.Widget";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		test = new TimeCell();
	}
	
	public void testDisplay() {
		assertEquals("5:0",test.display(5.0));
		assertEquals("asdasd",test.display("asdasd"));
		assertEquals("2012-5-15",test.display(Datetime.getInstance(Datetime.YEAR,Datetime.DAY,new Date("2012/05/15"))));
	}
	
	public void testValidate() {
		assertTrue(test.validate(5.0).isEmpty());
		assertFalse(test.validate("asdasd").isEmpty());
	}
	
	public void testFinishEditing() {
		TextBox<String> editor = (TextBox<String>)test.getWidget();
		
		editor.setText("5:0");
		assertEquals(5.0,test.finishEditing());
		editor.setText("asdasd");
		assertEquals("asdasd",test.finishEditing());
		editor.setText("5:15");
		assertEquals(5.25,test.finishEditing());
	}
	
	

}
