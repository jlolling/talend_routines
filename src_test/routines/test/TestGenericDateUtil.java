package routines.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import routines.GenericDateUtil;
import routines.GenericDateUtil.DateParser;

public class TestGenericDateUtil {
	
	@Test
	public void testTime() throws ParseException {
		String s = "4m 55s";
		Date result = GenericDateUtil.parseDuration(s, "mm'm'ss's'");
		long actual = result.getTime();
		long expected = 295000l;
		System.out.println("(1) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "4'55\""; // 4'55"
		result = GenericDateUtil.parseDuration(s, (String) null); 
		actual = result.getTime();
		System.out.println("(2) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "4' 55\""; // 4'55"
		result = GenericDateUtil.parseDuration(s, "HH:mm:ss"); 
		actual = result.getTime();
		System.out.println("(3) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "00:04:55"; 
		result = GenericDateUtil.parseDuration(s, "HH:mm:ss");
		actual = result.getTime();
		System.out.println("(4) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "23:59"; 
		result = GenericDateUtil.parseDuration(s, "HH:mm:ss");
		expected = 1439000l;
		actual = result.getTime();
		System.out.println("(5) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "4m 55s";
		result = GenericDateUtil.parseDuration(s, "HH'h'mm'm'ss's'");
		actual = result.getTime();
		expected = 295000l;
		System.out.println("(6) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "4′ 55″";
		result = GenericDateUtil.parseDuration(s, (String) null); 
		actual = result.getTime();
		System.out.println("(7) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "0455";
		result = GenericDateUtil.parseDuration(s, (String) null); 
		actual = result.getTime();
		System.out.println("(8) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "000455";
		result = GenericDateUtil.parseDuration(s, (String) null); 
		actual = result.getTime();
		System.out.println("(9) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "12:04:55 AM";
		result = GenericDateUtil.parseDuration(s, (String) null); 
		actual = result.getTime();
		System.out.println("(9) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "00:04:55 AM";
		result = GenericDateUtil.parseDuration(s, (String) null); 
		actual = result.getTime();
		System.out.println("(9) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "04:55";
		result = GenericDateUtil.parseDuration(s, (String) null); 
		actual = result.getTime();
		System.out.println("(9) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "12:04:55 PM";
		result = GenericDateUtil.parseDuration(s, (String) null); 
		actual = result.getTime();
		expected = 43495000l;
		System.out.println("(9) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "00:55";
		expected = 55000l;
		result = GenericDateUtil.parseDuration(s, (String) null); 
		actual = result.getTime();
		System.out.println("(9) Time in ms: " + actual);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDate() throws ParseException {
		String s = "2016-12-11 13:26:11";
		Long actual = GenericDateUtil.parseDate(s, "yyyy-MM-dd HH:mm:ss").getTime();
		Long expected = 1481459171000l;
		assertEquals(expected, actual);
	}
	
	@Test
	public void testInvalidDate() throws ParseException {
		String s = "2016-13-11 13:26:11";
		DateParser p =  GenericDateUtil.getDateParser(false);
		try {
			p.setLenient(false);
			Date actual = p.parseDate(s, "yyyy-MM-dd HH:mm:ss");
			System.out.println(actual);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testDateYearAndFull() throws Exception {
		String s1 = "2011";
		String s2 = "03.04.2011";
		Date date1 = GenericDateUtil.parseDate(s1, "dd.MM.yyyy");
		Date date2 = GenericDateUtil.parseDate(s2, "dd.MM.yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("date1: " + sdf.format(date1));
		System.out.println("date2: " + sdf.format(date2));
		assertTrue(date1.before(date2));
		s1 = "01.01.2011";
		date1 = GenericDateUtil.parseDate(s1, "dd.MM.yyyy");
		System.out.println("date1: " + sdf.format(date1));
		System.out.println("date2: " + sdf.format(date2));
		assertTrue(date1.before(date2));
		s1 = "2011-01-01";
		date1 = GenericDateUtil.parseDate(s1, "dd.MM.yyyy");
		System.out.println("date1: " + sdf.format(date1));
		System.out.println("date2: " + sdf.format(date2));
		assertTrue(date1.before(date2));
		s1 = "01/14/2011";
		date1 = GenericDateUtil.parseDate(s1, "dd.MM.yyyy");
		System.out.println("date1: " + sdf.format(date1));
		System.out.println("date2: " + sdf.format(date2));
		assertTrue(date1.before(date2));
	}
	
	@Test
	public void testDetectZeroAsNull() throws ParseException {
		String test = "00000000";
		Date actual = GenericDateUtil.parseDate(test);
		assertTrue(actual == null);
	}

	@Test
	public void testZeroDate() throws Exception {
		String test = "0000-00-00";
		Date actual = GenericDateUtil.parseDate(test);
		if (actual != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			System.out.println(sdf.format(actual));
			assertTrue(false);
		} else {
			assertTrue(true);
		}
	}
	
}
