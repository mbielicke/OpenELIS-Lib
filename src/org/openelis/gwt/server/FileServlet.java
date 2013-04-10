package org.openelis.gwt.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * This class provide the parse capability for file upload. This class works
 * with file upload widget to send files from the client to server.
 */

public class FileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
                                                                                  IOException {
        List<FileItem> files;
        FileItemFactory factory;
        ServletFileUpload upload;

        try {
            factory = new DiskFileItemFactory();
            upload = new ServletFileUpload(factory);
            files = upload.parseRequest(req);
            if (files.size() > 0)
                req.getSession().setAttribute("upload", files.get(0));
        } catch (Exception e) {
            throw (ServletException)e.getCause();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
                                                                                   IOException {
        doGet(req, resp);
    }
}