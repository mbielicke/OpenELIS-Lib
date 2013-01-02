package org.openelis.gwt.common;

import java.io.Serializable;
import java.util.ArrayList;

public class Exceptions implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	ArrayList<LocalizedException> validateExceptions,userExceptions;

	public boolean hasExceptions() {
		return userExceptions != null || validateExceptions != null;
	}

	public void addException(LocalizedException exception) {
		if (userExceptions == null)
			userExceptions = new ArrayList<LocalizedException>();
		userExceptions.add(exception);
	}
	
	public void addValidateException(LocalizedException exception) {
		if (validateExceptions == null)
			validateExceptions = new ArrayList<LocalizedException>();
		validateExceptions.add(exception);
	}

	public ArrayList<LocalizedException> getEndUserExceptions() {
		return userExceptions;
	}

	public ArrayList<LocalizedException> getValidateExceptions() {
		return validateExceptions;
	}

	public void clearExceptions() {
		userExceptions = null;
		validateExceptions = null;
	}

	public void clearEndUserExceptions() {
		userExceptions = null;
	}

	public void clearValidateExceptions() {
		validateExceptions = null;
	}

}
