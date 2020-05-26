package routines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLParameterReplace {
	
	private static Map<String, String> values = new HashMap<String, String>();
	
	/**
	 * add parameter to replace later
	 * @param paramName
	 * @param paramValue
	 * 
	 * {Category} SQLParameterReplace
     * 
     * {talendTypes} String
     * 
     * {param} String(paramName) paramName: String.
     * {param} String(paramValue) paramValue: String.
     * 
     * {example} addValue(paramName,paramValue)
	 */
	public static void addValue(String paramName, String paramValue) {
		values.put(paramName, paramValue);
	}
	
	/**
	 * replace the parameters in the given SQL code and check for missing
	 * @param sql
	 * @return sql code with replaced parameters
	 * 
	 * {Category} SQLParameterReplace
     * 
     * {talendTypes} String
     * 
     * {param} String(sql) sql: String.
     * 
     * {example} replaceAndCheckParameters(sql)
	 */
	public static String replaceAndCheckParameters(String sql) throws Exception {
		for (Map.Entry<String, String> entry : values.entrySet()) {
			sql = RegexUtil.replaceByRegexGroups(sql, "(:"+entry.getKey()+")", entry.getValue());
		}
		String errors = checkParametersNotReplaced(sql, ",");
		if (StringUtil.isEmpty(errors) == false) {
			throw new Exception("Missing parameters: " + errors + " in the query:\n" + sql);
		}
		return sql;
	}

	/**
	 * replace the parameters in the given SQL code
	 * @param sql
	 * @return sql code with replaced parameters
	 * 
	 * {Category} SQLParameterReplace
     * 
     * {talendTypes} String
     * 
     * {param} String(sql) sql: String.
     * 
     * {example} replaceParameters(sql)
	 */
	public static String replaceParameters(String sql) {
		for (Map.Entry<String, String> entry : values.entrySet()) {
			sql = RegexUtil.replaceByRegexGroups(sql, "(:"+entry.getKey()+")", entry.getValue());
		}
		return sql;
	}
	
	/**
	 * check if parameters are not replaced
	 * @param sql
	 * @param delimiter
	 * @return String containing the missing parameters delimited
	 * {Category} SQLParameterReplace
     * 
     * {talendTypes} String
     * 
     * {param} String(sql) sql: String.
     * {param} String(delimiter) delimiter: String.
     * 
     * {example} checkParametersNotReplaced(sql,",")
	 */
	public static String checkParametersNotReplaced(String sql, String delimiter) {
		StringBuilder missingParameters = new StringBuilder();
		List<String> missingParamsList = RegexUtil.extractByRegexGroupsToList(sql, "(:[a-z_0-9]{1,}[:]{0,1})");
		boolean firstLoop = true;
		for (String p : missingParamsList) {
			if (p.endsWith(":")) {
				continue;
			}
			if (firstLoop) {
				firstLoop = false;
			} else {
				missingParameters.append(delimiter);
			}
			missingParameters.append(p.substring(1));
		}
		return missingParameters.toString();
	}

}
