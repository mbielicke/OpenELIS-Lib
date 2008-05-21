package org.openelis.gwt.server;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.services.CalendarServiceInt;
import org.openelis.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;

public class CalendarServlet extends AppServlet implements CalendarServiceInt{

    private static final long serialVersionUID = 1L;

    private static String appRoot;
    
    public void init() throws ServletException {
        appRoot = getServletConfig().getInitParameter("app.root");
    }
    
    public String getXML() throws RPCException {
        try {
            Calendar cal = Calendar.getInstance();
            Document doc = XMLUtil.createNew("doc");
            Element root = doc.getDocumentElement();
            Element month = doc.createElement("month");
            month.appendChild(doc.createTextNode(String.valueOf(cal.get(Calendar.MONTH))));
            root.appendChild(month);
            Element year = doc.createElement("year");
            year.appendChild(doc.createTextNode(String.valueOf(cal.get(Calendar.YEAR))));
            root.appendChild(year);
            Element day = doc.createElement("day");
            day.appendChild(doc.createTextNode(String.valueOf(cal.get(Calendar.DATE))));
            root.appendChild(day);
            return ServiceUtils.getXML(appRoot+"Forms/calendar.xsl",doc);
        }catch(Exception e){
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }
    }

    public HashMap getXMLData() throws RPCException {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String getMonth(String month, String year, String date) throws RPCException {
        try {
            date = date.replace('-','/');
            Document doc = XMLUtil.createNew("doc");
            Element root = doc.getDocumentElement();
            Element monthEl = doc.createElement("month");
            monthEl.appendChild(doc.createTextNode(month));
            root.appendChild(monthEl);
            Element yearEl = doc.createElement("year");
            yearEl.appendChild(doc.createTextNode(year));
            root.appendChild(yearEl);
            Element day = doc.createElement("date");
            day.appendChild(doc.createTextNode(date));
            root.appendChild(day);
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

	public HashMap getXMLData(HashMap args) throws RPCException {
        try {
            String date = (String)((StringObject)args.get("date")).getValue();
            System.out.println("date = "+date);
            Calendar cal = Calendar.getInstance();
            if(!date.equals("")){
                date = date.replace('-','/');
                cal.setTime(new Date(date));
            }else{
                date = cal.get(Calendar.YEAR) +"/" +(cal.get(Calendar.MONTH)+1) + "/" +cal.get(Calendar.DATE);
            }
            Document doc = XMLUtil.createNew("doc");
            Element root = doc.getDocumentElement();
            Element month = doc.createElement("month");
            month.appendChild(doc.createTextNode(String.valueOf(cal.get(Calendar.MONTH))));
            root.appendChild(month);
            Element year = doc.createElement("year");
            year.appendChild(doc.createTextNode(String.valueOf(cal.get(Calendar.YEAR))));
            root.appendChild(year);
            Element day = doc.createElement("date");
            day.appendChild(doc.createTextNode(date));
            root.appendChild(day);
            HashMap map = new HashMap();
            map.put("xml", new StringObject(ServiceUtils.getXML(appRoot+"Forms/calendar.xsl",doc)));
            return map;
        }catch(Exception e){
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }
	}
    
}
