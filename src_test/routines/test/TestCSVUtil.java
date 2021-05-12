package routines.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import routines.CSVUtil;

public class TestCSVUtil {

	@Test
	public void testCountColunms() throws Exception {
		String data = ";12345;\"abcd\";true;";
		int expected = 5;
		int actual = CSVUtil.getCountColumns(data, ';', '\"');
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCountColunmsAllEmpy() throws Exception {
		String data = ";;;;";
		int expected = 5;
		int actual = CSVUtil.getCountColumns(data, ';', '\"');
		assertEquals(expected, actual);
	}

	@Test
	public void testCountColunmsContentNull() throws Exception {
		String data = null;
		int expected = 0;
		int actual = CSVUtil.getCountColumns(data, ';', '\"');
		assertEquals(expected, actual);
	}

	@Test
	public void testCountColunmsDelimiterInContent() throws Exception {
		String data = ";12345;\"ab;cd\";true;";
		int expected = 5;
		int actual = CSVUtil.getCountColumns(data, ';', '\"');
		assertEquals(expected, actual);
	}

}
