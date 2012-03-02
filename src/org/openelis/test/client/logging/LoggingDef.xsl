<xsl:stylesheet
  extension-element-prefixes="resource"
  version="1.0"
  xmlns:locale="xalan://java.util.Locale"
  xmlns:resource="xalan://org.openelis.util.UTFResource"
  xmlns:xalan="http://xml.apache.org/xalan"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xsi:noNamespaceSchemaLocation="http://openelis.uhl.uiowa.edu/schema/ScreenSchemaNew.xsd"
  xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://openelis.uhl.uiowa.edu/schema/XSLTSchema.xsd">

  <xsl:template match="doc">
<!-- main screen -->
    <screen name="Logs">
      <HorizontalPanel padding="0" spacing="0" height="250px">
        <VerticalPanel padding="0" spacing="0" style="WhiteContentPanel" width="100%" height="250px">
          <AbsolutePanel css="border : 1px solid black">
            <ScrollPanel key="logContainer" height="400px" width="700px"/>
          </AbsolutePanel>
          <TablePanel style="Form">
            <row>
              <text style="Prompt">Log Level:</text>
              <select key="logLevel" width="100"/>
              <button style="Button" text="Clear Log" key="clearLog"/>
              <text style="Prompt">Send all logs to Remote Server</text>
              <check key="remoteAll"></check>
            </row>
          </TablePanel>
        </VerticalPanel>
      </HorizontalPanel>
    </screen>
 </xsl:template>
 
</xsl:stylesheet>   