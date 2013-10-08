package com.linkage.audit.util;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.Test;

public class DateUtilTest {
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
    
    @Test
    public void getCurTime() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	System.out.println(sdf.format(new java.util.Date()));
    }
    
    @Test
    public void testArrays(){
		String[] array1 = new String[]{"aaa","bbb","ccc"};   
        String[] array2 = new String[]{"bbb","ccc","aaa"}; 
        String[] array3 = new String[]{"aaa","bbb","ccc"};  
        
        Arrays.sort(array1);
        Arrays.sort(array2);
        
        if(Arrays.deepEquals(array1, array2)){
        	System.out.println("相等！");
        }else{
        	System.out.println("不相等！");
        }
        if(Arrays.deepEquals(array1, array3)){
        	System.out.println("相等！");
        }else{
        	System.out.println("不相等！");
        }
    }
}
