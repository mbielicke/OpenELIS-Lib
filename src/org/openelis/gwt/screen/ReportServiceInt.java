package org.openelis.gwt.screen;

import java.util.ArrayList;

import org.openelis.gwt.common.Prompt;
import org.openelis.gwt.common.ReportStatus;
import org.openelis.gwt.common.data.Query;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ReportServiceInt extends RemoteService {
	
	public ArrayList<Prompt> getPrompts() throws Exception;
	
	public ReportStatus runReport(Query query) throws Exception;
	

}
