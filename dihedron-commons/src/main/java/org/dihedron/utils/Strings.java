/**
 * Copyright (c) 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron Common Utilities library ("Commons").
 *
 * "Commons" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "Commons" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "Commons". If not, see <http://www.gnu.org/licenses/>.
 */

package org.dihedron.utils;



/**
 * Utility package for string operations.
 * 
 * @author Andrea Funto'
 */
public class Strings {
	

	/**
	 * Checks whether the given string is neither null nor blank.
	 * 
	 * @param string
	 *   the string to be checked.
	 * @return
	 *   <code>true</code> if the string is not null and has some content besides 
	 *   blank spaces.
	 */
	public static boolean isValid(String string) {
		return (string != null && string.trim().length() > 0);
	}

	/**
	 * Trims the input string if it is not null.
	 * 
	 * @param string
	 *   the string to be trimmed if not null.
	 * @return
	 *   the trimmed string, or null.
	 */
	public static String trim(String string) {
		if(string != null) {
			return string.trim();
		}
		return string;
	}
	
	/**
	 * Returns a safe concatenation of the input strings, or null if all strings 
	 * are null.
	 * 
	 * @param strings
	 *   the set of string to concatenate.
	 * @return
	 *   the concatenation of the input strings, or null if all strings are null.
	 */
	public static String concatenate(String... strings) {
		return join("", strings);
	}
	
	/**
	 * Joins the given set of strings using the provided separator.
	 * 
	 * @param separator
	 *   a character sequence used to separate strings.
	 * @param strings
	 *   the set of strings to be joined.
	 * @return
	 *   a string containing the list of input strings, or null if no valid input
	 *   was provided.
	 */
	public static String join(String separator, String... strings) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(String string : strings) {
			if(string != null) {				
				if(!first) {
					builder.append(separator);					
				}
				builder.append(string);
				first = false;
			}
		}
		if(builder.length() > 0) {
			return builder.toString();
		}
		return null;		
	}
	
	/**
	 * Splits the given string using the given delimiter.
	 * 
	 * @param delimiter
	 *   a character sequence that is assumed to be separating the items to be
	 *   extracted from the joined string.
	 * @param joined
	 *   the string to extract items from.
	 * @return
	 *   an array of (trimmed) strings.
	 */
	public static String[] split(String delimiter, String joined) {
		StringTokeniser tokeniser = new StringTokeniser(delimiter);
		tokeniser.setTrimSpaces(true);
		return tokeniser.tokenise(joined);
	}
	
	/**
	 * Returns the first valid string in the given set.
	 * 
	 * @param strings
	 *   a set of strings.
	 * @return
	 *   the first valid string in the set, or null if none is valid.
	 */
	public static String firstValidOf(String... strings) {
		if(strings != null) {
			for(int i = 0; i < strings.length; ++i) {
				if(Strings.isValid(strings[i])) {
					return strings[i];
				}
			}
		}
		return null;	
	}
}
