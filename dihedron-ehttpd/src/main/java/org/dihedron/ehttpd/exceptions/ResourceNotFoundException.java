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
package org.dihedron.ehttpd.exceptions;

/**
 * This class represents the classic "404 Page Not Found", and is thrown whenever 
 * a request is made that can not be fulfilled by the application because the
 * request it refers to is not found on the server.
 * 
 * @author Andrea Funto'
 */
public class ResourceNotFoundException extends ApplicationException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -574243297639436372L;

	/**
	 * Constructor.
	 */
	public ResourceNotFoundException() {
	}

	/**
	 * Constructor.
	 *
	 * @param message
	 *   the error message.
	 */
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
