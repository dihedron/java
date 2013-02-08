/**
 * Copyright (c) 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Object Graph Traversal Language framework ("OGTL").
 *
 * "OGTL" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "OGTL" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "OGTL". If not, see <http://www.gnu.org/licenses/>.
 */


package org.dihedron.ogtl;

import java.util.regex.Pattern;

/**
 * A class representing a token extracted from the input expression language.
 * Each token has a type (amon an enumerated set of valid values) and a value.
 * 
 * @author Andrea Funto'
 */
public class Token {
	
	/**
	 * The type of tokens extracted from the input expression.
	 * 
	 * @author Andrea Funto'
	 */
	public enum Type {
				
		/**
		 * Tokens of type "INDEX" represent access operations to the n-th element 
		 * in an array or a list; they are expressed either as <code>.3</code> or 
		 * as <code>[3]</code>. This pattern MUST be evaluated before the PROPERTY 
		 * type to ensure proper lexer behaviour..
		 */		
		INDEX("^((?:\\s*)(?:\\[)(?:\\s*)(\\d+)(?:\\s*)(?:\\])|(?:\\s*)(?:\\.)(?:\\s*)(\\d+)(?:[^a-zA-Z_]))"),

		/**
		 * Tokens of type "PROPERTY" represent access operations to methods or fields
		 * of the current object; they are typically expressed as <code>.myField</code>, 
		 * <code>['myField']</code> or <code>["myField"]</code>. 
		 */		
		PROPERTY("^((?:\\s*)(?:\\.?)(?:\\s*)([a-zA-Z_]\\w*)|(?:\\s*)(?:\\[)(?:\\s*)(?:')(?:\\s*)([a-zA-Z_]\\w*)(?:\\s*)(?:')(?:\\s*)(?:\\])|(?:\\s*)(?:\\[)(?:\\s*)(?:\")(?:\\s*)([a-zA-Z_]\\w*)(?:\\s*)(?:\")(?:\\s*)(?:\\]))"),		
		
		/**
		 * Tokens of type "INVOKE" represent method invocations; they are expressed 
		 * by a pair of parentheses, as <code>()</code>.
		 */		
		INVOKE("^(?:\\s*)\\((?:\\s*)\\)");
		
		/**
		 * Private constructor.
		 * 
		 * @param regexp
		 *   the regular expression expressing the token's syntax.
		 */
		private Type(String regexp) {
			this.regexp = regexp;
			this.pattern = Pattern.compile(regexp);
		}
		
		/**
		 * Returns the regular expression representing the type's syntax.
		 * 
		 * @return
		 *   the regular expression representing the type's syntax.
		 */
		public String getRegularExpression() {
			return regexp;
		}
		
		/**
		 * Returns the <code>Pattern</code> object representing the type.
		 * 
		 * @return
		 *   the <code>Pattern</code> object representing the type.
		 */
		public Pattern getPattern() {
			return pattern;
		}
		
		/**
		 * The regular expression for the present type.
		 */
		private String regexp;
		
		/**
		 * The <code>Pattern</code> object for the present type.
		 */
		private Pattern pattern;		
	}

	/**
	 * 
	 */
	public Token(Type type, String value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Returns the token's type.
	 * 
	 * @return
	 *   the token's type.
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Returns the token's value.
	 * 
	 * @return
	 *   the token's value.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns a string representation of a token, including its type and value.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return type.toString() + "<" + value + ">";
	}
	
	/**
	 * The token's type.
	 */
	private Type type;
	
	/**
	 * The token's value.
	 */
	private String value;
}
