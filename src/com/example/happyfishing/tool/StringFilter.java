package com.example.happyfishing.tool;


import java.util.regex.PatternSyntaxException;

public  class StringFilter {
	public static boolean fileterPassword(String str) throws PatternSyntaxException{
		if (str == null) {
			return false;
		}
		String regEx = "^[A-Za-z0-9]+$";
		return str.matches(regEx);
	}
}
