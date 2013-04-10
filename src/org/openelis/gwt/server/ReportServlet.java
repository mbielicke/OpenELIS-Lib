package org.openelis.gwt.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openelis.ui.common.DataBaseUtil;
import org.openelis.ui.common.ReportStatus;

/**
 * This servlet streams report files back to the requester.
 */
public class ReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
                                                                                  IOException {
        int l;
        byte b[];
        File file;
        FileInputStream in;
        ServletOutputStream out;
        ReportStatus status;
        String filename, contentType, attachment;

        in = null;
        out = null;
        filename = null;
        try {
            /*
             * make sure the requested file is in user session (we have
             * generated the file)
             */
            filename = req.getParameter("file");
            if (DataBaseUtil.isEmpty(filename)) {
                error(resp, "Missing file name; please report this error to Lab IT");
                return;
            }

            status = (ReportStatus)req.getSession().getAttribute(filename);
            if (status == null || status.getStatus() != ReportStatus.Status.SAVED) {
                error(resp, "Specified file key is not valid; please report this error to Lab IT");
                return;
            }

            file = new File(status.getPath(), status.getMessage());
            if (!file.exists()) {
                error(resp, "Specified file is not valid; please report this error to Lab IT");
                System.out.println("File " + file.getAbsolutePath() + " not found");
                return;
            }

            /*
             * stream the file
             */
            contentType = getContentType(filename);
            resp.setContentType(contentType);

            attachment = req.getParameter("attachment");
            if (!DataBaseUtil.isEmpty(attachment))
                resp.setHeader("Content-Disposition", "attachment;filename=\"" + attachment + "\"");
            else
                resp.setHeader("Content-Disposition", "filename=\"" + filename + "\"");

            /*
             * TODO: Remove this when we no longer support IE8
             * Headers are set to fix IE8 download bug
             */
            resp.setHeader("Pragma", "token");
            resp.setHeader("Cache-control", "private");

            in = new FileInputStream(file);
            out = resp.getOutputStream();

            b = new byte[1024];
            while ((l = in.read(b)) > 0)
                out.write(b, 0, l);

            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (filename != null)
                req.getSession().removeAttribute(filename);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
                    IOException {
        doGet(req, resp);
    }

    /**
     * Displays the specified message in html
     */
    private void error(HttpServletResponse resp, String message) {
        String htmlMessage;

        htmlMessage = "<html>\n" + "<header>Error in generating report:</header>" + "<body>" +
                      message + "</body>" + "</html>";

        resp.setContentType("text/html");
        try {
            resp.getWriter().println(htmlMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a mime type based on the filename extension
     */
    protected String getContentType(String filename) {
        if (DataBaseUtil.isEmpty(filename))
            return "text/html";
        else if (filename.endsWith(".pdf"))
            return "application/pdf";
        else if (filename.endsWith(".xls"))
            return "application/vnd.ms-excel";
        else if (filename.endsWith(".xlsx"))
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        else if (filename.endsWith(".doc"))
            return "application/msword";
        else if (filename.endsWith(".docx"))
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        else if (filename.endsWith(".xml"))
            return "text/xml";
        else
            return "application/octet-stream";
    }
}