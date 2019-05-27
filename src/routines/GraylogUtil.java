package routines;

import java.util.Date;

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
public class GraylogUtil {

    /**
     * build a link for Graylog.
     * 
     * {talendTypes} String
     * 
     * {Category} GraylogUtil
     * 
     * {param} graylogServerUrl("https://graylog.gvl.local");
     * {param} source("talendjobtest02")
     * {param} taskName("dwh_vts_load")
     * {param} startTs(startDate)
     * {param} endTs(endDate)
     *  
     * {example} buildGraylogLink("https://graylog.gvl.local","talendjob01","dwh_vts_load",startTs, endTs)
     */
    public static String buildGraylogLink(String graylogServerUrl, String source, String taskName, Date startTs, Date endTs) {
    	return graylogServerUrl + "/search?rangetype=absolute&fields=source%2Capplication_name%2Capplication_version%2Cjob_started_at%2Cmessage&width=1920&highlightMessage=&from=" +
    			TimestampUtil.formatAsUTC(startTs,"yyyy-MM-dd'T'HH'%3A'mm'%3A'ss'.000Z'") + 
    			"&to=" + 
    			TimestampUtil.formatAsUTC(endTs,"yyyy-MM-dd'T'HH'%3A'mm'%3A'ss'.000Z'") +
    			"&q=source%3A" + 
    			source + 
    			"*+AND+application_name%3A" +
    			taskName;
    }
}
