package routines;

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
        
}
