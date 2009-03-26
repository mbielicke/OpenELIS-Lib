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

  <xalan:component prefix="cal">
    <xalan:script lang="javaclass" src="xalan://java.util.Calendar"/>
  </xalan:component>
  
  <xalan:component prefix="calUtils">
    <xalan:script lang="javaclass" src="xalan://org.openelis.util.CalendarUtils"/>
  </xalan:component>

    <xsl:variable name="month"><xsl:value-of select="doc/month"/></xsl:variable>
  <xsl:variable name="year"><xsl:value-of select="doc/year"/></xsl:variable>
  <xsl:variable name="monthDisplay" select="calUtils:getMonthYear(string($month),string($year))"/>
  <xsl:variable name="calendar" select="calUtils:getCalforMonth(string($month),string($year))"/>
  <xsl:variable name="selected"><xsl:value-of select="doc/date"/></xsl:variable>
  <xsl:variable name="today" select="cal:getInstance()"/>;


  <xsl:template match="doc"> 

  <screen id="Calendar" name="Calendar" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<display>
    <VerticalPanel width="100%" style="CalendarWidget">
      <HorizontalPanel layout="horizontal" style="MonthBar">
        <appButton action="prevMonth" key="prevMonth" onclick="this">
          <AbsolutePanel style="PreviousMonth"/>
        </appButton>
        <text style="MonthDisplay"><xsl:value-of select="$monthDisplay"/></text>
        <appButton action="monthSelect" key="monthSelect" onclick="this">
		  <AbsolutePanel style="MonthSelect"/>
        </appButton>
        <appButton action="nextMonth" key="nextMonth" onclick="this">
		  <AbsolutePanel style="NextMonth"/>
        </appButton>
      </HorizontalPanel>
      <VerticalPanel width="100%">
        <TablePanel style="Calendar" width="100%" spacing="0" padding="0">
          <row style="DayBar">
            <AbsolutePanel style="DayCell">
               <text style="DayText">S</text>
            </AbsolutePanel>
            <AbsolutePanel style="DayCell">
              <text style="DayText">M</text>
            </AbsolutePanel>
            <AbsolutePanel style="DayCell">
              <text style="DayText">T</text>
            </AbsolutePanel>
            <AbsolutePanel style="DayCell">
              <text style="DayText">W</text>
            </AbsolutePanel>
            <AbsolutePanel style="DayCell">
              <text style="DayText">T</text>
            </AbsolutePanel>
            <AbsolutePanel style="DayCell">
              <text style="DayText">F</text>
            </AbsolutePanel>
            <AbsolutePanel style="DayCell">
              <text style="DayText">S</text>
            </AbsolutePanel>
          </row>
          <row>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
          </row>
          <row>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
          </row>
          <row>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
          </row>
          <row>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
          </row>
          <row>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
         </row>
         <row>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    </xsl:call-template>
         </row>
      </TablePanel>
   </VerticalPanel>
   <AbsolutePanel style="TodayBar" width="100%" halign="center">
     <appButton action="today" key="today">
       <text value="{calUtils:getCurrentDateString()}" onPanelClick="this">Today</text>
     </appButton>
   </AbsolutePanel>
   </VerticalPanel>
   </display>
   <rpc key="display">
     <integer key="month" required="false"><xsl:value-of select="$month"/></integer>
     <integer key="year" required="false"><xsl:value-of select="$year"/></integer>
     <string key="date" required="false"><xsl:value-of select="$selected"/></string>
   </rpc>
   </screen>
 </xsl:template>
 
     <xsl:template name="DateCell">
 <xsl:param name="date" />
            <AbsolutePanel style="DateCell" hover="Hover" onPanelClick="this" value="{calUtils:getDateString($calendar)}">
              <widget>
                <text>
                  <xsl:choose>
                    <xsl:when test="$month != cal:get($calendar,calUtils:getField('MONTH'))">
                      <xsl:attribute name="style">DateText,OffMonth</xsl:attribute>
                    </xsl:when>
                    <xsl:when test="calUtils:isSelected($calendar,$selected)">
                      <xsl:attribute name="style">DateText,Current</xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="style">DateText</xsl:attribute>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:value-of select="$date"/>
                </text>
              </widget>
           </AbsolutePanel>
  </xsl:template>

 
</xsl:stylesheet>
