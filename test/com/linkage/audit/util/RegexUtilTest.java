package com.linkage.audit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * 正则匹配帮助类.
 * @author kelen
 * 
 * @version 1.0.0
 * 
 * @since 2012-3-22 
 */
public class RegexUtilTest {

	public static Matcher getMatcher(String regex, String str) {
		if (str == null)
			return null;
		Pattern p = Pattern.compile(regex);
		return p.matcher(str);
	}

	public static String getGroupValue(String regex, String str, int index) {
		Matcher m = getMatcher(regex, str);
		if(m==null) return null;
		if (m.find()) {
			return m.group(index);
		}
		return null;
	}

	public static String[] getGroupValues(String regex, String str) {
		Matcher m = getMatcher(regex, str);
		if(m==null) return null;
		if (m.find()) {
			int groupCount = m.groupCount();
			String[] result = new String[groupCount+1];
			for (int i = 0; i <= groupCount; i++) {
				result[i] = m.group(i);
			}
			return result;
		}
		return null;
	}

	public static boolean isMatches(String regex, String str) {
		Matcher m = getMatcher(regex, str);
		if(m==null) return false;
		if (m == null)
			return false;
		return m.find();
	}


	public static List<String[]> getGroupList(String regex, String str) {
		Matcher m = getMatcher(regex, str);
		if(m==null) return null;
		List<String[]> list = new ArrayList<String[]>();
		while (m.find()) {
			int groupCount = m.groupCount();
			String[] result = new String[groupCount+1];
			for (int i = 0; i <= groupCount; i++) {
				result[i] = m.group(i);
			}
			list.add(result);
		}
		return list;
	}
	
	/**
	 * 替换特殊字符
	 * @return
	 */
	public static String replaceSpeChar(String regex) {
		regex=regex.replace("$", "\\$")
					.replace("(", "\\(")
					.replace(")", "\\)")
					.replace("*", "\\*")
					.replace("+", "\\+")
					.replace("-", "\\-")
					.replace(".", "\\.")
					.replace("[", "\\[")
					.replace("]", "\\]")
					.replace("?", "\\?")
					.replace("\\", "\\\\")
					.replace("^", "\\^")
					.replace("{", "\\{")
					.replace("}", "\\}")
					.replace("|", "\\|");
		
		return regex.replaceAll("\\\\\\\\", "\\\\");
	}
	
	@Test
	public void testReplaceSpeChar() {
		String regex="$时间 - 设备型号 - [ip地址] 用户 - 内容";
		System.out.println(replaceSpeChar(regex));
		
		regex = replaceSpeChar(regex);
		
		String REGEX =  "(.*) - (.*) - \\[(.*)\\] (.*) - (.*)"; 
		System.out.println(REGEX);
	}
}
