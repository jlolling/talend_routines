import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import routines.DateRangeBuilder;


public class TestDateRangeBuilder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DateRangeBuilder sp = new DateRangeBuilder();
		sp.setMaxDateDiff(10);
		sp.setMaxAllowedGap(5);
		//sp.setNullCuts(false);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> entries = new ArrayList<String>();
//		entries.add("2014-12-30 A");
//		entries.add("2015-01-01 B");
//		entries.add("2015-01-02 B");
//		entries.add("2015-01-03 B");
//		entries.add("2015-01-04 B");
//		entries.add("2015-01-05 B");
//		entries.add("2015-01-06 B");
//		entries.add("2015-01-07 B");
		entries.add("2015-01-08 B");
		entries.add("2015-01-09 B");
		entries.add("2015-01-10 B");
		entries.add("2015-01-11 B");
//		entries.add("2015-01-01 C");
		entries.add("2015-01-01 C");
		entries.add("2015-01-02 C");
		entries.add("2015-01-03 C");
		entries.add("2015-01-03 D");
		entries.add("2015-01-02 E");
		entries.add("2015-01-07 E");
		entries.add("9999-01-01 X");
		for (String s : entries) {
			String dateStr = null;
			String resetValue = null;
			if (s.length() > 0) {
				dateStr = s.substring(0, 10);
				resetValue = s.substring(10);
			}
			try {
				Object prevResetValue = sp.checkReset(resetValue);
				if (sp.addValue(sdf.parse(dateStr))) {
					System.out.println(
							sdf.format(sp.getMinValue()) + 
							" - " + 
							sdf.format(sp.getMaxValue()) + 
							" : " + 
							sp.getCount() +
							" resetValue=" +
							prevResetValue
							);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
