package routines.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

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

}