package org.openelis.gwt.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
        FileItem file = null;
        FileItemFactory factory;
        ServletFileUpload upload;
        String service = null, method = null;
        Object serviceInst;

        try {
            factory = new DiskFileItemFactory();
            upload = new ServletFileUpload(factory);
            files = upload.parseRequest(req);

            /*
             * File name, service, and method are required from front 
             */
            file = files.get(0);
            for (int i = 1; i < files.size(); i++ ) {
                if ("service".equals(files.get(i).getFieldName()))
                    service = files.get(i).getString();
                else if ("method".equals(files.get(i).getFieldName()))
                    method = files.get(i).getString();
            }
        } catch (Exception e) {
            throw (ServletException)e.getCause();
        }

        /*
         * call the method with the file name parameter for processing
         */
        try {
            serviceInst = Class.forName(service).newInstance();
            serviceInst.getClass().getMethod(method, new Class[]{FileItem.class}).invoke(serviceInst, new Object[]{file});
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            if (e.getCause() != null)
                throw new ServletException(e.getCause());
            else
                throw new ServletException(e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new ServletException("NoSuchMethodException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
                    IOException {
        doGet(req, resp);
    }
}
