package routines.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import routines.NumberUtil;

public class TestNumberUtil {

	@Test
	public void testSortLongNumberList() throws Exception {
		String test = "333333,111111,222222,5";
		String expected = "5,111111,222222,333333";
		String actual = NumberUtil.sortLongNumberList(test);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRoundScaleN() {
		double expected = 6.68d;
		double test = 6.675000000000003d;
		int precision = 2;
		double actual = NumberUtil.roundScaleN(test, precision);
		assertEquals(expected, actual, 0.00001d);
	}

	@Test
	public void testEqualsWithPrecision() {
		double t1 = 9d;
		double t2 = 9.234567d;
		int precision = 1;
		assertTrue("Negative test failed", NumberUtil.equals(t1, t2, precision) == false);
		t1 = 6.674999999999999d;
		t2 = 6.675000000000003d;
		assertTrue("Positive test failed", NumberUtil.equals(t1, t2, precision));
	}

	@Test
	public void testNumber2String() {
		double d = 100000000d;
		String expected = "100000000";
		String actual = NumberUtil.numberToString(d, false, false, 4);
		System.out.println(actual);
		assertEquals("Format wrong", expected, actual);
	}
	
	@Test
	public void testStringToDouble() {
		String test = "1234.567-";
		double expected = -1234.567d;
		double actual = NumberUtil.getNullSaveDouble(test);
		System.out.println(actual);
		assertEquals(expected, actual,0.001d);
	}

}
