/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.util;

import java.util.ArrayList;
import java.util.List;

public class GetPage {

	public static List getPage(List resultList, int first, int max){
		List returnList = new ArrayList();
		if(resultList.size() == 0)
			return returnList;
		
		if(first > resultList.size()-1)
			return null;
		
		int i=first;
		int maxCount=0;
		while(i<resultList.size() && maxCount < max){
			returnList.add(resultList.get(i));
			
			i++;
			maxCount++;
		}
		
		return returnList;
	}
}
