package routines.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import routines.FileUtil;
import routines.TextFileConverter;

public class TestTextFileConverter {
	
	private File testFile = null;
	
	@Before
	public void createTestFileRegex() throws Exception {
		String content = "168171|W115315  | PMX0112730|24339805|1|CDM\n168172|W995315  | PMX9912730 | AS3X|24339806|1|CDM";
		testFile = File.createTempFile("textfileconverterRegx", ".txt");
		System.out.println("Test file: " + testFile.getAbsolutePath());
		FileUtil.writeContentToFile(testFile.getAbsolutePath(), content, null);
	}
	
	@After
	public void cleanup() {
		if (testFile != null) {
			testFile.delete();
		}
	}
	
	@Test
	public void testReplaceRegex1() throws Exception {
		String search = "|";
		String replace = "";
		String regex = "^[0-9]{4,8}\\|(.*)\\|[0-9]{8}\\|";
		String expected = "168171|W115315 PMX0112730|24339805|1|CDM\n168172|W995315 PMX9912730 AS3X|24339806|1|CDM";
		TextFileConverter c = new TextFileConverter();
		c.setSourcePath(testFile.getAbsolutePath());
		c.setTargetPath(testFile.getAbsolutePath());
		c.addReplacement(search, replace, regex, true);
		c.convert();
		String actual = FileUtil.readContentfromFile(testFile.getAbsolutePath(), null);
		System.out.println(actual);
		assertEquals("Convert failed", expected.trim(), actual.trim());
	}

}
