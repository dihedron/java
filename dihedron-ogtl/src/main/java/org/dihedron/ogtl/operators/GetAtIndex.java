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

import org.dihedron.reflection.ObjectInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the retrieval of the n-th element on an array or an object
 * implementing the <code>List</code> interface. 
 * 
 * @author Andrea Funto'
 */
public class GetAtIndex implements Operator {
	
	/**
	 * The logger
	 */
	private final static Logger logger = LoggerFactory.getLogger(GetAtIndex.class);
	
	/**
	 * The index of the element to be retrieved. 
	 */
	private int index = -1;
	
	/**
	 * Constructor.
	 * 
	 * @param index
	 *   the index of the element to be accessed.
	 */
	public GetAtIndex(String index) {
		logger.info("getting array element at index '{}'", index);
		this.index = Integer.parseInt(index);
	}
	
	/**
	 * Applies the operator to the given input parameter.
	 * 
	 * @param operand
	 *   the object to which the operator will be applied.
	 */
	public Object apply(Object operand) throws Exception {		
		assert operand != null : "operand must be a valid object";
		return new ObjectInspector().applyTo(operand).getElementAtIndex(index);
	}
	
	/**
	 * Returns a representation of the operator as a String.
	 */
	public String toString() {
		return "[" + index + "]";
	}
}
