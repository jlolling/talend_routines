package routines.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import routines.GenericDateUtil;
import routines.TimestampUtil;

public class TestTimestampUtil {

	@Test
	public void testIsOverlapping() throws ParseException {
		System.out.println("#### testIsOverlapping");
		String dr1s = "2016-01-01";
		String dr1e = "2016-02-01";
		String dr2s = "2016-01-15";
		String dr2e = "2016-02-15";
		boolean actual = TimestampUtil.isOverlapping(dr1s, dr1e, dr2s, dr2e);
		assertTrue("Overlapping failed", actual);
		dr2s = "2017-01-15";
		dr2e = "2017-02-15";
		actual = TimestampUtil.isOverlapping(dr1s, dr1e, dr2s, dr2e);
		assertTrue("No overlapping failed", actual == false);
		dr2s = "2016-01-15";
		dr2e = "2016-01-17";
		actual = TimestampUtil.isOverlapping(dr1s, dr1e, dr2s, dr2e);
		assertTrue("Include failed", actual);
		dr2s = "2016-01-01";
		dr2e = "2016-02-01";
		dr1s = "2017-01-15";
		dr1e = "2017-02-15";
		actual = TimestampUtil.isOverlapping(dr1s, dr1e, dr2s, dr2e);
		assertTrue("No overlapping failed", actual == false);
		dr1s = "2016-01-15";
		dr1e = "2016-01-17";
		actual = TimestampUtil.isOverlapping(dr1s, dr1e, dr2s, dr2e);
		assertTrue("Include failed", actual);
	}

	@Test
	public void testGetOverlappingRange() throws ParseException {
		System.out.println("#### testGetOverlappingRange");
		String dr1s = "2016-01-01";
		String dr1e = "2016-02-01";
		String dr2s = "2016-01-15";
		String dr2e = "2016-02-15";
		Date[] range = TimestampUtil.getOverlappingRange(dr1s, dr1e, dr2s, dr2e);
		assertNotNull(range);
		Date expectedRangeStart = GenericDateUtil.parseDate("2016-01-15");
		Date expectedRangeEnd = GenericDateUtil.parseDate("2016-02-01");
		assertEquals(expectedRangeStart, range[0]);
		assertEquals(expectedRangeEnd, range[1]);
	}

	@Test
	public void testGetNextDay() throws ParseException {
		System.out.println("#### testGetNextDay");
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

	@Test
	public void testBetween() throws Exception {
		System.out.println("#### testBetween");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = sdf.parse("2015-03-31");
		Date end = sdf.parse("2015-04-01");
		Date test = sdf.parse("2015-03-31");
		boolean actual = TimestampUtil.between(test, start, end);
		assertTrue(actual);
		test = sdf.parse("2015-05-31");
		actual = TimestampUtil.between(test, start, end);
		assertTrue(actual == false);
	}
	
	@Test
	public void testGetTimeByLongTime() {
		System.out.println("#### testGetTimeByLongTime");
		Date d = TimestampUtil.getTimeByLongTime(178000l);
		System.out.println(d);
		String expected = "02:58";
		String actual = TimestampUtil.format(d, "mm:ss");
		System.out.println(actual);
		assertEquals("Time wrong", expected, actual);
	}


	@Test
	public void testGetDatetimeAsLong() throws ParseException {
		System.out.println("#### testGetDatetimeAsLong");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date test = sdf.parse("20171201164211");
		long expected = 20171201164211l;
		long actual = TimestampUtil.getDatetimeAsLong(test);
		assertEquals("Time wrong", expected, actual);
	}

	@Test
	public void testGetTimeAsSeconds() throws ParseException {
		System.out.println("#### testGetTimeAsSeconds");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date test = sdf.parse("2016-08-01 00:05:00.000");
		long expected = 300l;
		long actual = TimestampUtil.getTimeAsSeconds(test);
		assertEquals("Time wrong", expected, actual);
	}

}
