package routines.test;

import org.junit.Test;

import routines.HTMLTable;

import static org.junit.Assert.assertEquals;

public class TestHTMLTable {
	
	@Test
	public void testHtmlTableOneColoredRow() {
		HTMLTable t = new HTMLTable();
		t.addRow(HTMLTable.STYLE_BG_RED);
		t.addDataValue(true);
		String actual = t.buildHtmlCode();
		System.out.println(actual);
		String expected = "<table style=\"border-collapse: collapse\">\n"
				+ "<tr style=\"border: 1px solid black; padding: 5px;background-color: #FFCCCB;\">\n"
				+ "<td style=\"border: 1px solid black; padding: 5px;\">true</td>\n"
				+ "</tr>\n"
				+ "</table>";
		assertEquals("HTML code does not match", expected.trim(), actual.trim());
	}

	@Test
	public void testHtmlTableOneColoredRowWithHeader() {
		HTMLTable t = new HTMLTable();
		t.setHeader("Boolean value", "Integer Value", "String Value");
		t.addRow(HTMLTable.STYLE_BG_RED);
		t.addDataValue(true);
		t.addDataValue(12345);
		t.addDataValue("TEXTüöä");
		t.addRow();
		t.addRow(HTMLTable.STYLE_BG_GREEN);
		t.addDataValue(false);
		t.addDataValue(99);
		t.addDataValue("Z&%");
		String actual = t.buildHtmlCode();
		System.out.println(actual);
		String expected = "<table style=\"border-collapse: collapse\">\n"
				+ "<tr>\n"
				+ "<th style=\"border: 2px solid black; padding: 5px; background-color: #DDDDDD; font-weight: bold;\">Boolean value</th>\n"
				+ "<th style=\"border: 2px solid black; padding: 5px; background-color: #DDDDDD; font-weight: bold;\">Integer Value</th>\n"
				+ "<th style=\"border: 2px solid black; padding: 5px; background-color: #DDDDDD; font-weight: bold;\">String Value</th>\n"
				+ "</tr>\n"
				+ "<tr style=\"border: 1px solid black; padding: 5px;background-color: #FFCCCB;\">\n"
				+ "<td style=\"border: 1px solid black; padding: 5px;\">true</td>\n"
				+ "<td style=\"border: 1px solid black; padding: 5px;\">12345</td>\n"
				+ "<td style=\"border: 1px solid black; padding: 5px;\">TEXT&uuml;&ouml;&auml;</td>\n"
				+ "</tr>\n"
				+ "<tr style=\"border: 1px solid black; padding: 5px;background-color: #EEEEEE;\">\n"
				+ "</tr>\n"
				+ "<tr style=\"border: 1px solid black; padding: 5px;background-color: #90EE90;\">\n"
				+ "<td style=\"border: 1px solid black; padding: 5px;\">false</td>\n"
				+ "<td style=\"border: 1px solid black; padding: 5px;\">99</td>\n"
				+ "<td style=\"border: 1px solid black; padding: 5px;\">Z&%</td>\n"
				+ "</tr>\n"
				+ "</table>";
		assertEquals("HTML code does not match", expected.trim(), actual.trim());
	}

}
