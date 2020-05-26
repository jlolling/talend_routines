package routines.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
	public void testExtractByRegexGroups2() {
		String pattern = "(-Xmx[0-9]{1,6}[mG])";
		String test = "java -Xmx2G -cp somejars";
		String expected = "-Xmx2G";
		String actual = RegexUtil.extractByRegexGroups(test, pattern);
		System.out.println(actual);
		assertEquals("Result wrong", expected, actual);
	}

	@Test
	public void testExtractByRegexGroups3() {
		String pattern = ":([0-9]{1,})$";
		String test = "xyz-:9999sdfsdf987-users:45678";
		String expected = "45678";
		String actual = RegexUtil.extractByRegexGroups(test, pattern);
		System.out.println(actual);
		assertEquals("Result wrong", expected, actual);
	}

	@Test
	public void testExtractByRegexGroupsToList() {
		String pattern = "(-Xmx[0-9]{1,6}[mG])";
		String test = "java -Xmx2G -cp somejars -Xmx512m";
		List<String> result = RegexUtil.extractByRegexGroupsToList(test, pattern);
		assertEquals(2, result.size());
		assertEquals("Result wrong", "-Xmx2G", result.get(0));
		assertEquals("Result wrong", "-Xmx512m", result.get(1));
	}

	@Test
	public void testReplaceByRegexGroups() {
		String pattern = "(-Xmx[0-9]{1,6}[mG])";
		String test = "java -Xmx128m -cp somejars";
		String expected = "java -Xmx2G -cp somejars";
		String actual = RegexUtil.replaceByRegexGroups(test, pattern, "-Xmx2G");
		System.out.println(actual);
		assertEquals("Result wrong", expected, actual);
	}

}
