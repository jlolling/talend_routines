package routines;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;

public class StringUtil {

	/**
	 * Specifies a substring consisting of the first n characters of a string.
	 * 
	 * {Category} StringUtil
	 * 
	 * {param} string("hello world!") string: String.
	 * 
	 * {param} int(5) index : index
	 * 
	 * {example} startFrom("hello world!",5) # hello
	 */
	public static String startFrom(String string, int index) {
		return string == null ? null : string.substring(0,
				Math.min(string.length(), index));
	}

	/**
	 * Retrieves a value from a URL query string.
	 * 
	 * {Category} StringUtil
	 * 
	 * {param} string("url") url: String.
	 * 
	 * {param} string("key") key: String
	 * 
	 * {example} getValueFromUrl(
	 * "http://host/query?key1=value1&key2=value2&key3=value3,"key2") # value2
	 */
	public static String getValueFromUrl(String url, String key) {
		if (url == null) {
			return "";
		}
		if (key == null) {
			return "";
		}
		String value = "";
		try {
			url = java.net.URLDecoder.decode(url.trim(), "ASCII");
		} catch (UnsupportedEncodingException e) {
			new RuntimeException("Wrong Encoding configured", e);
		}
		String lowerUrl = url.toLowerCase();
		key = key.trim().toLowerCase();
		int pos0 = lowerUrl.indexOf("&" + key);
		if (pos0 == -1) {
			pos0 = lowerUrl.indexOf('?' + key);
		}
		int pos1 = 0;
		if (pos0 != -1) {
			// an url need a = to start the value
			pos1 = lowerUrl.indexOf('&', pos0 + 1); // next parameter
			if (pos1 == -1) {
				pos1 = lowerUrl.length();
			}
			if (pos1 != -1) {
				value = url.substring(pos0 + 1 + key.length() + 1, pos1);
			}
		}
		return value;
	}

	/**
	 * Returns a empty not null String if input is null or equals "null",
	 * otherwise returns input
	 * 
	 * {Category} StringUtil
	 * 
	 * {param} string(input) input: String.
	 * 
	 * {example} getNullSaveStr(null) # ""
	 */
	public static String getNullSaveStr(String input) {
		if (input == null) {
			return "";
		} else if ("null".equalsIgnoreCase(input)) {
			return "";
		} else {
			return input;
		}
	}

	/**
	 * returns the upper case string, is null save
	 * 
	 * {Category} StringUtil
	 * 
	 * {param} string(input) input: String.
	 * 
	 * {example} toUpperCase("abc") # "ABC"
	 */
	public static String toUpperCase(String input) {
		String s = getNullSaveStr(input);
		return s.toUpperCase();
	}

	/**
	 * returns the lower case string, is null save
	 * 
	 * {Category} StringUtil
	 * 
	 * {param} string(input) input: String.
	 * 
	 * {example} toLowerCase("AbC") # "abc"
	 */
	public static String toLowerCase(String input) {
		String s = getNullSaveStr(input);
		return s.toLowerCase();
	}

	/**
	 * Returns the relative path from a full path based on a base path
	 * 
	 * {Category} StringUtil
	 * 
	 * {param} string(fullPath) fullPath: String.
	 * 
	 * {param} string(basePath) basePath: String.
	 * 
	 * {example} getRelativePath(basePath, fullPath) # ""
	 */
	public static String getRelativePath(String fullPath, String basePath) {
		if (fullPath == null || fullPath.trim().isEmpty()) {
			return null;
		}
		if (basePath == null || basePath.trim().isEmpty()) {
			return fullPath;
		}
		// normalize path
		fullPath = fullPath.replaceAll("\\\\", "/").trim();
		basePath = basePath.replaceAll("\\\\", "/").trim();
		fullPath = fullPath.replaceAll("[/]{2,}", "/").trim();
		fullPath = fullPath.replaceAll("/./", "/").trim();
		basePath = basePath.replaceAll("[/]{2,}", "/").trim();
		basePath = basePath.replaceAll("/./", "/").trim();
		if (basePath.endsWith("/")) {
			basePath = basePath.substring(0, basePath.length() - 1);
		}
		int pos = fullPath.indexOf(basePath);
		if (pos == -1) {
			throw new IllegalArgumentException(
					"fullPath does not contains basePath!");
		}
		return fullPath.substring(pos + basePath.length() + 1);
	}

	/**
	 * Returns the name of file without path
	 * 
	 * {Category} StringUtil
	 * 
	 * {param} string(fullPath) fullPath: String.
	 * 
	 * {example} getFileName(fullPath) # ""
	 */
	public static String getFileName(String filePath) {
		File f = new File(filePath);
		return f.getName();
	}

	/**
	 * Returns the name of file without path and extension
	 * 
	 * {Category} StringUtil
	 * 
	 * {param} string(fullPath) fullPath: String.
	 * 
	 * {example} getFileNameWithoutExt(fullPath) # ""
	 */
	public static String getFileNameWithoutExt(String filePath) {
		File f = new File(filePath);
		String name = f.getName();
		int pos = name.lastIndexOf('.');
		if (pos > 0) {
			return name.substring(0, pos);
		} else {
			return name;
		}
	}

	/**
	 * Returns the directory of the file
	 * 
	 * {Category} StringUtil
	 * 
	 * {param} string(fullPath) fullPath: String.
	 * 
	 * {example} getFileDir(fullPath) # ""
	 */
	public static String getFileDir(String filePath) {
		File f = new File(filePath);
		String parent = f.getParent();
		if (parent != null) {
			return parent;
		} else {
			return "";
		}
	}

	/**
	 * checks if the first string is in the list of following strings (case
	 * insensitive regardless of white spaces)
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} boolean | Boolean
	 * 
	 * {param} string("to_test") test: String.
	 * 
	 * {param} string("possibleValue") posibleValues: String.
	 * 
	 * {example} int(" test","hans"," TEST ","Tata") # true
	 */
	public static boolean in(String test, String... possibleValues) {
		if (test != null) {
			for (String v : possibleValues) {
				if (v != null && test.trim().equalsIgnoreCase(v.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Retrieves the host from a URL
	 * 
	 * {Category} StringUtil
	 * 
	 * {param} string(url) url: String.
	 * 
	 * {example} getHost(url) # DE
	 */
	public static String getHost(String urlString) {
		if (urlString == null) {
			return null;
		}
		urlString = urlString.trim().toLowerCase();
		if (urlString.startsWith("http://") == false) {
			urlString = "http://" + urlString;
		}
		String decUrl = null;
		try {
			decUrl = java.net.URLDecoder.decode(urlString, "ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("getHost failed: " + e.getMessage(), e);
		}
		java.net.URL url = null;
		try {
			url = new java.net.URL(decUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException("getHost failed: " + e.getMessage(), e);
		}
		return url.getHost();
	}

	/**
	 * Retrieves the top level domain from a URL
	 * 
	 * {Category} StringUtil
	 * 
	 * {param} string(url) url: String.
	 * 
	 * {example} getTLD(url) # DE
	 */
	public static String getTLD(String urlString) {
		String host = getHost(urlString);
		String tld = null;
		if (host != null) {
			int pos = host.lastIndexOf('.');
			if (pos != -1) {
				tld = host.substring(pos + 1);
			} else {
				tld = host;
			}
		}
		return tld;
	}

	/**
	 * returns the first not empty string
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} string("testMe1","testMe2") strings: String.
	 * 
	 * {example} coalesce(" test","hans"," TEST ","Tata") # "test"
	 */
	public static String coalesce(String... strings) {
		for (String s : strings) {
			if (s != null) {
				s = s.trim();
				if (isEmpty(s) == false) {
					return s;
				}
			}
		}
		return null;
	}

	/**
	 * shows if string substring from another string {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} string("cdf") small: String. {param} string("abcdfegf") large:
	 * String.
	 *
	 * {param} isSubString(part,content) strings: String.
	 * 
	 */
	public static boolean isSubString(String small, String large) {
		int lastj = small.length() - 1;
		int lasti = large.length() - small.length();
		int i = 0;
		WHILE1: while (i <= lasti) {
			if (large.charAt(i) == small.charAt(0)) {
				int j = 1;
				while (j <= lastj) {
					if (large.charAt(i + j) != small.charAt(j)) {
						i++;
						continue WHILE1;
					} else {
						j++;
					}
				}
				return true;
			} else {
				i++;
			}
		}
		return false;
	}

	/**
	 * returns a string which works as hash based key
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} object("value1","value2") strings: String.
	 * 
	 * {example} getHashBasedKey(aString,aDouble,aDate,anObject) # 2323133_18
	 */
	public static String getHashBasedKey(Object... objects) {
		StringBuilder sb = new StringBuilder();
		for (Object o : objects) {
			if (o instanceof String) {
				sb.append((String) o);
				sb.append('_');
			} else if (o instanceof java.util.Date) {
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
						"yyyyMMdd_HHmmss");
				sb.append(sdf.format((java.util.Date) o));
			} else if (o instanceof Number) {
				NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
				sb.append(nf.format((Number) o));
			} else if (o != null) {
				sb.append(o.toString());
			} else {
				sb.append("\\N");
			}
		}
		int sbLength = sb.length();
		// build hash code and add salt
		return sb.toString().hashCode() + "_" + sbLength;
	}

	/**
	 * returns true if the string is not filled or contains "null"
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} object("value1") strings: String.
	 * 
	 * {example} isEmpty(aString) # 2323133_18
	 */
	public static boolean isEmpty(String s) {
		if (s == null) {
			return true;
		}
		if (s.trim().isEmpty()) {
			return true;
		}
		if (s.trim().equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}

	/**
	 * returns a UTF-8 encoded string
	 * 
	 * @param text
	 *            text with potential UTF16 encoded chars
	 * @return text in UTF-8
	 * 
	 *         {Category} StringUtil
	 * 
	 *         {talendTypes} String
	 * 
	 *         {param} String("value1") text: String.
	 * 
	 *         {example} getUTF_8(aString) # 2323133_18
	 */
	public static String getUTF_8(String text) {
		if (text == null) {
			return null;
		}
		if (text.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		java.nio.charset.Charset cs = java.nio.charset.Charset.forName("UTF-8");
		// replaces all not UTF-8 chars to standard replacement chars
		java.nio.ByteBuffer bb = cs.encode(text);
		// get it back as char buffer
		java.nio.CharBuffer cb = cs.decode(bb);
		while (cb.hasRemaining()) {
			sb.append(cb.get()); // add chars to output buffer
		}
		return sb.toString();
	}

	/**
	 * This method ensures that the output String has only valid XML unicode
	 * characters as specified by the XML 1.0 standard. For reference, please
	 * see <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the
	 * standard</a>. This method will return an empty String if the input is
	 * null or empty.
	 *
	 * @param in The String whose non-valid characters we want to remove.
	 * @return The in String, stripped of non-valid characters. 
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} String("value1") text: String.
	 * 
	 * {example} stripNonValidXMLCharacters(aString) # 2323133_18
	 */
	public static String stripNonValidXMLCharacters(String in) {
		if (in == null) {
			return null;
		}
		if (in.isEmpty()) {
			return "";
		}
		StringBuffer out = new StringBuffer();
		char current;
		char next = ' ';
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i);
			if (i < in.length() - 1) {
				next = in.charAt(i + 1);
			}
			if (current == '\\') {
				if (next != 'n' && next != 'r' && next != 't' && next != '\"' && next != '\\') {
					current = ' ';
				}
			}
			if ((current == 0x9) || (current == 0xA) || (current == 0xD)
					|| ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF))) {
				out.append(current);
			}
		}
		return out.toString();
	}

	public static String reduceMultipleSpacesToOne(String text) {
		text = text.trim();
		int pos = 0;
		while (pos != -1) {
			text = text.replace("  ", " ");
			pos = text.indexOf("  ");
		}
		return text;
	}

	/**
	 * returns true if the string points to an archive file
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} boolean
	 * 
	 * {param} String(file) strings: String.
	 * 
	 * {example} isArchiveFile(context.currentFile) # 2323133_18
	 */
	public static boolean isArchiveFile(String file) {
		if (file != null) {
			return file.toLowerCase().endsWith(".zip")
					|| file.toLowerCase().endsWith(".gz");
		} else {
			return false;
		}
	}

	/**
	 * returns a modified name
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} String(fileName) fileName: String. {param} String(somethingToAdd)
	 * somethingToAdd: String.
	 * 
	 * {example} addNamePartBeforeExtension(context.currentFile, str) #
	 */
	public static String addNamePartBeforeExtension(String fileName,
			String somethingToAdd) {
		int pos = fileName.lastIndexOf(".");
		if (pos != -1 && pos < fileName.length()) {
			String name = fileName.substring(0, pos);
			String ext = fileName.substring(pos);
			return name + somethingToAdd + ext;
		} else {
			return fileName + somethingToAdd;
		}
	}

	/**
	 * returns the sha-1 hash for an input string name
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} String(input) fileName: String.
	 * 
	 * {example} sha1(str) #
	 */
	public static String sha1(String input) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}

	/**
	 * returns a fixed length string, filled at the start with given char
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} String(input) input: String. {param} int(10) length: String.
	 * {param} char('-') length: String.
	 * 
	 * {example} fillLeftPadding(input, 10, '-') #
	 */
	public static String fillLeftPadding(String input, int length, char c) {
		if (input == null) {
			input = "";
		}
		if (input.length() > length) {
			return input;
		}
		StringBuilder sb = new StringBuilder(length);
		sb.append(input);
		for (int i = sb.length(); i < length; i++) {
			sb.insert(0, c);
		}
		return sb.toString();
	}

	/**
	 * returns a fixed length string, filled at the end with given char
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} String(input) input: String. {param} int(10) length: String.
	 * {param} char('-') length: String.
	 * 
	 * {example} fillRightPadding(input, 10, '-') #
	 */
	public static String fillRightPadding(String input, int length, char c) {
		if (input == null) {
			input = "";
		}
		if (input.length() > length) {
			return input;
		}
		StringBuilder sb = new StringBuilder(length);
		sb.append(input);
		for (int i = sb.length(); i < length; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * returns a encoded String for the usage in an email subject.
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} String("subject") subjectText: String. 
	 * 
	 * {example} buildEmailSubject(subjectText) #
	 */
	public static String buildEmailSubject(String subjectText) {
		return buildEmailSubject(subjectText, null);
	}

	/**
	 * returns a encoded String for the usage in an email subject.
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} String("subject") subjectText: String. {param} String("utf-8")
	 * charset: String. default utf-8
	 * 
	 * {example} buildEmailSubject(subjectText, charset) #
	 */
	public static String buildEmailSubject(String subjectText, String charset) {
		if (charset == null) {
			charset = "utf-8";
		}
		if (subjectText == null) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("=?");
			sb.append(charset);
			sb.append("?B?");
			try {
				sb.append(Base64.encodeToBase64String(subjectText
						.getBytes(charset)));
				sb.append("?=");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return sb.toString();
		}
	}

	/**
	 * converts string value into a Boolean
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} Boolean
	 * 
	 * {param} String("true") aString: String.
	 * 
	 * {example} getNullSaveBoolean(aString) # true
	 */
	public Boolean getNullSaveBoolean(String value) throws Exception {
		if (value == null) {
			return null;
		}
		value = value.toLowerCase();
		if ("true".equals(value)) {
			return Boolean.TRUE;
		} else if ("false".equals(value)) {
			return Boolean.FALSE;
		} else if ("1".equals(value)) {
			return Boolean.TRUE;
		} else if ("0".equals(value)) {
			return Boolean.FALSE;
		} else if ("yes".equals(value)) {
			return Boolean.TRUE;
		} else if ("y".equals(value)) {
			return Boolean.TRUE;
		} else if ("sí".equals(value)) {
			return Boolean.TRUE;
		} else if ("да".equals(value)) {
			return Boolean.TRUE;
		} else if ("no".equals(value)) {
			return Boolean.FALSE;
		} else if ("нет".equals(value)) {
			return Boolean.FALSE;
		} else if ("n".equals(value)) {
			return Boolean.FALSE;
		} else if ("ja".equals(value)) {
			return Boolean.TRUE;
		} else if ("j".equals(value)) {
			return Boolean.TRUE;
		} else if ("nein".equals(value)) {
			return Boolean.FALSE;
		} else if ("oui".equals(value)) {
			return Boolean.TRUE;
		} else if ("non".equals(value)) {
			return Boolean.FALSE;
		} else if ("ok".equals(value)) {
			return Boolean.TRUE;
		} else if ("x".equals(value)) {
			return Boolean.TRUE;
		} else if (value != null) {
			return Boolean.FALSE;
		} else {
			return null;
		}
	}

	/**
	 * limits the message text to avoid overflow database field
	 * @param size to limit
	 * @param cutPosition 'b' at beginning, 'm' cuts in the middle, 'e' cuts at the end
 	 * @return limited text
 	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} String(message) message: String.
	 * {param} size(1000) size: max size of the message
	 * {param} cutPosition('m')
	 * 
	 * {example} enforceTextLength(message, 1000, 'm') # text
	 */
	public static String enforceTextLength(String message, int size, char cutPosition) {
		if (message != null && message.trim().isEmpty() == false) {
			message = message.trim();
			if (message.length() > size) {
				size = size - 3; // to have space for "..."
				if (cutPosition == 'e') {
					return message.substring(0, size) + "...";
				} else if (cutPosition == 'm') {
					StringBuilder sb = new StringBuilder();
					sb.append(message.substring(0, size / 2));
					sb.append("...");
					sb.append(message.substring(message.length() - size / 2, message.length()));
					return sb.toString();
				} else if (cutPosition == 'b') {
					return "..." + message.substring(message.length() - size);
				} else {
					throw new IllegalArgumentException("Unknown cutPosition: " + cutPosition);
				}
			} else {
				return message;
			}
		} else {
			return null;
		}
	}
	
	/**
     * Get the next rolling sequence value
     * If the value reaches the max value, the sequence resets it self
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} int
     * 
     * {param} String("seq1") sequenceName: Name of the sequence
     * {param} String("Value1","Value2","Value3","Value4") sequenceName: Name of the sequence
     * 
     * {example} getNextRollingSequenceValue("seq1", 20) result: current value
     * 
     */
	public static String getRollingElementFromList(String sequenceName, String ...values) {
		if (values != null && values.length > 0) {
			int index = NumberUtil.getNextRollingSequenceValue(sequenceName, values.length - 1);
			return values[index];
		} else {
			return "";
		}
	}

	/**
     * Replace the german umlauts
     * 
     * {Category} StringUtil
     * 
     * {talendTypes} String
     * 
     * {param} String("äöü") text
     * 
     * {example} replaceGermanUmlauts(text) 
     * 
     */
	public static String replaceGermanUmlauts(String text) {
		text = text.replace("Ä", "AE");
		text = text.replace("Ü", "UE");
		text = text.replace("Ö", "OE");
		text = text.replace("ä", "ae");
		text = text.replace("ü", "ue");
		text = text.replace("ö", "oe");
		text = text.replace("ß", "sz");
		return text;
	}

	/**
     * Escape line breaks
     * 
     * {Category} StringUtil
     * 
     * {talendTypes} String
     * 
     * {param} String("text") text
     * 
     * {example} getEscapeTextForJSON(text) 
     * 
     */
	public static String getEscapeTextForJSON(String text) {
		if (text == null) {
			return null;
		}
		text = text.replace("\n", "\\n");
		text = text.replace("\"", "\\\"");
		return text;
	}

}