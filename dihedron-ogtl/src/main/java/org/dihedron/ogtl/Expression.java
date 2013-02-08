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

import org.dihedron.ogtl.operators.GetAtIndex;
import org.dihedron.ogtl.operators.Operator;
import org.dihedron.reflection.ObjectInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An object graph traversal expression; by applying the expression to a root
 * object, it will traverse the object graph and return the final navigation 
 * point.
 * 
 * @author Andrea Funto'
 */
public class Expression {
	
	/**
	 * The logger
	 */
	private final static Logger logger = LoggerFactory.getLogger(Expression.class);
	
	/**
	 * The list of operators comprising this expression.
	 */
	private List<Operator> operators = new ArrayList<Operator>();
	
	/**
	 * Constructor.
	 */
	public Expression() {
	}
	
	/**
	 * Adds an operator to the current expression.
	 * 
	 * @param operator
	 *   the a non-null operator.
	 * @return
	 *   the object itself, for chaining.
	 */
	public Expression add(Operator operator) {
		if(operator != null) {
			logger.trace("adding operator '{}' to expression", operator);
			operators.add(operator);
		} else {
			logger.error("null operator in input");
		}
		return this;
	}
	
	/**
	 * Evaluates the expression on the given root operand.
	 * 
	 * @param operand
	 *   the evaluation starting point.
	 * @return
	 *   the result of the evaluation.
	 * @throws Exception
	 */
	public Object apply(Object operand) throws Exception {
		logger.debug("applying expression '{}' to object of class '{}'", this.toString(), operand.getClass().getSimpleName());
		
		if(operand != null && !operators.isEmpty()) {			
			ObjectInspector inspector = new ObjectInspector();
			
			for(Operator operator : operators) {
				logger.debug("applying operator '{}' to object '{}'", operator.toString(), operand.getClass().getSimpleName());
				inspector.applyTo(operand);
				if(!(operator instanceof GetAtIndex) && (inspector.isArray() || inspector.isList())) {
					List<Object> results = new ArrayList<Object>();
					int size = inspector.getArrayLength();
					for(int i = 0; i < size; ++i) {
						operand = inspector.getElementAtIndex(i);
						Object result = operator.apply(operand);
						ObjectInspector insp = new ObjectInspector();
						insp.applyTo(result);
						if(insp.isArray() || insp.isList()) {
							int resultSize = insp.getArrayLength();
							for(int j = 0; j < resultSize; ++j) {
								results.add(insp.getElementAtIndex(j));
							}
						} else {
							results.add(result);
						}
					}
					operand = results;
				} else {
					operand = operator.apply(operand);
				}
			}
		}		
		return operand;
	}
	
	/**
	 * Returns a string representation of the expression.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(!operators.isEmpty()) { 
			sb.append("<object>");
			for(Operator operator : operators) {
				sb.append(operator.toString()); 
			}
			sb.append(";");
		} else {
			sb.append("<empty expression>");
		}
		return sb.toString();
	}
}
