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
