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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility class to parse a String into a Date 
 * by testing a number of common pattern
 * This class is thread save.
 * 
 * @author jan.lolling@gmail.com
 */
public class GenericDateUtil {
	
	private static ThreadLocal<DateParser> threadLocal = new ThreadLocal<DateParser>();
	
	public static Date parseDate(String source) throws ParseException {
		return parseDate(source, null);
	}

    /**
     * parseDate: returns the Date from the given text representation
     * Tolerates if the content does not fit to the given pattern and retries it
     * with build in patterns
     * 
     * {Category} GenericDateUtil
     * 
     * {talendTypes} Date
     * 
     * {param} String(dateString)
     * {param} String(suggestedPattern)
     * 
     * {example} parseDate(dateString, suggestedPattern).
     */
	public static Date parseDate(String source, String suggestedPattern) throws ParseException {
		DateParser p = threadLocal.get();
		if (p == null) {
			p = new DateParser();
			threadLocal.set(p);
		}
		return p.parseDate(source, suggestedPattern);
	}
	
	static class DateParser {
		
		private List<String> datePatternList = null;
		private List<String> timePatternList = null;

		DateParser() {
			datePatternList = new ArrayList<String>();
			datePatternList.add("yyyy-MM-dd");
			datePatternList.add("dd.MM.yyyy");
			datePatternList.add("d.MM.yyyy");
			datePatternList.add("d.M.yy");
			datePatternList.add("dd.MM.yy");
			datePatternList.add("dd.MMM.yyyy");
			datePatternList.add("yyyyMMdd");
			datePatternList.add("dd/MM/yyyy");
			datePatternList.add("dd/MM/yy");
			datePatternList.add("dd/MMM/yyyy");
			datePatternList.add("d/M/yy");
			datePatternList.add("MM/dd/yyyy");
			datePatternList.add("MM/dd/yy");
			datePatternList.add("dd/MMM/yyyy");
			datePatternList.add("M/d/yy");
			datePatternList.add("dd-MM-yyyy");
			datePatternList.add("dd-MM-yy");
			datePatternList.add("dd-MMM-yyyy");
			datePatternList.add("d-M-yy");
			timePatternList = new ArrayList<String>();
			timePatternList.add(" HHmmss");
			timePatternList.add(" HH:mm:ss");
			timePatternList.add(" HH:mm:ss.SSS");
			timePatternList.add("'T'HH:mm:ss.SSSZ");
		}
		
		public Date parseDate(String text, String userPattern) throws ParseException {
			if (text != null) {
				SimpleDateFormat sdf = new SimpleDateFormat();
				Date dateValue = null;
				if (userPattern != null && userPattern.isEmpty() == false) {
					if (datePatternList.contains(userPattern) == false) {
						datePatternList.add(0, userPattern);
					}
				}
				for (String pattern : datePatternList) {
					sdf.applyPattern(pattern);
					try {
						dateValue = sdf.parse(text);
						// if we continue here the pattern fits
						// now we know the date is correct, lets try the time part:
						if (text.length() - pattern.length() >= 6) {
							// there is more in the text than only the date
							for (String timepattern : timePatternList) {
								String dateTimePattern = pattern + timepattern;
								sdf.applyPattern(dateTimePattern);
								try {
									dateValue = sdf.parse(text);
									// we got it
									pattern = dateTimePattern;
									break;
								} catch (ParseException e1) {
									// ignore parsing errors, we are trying
								}
							}
						}
						// set this pattern at the top of the list to shorten the next attempt
						int pos = datePatternList.indexOf(pattern);
						if (pos > 0) {
							datePatternList.remove(pos);
						}
						datePatternList.add(0, pattern);
						return dateValue;
					} catch (ParseException e) {
						// the pattern obviously does not work
						continue;
					}
				}
				throw new ParseException("The value: " + text + " could not be parsed to a Date.", 0);
			} else {
				return null;
			}
		}

	}
	
}