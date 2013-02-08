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

import java.util.List;

import org.dihedron.ogtl.operators.GetAtIndex;
import org.dihedron.ogtl.operators.GetProperty;
import org.dihedron.ogtl.operators.Invoke;
import org.dihedron.ogtl.operators.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Funto'
 */
public class ExpressionCompiler {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(ExpressionCompiler.class);

	/**
	 * The input string parser.
	 */
	private Lexer lexer;
	
	/**
	 * Constructor.
	 */
	public ExpressionCompiler() {
		logger.debug("instantiating expression compiler");
		lexer = new Lexer();
	}
	
	public Expression compile(String input) throws LexerException {
		logger.info("compiling expression '" + input + "'");
		List<Token> tokens = lexer.process(input);
		Expression expression = new Expression();
		for(Token token : tokens) {
			Operator operator = makeOperator(token);
			expression.add(operator);
		}
		return expression;
	}

	/**
	 * Creates the operator corresponding to the given input token.
	 * 
	 * @param token
	 *   the input token containing information about the operator
	 *   to be instantiated.
	 * @return
	 *   the new operator object.
	 */
	private Operator makeOperator(Token token) {
		switch(token.getType()) {
		case PROPERTY:
			logger.debug("creating operator GetProperty for: {}", token);
			return new GetProperty(token.getValue());
		case INDEX:
			logger.debug("creating operator GetAtIndex for: {}", token);
			return new GetAtIndex(token.getValue());
		case INVOKE:
			logger.debug("creating operator Invoke for: {}", token);
			return new Invoke();
		}
		logger.error("invalid input token: {}", token);
		return null;
	}
}
