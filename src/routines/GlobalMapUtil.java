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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GlobalMapUtil {

	public static final String OVERRIDE_DIE_CODE_KEY = "OVERRIDE_DIE_CODE";
	
    /**
     * adds a value to a list and stores this in the globalMap
     * use the first value found.
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} List
     * 
     * {param} String(globalMap) String: reference to globalMap.
     * {param} String(listName)
     * {param} Object(value)
     * 
     * {example} addToList(globalMap,listName, value)
     * 
     */
	public static List<Object> addToList(Map<String, Object> globalMap, String listName, Object value) {
		Object test = globalMap.get(listName);
		if (test == null) {
			List<Object> list = new ArrayList<Object>();
			list.add(value);
			globalMap.put(listName, list);
			return list;
		} else if (test instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) test;
			list.add(value);
			return list;
		} else {
			throw new IllegalStateException("globalMap contains already a none-list value with the key: " + listName);
		}
	}
	
    /**
     * returns a list within the globalMap
     * If the list does not exist, it will be created
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} List
     * 
     * {param} String(globalMap) String: reference to globalMap.
     * 
     * {param} String(listName)
     * 
     * {example} getList(globalMap,listName)
     * 
     */
	@SuppressWarnings("unchecked")
	public static List<Object> getList(Map<String, Object> globalMap, String listName) {
		Object test = globalMap.get(listName);
		if (test == null) {
			return (List<Object>) globalMap.put(listName, new ArrayList<Object>());
		} else if (test instanceof List) {
			return (List<Object>) test;
		} else {
			throw new IllegalStateException("globalMap has a none-list value with the key: " + listName);
		}
	}
	
    /**
     * return integer and accepts Integer and String. Null returns 0
     * use the first value found.
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} String(globalMap) String: reference to globalMap.
     * 
     * {param} String("key1","keyN") input: float pointing format.
     * 
     * {example} getInt(globalMap, "tFileInputDelimited_1_NB_LINE") result: 123 ...
     * 
     */
    public static int getInt(Map<String, Object> globalMap, String ...key) {
		Set<String> keys = globalMap.keySet();
    	for (String k : key) {
    		if (k != null) {
        		String mapKey = null;
        		for (String t : keys) {
        			if (t.equalsIgnoreCase(k.trim())) {
            			mapKey = t;
        			}
        		}
        		if (mapKey != null) {
            		Object v = globalMap.get(mapKey);
            		if (v instanceof Integer) {
            			int count = getNullSaveInt(v);
            			if (count > 0) {
            				return count;
            			}
            		}
        		}
    		}
    	}
    	return 0;
    }
    
    /**
     * return sum of all input lines for components
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} String(globalMap) globalMap: reference to globalMap.
     * 
     * {param} String("componente_1_Name","componente_N_Name") input: key to value in global map.
     * 
     * {example} getSumOfCurrentInputLines(globalMap, "tFileInputDelimited_1") result: 123 ...
     * 
     */
    public static int getSumOfCurrentInputLines(Map<String, Object> globalMap, String ...componentNames) {
    	int count = 0;
    	for (String componentName : componentNames) {
    		count = count + getInt(globalMap, componentName + "_NB_LINE", componentName + "_NB_FILE");
    	}
    	return count;
    }
    
    /**
     * returns reject counts for the given components
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} String(globalMap) globalMap: reference to globalMap.
     * 
     * {param} String("tPostgresqlInput_2")
     * 
     * {param} String("componente_1_Name","componente_N_Name") input: key to value in global map.
     * 
     * {example} getRejectCount(globalMap, "tPostgresqlInput_2", "componente_1_Name") result: 123 ...
     * 
     */
    public static int getRejectCount(Map<String, Object> globalMap, String inputComponent, String ...insertComponents) {
    	int inputCount = getInt(globalMap, inputComponent + "_NB_LINE", inputComponent + "_NB_FILE");
    	
    	int outCount=0;
    	
    	for (String componentName : insertComponents) {
    		outCount += getInt(globalMap, componentName + "_NB_LINE", componentName + "_NB_FILE");
    	}    	
    	
    	return inputCount - outCount;
    }
    
    /**
     * returns reject counts for the given components
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} String(globalMap) globalMap: reference to globalMap.
     * 
     * {param} String("tPostgresqlInput_2")
     * 
     * {param} int(context.insert_count) input: the current insert count.
     * 
     * {example} getRejectCount(globalMap, "tPostgresqlInput_2", 123) result: 123 ...
     * 
     */
    public static int getRejectCount(Map<String, Object> globalMap, String inputComponent, int insertCount) {
    	int inputCount = getInt(globalMap, inputComponent + "_NB_LINE", inputComponent + "_NB_FILE");
    	return inputCount - insertCount;
    }

    /**
     * returns the errorCode from tDie component (not zero) even if there is a post job 
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} String(globalMap) globalMap: reference to globalMap.
     * 
     * {param} String(errorCode) errorCode: key to value in global map.
     * 
     * {example} getErrorCode(globalMap, errorCode) result: 123 ...
     * 
     */
    public static Integer getErrorCode(Map<String, Object> globalMap, Integer errorCode) {
    	Integer errorCodeFromTDie = errorCode;
    	if (globalMap.containsKey(OVERRIDE_DIE_CODE_KEY)) {
    		Integer code = (Integer) globalMap.get(OVERRIDE_DIE_CODE_KEY);
			if (code.intValue() != 0) {
				errorCodeFromTDie = code;
			}
    	} else {
        	for (Map.Entry<String, Object> entry : globalMap.entrySet()) {
        		if (entry.getKey().endsWith("DIE_CODE")) {
        			if (entry.getValue() instanceof Integer) {
        				Integer code = (Integer) entry.getValue();
        				if (code.intValue() != 0) {
        					errorCodeFromTDie = code;
        				}
        			}
        		}
        	}
    	}
    	return errorCodeFromTDie;
    }
    
    /**
     * returns the error message from component 
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} String
     * 
     * {param} String(globalMap) globalMap: reference to globalMap.
     * 
     * {example} getErrorMessage(globalMap) result: 123 ...
     * 
     */
	public static String getErrorMessage(Map<String, Object> globalMap) {
		String tDieMessage = null;
		String compName = null;
    	StringBuilder message = new StringBuilder();
    	for (Map.Entry<String, Object> entry : globalMap.entrySet()) {
    		String key = entry.getKey();
			compName = key.replace("_DIE_CODE", "");
    		if (key.endsWith("_DIE_CODE")) {
    			if (entry.getValue() instanceof Integer) {
    				Integer code = (Integer) entry.getValue();
    				if (code.intValue() != 0) {
    					key = key.replace("_DIE_CODE", "_DIE_MESSAGE");
    					tDieMessage = (String) globalMap.get(key);
    					break;
    				}
    			}
    		}
    	}
    	if (tDieMessage != null && tDieMessage.trim().isEmpty() == false && "the end is near".equalsIgnoreCase(tDieMessage) == false) {
			message.append(compName);
			message.append(":");
    		message.append(tDieMessage);
    		message.append("\n");
    	}
		// get error message
    	for (Map.Entry<String, Object> entry : globalMap.entrySet()) {
    		String key = entry.getKey();
    		if (key.endsWith("_ERROR_MESSAGE") && entry.getValue() !=  null) {
    			compName = key.replace("_ERROR_MESSAGE", "");
    			message.append(compName);
    			message.append(":");
        		message.append(entry.getValue().toString());
        		message.append("\n");
        		if (compName.contains("tRunJob")) {
        			message.append(compName + " returncode: " + globalMap.get(compName + "_CHILD_RETURN_CODE"));
        			message.append("\n" + compName + " stacktrace: " + globalMap.get(compName + "_CHILD_EXCEPTION_STACKTRACE"));
        		}
    		}
    	}
		if (message.length() > 0) {
			return message.toString();
		} else {
			return null;
		}
	}

    /**
     * returns the stack traces from tRunJob components
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} String
     * 
     * {param} String(globalMap) globalMap: reference to globalMap.
     * 
     * {example} getTRunJobStackTraces(globalMap) result: 123 ...
     * 
     */
	public static String getTRunJobStackTraces(Map<String, Object> globalMap) {
		StringBuilder message = new StringBuilder();
		for (Map.Entry<String, Object> entry : globalMap.entrySet()) {
			String key = entry.getKey();
			if (key.endsWith("_CHILD_EXCEPTION_STACKTRACE")) {
				if (entry.getValue() instanceof String) {
					int pos = key.length() - "_CHILD_EXCEPTION_STACKTRACE".length();
					String compName = key.substring(0, pos);
					message.append(compName);
					message.append(": \n");
					message.append((String) entry.getValue());
					message.append("\n");
					break; 
				}
			}
		}
		if (message.length() > 0) {
			return message.toString();
		} else {
			return null;
		}
	}

	/**
     * return sum of all inserted lines for components
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} String(globalMap) globalMap: reference to globalMap.
     * 
     * {param} String("componente_1_Name","componente_N_Name") input: key to value in global map.
     * 
     * {example} getSumOfCurrentLinesInserted(globalMap, "tPostgresqlOutput_1") result: 123 ...
     * 
     */
    public static int getSumOfCurrentLinesInserted(Map<String, Object> globalMap, String ...componentNames) {
    	int count = 0;
    	for (String componentName : componentNames) {
    		count = count + getInt(globalMap, componentName + "_NB_LINE_INSERTED", componentName + "_NB_LINE");
    	}
    	return count;
    }

    /**
     * return sum of all updated lines for components
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} String(globalMap) globalMap: reference to globalMap.
     * 
     * {param} String("componente_1_Name","componente_N_Name") input: key to value in global map.
     * 
     * {example} getSumOfCurrentUpdatedLines(globalMap, "tPostgresqlOutput_1") result: 123 ...
     * 
     */
    public static int getSumOfCurrentUpdatedLines(Map<String, Object> globalMap, String ...componentNames) {
    	int count = 0;
    	for (String componentName : componentNames) {
    		count = count + getInt(globalMap, componentName + "_NB_LINE_UPDATED");
    	}
    	return count;
    }
    
    /**
     * return sum of all proceed lines for components
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} String(globalMap) globalMap: reference to globalMap.
     * 
     * {param} String("componente_1_Name","componente_N_Name") input: key to value in global map.
     * 
     * {example} getSumOfCurrentRejectedLines(globalMap, "tPostgresqlOutput_1") result: 123 ...
     * 
     */
    public static int getSumOfCurrentRejectedLines(Map<String, Object> globalMap, String ...componentNames) {
    	int count = 0;
    	for (String componentName : componentNames) {
    		count = count + getInt(globalMap, componentName + "_NB_LINE_REJECTED");
    	}
    	return count;
    }

    /**
     * check if this code line will be processed first time
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} boolean | Boolean
     * 
     * {param} String(globalMap) globalMap: reference to globalMap.
     * 
     * {param} String("flag") flag: unique flag to indicate the step you want to track.
     * 
     * {example} checkFirstRun(globalMap, "my_step") result: true
     * 
     */
    public static boolean checkFirstRun(Map<String, Object> globalMap, String flag) {
    	String key = "flag_" + flag;
    	if (globalMap.containsKey(key)) {
    		return false;
    	} else {
    		globalMap.put(key, new java.util.Date());
    		return true;
    	}
    }

    /**
     * return integer and accepts Integer and String. Null returns 0
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} int | Integer
     * 
     * {param} Integer(123) input: float pointing format.
     * 
     * {example} getNullSaveInt("123", "replacements) result: 123 ...
     * 
     */
	public static int getNullSaveInt(Object input, String ...toRemoveString) {
		if (input instanceof Integer) {
			Integer i = (Integer) input;
			if (i == 0) {
				return 0;
			} else {
				return i.intValue();
			}
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
     * Add an error message accessible for tJobInstanceEnd
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} String
     * 
     * {param} Object(globalMap) globalMap: 
     * {param} String(componentId) componentId: 
     * {param} String(message) message: 
     * 
     * {example} addErrorMessage(globalMap, "tJava_1", "The end is near") returns "The end is near"
     * 
     */
	public static String addErrorMessage(Map<String, Object> globalMap, String componentId, String message) {
		String prevMessage = (String) globalMap.get(componentId + "_ERROR_MESSAGE");
		if (prevMessage == null) {
			globalMap.put(componentId + "_ERROR_MESSAGE", message);
			return message;
		} else {
			String newMessage = prevMessage + "\n" + message;
			globalMap.put(componentId + "_ERROR_MESSAGE", newMessage);
			return newMessage;
		}
	}
	
	private static final Map<String, Integer> counterMap = new HashMap<>();
	
    /**
     * add a counter
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} void
     * 
     * {param} String(pid) pid
     * {param} String(countName) countName
     * {param} Integer(count) count
     * 
     * {example} addCounter(globalMap, "NB_LINE", 0) result: 123 ...
     * 
     */
	public static void addCounter(String pid, String countName, Integer count) {
		if (count != null) {
			synchronized(counterMap) {
				Integer c = counterMap.get(pid + countName);
				if (c == null) {
					c = count;
				} else {
					c = c.intValue() + count.intValue();
				}
				counterMap.put(pid + countName, c);
			}
		}
	}
	
    /**
     * get a counter
     * 
     * {Category} GlobalMapUtil
     * 
     * {talendTypes} Integer
     * 
     * {param} String(pid) pid
     * {param} String(counterName) countName
     * 
     * {example} getCounter(this.pid, "NB_LINE", 0) result: 123 ...
     * 
     */
	public static int getCounter(String pid, String countName) {
		Integer r = counterMap.get(pid + countName);
		if (r != null) {
			return r.intValue();
		} else {
			return 0;
		}
	}

}