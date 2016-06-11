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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This is a helper class to detect date ranges and combine them 
 * to new records containing the min and max value of the range.
 * 
 * This is not a typical routine, this class needs to be instantiated
 * within a tJavaFlex.
 * 
 *   
 * @author jan.lolling@cimt-ag.de
 *
 */
public class DateRangeBuilder {

	private Date lastValue = null;
	private Date lastMinValue = null;
	private Date lastMaxValue = null;
	private int currentCount = 1;
	private int count = 0;
	private Date maxValue = null;
	private Date minValue = null;
	private boolean nullCuts = true;
	private boolean valueBeforeCuts = true;
	private Object lastResetValue = null;
	private Object prevResetValue = null;
	private boolean reset = false;
	private int maxDateDiff = 10;
	private int maxAllowedGap = 3;
	
	public Object checkReset(Object test) {
		reset = false;
		if (test != null) {
			if (lastResetValue != null) {
				if (test.equals(lastResetValue) == false) {
					prevResetValue = lastResetValue;
					reset = true;
				}
			}
		} else if (lastResetValue != null) {
			reset = true;
		}
		lastResetValue = test;
		if (prevResetValue == null) {
			prevResetValue = lastResetValue;
		}
		return prevResetValue;
	}
	
	public boolean addValue(Date value) {
		if (value == null) {
			if (nullCuts && lastValue != null) {
				minValue = lastMinValue;
				maxValue = lastMaxValue;
				count = currentCount;
				lastValue = null;
				lastMaxValue = null;
				lastMinValue = null;
				currentCount = 1;
				prevResetValue = null;
				return true;
			} else {
				return false;
			}
		}
		if (lastValue == null) {
			lastValue = value;
			if (lastMaxValue == null || before(lastMaxValue, value)) {
				lastMaxValue = value;
			}
			if (lastMinValue == null || after(lastMinValue, value)) {
				lastMinValue = value;
			}
			return false;
		}
		int diff = getDifferenceToLast(value);
		if (diff > 0 && diff <= maxAllowedGap) {
			// we are got the next day
			if (reset || currentCount == maxDateDiff) {
				minValue = lastMinValue;
				maxValue = lastMaxValue;
				count = currentCount;
				lastMinValue = value;
				lastMaxValue = value;
				lastValue = value;
				currentCount = 1;
				prevResetValue = null;
				return true;
			} else {
				if (lastMaxValue == null || before(lastMaxValue, value)) {
					lastMaxValue = value;
				}
				if (lastMinValue == null || after(lastMinValue, value)) {
					lastMinValue = value;
				}
				lastValue = value;
				currentCount = currentCount + diff;
				return false;
			}
		} else if (diff > maxAllowedGap) {
			minValue = lastMinValue;
			maxValue = lastMaxValue;
			count = currentCount;
			lastMinValue = value;
			lastMaxValue = value;
			lastValue = value;
			currentCount = 1;
			prevResetValue = null;
			return true;
		} else if (diff < 0) {
			if (valueBeforeCuts) {
				minValue = lastMinValue;
				maxValue = lastMaxValue;
				count = currentCount;
				lastMinValue = value;
				lastMaxValue = value;
				lastValue = value;
				currentCount = 1;
				prevResetValue = null;
				return true;
			} else {
				throw new IllegalArgumentException("The current value:" + value + " is before the last value:" + lastValue);
			}
		} else {
			// diff == 0
			if (reset) {
				minValue = lastMinValue;
				maxValue = lastMaxValue;
				count = currentCount;
				lastMinValue = value;
				lastMaxValue = value;
				lastValue = value;
				currentCount = 1;
				prevResetValue = null;
				return true;
			} else {
				lastValue = value;
				if (lastMaxValue == null || before(lastMaxValue, value)) {
					lastMaxValue = value;
				}
				if (lastMinValue == null || after(lastMinValue, value)) {
					lastMinValue = value;
				}
				return false;
			}
		}
	}
	
	private boolean before(Date value1, Date value2) {
		return value1.before(value2);
	}
	
	private boolean after(Date value1, Date value2) {
		return value1.after(value2);
	}

	private int getDifferenceToLast(Date value) {
		Calendar cal1 = Calendar.getInstance(Locale.GERMAN);
		cal1.setTime(lastValue);
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		Calendar cal2 = Calendar.getInstance(Locale.GERMAN);
		cal2.setTime(value);
		cal2.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		if (cal1.equals(cal2)) {
			return 0;
		} else {
	    	long oneDayInMillis = 24 * 60 * 60 * 1000;
	    	return (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / oneDayInMillis);
		}
	}

	public Date getMinValue() {
		return minValue;
	}

	public Date getMaxValue() {
		return maxValue;
	}

	public int getCount() {
		return count;
	}

	public boolean isNullCuts() {
		return nullCuts;
	}

	public void setNullCuts(boolean nullCuts) {
		this.nullCuts = nullCuts;
	}

	public boolean isValueBeforeCuts() {
		return valueBeforeCuts;
	}

	public void setValueBeforeCuts(boolean valueBeforeCuts) {
		this.valueBeforeCuts = valueBeforeCuts;
	}

	public int getMaxDateDiff() {
		return maxDateDiff;
	}

	public void setMaxDateDiff(Integer maxDateDiff) {
		if (maxDateDiff != null && maxDateDiff > 0) {
			this.maxDateDiff = maxDateDiff;
		}
	}

	public int getMaxAllowedGap() {
		return maxAllowedGap;
	}

	public void setMaxAllowedGap(Integer maxAllowedGap) {
		if (maxAllowedGap != null && maxAllowedGap > 0) {
			this.maxAllowedGap = maxAllowedGap + 1;
		}
	}
	
}
