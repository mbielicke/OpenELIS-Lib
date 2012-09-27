package org.openelis.gwt.widget;

import org.openelis.gwt.widget.table.TableTestInteg;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

public class OpenELISLibTestSuite extends GWTTestSuite {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test Suite for all Lib Widgets");
		suite.addTestSuite(TextBoxInteg.class);
		suite.addTestSuite(DropdownTest.class);
		suite.addTestSuite(TableTestInteg.class);
		return suite;
	}
}
