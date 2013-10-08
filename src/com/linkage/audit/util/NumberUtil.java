package com.linkage.audit.util;

public class NumberUtil {
	public static void main(String[] args) {
		String str1 = "1122.2.2";
		String str2 = "13814510725";
		String str3 = "111.2";
		String str4 = "111s";
		String str5 = "111.s";
		String str6 = "1s11";
		
		String str7 = "-";
		System.out.println(str1 + ":" + isNum(str1));
		System.out.println(str2 + ":" + isNum(str2));
		System.out.println(str3 + ":" + isNum(str3));
		System.out.println(str4 + ":" + isNum(str4));
		System.out.println(str5 + ":" + isNum(str5));
		System.out.println(str6 + ":" + isNum(str6));
		
		System.out.println(str7 + ":" + isNum(str7));
	}

	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+)))$");
	}
	
	public static Integer getInteger(String str) {
		Integer integer = null;
		if(str!=null&&!"".equals(str)) {
			integer = Integer.parseInt(str);
		}
		return integer;
	}
}