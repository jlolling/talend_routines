package routines.test;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import routines.GenericDateUtil;

public class TestGenericDateUtil {
	
	@Test
	public void testTime() throws ParseException {
		String s = "4m55s";
		Date result = GenericDateUtil.parseTime(s, "mm'm'ss's'");
		System.out.println("Time in ms: " + result.getTime());
		assertTrue(true);
	}

}
