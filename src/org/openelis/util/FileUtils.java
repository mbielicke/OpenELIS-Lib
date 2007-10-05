/**
 * This class used to perform various file operations
 */
package org.openelis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class FileUtils {
    public void fileToStream(String filename, HttpServletResponse response) throws IOException {
        PrintWriter out;
        File file = null;
        BufferedReader formFile;
        String line;
        out = response.getWriter();
        response.setContentType("text/xml");
        file = new File(filename);
        formFile = new BufferedReader(new FileReader(file));
        while ((line = formFile.readLine()) != null) {
            out.println(line);
        }
        formFile.close();
    }
}
