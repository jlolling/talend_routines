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
	
	public static class Replacement {
		
		String searchString = null;
		String replaceString = null;
		
		public Replacement(String searchString, String replaceString) {
			this.searchString = searchString;
			this.replaceString = replaceString;
		}
		
	}
	
	public void addReplacement(String searchStr, String replacement) {
		if (searchStr != null) {
			if (replacement == null) {
				replacement = "";
			}
			list.add(new Replacement(searchStr, replacement));
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
		convert(source, target, targetLineSeparator);
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
			line = line.replace(r.searchString, r.replaceString);
		}
		return line;
	}
	
	private void convert(final File source, final File target, final String targetLineSeparator) throws IOException {
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
			out.write(replace(line));
			out.write(targetLineSeparator);
			currentOutputLineNumber++;
		}
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
	
}
