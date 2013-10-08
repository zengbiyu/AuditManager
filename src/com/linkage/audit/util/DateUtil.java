package com.linkage.audit.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
	/**
	 * 日时间前缀
	 * @return
	 */
    public static String getCurDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal=Calendar.getInstance();
        //cal.add(Calendar.DAY_OF_MONTH, -1);
        return sdf.format(cal.getTime());
    }
    
    /**
     * 月时间前缀
     * @return
     */
    public static String getCurMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar cal=Calendar.getInstance();
        //cal.add(Calendar.MONTH, -1);
        return sdf.format(cal.getTime());
    }
    
    public static String getCurTime2() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdf.format(new java.util.Date());
    }
    
    public static String getCurTime() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	return sdf.format(new java.util.Date());
    }
}
