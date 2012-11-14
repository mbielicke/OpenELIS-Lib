package org.openelis.gwt.constants;

import com.google.gwt.i18n.client.Messages;

public interface LibraryConstants extends Messages {
	
	/******** Exceptions *****************/
	@Key("exc.fieldRequired")
	String fieldRequired();
	
	@Key("exc.fieldUnique")
	String fieldUnique();
	
	@Key("exc.fieldUniqueOnly")
	String fieldUniqueOnly();
	
	@Key("exc.modulePerm")
	String modelPerm(String perm, String module);
	
	@Key("exc.sectionPerm")
	String sectionPerm(String perm, String section);
	
	@Key("exc.selectPermRequired")
	String selectPermRequired();
	
	@Key("exc.viewPermRequired")
	String viewPermRequired();
	
	@Key("exc.expiredLock")
	String expiredLock();
	
	@Key("exc.entityLock")
	String entityLock(String user,String time);
	
	@Key("exc.invalidNumeric")
	String invalidNumeric();
	
	@Key("exc.invalidDate")
	String invalidDate();
	
	@Key("exc.invalidType")
	String invalidType();

	/*************** Dropdown ******************/
	@Key("drop.optionsSelected")
	String optionSelected();
	
	@Key("drop.all")
	String all();
	
	/*************** Confirm *****************/
	@Key("confirm.error")
	String error();
	
	@Key("confirm.warning")
	String warning();
	
	@Key("confirm.question")
	String question();
	
	@Key("confirm.busy")
	String busy();
	
	/************* Browser *****************/
	@Key("browser.tooManyWindows")
	String tooManyWindows();
	
	/************* Dates ******************/
	@Key("gen.datePattern")
	String datePattern();
	
	@Key("gen.dateTimePattern")
	String dateTimePattern();
	
	@Key("gen.timePattern")
	String timePattern();
	
	@Key("gen.timeMask")
	String timeMask();
	
	@Key("gen.dateMask")
	String dateMask();
	
	@Key("gen.dateTimeMask")
	String dateTimeMask();
	
	@Key("gen.dayInYearPattern")
	String dayInYearPattern();
	
	/**************** Password ************/
	@Key("pass.oldPass")
	String oldPassword();
	
	@Key("pass.newPass")
	String newPassword();
	
	@Key("pass.conPass")
	String confirmPassword();
	
	/**************** Window ************/
	@Key("window.loading")
	String loading();
	
	@Key("window.done")
	String done();
	
	/**************** Calendar **********/
	@Key("cal.day0")
	String day0();
	
	@Key("cal.day1")
	String day1();
	
	@Key("cal.day2")
	String day2();
	
	@Key("cal.day3")
	String day3();
	
	@Key("cal.day4")
	String day4();
	
	@Key("cal.day5")
	String day5();
	
	@Key("cal.day6")
	String day6();
	
	/**************** Header ************/
	@Key("header.ascending")
	String ascending();
	
	@Key("header.descending")
	String descending();
	
	/**************** Status Messages *********/
	@Key("msg.addAborted")
	String addAborted();
	
	@Key("msg.adding")
	String adding();
	
	@Key("msg.addingComplete")
	String addingComplete();
	
	@Key("msg.cancelChanges")
	String cancelChanges();
	
	@Key("msg.deleteComplete")
	String deleteComplete();
	
	@Key("msg.deleteMessage")
	String deleteMessage();
	
	@Key("msg.deleteAborted")
	String deleteAborted();
	
	@Key("msg.deleting")
	String deleting();
	
	@Key("msg.enterFieldsToQuery")
	String enterFieldsToQuery();
	
	@Key("msg.enterInformationPressCommit")
	String enterInformationPressCommit();
	
	@Key("msg.correctErrors")
	String correctErrors();
	
	@Key("msg.queryAborted")
	String queryAborted();
	
	@Key("msg.querying")
	String querying();
	
	@Key("msg.queryingComplete")
	String queryingComlpete();
	
	@Key("msg.queryFailed")
	String queryFailed();
	
	@Key("msg.updating")
	String updating();
	
	@Key("msg.updateAborted")
	String updateAborted();
	
	@Key("msg.updatingComplete")
	String updatingComplete();
	
	@Key("msg.mustCommitOrAbort")
	String mustCommitOrAbort();
	
	@Key("msg.lockForUpdate")
	String lockForUpdate();
	
	@Key("msg.fetching")
	String fetching();
	
	@Key("msg.fetchFailed")
	String fetchFailed();
	
	@Key("msg.noRecordsFound")
	String noRecordsFound();
	
	@Key("msg.noMoreRecordInDir")
	String noMoreRecordInDir();
	
	/************* Log ******************/
	@Key("log.severe")
	String logSevere();
	
	@Key("log.warning")
	String logWarning();
	
	@Key("log.info")
	String logInfo();
	
	@Key("log.config")
	String logConfig();
	
	@Key("log.fine")
	String logFine();
	
	@Key("log.finer")
	String logFiner();
	
	@Key("log.finest")
	String logFinest();
	
	@Key("log.all")
	String logAll();
	
	@Key("log.name")
	String logName();
	
	/*************** Button ************/
	@Key("btn.runReport")
	String runReport();
	
	@Key("btn.reset")
	String reset();
	
	@Key("report.generatingReport")
	String generatingReport();
	
	@Key("btn.query")
	String query();
	
	@Key("btn.add")
	String add();
	
	@Key("btn.next")
	String next();
	
	@Key("btn.previous")
	String previous();
	
	@Key("btn.update")
	String update();
	
	@Key("btn.delete")
	String delete();
	
	@Key("btn.commit")
	String commit();
	
	@Key("btn.abort")
	String abort();
	
	/************** TimeOut ***********/
	@Key("gen.timeoutHeader")
	String timeoutHeader();
	
	@Key("gen.timeoutWarning")
	String timeoutWarning();
	
	@Key("gen.timeoutExtendTime")
	String timeoutExtendTime();
	
	@Key("gen.timeoutLogout")
	String timeoutLogout();
	
	@Key("gen.couldNotCall")
	String couldNotCall();
	
	@Key("cal.month0")
	String month0();
	
	@Key("cal.month1")
	String month1();
	
	@Key("cal.month2")
	String month2();
	
	@Key("cal.month3")
	String month3();
	
	@Key("cal.month4")
	String month4();
	
	@Key("cal.month5")
	String month5();
	
	@Key("cal.month6")
	String month6();
	
	@Key("cal.month7")
	String month7();
	
	@Key("cal.month8")
	String month8();
	
	@Key("cal.month9")
	String month9();
	
	@Key("cal.month10")
	String month10();
	
	@Key("cal.month11")
	String month11();
	
}
