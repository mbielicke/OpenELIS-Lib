package org.openelis.gwt.server;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.services.CalendarServiceInt;
import org.openelis.util.CalendarUtils;
import org.openelis.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Calendar;

import javax.servlet.ServletException;

public class CalendarServlet extends AppServlet implements CalendarServiceInt{

    private static final long serialVersionUID = 1L;

    private static String appRoot;
    
    public void init() throws ServletException {
        appRoot = getServletConfig().getInitParameter("app.root");
    }
    
    public String getXML() throws RPCException {
        // TODO Auto-generated method stub
        try {
            Document doc = XMLUtil.createNew("doc");
            Element root = doc.getDocumentElement();
            Element month = doc.createElement("month");
            month.appendChild(doc.createTextNode(String.valueOf(Calendar.getInstance().get(Calendar.MONTH))));
            root.appendChild(month);
            Element year = doc.createElement("year");
            year.appendChild(doc.createTextNode(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))));
            root.appendChild(year);
            return ServiceUtils.getXML(appRoot+"Forms/calendar.xsl",doc);
        }catch(Exception e){
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }
    }

    public DataObject[] getXMLData() throws RPCException {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String getMonth(String month, String year) throws RPCException {
        try {
            Document doc = XMLUtil.createNew("doc");
            Element root = doc.getDocumentElement();
            Element monthEl = doc.createElement("month");
            monthEl.appendChild(doc.createTextNode(month));
            root.appendChild(monthEl);
            Element yearEl = doc.createElement("year");
            yearEl.appendChild(doc.createTextNode(year));
            root.appendChild(yearEl);
            return ServiceUtils.getXML(appRoot+"Forms/calendar.xsl",doc);
        }catch(Exception e){
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }
        
    }

    public String getMonthSelect(String month, String year) throws RPCException {
        try {
            Document doc = XMLUtil.createNew("doc");
            Element root = doc.getDocumentElement();
            Element monthEl = doc.createElement("month");
            monthEl.appendChild(doc.createTextNode(month));
            root.appendChild(monthEl);
            Element yearEl = doc.createElement("year");
            yearEl.appendChild(doc.createTextNode(String.valueOf(Integer.parseInt(year)/10*10)));
            root.appendChild(yearEl);
            Element yearCellEl = doc.createElement("yearCell");
            yearCellEl.appendChild(doc.createTextNode(String.valueOf(Integer.parseInt(year)%10)));
            root.appendChild(yearCellEl);
            return ServiceUtils.getXML(appRoot+"Forms/monthYear.xsl",doc);
        }catch(Exception e){
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }
    }
    
}
