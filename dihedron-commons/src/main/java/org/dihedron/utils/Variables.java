/**
 * Copyright (c) 2011, 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron embeddable web container ("e-httpd").
 *
 * "e-httpd" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "e-httpd" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "e-httpd". If not, see <http://www.gnu.org/licenses/>.
 */
package org.dihedron.utils;


import java.util.List;
import java.util.Map;

import org.dihedron.regex.Regex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class takes care of replacing scalar and vector variables, 
 * indicated with ${&gt;var_name&lt;} and @{&gt;var_name&lt;}
 * respectively, in text files.
 * 
 * @author Andrea Funto' 
 */
public class Variables {
	
	public enum Type {
		/**
		 * Scalar variables have the form <code>${variable.name}</code>.
		 */
		SCALAR,
		
		/**
		 * Vector variables have the form <code>@{variable.name}</code>.
		 */
		VECTOR
	}
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Variables.class);

	/**
	 * Regular expression to identify scalar variables.
	 */
	private final static String SCALAR_VARIABLE_PATTERN = "(?:\\$\\{([A-Za-z_\\.][A-Za-z_0-9\\.]*)\\})";
	
	/**
	 * Regular expression to identify vector variables. 
	 */
	private final static String VECTOR_VARIABLE_PATTERN = "(?:@\\{([A-Za-z_\\.][A-Za-z_0-9\\.]*)(?::([^\\}]+))\\})";
	
	/**
	 * Default item separator in vector variables substitution.
	 */
	public final static String DEFAULT_SEPARATOR = ";";
	
	/**
	 * Replaces all variables in the input string; scalar variables are replaced
	 * first, then vector variables follow.
	 * 
	 * @param input
	 *   the input expression, containing variables.
	 * @param variables
	 *   the map of variable names and corresponding values.
	 * @return
	 *   the bound string, where all available variables have been replaced.
	 */
	public static String replace(String input, Map<String, Object> variables) {
		input = replaceScalar(input, variables);
		return replaceVector(input, variables);
	}
	
	/**
	 * Replaces scalar variables in the input string with the values found in 
	 * the input variables map.
	 * 
	 * @param input
	 *   the input string where variable identifiers are to be substituted.
	 * @param variables
	 *   a map of variable names and values.
	 * @return
	 *   the input with all available variables replaced with the appropriate
	 *   values.
	 */
	public static String replaceScalar(String input, Map<String, Object> variables) {
		
		logger.debug("replacing scalar variables...");
		
		Regex scalar = new Regex(SCALAR_VARIABLE_PATTERN);
		String result = input;
		List<String[]> matches = scalar.getAllMatches(result);
		for (String[] group : matches) {			
			String variable = group[0];
			if(variables.containsKey(variable)) {				
				logger.debug("variable '{}' has value '{}'", variable, variables.get(variable));
				String value = variables.get(variable).toString();
				//logger.debug("template before replacement: \"" + result + "\"");
				logger.debug("replacing scalar variable ${{}} with {}", variable, value);
				result = result.replaceAll("\\$\\{" + variable + "\\}", value);
				//logger.debug("template after replacement: \"" + result + "\"");
			}
		}
		return result;
	}
	
	/**
	 * Replaces vector variables in the input string with the values found in 
	 * the input variables map.
	 * 
	 * @param input
	 *   the input string where variable identifiers are to be substituted.
	 * @param variables
	 *   a map of variable names and values.
	 * @return
	 *   the input with all available variables replaced with the appropriate
	 *   values.
	 */
	public static String replaceVector(String input, Map<String, Object> variables) {
		
		logger.debug("replacing vector variables...");
		
		Regex scalar = new Regex(VECTOR_VARIABLE_PATTERN);
		String result = input;
		List<String[]> matches = scalar.getAllMatches(result);
		for (String[] group : matches) {
			StringBuilder pattern = new StringBuilder();
			String variable = group[0];
			pattern.append("@\\{").append(variable);
			String separator;
			if(group.length > 1) {
				pattern.append(":").append(group[1]);
				separator = group[1];
			} else {
				separator = DEFAULT_SEPARATOR;
			}
			pattern.append("\\}");
			if(variables.containsKey(variable)) {								
				Object values = variables.get(variable);
				logger.debug("replacing variable @{{}} with {}", variable, values);
				StringBuilder sb = new StringBuilder();
				if(values instanceof List<?>) {
					boolean first = true;
					for(Object object : (List<?>)values) {
						if(first) {
							first = false;
						} else {
							sb.append(separator);
						}
						sb.append(object.toString());
					}
				}
				//logger.debug("template before replacement: \"" + result + "\"");
				logger.debug("replacing vector variable @{{}} with {}", variable, sb.toString());
				result = result.replaceAll("@\\{" + variable + "\\}", sb.toString());
				//logger.debug("template after replacement: \"" + result + "\"");
			}
		}
		return result;
	}
}
