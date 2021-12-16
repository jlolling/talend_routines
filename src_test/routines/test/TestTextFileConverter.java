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
	public void createTestFile() throws Exception {
		String content = "abcd\ndefg\nijabkhcd\n987";
		testFile = File.createTempFile("textfileconverter", ".txt");
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
	public void testReplaceWithLineFeed() throws Exception {
		String search = "cd\n";
		String replace = "cd_";
		String expected = "abcd_defg\nijabkhcd_987";
		TextFileConverter c = new TextFileConverter();
		c.setSourcePath(testFile.getAbsolutePath());
		c.setTargetPath(testFile.getAbsolutePath());
		c.addReplacement(search, replace);
		c.convert();
		String actual = FileUtil.readContentfromFile(testFile.getAbsolutePath(), null);
		System.out.println(actual);
		assertEquals("Convert failed", expected.trim(), actual.trim());
	}

	@Test
	public void testReplaceAtStart() throws Exception {
		String search = "ab";
		String replace = "AB";
		String expected = "ABcd\ndefg\nijABkhcd\n987";
		TextFileConverter c = new TextFileConverter();
		c.setSourcePath(testFile.getAbsolutePath());
		c.setTargetPath(testFile.getAbsolutePath());
		c.addReplacement(search, replace);
		c.convert();
		String actual = FileUtil.readContentfromFile(testFile.getAbsolutePath(), null);
		System.out.println(actual);
		assertEquals("Convert failed", expected.trim(), actual.trim());
	}

	@Test
	public void testReplaceAtMiddle() throws Exception {
		String search = "ij";
		String replace = "IJ";
		String expected = "abcd\ndefg\nIJabkhcd\n987";
		TextFileConverter c = new TextFileConverter();
		c.setSourcePath(testFile.getAbsolutePath());
		c.setTargetPath(testFile.getAbsolutePath());
		c.addReplacement(search, replace);
		c.convert();
		String actual = FileUtil.readContentfromFile(testFile.getAbsolutePath(), null);
		System.out.println(actual);
		assertEquals("Convert failed", expected.trim(), actual.trim());
	}

}
