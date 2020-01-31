package routines;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class CreateTableFromResultSet {
	
	private ResultSetMetaData rsmd = null;
	private List<String> columnTypesWithoutLength = new ArrayList<String>();
	private String generatedSQL = null;
	
	public void addColumnTypeWithoutLength(String columnType) {
		if (columnType != null && columnType.trim().isEmpty() == false) {
			columnTypesWithoutLength.add(columnType.toLowerCase());
		}
	}
	
	public String buildCreateTable(ResultSet rs, String targetTableName) throws Exception {
		if (rs == null) {
			throw new IllegalArgumentException("ResultSet rs cannot be null");
		}
		rsmd = rs.getMetaData();
		rs.close();
		StringBuilder sb = new StringBuilder();
		sb.append("create table "); 
		sb.append(targetTableName);
		sb.append(" (\n");
		int columnCount = rsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = rsmd.getColumnLabel(i);
			String columnType = rsmd.getColumnTypeName(i);
			sb.append("    ");
			sb.append(columnName);
			sb.append(" ");
			sb.append(columnType);
			int length = rsmd.getPrecision(i);
			if (length > 0 && columnTypesWithoutLength.contains(columnType.toLowerCase()) == false) {
				String className = rsmd.getColumnClassName(i).toLowerCase();
				sb.append("(");
				sb.append(String.valueOf(length));
				if (className.contains("double") || className.contains("float") || className.contains("decimal")) {
					sb.append(",");
					int scale = rsmd.getScale(i);
					sb.append(scale);
				}
				sb.append(")");
			}
			if (i < columnCount) {
				sb.append(",\n");
			}
		}
		sb.append("\n);");
		generatedSQL = sb.toString();
		return generatedSQL;
	}

	public String getGeneratedSQL() {
		return generatedSQL;
	}

}
