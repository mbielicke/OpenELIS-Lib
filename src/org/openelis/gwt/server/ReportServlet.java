package org.openelis.gwt.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
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
    
    private static final Logger log = Logger.getLogger("openelis");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
                    IOException {
        Path path;
        ReportStatus status;
        String key, contentType, attachment;
        OutputStream out;

        key = null;
        out = null;
        path = null;
        try {
            /*
             * make sure the requested file is in user session (we have
             * generated the file)
             */
            key = req.getParameter("file");
            if (DataBaseUtil.isEmpty(key)) {
                error(resp, "Missing file name; please report this error to Lab IT");
                return;
            }

            status = (ReportStatus)req.getSession().getAttribute(key);
            if (status == null || status.getStatus() != ReportStatus.Status.SAVED) {
                error(resp, "Specified file key is not valid; please report this error to Lab IT");
                return;
            }

            try {
                path = Paths.get(status.getPath());
            } catch (InvalidPathException e) {
                error(resp, "Specified file is not valid; please report this error to Lab IT");
                log.severe("File " + status.getPath() + " not found");
                return;
            }

            /*
             * stream the file
             */
            contentType = getContentType(key);
            resp.setContentType(contentType);

            attachment = req.getParameter("attachment");
            if ( !DataBaseUtil.isEmpty(attachment))
                resp.setHeader("Content-Disposition", "attachment;filename=\"" + removeCRLF(attachment) +
                               "\"");
            else
                resp.setHeader("Content-Disposition", "filename=\"" + status.getMessage() + "\"");

            /*
             * TODO: Remove this when we no longer support IE8 Headers are set
             * to fix IE8 download bug
             */
            resp.setHeader("Pragma", "token");
            resp.setHeader("Cache-control", "private");

            out = resp.getOutputStream();
            Files.copy(path, out);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new ServletException(e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();
                if (key != null)
                    req.getSession().removeAttribute(key);
                if (path != null)
                    Files.delete(path);
            } catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage(), e);
            }
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
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Returns a mime type based on the filename extension
     */
    protected String getContentType(String filename) {
        String temp;
        
        temp = filename.toLowerCase();
        if (DataBaseUtil.isEmpty(temp))
            return "text/html";
        else if (temp.endsWith(".pdf"))
            return "application/pdf";
        else if (temp.endsWith(".xls"))
            return "application/vnd.ms-excel";
        else if (temp.endsWith(".xlsx"))
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        else if (temp.endsWith(".doc"))
            return "application/msword";
        else if (temp.endsWith(".docx"))
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        else if (temp.endsWith(".xml"))
            return "text/xml";
        else if (temp.endsWith(".png"))
            return "image/png";
        else
            return "application/octet-stream";
    }
    
    /**
     * Remove cr/lf from attachment
     */
    protected String removeCRLF(String attachment) {
        return attachment.replaceAll("[\n\r]", "");
    }
}