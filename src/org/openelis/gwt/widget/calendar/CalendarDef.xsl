
<!--
Exhibit A - UIRF Open-source Based Public Software License.
  
The contents of this file are subject to the UIRF Open-source Based
Public Software License(the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at
openelis.uhl.uiowa.edu
  
Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.
  
The Original Code is OpenELIS code.
  
The Initial Developer of the Original Code is The University of Iowa.
Portions created by The University of Iowa are Copyright 2006-2008. All
Rights Reserved.
  
Contributor(s): ______________________________________.
  
Alternatively, the contents of this file marked
"Separately-Licensed" may be used under the terms of a UIRF Software
license ("UIRF Software License"), in which case the provisions of a
UIRF Software License are applicable instead of those above. 
  -->

<xsl:stylesheet
  extension-element-prefixes="resource"
  version="1.0"
  xmlns:locale="xalan://java.util.Locale"
  xmlns:resource="xalan://org.openelis.util.UTFResource"
  xmlns:xalan="http://xml.apache.org/xalan"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="doc">
    <xsl:variable name="language" select="locale" />
    <xsl:variable name="props" select="props" />
    <xsl:variable name="constants" select="resource:getBundle(string('org.openelis.gwt.server.CalendarConstants'),locale:new(string($language)))" />
    <screen name="Calendar" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <FocusPanel key="CalFocus">
        <VerticalPanel style="CalendarWidget">
          <widget halign="center">
            <HorizontalPanel key="TimeBar" style="TimeBar" width="100%">
              <widget halign="center">
                <textbox begin="3" end="5" field="Date" key="time" pattern="HH:mm" width="45px" />
              </widget>
            </HorizontalPanel>
          </widget>
          <HorizontalPanel style="MonthBar">
            <button action="prevMonth" key="prevMonth" style="Button">
              <AbsolutePanel style="PreviousMonth" />
            </button>
            <label key="MonthDisplay" style="MonthDisplay"></label>
            <button action="monthSelect" key="monthSelect" style="Button">
              <AbsolutePanel style="MonthSelect" />
            </button>
            <button action="nextMonth" key="nextMonth" style="Button">
              <AbsolutePanel style="NextMonth" />
            </button>
          </HorizontalPanel>
          <VerticalPanel key="calContainer" width="100%"></VerticalPanel>
          <AbsolutePanel halign="center" style="TodayBar" width="100%">
            <button action="today" key="today" style="Button" width="75px">
              <text>Today</text>
            </button>
          </AbsolutePanel>
          <AbsolutePanel visible="false">
            <label key="month0" text="{resource:getString($constants,'month0')}" />
            <label key="month1" text="{resource:getString($constants,'month1')}" />
            <label key="month2" text="{resource:getString($constants,'month2')}" />
            <label key="month3" text="{resource:getString($constants,'month3')}" />
            <label key="month4" text="{resource:getString($constants,'month4')}" />
            <label key="month5" text="{resource:getString($constants,'month5')}" />
            <label key="month6" text="{resource:getString($constants,'month6')}" />
            <label key="month7" text="{resource:getString($constants,'month7')}" />
            <label key="month8" text="{resource:getString($constants,'month8')}" />
            <label key="month9" text="{resource:getString($constants,'month9')}" />
            <label key="month10" text="{resource:getString($constants,'month10')}" />
            <label key="month11" text="{resource:getString($constants,'month11')}" />
          </AbsolutePanel>
        </VerticalPanel>
      </FocusPanel>
    </screen>
  </xsl:template>
</xsl:stylesheet>
