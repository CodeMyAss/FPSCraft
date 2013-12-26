package net.castegaming.plugins.FPSCaste.util;

import java.util.Arrays;

public class Parse {
	/**
	 * Safety, to prevent creation
	 */
	private Parse() {}
	
	/**
	 * Parses a string into an int<br/>
	 * This will return null if it fails
	 * @param number the number to parse
	 * @return an int if successfull, otherwise null
	 */
	public static int parseInt(String number){
		return parseInt(number, null);
	}
	
	/**
	 * Parses an object into an int.<br/>
	 * IF this fails, the second parameter will be returned
	 * @param number the Object to check
	 * @param failed the number to return if it failed
	 * @return the converted or the failed number
	 */
	public static int parseInt(Object number, Integer failed){
		if (number == null) return failed;
		else return parseInt(number.toString(), failed);
	}
	
	/**
	 * Parses a String into an int.<br/>
	 * IF this fails, the second parameter will be returned
	 * @param number the String to check
	 * @param failed the number to return if it failed
	 * @return the converted or the failed number
	 */
	public static int parseInt(String number, Integer failed){
		try {
			return Integer.parseInt(number);
		} catch (Exception e){
			return failed;
		}
	}
	
	/**
	 * Parses a string into a number. <br/>
	 * This will return null if it fails
	 * @param number the string to convert
	 * @return a double if successfull, otherwise null
	 */
	public static double parseDouble(String number){
		return parseDouble(number, null);
	}
	
	/**
	 * Parses an object into a double.<br/>
	 * IF this fails, the second parameter will be returned
	 * @param number the object to check
	 * @param failed the number to return if it failed
	 * @return the converted or the failed number
	 */
	public static double parseDouble(Object number, Double failed){
		if (number == null){
			return failed;
		} else {
			try {
				return Double.parseDouble(number.toString());
			} catch (Exception e){
				return failed;
			}
		}
	}

	/**
	 * Parses the array of strings into a nice to read string.<br/>
	 * Default format is: ", " inbetween.
	 * @param list the list to convert
	 * @return the converted string.
	 */
	public static String ArrayToString(String[] list) {
		return ArrayToString(list, ", ");
	}
	
	/**
	 * Parses the array of strings into a nice to read string.<br/>
	 * Default format is: ", " inbetween.
	 * @param list the list to convert
	 * @param inbetween defines the characters used inbetween 2 values
	 * @return the converted string.
	 */
	public static String ArrayToString(String[] list, String inbetween){ 
		return Arrays.toString(list).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ",  inbetween);
	}

	/**
	 * Checks if the string isnt null.<br/>
	 * @param string the string to check
	 * @param fallback the string to return if it doenst exist
	 * @return The string if its not null, otherwise the fallback string
	 */
	public static String parseString(String string, String fallback) {
		return string != null ? string : fallback;
	}
}
