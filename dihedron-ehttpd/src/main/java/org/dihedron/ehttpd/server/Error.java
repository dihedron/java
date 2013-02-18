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
package org.dihedron.ehttpd.server;


/**
 * This class wraps a Throwable object and provides some utility methods to
 * compare exceptions to each other in order to ascertain whether a given
 * exception is handled through an URL or not.
 * 
 * @author Andrea Funto'
 */
public class  Error {
	
	/**
	 * The wrapped exception class.
	 */
	private Class<? extends Throwable> clazz;
	
	/**
	 * Constructor.
	 *
	 * @param clazz
	 *   the exception class of this error.
	 */
	public Error(Class<? extends Throwable> clazz) {
		this.clazz = clazz;
	}
	
	public boolean isSuperClassOf(Throwable throwable) {
		if(throwable == null) {
			return false;
		}
		try {
			throwable.getClass().asSubclass(clazz);
			return true;
		} catch(ClassCastException e) {
			return false;
		}
	}
	
	public boolean isSuperClassOf(Class<?> other) {
		if(other == null) {
			return false;
		}
		try {
			other.asSubclass(clazz);
			return true;
		} catch(ClassCastException e) {
			return false;
		}
	}	
}
