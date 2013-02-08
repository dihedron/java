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

package org.dihedron.strings;


/**
 * Utility package for string operations.
 * 
 * @author Andrea Funto'
 */
public class Strings {
	
	private static ThreadLocal<StringBuilder> buffer = new ThreadLocal<StringBuilder>() {
		@Override
		public StringBuilder initialValue() {
			return new StringBuilder();
		}
	};

	/**
	 * The logger.
	 */
//	private static Logger logger = LoggerFactory.getLogger(Strings.class);

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
		StringBuilder builder = buffer.get();
		builder.setLength(0);
		for(String string : strings) {
			if(string != null) {
				builder.append(string);
			}
		}
		if(builder.length() > 0) {
			return builder.toString();
		}
		return null;
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
