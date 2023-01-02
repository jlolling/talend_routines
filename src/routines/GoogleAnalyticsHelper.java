package routines;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class GoogleAnalyticsHelper {

    /**
     * build on string from line separated filter terms
     * 
     * 
     * {talendTypes} String
     * 
     * {Category} GoogleAnalyticsHelper
     * 
     * {param} string("ga:source==google","ga:medium==organic") filterTerms: The string need to be concatenated.
     * 
     * {example} buildAndFilter("ga:source==google","ga:medium==organic") # 
     */
    public static String buildAndFilter(String ...filterTerms) {
    	if (filterTerms != null) {
    		StringBuilder sb = new StringBuilder();
    		boolean firstLoop = true;
    		for (String term : filterTerms) {
    			if (term != null && term.isEmpty() == false) {
        			if (firstLoop) {
        				firstLoop = false;
        			} else {
        				sb.append(";");
        			}
        			// remove line breaks
        			term = term.replace("\n", "");
        			term = term.replace("\r", "");
        			sb.append(term);
    			}
    		}
    		return sb.toString();
    	} else {
    		return null;
    	}
    }
    
    /**
     * build technical name for ui names of dims and metrics
     * 
     * 
     * {talendTypes} String
     * 
     * {Category} GoogleAnalyticsHelper
     * 
     * {param} string(ga_ui_name) ga_ui_name: The string need to be concatenated.
     * 
     * {example} buildTechnName(ga_ui_name) # 
     */    
    public static String buildTechnName(String ga_ui_name) {
    	if (ga_ui_name == null) {
    		throw new IllegalArgumentException("ga_ui_name cannot be null");
    	}
    	ga_ui_name = StringUtil.camelToSnake(ga_ui_name);
    	ga_ui_name = ga_ui_name
    					.replace("/", " ")
    					.replace("(", " ")
    					.replace(")", " ")
    					.replace(".", " ")
    					.replace(":", " ")
    					.replace("-", " ")
    					.replace("%", "perc");
    	ga_ui_name = StringUtil.reduceMultipleSpacesToOne(ga_ui_name);
    	ga_ui_name = ga_ui_name.replace(' ', '_');
    	return ga_ui_name;
    }
    
    /**
     * returns a list with maps. The key of the map is the placeholder name, the value in the map is the placeholder value.
     * @param query
     * @return list of placeholder key value maps
     * @throws Exception
     */
    public static List<Map<String, String>> selectPlaceholderValues(String query, Connection conn) throws Exception {
    	if (StringUtil.isEmpty(query)) {
    		throw new Exception("Query for fetching placeholder values cannot be empty or null");
    	}
    	if (conn == null) {
    		throw new Exception("Connection cannot be null");
    	}
    	if (conn.isClosed()) {
    		throw new Exception("Connection is already closed");
    	}
    	final Statement stat = conn.createStatement();
    	final ResultSet rs = stat.executeQuery(query);
    	final ResultSetMetaData rsmd = rs.getMetaData();
    	final List<String> columnNames = new ArrayList<>();
    	int columnCount = rsmd.getColumnCount();
    	for (int i = 1; i <= columnCount; i++) {
    		String name = rsmd.getColumnLabel(i);
    		if (name == null) {
    			name = rsmd.getColumnName(i);
    		}
    		columnNames.add(name);
    	}
    	final List<Map<String, String>> results = new ArrayList<>();
    	while (rs.next()) {
    		for (int i = 1; i <= columnCount; i++) {
    			Map<String, String> record = new HashMap<>();
    			String value = rs.getString(i);
    			if (value == null) {
    				value = ""; // we need always a value
    			}
    			record.put(columnNames.get(i - 1), value);
    			results.add(record);
    		}
    	}
    	rs.close();
    	stat.close();
    	return results;
    }
    
    /**
     * Placeholder replace function
     * @param values map with key=name of placeholder and value as replacement
     * @param template contains the placeholders in form of {<name>}
     * @return the string with replaced placeholders
     * @throws Exception if there are still placeholders in the result - means placeholders are missed
     */
    public static String replacePlaceholders(Map<String, String> values, String template) throws Exception {
    	if (values == null) {
    		throw new Exception("Placeholder map cannot be null");
    	}
    	if (StringUtil.isEmpty(template)) {
    		return template;
    	}
    	if (template.contains("{") == false) {
    		return template; // because there are no placeholders
    	}
    	// iterate through the received placeholder values and replace them in the template
    	for (Map.Entry<String, String> entry : values.entrySet()) {
    		template = template.replace("{" + entry.getKey() + "}", entry.getValue());
    	}
    	if (template.contains("{") || template.contains("}")) {
    		throw new Exception("Not all placeholders are replaced! result=" + template);
    	}
    	return template;
    }
}
