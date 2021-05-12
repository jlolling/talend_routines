package routines;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVUtil {
	
	private static Map<String, CSVFormat> formats = new HashMap<>();
	
	public static int getCountColumns(String row, char delimiter, char enclosure) throws Exception {
		int count = 0;
		if (row != null && row.trim().isEmpty() == false) {
			CSVFormat csvFormat =  formats.get(delimiter + "/" + enclosure);
			if (csvFormat == null) {
				csvFormat = CSVFormat.newFormat(delimiter).withQuote(enclosure);
				formats.put(delimiter + "/" + enclosure, csvFormat);
			}
			CSVParser parser = csvFormat.parse(new StringReader(row));
			CSVRecord firstRecord = parser.iterator().next();
			return firstRecord.size();
		}		
		return count;
	}

}
