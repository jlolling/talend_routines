package routines.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import routines.RegexUtil;

public class TestRegexUtil {
	
	@Test
	public void testExtractByRegexGroups() {
		String pattern = ",([A-Z0-9_-a-z\\s]*)}";
		String test = "{SSO_ID,ID Fremd-VG}";
		String expected = "ID Fremd-VG";
		String actual = RegexUtil.extractByRegexGroups(test, pattern);
		System.out.println(actual);
		assertEquals("Result wrong", expected, actual);
	}
	
	@Test
	public void testReplaceByRegexGroups() {
		String pattern = "(-Xmx[0-9]{1,6}[mG])";
		String test = "{SSO_ID,ID Fremd-VG}";
		String expected = "ID Fremd-VG";
		String actual = RegexUtil.extractByRegexGroups(test, pattern);
		System.out.println(actual);
		assertEquals("Result wrong", expected, actual);
	}

}
