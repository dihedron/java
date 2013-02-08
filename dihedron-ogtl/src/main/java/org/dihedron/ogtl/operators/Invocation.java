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

import java.lang.reflect.Method;

/**
 * Objects of this class represent an abstraction of a method invocation: they 
 * contain a reference to the target object and to the method to be invoked.
 * 
 * @author Andrea Funto'
 */
public class Invocation {

	/**
	 * The object on which the method is to be invoked.
	 */
	private Object object;
	
	/**
	 * The method to be invoked.
	 */
	private Method method;
	
	/**
	 * Constructor.
	 * 
	 * @param object
	 *   the object on which the method is to be invoked.
	 * @param
	 *   the method to be invoked.
	 */
	public Invocation(Object object, Method method) {
		this.object = object;
		this.method = method;
	}
	
	/**
	 * Returns the object on which the method is to be invoked.
	 * 
	 * @return
	 *   the obejct on which the method is to be invoked.
	 */
	public Object getObject() {
		return object;
	}
	
	/**
	 * Returns the method to be invoked.
	 * 
	 * @return
	 *   the method to be invoked.
	 */
	public Method getMethod() {
		return method;
	}
}
