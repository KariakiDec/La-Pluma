package cn.earthsky.dev.project.lapluma.common.url;

/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2003 Alexander Maryanovsky.
 * All rights reserved.
 *
 * The utillib library is free software; you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * The utillib library is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with utillib library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * <p>A URLStreamHandler which allows accessing specific zip entries within a
 * zip file. The format of the file part of the URL is
 * <code>[full path to the zip file]|[name of zip entry]</code>, but you are
 * encouraged to use the
 * <code>createURL(String fileName, String entryName)</code> method
 * instead of creating a URL manually. The host and port part of the URL should
 * be <code>null</code>. An application is responsible for setting a
 * <code>URLStreamHandlerFactory</code> that will return a ZipURLStreamHandler
 * when appropriate.
 */

public class ZipURLStreamHandler extends URLStreamHandler{


    static{
        URL.setURLStreamHandlerFactory(protocol -> {
            if(Objects.equals(protocol, "zip")){
               return new ZipURLStreamHandler();
            }
            return null;
        });
    }

    /**
     * Returns a ZipURLConnection for the specified URL.
     */

    public URLConnection openConnection(URL url) throws IOException{
        String urlFile = url.getFile();
        int barIndex = urlFile.indexOf("|");
        if (barIndex == -1)
            throw new MalformedURLException("Missing '|'");

        String fileName = urlFile.substring(0, barIndex);
        String entryName = urlFile.substring(barIndex + 1);
        return new ZipURLConnection(url, new File(fileName), entryName);
    }



    /**
     * Parses the specified URL string.
     */

    protected void parseURL(URL url, String spec, int start, int limit){
        String urlFile = url.getFile();
        int barIndex = urlFile.indexOf("|");

        String fileName = barIndex == -1 ? urlFile : urlFile.substring(0, barIndex);
        String entryName = barIndex == -1 ? "" : urlFile.substring(barIndex + 1);

        int lastSlashIndex = entryName.lastIndexOf("/");
        String newEntryName = entryName.substring(0, lastSlashIndex + 1) + spec.substring(start, limit);

        setURL(url, url.getProtocol(), "", -1, fileName + "|" + newEntryName, null);
    }



    /**
     * Creates a <code>URL</code> that points to the specified entry within the
     * specified zip file. The <code>URL</code> will have "zip" as the protocol
     * name. To use the resulting <code>URL</code>, an application must set
     * a <code>URLStreamHandlerFactory</code> (via the <code>URL</code> class)
     * which will return a <code>ZipURLStreamHandler</code> for the "zip"
     * protocol. Returns <code>null</code> if the "zip: protocol is unrecognized.
     */

    public static URL createURL(String fileName, String entryName){
        return createURL("zip", fileName, entryName);
    }



    /**
     * Creates a <code>URL</code> that points to the specified entry within the
     * specified zip file and has the specified protocol name. To use the
     * resulting <code>URL</code>, an application must set a
     * <code>URLStreamHandlerFactory</code> (via the <code>URL</code> class)
     * which will return a <code>ZipURLStreamHandler</code> for the protocol name
     * given to this method. Returns <code>null</code> if the specified protocol
     * is unrecognized.
     */

    public static URL createURL(String protocol, String fileName, String entryName){
        try{
            return new URL(protocol, "", -1, fileName + "|" + entryName);
        } catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }



}
/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2003 Alexander Maryanovsky.
 * All rights reserved.
 *
 * The utillib library is free software; you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * The utillib library is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with utillib library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */



/**
 * A <code>URLConnection</code> which loads its content from a specified zip
 * entry within a local file.
 */

class ZipURLConnection extends URLConnection{



    /**
     * The zip file.
     */

    private final File file;



    /**
     * The name of the zip entry within the zip file.
     */

    private final String zipEntryName;



    /**
     * The <code>ZipFile</code> object for the zip file. Created when
     * <code>connect()</code> is called.
     */

    private ZipFile zipFile = null;



    /**
     * The <code>ZipEntry</code> object for the entry in the zip file. Created
     * when <code>connect()</code> is called.
     */

    private ZipEntry zipEntry = null;



    /**
     * Creates a new <code>ZipURLConnection</code> for the specified zip entry
     * within the specified <code>File</code>.
     */

    public ZipURLConnection(URL url, File file, String zipEntryName){
        super(url);

        this.file = file;
        this.zipEntryName = zipEntryName;
    }



    /**
     * Attempts to open the zip entry.
     */

    public void connect() throws IOException{
        this.zipFile = new ZipFile(file);
        this.zipEntry = zipFile.getEntry(zipEntryName);
        if (zipEntry == null)
            throw new IOException("Entry " + zipEntryName + " not found in file " + file);
        this.connected = true;
    }



    /**
     * Returns the <code>InputStream</code> that reads from this connection.
     */

    public InputStream getInputStream() throws IOException{
        if (!connected)
            connect();

        return zipFile.getInputStream(zipEntry);
    }



    /**
     * Returns the length of the uncompressed zip entry.
     */

    public int getContentLength(){
        if (!connected)
            return -1;

        return (int)zipEntry.getSize();
    }


}