package com.linkage.audit.util;

/**
 * String工具类
 * @author 
 *
 */
public class StringUtil {
	public static String getString(Object o) {
		String str = "";
		if(o!=null) {
			str = o.toString();
		}
		return str.trim();
	}
}
