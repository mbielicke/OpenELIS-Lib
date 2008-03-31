<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xalan="http://xml.apache.org/xalan"
                xmlns:cal="xalan://java.util.Calendar"
                xmlns:calUtils="xalan://org.openelis.util.CalendarUtils"
                extension-element-prefixes="resource"
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
  <xsl:variable name="today" select="cal:getInstance()"/>;


  <xsl:template match="doc"> 

  <screen id="Organization" serviceUrl="ElisService" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<display>
    <panel layout="vertical" width="100%" style="CalendarWidget">
      <panel layout="horizontal" style="MonthBar">
        <appButton action="prevMonth" key="prevMonth" onclick="this">
          <panel layout="absolute" style="PreviousMonth"/>
        </appButton>
        <widget>
          <text style="MonthDisplay"><xsl:value-of select="$monthDisplay"/></text>
        </widget>
        <appButton action="monthSelect" key="monthSelect" onclick="this">
		  <panel layout="absolute" style="MonthSelect"/>
        </appButton>
        <appButton action="nextMonth" key="nextMonth" onclick="this">
		  <panel layout="absolute" style="NextMonth"/>
        </appButton>
      </panel>
      <panel layout="vertical" width="100%">
        <panel layout="table" style="Calendar" width="100%" spacing="0" padding="0">
          <row style="DayBar">
            <panel layout="absolute" style="DayCell">
              <widget>
                 <text style="DayText">S</text>
              </widget>
            </panel>
            <panel layout="absolute" style="DayCell">
              <widget>
                 <text style="DayText">M</text>
              </widget>
            </panel>
            <panel layout="absolute" style="DayCell">
              <widget>
                 <text style="DayText">T</text>
              </widget>
            </panel>
            <panel layout="absolute" style="DayCell">
              <widget>
                 <text style="DayText">W</text>
              </widget>
            </panel>
            <panel layout="absolute" style="DayCell">
              <widget>
                 <text style="DayText">T</text>
              </widget>
            </panel>
            <panel layout="absolute" style="DayCell">
              <widget>
                 <text style="DayText">F</text>
              </widget>
            </panel>
            <panel layout="absolute" style="DayCell">
              <widget>
                 <text style="DayText">S</text>
              </widget>
            </panel>
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
      </panel>
   </panel>
   <panel layout="absolute" style="TodayBar" width="100%" halign="center">
     <appButton action="today" key="today">
       <widget>
         <text value="{calUtils:getCurrentDateString()}" onPanelClick="this">Today</text>
       </widget>
     </appButton>
   </panel>
   </panel>
   </display>
   <rpc key="display">
     <number key="month" type="integer" required="false"><xsl:value-of select="$month"/></number>
     <number key="year" type="integer" required="false"><xsl:value-of select="$year"/></number>
   </rpc>
   </screen>
 </xsl:template>
 
     <xsl:template name="DateCell">
 <xsl:param name="date" />
            <panel layout="absolute" style="DateCell" hover="Hover" onPanelClick="this" value="{calUtils:getDateString($calendar)}">
              <widget>
                <text>
                  <xsl:choose>
                    <xsl:when test="$month != cal:get($calendar,calUtils:getField('MONTH'))">
                      <xsl:attribute name="style">DateText,OffMonth</xsl:attribute>
                    </xsl:when>
                    <xsl:when test="calUtils:isToday($calendar)">
                      <xsl:attribute name="style">DateText,Current</xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="style">DateText</xsl:attribute>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:value-of select="$date"/>
                </text>
              </widget>
           </panel>
  </xsl:template>

 
</xsl:stylesheet>