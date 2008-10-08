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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0">
<xsl:import href="button.xsl"/>
	<xsl:template name="aToZLeftPanelButtons">
	<VerticalPanel spacing="0" padding="0" style="AtoZ">
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">*</xsl:with-param>
			<xsl:with-param name="queryParam">* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">A</xsl:with-param>
			<xsl:with-param name="queryParam">A* | a* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">B</xsl:with-param>
			<xsl:with-param name="queryParam">B* | b* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">C</xsl:with-param>
			<xsl:with-param name="queryParam">C* | c* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">D</xsl:with-param>
			<xsl:with-param name="queryParam">D* | d* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">E</xsl:with-param>
			<xsl:with-param name="queryParam">E* | e* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">F</xsl:with-param>
			<xsl:with-param name="queryParam">F* | f* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">G</xsl:with-param>
			<xsl:with-param name="queryParam">G* | g* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">H</xsl:with-param>
			<xsl:with-param name="queryParam">H* | h* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">I</xsl:with-param>
			<xsl:with-param name="queryParam">I* | i* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">J</xsl:with-param>
			<xsl:with-param name="queryParam">J* | j* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">K</xsl:with-param>
			<xsl:with-param name="queryParam">K* | k* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">L</xsl:with-param>
			<xsl:with-param name="queryParam">L* | l* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">M</xsl:with-param>
			<xsl:with-param name="queryParam">M* | m* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">N</xsl:with-param>
			<xsl:with-param name="queryParam">N* | n* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">O</xsl:with-param>
			<xsl:with-param name="queryParam">O* | o* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">P</xsl:with-param>
			<xsl:with-param name="queryParam">P* | p* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">Q</xsl:with-param>
			<xsl:with-param name="queryParam">Q* | q* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">R</xsl:with-param>
			<xsl:with-param name="queryParam">R* | r* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">S</xsl:with-param>
			<xsl:with-param name="queryParam">S* | s* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">T</xsl:with-param>
			<xsl:with-param name="queryParam">T* | t* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">U</xsl:with-param>
			<xsl:with-param name="queryParam">U* | u* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">V</xsl:with-param>
			<xsl:with-param name="queryParam">V* | v* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">W</xsl:with-param>
			<xsl:with-param name="queryParam">W* | w* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">X</xsl:with-param>
			<xsl:with-param name="queryParam">X* | x* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
					<xsl:with-param name="keyParam">Y</xsl:with-param>
			<xsl:with-param name="queryParam">Y* | y* </xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="aToZButton">
			<xsl:with-param name="keyParam">Z</xsl:with-param>
			<xsl:with-param name="queryParam">Z* | z* </xsl:with-param>
		</xsl:call-template>
      </VerticalPanel>
	</xsl:template>
</xsl:stylesheet>
