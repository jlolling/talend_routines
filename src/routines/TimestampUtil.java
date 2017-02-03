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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimestampUtil {
	
    /**
     * getNextDay: returns the start of the next day.
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Date() day: any day.
     * 
     * {example} getNextDay(TalendDate.getCurentDate()).
     */
    public static java.util.Date getNextDay(java.util.Date day) {
    	java.util.Calendar c = java.util.Calendar.getInstance();
    	c.setTime(day);
    	// cut minutes and seconds
    	c.set(java.util.Calendar.MINUTE, 0);
    	c.set(java.util.Calendar.SECOND, 0);
    	c.set(java.util.Calendar.MILLISECOND, 0);
    	c.add(java.util.Calendar.DATE, 1);
    	return c.getTime();
    }
    
    /**
     * truncateToDay: returns the start of the given day.
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Date() day: any day.
     * 
     * {example} truncateToDay(TalendDate.getCurentDate()).
     */
    public static java.util.Date truncateToDay(java.util.Date timestamp) {
    	if (timestamp == null) return null;
    	java.util.Calendar c = java.util.Calendar.getInstance();
    	c.setTime(timestamp);
    	// cut time
    	c.set(java.util.Calendar.MINUTE, 0);
    	c.set(java.util.Calendar.SECOND, 0);
    	c.set(java.util.Calendar.MILLISECOND, 0);
    	c.set(java.util.Calendar.HOUR_OF_DAY, 0);
    	return c.getTime();
    }
    
    /**
     * truncateToDay: returns the start of the given day.
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Date() day: any day.
     * 
     * {param} int framelagnth: length of the frame in minutes. (if framelength is not an integer devider of 60, last frame will be shorter)
     * 
     * {example} truncateToMinuteFrame(TalendDate.getCurentDate(),5).
     */
    public static java.util.Date truncateToMinuteFrame(java.util.Date timestamp, int framelength) {
    	if(timestamp == null) return null;
    	java.util.Calendar c = java.util.Calendar.getInstance();
    	c.setTime(timestamp);
    	// cut time
    	c.set(java.util.Calendar.MINUTE, c.get(Calendar.MINUTE)/framelength*framelength);
    	c.set(java.util.Calendar.SECOND, 0);
    	c.set(java.util.Calendar.MILLISECOND, 0);
    	return c.getTime();
    }
    
    /**
     * truncateToDay: returns the date starting with the current minute
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Date() day: any day.
     * 
     * {example} truncateToMinute(TalendDate.getCurentDate()).
     */
    public static java.util.Date truncateToMinute(java.util.Date timestamp) {
    	if(timestamp == null) return null;
    	java.util.Calendar c = java.util.Calendar.getInstance();
    	c.setTime(timestamp);
    	// cut time
    	c.set(java.util.Calendar.SECOND, 0);
    	c.set(java.util.Calendar.MILLISECOND, 0);
    	return c.getTime();
    }

    /**
     * truncateToMonth: returns the first day of the month of the given day.
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Date() day: any day.
     * 
     * {example} truncateToMonth(TalendDate.getCurentDate()).
     */
    public static java.util.Date truncateToMonth(java.util.Date timestamp) {
    	if(timestamp == null) return null;
    	// cut time
    	timestamp = truncateToDay(timestamp);
    	java.util.Calendar c = java.util.Calendar.getInstance();
    	c.setTime(timestamp);
    	// cut day
    	c.set(java.util.Calendar.DAY_OF_MONTH, 1);
    	return c.getTime();
    }    

    /**
     * truncateDate: returns the time without date part
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Date() day: any day.
     * 
     * {example} truncateDate(TalendDate.getCurentDate()).
     */
    public static java.util.Date truncateToTime(java.util.Date timestamp) {
    	if(timestamp == null) return null;
    	// let only survive the time
    	java.util.Calendar c = java.util.Calendar.getInstance();
    	c.setTime(timestamp);
    	// cut day
    	c.set(java.util.Calendar.DAY_OF_YEAR, 1);
    	c.set(java.util.Calendar.YEAR, 1970);
    	return c.getTime();
    }

    /**
     * truncateToWeek: returns the first day of the week of the given day.
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Date() day: any day.
     * 
     * {example} truncateToWeek(TalendDate.getCurentDate()).
     */
    public static java.util.Date truncateToWeek(java.util.Date timestamp, java.util.Locale locale) {
    	if(timestamp == null) return null;
    	// cut time
    	timestamp = truncateToDay(timestamp);
    	java.util.Calendar c = java.util.Calendar.getInstance(locale);
    	c.setTime(timestamp);
    	// cut day
    	c.set(java.util.Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
    	return c.getTime();
    }    

    /**
     * truncateToWeek: returns the first day of the week of the given day with Germany as locale.
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Date() day: any day.
     * 
     * {example} truncateToWeek(TalendDate.getCurentDate()).
     */
    public static java.util.Date truncateToWeek(java.util.Date timestamp) {
    	return truncateToWeek(timestamp, java.util.Locale.GERMANY);
    }    

    /**
     * getUTC: returns the UTC time.
     * @param siteTimestamp Timestamp from source data
     * @return Date with UTC time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} date() siteTimestamp: any day.
     * 
     * {example} getUTC(TalendDate.getCurentDate()).
     */
    public static java.util.Date getUTC(java.util.Date siteTimestamp) {
    	return getUTC(java.util.Locale.GERMANY, siteTimestamp);
    }

    /**
     * getUTC: returns the UTC time.
     * @param local country representing the time zone from source data
     * @param siteTimestamp Timestamp from source data
     * @return Date with UTC time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} Locale(locale) locale: from which country
     * 
     * {param} date(siteTimestamp) siteTimestamp : the date value
     * 
     * {example} getUTC(Locale.SPAIN, TalendDate.getCurentDate()).
     */
    public static java.util.Date getUTC(java.util.Locale locale, java.util.Date siteTimestamp) {
    	if (siteTimestamp != null) {
    		java.util.Calendar c = java.util.Calendar.getInstance(locale);
        	c.setTimeInMillis(siteTimestamp.getTime());
            java.util.TimeZone z = c.getTimeZone();
            int offset = z.getOffset(siteTimestamp.getTime());
            int offsetHrs = offset / 1000 / 60 / 60;
            int offsetMins = offset / 1000 / 60 % 60;
            c.add(java.util.Calendar.HOUR_OF_DAY, (-offsetHrs));
            c.add(java.util.Calendar.MINUTE, (-offsetMins));
            return c.getTime();
    	} else {
    		return null;
    	}
    }
    
    /**
     * addMinutes: returns modified date
     * @param today any day
     * @param MinutesToAdd Minutes to add
     * @return new date_time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} date today: any day.
     * 
     * {param} long day: days to be added. (-1 for yesterday)
     * 
     * {example} addDays(today, daysToAdd).
     */
    public static java.util.Date addMinutes(java.util.Date today, long minutesToAdd) {
    	if (today != null) {
    		return new java.util.Date(today.getTime() + (1000 * 60 * minutesToAdd));
    	} else {
    		return null;
    	}
    }
    /**
     * addDays: returns date of yesterday
     * @param today any day
     * @param daysToAdd difference in days
     * @return Date with UTC time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} date today: any day.
     * 
     * {param} long day: days to be added. (-1 for yesterday)
     * 
     * {example} addDays(today, daysToAdd).
     */
    public static java.util.Date addDays(java.util.Date today, long daysToAdd) {
    	if (today != null) {
    		return new java.util.Date(today.getTime() + (1000 * 60 * 60 * 24 * daysToAdd));
    	} else {
    		return null;
    	}
    }
    
    /**
     * addMonths: returns date + given months
     * @param today any day
     * @param monthsToAdd difference in months
     * @return Date with UTC time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} date today: any day.
     * 
     * {param} int month: months to be added. (-1 for yesterday)
     * 
     * {example} addDays(today, monthsToAdd).
     */
    public static java.util.Date addMonths(java.util.Date today, int monthsToAdd) {
    	if (today != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance();
        	c.setTime(today);
        	c.add(Calendar.MONTH, monthsToAdd);
    		return c.getTime();
    	} else {
    		return null;
    	}
    }
    
    /**
     * returns the younger of both
     * @param date1
     * @param date2
     * @return the younger of both
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} Date date1: any day.
     * 
     * {param} Date date2: any day
     * 
     * {example} getYounger(date1, date2).
     */
    public static java.util.Date getYounger(java.util.Date date1, java.util.Date date2) {
    	if (date1 != null && date2 != null) {
    		if (date1.after(date2)) {
    			return date1;
    		} else {
    			return date2;
    		}
    	} else if (date1 != null) {
    		return date1;
    	} else if (date2 != null) {
    		return date2;
    	} else {
    		return null;
    	}
    }

    /**
     * returns the older of both
     * @param date1
     * @param date2
     * @return the older of both
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} Date date1: any day.
     * 
     * {param} Date date2: any day
     * 
     * {example} getOlder(date1, date2).
     */
    public static java.util.Date getOlder(java.util.Date date1, java.util.Date date2) {
    	if (date1 != null && date2 != null) {
    		if (date1.before(date2)) {
    			return date1;
    		} else {
    			return date2;
    		}
    	} else if (date1 != null) {
    		return date1;
    	} else if (date2 != null) {
    		return date2;
    	} else {
    		return null;
    	}
    }
    
    /**
     * getCurrentDayInyyyyMMdd: returns the current day.
     * 
     * {talendTypes} String
     * 
     * {Category} TimestampUtil
     * 
     * {example} getCurrentDayInyyyyMMdd().
     */
    public static String getCurrentDayInyyyyMMdd() {
    	return new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
    }
    
    /**
     * getCurrentDayInyyyyMMdd: returns the current day.
     * 
     * {talendTypes} String
     * 
     * {Category} TimestampUtil
     * 
     * {param} Date(date) date : date to format
     * 
     * {example} getCurrentDayInyyyyMMdd().
     */
    public static String formatInyyyyMMdd(java.util.Date date) {
    	if (date != null) {
        	return new SimpleDateFormat("yyyyMMdd").format(date);
    	} else {
    		return null;
    	}
    }

    /**
     * formatInyyyyMMddHHmmss: returns the current day.
     * 
     * {talendTypes} String
     * 
     * {Category} TimestampUtil
     * 
     * {param} Date(date) date : date to format
     * 
     * {example} formatInyyyyMMddHHmmss(date).
     */
    public static String formatInyyyyMMddHHmmss(java.util.Date date) {
    	if (date != null) {
        	return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
    	} else {
    		return null;
    	}
    }

    /**
     * format: returns the formatted date as String.
     * 
     * {talendTypes} String
     * 
     * {Category} TimestampUtil
     * 
     * {param} Date(date) date : date to format
     * 
     * {param} String(pattern) pattern : date to format
     * 
     * {example} format(date, "dd.MM.yyyy")
     */
    public static String format(java.util.Date date, String pattern) {
    	if (date != null) {
        	return new SimpleDateFormat(pattern).format(date);
    	} else {
    		return null;
    	}
    }
    
    /**
     * format: returns the formatted date as String.
     * 
     * {talendTypes} String
     * 
     * {Category} TimestampUtil
     * 
     * {param} Date(date) date : date to format
     * 
     * {param} String(pattern) pattern : date to format
     * 
     * {example} formatAsUTC(date, "dd.MM.yyyy")
     */
    public static String formatAsUTC(java.util.Date date, String pattern) {
    	if (date != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    		sdf.setTimeZone(getUTCTimeZone());
        	return sdf.format(date);
    	} else {
    		return null;
    	}
    }

    /**
     * getLocal: returns the local time.
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} string("Europe/Madrid") localTimeZoneId : ID of time zone
     * 
     * {param} date(utcTimestamp) utcTimestamp : the date value
     * 
     * {example} getLocalTimeFromUTC("Europe/Madrid", utcTimestamp).
     */
    public static java.util.Date getLocalTimeFromUTC(String localTimeZoneId, java.util.Date utcTimestamp) {
    	java.util.TimeZone tz = getTimeZone(localTimeZoneId);
    	return getLocalTimeFromUTC(tz, utcTimestamp);
    }

    /**
     * getLocal: returns the local time.
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} string("Europe/Madrid") localTimeZoneId : ID of time zone
     * 
     * {param} date(mezTimestamp) utcTimestamp : the date value
     * 
     * {example} getLocalTimeFromMEZ("Europe/Madrid", mezTimestamp).
     */
    public static java.util.Date getLocalTimeFromMEZ(String localTimeZoneId, java.util.Date mezTimestamp) {
    	java.util.TimeZone tz = getTimeZone(localTimeZoneId);
    	return getLocalTimeFromMEZ(tz, mezTimestamp);
    }

    private static java.util.TimeZone getTimeZone(String idParam) {
		if (idParam == null || idParam.isEmpty()) {
			throw new IllegalArgumentException("id for time zone cannot be null or empty");
		}
		idParam = idParam.trim();
		String[] tzArray = java.util.TimeZone.getAvailableIDs();
		for (String id : tzArray) {
			if (id.equalsIgnoreCase(idParam)) {
				return java.util.TimeZone.getTimeZone(id);
			}
		}
		return null;
	}

    private static java.util.TimeZone utcTimeZone = null;
    private static java.util.TimeZone mezTimeZone = null;

    private static java.util.TimeZone getUTCTimeZone() {
    	if (utcTimeZone == null) {
    		utcTimeZone = java.util.TimeZone.getTimeZone("UTC");
    	}
    	return utcTimeZone;
    }
    
    private static java.util.TimeZone getMEZTimeZone() {
    	if (mezTimeZone == null) {
    		mezTimeZone = java.util.TimeZone.getTimeZone("Europe/Berlin");
    	}
    	return mezTimeZone;
    }

    private static java.util.Date getLocalTimeFromUTC(java.util.TimeZone tz, java.util.Date utcTimestamp) {
    	return getLocalTime(tz, utcTimestamp, getUTCTimeZone());
    }

    private static java.util.Date getLocalTimeFromMEZ(java.util.TimeZone tz, java.util.Date mezTimestamp) {
    	return getLocalTime(tz, mezTimestamp, getMEZTimeZone());
    }

    private static java.util.Date getLocalTime(java.util.TimeZone localTimeZone, java.util.Date timestamp, java.util.TimeZone sourceTimeZone) {
    	if (timestamp != null) {
    		if (localTimeZone == null) {
    			throw new IllegalArgumentException("localTimeZone cannot be null");
    		}
    		if (sourceTimeZone == null) {
    			throw new IllegalArgumentException("sourceTimeZone cannot be null");
    		}
    		java.util.Calendar c = java.util.Calendar.getInstance();
    		//c.setTimeZone(sourceTimeZone);
    		c.clear();
        	c.setTimeInMillis(timestamp.getTime());
        	// adapt the local time zone
            int offset = localTimeZone.getOffset(timestamp.getTime());
            offset = offset - sourceTimeZone.getOffset(timestamp.getTime());
            int offsetHrs = offset / 1000 / 60 / 60;
            int offsetMins = offset / 1000 / 60 % 60;
            c.add(java.util.Calendar.HOUR_OF_DAY, (offsetHrs));
            c.add(java.util.Calendar.MINUTE, (offsetMins));
            return c.getTime();
    	} else {
    		return null;
    	}
    }

    /**
     * getTimeRatioInRange: returns relative position of a test date in a time range
     * 
     * {talendTypes} double
     * 
     * {Category} TimestampUtil
     * 
     * {param} date(mesaure_date) test : test date
     * 
     * {param} date(max_date) rangeEnd : the end of range
     * 
     * {param} int(7) workDateRange : number of dates for range (regardless of sign)
     * 
     * {example} getTimeRatioInRange(measure_date, max_date, workDateRange).
     */
    public static double getTimeRatioInRange(java.util.Date test, java.util.Date rangeEnd, long workDateRange) {
    	if (test == null || rangeEnd == null) {
    		return 0;
    	}
    	long oneDayInMillis = 24l * 60l * 60l * 1000l;
    	workDateRange = Math.abs(workDateRange);
    	long workDateRangeInMillis = workDateRange * oneDayInMillis;
    	long testDateInMillis = test.getTime();
    	long rangeEndInMillis = rangeEnd.getTime();
    	long diff = rangeEndInMillis - testDateInMillis + oneDayInMillis;
    	if (diff > (oneDayInMillis)) {
    		return (double) workDateRangeInMillis / diff;
    	} else {
    		return workDateRange;
    	}
    }
    
    public static int getDaysBetween(java.util.Date beginDate, java.util.Date endDate ) {
    	long oneDayInMillis = 24 * 60 * 60 * 1000;
    	return (int)((endDate.getTime()-beginDate.getTime())/oneDayInMillis);
    }
 
    /**
     * getSQLForWeekMonday: sql for sunday
     * @param today any day
     * @return Date with no time
     * 
     * {talendTypes} String
     * 
     * {Category} TimestampUtil
     * 
     * {param} string week: German week in yyyy-ww.
     * 
     * {example} getSQLForWeekMonday(week).
     */
	public static String getSQLForWeekMonday(String weekString) throws java.text.ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-ww",new Locale("de","DE")); // we use DIN 1355/ISO 8601 weeks
		java.util.Date startDate = getLastMonday(sdf.parse(weekString));
		SimpleDateFormat sdfOutput = new SimpleDateFormat("dd.MM.yyyy");
		return "to_date('" + sdfOutput.format(startDate) + "','DD.MM.YYYY')";
	}
	
    /**
     * getSQLForWeekSunday: sql for sunday
     * @param today any day
     * @return Date with no time
     * 
     * {talendTypes} String
     * 
     * {Category} TimestampUtil
     * 
     * {param} string week: week in yyyy-ww.
     * 
     * {example} getSQLForWeekSunday(week).
     */
	public static String getSQLForWeekSunday(String weekString) throws java.text.ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-ww",new Locale("de","DE"));// we use DIN 1355/ISO 8601 weeks
		java.util.Date startDate = getNextSunday(sdf.parse(weekString));
		SimpleDateFormat sdfOutput = new SimpleDateFormat("dd.MM.yyyy");
		return "to_date('" + sdfOutput.format(startDate) + "','DD.MM.YYYY')";
	}

    /**
     * getLastMonday: returns date + given months
     * @param today any day
     * @return Date with no time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} date today: any day.
     * 
     * {example} getLastMonday(today).
     */
	public static java.util.Date getLastMonday(java.util.Date today) {
    	if (today != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(Locale.GERMANY);
        	c.setTime(today);
        	c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        	c.set(java.util.Calendar.MINUTE, 0);
        	c.set(java.util.Calendar.SECOND, 0);
        	c.set(java.util.Calendar.MILLISECOND, 0);
        	c.set(java.util.Calendar.HOUR_OF_DAY, 0);
    		return c.getTime();
    	} else {
    		return null;
    	}
    }
    
    /**
     * getToday: returns today
     * @return Date with no time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {example} getToday().
     */
	public static java.util.Date getToday() {
		return truncateToDay(new Date());
    }

    /**
     * getYesterday: returns today
     * @return Date with no time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {example} getYesterday().
     */
	public static java.util.Date getYesterday() {
		return truncateToDay(addDays(new Date(), -1));
    }

	/**
     * getLastWorkday: returns the last work day
     * @return last work date without time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {example} getLastWorkday().
     */
	public static java.util.Date getLastWorkday() {
		return getLastWorkday(new Date());
	}
	
	/**
     * getLastWorkday: returns the last work day
     * @param today any day
     * @return work date without time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} date today: any day.
     * 
     * {example} getLastWorkday(today).
     */
	public static java.util.Date getLastWorkday(java.util.Date today) {
    	if (today != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(Locale.GERMANY);
        	c.setTime(today);
    		c.add(Calendar.DAY_OF_YEAR, -1);
        	int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        	while (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
        		c.add(Calendar.DAY_OF_YEAR, -1);
        		dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        	}
        	c.set(java.util.Calendar.MINUTE, 0);
        	c.set(java.util.Calendar.SECOND, 0);
        	c.set(java.util.Calendar.MILLISECOND, 0);
        	c.set(java.util.Calendar.HOUR_OF_DAY, 0);
    		return c.getTime();
    	} else {
    		return null;
    	}
    }

	/**
     * getYear: returns the year of the date
     * @param today any day
     * @return year
     * 
     * {talendTypes} Integer
     * 
     * {Category} TimestampUtil
     * 
     * {param} date today: any day.
     * 
     * {example} getYear(today).
     */
	public static Integer getYear(java.util.Date today) {
    	if (today != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(Locale.GERMANY);
        	c.setTime(today);
    		return c.get(Calendar.YEAR);
    	} else {
    		return null;
    	}
    }

	/**
     * getWeek: returns the week of the date
     * @param today any day
     * @return year
     * 
     * {talendTypes} Integer
     * 
     * {Category} TimestampUtil
     * 
     * {param} date today: any day.
     * 
     * {example} getWeek(today).
     */
	public static Integer getWeek(java.util.Date today) {
    	if (today != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(Locale.GERMANY);
        	c.setTime(today);
    		return c.get(Calendar.WEEK_OF_YEAR);
    	} else {
    		return null;
    	}
    }

	/**
     * getMonth: returns the year of the date
     * @param today any day
     * @return year
     * 
     * {talendTypes} Integer
     * 
     * {Category} TimestampUtil
     * 
     * {param} date today: any day.
     * 
     * {example} getMonth(today).
     */
	public static Integer getMonth(java.util.Date today) {
    	if (today != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(Locale.GERMANY);
        	c.setTime(today);
    		return c.get(Calendar.MONTH);
    	} else {
    		return null;
    	}
    }

	public static java.util.Date getNextSunday(java.util.Date today) {
    	if (today != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(Locale.GERMANY);
        	c.setTime(today);
        	c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        	c.set(java.util.Calendar.MINUTE, 0);
        	c.set(java.util.Calendar.SECOND, 0);
        	c.set(java.util.Calendar.MILLISECOND, 0);
        	c.set(java.util.Calendar.HOUR_OF_DAY, 0);
    		return c.getTime();
    	} else {
    		return null;
    	}
    }

    /**
     * getLastWeekDay: returns the last date which belongs to the given week day
     * @param today any day
     * @return week date without time 
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} date today: any day.
     * 
     * {param} int weekDayIndex: use constants Calendar.MONDAY ... Calendar.SUNDAY
     * 
     * {example} getLastWeekDay(today).
     */
	public static java.util.Date getLastWeekDay(java.util.Date today, int weekDayIndex) {
    	if (today != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(Locale.GERMANY);
        	c.setTime(today);
        	if (weekDayIndex > c.get(Calendar.DAY_OF_WEEK)) {
        		c.add(Calendar.WEEK_OF_YEAR, -1);
        	}
        	c.set(Calendar.DAY_OF_WEEK, weekDayIndex);
        	c.set(java.util.Calendar.MINUTE, 0);
        	c.set(java.util.Calendar.SECOND, 0);
        	c.set(java.util.Calendar.MILLISECOND, 0);
        	c.set(java.util.Calendar.HOUR_OF_DAY, 0);
    		return c.getTime();
    	} else {
    		return null;
    	}
    }
	
	/**
     * returns the first not empty date
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} date(date1,date2) dates: Date.
     * 
     * {example} coalesce(date1,date2) # "test"
     */
	public static java.util.Date coalesce(java.util.Date ...dates) {
		for (java.util.Date d : dates) {
			if (d != null) 
				return d;			
		}
		return null;
	}
	
	/**
     * returns coalesce(updatedAt, createdAt, current date)
     * @param createdAt created at date
     * @param updatedAt updated at date
     * @return coalesce(updatedAt, createdAt, current date)
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Date(createdAt) createdAt: Date.
     * {param} Date(updatedAt) updatedAt: Date.
     * 
     * {example} getLastModified(createdAt,updatedAt) # date
     */
	public static java.util.Date getLastModified(java.util.Date createdAt, java.util.Date updatedAt) {
		java.util.Calendar c = Calendar.getInstance(Locale.GERMAN);
		if (updatedAt != null) {
			c.setTime(updatedAt);
			c.add(Calendar.MINUTE, 1);
		} else if (createdAt != null) {
			c.setTime(createdAt);
			c.add(Calendar.MINUTE, 1);
		}
		return c.getTime();
	}
	
	/**
     * Checks the given date if it belongs to today
     * @param anyDate any Date to check
     * @return true if anyDate belongs to today 
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Date(anyDate) anyDate: Date.
     * 
     * {example} isCurrentDay(anyDate) # true if today
     */
	public static boolean isCurrentDay(java.util.Date anyDate) {
		java.util.Calendar c = Calendar.getInstance();
		c.setTime(anyDate);
		java.util.Calendar now = Calendar.getInstance();
		// same day and year?
		return (c.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) && (c.get(Calendar.YEAR) == now.get(Calendar.YEAR));
	}
	
    /**
     * daysBetween: returns the desired weekday of the previous week 
     * @param from
     * @param to
     * @return count days between from and to
     * 
     * {talendTypes} Integer
     * 
     * {Category} TimestampUtil
     * 
     * {param} date from: start date.
     * 
     * {param} date to: end date
     * 
     * {example} daysBetween(from, to).
     */
	public static Integer daysBetween(java.util.Date from, java.util.Date to) {
		if (from == null || to == null) {
			return null;
		}
		long fromMillis = from.getTime();
		long toMillis = to.getTime();
		long diffMillis = toMillis - fromMillis;
		long days = diffMillis / 1000 / 60 / 60 / 24;
		return Integer.valueOf((int) days);
	}

    /**
     * getFirstDayOfMonth: returns the first day of the given dates month
     * @param date any day
     * @return Date with no time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} Date date: any day.
     * 
     * {example} getFirstDayOfMonth(date).
     */
    public static java.util.Date getFirstDayOfMonth(java.util.Date date) {
    	date= truncateToDay(date);
    	if (date != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(Locale.GERMANY);
        	c.setTime(date);
        	c.set(Calendar.DAY_OF_MONTH, 1);        	
    		return c.getTime();
    	} else {
    		return null;
    	}
    }

    /**
     * getLastDayOfMonth: returns the last day of the given dates month
     * @param date any day
     * @return Date with no time
     * 
     * {talendTypes} Date
     * 
     * {Category} TimestampUtil
     * 
     * {param} Date date: any day.
     * 
     * {example} getLastDayOfMonth(date).
     */
    public static java.util.Date getLastDayOfMonth(java.util.Date date) {
    	date = addMonths(date, 1);
    	return addDays(getFirstDayOfMonth(date), -1);    	
    }

	/**
     * areInSameMonth: returns whether the given dates are in the same month 
     * @param date1
     * @param date2
     * @return true, false or null if at least one of the given dates is null
     * 
     * {talendTypes} Boolean
     * 
     * {Category} TimestampUtil
     * 
     * {param} date date1: date1.
     * 
     * {param} date date2: date2
     * 
     * {example} areInSameMonth(date1, date2).
     */
	public static Boolean areInSameMonth(java.util.Date date1, java.util.Date date2){
		if (date1 == null || date2 == null) {
			return null;
		}
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.setTime(date1);
		c2.setTime(date2);
		
		return 
			c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && 
			c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
	}

    /**
     * isGivenDayOfMonth: returns whether the given date is the given day of month.
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} boolean
     * 
     * {param} Date(date) date: any date.
     * {param} Integer(1) dayOfMonth: day index of month
     * 
     * {example} isGivenDayOfMonth(TalendDate.getCurentDate(), 1).
     */
    public static boolean isGivenDayOfMonth(java.util.Date date, int dayOfMonth) {
    	java.util.Calendar c = java.util.Calendar.getInstance();
    	c.setTime(date);
    	return (c.get(java.util.Calendar.DAY_OF_MONTH) == dayOfMonth);    	
    }

	/**
     * The between method in Java
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Boolean
     * 
     * {param} Date(test) test: Date.
     * {param} Date(start) start: Date.
     * {param} Date(end) end: Date.
     * 
     * {example} between(test,start,end) # true
     */
	public static boolean between(java.util.Date test, java.util.Date start, java.util.Date end) {
		if (test == null) {
			return false;
		}
		if (start.equals(test)) {
			return true;
		} else if (end.equals(test)) {
			return true;
		} else if (start.before(test) && end.after(test)) {
			return true;
		} else {
			return false;
		}
	}
	
    /**
     * getDateAsId: returns the current day as integer
     * 
     * {talendTypes} String
     * 
     * {Category} TimestampUtil
     * 
     * {param} Date(date) date : date to format
     * 
     * {example} getDateAsInt(date) # 20150403
     */
    public static Integer getDateAsInt(java.util.Date date) {
    	if (date != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance();
        	c.setTime(date);
        	// cut time
        	c.set(java.util.Calendar.MINUTE, 0);
        	c.set(java.util.Calendar.SECOND, 0);
        	c.set(java.util.Calendar.MILLISECOND, 0);
        	c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        	int id = c.get(Calendar.YEAR) * 10000;
        	id = id + (c.get(Calendar.MONTH) + 1) * 100;
        	id = id + c.get(Calendar.DAY_OF_MONTH);
        	return id;
    	} else {
    		return null;
    	}
    }

    /**
     * getIntAsDate: returns the integer as date
     * 
     * {talendTypes} String
     * 
     * {Category} TimestampUtil
     * 
     * {param} Integer(date) date : date to format
     * 
     * {example} getIntAsDate(date) # 20150403
     */
    public static java.util.Date getIntAsDate(Integer dateAsInt) {
    	if (dateAsInt != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance();
        	// cut time
        	c.set(java.util.Calendar.MINUTE, 0);
        	c.set(java.util.Calendar.SECOND, 0);
        	c.set(java.util.Calendar.MILLISECOND, 0);
        	c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        	int year = dateAsInt / 10000;
        	int monthDay = (dateAsInt - (year * 10000));
        	int month = monthDay / 100;
        	int day = monthDay % 100;
        	c.set(Calendar.YEAR, year);
        	c.set(Calendar.MONTH, month - 1);
        	c.set(Calendar.DAY_OF_MONTH, day);
        	return c.getTime();
    	} else {
    		return null;
    	}
    }

    /**
     * addDays: returns the integer as date
     * 
     * {talendTypes} String
     * 
     * {Category} TimestampUtil
     * 
     * {param} Integer(dateAsInt) dateAsInt : 
     * {param} Integer(daysToAdd) daysToAdd : 
     * 
     * {example} addDays(dateAsInt, daysToAdd) # 20150403
     */
    public static Integer addDays(Integer dateAsInt, Integer daysToAdd) {
    	if (dateAsInt != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance();
        	// cut time
        	c.set(java.util.Calendar.MINUTE, 0);
        	c.set(java.util.Calendar.SECOND, 0);
        	c.set(java.util.Calendar.MILLISECOND, 0);
        	c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        	int year = dateAsInt / 10000;
        	int monthDay = (dateAsInt - (year * 10000));
        	int month = monthDay / 100;
        	int day = monthDay % 100;
        	c.set(Calendar.YEAR, year);
        	c.set(Calendar.MONTH, month - 1);
        	c.set(Calendar.DAY_OF_MONTH, day);
        	c.add(Calendar.DAY_OF_YEAR, daysToAdd);
        	return getDateAsInt(c.getTime());
    	} else {
    		return null;
    	}
    }

    /**
     * getTimeDateByMinute: converts the minutes to an Date object
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Integer(5): The timestamp within a timeframe.
     * 
     * {example} getTimeDateByMinute(5).
     */
    public static java.util.Date getTimeDateByMinute(Integer minutes) {
    	if (minutes != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(getUTCTimeZone());
        	c.setTime(new java.util.Date(0l));
        	// cut minutes and seconds
        	c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        	c.set(java.util.Calendar.MINUTE, 0);
        	c.set(java.util.Calendar.SECOND, 0);
        	c.add(java.util.Calendar.MINUTE, minutes);
        	c.set(java.util.Calendar.MILLISECOND, 0);
        	return c.getTime();
    	} else {
    		return null;
    	}
    }

    /**
     * getTimeDateByMinuteAndSeconds: converts the minutes to an Date object
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {param} Integer(5): minutes
     * {param} Integer(5): seconds
     * 
     * {example} getTimeDateByMinuteAndSeconds(90,120).
     */
    public static java.util.Date getTimeDateByMinuteAndSeconds(Integer minutes, Integer seconds) {
    	if (minutes != null || seconds != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(getUTCTimeZone());
        	c.setTime(new java.util.Date(0l));
        	// cut minutes and seconds
        	c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        	c.set(java.util.Calendar.MINUTE, 0);
        	c.set(java.util.Calendar.SECOND, 0);
        	if (minutes != null) {
            	c.add(java.util.Calendar.MINUTE, minutes);
        	}
        	if (seconds != null) {
            	c.add(java.util.Calendar.SECOND, seconds);
        	}
        	c.set(java.util.Calendar.MILLISECOND, 0);
        	return c.getTime();
    	} else {
    		return null;
    	}
    }
    
    /**
     * Returns the seconds of the time without date
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Long
     * 
     * {param} Date(today): The timestamp within a timeframe.
     * 
     * {example} getTimeAsSeconds(new Date()).
     */
    public static Long getTimeAsSeconds(Date dateTime) {
    	if (dateTime != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(Locale.GERMAN);
        	c.setTime(dateTime);
        	c.setTimeZone(getUTCTimeZone());
        	// cut minutes and seconds
        	c.set(java.util.Calendar.YEAR, 1970);
        	c.set(java.util.Calendar.DAY_OF_YEAR, 1);
        	c.set(java.util.Calendar.MILLISECOND, 0);
        	return c.getTime().getTime() / 1000l;
    	} else {
    		return null;
    	}
    }

    /**
     * Returns the seconds of the time without date
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Long
     * 
     * {param} Date(today): The timestamp within a timeframe.
     * 
     * {example} getTimeAsMilliSeconds(new Date()).
     */
    public static Long getTimeAsMilliSeconds(Date dateTime) {
    	if (dateTime != null) {
        	java.util.Calendar c = java.util.Calendar.getInstance(Locale.GERMAN);
        	c.setTime(dateTime);
        	c.setTimeZone(getUTCTimeZone());
        	// cut minutes and seconds
        	c.set(java.util.Calendar.YEAR, 1970);
        	c.set(java.util.Calendar.DAY_OF_YEAR, 1);
        	return c.getTime().getTime();
    	} else {
    		return null;
    	}
    }

    private static Date scdEndDate = null;
    /**
     * Returns the SCD end date: 2999-01-01
     * 
     * {Category} TimestampUtil
     * 
     * {talendTypes} Date
     * 
     * {example} getSCDEndDate().
     */
    public static Date getSCDEndDate() {
    	if (scdEndDate == null) {
    		scdEndDate = getIntAsDate(29990101);
    	}
    	return scdEndDate;
    }

}
