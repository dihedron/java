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

package org.dihedron.ogtl.operators;

import org.dihedron.reflection.Reflector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performa a method invocation on the operand.
 * 
 * @author Andrea Funto'
 */
public class Invoke implements Operator {

	/**
	 * The logger
	 */
	private final static Logger logger = LoggerFactory.getLogger(Invoke.class);
	
	/**
	 * Constructor.
	 * 
	 * @param methodName
	 *   the name of the method to be invoked.
	 */
	public Invoke() {
		logger.info("invoking method");
	}

	
	/**
	 * Invokes the method on the given operand.
	 * 
	 * @param operand
	 *   the <code>Invocation</code> object, containing information about the
	 *   target object and the method to be invoked.
	 * @return 
	 *   the object returned by the method invocation.
	 */
	public Object apply(Object operand) throws Exception {
		assert operand != null : "operand must be a valid object";
		Invocation invocation = (Invocation)operand;		
		return new Reflector().applyTo(invocation.getObject()).invoke(invocation.getMethod());
	}

	/**
	 * Returns a string representation of the <code>Operator</code>.
	 */
	public String toString() {
		return "()";
	}	
}
