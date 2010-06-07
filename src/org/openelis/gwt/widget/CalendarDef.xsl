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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xalan="http://xml.apache.org/xalan"
                extension-element-prefixes="resource"
                xmlns:locale="xalan://java.util.Locale"
                xmlns:resource="xalan://org.openelis.util.UTFResource"
                xmlns:cal="xalan://java.util.Calendar"
                xmlns:calUtils="xalan://org.openelis.util.CalendarUtils"
                version="1.0">

  <xsl:template match="doc"> 
    <xsl:variable name="language" select="locale" />
    <xsl:variable name="props" select="props" />
    <xsl:variable name="constants" select="resource:getBundle(string('org.openelis.gwt.server.CalendarConstants'),locale:new(string($language)))" />
  <screen name="Calendar" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <VerticalPanel style="CalendarWidget">
            <widget halign="center">
   	<HorizontalPanel style="TimeBar" width="100%" key="TimeBar">
    	 <widget halign="center">
     		<textbox key="time" field="Date" begin="3" end="5" width="45px" pattern="HH:mm"/>
     	</widget>
   	</HorizontalPanel>
   	</widget>
      <HorizontalPanel style="MonthBar">
        <appButton action="prevMonth" key="prevMonth" style="Button">
          <AbsolutePanel style="PreviousMonth"/>
        </appButton>
        <label key="MonthDisplay" style="MonthDisplay"></label>
        <appButton action="monthSelect" key="monthSelect" style="Button">
		  <AbsolutePanel style="MonthSelect"/>
        </appButton>
        <appButton action="nextMonth" key="nextMonth" style="Button">
		  <AbsolutePanel style="NextMonth"/>
        </appButton>
      </HorizontalPanel>
      <VerticalPanel width="100%" key="calContainer">
 
       <!-- 
        <TablePanel style="Calendar" width="100%" spacing="0" padding="0" key="ucalTable">
          <row style="DayBar">
            <widget style="DayCell">
               <text style="DayText">S</text>
            </widget>
            <widget style="DayCell">
              <text style="DayText">M</text>
            </widget>
            <widget style="DayCell">
              <text style="DayText">T</text>
            </widget>
            <widget style="DayCell">
              <text style="DayText">W</text>
            </widget>
            <widget style="DayCell">
              <text style="DayText">T</text>
            </widget>
            <widget style="DayCell">
              <text style="DayText">F</text>
            </widget>
            <widget style="DayCell">
              <text style="DayText">S</text>
            </widget>
          </row>
    
          <row>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
          </row>
          <row>
		    <widget style="DateCell">
		      <text  style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
          </row>
          <row>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
          </row>
          <row>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
          </row>
          <row>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
         </row>
         <row>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
		    <widget style="DateCell">
		      <text style="DateText"/>
		    </widget>
         </row>
      </TablePanel>
     -->
   </VerticalPanel>

   <AbsolutePanel style="TodayBar" width="100%" halign="center">
     <appButton action="today" key="today" style="Button" width="75px">
       <text>Today</text>
     </appButton>
   </AbsolutePanel>
      <AbsolutePanel visible="false">
     <label key="month0" text="{resource:getString($constants,'month0')}"/>
     <label key="month1" text="{resource:getString($constants,'month1')}"/>
     <label key="month2" text="{resource:getString($constants,'month2')}"/>
     <label key="month3" text="{resource:getString($constants,'month3')}"/>
     <label key="month4" text="{resource:getString($constants,'month4')}"/>
     <label key="month5" text="{resource:getString($constants,'month5')}"/>
     <label key="month6" text="{resource:getString($constants,'month6')}"/>
     <label key="month7" text="{resource:getString($constants,'month7')}"/>
     <label key="month8" text="{resource:getString($constants,'month8')}"/>
     <label key="month9" text="{resource:getString($constants,'month9')}"/>
     <label key="month10" text="{resource:getString($constants,'month10')}"/>
     <label key="month11" text="{resource:getString($constants,'month11')}"/>     
   </AbsolutePanel>
   </VerticalPanel>

   </screen>
 </xsl:template>
 
</xsl:stylesheet>
