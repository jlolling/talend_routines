/**
 * Copyright 2015 Jan Lolling jan.lolling@gmail.com
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
package routines;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class helps to render records as a html table
 * @author jan.lolling@cimt-ag.de
 */
public class HTMLTable {
	
	private List<String> headerRow = null;
	private List<List<String>> rows = null;
	private int rowIndex = -1;
	private String tableStyle = "border-collapse: collapse";
	private String headerStyle = "border: 2px solid black; padding: 5px;";
	private String dataCellStyle = "border: 1px solid black; padding: 5px;";
	private Map<Integer, String> additionalRowStyles = new HashMap<Integer, String>();
	private Map<String, String> additionalCellSyles = new HashMap<String, String>();
	
	private String getDataRowStyle(int rowIndex) {
		String style = null;
		if (dataCellStyle != null) {
			style = dataCellStyle;
		}
		String additionalRowStyle = additionalRowStyles.get(rowIndex);
		if (additionalRowStyle != null) {
			if (style.trim().endsWith(";") == false) {
				style = style + ";";
			}
			style = style + additionalRowStyle;
		}
		return style;
	}
	
	private String getDataCellStyle(int rowIndex, int columnIndex) {
		String style = null;
		if (dataCellStyle != null) {
			style = dataCellStyle;
		}
		String key = rowIndex + "#" + columnIndex;
		String additionalCellStyle = additionalCellSyles.get(key);
		if (additionalCellStyle != null && additionalCellStyle.trim().isEmpty() == false) {
			if (style.trim().endsWith(";") == false) {
				style = style + ";";
			}
			style = style + additionalCellStyle;
		}
		return style;
	}
	
	public String getTableStyle() {
		return tableStyle;
	}

	public void setTableStyle(String tableStyle) {
		this.tableStyle = tableStyle;
	}

	public String getHeaderStyle() {
		return headerStyle;
	}

	public void setHeaderStyle(String headerStyle) {
		this.headerStyle = headerStyle;
	}

	public String getDataStyle() {
		return dataCellStyle;
	}

	public void setDataStyle(String dataStyle) {
		this.dataCellStyle = dataStyle;
	}

	/**
	 * set here the names for the header row
	 * if this method will no b called, the table will not have a header
	 * @param header comma separated list of names
	 */
	public void setHeader(String ...header) {
		if (header != null && header.length > 0) {
			headerRow = new ArrayList<String>();
			for (String value : header) {
				headerRow.add(value);
			}
		}
	}
	
	/**
     * add a new row without applying a style
	 */
	public void addRow() {
		addRow(null);
	}
	
	/**
	 * add a new empty row
	 * @param style if not null this row will rendered with the given CSS style
	 */
	public void addRow(String style) {
		if (rows == null) {
			rows = new ArrayList<List<String>>();
		}
		rowIndex++;
		rows.add(new ArrayList<String>());
		if (style != null && style.trim().isEmpty() == false) {
			additionalRowStyles.put(rowIndex, style);
		}
	}
	
	/**
	 * add a value to the current row
	 * @param value a string value
	 */
	public void addDataValue(String value) {
		if (rowIndex < 0) {
			throw new IllegalArgumentException("Call addRow() first!");
		}
		List<String> row = rows.get(rowIndex);
		if (value == null) {
			value = "";
		}
		row.add(value);
	}

	private void setupCellStyle(List<String> row, int rowIndex, String style) {
		int columnIndex = row.size() - 1; 
		if (style != null && style.trim().isEmpty() == false) {
			String key = rowIndex + "#" + columnIndex;
			additionalCellSyles.put(key, style);
		}
	}

	/**
	 * add a value to the current row
	 * @param value a boolean value
	 */
	public void addDataValue(Boolean value) {
		addDataValue(value, null);
	}
	
	/**
	 * add a value to the current row
	 * @param value a boolean value
	 * @param additionalCellStyle CSS style for this cell
	 */
	public void addDataValue(Boolean value, String additionalCellStyle) {
		if (rowIndex < 0) {
			throw new IllegalArgumentException("Call addRow() first!");
		}
		List<String> row = rows.get(rowIndex);
		if (value != null) {
			row.add(String.valueOf(value));
		} else {
			row.add("");
		}
		setupCellStyle(row, rowIndex, additionalCellStyle);
	}
	
	/**
	 * add a value to the current row
	 * @param value a boolean value
	 */
	public void addDataValue(Date value) {
		addDataValue(value, null, null);
	}

	/**
	 * add a value to the current row
	 * @param value a boolean value
	 * @param pattern SimpleDateFormat pattern
	 * @param additionalCellStyle CSS style for this cell
	 */
	public void addDataValue(Date value, String pattern, String additionalCellStyle) {
		if (rowIndex < 0) {
			throw new IllegalArgumentException("Call addRow() first!");
		}
		List<String> row = rows.get(rowIndex);
		if (value != null) {
			if (pattern == null) {
				pattern = "yyyy-MM-dd HH:mm:ss";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			row.add(sdf.format(value));
		} else {
			row.add("");
		}
		setupCellStyle(row, rowIndex, additionalCellStyle);
	}
	
	/**
	 * add a value to the current row
	 * @param value a boolean value
	 */
	public void addDataValue(Number value) {
		addDataValue(value, null, null);
	}

	/**
	 * add a value to the current row
	 * @param value a boolean value
	 * @param format see DecimalFormat
	 * @param additionalCellStyle CSS style for this cell
	 */
	public void addDataValue(Number value, String format, String additionalCellStyle) {
		if (rowIndex < 0) {
			throw new IllegalArgumentException("Call addRow() first!");
		}
		List<String> row = rows.get(rowIndex);
		if (value != null) {
			if (format == null || format.isEmpty()) {
				if (value instanceof Short || value instanceof Integer || value instanceof Long) {
					format = "##0";
				} else {
					format = "#,##0.00";
				}
			}
			NumberFormat nf = new DecimalFormat(format);
			row.add(nf.format(value));
		} else {
			row.add("");
		}
		setupCellStyle(row, rowIndex, additionalCellStyle);
	}

	/**
	 * Checks if there are rows added to this table
	 * @return true if at least one row was added
	 */
	public boolean hasRows() {
		return rowIndex >= 0;
	}
	
	/**
	 * builds the html code for the table and applies all styles
	 * @return html code fragment for the table
	 */
	public String buildHtmlCode() {
		if (rows == null) {
			return ""; // if no rows added, simply render nothing
		}
		StringBuilder table = new StringBuilder();
		if (tableStyle != null) {
			table.append("<table style=\"");
			table.append(tableStyle);
			table.append("\">\n");
		} else {
			table.append("<table>\n");
		}
		if (headerRow != null && headerRow.isEmpty() == false) {
			table.append("<tr>\n");
			for (String value : headerRow) {
				if (headerStyle != null) {
					table.append("<th style=\"");
					table.append(headerStyle);
					table.append("\">");
				} else {
					table.append("<th>");
				}
				table.append(value);
				table.append("</th>\n");
			}
			table.append("</tr>\n");
		}
		rowIndex = 0;
		for (List<String> row : rows) {
			String rowStyle = getDataRowStyle(rowIndex);
			if (rowStyle != null) {
				table.append("<tr style=\"");
				table.append(rowStyle);
				table.append("\">\n");
			} else {
				table.append("<tr>\n");
			}
			int columnIndex = 0;
			for (String value : row) {
				String cellStyle = getDataCellStyle(rowIndex, columnIndex);
				if (cellStyle != null) {
					table.append("<td style=\"");
					table.append(cellStyle);
					table.append("\">");
				} else {
					table.append("<td>");
				}
				table.append(value);
				table.append("</td>\n");
				columnIndex++;
			}
			table.append("</tr>\n");
			rowIndex++;
		}
		table.append("</table>\n");
		return table.toString();
	}

}