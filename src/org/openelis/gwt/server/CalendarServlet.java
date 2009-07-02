/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.server;

import org.openelis.gwt.common.CalendarForm;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.services.CalendarServiceInt;
import org.openelis.util.Datetime;
import org.openelis.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;

public class CalendarServlet extends AppServlet implements CalendarServiceInt{

    private static final long serialVersionUID = 1L;

    private static String appRoot ;
    
    public void init() throws ServletException {
        appRoot = getServletConfig().getInitParameter("app.root");
        //appRoot = "/home/tschmidt/workspace/OpenELIS-KitchenSink/www/org.openelis.ks.KitchenSink/";
    }
    
    public CalendarForm getMonth(CalendarForm form) throws RPCException {
        try {
            Calendar cal = Calendar.getInstance();
            if(form.date != null && !form.date.equals("")){
                cal.setTime(form.date);
            }else{
                form.date = cal.getTime();
            }
            Document doc = XMLUtil.createNew("doc");
            Element root = doc.getDocumentElement();
            Element monthEl = doc.createElement("month");
            monthEl.appendChild(doc.createTextNode(String.valueOf(cal.get(Calendar.MONTH))));
            root.appendChild(monthEl);
            Element yearEl = doc.createElement("year");
            yearEl.appendChild(doc.createTextNode(String.valueOf(cal.get(Calendar.YEAR))));
            root.appendChild(yearEl);
            Element day = doc.createElement("date");
            day.appendChild(doc.createTextNode(new Datetime(Datetime.YEAR,Datetime.DAY,form.date).toString()));
            root.appendChild(day);
            form.xml = ServiceUtils.getXML(appRoot+"Forms/calendar.xsl",doc);
            return form;
        }catch(Exception e){
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }
        
    }

    public CalendarForm getMonthSelect(CalendarForm form) throws RPCException {
        try {
            Document doc = XMLUtil.createNew("doc");
            Element root = doc.getDocumentElement();
            Element monthEl = doc.createElement("month");
            monthEl.appendChild(doc.createTextNode(String.valueOf(form.month)));
            root.appendChild(monthEl);
            Element yearEl = doc.createElement("year");
            yearEl.appendChild(doc.createTextNode(String.valueOf(form.year/10*10)));
            root.appendChild(yearEl);
            Element yearCellEl = doc.createElement("yearCell");
            yearCellEl.appendChild(doc.createTextNode(String.valueOf(form.year%10)));
            root.appendChild(yearCellEl);
            form.xml = ServiceUtils.getXML(appRoot+"Forms/monthYear.xsl",doc);
            return form;
        }catch(Exception e){
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }
    }

    public CalendarForm getScreen(CalendarForm rpc) throws RPCException {
        try {
            Calendar cal = Calendar.getInstance();
            if(rpc.date != null){
                cal.setTime(rpc.date);
            }else{
                rpc.date = cal.getTime();
            }
            Document doc = XMLUtil.createNew("doc");
            Element root = doc.getDocumentElement();
            Element month = doc.createElement("month");
            rpc.month = cal.get(Calendar.MONTH);
            month.appendChild(doc.createTextNode(String.valueOf(rpc.month)));
            root.appendChild(month);
            Element year = doc.createElement("year");
            rpc.year = cal.get(Calendar.YEAR);
            year.appendChild(doc.createTextNode(String.valueOf(rpc.year)));
            root.appendChild(year);
            Element day = doc.createElement("date");
            day.appendChild(doc.createTextNode(new Datetime(Datetime.YEAR,Datetime.DAY,rpc.date).toString()));
            root.appendChild(day);
            rpc.xml = ServiceUtils.getXML(appRoot+"Forms/calendar.xsl",doc);
            return rpc;
        }catch(Exception e){
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }
    }
    
}
