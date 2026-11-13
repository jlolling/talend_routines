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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

/*
 * user specification: the function's comment should contain keys as follows: 1. write about the function's comment.but
 * it must be before the "{talendTypes}" key.
 * 
 * 2. {talendTypes} 's value must be talend Type, it is required . its value should be one of: String, char | Character,
 * long | Long, int | Integer, boolean | Boolean, byte | Byte, Date, double | Double, float | Float, Object, short |
 * Short
 * 
 * 3. {Category} define a category for the Function. it is required. its value is user-defined .
 * 
 * 4. {param} 's format is: {param} <type>[(<default value or closed list values>)] <name>[ : <comment>]
 * 
 * <type> 's value should be one of: string, int, list, double, object, boolean, long, char, date. <name>'s value is the
 * Function's parameter name. the {param} is optional. so if you the Function without the parameters. the {param} don't
 * added. you can have many parameters for the Function.
 * 
 * 5. {example} gives a example for the Function. it is optional.
 */
public class NumberUtil {

	
	 /**
     * returns the first not null Integer
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} Integer
     * 
     * {param} Interer(null,1,2) intnumbers: Integer.
     * 
     * {example} coalesce(null,13,null,2) # 13
     */
	public static Integer coalesce(Integer ...intnumbers) {
		for (Integer i : intnumbers) {
			if (i != null) {
				return i;
			}
		}
		return null;
	}
	
	
    /**
     * return integer and accepts Integer and String. Null returns 0
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} long | Long
     * 
     * {param} Long(123) input: long format.
     * 
     * {example} getNullSaveLong("123") result: 123 ...
     * 
     */
	public static long getNullSaveLong(Object input) {
		if (input instanceof Number) {
			return ((Number) input).longValue();
		} else if (input instanceof String) {
			String s = (String) input;
			if (s == null || s.isEmpty()) {
				return 0;
			}
			return Long.parseLong(s);
		} else {
			return 0;
		}
	}

    /**
     * return Long and accepts Integer and String. Null returns null if input null of not a number
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} long | Long
     * 
     * {param} Long(123) input: long format.
     * 
     * {example} getFailSaveLong("123") result: 123 ...
     * 
     */
	public static Long getFailSaveLong(Object input) {
		if (input instanceof Number) {
			return ((Number) input).longValue();
		} else if (input instanceof String) {
			String s = (String) input;
			if (s == null || s.isEmpty()) {
				return null;
			}
			Long v = null;
			try {
				v = Long.parseLong(s);
			} catch (Exception e) {}
			return v;
		} else {
			return null;
		}
	}

	/**
     * return integer and accepts Integer and String. Null returns 0
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} Integer(123) input: float pointing format.
     * 
     * {example} getNullSaveInt("123") result: 123 ...
     * 
     */
	public static int getNullSaveInt(Object input) {
		if (input instanceof Number) {
			return ((Number) input).intValue();
		} else if (input instanceof String) {
			String s = (String) input;
			if (s == null || s.isEmpty()) {
				return 0;
			}
			return Integer.parseInt(s);
		} else {
			return 0;
		}
	}

	/**
     * return integer and accepts Numbers or String.
     * If the value is not a number, it returns null or 0
     * @param input (input value
     * @param notNull if true: if null or not a number, it returns 0
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} Object(123) input: Number or String
     * {param} Boolean(notNull)
     * 
     * {example} getFailSaveInt("123", false) result: 123 ...
     * 
     */
	public static Integer getFailSaveInt(Object input, boolean notNull) {
		if (input instanceof Number) {
			return ((Number) input).intValue();
		} else if (input instanceof String) {
			String s = (String) input;
			if (s.trim().isEmpty()) {
				if (notNull) {
					return 0;
				} else {
					return null;
				}
			}
			try {
				return Integer.parseInt(s.trim());
			} catch (Exception e) {
				if (notNull) {
					return 0;
				} else {
					return null;
				}
			}
		} else if (notNull) {
			return 0;
		} else {
			return null;
		}
	}

	/**
     * return Long and accepts Numbers or String.
     * If the value is not a number, it returns null or 0
     * @param input (input value
     * @param notNull if true: if null or not a number, it returns 0
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} long | Long
     * 
     * {param} Object(123) input: Number or String
     * {param} Boolean(notNull)
     * 
     * {example} getFailSaveLong("123", false) result: 123 ...
     * 
     */
	public static Long getFailSaveLong(Object input, boolean notNull) {
		if (input instanceof Number) {
			return ((Number) input).longValue();
		} else if (input instanceof String) {
			String s = (String) input;
			if (s.trim().isEmpty()) {
				if (notNull) {
					return 0l;
				} else {
					return null;
				}
			}
			try {
				return Long.parseLong(s.trim());
			} catch (Exception e) {
				if (notNull) {
					return 0l;
				} else {
					return null;
				}
			}
		} else if (notNull) {
			return 0l;
		} else {
			return null;
		}
	}

	/**
     * return integer and accepts Integer and String. Null returns 0
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} Integer(123) input: float pointing format.
     * 
     * {example} getNullSaveInt("123", "replacements) result: 123 ...
     * 
     */
	public static int getNullSaveInt(Object input, String ...toRemoveString) {
		if (input instanceof Number) {
			return ((Number) input).intValue();
		} else if (input instanceof String) {
			String s = (String) input;
			if (toRemoveString != null) {
				for (String r : toRemoveString) {
					s = s.replace(r, "");
				}
			}
			if (s == null || s.isEmpty()) {
				return 0;
			}
			return Integer.parseInt(s);
		} else {
			return 0;
		}
	}

    /**
     * return integer and accepts Integer and String. Null returns null
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} Integer(123) input: float pointing format.
     * 
     * {example} getFailSaveInt("123", "replacements) result: 123 ...
     * 
     */
	public static Integer getFailSaveInt(Object input, String ...toRemoveString) {
		if (input instanceof Number) {
			return ((Number) input).intValue();
		} else if (input instanceof String) {
			String s = (String) input;
			if (toRemoveString != null) {
				for (String r : toRemoveString) {
					s = s.replace(r, "");
				}
			}
			if (s == null || s.isEmpty()) {
				return null;
			}
			try {
				return Integer.parseInt(s);
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

    /**
     * return integer and accepts Integer and String. Null returns null
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} Double(123) input: float pointing format.
     * 
     * {example} getFailSaveDouble("123", "replacements) result: 123 ...
     * 
     */
	public static Double getFailSaveDouble(Object input, String ...toRemoveString) {
		if (input instanceof Number) {
			return ((Number) input).doubleValue();
		} else if (input instanceof String) {
			String s = (String) input;
			if (toRemoveString != null) {
				for (String r : toRemoveString) {
					s = s.replace(r, "");
				}
			}
			if (s == null || s.isEmpty()) {
				return null;
			}
			try {
				return Double.parseDouble(s);
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
     * return Double and accepts Numbers or String.
     * If the value is not a number, it returns null or 0
     * @param input value
     * @param notNull if true: if null or not a number, it returns 0
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} Object(123) input: Number or String
     * {param} Boolean(notNull)
     * 
     * {example} getFailSaveDouble("123", false) result: 123 ...
     * 
     */
	public static Double getFailSaveDouble(Object input, boolean notNull) {
		if (input instanceof Number) {
			return ((Number) input).doubleValue();
		} else if (input instanceof String) {
			String s = (String) input;
			if (s.trim().isEmpty()) {
				if (notNull) {
					return 0d;
				} else {
					return null;
				}
			}
			try {
				return Double.parseDouble(s.trim());
			} catch (Exception e) {
				if (notNull) {
					return 0d;
				} else {
					return null;
				}
			}
		} else if (notNull) {
			return 0d;
		} else {
			return null;
		}
	}

	/**
     * return double and accepts Double and String. Null returns 0
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} Double(123) input: float pointing format.
     * 
     * {example} getNullSaveDouble("123") result: 123 ...
     * 
     */
	public static double getNullSaveDouble(String input) {
		return getNullSaveDouble(input, Locale.US);
	}

    /**
     * return double and accepts Double and String. Null returns 0
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} Double(123) input: float pointing format.
     * 
     * {param} Object(java.util.Local) locale: Locale.
     * 
     * {example} getNullSaveDouble("123") result: 123 ...
     * 
     */
	public static double getNullSaveDouble(String input, Locale locale) {
		if (input != null && input.isEmpty() == false) {
			input = input.replace("%", "").replace('"', ' ').trim();
			if (input == null || input.isEmpty()) {
				return 0d;
			}
			if (locale == null) {
				locale = Locale.ENGLISH;
			}
			NumberFormat nf = NumberFormat.getInstance(locale);
			Number n = null;
			if (input.endsWith("-")) {
				input = "-" + input.substring(0, input.length() - 1);
			}
			try {
				n = nf.parse(input);
			} catch (Exception e) {
				return 0d;
			}
			return n.doubleValue();
		} else {
			return 0;
		}
	}

	public static double getNullSaveDouble(Number input) {
		if (input != null) {
			return input.doubleValue();
		} else {
			return 0;
		}
	}

	/**
     * return double and accepts Double and String. Null returns 0
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} Double(123) input: float pointing format.
     * 
     * {example} getNullSaveDouble("123", "toRemoveString) result: 123 ...
     * 
     */
	public static double getNullSaveDouble(String input, String ...toRemoveString) {
		return getNullSaveDoubleLocal(input, Locale.US, toRemoveString);
	}	
	
	/**
     * return double and accepts Double and String. Null returns 0
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} Double(123) input: float pointing format.
     * 
     * {param} Object(java.util.Local) locale: Locale.
     * 
     * {example} getNullSaveDoubleLocal("123", "toRemoveString) result: 123 ...
     * 
     */
	public static double getNullSaveDoubleLocal(String input, Locale locale, String ...toRemoveString) {
		if (input != null && input.isEmpty() == false) {
			String s = input;
			if (toRemoveString != null) {
				for (String r : toRemoveString) {
					s = s.replace(r, "");
				}
			}
			return getNullSaveDouble(s, locale);
		} else {
			return 0;
		}
	}
	
	/**
     * return true if the first parameter is in the list of the following parameters
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} boolean | Boolean
     * 
     * {param} int(99) test: int test value.
     * 
     * {param} int(2,5,99,120) possibleValues: one or more int.
     * 
     * {example} in(25,2,4,25,66) result: true
     * 
     */
	public static boolean in(Integer test, int ... possibleValues) {
		if (test != null) {
			for (int v : possibleValues) {
				if (test.intValue() == v) {
					return true;
				}
			}
		}
		return false;
	}

	/**
     * build the sum of given Integers regardless of null
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} int(2,5,99,120) integers: integers to sum.
     * 
     * {example} sum(25,2,4,25,66) result: 8889
     * 
     */
	public static int sum(Integer ...integers) {
		int sum = 0;
		for (Integer i : integers) {
			if (i != null) {
				sum = sum + i.intValue();
			}
		}
		return sum;
	}
	
	/**
     * build the division of given Doubles regardless of null
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} double(2.5) dividend: dividend
     * 
     * {param} double(4.3) quotient: quotient
     * 
     * {example} div(2,4) result: 0.5
     * 
     */
	public static double div(Double dividend, Double quotient) {
		if (dividend == null || quotient == null) {
			return 0;
		} else {
			if (quotient.doubleValue() == 0) {
				return 0;
			} else {
				return dividend.doubleValue() / quotient.doubleValue();
			}
		}
	}
	
	/**
     * build the division of given Doubles regardless of null
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} double(2.5) dividend: dividend
     * 
     * {param} int(4.3) quotient: quotient
     * 
     * {example} div(2,4) result: 0.5
     * 
     */
	public static double div(Double dividend, Integer quotient) {
		if (dividend == null || quotient == null) {
			return 0;
		} else {
			if (quotient.doubleValue() == 0) {
				return 0;
			} else {
				return dividend.doubleValue() / quotient.intValue();
			}
		}
	}

	/**
     * build the division of given Doubles regardless of null
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} double(2.5) dividend: dividend
     * 
     * {param} int(4.3) quotient: quotient
     * 
     * {example} div(2,4) result: 0.5
     * 
     */
	public static double div(Integer dividend, Integer quotient) {
		if (dividend == null || quotient == null) {
			return 0;
		} else {
			if (quotient.doubleValue() == 0) {
				return 0;
			} else {
				return (double) dividend.intValue() / quotient.intValue();
			}
		}
	}

	/**
     * build the division of given Doubles regardless of null
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} double(2.5) dividend: dividend
     * 
     * {param} int(4.3) quotient: quotient
     * 
     * {example} div(2,4) result: 0.5
     * 
     */
	public static double div(Integer dividend, Double quotient) {
		if (dividend == null || quotient == null) {
			return 0;
		} else {
			if (quotient.doubleValue() == 0) {
				return 0;
			} else {
				return (dividend.intValue()) / quotient.doubleValue();
			}
		}
	}

	/**
     * formats the number
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} String
     * 
     * {param} double(2.5) number: number to format
     * 
     * {example} numberToPercent2Scale(0.12345) result: 12,34%
     * 
     */
	public static String numberToPercent2Scale(Double number) {
		if (number != null) {
			return numberToString(roundScale2(number * 100d)) + "%";
		} else {
			return "";
		}
	}

	/**
     * formats the number
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} String
     * 
     * {param} double(2.5) number: number to format
     * 
     * {example} numberToString(1234.12345) result: "1.234,12345"
     * 
     */
	public static String numberToString(Number number) {
		if (number != null) {
			NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
			return nf.format(number);
		} else {
			return "";
		}
	}
	
	/**
     * formats the number
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} String
     * 
     * {param} double(2.5) number: number to format
     * {param} boolean(false) german: number to format
     * {param} boolean(false) grouping: number to format
     * {param} int(2) maxfraction: number to format
     * 
     * {example} numberToString(1234.12345,false,false,2) result: "1,234.12345"
     * 
     */
	public static String numberToString(Number number, boolean german, boolean grouping, int maxFractionDigits) {
		if (number != null) {
			NumberFormat nf = NumberFormat.getInstance(german ? Locale.GERMAN : Locale.ENGLISH);
			nf.setGroupingUsed(grouping);
			nf.setMaximumFractionDigits(maxFractionDigits);
			return nf.format(number);
		} else {
			return "";
		}
	}
	
	/**
     * formats the number
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} String
     * 
     * {param} double(2.5) number: number to format
     * 
     * {example} numberToStringRoundScale2(1234.12345) result: "1.234,12"
     * 
     */
	public static String numberToStringRoundScale2(Number number) {
		if (number != null) {
			NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
			return nf.format(roundScale2(number));
		} else {
			return "";
		}
	}

	/**
     * formats the number
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} String
     * 
     * {param} double(2.5) number: number to format
     * 
     * {example} numberToString(1234) result: 1.234
     * 
     */
	public static String numberToString(Integer number) {
		if (number != null) {
			NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
			return nf.format(number);
		} else {
			return "";
		}
	}
	
	/**
     * formats the number
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} String
     * 
     * {param} Integer(1234) number: number to format
     * {param} Integer(6) lengthWithLeadingZeros: length with leading zeors
     * 
     * {example} numberToString(1234, 6) result: 001,234
     * 
     */
	public static String numberToString(Integer number, int lengthWithLeadingZeros) {
		if (number != null) {
			String s = String.valueOf(number);
			String rawString = s.replace(".", "").replace(",", "");
			if (lengthWithLeadingZeros > rawString.length()) {
				StringBuilder sb = new StringBuilder();
				for (int i = rawString.length(); i < lengthWithLeadingZeros; i++) {
					sb.append('0');
				}
				sb.append(s);
				return sb.toString();
			} else {
				return s;
			}
		} else {
			return "";
		}
	}

	/**
     * rounds the number to a value with 2 decimal digits
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} double(2.5) number: number to format
     * 
     * {example} numberToString(1234.12345) result: 1234.12
     * 
     */
	public static Double roundScale2(Number number) {
		if (number != null) {
			return Math.round(number.doubleValue() * 100d) / 100d;
		} else {
			return null;
		}
	}
	
	/**
     * returns a short from byte
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} short | Short
     * 
     * {param} byte(2) number: number to format
     * 
     * {example} getShort(row1.byte) result: 1
     * 
     */
	public static Short getShort(Byte b) {
		if (b == null) {
			return null;
		} else {
			return (short) b;
		}
	}
	
	public static Boolean getBoolean(Short n) {
		if (n != null) {
			return n.shortValue() != 0;
		} else {
			return null;
		}
	}
	
	public static Boolean getBoolean(Integer n) {
		if (n != null) {
			return n.intValue() != 0;
		} else {
			return null;
		}
	}

	public static Boolean getBoolean(Byte n) {
		if (n != null) {
			return n.byteValue() != 0;
		} else {
			return null;
		}
	}
	
	/**
     * checks if a value is a number
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} boolean
     * 
     * {param} Object(any) number: number to format
     * 
     * {example} isNumber(row1.value) result: true
     * 
     */
	public static boolean isNumber(Object o) {
		if (o instanceof Number) {
			return true;
		} else if (o instanceof String) {
			if (((String) o).equalsIgnoreCase("NaN")) {
				return false;
			}
			try {
				Double.parseDouble((String) o);
				return true;
			} catch (Throwable nfe) {
				return false;
			}
		} else {
			return false;
		}
	}

	
	/**
	 * Returns a String representation of a integer
	 * @param value
	 * @param desiredLength
	 * @return
	 * 
     * {Category} NumberUtil
     * 
     * {talendTypes} boolean
     * 
     * {param} Integer(value) number: number to format
     * 
     * {example} getNumberString(123,2)
	 */
	public static String getNumberString(Integer value, int desiredLength) {
		StringBuilder sb = new StringBuilder();
		sb.append(Integer.toString(value));
		if (sb.length() < desiredLength) {
			int zerosToAdd = desiredLength - sb.length();
			if (zerosToAdd > 0) {
				for (int i = 0; i < zerosToAdd; i++) {
					sb.insert(0, '0');
				}
			}
		}
		return sb.toString();
	}
	
	private static Map<String, Integer> sequence = new HashMap<String, Integer>();
	
	/**
     * Get the next rolling sequence value
     * If the value reaches the max value, the sequence resets it self
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} int
     * 
     * {param} String("seq1") sequenceName: Name of the sequence
     * {param} int(20) max: Maximal value
     * 
     * {example} getNextRollingSequenceValue("seq1", 20) result: current value
     * 
     */
	public static int getNextRollingSequenceValue(String sequenceName, int max) {
		if (sequenceName == null) {
			throw new IllegalArgumentException("Name of the sequence cannot be null");
		}
		Integer value = null;
		synchronized (sequence) {
			value = sequence.get(sequenceName);
			if (value == null) {
				value = Integer.valueOf(0);
			} else {
				if (value < max) {
					value = value + 1;
				} else {
					value = Integer.valueOf(0);
				}
			}
			sequence.put(sequenceName, value);
		}
		return value.intValue();
	}
	
	/**
     * compares 2 numbers
     * @param n1 number 1
     * @param n2 number 2
     * @return true if equals
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} int
     * 
     * {param} Number(10)
     * {param} Number(20) 
     * 
     * {example} equals(10, 20) result: current value
     * 
     */
	public static boolean equals(Number n1, Number n2) {
		long l1 = 0l;
		double d1 = 0d;
		BigDecimal b1 = new BigDecimal("0");
		long l2 = 0l;
		double d2 = 0d;
		BigDecimal b2 = new BigDecimal("0");
		if (n1 != null && n2 != null) {
			l1 = n1.longValue();
			d1 = n1.doubleValue();
			b1 = new BigDecimal(n1.toString());
			l2 = n2.longValue();
			d2 = n2.doubleValue();
			b2 = new BigDecimal(n2.toString());
			return l1 == l2 && d1 == d2 && b1.equals(b2);
		} else {
			return false;
		}
	}
	
	/**
     * compares 2 numbers
     * @param n1 number 1
     * @param n2 number 2
     * @param precision
     * @return true if equals
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} int
     * 
     * {param} Number(10)
     * {param} Number(20) 
     * 
     * {example} equals(10, 20) result: current value
     * 
     */
	public static boolean equals(Double n1, Double n2, int precision) {
		if (n1 != null && n2 != null) {	
			return Math.abs(n1 - n2) < Math.pow(10d, -1 * (precision + 1));
		} else {
			return false;
		}
	}
	
	/**
     * rounds the number to a value with 2 decimal digits
     * 
     * {Category} NumberUtil
     * 
     * {talendTypes} double | Double
     * 
     * {param} double(2.5) number: number to format
     * 
     * {example} numberToString(1234.12345) result: 1234.12
     * 
     */
	public static Double roundScaleN(Number number, int precision) {
		if (number != null) {
			return Math.round(number.doubleValue() * Math.pow(10d, precision)) / Math.pow(10d, precision);
		} else {
			return null;
		}
	}

	/**
	 * sort a given list of long numbers given as string
	 * @param listString list long numbers
	 * @return list long numbers sorted numerical
	 * @throws Exception
	 * 
     * {Category} NumberUtil
     * 
     * {talendTypes} String
     * 
     * {param} String("2,5,3");
     * 
     * {example} sortLongNumberList("2,5,3") result: sorted list
	 */
	public static String sortLongNumberList(String listString) {
		if (listString == null || listString.trim().isEmpty()) {
			return listString;
		}
		try {
			StringTokenizer st = new StringTokenizer(listString, ",");
			List<Long> listNumbers = new ArrayList<Long>();
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token != null && token.trim().isEmpty() == false) {
					listNumbers.add(Long.valueOf(token));
				}
			}
			Collections.sort(listNumbers);
			boolean firstLoop = true;
			StringBuilder result = new StringBuilder();
			for (Long value : listNumbers) {
				if (firstLoop) {
					firstLoop = false;
				} else {
					result.append(",");
				}
				result.append(value);
			}
			return result.toString();
		} catch (NumberFormatException nfe) {
			System.err.println("Could not convert text to Long: " + nfe.getMessage());
			return listString;
		}
	}

}
