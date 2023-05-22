package routines;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

/*
 * user specification: the function's comment should contain keys as follows: 1. write about the function's comment.but
 * it must be before the "{talendTypes}" key.
 * 
 * 2. {talendTypes} 's value must be talend Type, it is required . its value should be one of: String, char | Character,
 * long | Long, int | Integer, boolean | Boolean, byte | Byte, Date, double | Double, float | Float, Object, short |
 * Short
 * 
 * 3. {Category} define a category for the Function. it is required. its value is user-defined .
 * 
 * 4. {param} 's format is: {param} <type>[(<default value or closed list values>)] <name>[ : <comment>]
 * 
 * <type> 's value should be one of: string, int, list, double, object, boolean, long, char, date. <name>'s value is the
 * Function's parameter name. the {param} is optional. so if you the Function without the parameters. the {param} don't
 * added. you can have many parameters for the Function.
 * 
 * 5. {example} gives a example for the Function. it is optional.
 */
public class JobUtil {

    /**
     * Returns the name of the job package
     * 
     * 
     * {talendTypes} String
     * 
     * {Category} JobUtil
     * 
     * {param} string(jobName) jobName: 
     * 
     * {example} getJobPackageName(jobName) # hello world !.
     */
    public static String getJobPackageName(String jobName) {
    	if (jobName == null) {
    		throw new IllegalArgumentException("jobName cannot be null");
    	}
    	int pos = jobName.indexOf("__");
    	if (pos > 0) {
    		return jobName.substring(0, pos);
    	} else {
    		return jobName;
    	}
    }
    
    /**
     * Returns the host name
     * 
     * {talendTypes} String
     * 
     * {Category} JobUtil
     * 
     * {example} getHostName() # jobserver.
     */
    public static String getHostName() {
    	String processInfo = ManagementFactory.getRuntimeMXBean().getName();
		int p = processInfo.indexOf('@');
		if (p > 0) {
			return processInfo.substring(p + 1);
		} else {
			return processInfo;
		}
    }
    
    /**
     * Returns true if jobserver is a unix system
     * 
     * {talendTypes} Boolean
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
     * Runs a process
     * @param command an array of the command and its arguments
     * @return the exit code of the process
     * 
     * {talendTypes} Integer
     * {Category} JobUtil
     * {param} string(command) command: 
     * {example} runProcess(command) # 0
     */
    public static int runProcess(String...command) {
    	int exitCode = -1;
    	try {
			final Process p = new ProcessBuilder(command).start();
			Thread st = new Thread(new Runnable() {

				@Override
				public void run() {
		            try {
				        try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
				            String line = null;
							while ((line = input.readLine()) != null) {
							    System.out.println(line);
							}
				        }
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			});
			Thread et = new Thread(new Runnable() {

				@Override
				public void run() {
		            try {
				        try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
				            String line = null;
							while ((line = input.readLine()) != null) {
							    System.err.println(line);
							}
				        }
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			});
			st.start();
			et.start();
			exitCode = p.waitFor();
			st.join(100);
			et.join(100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return exitCode;
    }

    /**
     * Makes the file executable under Unix
     * @param file
     * 
     * {talendTypes} Void
     * {Category} JobUtil
     * {param} string(file) command: 
     * {example} makeExecutable(file) # 0
     */
    public static void makeExecutable(String file) {
    	if (isUnixSystem()) {
    		try {
        		java.nio.file.Files.setPosixFilePermissions(java.nio.file.Paths.get(file), java.nio.file.attribute.PosixFilePermissions.fromString("rwxr--r--"));
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    /**
     * Makes the file executable under Unix
     * @param message
     * @throws Exception
     * 
     * {talendTypes} Void
     * {Category} JobUtil
     * {param} string(message) message: 
     * {example} throwException(message) # 0
     */
    public static void throwException(String ...message ) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	if (message != null) {
        	for (String s : message) {
        		if (s != null) {
        			sb.append(s);
        		}
        	}
    	}
    	if (true) {
    		throw new Exception(sb.toString());
    	}
    }


}
