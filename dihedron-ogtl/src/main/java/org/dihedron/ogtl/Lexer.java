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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.dihedron.ogtl.Token.Type;
import org.dihedron.strings.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A lexer for input expressons; this class will extract token from the input
 * expression and rturn a list of typed tokens along with their value.
 * 
 * @author Andrea Funto'
 */
public class Lexer {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Lexer.class);
	
	/**
	 * Constructor.
	 */
	public Lexer() {
		logger.debug("creating new lexer");
	}
			
	/**
	 * Parses an expression and returns the chain of tokens extracted from the 
	 * input expression.
	 *  
	 * @param string
	 *   the input expression string.
	 * @return
	 *   the list of tokens extracted from the input.
	 */	
	public List<Token> process(String expression) throws LexerException {		
		List<Token> tokens = new ArrayList<Token>();
		while(expression != null && expression.trim().length() > 0) {
			logger.trace("------------------------------------------------");
			logger.trace("expression is '" + expression + "'");
			boolean found = false;
			for(Type type : Type.values()) {
				Matcher matcher = type.getPattern().matcher(expression);
				if(matcher.find()) {
					logger.trace("matched with '" + type + "'");
					found = true;
					String value = null;
					switch(type) {
					case PROPERTY: value = Strings.firstValidOf(matcher.group(2), matcher.group(3), matcher.group(4)); break;
						//for(int i = 0; i <= matcher.groupCount(); ++i) System.out.println(" >>> " + i + "'" + matcher.group(i) + "'");
							
//						break;
					case INDEX:	value = matcher.group(2) != null ? matcher.group(2) : matcher.group(3);	break;
					case INVOKE: break;						
					}
					Token token = new Token(type, value);
					logger.trace("adding token {}", token);
					tokens.add(token);
					// bump expression
					expression = expression.substring(matcher.end()).trim();
					break;
				}
				
			}
			if(!found) {
				throw new LexerException("invalid token", expression);
			}			
		}
		return tokens;
	}
}
