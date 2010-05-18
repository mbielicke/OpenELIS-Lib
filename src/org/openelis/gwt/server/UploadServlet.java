package org.openelis.gwt.server;


import gwtupload.client.IUploader.Utils;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.auth.StaticUserAuthenticator;
import org.apache.commons.vfs.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.commons.vfs.provider.sftp.SftpFileSystemConfigBuilder;
import org.openelis.gwt.common.FtpRPC;
import org.openelis.gwt.common.LocalizedException;
import org.openelis.util.SessionManager;

public class UploadServlet extends UploadAction {

	private static final long serialVersionUID = 1L;
	/**
	 * Override executeAction to save the received files in a custom place
	 * and delete this items from session.  
	 */
	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {
					// You can also specify the temporary folder
					// File file = File.createTempFile("upload-", ".bin", new File("/tmp"));
					File file = File.createTempFile("upload-", ".bin");
					item.write(file);
					System.out.println("settting file with name : "+item.getName());
					SessionManager.getSession().setAttribute(item.getName(),file.getAbsolutePath());
					SessionManager.getSession().setAttribute(item.getName()+"-content", item.getContentType());
				} catch (Exception e) {
					throw new UploadActionException(e.getMessage());
				}
			}
			removeSessionFileItems(request);
		}
		return null;
	}

	/**
	 * Remove a file when the user sends a delete request
	 */
	@Override
	public void removeItem(HttpServletRequest request, String fieldName)  throws UploadActionException {
		File file = new File((String)SessionManager.getSession().getAttribute(fieldName));
		SessionManager.getSession().removeAttribute(fieldName);
		if (file != null)  file.delete();
	}

	/**
	 * Get the content of an uploaded file
	 */
	@Override
	public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fieldName = request.getParameter(PARAM_SHOW);
		File f = new File((String)SessionManager.getSession().getAttribute(fieldName));
		if (f != null) {
			response.setContentType((String)SessionManager.getSession().getAttribute(fieldName+"-content"));
			FileInputStream is = new FileInputStream(f);
			copyFromInputStreamToOutputStream(is, response.getOutputStream());
		} else {
			renderXmlResponse(request, response, ERROR_ITEM_NOT_FOUND);
		}
	}

	public void startFTPLoad(FtpRPC rpc) throws Exception {
		try {
			FTPStatus status = new FTPStatus();
			SessionManager.getSession().setAttribute("ftpStatus", status);
			status.status = FTPStatus.Status.IN_PROGRESS;

			String basename = Utils.basename(rpc.path);
			FileSystemOptions opts = new FileSystemOptions();
			
			Object serviceInst = Class.forName(rpc.service).newInstance();
		    String[] ftpAccount = (String[])serviceInst.getClass().getMethod("getFTPAccount", new Class[] {String.class}).invoke(serviceInst, new Object[]{rpc.path});
						
			StaticUserAuthenticator auth = new StaticUserAuthenticator(null, ftpAccount[1], ftpAccount[2]);
			DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts,"no");
			FileSystemManager fsManager = VFS.getManager();
			FileObject sourceFile = fsManager.resolveFile(ftpAccount[0],opts);

			byte[] buffer = new byte[1];
			status.totalBytes = sourceFile.getContent().getSize();
			File outFile = File.createTempFile("upload-", ".bin");
			FileOutputStream out = new FileOutputStream(outFile);
			InputStream in = sourceFile.getContent().getInputStream();
			int counter = 0;
			while (true) {
				int bytes = in.read(buffer);
				if (bytes < 0)
					break;

				out.write(buffer, 0, bytes);
				counter += bytes;
				((FTPStatus)SessionManager.getSession().getAttribute("ftpStatus")).currentBytes = counter;
			}
			((FTPStatus)SessionManager.getSession().getAttribute("ftpStatus")).status = FTPStatus.Status.FINISHED;
			out.close();
			in.close();
			SessionManager.getSession().setAttribute(basename,outFile.getAbsolutePath());
			SessionManager.getSession().setAttribute(basename+"-content", sourceFile.getContent().getContentInfo().getContentType());
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	public String getFTPStatus(String fieldname) {
		
		Map<String, String> ret = new HashMap<String, String>();
		long currentBytes = 0;
		long totalBytes = 0;
		long percent = 0;
		FTPStatus ftpStat = (FTPStatus)SessionManager.getSession().getAttribute("ftpStatus");
		if (ftpStat != null) {
			if (ftpStat.exception != null) {
				String errorMsg = "The upload was cancelled because there was an error in the server.\nServer's error is:\n" + ftpStat.exception.getMessage();
				ret.put("error", errorMsg);
				ret.put("finished", "error");

			} else {
				currentBytes = ftpStat.currentBytes;
				totalBytes = ftpStat.totalBytes;
				percent = totalBytes != 0 ? currentBytes * 100 / totalBytes : 0;
				ret.put("percent", "" + percent);
				ret.put("currentBytes", "" + currentBytes);
				ret.put("totalBytes", "" + totalBytes);
				if (ftpStat.status == FTPStatus.Status.FINISHED) {
					ret.put("finished", "ok");
				}
			}
		}
		if (ret.containsKey("finished")) {
			SessionManager.getSession().removeAttribute("ftpStatus");
		}


		String message = "<doc>";
		for (Entry<String, String> e : ret.entrySet()) {
			if (e.getValue() != null) {
				String k = e.getKey();
				String v = e.getValue().replaceAll("</*pre>", "").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
				message += "<" + k + ">" + v + "</" + k + ">\n";
			}
		}
		return message+"</doc>";
	}

	private static class FTPStatus {
		long currentBytes;
		long percent;
		long totalBytes;
		enum Status {IN_PROGRESS,FINISHED,CANCELED,ERROR}
		Status status;
		LocalizedException exception;

	}




}
