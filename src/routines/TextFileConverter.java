package routines;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class TextFileConverter {
	
	private String sourcePath = null;
	private String targetPath = null;
	private String sourceEncoding = "UTF-8";
	private String targetEncoding = "UTF-8";
	private String targetLineSeparator = "\n";
	private List<Replacement> list = new ArrayList<>();
	private long currentInputLineNumber = 0;
	private long maxLinesPerFile = 0;
	private long currentOutputLineNumber = 0;
	private boolean readWholeText = false;
	private boolean unescapeJava = false;
	private int countExpectedDelimitersPerLine = 0;
	private char delimiter = '|';
	private List<Long> suspectedLines = new ArrayList<>();
	private boolean takeCountExpectedDelimitersFromHeaderLine = true;
	
	public boolean isReadWholeText() {
		return readWholeText;
	}
	
	public static class RegexReplacement extends Replacement {
		
		String searchFieldRegex = null;
		boolean trimSpaces = true;
	
		public RegexReplacement(String searchString, String replaceString, String searchFieldRegex, boolean trimSpaces) {
			super(searchString, replaceString);
			this.searchFieldRegex = searchFieldRegex;
			this.trimSpaces = trimSpaces;
		}
	}

	public static class Replacement {
		
		String searchString = null;
		String replaceString = null;
		
		public Replacement(String searchString, String replaceString) {
			this.searchString = searchString;
			this.replaceString = replaceString;
		}
		
	}
	
	public void addReplacement(String searchStr, String replacement) {
		if (searchStr != null && searchStr.isEmpty() == false) {
			if (replacement == null) {
				replacement = "";
			}
			if (unescapeJava) {
				searchStr = StringUtil.unescapeJava(searchStr);
				replacement = StringUtil.unescapeJava(replacement);
			}
			list.add(new Replacement(searchStr, replacement));
			if (searchStr.contains("\n")) {
				readWholeText = true;
			}
		}
	}

	public void addReplacement(String searchStr, String replacement, String searchFieldRegex, boolean trimSpaces) {
		if (searchStr != null && searchStr.isEmpty() == false) {
			if (replacement == null) {
				replacement = "";
			}
			if (unescapeJava) {
				searchStr = StringUtil.unescapeJava(searchStr);
				replacement = StringUtil.unescapeJava(replacement);
			}
			list.add(new RegexReplacement(searchStr, replacement, searchFieldRegex, trimSpaces));
			if (searchStr.contains("\n")) {
				readWholeText = true;
			}
		}
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public String getSourceEncoding() {
		return sourceEncoding;
	}

	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}

	public String getTargetEncoding() {
		return targetEncoding;
	}

	public void setTargetEncoding(String targetEncoding) {
		this.targetEncoding = targetEncoding;
	}

	public void reset() {
		currentInputLineNumber = 0;
		maxLinesPerFile = 0;
		currentOutputLineNumber = 0;
	}

	public void setMaxLinesPerFile(long maxLinesPerFile) {
		this.maxLinesPerFile = maxLinesPerFile;
	}
	
	public long getMaxLinesPerFile() {
		return maxLinesPerFile;
	}
	
	private void checkNull(String name, String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException("Parameter " + name + " cannot be null or empty.");
		}
	}
	
	public void convert() throws Exception {
		checkNull("sourcePath", sourcePath);
		checkNull("targetPath", targetPath);
		checkNull("sourceEncoding", sourceEncoding);
		File source = new File(sourcePath);
		if (source.exists() == false) {
			throw new Exception("Source file: " + source.getAbsolutePath() + " does not exist.");
		}
		if (targetPath.equals(sourcePath)) {
			targetPath = source.getAbsolutePath()+"-tmpfile";
		}
		File target = new File(targetPath);
		File targetDir = target.getParentFile();
		targetDir.mkdirs();
		if (targetDir.exists() == false) {
			throw new Exception("Target file folder: " + targetDir.getAbsolutePath() + " does not exist.");
		}
		if (readWholeText) {
			convertAllAtOnce(source, target, targetLineSeparator);
		} else {
			convertLineByLine(source, target, targetLineSeparator);
		}
		if (targetPath.equals(source.getAbsolutePath()+"-tmpfile")) {
			// delete former source file
			if (source.delete() == false) {
				throw new Exception("To rename target to source: delete original source file failed!");
			}
			// rename the target file as source file
			if (target.renameTo(source) == false) {
				throw new Exception("Failed to rename target to source: " + source.getAbsolutePath() + " to " + target.getAbsolutePath());
			}
		}
	}
	
	public String replace(String line) {
		for (Replacement r : list) {
			if (r instanceof RegexReplacement) {
				// extract the field content
				RegexReplacement rr = (RegexReplacement) r;
				String fieldContent = RegexUtil.extractByRegexGroup(line, rr.searchFieldRegex, 1);
				if (StringUtil.isEmpty(fieldContent) == false) {
					// fix the field
					String newFieldContent = fieldContent.replace(rr.searchString, rr.replaceString);
					if (rr.trimSpaces) {
						newFieldContent = StringUtil.reduceMultipleSpacesToOne(newFieldContent);
					}
					// replace the old field by the new field content
					line = line.replace(fieldContent, newFieldContent);
				}
			} else {
				line = line.replace(r.searchString, r.replaceString);
			}
		}
		return line;
	}
	
    private void convertLineByLine(final File source, final File target, final String targetLineSeparator) throws IOException {
		if (source.equals(target)) {
			throw new IllegalArgumentException("source cannot be the same as target file");
		}
		File currentTargetFile = target;
		final BufferedReader in = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(source), sourceEncoding));
		BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(currentTargetFile), targetEncoding));
		String line = null;
		int fileIndex = 0;
		boolean firstLine = true;
		while ((line = in.readLine()) != null) {
			if (Thread.currentThread().isInterrupted()) {
				break;
			}
			currentInputLineNumber++;
			if (maxLinesPerFile > 0 && currentOutputLineNumber == maxLinesPerFile) {
				currentOutputLineNumber = 0;
				out.flush();
				out.close();
				currentTargetFile = createNextFile(target, ++fileIndex);
				out = new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(currentTargetFile), targetEncoding));
			}
			if (firstLine) {
				if (takeCountExpectedDelimitersFromHeaderLine) {
					countExpectedDelimitersPerLine = countDelimiters(line);
				}
				firstLine = false;
			} else {
				out.write(targetLineSeparator);
			}
			line = replace(line);
			out.write(line);
			if (countExpectedDelimitersPerLine > 0) {
				if (countDelimiters(line) != countExpectedDelimitersPerLine) {
					suspectedLines.add(currentInputLineNumber);
				}
			}
		}
		out.flush();
		out.close();
		in.close();
	}
    
    private int countDelimiters(String line) {
    	char[] la = line.toCharArray();
    	int count = 0;
    	for (char c : la) {
    		if (c == delimiter) {
    			count++;
    		}
    	}
    	return count;
    }
	
	private void convertAllAtOnce(final File source, final File target, final String targetLineSeparator) throws IOException {
		if (source.equals(target)) {
			throw new IllegalArgumentException("source cannot be the same as target file");
		}
		File currentTargetFile = target;
		final BufferedReader in = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(source), sourceEncoding));
		BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(currentTargetFile), targetEncoding));
		String line = null;
		StringBuilder content = new StringBuilder(10000);
		while ((line = in.readLine()) != null) {
			if (Thread.currentThread().isInterrupted()) {
				break;
			}
			currentInputLineNumber++;
			content.append(line);
			content.append(targetLineSeparator);
			currentOutputLineNumber++;
		}
		out.write(replace(content.toString()));
		out.flush();
		out.close();
		in.close();
	}

	public long getCurrentLineNumber() {
		return currentInputLineNumber;
	}
	
	private File createNextFile(File originalTargetFile, int index) {
        String path = originalTargetFile.getParent();
        final String originalName = originalTargetFile.getName();
        final int p0 = originalName.lastIndexOf(".");
        String newName;
        if (p0 != -1) {
            newName = originalName.substring(0, p0) 
            	+ "_"
                + String.valueOf(index) 
                + originalName.substring(p0, originalName.length());
        } else {
            newName = originalName 
            	+ "_" 
            	+ String.valueOf(index);
        }
        if (path != null) {
            return new File(path, newName);
        } else {
            return new File(newName);
        }
    }

	public String getTargetLineSeparator() {
		return targetLineSeparator;
	}

	public void setTargetLineSeparator(String targetLineSeparator) {
		if (targetLineSeparator != null && targetLineSeparator.trim().isEmpty() == false) {
			this.targetLineSeparator = targetLineSeparator;
		}
	}

	public boolean isUnescapeJava() {
		return unescapeJava;
	}

	public void setUnescapeJava(boolean unescapeJava) {
		this.unescapeJava = unescapeJava;
	}
	
	public String getInfoAboutSuspectedLines() {
		if (suspectedLines.isEmpty() == false) {
			StringBuilder sb = new StringBuilder();
			sb.append("Following line numbers have invalid number of delimiters (expected: ");
			sb.append(countExpectedDelimitersPerLine);
			sb.append("): \n");
			for (Long l : suspectedLines) {
				sb.append(l);
				sb.append("\n");
			}
			return sb.toString();
		} else {
			return "";
		}
	}
	
}
