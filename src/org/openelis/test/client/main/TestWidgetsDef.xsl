<xsl:stylesheet
  extension-element-prefixes="resource"
  version="1.0"
  xmlns:fn="http://www.w3.org/2005/xpath-functions"
  xmlns:locale="xalan://java.util.Locale"
  xmlns:resource="xalan://org.openelis.util.UTFResource"
  xmlns:xalan="http://xml.apache.org/xalan"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xsi:noNamespaceSchemaLocation="http://openelis.uhl.uiowa.edu/schema/ScreenSchemaNew.xsd"
  xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://openelis.uhl.uiowa.edu/schema/XSLTSchema.xsd">

  <xalan:component prefix="resource">
    <xalan:script lang="javaclass" src="xalan://org.openelis.util.UTFResource" />
  </xalan:component>
  <xalan:component prefix="locale">
    <xalan:script lang="javaclass" src="xalan://java.util.Locale" />
  </xalan:component>
  <xsl:variable name="language">
    <xsl:value-of select="doc/locale" />
  </xsl:variable>
  <xsl:variable name="props">
    <xsl:value-of select="doc/props" />
  </xsl:variable>
  <xsl:variable name="consts" select="resource:getBundle(string($props),locale:new(string($language)))" />
  <xsl:template match="doc">
    <screen>
      <VerticalPanel style="AppBackground">
        <AbsolutePanel style="topMenuBar">
          <menuBar>
            <menu display="Widgets">
              <menuItem description="Screen to test TextBox functionality" display="TextBox" enabled="false" icon="nothing" key="textbox"/>
              <menuItem description="Screen to test Dropdown functionality" enabled="false" display="Dropdown" icon="nothing" key="dropdown"/>
              <menuItem description="Screen to test Table functionality" display="Table" enabled="true" icon="nothing" key="table" />
              <menuItem description="Screen to test Tree functionality" display="Tree" enabled="false" icon="nothing" key="tree" />
              <menuItem description="Screen to test Calendar functionality" display="Calendar" enabled="false" icon="nothing" key="calendar" />
              <menuItem description="Screen to test Selection functionality" display="Selection" enabled="false" icon="nothing" key="selection" />
              <menuItem description="Screen to test Button functionality" display="Button" enabled="false" icon="nothing" key="button"/>  
              <menuItem description="Screen to test AutoComplete functionality" display="AutoComplete" enabled="false" icon="nothing" key="autoComplete"/>
              <menuItem description="Screen to test Text Area functionality" display="Text Area" enabled="false" icon="nothing" key="textarea"/>
              <menuItem description="Screen to text CheckBox functionality" display="CheckBox" enabled="false" icon="nothing" key="checkbox"/>
            </menu>
            <menu display="Help">
              <menuItem description="" display="Logs" enabled="false" icon="nothing" key="logs"/>
            </menu>
          </menuBar>
        </AbsolutePanel>
        <HorizontalPanel>
          <browser key="browser" sizeToWindow="true" />
        </HorizontalPanel>
      </VerticalPanel>
    </screen>
  </xsl:template>
</xsl:stylesheet>