package routines;

public class BooleanUtil {

    /**
     * getBoolean extracts a boolean from various inputs
     * 
     * 
     * {talendTypes} boolean
     * 
     * {Category} BooleanUtil
     * 
     * {param} object("true") object
     * 
     * {example} getBoolean("true") # true
     */
    public static boolean getBoolean(Object object) {
    	if (object instanceof Boolean) {
    		return ((Boolean) object).booleanValue();
    	} else if (object instanceof String) {
    		String value = (String) object;
    		value = value.toLowerCase();
    		if ("true".equals(value)) {
    			return true;
    		} else if ("false".equals(value)) {
    			return false;
    		} else if ("f".equals(value)) {
    			return false;
    		} else if ("t".equals(value)) {
    			return true;
    		} else if ("1".equals(value)) {
    			return true;
    		} else if ("0".equals(value)) {
    			return false;
    		} else if ("yes".equals(value)) {
    			return true;
    		} else if ("y".equals(value)) {
    			return true;
    		} else if ("sí".equals(value)) {
    			return true;
    		} else if ("да".equals(value)) {
    			return true;
    		} else if ("no".equals(value)) {
    			return false;
    		} else if ("нет".equals(value)) {
    			return false;
    		} else if ("n".equals(value)) {
    			return false;
    		} else if ("ja".equals(value)) {
    			return true;
    		} else if ("j".equals(value)) {
    			return true;
    		} else if ("nein".equals(value)) {
    			return false;
    		} else if ("oui".equals(value)) {
    			return true;
    		} else if ("non".equals(value)) {
    			return false;
    		} else if ("ok".equals(value)) {
    			return true;
    		} else if ("x".equals(value)) {
    			return true;
    		} else if (value != null) {
    			return false;
    		} else {
    			return false;
    		}
    	} else if (object instanceof Number) {
    		int n = ((Number) object).intValue();
    		if (n == 0) {
    			return false;
    		} else if (n > 0) {
    			return true;
    		} else {
    			return false;
    		}
    	} else if (object != null) {
    		throw new IllegalArgumentException("Type: " + object.getClass().getName() + " cannot be converted into a boolean value.");
    	} else {
    		return false;
    	}
    }
    
}
