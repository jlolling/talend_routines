package routines.test;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import routines.GenericDateUtil;

public class TestGenericDateUtil {
	
	@Test
	public void testTime() throws ParseException {
		String s = "4m55s";
		Date result = GenericDateUtil.parseTime(s, "mm'm'ss's'");
		long actual = result.getTime();
		long expected = 295000l;
		System.out.println("Time in ms: " + actual);
		assertEquals(expected, actual);
	}

	
	@Test
	public void testTimeInSec() throws ParseException {
		String s = "04:55";
		Long actual = GenericDateUtil.parseTime(s, "mm:ss", "HH:mm:ss").getTime();
		Long expected = 295000l;
		assertEquals(expected, actual);
		s = "00:04:55";
		actual = GenericDateUtil.parseTime(s, "mm:ss", "HH:mm:ss").getTime();
		assertEquals(expected, actual);
	}

}
