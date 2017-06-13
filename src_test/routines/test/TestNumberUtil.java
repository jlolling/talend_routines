package routines.test;

import static org.junit.Assert.assertEquals;

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

}
