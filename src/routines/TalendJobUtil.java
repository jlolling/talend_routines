package routines;

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
public class TalendJobUtil {

    /**
     * Update the classpath and make it much shorter
     * 
     * 
     * {talendTypes} boolean
     * 
     * {Category} TalendJobUtil
     * 
     * {param} string(context.local_jobs_root_dir) localJobsRootDir: 
     * {param} string("myjob") jobname: 
     * {param} string("1.2") jobversion: 
     * 
     * {example} updateRunScriptClasspath(context.local_jobs_root_dir, "myjob", "1.2") #.
     */
    public static boolean updateRunScript(String localJobsRootDir, String jobname, String jobversion, String jvmOptions) throws Exception {
    	String talendRunScriptPath = localJobsRootDir + jobname + "/" + jobversion + "/" + jobname + "/" + jobname + "_run.sh";
    	//System.out.println("Check talend run script: " + talendRunScriptPath);
    	java.io.File f = new java.io.File(talendRunScriptPath);
    	if (f.exists() == false) {
    		throw new Exception("Talend job run script: " + f.getAbsolutePath() + " does not exist.");
    	}
    	String scriptContent = FileUtil.readContentfromFile(talendRunScriptPath, null);
    	String newScriptContent = scriptContent;
    	newScriptContent = newScriptContent.replace(":$ROOT_PATH/..", ":.."); // replace useless absolute path in cp
    	newScriptContent = newScriptContent.replace(":$ROOT_PATH:", ":"); // replace useless path because we have the current path before
    	newScriptContent = newScriptContent.replace(":$ROOT_PATH/", ":./"); // replace useless path because we have the current path before
    	newScriptContent = updateJvmSettingsInJobRunScriptContent(newScriptContent, jvmOptions);
    	if (newScriptContent.equals(scriptContent) == false) {
        	System.out.println("Update talend run script: " + talendRunScriptPath);
        	FileUtil.writeContentToFile(talendRunScriptPath, newScriptContent, null);
        	return true;
    	} else {
    		return false;
    	}
    }

    /**
     * Returns changed run script content
     * 
     * {talendTypes} String
     * 
     * {Category} TalendJobUtil
     * {param} string(scriptContent) scriptContent: 
     * {param} string(newSettings) newSettings: 
     * 
     * {example} updateJvmSettingsInJobRunScriptContent(scriptContent,newSettings) # true.
     */
    public static String updateJvmSettingsInJobRunScriptContent(String scriptContent, String newSettings) {
    	if (StringUtil.isEmpty(newSettings) == false) {
        	StringBuilder newContent = new StringBuilder();
        	int pos0 = scriptContent.indexOf("\njava ", 0);
        	if (pos0 > 0) {
        		int pos1 = scriptContent.indexOf(" -cp ", pos0);
        		if (pos1 > pos0) {
        			newContent.append(scriptContent.substring(0, pos0 + "\njava ".length()));
        			newContent.append(newSettings);
        			newContent.append(scriptContent.substring(pos1));
        			return newContent.toString();
        		}
        	}
    	}
    	return scriptContent;
    }

}
