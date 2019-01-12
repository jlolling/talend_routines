package routines.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import routines.FileUtil;

public class TestFileUtil {
	
	@Test
	public void testGetFileExtension() {
		String test = "/path/to/file.xlsx";
		String expected = "xlsx";
		String actual = FileUtil.getFileExtension(test);
		assertEquals("Filled extension not correct", expected, actual);
		test = ".xlsx";
		expected = "xlsx";
		actual = FileUtil.getFileExtension(test);
		assertEquals("Only extension expected", expected, actual);
		test = "file";
		expected = "";
		actual = FileUtil.getFileExtension(test);
		assertEquals("Missing extension expected", expected, actual);
		test = "file.";
		expected = "";
		actual = FileUtil.getFileExtension(test);
		assertEquals("Empty extension expected", expected, actual);
	}
	
	@Test
	public void testFileExists() throws Exception {
		File test = File.createTempFile("TestFileUtil_", "test");
		boolean actual = FileUtil.doesFileExist(test.getAbsolutePath());
		boolean expected = true;
		assertTrue("Existing file check (ful path) failed", (actual == expected));
		actual = FileUtil.doesFileExist(test.getParent(), test.getName());
		assertTrue("Existing file check (path+name) failed", (actual == expected));
		String missing = "/path/to/missing";
		actual = FileUtil.doesFileExist(missing);
		expected = false;
		assertTrue("Missing file check (ful path) failed", (actual == expected));
	}

	@Test
	public void testFileUtilGetName() throws Exception {
		String actual = FileUtil.getFileName("\\path\\to\\file");
		String expected = "file";
		assertEquals("Existing file check (ful path) failed", expected, actual);
	}
	
	@Test
	public void testWriteAndReadContentToFromFile() throws Exception {
		String filePath = "/tmp/test_write_content_to_file";
		String expectedContent = "Bla Bla\nNothing";
		FileUtil.writeContentToFile(filePath, expectedContent, null);
		String actualContent = FileUtil.readContentfromFile(filePath, null);
		assertEquals("Content doers not match", expectedContent, actualContent);
	}

}
