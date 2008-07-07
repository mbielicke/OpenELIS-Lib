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
