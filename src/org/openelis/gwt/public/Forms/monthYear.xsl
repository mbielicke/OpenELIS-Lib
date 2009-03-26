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
  <xsl:variable name="yearCell"><xsl:value-of select="doc/yearCell"/></xsl:variable>


  <xsl:template match="doc"> 

  <screen id="Organization" serviceUrl="ElisService" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<display>
	  <VerticalPanel width="100%" style="CalendarWidget">
	    <HorizontalPanel style="Calendar" width="100%">
	      <TablePanel spacing="0" padding="0" width="100%">
	        <row>
		      <xsl:call-template name="MYCell">
		    	  <xsl:with-param name="text">0</xsl:with-param>
		    	  <xsl:with-param name="type">month</xsl:with-param>
  		      </xsl:call-template>
	  	      <xsl:call-template name="MYCell">
		      	<xsl:with-param name="text">6</xsl:with-param>
		      	<xsl:with-param name="type">month</xsl:with-param>
              </xsl:call-template>
	      </row>
	      <row>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">1</xsl:with-param>
		    	<xsl:with-param name="type">month</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">7</xsl:with-param>
		    	<xsl:with-param name="type">month</xsl:with-param>
		    </xsl:call-template>
	      </row>	     
	      <row>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">2</xsl:with-param>
		    	<xsl:with-param name="type">month</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">8</xsl:with-param>
		    	<xsl:with-param name="type">month</xsl:with-param>
		    </xsl:call-template>
	      </row>	     
	      <row>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">3</xsl:with-param>
		    	<xsl:with-param name="type">month</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">9</xsl:with-param>
		    	<xsl:with-param name="type">month</xsl:with-param>
		    </xsl:call-template>
	      </row>	     
	      <row>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">4</xsl:with-param>
		    	<xsl:with-param name="type">month</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">10</xsl:with-param>
		    	<xsl:with-param name="type">month</xsl:with-param>
		    </xsl:call-template>
	      </row>	     
	      <row>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">5</xsl:with-param>
		    	<xsl:with-param name="type">month</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">11</xsl:with-param>
		    	<xsl:with-param name="type">month</xsl:with-param>
		    </xsl:call-template>
	      </row>	          
	    </TablePanel>
	    <TablePanel spacing="0" padding="0" width="100%">
	      <row>
	        <widget>
    	        <AbsolutePanel key="prevDecade" onPanelClick="this" style="prevNavIndex"/>
    	    </widget>
    	    <widget>
		        <AbsolutePanel key="nextDecade" onPanelClick="this" style="nextNavIndex"/>
		    </widget>
	      </row>	      
	      <row>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">0</xsl:with-param>
		    	<xsl:with-param name="type">year</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">5</xsl:with-param>
		    	<xsl:with-param name="type">year</xsl:with-param>
		    </xsl:call-template>
	      </row>	 
	      <row>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">1</xsl:with-param>
		    	<xsl:with-param name="type">year</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">6</xsl:with-param>
		    	<xsl:with-param name="type">year</xsl:with-param>
		    </xsl:call-template>
	      </row>		  
	      <row>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">2</xsl:with-param>
		    	<xsl:with-param name="type">year</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">7</xsl:with-param>
		    	<xsl:with-param name="type">year</xsl:with-param>
		    </xsl:call-template>
	      </row>		  
	      <row>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">3</xsl:with-param>
		    	<xsl:with-param name="type">year</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">8</xsl:with-param>
		    	<xsl:with-param name="type">year</xsl:with-param>
		    </xsl:call-template>
	      </row>		  
	      <row>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">4</xsl:with-param>
		    	<xsl:with-param name="type">year</xsl:with-param>
		    </xsl:call-template>
		    <xsl:call-template name="MYCell">
		    	<xsl:with-param name="text">9</xsl:with-param>
		    	<xsl:with-param name="type">year</xsl:with-param>
		    </xsl:call-template>
	      </row>		                         
	    </TablePanel>
	    </HorizontalPanel>
	    <AbsolutePanel layout="absolute" style="SelectBar" halign="center">
	    <HorizontalPanel layout="horizontal" >
	        <appButton action="ok" key="ok" onclick="this">
	            <text>OK</text>
	        </appButton>
            <appButton action="cancel" key="cancel" onclick="this" align="left">
	            <text>CANCEL</text>
            </appButton>
	    </HorizontalPanel>
	   </AbsolutePanel>
	  </VerticalPanel>
	</display>
   <rpc key="display">
     <integer key="month" required="false"><xsl:value-of select="$month"/></integer>
     <integer key="year" required="false"><xsl:value-of select="number($year)+number($yearCell)"/></integer>
   </rpc> 
  </screen>
  
  </xsl:template>
  
  <xsl:template name="MYCell">
   <xsl:param name="text"/>
   <xsl:param name="type"/>
            <AbsolutePanel key="{$type}:{$text}" hover="Hover" style="MYCell">
                <label key="{$type}:{$text}Text" onClick="this" wordwrap="true" value="{$type},{$text}">
                  <xsl:choose>
                    <xsl:when test="$type = 'month' and string($month) = string($text)">
                      <xsl:attribute name="style">Current</xsl:attribute>
                    </xsl:when>
                    <xsl:when test="$type = 'year' and $yearCell = $text">
                      <xsl:attribute name="style">Current</xsl:attribute>
                    </xsl:when>
                  </xsl:choose>
                  <xsl:choose>
                    <xsl:when test="$type = 'month'">
	                  <xsl:value-of select="calUtils:getMonthAbrv(string($text))"/>
	                </xsl:when>
	                <xsl:otherwise>
	                  <xsl:value-of select="number($text)+number($year)"/>
	                </xsl:otherwise>
	              </xsl:choose>
                </label>
           </AbsolutePanel>
  </xsl:template>
  
</xsl:stylesheet>
