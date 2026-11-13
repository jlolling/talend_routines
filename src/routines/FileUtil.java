package routines;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.io.PushbackInputStream;

/**
 * Copyright 2021 Jan Lolling jan.lolling@gmail.de
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
public class FileUtil {

	/**
	 * Returns the file extension of file
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} string(filePath) fullPath: String.
	 * 
	 * {example} getFileExtension(filePath) # ""
	 */
	public static String getFileExtension(String filePath) {
		String name = getFileName(filePath);
		int pos = name.lastIndexOf('.');
		if (pos >= 0 && pos < name.length() - 1) {
			return name.substring(pos + 1);
		} else {
			return "";
		}
	}

	/**
	 * Returns the directory of the file
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} string(fullPath) fullPath: String.
	 * 
	 * {example} getFileDir(fullPath) # ""
	 */
	public static String getFileDir(String filePath) {
		if (filePath == null || filePath.trim().isEmpty()) {
			return null;
		}
		filePath = filePath.replace('\\', '/');
		File f = new File(filePath);
		String parent = f.getParent();
		if (parent != null) {
			return parent;
		} else {
			return "";
		}
	}

	/**
	 * Returns the name of file without path
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} string(fullPath) fullPath: String.
	 * 
	 * {example} getFileName(fullPath) # ""
	 */
	public static String getFileName(String filePath) {
		if (filePath == null) {
			return null;
		}
		filePath = filePath.replace('\\', '/');
		File f = new File(filePath);
		return f.getName();
	}

	/**
	 * Returns the name of file without path and extension
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} string(fullPath) fullPath: String.
	 * 
	 * {example} getFileNameWithoutExt(fullPath) # ""
	 */
	public static String getFileNameWithoutExt(String filePath) {
		String name = getFileName(filePath);
		int pos = name.lastIndexOf('.');
		if (pos > 0) {
			return name.substring(0, pos);
		} else {
			return name;
		}
	}

	/**
	 * Returns the relative path from a full path based on a base path
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} string(fullPath) fullPath: String.
	 * 
	 * {param} string(basePath) basePath: String.
	 * 
	 * {example} getRelativePath(basePath, fullPath) # ""
	 */
	public static String getRelativePath(String fullPath, String basePath) {
		if (fullPath == null || fullPath.trim().isEmpty()) {
			return null;
		}
		if (basePath == null || basePath.trim().isEmpty()) {
			return fullPath;
		}
		// normalize path
		fullPath = fullPath.replaceAll("\\\\", "/").trim();
		basePath = basePath.replaceAll("\\\\", "/").trim();
		fullPath = fullPath.replaceAll("[/]{2,}", "/").trim();
		fullPath = fullPath.replaceAll("/./", "/").trim();
		basePath = basePath.replaceAll("[/]{2,}", "/").trim();
		basePath = basePath.replaceAll("/./", "/").trim();
		if (basePath.endsWith("/")) {
			basePath = basePath.substring(0, basePath.length() - 1);
		}
		int pos = fullPath.indexOf(basePath);
		if (pos == -1) {
			return null;
		}
		return fullPath.substring(pos + basePath.length() + 1);
	}

	/**
	 * Setup a path to a directory, all missing parts will be created
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} string(directoryPath) basePath: String.
	 * 
	 * {example} ensureDirectoryExists(directoryPath) # ""
	 */
	public static String ensureDirectoryExists(String directoryPath) throws Exception {
		if (directoryPath == null || directoryPath.trim().isEmpty()) {
			throw new Exception("directoryPath cannot be null or empty");
		}
		directoryPath = directoryPath.trim();
		boolean pathEndsWithDelimiter = directoryPath.endsWith("/") || directoryPath.endsWith("\\");
		String endChar = "";
		if (pathEndsWithDelimiter) {
			endChar = String.valueOf(directoryPath.charAt(directoryPath.length() - 1));
		}
		File dir = new File(directoryPath);
		if (dir.exists() == false) {
			dir.mkdirs();
		} else {
			if (dir.isFile()) {
				throw new Exception("The directory path: " + directoryPath
						+ " points to a file and not to a directory as expected!");
			}
		}
		if (dir.exists() == false) {
			throw new Exception(
					"The directory path: " + directoryPath + " cannot be created. Check rights or path syntax.");
		}
		String returnPath = dir.getAbsolutePath();
		if (pathEndsWithDelimiter && (returnPath.endsWith("/") || returnPath.endsWith("\\")) == false) {
			returnPath = returnPath + endChar;
		}
		return returnPath;
	}

	/**
	 * Checks if a file exists
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} string(filePath) basePath: String.
	 * 
	 * {example} doesFileExist(filePath) # ""
	 */
	public static boolean doesFileExist(String filePath) {
		if (filePath == null || filePath.trim().isEmpty()) {
			return false;
		}
		filePath = filePath.replace('\\', '/');
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * Fails if a file exists
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} string(filePath) basePath: String.
	 * 
	 * {example} failIfFileNotExists(filePath) # ""
	 */
	public static void failIfFileNotExists(String filePath) throws Exception {
		if (doesFileExist(filePath) == false) {
			throw new Exception("The given file path: " + filePath + " does not exist!");
		}
	}
	
	/**
	 * Checks if a file exists, can be read and write
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} string(filePath) basePath: String.
	 * 
	 * {example} fileReadableAndWritable(filePath) # ""
	 */
	public static boolean fileReadableAndWritable(String filePath) {
		if (filePath == null || filePath.trim().isEmpty()) {
			return false;
		}
		filePath = filePath.replace('\\', '/');
		File file = new File(filePath);
		return file.exists() && file.canRead() && file.canWrite();
	}

	/**
	 * Checks if a file exists, can be read
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} string(filePath) basePath: String.
	 * 
	 * {example} fileReadable(filePath) # ""
	 */
	public static boolean fileReadable(String filePath) {
		if (filePath == null || filePath.trim().isEmpty()) {
			return false;
		}
		filePath = filePath.replace('\\', '/');
		File file = new File(filePath);
		return file.exists() && file.canRead();
	}

	/**
	 * Checks if a file exists
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} string(dirPath) basePath: String. 
	 * {param} string(fileName) basePath: String.
	 * 
	 * {example} doesFileExist(dirPath, fileName) # ""
	 */
	public static boolean doesFileExist(String dirPath, String fileName) {
		if (dirPath == null || dirPath.trim().isEmpty()) {
			return false;
		}
		if (fileName == null || fileName.trim().isEmpty()) {
			return false;
		}
		dirPath = dirPath.replace('\\', '/');
		File file = new File(dirPath, fileName);
		return file.exists();
	}

	/**
	 * returns a modified name
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} String(fileName) fileName: String. 
	 * {param} String(somethingToAdd) somethingToAdd: String.
	 * 
	 * {example} addNamePartBeforeExtension(context.currentFile, str) #
	 */
	public static String addNamePartBeforeExtension(String fileName, String somethingToAdd) {
		int pos = fileName.lastIndexOf(".");
		if (pos != -1 && pos < fileName.length()) {
			String name = fileName.substring(0, pos);
			String ext = fileName.substring(pos);
			return name + somethingToAdd + ext;
		} else {
			return fileName + somethingToAdd;
		}
	}

	/**
	 * returns true if the string points to an archive file
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} String(file) strings: String.
	 * 
	 * {example} isArchiveFile(context.currentFile) # 2323133_18
	 */
	public static boolean isArchiveFile(String file) {
		if (file != null) {
			return file.toLowerCase().endsWith(".zip") || file.toLowerCase().endsWith(".gz");
		} else {
			return false;
		}
	}

	/**
	 * Delete a dir recursively deleting anything inside it.
	 * 
	 * @param dir The dir to delete
	 * @return true if the dir was successfully deleted 
	 *         
	 *         {Category} FileUtil
	 * 
	 *         {param} String(dir) strings: String.
	 * 
	 *         {example} deleteDirectory(context.currentDir)
	 * 
	 */
	public static boolean deleteDirectory(String dirStr) {
		File dir = new File(dirStr);
		if (dir.exists() == false || dir.isDirectory() == false) {
			return false;
		}
		String[] files = dir.list();
		for (int i = 0, len = files.length; i < len; i++) {
			File f = new File(dir, files[i]);
			if (f.isDirectory()) {
				deleteDirectory(f.getAbsolutePath());
			} else {
				f.delete();
			}
		}
		return dir.delete();
	}

	/**
	 * Delete a file
	 * 
	 * {Category} FileUtil
	 * 
	 * {param} String(fileStr) strings: String.
	 * 
	 * {example} deleteFile(context.currentFile)
	 * 
	 */
	public static boolean deleteFile(String fileStr) {
		File file = new File(fileStr);
		if (file.exists() == false || file.isDirectory()) {
			return false;
		}
		return file.delete();
	}

	/**
	 * Writes text content to a file
	 * 
	 * @param filePath the file path
	 * @param content
	 * @param charset  if null UTF-8 will be used
	 *
	 *                 {Category} FileUtil
	 * 
	 *                 {param} String(filePath) strings: String. 
	 *                 {param} String(content) strings: String. 
	 *                 {param} String(charset) strings: String.
	 * 
	 *                 {example} writeContentToFile(context.currentDir) # 2323133_18
	 * 
	 */
	public static void writeContentToFile(String filePath, String content, String charset) throws Exception {
		if (filePath == null || content == null) {
			return;
		}
		if (charset == null || charset.trim().isEmpty()) {
			charset = "UTF-8";
		}
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
		Files.write(java.nio.file.Paths.get(filePath), content.getBytes(charset), StandardOpenOption.CREATE);
	}

	/**
	 * Reads text content from a file
	 * 
	 * @param filePath the file path
	 * @param charset  if null UTF-8 will be used
	 *
	 *                 {Category} FileUtil
	 * 
	 *                 {param} String(filePath) strings: String. 
	 *                 {param} String(charset) strings: String.
	 * 
	 *                 {example} readContentfromFile(context.currentDir, "UTF-8")
	 * 
	 */
	public static String readContentfromFile(String filePath, String charset) throws Exception {
		if (filePath == null) {
			return null;
		}
		File f = new File(filePath);
		if (f.exists() == false) {
			throw new Exception("File: " + filePath + " does not exist.");
		}
		if (charset == null || charset.trim().isEmpty()) {
			charset = "UTF-8";
		}
		Path p = java.nio.file.Paths.get(filePath);
		byte[] bytes = Files.readAllBytes(p);
		if (bytes != null && bytes.length > 0) {
			return new String(bytes, charset);
		} else {
			return null;
		}
	}

	/**
	 * Tests if the file is an xml file
	 * 
	 * @param filePath the file path
	 * @return content-type
	 *
	 *                 {Category} FileUtil
	 * 
	 *                 {param} String(filePath) strings: String. 
	 * 
	 *                 {example} isXMLFile(context.current_file)
	 * 
	 */
	public static boolean isXMLFile(String filePath) throws Exception {
		if (filePath == null) {
			return false;
		}
		File f = new File(filePath);
		if (f.exists() == false) {
			throw new Exception("File: " + filePath + " does not exist.");
		}
		if (f.length() == 0) {
			return false;
		}
		
		RandomAccessFile rf = new RandomAccessFile(filePath, "r");
		int bufferLength = Math.min(100, (int)f.length());
		byte[] b = new byte[bufferLength];
		rf.read(b, 0, bufferLength);
		try {
			String test = new String(b, "UTF-8");
			if (test.trim().startsWith("<?xml")) {
				return true;
			} else {
				return false;
			}
		} catch (Throwable t) {
			return false;
		} finally {
			try {
				rf.close();
			} catch (Throwable tc) {}
		}
	}

	/**
	 * Writes text content to a file if the content has been changed
	 * 
	 * @param filePath the file path
	 * @param content
	 * @param charset  if null UTF-8 will be used
	 *
	 *                 {Category} FileUtil 
	 *                 {param} String(filePath) strings: String. 
	 *                 {param} String(content) strings: String. 
	 *                 {param} String(charset) strings: String.
	 * 
	 *                 {example} writeContentToFile(context.currentDir) # 2323133_18
	 * 
	 */
	public static boolean writeContentToFileIfChanged(String filePath, String content, String charset)
			throws Exception {
		File f = new File(filePath);
		if (f.exists() == false) {
			FileUtil.writeContentToFile(filePath, content, charset);
			return true;
		} else {
			String prevcontent = FileUtil.readContentfromFile(filePath, charset);
			if (content.equals(prevcontent) == false) {
				FileUtil.writeContentToFile(filePath, content, charset);
				return true;
			}
		}
		return false;
	}

	/**
	 * Builds the MD5 checksum over a file
	 * 
	 * @param filename
	 * @return MD5 checksum
	 * @throws Exception
	 * 
	 *                   {Category} FileUtil 
	 * 
	 *                   {param} String(filename)
	 * 
	 *                   {example} buildMD5(filename)
	 * 
	 */
	public static String buildMD5(String filePath) throws Exception {
		File f = new File(filePath);
		if (f.exists() == false) {
			throw new Exception(
					"buildMD5 failed. File: " + f.getAbsolutePath() + " is not readable or does not exists.");
		}
		InputStream fis = new FileInputStream(f);
		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		byte[] b = complete.digest();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
		}
		return result.toString();
	}

	/**
	 * Builds a file path
	 * 
	 * @param filename
	 * @param path     parts
	 * @return the complete path
	 * @throws Exception
	 * 
	 *                   {Category} FileUtil 
	 * 
	 *                   {param} String(filename) 
	 *                   {param} String(filename) 
	 *                   {param} String(filename)
	 * 
	 *                   {example} buildPath(filename, path1...pathn)
	 * 
	 */
	public static String buildPath(String filename, String... pathParts) {
		StringBuilder path = new StringBuilder();
		if (pathParts != null) {
			boolean firstLoop = true;
			for (String p : pathParts) {
				if (p != null && p.trim().isEmpty() == false) {
					if (p.contains("\\")) {
						p = p.replace('\\', '/');
					}
					if (firstLoop) {
						firstLoop = false;
					} else {
						if (p.startsWith("/")) {
							p = p.substring(1);
						}
					}
					if (p.endsWith("/")) {
						path.append(p);
					} else {
						path.append(p);
						path.append("/");
					}
				}
			}
		}
		if (filename != null) {
			path.append(filename);
		}
		return path.toString();
	}

	/**
	 * Moves a file
	 * 
	 * @param filePath
	 * @param targetDir
	 * @return the absolute path of the moved file
	 * @throws Exception
	 * 
	 *                   {Category} FileUtil 
	 * 
	 *                   {param} String(filePath) 
	 *                   {param} String(targetDir)
	 * 
	 *                   {example} moveFile(filePath, targetDir)
	 * 
	 */
	public static String moveFile(String filePath, String targetDir) throws Exception {
		if (StringUtil.isEmpty(targetDir)) {
			throw new Exception("targetDir cannot be null or empty!");
		}
		if (StringUtil.isEmpty(filePath)) {
			return null;
		} else {
			File f = new File(filePath);
			if (f.exists() == false) {
				throw new Exception("moveFile: file: " + filePath + " failed: file does not exist");
			} else {
				File td = new File(targetDir);
				if (td.exists() == false) {
					td.mkdirs();
				}
				if (td.exists()) {
					if (td.isFile()) {
						throw new Exception(
								"targetDir: " + targetDir + " points to an existing file but must be a directory");
					} else {
						File tf = new File(td, f.getName());
						if (f.renameTo(tf)) {
							return tf.getAbsolutePath();
						} else {
							throw new Exception("moveFile: file: " + filePath + " failed: targetDir+file: "
									+ tf.getAbsolutePath() + " the filesystem has not performed the move");
						}
					}
				} else {
					throw new Exception("moveFile: file: " + filePath + " failed: targetDir: " + td.getAbsolutePath()
							+ " does not exist and cannot be created");
				}
			}
		}
	}
	
	/**
	 * Copy a file
	 * 
	 * @param filePath
	 * @param targetPath
	 * @return the absolute path of the moved file
	 * @throws Exception
	 * 
	 *                   {Category} FileUtil 
	 * 
	 *                   {param} String(filePath) 
	 *                   {param} String(targetPath)
	 * 
	 *                   {example} copyFile(filePath, targetPath)
	 * 
	 */
	public static String copyFile(String sourcePath, String targetPath) throws Exception {
		if (StringUtil.isEmpty(sourcePath)) {
			throw new IllegalArgumentException("Source file path cannot be null or empty");
		}
		if (StringUtil.isEmpty(targetPath)) {
			throw new IllegalArgumentException("Target file path cannot be null or empty");
		}
		File sp = new File(sourcePath);
		if (sp.exists() == false) {
			throw new Exception("Source file path: " + sp.getAbsolutePath() + " does not exist");
		}
		File tp = new File(targetPath);
		File td = tp.getParentFile();
		if (td == null) {
			throw new Exception("Target path: " + targetPath + " does not contains a target folder");
		}
		if (td.exists() == false) {
			td.mkdirs();
		}
		if (td.exists() == false) {
			throw new Exception("Target dir: " + td.getAbsolutePath() + " does not exist and cannot be created"); 
		}
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(sp));
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tp));
		try {
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
		} catch(Exception e) {
			throw new Exception("Copy source file: " + sp.getAbsolutePath() + " to target file: " + tp.getAbsolutePath() + " failed: " + e.getMessage(), e);
		} finally {
			in.close();
			if (out != null) {
				out.flush();
				out.close();
			}
		}
		return tp.getAbsolutePath();
	}

	/**
	 * Converts the path to a UNIX style path (which works on both: Windows and
	 * UNIX)
	 * 
	 * @param filePath
	 * @return the unix style path
	 * 
	 *         {Category} FileUtil 
	 * 
	 *         {param} String(filePath)
	 * 
	 *         {example} getUnixPath(filePath)
	 * 
	 */
	public static String getUnixPath(String filePath) {
		if (filePath == null) {
			return null;
		}
		filePath = filePath.replace('\\', '/');
		return RegexUtil.replaceByRegexGroups(filePath, "^([a-zA-Z]:)", "");
	}

	/**
	 * Converts the the file into a Base64 String
	 * 
	 * @param filePath
	 * @return the Base64 encoded String
	 * 
	 *         {Category} FileUtil 
	 * 
	 *         {param} String(filePath)
	 * 
	 *         {example} getBytesAsBase64(filePath)
	 * 
	 */
	public static String getBytesAsBase64(String filePath) throws Exception {
		try {
			if (doesFileExist(filePath) == false) {
				throw new Exception("File: " + filePath + " does not exist or is not readable");
			}
			Path path = Paths.get(filePath);
			byte[] b = Files.readAllBytes(path);
			b = Base64.encode(b);
			String retString = new String(b, StandardCharsets.UTF_8);
			return retString;
		} catch (Exception e) {
			throw new Exception("Fail to convert file: " + filePath + " to base64: " + e.getMessage(), e);
		}
	}

	/**
	 * Reads a file in reverse order of the lines
	 * 
	 * @param filePath
	 * @param limitNumberLines (-1 means without limit)
	 * @param charset          charset (null means UTF-8)
	 * @return the the reverse file content
	 * 
	 *         {Category} FileUtil 
	 * 
	 *         {param} String(filePath) 
	 *         {param} Integer(limitNumberLines) 
	 *         {param} String(charset)
	 * 
	 *         {example} readReverseNumberLines(filePath,limitNumberLines,charset)
	 * 
	 */
	public static String readReverseNumberLines(String filePath, int limitNumberLines, String charset)
			throws Exception {
		if (filePath == null || filePath.trim().isEmpty()) {
			throw new IllegalArgumentException("Parameter filePath cannot be null or empty");
		}
		if (charset == null || charset.trim().isEmpty()) {
			charset = "UTF-8";
		}
		File file = new File(filePath);
		if (file.canRead() == false) {
			throw new Exception("File: " + file.getAbsolutePath() + " cannot be read.");
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(new ReverseLineInputStream(file), charset));
		try {
			boolean firstLoop = true;
			StringBuilder sb = new StringBuilder();
			int countLines = 0;
			while (true) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				if (firstLoop) {
					// ignore line breaks at the end of the file
					if (line.trim().isEmpty()) {
						continue;
					}
					firstLoop = false;
				} else {
					sb.append("\n");
				}
				sb.append(line);
				countLines++;
				if (limitNumberLines > 0 && limitNumberLines <= countLines) {
					break;
				}
			}
			return sb.toString();
		} catch (Exception e) {
			throw new Exception("Fail to reverse read file: " + file.getAbsolutePath(), e);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static class ReverseLineInputStream extends InputStream {

		private RandomAccessFile in;
		private long currentLineStart = -1;
		private long currentLineEnd = -1;
		private long currentPos = -1;
		private long lastPosInFile = -1;

		public ReverseLineInputStream(File file) throws FileNotFoundException {
			in = new RandomAccessFile(file, "r");
			currentLineStart = file.length();
			currentLineEnd = file.length();
			lastPosInFile = file.length() - 1;
			currentPos = currentLineEnd;
		}

		public void findPrevLine() throws IOException {
			currentLineEnd = currentLineStart;
			// There are no more lines, since we are at the beginning of the file and no
			// lines.
			if (currentLineEnd == 0) {
				currentLineEnd = -1;
				currentLineStart = -1;
				currentPos = -1;
				return;
			}
			long filePointer = currentLineStart - 1;
			while (true) {
				filePointer--;
				// we are at start of file so this is the first line in the file.
				if (filePointer < 0) {
					break;
				}
				in.seek(filePointer);
				int readByte = in.readByte();
				// We ignore last LF in file. search back to find the previous LF.
				if (readByte == 0xA && filePointer != lastPosInFile) {
					break;
				}
			}
			// we want to start at pointer +1 so we are after the LF we found or at 0 the
			// start of the file.
			currentLineStart = filePointer + 1;
			currentPos = currentLineStart;
		}

		@Override
		public int read() throws IOException {
			if (currentPos < currentLineEnd) {
				in.seek(currentPos++);
				int readByte = in.readByte();
				return readByte;
			} else if (currentPos < 0) {
				return -1;
			} else {
				findPrevLine();
				return read();
			}
		}
	}

	/**
	 * Checks if a file finally ends with a text portion
	 * 
	 * @param filePath
	 * @param contentAtTheEndOfFile
	 * @param charset               charset (null means UTF-8)
	 * @return the the reverse file content
	 * 
	 *         {Category} FileUtil 
	 * 
	 *         {param} String(filePath) 
	 *         {param} String(contentAtTheEndOfFile)
	 *         {param} String(charset)
	 * 
	 *         {example} waitForEndOfFile(filePath,limitNumberLines,charset)
	 * 
	 */
	public static void waitForEndOfFile(String filePath, String contentAtTheEndOfFile, String charset)
			throws Exception {
		if (filePath == null || filePath.trim().isEmpty()) {
			throw new IllegalArgumentException("filepath cannot be  null or empty");
		}
		File file = new File(filePath);
		if (file.exists() == false) {
			throw new Exception("File: " + file.getAbsolutePath() + " does not exist.");
		} else if (file.canRead() == false) {
			throw new Exception("File: " + file.getAbsolutePath() + " cannot be read.");
		}
		while (true) {
			String lastLine = readReverseNumberLines(file.getAbsolutePath(), 2, charset);
			if (lastLine.contains(contentAtTheEndOfFile)) {
				break;
			} else {
				Thread.sleep(1000);
			}
		}
	}

	public static String waitForFileFinishedWriting(String filePath, boolean allowWaitingForFileCreate, int timeout)
			throws Exception {
		if (filePath == null || filePath.trim().isEmpty()) {
			throw new IllegalArgumentException("filepath cannot be  null or empty");
		}
		File file = new File(filePath);
		long start = System.currentTimeMillis();
		while (true) {
			if (file.exists() == false) {
				if (allowWaitingForFileCreate) {
					long current = System.currentTimeMillis();
					if (timeout > 0 && (current - start) > timeout) {
						throw new Exception(
								"Timeout while waiting for files existence. File: " + file.getAbsolutePath());
					}
				}
				Thread.sleep(100);
			} else {
				long lastSize = file.length();
				long lastModified = file.lastModified();
				Thread.sleep(100);
				if (lastModified == file.lastModified() && lastSize == file.length()) {
					break;
				}
			}
		}
		return file.getAbsolutePath();
	}

	/**
	 * Filter BOMs in a file content f
	 * 
	 * @param sourceFilePath source file path
	 * @param targetFilePath if null, the original file will be replaced
	 * @param charset charset (null means UTF-8)
	 * @return true if a BOM was detected
	 * 
	 *         {Category} FileUtil
	 * 
	 *         {param} String(sourceFilePath) 
	 *         {param} String(targetFilePath)
	 *         {param} String(charset)
	 * 
	 *         {example} removeBOM(sourceFilePath,targetFilePath,charset)
	 * 
	 */
	public static boolean removeBOM(String sourceFilePath, String targetFilePath, String charset) throws Exception {
		if (sourceFilePath == null || sourceFilePath.trim().isEmpty()) {
			throw new Exception("sourceFilePath cannot be null or empty");
		}
		sourceFilePath = sourceFilePath.trim();
		if (charset == null || charset.trim().isEmpty()) {
			charset = "UTF-8";
		}
		if (targetFilePath != null && targetFilePath.trim().isEmpty() == false) {
			targetFilePath = targetFilePath.trim();
		} else {
			targetFilePath = null;
		}
		File sourceFile = new File(sourceFilePath);
		if (sourceFile.exists() == false) {
			throw new Exception("Source file: " + sourceFile.getAbsolutePath() + " does not exist");
		}
		File targetFile = null;
		if (targetFilePath == null) {
			if (sourceFile.canWrite() == false) {
				throw new Exception("The routine will attempt to replace the source file: " + sourceFile.getAbsolutePath() + " but file cannot be overwritten!");
			}
			targetFile = File.createTempFile(sourceFile.getName(), ".txt");
		} else {
			targetFile = new File(targetFilePath);
		}
		FileInputStream fin = new FileInputStream(sourceFile);
		UnicodeBOMInputStream inBOM = new UnicodeBOMInputStream(fin);
		inBOM.skipBOM();
		boolean hasBOM = inBOM.hasBOM();
		if (hasBOM == false && targetFilePath == null) {
			// we do not have a BOM and we do not want to copy the file.
			// nothing to do here
			inBOM.close();
			return false;
		}
		BufferedInputStream in = new BufferedInputStream(inBOM);
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile));
		try {
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
		} finally {
			inBOM.close();
			if (out != null) {
				out.flush();
				out.close();
			}
		}
		if (targetFilePath == null) {
			sourceFile.delete();
			String tempFilePath = targetFile.getAbsolutePath();
			if (targetFile.renameTo(sourceFile)) {
				// check if the target file really exist!
				Thread.sleep(500);
				if (sourceFile.exists() == false) {
					throw new Exception("Move of temp file: " + tempFilePath + " to original source file: " + sourceFile.getAbsolutePath() + " has actually succeeded but something went wrong! Now the source file also does not exist anymore!");
				}
			} else {
				// move did not work because probably not the same file system between source and target
				// check if the temp file still exist and copy it to the target
				if (targetFile.exists() == false) {
					throw new Exception("Move of temp file: " + tempFilePath + " to original file: " + sourceFile.getAbsolutePath() + " has been failed! The temp file also does not exist anymore!");
				}
				copyFile(tempFilePath, sourceFile.getAbsolutePath());
				deleteFile(tempFilePath);
			}
		}
		return hasBOM;
	}
	
	/**
	 * A class representing BOMs in different flavors
	 */
	public static final class BOM {
		/**
		 * NONE.
		 */
		public static final BOM NONE = new BOM(new byte[] {}, "NONE");

		/**
		 * UTF-8 BOM (EF BB BF).
		 */
		public static final BOM UTF_8 = new BOM(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }, "UTF-8");

		/**
		 * UTF-16, little-endian (FF FE).
		 */
		public static final BOM UTF_16_LE = new BOM(new byte[] { (byte) 0xFF, (byte) 0xFE }, "UTF-16 little-endian");

		/**
		 * UTF-16, big-endian (FE FF).
		 */
		public static final BOM UTF_16_BE = new BOM(new byte[] { (byte) 0xFE, (byte) 0xFF }, "UTF-16 big-endian");

		/**
		 * UTF-32, little-endian (FF FE 00 00).
		 */
		public static final BOM UTF_32_LE = new BOM(new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00 },
				"UTF-32 little-endian");

		/**
		 * UTF-32, big-endian (00 00 FE FF).
		 */
		public static final BOM UTF_32_BE = new BOM(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF },
				"UTF-32 big-endian");

		/**
		 * Returns a {@link String} representation of this {@link BOM}. value.
		 */
		public final String toString() {
			return description;
		}

		/**
		 * Returns the bytes corresponding to this {@link BOM} value.
		 */
		public final byte[] getBytes() {
			final int length = bytes.length;
			final byte[] result = new byte[length];

			// Make a defensive copy
			System.arraycopy(bytes, 0, result, 0, length);

			return result;
		}

		private BOM(final byte bom[], final String description) {
			this.bytes = bom;
			this.description = description;
		}

		final byte bytes[];
		private final String description;

	}

	/**
	 * The {@link UnicodeBOMInputStream} class wraps any {@link InputStream} and
	 * detects the presence of any Unicode BOM (Byte Order Mark) at its beginning,
	 * as defined by <a href="http://www.faqs.org/rfcs/rfc3629.html">RFC 3629 -
	 * UTF-8, a transformation format of ISO 10646</a>
	 *
	 * <p>
	 * The <a href="http://www.unicode.org/unicode/faq/utf_bom.html">Unicode FAQ</a>
	 * defines 5 types of BOMs:
	 * <ul>
	 * <li>
	 * 
	 * <pre>
	 * 00 00 FE FF  = UTF-32, big-endian
	 * </pre>
	 * 
	 * </li>
	 * <li>
	 * 
	 * <pre>
	 * FF FE 00 00  = UTF-32, little-endian
	 * </pre>
	 * 
	 * </li>
	 * <li>
	 * 
	 * <pre>
	 * FE FF        = UTF-16, big-endian
	 * </pre>
	 * 
	 * </li>
	 * <li>
	 * 
	 * <pre>
	 * FF FE        = UTF-16, little-endian
	 * </pre>
	 * 
	 * </li>
	 * <li>
	 * 
	 * <pre>
	 * EF BB BF     = UTF-8
	 * </pre>
	 * 
	 * </li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Use the {@link #getBOM()} method to know whether a BOM has been detected or
	 * not.
	 * </p>
	 * <p>
	 * Use the {@link #skipBOM()} method to remove the detected BOM from the wrapped
	 * {@link InputStream} object.
	 * </p>
	 *
	 * @author Gregory Pakosz
	 * @see http://stackoverflow.com/q/1835430/39321#1835529
	 */
	public static class UnicodeBOMInputStream extends InputStream {
		/**
		 * Type safe enumeration class that describes the different types of Unicode
		 * BOMs.
		 */

		/**
		 * Constructs a new {@link UnicodeBOMInputStream} that wraps the specified
		 * {@link InputStream}.
		 *
		 * @param inputStream an {@link InputStream}.
		 *
		 * @throws IOException on reading from the specified {@link InputStream} when
		 *                     trying to detect the Unicode BOM.
		 */
		public UnicodeBOMInputStream(final InputStream inputStream) throws IOException

		{
			in = new PushbackInputStream(inputStream, 4);

			final byte bom[] = new byte[4];
			final int read = in.read(bom);

			switch (read) {
			case 4:
				if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00)
						&& (bom[3] == (byte) 0x00)) {
					this.bom = BOM.UTF_32_LE;
					break;
				} else if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE)
						&& (bom[3] == (byte) 0xFF)) {
					this.bom = BOM.UTF_32_BE;
					break;
				}
			case 3:
				if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
					this.bom = BOM.UTF_8;
					break;
				}
			case 2:
				if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
					this.bom = BOM.UTF_16_LE;
					break;
				} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
					this.bom = BOM.UTF_16_BE;
					break;
				}
			default:
				this.bom = BOM.NONE;
				break;
			}

			if (read > 0) {
				in.unread(bom, 0, read);
			}
		}

		/**
		 * Returns the {@link BOM} that was detected in the wrapped {@link InputStream}
		 * object.
		 *
		 * @return a {@link BOM} value.
		 */
		public final BOM getBOM() {
			// BOM type is immutable.
			return bom;
		}
		
		public boolean hasBOM() {
			return (bom != null && bom != BOM.NONE);
		}

		/**
		 * Skips the {@link BOM} that was found in the wrapped {@link InputStream}
		 * object.
		 *
		 * @return this {@link UnicodeBOMInputStream}.
		 *
		 * @throws IOException when trying to skip the BOM from the wrapped
		 *                     {@link InputStream} object.
		 */
		public final synchronized UnicodeBOMInputStream skipBOM() throws IOException {
			if (!skipped) {
				in.skip(bom.bytes.length);
				skipped = true;
			}
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		public int read() throws IOException {
			return in.read();
		}

		/**
		 * {@inheritDoc}
		 */
		public int read(final byte b[]) throws IOException, NullPointerException {
			return in.read(b, 0, b.length);
		}

		/**
		 * {@inheritDoc}
		 */
		public int read(final byte b[], final int off, final int len) throws IOException, NullPointerException {
			return in.read(b, off, len);
		}

		/**
		 * {@inheritDoc}
		 */
		public long skip(final long n) throws IOException {
			return in.skip(n);
		}

		/**
		 * {@inheritDoc}
		 */
		public int available() throws IOException {
			return in.available();
		}

		/**
		 * {@inheritDoc}
		 */
		public void close() throws IOException {
			in.close();
		}

		/**
		 * {@inheritDoc}
		 */
		public synchronized void mark(final int readlimit) {
			in.mark(readlimit);
		}

		/**
		 * {@inheritDoc}
		 */
		public synchronized void reset() throws IOException {
			in.reset();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean markSupported() {
			return in.markSupported();
		}

		private final PushbackInputStream in;
		private final BOM bom;
		private boolean skipped = false;

	}
	
	/**
	 * Deletes a file and repeate if it does not work
	 * 
	 * @param filePath
	 * @param attempts
	 * @return true if file could be deleted
	 * 
	 *         {Category} FileUtil 
	 * 
	 *         {param} String(filePath) 
	 *         {param} int(attempts)
	 * 
	 *         {example} delete(filePath,attempts)
	 * 
	 */
	public static boolean deleteFile(String filePath, int attempts) {
		if (filePath == null || filePath.trim().isEmpty()) {
			throw new IllegalArgumentException("filePath cannot be null or empty");
		}
		File f = new File(filePath);
		if (f.exists() == false) {
			throw new IllegalArgumentException("Given file to delete does not exist: " + f.getAbsolutePath());
		}
		for (int i = 0; i < attempts; i++) {
			f.delete();
			if (f.exists() == false) {
				return true;
			} else {
				try {
					Thread.sleep(5000);
				} catch (Exception e) {
					return false;
				}
			}
		}
		return false;
	}
	
    /**
     * Returns true if jobserver is a unix system
     * 
     * {Category} JobUtil
     * 
     * {example} isUnixSystem() # true.
     */
    public static boolean isUnixSystem() {
		String os = System.getProperty("os.name");
		if (os != null) {
			os = os.toLowerCase().trim();
		} else {
			return false;
		}
		if (os.contains("win")) {
			return false;
		} else {
			return true;
		}
    }

    /**
	 * Returns the system native file path
	 * 
	 * @param filePath
	 * @return the native path
	 * 
	 *         {Category} FileUtil 
	 * 
	 *         {param} String(filePath) 
	 * 
	 *         {example} getNativeFilePath(filePath)
	 * 
	 */
	public static String getNativeFilePath(String path) {
		if (path != null) {
			path = path.trim();
			if (isUnixSystem()) {
				if (path.toLowerCase().startsWith("c:") || path.toLowerCase().startsWith("d:")) {
					path = path.substring(2);
				}
				path = path.replace("\\", "/");
				return path;
			} else {
				path = path.replace("/", "\\");
				if (path.startsWith("\\")) {
					path = "c:" + path;
				}
				return path;
			}
		} else {
			return null;
		}
	}
	
    /**
	 * Returns the file length
	 * 
	 * @param filePath
	 * @return the file length
	 * 
	 *         {Category} FileUtil 
	 * 
	 *         {param} String(filePath) 
	 * 
	 *         {example} getFileLength(filePath)
	 * 
	 */
	public static long getFileLength(String filePath) throws Exception {
		if (filePath == null || filePath.trim().isEmpty()) {
			throw new IllegalArgumentException("Parameter filePath cannot be null or empty!");
		}
		File f = new File(filePath);
		if (f.exists() == false) {
			throw new Exception("File: " + f.getAbsolutePath() + " does not exist");
		}
		return f.length();
	}
	
    /**
	 * Check if file is empty
	 * 
	 * @param filePath
	 * @throws Exception if file is empty
	 * 
	 *         {Category} FileUtil 
	 * 
	 *         {param} String(filePath) 
	 * 
	 *         {example} failIfFileIsEmpty(filePath)
	 * 
	 */
	public static void failIfFileIsEmpty(String filePath) throws Exception {
		long length = getFileLength(filePath);
		if (length == 0) {
			throw new Exception("File: " + filePath + " is empty!");
		}
	}
	
    /**
	 * Renames a file
	 * 
	 * @param sourcePath full path of the source file 
	 * @param targetName name of the target file
	 * @return full path of the renamed file
	 * @throws Exception if sourceFile does not exists or targetFile already exist 
	 * 
	 *         {Category} FileUtil 
	 * 
	 *         {param} String(sourcePath) 
	 *         {param} String(targetName)
	 *         {example} renameFile(context.sourceFile, context.targetName)
	 * 
	 */
	public static String renameFile(String sourcePath, String targetName, boolean overwriteTarget) throws Exception {
		File s = new File(sourcePath);
		if (s.exists() == false) {
			throw new Exception("Rename file failed: Source file: " + s.getAbsolutePath() + " does not exist");
		}
		File t = new File(s.getParentFile(), targetName);
		if (t.exists()) {
			if (overwriteTarget) {
				if (t.delete() == false) {
					throw new Exception("Rename file failed: Source file: " + s.getAbsolutePath() + ": Could not delete already existing target file: " + t.getAbsolutePath());
				}
			}
		}
		if (s.renameTo(t) == false) {
			throw new Exception("Rename file failed: Unable to rename source: " + s.getAbsolutePath() + " to " + t.getAbsolutePath());
		}
		return t.getAbsolutePath();
	}
	
	/**
	 * Returns the absolute path of a file or folder
	 * @param filePath - relative or absolute path
	 * @param basePath if filePath is relative, this will the absolute anchor
	 * @return the absolute path
	 * 
	 * {Category} FileUtil
	 * {param} filePath - relative or absolute path
	 * {param} basePath if filePath is relative, this will the absolute anchor
	 * 
	 */
	public static String getAbsolutePath(String filePath, String basePath) {
		String path = FileUtil.getNativeFilePath(filePath);
		File f = new File(path);
		if (f.isAbsolute()) {
			return f.getAbsolutePath();
		} else {
			return buildPath(filePath, basePath);
		}
	}
	
	/**
	 * Returns the last folder
	 * @param path - relative or absolute path
	 * @return the name of the parent
	 * 
	 * {Category} FileUtil
	 * {param} path - relative or absolute path
	 * 
	 */
	public static String getParentName(String path) {
		File f = new File(path);
		String parent = f.getParent();
		return parent;
	}

}
