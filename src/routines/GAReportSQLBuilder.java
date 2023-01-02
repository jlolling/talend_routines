package routines;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GAReportSQLBuilder {
		
	private static class Entry {
		int id = 0;
		String apiName = null;
		String columnName = null;
		int length = 255;
		String sqlType = null;
		String sqlConversion = null;
		
		@Override
		public int hashCode() {
			return apiName.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof Entry) {
				return ((Entry) o).apiName.equalsIgnoreCase(apiName);
			}
			return false;
		}
	}
	
	private static class CalculatedColumn {
		String columnName = null;
		String sql = null;
		String type = null;
	}
	
	private static List<Entry> allDimensions = new ArrayList<Entry>();
	private static List<Entry> allMetrics = new ArrayList<Entry>();
	private String[] reportMetricList = null;
	private String[] reportDimensionList = null;
	private int reportId = 0;
	private String reportName = null;
	private String reportDimensions = null;
	private String reportMetrics = null;
	private String reportFilters = null;
	private String reportSegment = null;
	private static Map<Integer, List<CalculatedColumn>> mapCalculatedColumns = new HashMap<>();
	
	public String getReportFilters() {
		return reportFilters;
	}

	public void setReportFilters(String reportFilters) {
		this.reportFilters = reportFilters;
	}

	public String getReportSegment() {
		return reportSegment;
	}

	public void setReportSegment(String reportSegment) {
		this.reportSegment = reportSegment;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public void setReportDimensions(String reportDimensions) throws Exception {
		if (reportDimensions != null) {
			this.reportDimensions = reportDimensions.trim().replace(" ", "") + ",";
		} else {
			this.reportDimensions = "";
		}
		reportDimensionList = this.reportDimensions.split(",");
		if (reportDimensionList.length > 0) {
			String missing = checkDimensions();
			if (missing != null && missing.isEmpty() == false) {
				throw new Exception("Unknown report dimensions found: " + missing);
			}
		}
	}

	private String checkDimensions() {
		StringBuilder missing = new StringBuilder();
		for (String apiName : reportDimensionList) {
			boolean found = false;
			for (Entry e : allDimensions) {
				if (apiName.equalsIgnoreCase(e.apiName)) {
					found = true;
					break;
				}
			}
			if (found == false) {
				if (missing.length() > 0) {
					missing.append(",");
				}
				missing.append(apiName);
			}
		}
		return missing.toString();
	}
	
	private String checkMetrics() {
		StringBuilder missing = new StringBuilder();
		for (String apiName : reportMetricList) {
			boolean found = false;
			for (Entry e : allMetrics) {
				if (apiName.equalsIgnoreCase(e.apiName)) {
					found = true;
					break;
				}
			}
			if (found == false) {
				if (missing.length() > 0) {
					missing.append(",");
				}
				missing.append(apiName);
			}
		}
		return missing.toString();
	}

	public void setReportMetrics(String reportMetrics) throws Exception {
		if (reportMetrics != null && reportMetrics.trim().isEmpty() == false) {
			this.reportMetrics = reportMetrics.trim().replace(" ", "") + ",";
			reportMetricList = this.reportMetrics.split(",");
			String missing = checkMetrics();
			if (missing != null && missing.isEmpty() == false) {
				throw new Exception("Unknown report metrics found: " + missing);
			}
		} else {
			throw new IllegalArgumentException("reportMetrics cannot be null or empty");
		}
	}
	
	public int getReportId() {
		return reportId;
	}
	
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}
	
	public static void addDimension(int id, String apiName, String columnName, Integer length, String sqlType, String sqlConversion) {
		Entry e = new Entry();
		e.id = id;
		e.apiName = apiName;
		e.columnName = columnName;
		if (length != null && length > 0) {
			e.length = length;
		}
		e.sqlType = sqlType;
		e.sqlConversion = sqlConversion;
		allDimensions.add(e);
	}

	public static void addMetric(int id, String apiName, String columnName) {
		Entry e = new Entry();
		e.id = id;
		e.apiName = apiName;
		e.columnName = columnName;
		allMetrics.add(e);
	}
	
	private boolean isDimensionInReport(String apiName) {
		if (reportDimensions.toLowerCase().contains(apiName.toLowerCase() + ",")) {
			return true;
		}
		return false;
	}
	
	private boolean isMetricInReport(String apiName) {
		if (reportMetrics.toLowerCase().contains(apiName.toLowerCase() + ",")) {
			return true;
		}
		return false;
	}
	
	public String buildReportView() {
		if (reportId == 0) {
			throw new IllegalArgumentException("Report ID not given!");
		}
		if (allMetrics.isEmpty()) {
			throw new IllegalStateException("There are no metrics added. At least one metric is mandatory !");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("create or replace view v_ga_report_" + StringUtil.camelToSnake(reportName) + " as\n");
		sb.append("select\n");
		boolean firstLoop = true;
		// build field list for dimensions
		for (Entry e : allDimensions) {
			if (isDimensionInReport(e.apiName) == false) {
				continue;
			}
			if (firstLoop) {
				firstLoop = false;
				sb.append("    d");
				sb.append(e.id);
				sb.append(".report_date,\n");
				sb.append("    d");
				sb.append(e.id);
				sb.append(".profile_id,\n");
				sb.append("    d");
				sb.append(e.id);
				sb.append(".row_num,\n");
			} else {
				sb.append(",\n");
			}
			sb.append("    coalesce(d");
			sb.append(e.id);
			sb.append(".dimension_value, '-') as ");
			sb.append(e.columnName);
		}
		// build field list for metrics
		for (Entry e : allMetrics) {
			if (isMetricInReport(e.apiName) == false) {
				continue;
			}
			if (firstLoop) {
				firstLoop = false;
				sb.append("    m");
				sb.append(e.id);
				sb.append(".row_num,\n");
				sb.append("    m");
				sb.append(e.id);
				sb.append(".report_date,\n");
				sb.append("    m");
				sb.append(e.id);
				sb.append(".profile_id,\n");
			} else {
				sb.append(",\n");
			}
			sb.append("    coalesce(m");
			sb.append(e.id);
			sb.append(".metric_value, 0) as ");
			sb.append(e.columnName);
		}
		firstLoop = true;
		Entry firstMetric = null;
		// build from and joins for metrics
		for (Entry e : allMetrics) {
			if (isMetricInReport(e.apiName) == false) {
				continue;
			}
			if (firstLoop) {
				firstMetric = e;
				firstLoop = false;
				sb.append("\nfrom ga_report_result_metric_values_" + reportId + " m");
				sb.append(e.id);
			} else {
				sb.append("\njoin ga_report_result_metric_values_" + reportId + " m");
				sb.append(e.id);
				// on row_num
				sb.append(" on m");
				sb.append(e.id);
				sb.append(".row_num = ");
				sb.append("m");
				sb.append(firstMetric.id);
				sb.append(".row_num");
				// and profile_id
				sb.append(" and m");
				sb.append(e.id);
				sb.append(".profile_id = ");
				sb.append("m");
				sb.append(firstMetric.id);
				sb.append(".profile_id");
				// and report_date
				sb.append(" and m");
				sb.append(e.id);
				sb.append(".report_date = ");
				sb.append("m");
				sb.append(firstMetric.id);
				sb.append(".report_date");
			}
		}
		// build left joins for dimensions
		for (Entry e : allDimensions) {
			if (isDimensionInReport(e.apiName) == false) {
				continue;
			}
			sb.append("\nleft join ga_report_result_dimension_values_" + reportId + " d");
			sb.append(e.id);
			sb.append(" on d");
			sb.append(e.id);
			sb.append(".row_num = m");
			sb.append(firstMetric.id);
			sb.append(".row_num");
			sb.append(" and d");
			sb.append(e.id);
			sb.append(".dimension_id = ");
			sb.append(e.id);
			sb.append(" and d");
			sb.append(e.id);
			sb.append(".profile_id = m");
			sb.append(firstMetric.id);
			sb.append(".profile_id");
			sb.append(" and d");
			sb.append(e.id);
			sb.append(".report_date = m");
			sb.append(firstMetric.id);
			sb.append(".report_date");
		}
		// build where conditions for dimensions
		firstLoop = true;
		// build where conditions for metrics
		for (Entry e : allMetrics) {
			if (isMetricInReport(e.apiName) == false) {
				continue;
			}
			if (firstLoop) {
				firstMetric = e;
				firstLoop = false;
				sb.append("\nwhere m");
				sb.append(e.id);
				sb.append(".metric_id = ");
				sb.append(e.id);
			} else {
				sb.append("\n  and m");
				sb.append(e.id);
				sb.append(".metric_id = ");
				sb.append(e.id);
			}
		}
		sb.append(";\n");
		return sb.toString();
	}
	
	public String buildReportTable() {
		if (reportId == 0) {
			throw new IllegalArgumentException("Report ID not given!");
		}
		if (allMetrics.isEmpty()) {
			throw new IllegalStateException("There are no metrics added. At least one metric is mandatory !");
		}
		StringBuilder sb = new StringBuilder();
		String tableName = "report_data_" + StringUtil.camelToSnake(reportName);
		sb.append("create table if not exists " + tableName + " (\n");
		sb.append("report_date datetime not null,\n");
		sb.append("profile_id bigint not null,\n");
		sb.append("row_num integer");
		for (Entry e : allDimensions) {
			if (isDimensionInReport(e.apiName) == false) {
				continue;
			}
			sb.append(",\n");
			sb.append(e.columnName);
			if (e.sqlType != null && e.sqlType.trim().isEmpty() == false) {
				sb.append(" ");
				sb.append(e.sqlType);
			} else {
				sb.append(" varchar(");
				sb.append(e.length);
				sb.append(")");
			}
		}
		List<Entry> dynParams = getDimensionsFromDynReports();
		for (Entry e : dynParams) {
			sb.append(",\n");
			sb.append(e.columnName);
			if (e.sqlType != null && e.sqlType.trim().isEmpty() == false) {
				sb.append(" ");
				sb.append(e.sqlType);
			} else {
				sb.append(" varchar(");
				sb.append(e.length);
				sb.append(")");
			}
		}
		// build where conditions for metrics
		for (Entry e : allMetrics) {
			if (isMetricInReport(e.apiName) == false) {
				continue;
			}
			sb.append(",\n");
			sb.append(e.columnName);
			sb.append(" double");
		}
		List<CalculatedColumn> calculatedColumns = mapCalculatedColumns.get(reportId);
		if (calculatedColumns != null) {
			for (CalculatedColumn c : calculatedColumns) {
				sb.append(",\n");
				sb.append(c.columnName);
				sb.append(" ");
				sb.append(c.type);
			}
		}
		sb.append(",\nindex (report_date, profile_id)");
		sb.append("\n);\n");
		return sb.toString();
	}
	
	public String buildReportDataTableSourceView() {
		if (reportId == 0) {
			throw new IllegalArgumentException("Report ID not given!");
		}
		if (allMetrics.isEmpty()) {
			throw new IllegalStateException("There are no metrics added. At least one metric is mandatory !");
		}
		StringBuilder sb = new StringBuilder();
		String tableName = "report_data_" + StringUtil.camelToSnake(reportName);
		String reportView = "v_ga_report_" + StringUtil.camelToSnake(reportName);
		sb.append("create or replace view  v_" + tableName + "_source as\n");
		sb.append("select\n");
		sb.append("v.report_date,\n");
		sb.append("v.profile_id,\n");
		sb.append("v.row_num");
		for (Entry e : allDimensions) {
			if (isDimensionInReport(e.apiName) == false) {
				continue;
			}
			sb.append(",\n");
			if (e.sqlConversion != null && e.sqlConversion.trim().isEmpty() == false) {
				sb.append(e.sqlConversion);
				sb.append(" as ");
				sb.append(e.columnName);
			} else {
				sb.append("v.");
				sb.append(e.columnName);
			}
		}
		final List<Entry> dynParams = getDimensionsFromDynReports();
		int index = 1;
		for (Entry e : dynParams) {
			sb.append(",\n");
			sb.append("d"+ index + ".dyn_param_value as ");
			sb.append(e.columnName);
			index++;
		}
		// build where conditions for metrics
		for (Entry e : allMetrics) {
			if (isMetricInReport(e.apiName) == false) {
				continue;
			}
			sb.append(",\n");
			sb.append("v.");
			sb.append(e.columnName);
		}
		List<CalculatedColumn> calculatedColumns = mapCalculatedColumns.get(reportId);
		if (calculatedColumns != null) {
			for (CalculatedColumn c : calculatedColumns) {
				sb.append(",\n");
				sb.append(c.sql);
				sb.append(" as ");
				sb.append(c.columnName);
			}
		}
		sb.append("\nfrom ");
		sb.append(reportView);
		sb.append(" v ");
		index = 1;
		for (Entry e : dynParams) {
			sb.append("\njoin ga_report_result_dyn_param_values_");
			sb.append(reportId);
			sb.append(" d" + index);
			sb.append(" on d" + index + ".profile_id = v.profile_id and d" + index + ".report_date = v.report_date and d" + index + ".row_num = v.row_num and d" + index + ".dyn_param_name = '" + e.columnName + "'");
			index++;
		}
		sb.append(";\n");
		return sb.toString();
	}

	public List<Entry> getDimensionsFromDynReports() {
		List<Entry> dimensions = new ArrayList<>();
		List<String> names = getPlaceHolderNames(reportFilters);
		for (String name : names) {
			Entry e = new Entry();
			e.apiName = name;
			e.columnName = name;
			e.id = -1;
			e.length = 255;
			dimensions.add(e);
		}
		names = getPlaceHolderNames(reportSegment);
		for (String name : names) {
			Entry e = new Entry();
			e.apiName = name;
			e.columnName = name;
			e.id = -1;
			e.length = 255;
			if (dimensions.contains(e) == false) {
				dimensions.add(e);
			}
		}
		return dimensions;
	}
	
	private List<String> getPlaceHolderNames(String text) {
		List<String> names = new ArrayList<>();
		if (text != null) {
			Pattern pattern = Pattern.compile("\\{([a-z_0-9]{1,})\\}");
	        Matcher matcher = pattern.matcher(text);
	        while (matcher.find()) {
	            if (matcher.start() < matcher.end()) {
	            	for (int i = 1, n = matcher.groupCount(); i <= n; i++) {
		                names.add(matcher.group(i));
	            	}
	            }
	        }
		}
		return names;
	}
	
	public static void addCalculatedColumn(int report_id, String name, String sql, String type) {
		List<CalculatedColumn> calculatedColumns = mapCalculatedColumns.get(report_id);
		if (calculatedColumns == null) {
			calculatedColumns = new ArrayList<>();
			mapCalculatedColumns.put(report_id, calculatedColumns);
		}
		CalculatedColumn c = new CalculatedColumn();
		c.columnName = name;
		c.sql = sql;
		c.type = type;
		calculatedColumns.add(c);
	}
		
}