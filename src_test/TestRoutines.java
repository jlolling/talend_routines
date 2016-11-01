import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileFilter;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import routines.GenericDateUtil;
import routines.NumberUtil;
import routines.RegexUtil;
import routines.StringCrypt;
import routines.StringUtil;
import routines.TimestampUtil;

public class TestRoutines {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(String.valueOf(123456789));
			//testNumberEquals();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testTimestampUtil() {
		System.out.println(TimestampUtil.getTimeDateByMinute(120));
	}
	
	public static void testCleanXML() {
		StringBuilder test = new StringBuilder(); 
		test.append("Hello World. ");
		test.append('\u0004');
		System.out.println(test);
		System.out.println(test.length());
		String test2 = StringUtil.stripNonValidXMLCharacters(test.toString());
		System.out.println(test2);
		System.out.println(test2.length());
	}
	
	public static void testBetween() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date test = new Date();
		try {
			Date start = sdf.parse("2015-03-31");
			Date end = sdf.parse("2015-04-01");
			System.out.println(TimestampUtil.between(test, start, end));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testGetDateAsInt() {
		Date date = new Date();
		System.out.println(TimestampUtil.getDateAsInt(date));
	}
	
	public static void testIntAsDate() {
		Integer i = 20151105;
		Integer d2 = TimestampUtil.addDays(i, 62);
		System.out.println(d2);
		Date d3 = TimestampUtil.getIntAsDate(d2);
		System.out.println(TimestampUtil.getDateAsInt(d3));
	}
	
	@Test
	public void testGetNextDay() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date test1 = sdf.parse("2002-01-01");
		Date result1 = sdf.parse("2002-01-02");
		assertEquals(result1, TimestampUtil.getNextDay(test1));

		Date test2 = sdf.parse("2002-12-31");
		Date result2 = sdf.parse("2003-01-01");
		assertEquals(result2, TimestampUtil.getNextDay(test2));

		// Start of DST in CEST
		Date test3 = sdf.parse("2016-03-27");
		Date result3 = sdf.parse("2016-03-28");
		assertEquals(result3, TimestampUtil.getNextDay(test3));

		// End of DST in CEST
		Date test4 = sdf.parse("2016-10-30");
		Date result4 = sdf.parse("2016-10-31");
		assertEquals(result4, TimestampUtil.getNextDay(test4));
	}

	public static void testRemoveMultipleSpaces() {
		String test = "\nJan Lolling  has spaces    X ";
		System.out.println(StringUtil.reduceMultipleSpacesToOne(test));
	}
	
	public static void testFillPadding() {
		String test = "1XXXXY";
		System.out.println(StringUtil.fillLeftPadding(test, 5, '0'));
		System.out.println(StringUtil.fillRightPadding(test, 5, '0'));
	}
	
	public static void testExtractRegexGroup1() {
		String test = "abc123_def456_20130304.csv";
		String regex = "([a-z0-9]{1,6})_([a-z0-9]{1,6})_([0-9]{8}).csv";
		String p1 = RegexUtil.extractByRegexGroup(test, regex, 1);
		String p2 = RegexUtil.extractByRegexGroup(test, regex, 2);
		String p3 = RegexUtil.extractByRegexGroup(test, regex, 3);
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
	}
	
	public static void testExtractRegexGroup2() {
		String test = "<br>Specific Text: blabla</br>";
		String regex = "<br>Specific Text: (.*)</br>";
		String p1 = RegexUtil.extractByRegexGroup(test, regex, 1);
		System.out.println(p1);
	}

	public static void testExtractRegexGroup3() {
		String test = "asdf{attr-1}_asda/{jobinst.anc_eid}";
		String regex = "(\\{[a-z0-9\\.\\-\\_]{1,}\\})";
		String p1 = RegexUtil.extractByRegexGroups(test, regex);
		System.out.println(p1);
	}

	public static void testSHA1() {
		String input = "aff";
		String hash = null;
		try {
			hash = StringUtil.sha1(input);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(hash);
	}

	public static void testFill() {
		String s = "1234567890";
//		String o = StringUtil.fillRigthSpaces(s, 10);
//		System.out.println("<" + o + ">" + " length=" + o.length());
	}
	
	public static void testCryptFileName() {
//		String name = "Bestandsanalyse_09900990099_2014-03-03.pdf"; // jq6FV0VQngxgRqP1b0oGpAq24tRVnnl28+hIeYca7Zd+qfC8k3CVdDgR5hefg83V
//		String name = "Bestandsanalyse_23456778876_2014-03-03.pdf"; // jq6FV0VQngxgRqP1b0oGpJ-xI1tZHG6b-EAZ09mHxbx+qfC8k3CVdDgR5hefg83V
//		String name = "Bestandsanalyse_23456778876_2014-03-04.pdf"; // jq6FV0VQngxgRqP1b0oGpJ-xI1tZHG6b-EAZ09mHxbwOhQQiy2ILyDgR5hefg83V
		String name = "23456778876_2014-03-05.pdf"; //                 n-EjW1kcbpv8QBnT2YfFvOC-U+hefNaeOBHmF5+DzdU=
		try {
			String encryptedName = StringCrypt.cryptToBcase64(name, "lolli").replace("/","-").replace(":", "#");
			System.out.println(encryptedName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testGetLastWorkday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			System.out.println(TimestampUtil.getLastWorkday(sdf.parse("2014-03-20")));
			System.out.println(TimestampUtil.getLastWorkday(sdf.parse("2014-03-21")));
			System.out.println(TimestampUtil.getLastWorkday(sdf.parse("2014-03-22")));
			System.out.println(TimestampUtil.getLastWorkday(sdf.parse("2014-03-23")));
			System.out.println(TimestampUtil.getLastWorkday(sdf.parse("2014-03-24")));
			System.out.println(TimestampUtil.getLastWorkday(sdf.parse("2014-03-25")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testGenericDateUtil() {
		String test1 = "01. Jan 2014";
		try {
			Date date = GenericDateUtil.parseDate(test1);
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testFileList() {
		File dir = new File("/var/testdata/many_files");
		dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	
	public static void testHttp() throws Exception {
		
		java.net.URL url_tHttpRequest_1 = new java.net.URL(
				"http://localhost:8080/");

		java.net.HttpURLConnection urlConn_tHttpRequest_1 = (java.net.HttpURLConnection) url_tHttpRequest_1
				.openConnection();

		urlConn_tHttpRequest_1.setRequestMethod("POST");
		urlConn_tHttpRequest_1.setDoOutput(true);
		urlConn_tHttpRequest_1.setDoInput(true);
		urlConn_tHttpRequest_1.setUseCaches(false);

		boolean connected_tHttpRequest_1 = false;
		int responseCode_tHttpRequest_1 = 0;
		String responseMessage_tHttpRequest_1 = null;
		try {
			urlConn_tHttpRequest_1.connect();
			connected_tHttpRequest_1 = true;
			responseMessage_tHttpRequest_1 = urlConn_tHttpRequest_1
					.getResponseMessage();

			byte[] buffer_tHttpRequest_1 = new byte[1024];
			int bos_buffer_tHttpRequest_1 = 0;

			if (java.net.HttpURLConnection.HTTP_OK == responseCode_tHttpRequest_1) {
				java.io.InputStream bis_tHttpRequest_1 = new java.io.BufferedInputStream(
						urlConn_tHttpRequest_1.getInputStream());
				while ((bos_buffer_tHttpRequest_1 = bis_tHttpRequest_1
						.read(buffer_tHttpRequest_1)) != -1) {
				}
				bis_tHttpRequest_1.close();
			} else {

				System.err.println(responseCode_tHttpRequest_1 + " "
						+ responseMessage_tHttpRequest_1);

			}

			urlConn_tHttpRequest_1.disconnect();
		} catch (Exception e) {

			System.err.println(e.getMessage());

		}
	}
	
	public static void testGetTimeAsSeconds() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			Date now = sdf.parse("2016-05-12 01:30:00.333");
			System.out.println(TimestampUtil.getTimeAsSeconds(now));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_getTimeDateByMinuteAndSeconds() {
		int minutes = 90;
		int seconds = 120;
		System.out.println(TimestampUtil.formatAsUTC(TimestampUtil.getTimeDateByMinuteAndSeconds(minutes, seconds), "HH:mm:ss"));
		System.out.println(TimestampUtil.getTimeDateByMinuteAndSeconds(minutes, seconds).getTime());
	}

	public static void test_getTimeDateByMinute() {
		int minutes = 90;
		System.out.println(TimestampUtil.format(TimestampUtil.getTimeDateByMinute(minutes), "HH:mm:ss"));
		System.out.println(TimestampUtil.getTimeDateByMinute(minutes).getTime());
	}
	
	public static void test_getSQLInList() {
		String test = "abc;def;xyz;;";
		String columnName = "name";
		String del = ";";
		System.out.println(StringUtil.buildSQLInListClause(test, del, columnName, false));
		System.out.println(StringUtil.buildSQLInListClause(test, del, columnName, true));
		System.out.println(StringUtil.buildSQLInListClause("  ", ".", columnName, false));
		
	}

	public static void testNumberEquals() {
		BigDecimal i1 = new BigDecimal(3);
		Long i2 = 1l;
		System.out.println(NumberUtil.equals(i1, i2));
	}
	
}
