/**
 * Copyright 2015 Jan Lolling jan.lolling@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package routines;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		} else if ("null".equals(input)) {
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
		if (input == null) {
			return null;
		} else {
			return input.toUpperCase();
		}
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
		if (input == null) {
			return null;
		} else {
			return input.toLowerCase();
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
	 * {param} string("value") in: String.
	 * {param} char('|') delimiter: char
	 * {param} string("value1|value2|value3") valueChain: String.
	 * 
	 * {example} inChain(" test", '|', "value1|value2|test|value3") # true
	 */
	public static boolean inChain(String test, char delimiter, String valueChain) {
		if (test != null && valueChain != null) {
			StringTokenizer st = new StringTokenizer(valueChain, String.valueOf(delimiter));
			while (st.hasMoreTokens()) {
				String value = st.nextToken();
				if (value != null && value.trim().isEmpty() == false) {
					if (test.trim().equals(value.trim())) {
						return true;
					}
				}
			}
		}
		return false;
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
	 * {example} in(" test","hans"," TEST ","Tata") # true
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
	 * Returns the index of test within the list of possible values
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} int | Integer
	 * 
	 * {param} string("to_test") test: String.
	 * 
	 * {param} string("possibleValue") posibleValues: String.
	 * 
	 * {example} index(" test","hans"," TEST ","Tata") # true
	 */
	public static int index(String test, String... possibleValues) {
		int index = -1;
		if (test != null) {
			for (String v : possibleValues) {
				index++;
				if (v != null && test.trim().equalsIgnoreCase(v.trim())) {
					break;
				}
			}
		}
		return index;
	}

	/**
	 * Returns the index of test within the list of possible values
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} int | Integer
	 * 
	 * {param} string("1234") test: String.
	 * 
	 * {param} string("1234,5678,8766") arrayStr: String.
	 * 
	 * {example} indexInConcatenated("1234","1234,5678,8766") # true
	 */
	public static int indexInConcatenated(String test, String arrayStr, String delimiter) {
		if (test == null || arrayStr == null) {
			return -1;
		}
		if (delimiter == null || delimiter.isEmpty()) {
			delimiter = ",";
		}
		String[] array = arrayStr.split(delimiter);
		for (int i = 0; i < array.length; i++) {
			if (test.equalsIgnoreCase(array[i])) {
				return i;
			}
		}
		return -1;
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
	 * {Category} StringUtil
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
				sb.append(nf.format(o));
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
		if (s.trim().equals("null")) {
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
	 * creates an SQL in clause including the needed column name
	 * @param separatedValueList list of values separated
	 * @param delimiter separator 
	 * @param columnName database column name to apply
	 * @param numbers values are numbers
	 * @return SQL in clause
	 * 
     * {Category} StringUtil
     * 
     * {talendTypes} String
     * 
     * {param} String("listvalues") separatedValueList
     * {param} String(";") delimiter
     * {param} String("name") columnName
     * {param} Boolean(false) numbers
     * 
     * {example} buildSQLInListClause("listvalues", ";", "name", true) 
	 */
	public static String buildSQLInListClause(String separatedValueList, String delimiter, String columnName, boolean numbers) {
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(separatedValueList, delimiter);
		boolean firstLoop = true;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			token = token.trim();
			if (token != null && token.isEmpty() == false) {
				if (firstLoop) {
					firstLoop = false;
					sb.append(" ");
					sb.append(columnName);
					sb.append(" in (");
				} else {
					sb.append(",");
				}
				if (numbers) {
					sb.append(token);
				} else {
					sb.append("'");
					sb.append(token);
					sb.append("'");
				}
			}
		}
		if (firstLoop) {
			// there was no entry in the list
			sb.append(" 1=0 ");
		} else {
			sb.append(")");
		}
		return sb.toString();
	}
	
	/**
	 * returns the length of a String null save
	 * @param test list of values separated
	 * @return length. In case of test is null it returns also 0
	 * 
     * {Category} StringUtil
     * 
     * {talendTypes} String
     * 
     * {param} String(test)
     * 
     * {example} length(test) 
	 */
	public static int length(String test) {
		if (test == null) {
			return 0;
		} else {
			return test.length();
		}
	}
	
	/**
	 * Compares to version strings in form 1.2.3
	 * @param version1
	 * @param version2
	 * @return 0= if both versions are equal, -1= if version 1 is lower than version 2, 1= version 1 is higher the version 2
	 * 
	 * {Category} StringUtil
	 * {talendTypes} int
	 * {param} String(version1)
	 * {param} String(version2)
	 * {example} compareVersions(v1,v2) 
	 */
	public static int compareVersions(String version1, String version2) throws Exception {
		if (isEmpty(version1) == false && isEmpty(version2)) {
			return 1;
		} else if (isEmpty(version1) && isEmpty(version2) == false) {
			return -1;
		} else if (isEmpty(version1) && isEmpty(version2)) {
			return 0;
		}
		try {
			Pattern pattern = Pattern.compile("([0-9]{1,})");
			Matcher m1 = pattern.matcher(version1);
			Matcher m2 = pattern.matcher(version2);
			int[] version1_array = new int[3];
			int v1_seq = 0;
			while (m1.find()) {
				version1_array[v1_seq++] = Integer.parseInt(m1.group());
			}
			int[] version2_array = new int[3];
			int v2_seq = 0;
			while (m2.find()) {
				version2_array[v2_seq++] = Integer.parseInt(m2.group());
			}
			for (int i = 0; i < version1_array.length; i++) {
				if (version1_array[i] < version2_array[i]) {
					return -1;
				} else if (version1_array[i] > version2_array[i]) {
					return 1;
				}
			}
			return 0;
		} catch (Exception e) {
			//throw e;
			return 0;
		}
	}
	

	/**
	 * Checks if the test contains the token as whole word
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} boolean | Boolean
	 * 
	 * {param} string(test) test: String.
	 * 
	 * {param} string(token) posibleValues: String.
	 * 
	 * {example} containsToken(test, token) # true
	 */
	public static boolean containsToken(String test, String token) {
		String tokenLimiter = "[\\s\\.,;/|:<_>\\(\\)\\{\\}\\-]";
		if (isEmpty(test) == false && isEmpty(token) == false) {
			String token0 = Pattern.quote(token);
			StringBuilder regex = new StringBuilder();
			regex.append("^(");
			regex.append(token0);
			regex.append(")");
			regex.append(tokenLimiter);
			regex.append("|");
			regex.append(tokenLimiter);
			regex.append("(");
			regex.append(token0);
			regex.append(")");
			regex.append(tokenLimiter);
			regex.append("|");
			regex.append(tokenLimiter);
			regex.append("(");
			regex.append(token0);
			regex.append(")$");
			regex.append("|");
			regex.append("^(");
			regex.append(token0);
			regex.append(")$");
			int pos = RegexUtil.matchesByRegex(test, regex.toString());
			if (pos != -1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the test contains the token as whole word
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} boolean | Boolean
	 * 
	 * {param} string(test) test: String.
	 * 
	 * {param} list(tokenList) posibleValues: String.
	 * 
	 * {example} containsToken(test, tokenList) # true
	 */
	public static String containsTokens(String test, List<String> tokenList) {
		if (test != null) {
			for (String token : tokenList) {
				if (containsToken(test, token)) {
					return token;
				}
			}
		}
		return null;
	}

	/**
	 * Checks if the test contains the token as whole word
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} boolean | Boolean
	 * 
	 * {param} string(test) test: String.
	 * 
	 * {param} list(tokenList) posibleValues: String.
	 * 
	 * {example} containsString(test, tokenList) # true
	 */
	public static String containsStrings(String test, List<String> stringList) {
		if (test != null) {
			for (String s : stringList) {
				if (test.contains(s)) {
					return s;
				}
			}
		}
		return null;
	}
	
	/**
	 * Checks if the test contains the token as whole word
	 * 
	 * {Category} StringUtil
	 * 
	 * {talendTypes} boolean | Boolean
	 * 
	 * {param} string(test) test: String.
	 * 
	 * {param} list(tokenList) posibleValues: String.
	 * 
	 * {example} containsString(test, tokenList) # true
	 */
	public static String containsString(String test, String string) {
		if (test != null && string != null) {
			if (test.toLowerCase().contains(string.toLowerCase())) {
				return string;
			}
		}
		return null;
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
     * {example} getEscapedTextForSQL(text) 
     * 
     */
	public static String getEscapedTextForSQL(String text) {
		if (text == null) {
			return "";
		}
		text = text.replace("'", "''");
		return text;
	}
	
	/**
     * Replicates the patternToReplicate, replaces a part of the text.
     * @param patternToReplicate will be replicated as often as replacementText Strings are existing
     * @param inBetweenText if not null it will be put between the replicated text parts
     * @param placeHolder place holder for the text to replace
     * @param replacementText text which replaces a placeholder, this text will be trimmed within the method!
     * @param replacementDelimiter delimiter to detect multiple values within replacementText
     * @return new text build of the replicated (and partly replaced) patternToReplicate, the inBetweenText
     * 
     * {Category} StringUtil
     * 
     * {talendTypes} String
     * 
     * {param} String(patternToReplicate) text
     * {param} String(inBetweenText) text
     * {param} String(placeHolder) text
     * {param} String(replacementText) text
     * {param} String(replacementDelimiter) text
     * 
     * {example} replaceAndReplicate(patternToReplicate,inBetweenText,placeHolder,replacementText,replacementDelimiter)
     */
	public static String replaceAndReplicate(String patternToReplicate, String inBetweenText, String placeHolder, String replacementText, String replacementDelimiter) {
		if (isEmpty(patternToReplicate) == false) {
			StringBuilder result = new StringBuilder();
			if (isEmpty(replacementDelimiter)) {
				replacementDelimiter = ",;|";
			}
			StringTokenizer st = new StringTokenizer(replacementText, replacementDelimiter);
			boolean firstLoop = true;
			while (st.hasMoreTokens()) {
				String oneReplacement = st.nextToken().trim();
				if (firstLoop) {
					firstLoop = false;
				} else if (isEmpty(inBetweenText) == false) {
					result.append(inBetweenText);
				}
				if (isEmpty(placeHolder) == false) {
					String oneResult = patternToReplicate.replace(placeHolder, oneReplacement);
					result.append(oneResult);
				} else {
					result.append(patternToReplicate);
				}
			}
			return result.toString();
		} else {
			return null;
		}
	}
	
    private final static int[] sOutputEscapes128;
    static {
        int[] table = new int[128];
        // Control chars need generic escape sequence
        for (int i = 0; i < 32; ++i) {
            // 04-Mar-2011, tatu: Used to use "-(i + 1)", replaced with constant
            table[i] = -1;
        }
        /* Others (and some within that range too) have explicit shorter
         * sequences
         */
        table['"'] = '"';
        table['\\'] = '\\';
        // Escaping of slash is optional, so let's not add it
        table[0x08] = 'b';
        table[0x09] = 't';
        table[0x0C] = 'f';
        table[0x0A] = 'n';
        table[0x0D] = 'r';
        sOutputEscapes128 = table;
    }
    private final static char[] HC = "0123456789ABCDEF".toCharArray();

	/**
     * Quotes a text for a json attribute name or value
     * @param content the content to qouted
     * @return qouted json value
     * 
     * {Category} StringUtil
     * 
     * {talendTypes} String
     * 
     * {param} String(content) content
     * 
     * {example} quoteForJson(content)
     */
    public static String quoteForJson(String content) {
    	StringBuilder sb = new StringBuilder();
        final int[] escCodes = sOutputEscapes128;
        int escLen = escCodes.length;
        for (int i = 0, len = content.length(); i < len; ++i) {
            char c = content.charAt(i);
            if (c >= escLen || escCodes[c] == 0) {
                sb.append(c);
                continue;
            }
            sb.append('\\');
            int escCode = escCodes[c];
            if (escCode < 0) { // generic quoting (hex value)
                // The only negative value sOutputEscapes128 returns
                // is CharacterEscapes.ESCAPE_STANDARD, which mean
                // appendQuotes should encode using the Unicode encoding;
                // not sure if this is the right way to encode for
                // CharacterEscapes.ESCAPE_CUSTOM or other (future)
                // CharacterEscapes.ESCAPE_XXX values.

                // We know that it has to fit in just 2 hex chars
                sb.append('u');
                sb.append('0');
                sb.append('0');
                int value = c;  // widening
                sb.append(HC[value >> 4]);
                sb.append(HC[value & 0xF]);
            } else { // "named", i.e. prepend with slash
                sb.append((char) escCode);
            }
        }
        return sb.toString();
	}
	
}