package org.openelis.gwt.screen;

import java.util.ArrayList;

import org.openelis.gwt.common.Prompt;
import org.openelis.gwt.common.ReportStatus;
import org.openelis.gwt.common.data.Query;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReportServiceIntAsync {

	void getPrompts(AsyncCallback<ArrayList<Prompt>> callback);

	void runReport(Query query, AsyncCallback<ReportStatus> callback);

}
