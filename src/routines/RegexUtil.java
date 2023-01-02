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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	private static Map<String, Pattern> patternMap = new HashMap<String, Pattern>();
	
	private static Pattern compile(String regex) {
		Pattern p = patternMap.get(regex);
		if (p == null) {
			p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			patternMap.put(regex, p);
		}
		return p;
	}
	
    /**
     * Extracts a string by regex groups.
     * 
     * {Category} RegexUtil
     * 
     * {param} string("content") content: String.
     * 
     * {param} string("regex") regex: String
     * 
     * {example} extractByRegexGroups("content","regex") # value2
     */
	public static String extractByRegexGroups(String content, String regex) {
		if (regex != null) {
			if (content != null) {
				content = content.trim();
				final StringBuffer sb = new StringBuffer();
				Pattern pattern = compile(regex);
		        Matcher matcher = pattern.matcher(content);
		        while (matcher.find()) {
		            if (matcher.start() < matcher.end()) {
		            	for (int i = 1, n = matcher.groupCount(); i <= n; i++) {
			                sb.append(matcher.group(i));
		            	}
		            }
		        }
				return sb.toString();
			} else {
				return null;
			}
		} else {
			return content;
		}
	}
	
    /**
     * Extracts a string by regex groups.
     * 
     * {Category} RegexUtil
     * 
     * {param} string("content") content: String.
     * 
     * {param} string("regex") regex: String
     * 
     * {example} extractByRegexGroupsToList("content","regex") # value2
     */
	public static List<String> extractByRegexGroupsToList(String content, String regex) {
		if (regex != null) {
			if (content != null) {
				content = content.trim();
				final List<String> result = new ArrayList<String>();
				Pattern pattern = compile(regex);
		        Matcher matcher = pattern.matcher(content);
		        while (matcher.find()) {
		            if (matcher.start() < matcher.end()) {
		            	for (int i = 1, n = matcher.groupCount(); i <= n; i++) {
		            		result.add(matcher.group(i));
		            	}
		            }
		        }
				return result;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
     * Extracts a string by a particular regex group.
     * 
     * {Category} RegexUtil
     * 
     * {param} string("content") content: String.
     * 
     * {param} string("regex") regex: String
     * 
     * {param} int(2) groupIndex
     * 
     * {example} extractByRegexGroup("content","regex", groupIndex) # value2
     */
	public static String extractByRegexGroup(String content, String regex, int groupIndex) {
		if (regex != null) {
			if (content != null) {
				content = content.trim();
				final StringBuffer sb = new StringBuffer();
				Pattern pattern = compile(regex);
		        Matcher matcher = pattern.matcher(content);
		        while (matcher.find()) {
		            if (matcher.start() < matcher.end()) {
		            	for (int i = 1, n = matcher.groupCount(); i <= n; i++) {
				            if (i == groupIndex) {
				                sb.append(matcher.group(i));
				            }
		            	}
		            }
		        }
				return sb.toString();
			} else {
				return null;
			}
		} else {
			return content;
		}
	}

	/**
     * Extracts a string by a particular regex group.
     * 
     * {Category} RegexUtil
     * 
     * {param} string("content") content: String.
     * {param} string("regex") regex: String 
     * {param} int(1) groupIndex
     * {param} int(2) groupOccurrence
     * 
     * {example} extractByRegexGroup("content","regex", groupIndex, groupOccurrence) # value2
     */
	public static String extractByRegexGroup(String content, String regex, int groupIndex, int groupOccurrence) {
		if (regex != null) {
			if (content != null) {
				content = content.trim();
				final StringBuffer sb = new StringBuffer();
				Pattern pattern = compile(regex);
		        Matcher matcher = pattern.matcher(content);
		        int index = 0;
		        while (matcher.find()) {
		        	index++;
		            if (matcher.start() < matcher.end()) {
		            	if (index == groupOccurrence) {
			            	for (int i = 1, n = matcher.groupCount(); i <= n; i++) {
					            if (i == groupIndex) {
					                sb.append(matcher.group(i));
					            }
			            	}
		            	}
		            	
		            }
		        }
				return sb.toString();
			} else {
				return null;
			}
		} else {
			return content;
		}
	}

	/**
     * Tests a content contains text matching to all groups.
     * 
     * {Category} RegexUtil
     * 
     * {param} string("content") url: String.
     * 
     * {param} string("regex") key: String
     * 
     * {example} containsAllRegexGroups("content","regex") # value2
     */
	public static boolean containsAllRegexGroups(String content, String regex) {
		if (regex != null) {
			if (content != null) {
				content = content.trim();
				boolean ok = false;
				Pattern pattern = compile(regex);
		        Matcher matcher = pattern.matcher(content);
		        while (matcher.find()) {
		            if (matcher.start() < matcher.end()) {
		            	for (int i = 1, n = matcher.groupCount(); i <= n; i++) {
			                String test = matcher.group(i);
			                if (test == null || test.isEmpty()) {
			                	ok = false;
			                	break;
			                } else {
			                	ok = true;
			                }
		            	}
		            } else {
	                	ok = false;
	                	break;
		            }
		        }
				return ok;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
     * Replaces portions of text identified by regex with a replacement
     * 
     * {Category} RegexUtil
     * 
     * {param} string("content") content: String.
     * 
     * {param} string("regex") regex: String
     * 
     * {param} string(replacement) replacement: String
     * 
     * {example} replaceByRegexGroups("content","regex", replacement) # value2
     */
	public static String replaceByRegexGroups(String content, String regex, String replacement) {
		if (content != null) {
			StringBuilder result = new StringBuilder();
			java.util.regex.Pattern pattern = compile(regex);
			java.util.regex.Matcher matcher = pattern.matcher(content);
			int prevEnd = 0;
			while (matcher.find()) {
        		for (int i = 1; i <= matcher.groupCount(); i++) {        			
        			int start = matcher.start(i);
        			int end = matcher.end(i);
                    if (start < end) {
                    	result.append(content.substring(prevEnd, start));
                    	if (replacement != null) {
                        	result.append(replacement);
                    	}
                        prevEnd = end;
                    }
        		}
			}
    		if (prevEnd > 0 && prevEnd < content.length()) {
    			result.append(content.substring(prevEnd, content.length()));
    		} else if (prevEnd == 0) {
    			// we have found nothing
    			result.append(content);
    		}
			return result.toString();
		} else {
			return null;
		}
	}

	/**
     * Tests if a given string contains text mathing to the given regex
     * Returns the index of the found content and if not found returns -1
     * 
     * {Category} RegexUtil
     * 
     * {param} string("content") content: String.
     * 
     * {param} string("regex") regex: String
     * 
     * {param} string(replacement) replacement: String
     * 
     * {example} replaceByRegexGroups("content","regex", replacement) # value2
     */
	public static int matchesByRegex(String content, String regex) {
		if (regex != null) {
			if (content != null) {
				content = content.trim();
				Pattern pattern = compile(regex);
		        Matcher matcher = pattern.matcher(content);
		        while (matcher.find()) {
		            if (matcher.start() < matcher.end()) {
		            	return matcher.start();
		            }
		        }
				return -1;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	/**
     * Extracts a content by regex expression without using groups
     * 
     * {Category} RegexUtil
     * 
     * {param} string("content") url: String.
     * 
     * {param} string("regex") key: String
     * 
     * {example} extractByRegex("content","regex") # value2
     */
	public static String extractByRegex(String content, String regex) {
		if (regex != null) {
			if (content != null) {
				content = content.trim();
				final StringBuffer sb = new StringBuffer();
				Pattern pattern = compile(regex);
		        Matcher matcher = pattern.matcher(content);
		        while (matcher.find()) {
		            if (matcher.start() < matcher.end()) {
		            	sb.append(content.subSequence(matcher.start(), matcher.end()));
		            }
		        }
				return sb.toString();
			} else {
				return null;
			}
		} else {
			return content;
		}
	}

}