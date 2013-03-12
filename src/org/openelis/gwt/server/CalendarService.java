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

import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

import org.openelis.gwt.common.CalendarRPC;
import org.openelis.ui.common.Datetime;
import org.openelis.util.SessionManager;
import org.openelis.util.UTFResource;
import org.openelis.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CalendarService {

    private static final long serialVersionUID = 1L;

    private static String appRoot ;
    
    private static UTFResource resource;
    
    
    public CalendarRPC getMonth(CalendarRPC form) throws Exception {
        try {
        	if(resource == null)
        	resource = UTFResource.getBundle("org.openelis.gwt.server.CalendarConstants",new Locale(((SessionManager.getSession() == null  || (String)SessionManager.getSession().getAttribute("locale") == null) 
                    ? "en" : (String)SessionManager.getSession().getAttribute("locale"))));
            Calendar cal = Calendar.getInstance();
            if(form.date != null && !form.date.equals("")){
                cal.setTime(form.date.getDate());
            }else{
                form.date = Datetime.getInstance(form.begin,form.end,cal.getTime());
            }
            if(form.month == -1)
            	form.month = cal.get(Calendar.MONTH);
            else
            	cal.set(Calendar.MONTH, form.month);
            if(form.year == -1)
            	form.year = cal.get(Calendar.YEAR);
            else
            	cal.set(Calendar.YEAR, form.year);
            
            cal.set(Calendar.DATE, 1);
            if(cal.get(Calendar.DAY_OF_WEEK) > 1)
                cal.add(Calendar.DATE, -cal.get(Calendar.DAY_OF_WEEK)+1);
            else
                cal.add(Calendar.DATE, -7);
            
            for(int i = 0; i < 6; i++) {
            	for(int j = 0; j < 7; j++) {
        			form.cells[i][j] = (byte)cal.get(Calendar.DATE);
        			cal.add(Calendar.DATE, 1);
            	}
            }
            form.monthDisplay = resource.getString("month"+form.month) + " " +(form.year);
            return form;
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        
    }

    public CalendarRPC getMonthSelect(CalendarRPC form) throws Exception {
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
            InputStream is = CalendarService.class.getClassLoader().getResourceAsStream("org/openelis/gwt/server/monthYear.xsl");
            form.xml = ServiceUtils.getXML(is,doc);
            return form;
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public CalendarRPC getScreen(CalendarRPC rpc) throws Exception {
        try {
            Calendar cal = Calendar.getInstance();
            if(rpc.date != null){
                cal.setTime(rpc.date.getDate());
            }else{
                rpc.date = Datetime.getInstance(Datetime.YEAR,Datetime.DAY,cal.getTime());
            }
            Document doc = XMLUtil.createNew("doc");
            Element root = doc.getDocumentElement();
            Element month = doc.createElement("month");
            rpc.month = cal.get(Calendar.MONTH);
            month.appendChild(doc.createTextNode(String.valueOf(rpc.month)));
            root.appendChild(month);
            Element year = doc.createElement("year");
            rpc.year = cal.get(Calendar.YEAR);
            System.out.println("year is "+rpc.year);
            year.appendChild(doc.createTextNode(String.valueOf(rpc.year)));
            root.appendChild(year);
            Element day = doc.createElement("date");
            System.out.println("date = "+rpc.date.toString());
            day.appendChild(doc.createTextNode(rpc.date.toString()));
            root.appendChild(day);
            InputStream is = CalendarService.class.getClassLoader().getResourceAsStream("org/openelis/gwt/server/calendar.xsl");
            rpc.xml = ServiceUtils.getXML(is,doc);
            return rpc;
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public Datetime getCurrentDatetime(byte begin, byte end) {
    	return Datetime.getInstance(begin,end);
    }
   
    
}
