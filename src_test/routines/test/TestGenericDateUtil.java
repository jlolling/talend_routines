package routines.test;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import routines.GenericDateUtil;

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
		result = GenericDateUtil.parseDuration(s, null); 
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
		result = GenericDateUtil.parseDuration(s, null); 
		actual = result.getTime();
		System.out.println("(7) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "0455";
		result = GenericDateUtil.parseDuration(s, null); 
		actual = result.getTime();
		System.out.println("(8) Time in ms: " + actual);
		assertEquals(expected, actual);
		s = "000455";
		result = GenericDateUtil.parseDuration(s, null); 
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

}
