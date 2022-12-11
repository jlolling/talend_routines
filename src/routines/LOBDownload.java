package routines;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;


public class LOBDownload {
	
	/**
	 * download the blob/clob content
	 * @param lobColumn a schema field of type Object containing the BLOB
	 * @param filePath the target file
	 * @param charset can be null
	 * @throws Exception
	 * 
	 * Downloads a LOB or CLOB object from the database
	 * Expect a schema column referring to the LOB object
	 * 
	 * {Category} LOBDownload
	 * 
	 * {param} object(lobColumn)
	 * {param} string(filePath)
	 * {param} string(charset)
	 * 
	 * {example} downloadLob(lobColumn, filePath, charset) # the file path downloaded
	 */
	public static String downloadLob(Object lobColumn, String filePath, String charset) throws Exception {
		if (lobColumn != null) {
			File file = new File(filePath);
			if (file.getParentFile().exists() == false) {
				file.getParentFile().mkdirs();
			}
			if (charset == null || charset.trim().isEmpty()) {
				charset = "UTF-8";
			}
			if (lobColumn instanceof Blob) {
				FileOutputStream os = new FileOutputStream(file);
				Blob blob = (Blob) lobColumn;
				InputStream is = null;
				try {
					is = blob.getBinaryStream();
					final byte[] buffer = new byte[1024];
					int length = -1;
					while ((length = is.read(buffer)) != -1) {
						os.write(buffer, 0, length);
					}
				} finally {
					if (os != null) {
						os.flush();
						os.close();
					}
					if (is != null) {
						is.close();
					}
				}
			} else if (lobColumn instanceof Clob) {
				Writer os = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
				Clob clob = (Clob) lobColumn;
				Reader is = null;
				try {
					is = clob.getCharacterStream();
					final char[] buffer = new char[1024];
					int length = -1;
					while ((length = is.read(buffer)) != -1) {
						os.write(buffer, 0, length);
					}
				} finally {
					if (os != null) {
						os.flush();
						os.close();
					}
					if (is != null) {
						is.close();
					}
				}
			} else if (lobColumn instanceof String) {
				Writer os = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
				Reader is = new StringReader((String) lobColumn);
				try {
					final char[] buffer = new char[1024];
					int length = -1;
					while ((length = is.read(buffer)) != -1) {
						os.write(buffer, 0, length);
					}
				} finally {
					if (os != null) {
						os.flush();
						os.close();
					}
					if (is != null) {
						is.close();
					}
				}
			} else if (lobColumn instanceof Byte[]) {
				FileOutputStream os = new FileOutputStream(file);
				try {
				    Byte[] array = (Byte[]) lobColumn;
				    for (Byte b : array) {
					    os.write(b);
				    }
				} finally {
					if (os != null) {
						os.flush();
						os.close();
					}
				}
			} else if (lobColumn instanceof byte[]) {
				FileOutputStream os = new FileOutputStream(file);
				try {
				    byte[] array = (byte[]) lobColumn;
				    os.write(array);
				} finally {
					if (os != null) {
						os.flush();
						os.close();
					}
				}
			} else {
				throw new Exception("Given object is not a Blob or Clob. It is of type: " + lobColumn.getClass().getName());
			}
			return file.getAbsolutePath();
		} else {
			return null;
		}
	}

}
