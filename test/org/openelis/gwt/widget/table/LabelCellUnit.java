package org.openelis.gwt.widget.table;

import java.util.Date;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.widget.DateHelper;
import org.openelis.gwt.widget.DoubleHelper;
import org.openelis.gwt.widget.IntegerHelper;
import org.openelis.gwt.widget.Label;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.junit.client.GWTTestCase;

public class LabelCellUnit extends GWTTestCase {

	Label<String> stringEditor;
	Label<Integer> intEditor;
	Label<Double> doubleEditor;
	Label<Datetime> dateEditor;
	LabelCell test; 

	
	@Override
	public String getModuleName() {
		return "org.openelis.gwt.Widget";
	}
	
	@Override
	protected void gwtSetUp() throws Exception {
		super.gwtSetUp();
		stringEditor = new Label<String>();
		
		intEditor = new Label<Integer>();
		intEditor.setHelper(new IntegerHelper());
		
		doubleEditor = new Label<Double>();
		doubleEditor.setHelper(new DoubleHelper());
		
		dateEditor = new Label<Datetime>();
		dateEditor.setHelper(new DateHelper());
		
	}
	
	
	public void testDisplay() {
		test = new LabelCell(stringEditor);
		
	    assertEquals("",test.display(null));
	    assertEquals("5",test.display(5));
	    assertEquals("sdf",test.display("sdf"));
	    
	    test = new LabelCell(intEditor);
	    
	    assertEquals("",test.display(null));
	    assertEquals("5",test.display(5));
	    assertEquals("sdf",test.display("sdf"));
	    assertEquals("5.0",test.display(5.0));
	    
	    test = new LabelCell(doubleEditor);
	    
	    assertEquals("",test.display(null));
	    assertEquals("5",test.display(5));
	    assertEquals("5.0",test.display(5.0));
	    assertEquals("sdf",test.display("sdf"));
	    
	    test = new LabelCell(dateEditor);
	    
	    assertEquals("",test.display(null));
	    assertEquals("2012-05-15",test.display(Datetime.getInstance(Datetime.YEAR,Datetime.DAY,new Date("2012/05/15"))));
	    assertEquals("5",test.display(5));
	    assertEquals("5.0",test.display(5.0));
	    assertEquals("sdf",test.display("sdf"));
	}
	
	public void testValidate() {
		test = new LabelCell(intEditor);
		
		assertTrue(test.validate(5).isEmpty());
		assertFalse(test.validate("5").isEmpty());
		
		
		test = new LabelCell(doubleEditor);
		
		assertTrue(test.validate(5.0).isEmpty());
		assertFalse(test.validate("5.0").isEmpty());
		
		test = new LabelCell(dateEditor);
		
		assertTrue(test.validate(Datetime.getInstance()).isEmpty());
		assertFalse(test.validate("334f335").isEmpty());
	}
	
}
