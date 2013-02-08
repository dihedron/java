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

import java.util.Map;

import org.dihedron.reflection.ObjectInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Funto'
 */
public class GetProperty implements Operator {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(GetProperty.class);

	/**
	 * The field or method to be accessed.
	 */
	private String property;
	
	/**
	 * Constructor.
	 * 
	 * @param property
	 *   the property or method to be accessed.
	 */
	public GetProperty(String property) {
		this.property = property;
	}

	/**
	 * @see org.dihedron.ogtl.operators.Operator#apply(java.lang.Object)
	 */
	@Override
	public Object apply(Object operand) throws Exception {
		assert operand != null : "operand must be a valid object";
		logger.debug("getting property '{}' from object of class '{}'", property, operand.getClass().getSimpleName());
		ObjectInspector inspector = new ObjectInspector(false).applyTo(operand);
		if(inspector.isMap()) {
			@SuppressWarnings("unchecked") Map<String, ?> map = (Map<String, ?>)operand;
			return map.get(property);
		} else {
			if(inspector.isField(property)) {
				return inspector.getFieldValue(property);
			} else if(inspector.isMethod(property)) {
				return new Invocation(operand, inspector.getMethod(property));
			}
			logger.error("operand of class '{}' has no property '{}'", operand.getClass().getSimpleName(), property);
		}
		return null;
	}
	
	/**
	 * Returns a representation of the operator as a String.
	 */
	public String toString() {
		return "." + property;
	}	
}
