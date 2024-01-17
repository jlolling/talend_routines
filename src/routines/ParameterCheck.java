package routines;

public class ParameterCheck {

    /**
     * Check if a variable is filled or not
     * @param value to check
     * @param varName name of the var
     * @zeroOrNegativeAllowed false= zero or less are not allowed for numbers, true=only null will be checked
     * @throws Exception of var is not filled
     * 
     * {talendTypes} String
     * 
     * {Category} ParameterCheck
     * 
     * {param} Object(value)
     * {param} string(varName)
     * {param} boolean(zeroOrNegativeAllowed)
     * 
     * {example} failIfNotFilled(value, "my_contextVar", false) # hello world !.
     */
    public static void failIfNotFilled(Object value, String varName, boolean zeroOrNegativeAllowed) throws Exception {
    	if (value == null) {
    		throw new Exception("Variable: " + varName + " is null but must be filled");
    	} else if (value instanceof String) {
    		String v = (String) value;
    		if (v.trim().isEmpty()) {
    			throw new Exception("Variable: " + varName + " is empty but must be filled");
    		}
    	} else if (zeroOrNegativeAllowed == false && value instanceof Number) {
    		Number n = (Number) value;
    		if (n.doubleValue() <= 0d) {
    			throw new Exception("Variable: " + varName + " is zero or negative but must be positive. Value=" + value);
    		}
    	}
    }
    
}
