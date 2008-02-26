<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns:xalan="http://xml.apache.org/xalan" 
xmlns:resource="xalan://org.openelis.server.constants.UTFResource" 
xmlns:locale="xalan://java.util.Locale" 
extension-element-prefixes="resource" 
version="1.0">
<xalan:component prefix="resource">
    <xalan:script lang="javaclass" src="xalan://org.openelis.server.constants.UTFResource"/>
  </xalan:component>
  
   <xalan:component prefix="locale">
    <xalan:script lang="javaclass" src="xalan://java.util.Locale"/>
  </xalan:component>
  
   <xsl:variable name="language"><xsl:value-of select="locale"/></xsl:variable>
    <xsl:variable name="constants" select="resource:getBundle('org.openelis.modules.main.server.constants.OpenELISConstants',locale:new(string($language)))"/>
    
	<xsl:template name="buttonPanelTemplate">
<xsl:param name="buttonsParam" />

<panel xsi:type="Absolute" layout="absolute" spacing="0" style="ButtonPanelContainer">
	<widget>
    	<buttonPanel key="buttons">
			<xsl:call-template name="loop">
				<xsl:with-param name="count"><xsl:value-of select="string-length($buttonsParam)"/></xsl:with-param>
				<xsl:with-param name="buttonsParam"><xsl:value-of select="string($buttonsParam)"/></xsl:with-param>
			</xsl:call-template>
  		</buttonPanel>
 	</widget>
</panel>
</xsl:template>

<xsl:template name="loop">
<xsl:param name="count"/>
<xsl:param name="iteration">1</xsl:param>
<xsl:param name="buttonsParam"/>
<!--code goes here-->
<xsl:if test="substring(string($buttonsParam),$iteration,1) = 'q'">
	<appButton action="query" toggle="true">	
		<panel xsi:type="Panel" layout="horizontal">
		<panel xsi:type="Absolute" layout="absolute" style="QueryButtonImage"/>
	     	<widget>
	        	<text><xsl:value-of select='resource:getString($constants,"query")'/></text>
			</widget>
		</panel>
	</appButton>
</xsl:if>
<xsl:if test="substring(string($buttonsParam),$iteration,1) = 'p'">
	<appButton action="prev" toggle="true">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="PreviousButtonImage"/>
	        <widget>
	           	<text><xsl:value-of select='resource:getString($constants,"previous")'/></text>
	        </widget>
	 	</panel>
	</appButton>
</xsl:if>
<xsl:if test="substring(string($buttonsParam),$iteration,1) = 'n'">
	<appButton action="next" toggle="true">
		 <panel xsi:type="Panel" layout="horizontal">
	  		<panel xsi:type="Absolute" layout="absolute" style="NextButtonImage"/>
	     	<widget>
	        	<text><xsl:value-of select='resource:getString($constants,"next")'/></text>
	     	</widget>
	 	</panel>
	</appButton>
</xsl:if>
<xsl:if test="substring(string($buttonsParam),$iteration,1) = 'a'">
	<appButton action="add" toggle="true">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="AddButtonImage"/>
	        <widget>
	        	<text><xsl:value-of select='resource:getString($constants,"add")'/></text>
	        </widget>
	 	</panel>
	</appButton>
</xsl:if>
<xsl:if test="substring(string($buttonsParam),$iteration,1) = 'u'">
	<appButton action="update" toggle="true">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="UpdateButtonImage"/>
	        <widget>
	        	<text><xsl:value-of select='resource:getString($constants,"update")'/></text>
	       	</widget>
	 	</panel>
	</appButton>
</xsl:if>
<xsl:if test="substring(string($buttonsParam),$iteration,1) = 'd'">
	<appButton action="delete" toggle="true">
		<panel xsi:type="Panel" layout="horizontal">
	     	<panel xsi:type="Absolute" layout="absolute" style="DeleteButtonImage"/>
	        <widget>
	        	<text><xsl:value-of select='resource:getString($constants,"delete")'/></text>
	      	</widget>
	 	</panel>
	</appButton>
</xsl:if>
<xsl:if test="substring(string($buttonsParam),$iteration,1) = 'c'">
	<appButton action="commit">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="CommitButtonImage"/>
	        <widget>
	        	<text><xsl:value-of select='resource:getString($constants,"commit")'/></text>
	      	</widget>
	  	</panel>
	</appButton>
</xsl:if>
<xsl:if test="substring(string($buttonsParam),$iteration,1) = 'b'">
	<appButton action="abort">
		<panel xsi:type="Panel" layout="horizontal">
	    	<panel xsi:type="Absolute" layout="absolute" style="AbortButtonImage"/>
	    	<widget>
	        	<text><xsl:value-of select='resource:getString($constants,"abort")'/></text>
	      	</widget>
	  	</panel>
	</appButton>
</xsl:if>
<xsl:if test="substring(string($buttonsParam),$iteration,1) = '|'">
	<panel xsi:type="Absolute" layout="absolute" style="ButtonDivider"/>
</xsl:if>
<!--end code-->
<xsl:if test="$iteration &lt; $count">
<xsl:call-template name="loop">
<xsl:with-param name="count" select="$count"/>
<xsl:with-param name="iteration" select="$iteration +1"/>
<xsl:with-param name="buttonsParam"><xsl:value-of select="string($buttonsParam)"/></xsl:with-param>
</xsl:call-template>
</xsl:if>

</xsl:template>
</xsl:stylesheet>