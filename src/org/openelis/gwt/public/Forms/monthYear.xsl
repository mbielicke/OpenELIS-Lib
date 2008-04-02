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
  <xsl:variable name="yearCell"><xsl:value-of select="doc/yearCell"/></xsl:variable>


  <xsl:template match="doc"> 

  <screen id="Organization" serviceUrl="ElisService" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<display>
	  <panel layout="vertical" width="100%" style="CalendarWidget">
	    <panel layout="horizontal" style="Calendar" width="100%">
	      <panel layout="table" spacing="0" padding="0" width="100%">
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
	    </panel>
	    <panel layout="table" spacing="0" padding="0" width="100%">
	      <row>
	        <panel layout="absolute" key="prevDecade" onPanelClick="this" style="prevNavIndex"/>
	        <panel layout="absolute" key="nextDecade" onPanelClick="this" style="nextNavIndex"/>
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
	    </panel>
	    </panel>
	    <panel layout="absolute" style="SelectBar" halign="center">
	    <panel layout="horizontal" >
	      <widget>
	        <appButton action="ok" key="ok" onclick="this">
	          <widget>
	            <text>OK</text>
	          </widget>
	        </appButton>
	      </widget>
	      <widget>
            <appButton action="cancel" key="cancel" onclick="this" align="left">
	          <widget >
	            <text>CANCEL</text>
	          </widget>
            </appButton>
          </widget>
	    </panel>
	   </panel>
	  </panel>
	</display>
   <rpc key="display">
     <number key="month" type="integer" required="false"><xsl:value-of select="$month"/></number>
     <number key="year" type="integer" required="false"><xsl:value-of select="number($year)+number($yearCell)"/></number>
   </rpc> 
  </screen>
  
  </xsl:template>
  
  <xsl:template name="MYCell">
   <xsl:param name="text"/>
   <xsl:param name="type"/>
            <panel key="{$type}:{$text}" layout="absolute" hover="Hover" style="MYCell">
              <widget>
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
              </widget>
           </panel>
  </xsl:template>
  
</xsl:stylesheet>