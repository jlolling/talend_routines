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

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
			return Long.parseLong((String) s);
		} else {
			return 0;
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
			return Integer.parseInt((String) s);
		} else {
			return 0;
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
			return Integer.parseInt((String) s);
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
			NumberFormat nf = NumberFormat.getInstance(locale);
			Number n = null;
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
			String s = (String) input;
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
				return dividend.doubleValue() / (double) quotient.intValue();
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
				return ((double) dividend.intValue()) / quotient.doubleValue();
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
			try {
				Double.parseDouble((String) o);
				return true;
			} catch (NumberFormatException nfe) {
				return false;
			}
		} else {
			return false;
		}
	}
	
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
	

	
}
