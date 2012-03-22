package org.openelis.gwt.widget;

import org.openelis.gwt.widget.table.TableTest;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

public class OpenELISLibTestSuite extends GWTTestSuite {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test Suite for all Lib Widgets");
		suite.addTestSuite(TextBoxTest.class);
		suite.addTestSuite(DropdownTest.class);
		suite.addTestSuite(TableTest.class);
		return suite;
	}
}
