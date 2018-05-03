package edu.buaa.sem.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	public static boolean matchLetterAndNumber(String regex) {
		if (regex != null && regex.isEmpty()) {
			Pattern p = Pattern.compile("^[a-zA-Z\\d]+$");
			Matcher m = p.matcher(regex);
			return m.find();
		} else
			return true;
	}
}
