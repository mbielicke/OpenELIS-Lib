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
                xmlns:cal="xalan://java.util.Calendar"
                xmlns:calUtils="xalan://org.openelis.util.CalendarUtils"
                version="1.0">

  <xsl:template match="doc"> 

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
      <VerticalPanel width="100%">
 
        <TablePanel style="Calendar" width="100%" spacing="0" padding="0">
          <row style="DayBar">
            <icon style="DayCell">
               <text style="DayText">S</text>
            </icon>
            <icon style="DayCell">
              <text style="DayText">M</text>
            </icon>
            <icon style="DayCell">
              <text style="DayText">T</text>
            </icon>
            <icon style="DayCell">
              <text style="DayText">W</text>
            </icon>
            <icon style="DayCell">
              <text style="DayText">T</text>
            </icon>
            <icon style="DayCell">
              <text style="DayText">F</text>
            </icon>
            <icon style="DayCell">
              <text style="DayText">S</text>
            </icon>
          </row>
    
          <row>
		    <icon style="DateCell">
		      <label key="cell:0:0" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:0:1" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:0:2" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:0:3" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:0:4" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:0:5" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:0:6" style="DateText"/>
		    </icon>
          </row>
          <row>
		    <icon style="DateCell">
		      <label key="cell:1:0" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:1:1" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:1:2" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:1:3" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:1:4" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:1:5" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:1:6" style="DateText"/>
		    </icon>
          </row>
          <row>
		    <icon style="DateCell">
		      <label key="cell:2:0" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:2:1" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:2:2" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:2:3" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:2:4" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:2:5" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:2:6" style="DateText"/>
		    </icon>
          </row>
          <row>
		    <icon style="DateCell">
		      <label key="cell:3:0" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:3:1" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:3:2" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:3:3" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:3:4" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:3:5" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:3:6" style="DateText"/>
		    </icon>
          </row>
          <row>
		    <icon style="DateCell">
		      <label key="cell:4:0" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:4:1" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:4:2" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:4:3" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:4:4" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:4:5" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:4:6" style="DateText"/>
		    </icon>
         </row>
         <row>
		    <icon style="DateCell">
		      <label key="cell:5:0" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:5:1" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:5:2" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:5:3" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:5:4" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:5:5" style="DateText"/>
		    </icon>
		    <icon style="DateCell">
		      <label key="cell:5:6" style="DateText"/>
		    </icon>
         </row>
      </TablePanel>
   </VerticalPanel>

   <AbsolutePanel style="TodayBar" width="100%" halign="center">
     <appButton action="today" key="today" style="Button" width="75px">
       <text>Today</text>
     </appButton>
   </AbsolutePanel>
   </VerticalPanel>
   </screen>
 </xsl:template>
 
</xsl:stylesheet>
