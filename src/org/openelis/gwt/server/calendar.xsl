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
  <xsl:variable name="today" select="cal:getInstance()"/>


  <xsl:template match="doc"> 

  <screen name="Calendar" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <VerticalPanel width="100%" style="CalendarWidget">
      <HorizontalPanel style="MonthBar">
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
		    	<xsl:with-param name="key">Cell1</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell2</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell3</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell4</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell5</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell6</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell7</xsl:with-param>
		    </xsl:call-template>
          </row>
          <row>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell8</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell9</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell10</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell11</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell12</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell13</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell14</xsl:with-param>
		    </xsl:call-template>
          </row>
          <row>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell15</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell16</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell17</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell18</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell19</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell20</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell21</xsl:with-param>
		    </xsl:call-template>
          </row>
          <row>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell22</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell23</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell24</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell25</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell26</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell27</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell28</xsl:with-param>
		    </xsl:call-template>
          </row>
          <row>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell29</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell30</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell31</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell32</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell33</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell34</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell35</xsl:with-param>
		    </xsl:call-template>
         </row>
         <row>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell36</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell37</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell38</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell39</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell40</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell41</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="DateCell">
		    	<xsl:with-param name="date"><xsl:value-of select="string(calUtils:getNextDay($calendar))"/></xsl:with-param>
		    	<xsl:with-param name="key">Cell42</xsl:with-param>
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
   </screen>
 </xsl:template>
 
     <xsl:template name="DateCell">
        <xsl:param name="date" />
        <xsl:param name="key"/>
            <icon style="DateCell">
              <widget>
                <label key="{$key}">
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
                </label>
              </widget>
           </icon>
  </xsl:template>

 
</xsl:stylesheet>
