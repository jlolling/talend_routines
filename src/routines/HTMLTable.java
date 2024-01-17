/**
 * Copyright 2022 Jan Lolling jan.lolling@gmail.com
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
 * @author jan.lolling@gmail.com
 */
public class HTMLTable {
	
	private List<String> headerRow = null;
	private List<List<String>> rows = null;
	private int rowIndex = -1;
	private String tableStyle = "border-collapse: collapse";
	private String headerStyle = "border: 2px solid black; padding: 5px; background-color: #DDDDDD; font-weight: bold;";
	private String dataCellStyle = "border: 1px solid black; padding: 5px;";
	private Map<Integer, String> alternativeRowStyles = new HashMap<Integer, String>();
	private Map<String, String> additionalCellSyles = new HashMap<String, String>();
	private String rowBackgroundEvenStyle = "background-color: #FFFFFF;";
	private String rowBackgroundOddStyle = "background-color: #EEEEEE;";
	private boolean useAlternatingBackgroundForRows = true;
	public static final String STYLE_BG_RED = "background-color: #FFCCCB;";
	public static final String STYLE_BG_YELLOW = "background-color: #FFFFE0;";
	public static final String STYLE_BG_GREEN = "background-color: #90EE90;";
	public static final String STYLE_BG_WHITE = "background-color: #FFFFFF;";
	
	public boolean isUseAlternatingBackgroundForRows() {
		return useAlternatingBackgroundForRows;
	}

	public void setUseAlternatingBackgroundForRows(Boolean useAlternatingBackgroundForRows) {
		if (useAlternatingBackgroundForRows != null) {
			this.useAlternatingBackgroundForRows = useAlternatingBackgroundForRows;
		}
	}

	private String getDataRowStyle(int rowIndex) {
		String style = null;
		if (dataCellStyle != null) {
			style = dataCellStyle;
		}
		String alternativeRowStyle = alternativeRowStyles.get(rowIndex);
		if (alternativeRowStyle != null) {
			if (style.trim().endsWith(";") == false) {
				style = style + ";";
			}
			style = style + alternativeRowStyle;
		} else {
			if (useAlternatingBackgroundForRows) {
				if (rowIndex % 2 == 0) {
					// even row
					if (style.trim().endsWith(";") == false) {
						style = style + ";";
					}
					style = style + rowBackgroundEvenStyle;
				} else {
					// even row
					if (style.trim().endsWith(";") == false) {
						style = style + ";";
					}
					style = style + rowBackgroundOddStyle;
				}
			}
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
			alternativeRowStyles.put(rowIndex, style);
		}
	}
	
	/**
	 * add a value to the current row
	 * @param value a string value
	 */
	public void addDataValue(String value) {
		addDataValue(value, null);
	}
	
	/**
	 * add a value to the current row
	 * @param value a string value
	 * @param additionalCellStyle CSS style for this cell 
	 */
	public void addDataValue(String value, String additionalCellStyle) {
		if (rowIndex < 0) {
			throw new IllegalArgumentException("Call addRow() first!");
		}
		if (value == null) {
			value = "";
		} else {
			value = getHTMLEncoded(value);
		}
		List<String> row = rows.get(rowIndex);
		row.add(value);
		setupCellStyle(row, rowIndex, additionalCellStyle);
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
	 * @param additionalCellStyle CSS style for this cell
	 * @param pattern SimpleDateFormat pattern
	 */
	public void addDataValue(Date value, String additionalCellStyle, String pattern) {
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
	 * @param additionalCellStyle CSS style for this cell
	 * @param format see DecimalFormat
	 */
	public void addDataValue(Number value, String additionalCellStyle, String format) {
		if (rowIndex < 0) {
			throw new IllegalArgumentException("Call addRow() first!");
		}
		List<String> row = rows.get(rowIndex);
		if (value != null) {
			if (format == null || format.isEmpty()) {
				if (value instanceof Short || value instanceof Integer || value instanceof Long) {
					format = "##0";
				} else {
					format = "#,##0.000";
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

	/**
	 * Replace the chars with html umlauts
	 * @param text
	 * @return encoded text
	 */
	public static String getHTMLEncoded(String text) {
		if (text == null) {
			return null;
		}
		text = text.replace("Ä","&Auml;");
		text = text.replace("ä","&auml;");
		text = text.replace("Ë","&Euml;");
		text = text.replace("ë","&euml;");
		text = text.replace("Ï","&Iuml;");
		text = text.replace("ï","&iuml;");
		text = text.replace("Ö","&Ouml;");
		text = text.replace("ö","&ouml;");
		text = text.replace("Ü","&Uuml;");
		text = text.replace("ü","&uuml;");
		text = text.replace("ß","&szlig;");
		text = text.replace("À","&Agrave;");
		text = text.replace("à","&agrave;");
		text = text.replace("Á","&Aacute;");
		text = text.replace("á","&aacute;");
		text = text.replace("Â","&Acirc;");
		text = text.replace("â","&acirc;");
		text = text.replace("Ç","&Ccedil;");
		text = text.replace("ç","&ccedil;");
		text = text.replace("È","&Egrave;");
		text = text.replace("è","&egrave;");
		text = text.replace("É","&Eacute;");
		text = text.replace("é","&eacute;");
		text = text.replace("Ê","&Ecirc;");
		text = text.replace("ê","&ecirc;");
		text = text.replace("Ñ","&Ntilde;");
		text = text.replace("ñ","&ntilde;");
		text = text.replace("Ò","&Ograve;");
		text = text.replace("ò","&ograve;");
		text = text.replace("Ó","&Oacute;");
		text = text.replace("ó","&oacute;");
		text = text.replace("Ô","&Ocirc;");
		text = text.replace("ô","&ocirc;");
		text = text.replace("õ","&otilde;");
		text = text.replace("Ÿ","&Yuml;");
		text = text.replace("ÿ","&yuml;");		
		return text;
	}
}
