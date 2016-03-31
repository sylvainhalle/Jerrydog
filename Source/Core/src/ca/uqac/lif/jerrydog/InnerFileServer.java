/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.jerrydog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class InnerFileServer extends Server
{
  protected String m_resourceFolder;

  protected Class<? extends InnerFileServer> m_referenceClass;
  
  public InnerFileServer(Class<? extends InnerFileServer> reference, boolean caching_enabled)
  {
    super();
    m_resourceFolder = "resource";
    InnerFileCallback ifc = new InnerFileCallback("", this.getClass());
    ifc.setEnabled(caching_enabled);
    registerCallback(0, ifc);
    m_referenceClass = this.getClass();    
  }

  protected InnerFileServer(Class<? extends InnerFileServer> c)
  {
    super();
    m_resourceFolder = "resource";
    registerCallback(0, new InnerFileCallback(m_resourceFolder, c));
    m_referenceClass = c;
  }
  
  public InputStream getResourceAsStream(String path)
  {
    return m_referenceClass.getResourceAsStream(path);
  }
  
  public String getResourceFolderName()
  {
    return m_resourceFolder;
  }

  public static byte[] readBytes(InputStream is)
  {
    int nRead;
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] data = new byte[2048];
    try
    {
      while ((nRead = is.read(data, 0, data.length)) != -1)
      {
        buffer.write(data, 0, nRead);
      }
      buffer.flush();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return buffer.toByteArray();
  }
  
  static class PackageFileReader
  {
    public static String readPackageFile(Class<?> c, String path)
    {
      InputStream in = c.getResourceAsStream(path);
      String out;
      try
      {
        out = readPackageFile(in);
      }
      catch (IOException e)
      {
        e.printStackTrace();
        return null;
      }
      return out;
    }
    
    public static byte[] readPackageFileToBytes(Class<?> c, String path)
    {
      InputStream in = getResourceAsStream(c, path);
      byte[] file_contents = null;
      if (in != null)
      {
        file_contents = InnerFileServer.readBytes(in);
      }
      return file_contents;
    }
    
    public static InputStream getResourceAsStream(Class<?> c, String path)
    {
      InputStream in = c.getResourceAsStream(path);
      return in;
    }
    
    /**
     * Reads a file and puts its contents in a string
     * @param in The input stream to read
     * @return The file's contents, and empty string if the file
     * does not exist
     */
    public static String readPackageFile(InputStream in) throws IOException
    {
      if (in == null)
      {
        throw new IOException();
      }
      java.util.Scanner scanner = null;
      StringBuilder out = new StringBuilder();
      try
      {
        scanner = new java.util.Scanner(in, "UTF-8");
        while (scanner.hasNextLine())
        {
          String line = scanner.nextLine();
          out.append(line).append(System.getProperty("line.separator"));
        }
      }
      finally
      {
        if (scanner != null)
          scanner.close();
      }
      return out.toString();
    }
  }
}
