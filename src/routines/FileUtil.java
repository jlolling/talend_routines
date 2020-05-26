package routines;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
/**
 * Copyright 2017 Jan Lolling jan.lolling@cimt-ag.de
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
	public static void ensureDirectoryExists(String directoryPath) throws Exception {
		File dir = new File(directoryPath);
		if (dir.exists() == false) {
			dir.mkdirs();
		} else {
			if (dir.isFile()) {
				throw new Exception("The directory path: " + directoryPath + " points to an existing file!");
			}
		}
		if (dir.exists() == false) {
			throw new Exception("The directory path: " + directoryPath + " cannot be created. Check rights or path syntax.");
		}
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
	public static boolean doesFileExist(String filePath) throws Exception {
		if (filePath == null) {
			return false;
		}
		filePath = filePath.replace('\\', '/');
		File file = new File(filePath);
		return file.exists();
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
	public static boolean doesFileExist(String dirPath, String fileName) throws Exception {
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
	 * {talendTypes} String
	 * 
	 * {param} String(fileName) fileName: String. {param} String(somethingToAdd)
	 * somethingToAdd: String.
	 * 
	 * {example} addNamePartBeforeExtension(context.currentFile, str) #
	 */
	public static String addNamePartBeforeExtension(String fileName,
			String somethingToAdd) {
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
	 * {talendTypes} boolean
	 * 
	 * {param} String(file) strings: String.
	 * 
	 * {example} isArchiveFile(context.currentFile) # 2323133_18
	 */
	public static boolean isArchiveFile(String file) {
		if (file != null) {
			return file.toLowerCase().endsWith(".zip")
					|| file.toLowerCase().endsWith(".gz");
		} else {
			return false;
		}
	}
	
	/**
	 * Delets a dir recursively deleting anything inside it.
	 * @param dir The dir to delete
	 * @return true if the dir was successfully deleted
	 * {Category} FileUtil
	 * 
	 * {talendTypes} boolean
	 * 
	 * {param} String(dir) strings: String.
	 * 
	 * {example} deleteDirectory(context.currentDir) # 2323133_18
	 * 
	 */
	public static boolean deleteDirectory(File dir) {
	    if (dir.exists() == false || dir.isDirectory() == false)    {
	        return false;
	    }
	    String[] files = dir.list();
	    for(int i = 0, len = files.length; i < len; i++)    {
	        File f = new File(dir, files[i]);
	        if (f.isDirectory()) {
	            deleteDirectory(f);
	        } else {
	            f.delete();
	        }
	    }
	    return dir.delete();
	}
	
	/**
	 * Writes text content to a file
	 * @param filePath the file path
	 * @param content
	 * @param charset if null UTF-8 will be used
	 *
	 * {Category} FileUtil
	 * 
	 * {talendTypes} void
	 * 
	 * {param} String(filePath) strings: String.
	 * {param} String(content) strings: String.
	 * {param} String(charset) strings: String.
	 * 
	 * {example} writeContentToFile(context.currentDir) # 2323133_18
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
	 * @param filePath the file path
	 * @param charset if null UTF-8 will be used
	 *
	 * {Category} FileUtil
	 * 
	 * {talendTypes} String
	 * 
	 * {param} String(filePath) strings: String.
	 * {param} String(charset) strings: String.
	 * 
	 * {example} readContentfromFile(context.currentDir, "UTF-8")
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
	 * Writes text content to a file if the content has been changed
	 * @param filePath the file path
	 * @param content
	 * @param charset if null UTF-8 will be used
	 *
	 * {Category} FileUtil
	 * 
	 * {talendTypes} void
	 * 
	 * {param} String(filePath) strings: String.
	 * {param} String(content) strings: String.
	 * {param} String(charset) strings: String.
	 * 
	 * {example} writeContentToFile(context.currentDir) # 2323133_18
	 * 
	 */
    public static boolean writeContentToFileIfChanged(String filePath, String content, String charset) throws Exception {
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
	 * @param filename
	 * @return MD5 checksum
	 * @throws Exception
	 * 
	 * {Category} FileUtil
	 * {talendTypes} String
	 * 
	 * {param} String(filename)
	 * 
	 * {example} buildMD5(filename)
	 * 
	 */
	public static String buildMD5(String filePath) throws Exception {
		File f = new File(filePath);
		if (f.exists() == false) {
			throw new Exception("buildMD5 failed. File: " + f.getAbsolutePath() + " is not readable or does not exists.");
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
	 * @param filename
	 * @param path parts
	 * @return the complete path
	 * @throws Exception
	 * 
	 * {Category} FileUtil
	 * {talendTypes} String
	 * 
	 * {param} String(filename)
	 * {param} String(filename)
	 * {param} String(filename)
	 * 
	 * {example} buildPath(filename, path1...pathn)
	 * 
	 */
	public static String buildPath(String filename, String...pathParts) {
		StringBuilder path = new StringBuilder();
		if (pathParts != null) {
			for (String p : pathParts) {
				if (p != null && p.trim().isEmpty() == false) {
					if (p.contains("\\")) {
						p = p.replace('\\', '/');
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
	 * @param filePath
	 * @param targetDir
	 * @return the absolute path of the moved file
	 * @throws Exception
	 * 
	 * {Category} FileUtil
	 * {talendTypes} String
	 * 
	 * {param} String(filePath)
	 * {param} String(targetDir)
	 * 
	 * {example} moveFile(filePath, targetDir)
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
						throw new Exception("targetDir: " + targetDir + " points to an existing file but must be a directory");
					} else {
						File tf = new File(td, f.getName());
						if (f.renameTo(tf)) {
							return tf.getAbsolutePath();
						} else {
							throw new Exception("moveFile: file: " + filePath + " failed: targetDir+file: " + tf.getAbsolutePath() + " the filesystem has not performed the move");
						}
					}
				} else {
					throw new Exception("moveFile: file: " + filePath + " failed: targetDir: " + td.getAbsolutePath() + " does not exist and cannot be created");
				}
			}
		}
	}
	
	/**
	 * Converts the path to a UNIX style path (which works on both: Windows and UNIX)
	 * @param filePath
	 * @return the unix style path
	 * 
	 * {Category} FileUtil
	 * {talendTypes} String
	 * 
	 * {param} String(filePath)
	 * 
	 * {example} getUnixPath(filePath)
	 * 
	 */
	public static String getUnixPath(String filePath) {
		if (filePath == null) {
			return null;
		}
		filePath = filePath.replace('\\', '/');
		return RegexUtil.replaceByRegexGroups(filePath, "^([a-z]:)", "");
	}
	
}
