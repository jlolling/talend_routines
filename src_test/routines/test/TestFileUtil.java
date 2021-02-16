package routines.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;

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
	
	@Test
	public void testBuildFilePath() {
		String expected = "/p1/p2/p3/p4/file.txt";
		String actual = FileUtil.buildPath("file.txt", "/p1", "p2/", "/p3", "p4");
		System.out.println(actual);
		assertEquals("Path not correct", expected, actual);
	}

	@Test
	public void testGetUnixPath1() {
		String expected = "/p1/p2/p3/file.txt";
		String actual = FileUtil.getUnixPath("d:\\p1\\p2\\p3\\file.txt");
		System.out.println(actual);
		assertEquals("Path not correct", expected, actual);
	}

	@Test
	public void testGetUnixPath2() {
		String expected = "/p1/p2/p3/file.txt";
		String actual = FileUtil.getUnixPath("/p1/p2/p3/file.txt");
		System.out.println(actual);
		assertEquals("Path not correct", expected, actual);
	}

	@Test
	public void testFileUnreadable() {
		String path = "/Data/Talend/testdata/test/unreadble_dir/";
		boolean actual = FileUtil.fileReadable(path);
		assertEquals("Path check failed", false, actual);
	}

	@Test
	public void testFileReadableAndWritable() {
		String path = "/Data/Talend/testdata/test/";
		boolean actual = FileUtil.fileReadableAndWritable(path);
		assertEquals("Path check failed", true, actual);
	}

	@Test
	public void testEnsureDirectoryExists1() throws Exception {
		String expected = "/Data/Talend/testdata/test-create/";
		String actual = FileUtil.ensureDirectoryExists(expected);
		assertEquals("Path check failed", expected, actual);
		File d = new File(actual);
		d.delete();
	}

	@Test
	public void testEnsureDirectoryExists2() throws Exception {
		String expected = "/Data/Talend/testdata/test-create";
		String actual = FileUtil.ensureDirectoryExists(expected);
		assertEquals("Path check failed", expected, actual);
		File d = new File(actual);
		d.delete();
	}
	
	@Test
	public void testConvertToBase64() throws Exception {
		String path = "/Data/Talend/testdata/test/excel/store_report.txt";
		System.out.println("Start convert file...");
		String actual = FileUtil.getBytesAsBase64(path);
		System.out.println("Read expected content...");
		String expected = FileUtil.readContentfromFile(path + ".base64", null);
		System.out.println("Compare content...");
		System.out.println("actual content length: " + actual.length());
		System.out.println("expected content length: " + expected.length());
		assertEquals("Check failed", expected, actual);
	}
	
	@Test
	public void testReadReverseOneLine() throws Exception {
		String expected = "Carrington Frozen Mushroom Pizza|19-12-1997|Gayle Watson|11.19|3.9165|3.0|2.8571428571";
		String path = "/Data/Talend/testdata/test/excel/store_report.txt";
		String actual = FileUtil.readReverseNumberLines(path, 1, null);
		System.out.println(actual);
		assertEquals("Check failed", expected, actual);
	}

	@Test
	public void testReadReverse3Lines() throws Exception {
		String expected = "Carrington Frozen Mushroom Pizza|19-12-1997|Gayle Watson|11.19|3.9165|3.0|2.8571428571\n" +
				          "Landslide Extra Chunky Peanut Butter|19-12-1997|Gayle Watson|2.19|0.9636|3.0|2.2727272727\n" +
                          "Plato Grape Jam|19-12-1997|Gayle Watson|7.92|3.4848|4.0|2.2727272727";
		String path = "/Data/Talend/testdata/test/excel/store_report.txt";
		String actual = FileUtil.readReverseNumberLines(path, 3, null);
		System.out.println(actual);
		assertEquals("Check failed", expected, actual);
	}
	
	@Test
	public void testWaitForFile1() throws Exception {
		File tempFile = File.createTempFile("test", "xyz");
		String actual = FileUtil.waitForFileFinishedWriting(tempFile.getAbsolutePath(), false, 100);
		System.out.println(actual);
		assertTrue(true);
	}
	
	@Test
	public void testWaitForFile2() throws Exception {
		File tempFile = File.createTempFile("test", "xyz");
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("Start writing file....");
				try {
					FileOutputStream o = new FileOutputStream(tempFile);
					for (int i = 0; i < 1000; i++) {
						o.write(i);
						o.flush();
						Thread.sleep(10);
					}
					o.close();
					System.out.println("write finished: " + System.currentTimeMillis());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		t.start();
		System.out.println("Start checking file....");
		String actual = FileUtil.waitForFileFinishedWriting(tempFile.getAbsolutePath(), false, 100);
		System.out.println("wait finished: " + System.currentTimeMillis());
		System.out.println(actual);
		assertTrue(true);
		tempFile.delete();
	}
	
	@Test
	public void testWaitForEndOfFile() throws Exception {
		File tempFile = File.createTempFile("test", "xyz");
		final String end = "End";
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("Start writing file....");
				try {
					FileOutputStream o = new FileOutputStream(tempFile);
					for (int i = 0; i < 1000; i++) {
						o.write(i);
						o.flush();
						Thread.sleep(10);
					}
					o.write(end.getBytes());
					o.close();
					System.out.println("write finished: " + System.currentTimeMillis());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		t.start();
		System.out.println("Start checking file....");
		FileUtil.waitForEndOfFile(tempFile.getAbsolutePath(), end, null);
		System.out.println("wait finished: " + System.currentTimeMillis());
		assertTrue(true);
		tempFile.delete();
	}
	
	@Test
	public void testRemoveBOM() throws Exception {
		String contentWithoutBOM = "more-text üöä";
		String contentWithBOM = "\uFEFF"+contentWithoutBOM;
		String sourceFilePath = File.createTempFile("testRemoveBOM", ".txt").getAbsolutePath();
		FileUtil.writeContentToFile(sourceFilePath, contentWithBOM, null);
		boolean removed = FileUtil.removeBOM(sourceFilePath, null, null);
		assertTrue(removed);
		String actual = FileUtil.readContentfromFile(sourceFilePath, "UTF-8");
		assertEquals("Result file content not correct", contentWithoutBOM, actual);
	}

}
