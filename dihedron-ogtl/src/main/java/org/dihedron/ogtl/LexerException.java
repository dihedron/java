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

/**
 * An exception thrown by the Lexer to indicate that the input expression is
 * not valid.
 * 
 * @author Andrea Funto'
 */
public class LexerException extends Exception {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 2401740903408587101L;

	/**
	 * @param message
	 *   the exception message.
	 * @param invalidToken
	 *   the next, invalid token.
	 */
	public LexerException(String message, String invalidToken) {
		super(message);
		this.invalidToken = invalidToken;
	}
	
	/**
	 * Returns the next, invalid token from the input expression..
	 * 
	 * @return
	 *   the next, invalid token from the input expression.
	 */
	public String getInvalidToken() {
		return invalidToken;
	}
	/**
	 * The next, invalid token from the input expression.
	 */
	private String invalidToken;
}
