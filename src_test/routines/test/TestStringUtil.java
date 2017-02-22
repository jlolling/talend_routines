package routines.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import routines.StringUtil;

public class TestStringUtil {

	@Test
	public void testStripNonValidXMLCharacters() {
		System.out.println("#### testStripNonValidXMLCharacters");
		StringBuilder test = new StringBuilder(); 
		test.append("Hello World. ");
		test.append('\u0004');
		String test2 = StringUtil.stripNonValidXMLCharacters(test.toString());
		assertEquals(13, test2.length());
	}
	
	@Test
	public void testRemoveMultipleSpaces() {
		System.out.println("#### testRemoveMultipleSpaces");
		String test = "\nJan Lolling  has spaces    X ";
		String expected = "Jan Lolling has spaces X";
		String actual = StringUtil.reduceMultipleSpacesToOne(test);
		assertEquals(expected, actual);
	}

	@Test
	public void testFillPadding() {
		System.out.println("#### testFillPadding");
		String test = "1XXY";
		String actual = StringUtil.fillLeftPadding(test, 5, '0');
		String expected = "01XXY";
		assertEquals(expected, actual);
		actual = StringUtil.fillRightPadding(test, 5, '0');
		expected = "1XXY0";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testInWithChain() {
		String test = "xyz ";
		String chain = "aaa|bbb|xyz |111";
		boolean actual = StringUtil.inChain(test, '|', chain);
		assertTrue(actual);
	}
	
}
