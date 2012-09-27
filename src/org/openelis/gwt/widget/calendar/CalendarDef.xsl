
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
    <xsl:variable name="constants" select="resource:getBundle(string('org.openelis.gwt.constants.LibraryConstants'),locale:new(string($language)))" />
    <screen name="Calendar" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <FocusPanel key="CalFocus">
        <VerticalPanel class="libRes.calendar.CalendarWidget">
          <widget halign="center">
            <HorizontalPanel key="TimeBar" class="libRes.calendar.TimeBar" width="100%">
              <widget halign="center">
                <textbox begin="3" end="5" field="Date" key="time" pattern="HH:mm" width="45px" />
              </widget>
            </HorizontalPanel>
          </widget>
          <HorizontalPanel class="libRes.calendar.MonthBar">
            <button action="prevMonth" key="prevMonth" icon="libRes.calendar.PreviousMonth"/>
            <label key="MonthDisplay" class="libRes.calendar.MonthDisplay"></label>
            <button action="monthSelect" key="monthSelect" icon="libRes.calendar.MonthSelect"/>
            <button action="nextMonth" key="nextMonth" icon="libRes.calendar.NextMonth"/>
          </HorizontalPanel>
          <VerticalPanel key="calContainer" width="100%"></VerticalPanel>
          <AbsolutePanel halign="center" class="libRes.calendar.TodayBar" width="100%">
            <button action="today" key="today" text="Today" width="75px"/>
          </AbsolutePanel>
          <AbsolutePanel visible="false">
            <label key="month0" text="{resource:getString($constants,'cal.month0')}" />
            <label key="month1" text="{resource:getString($constants,'cal.month1')}" />
            <label key="month2" text="{resource:getString($constants,'cal.month2')}" />
            <label key="month3" text="{resource:getString($constants,'cal.month3')}" />
            <label key="month4" text="{resource:getString($constants,'cal.month4')}" />
            <label key="month5" text="{resource:getString($constants,'cal.month5')}" />
            <label key="month6" text="{resource:getString($constants,'cal.month6')}" />
            <label key="month7" text="{resource:getString($constants,'cal.month7')}" />
            <label key="month8" text="{resource:getString($constants,'cal.month8')}" />
            <label key="month9" text="{resource:getString($constants,'cal.month9')}" />
            <label key="month10" text="{resource:getString($constants,'cal.month10')}" />
            <label key="month11" text="{resource:getString($constants,'cal.month11')}" />
          </AbsolutePanel>
        </VerticalPanel>
      </FocusPanel>
      <Resource field="libRes" source="org.openelis.gwt.resources.OpenELISResources"/>
    </screen>
  </xsl:template>
</xsl:stylesheet>
