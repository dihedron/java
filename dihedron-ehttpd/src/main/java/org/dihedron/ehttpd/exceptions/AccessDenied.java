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
 * The exception thrown when the client requests a protected resource for which 
 * no access has been granted (yet).
 * 
 * @author Andrea Funto'
 */
public class AccessDenied extends ApplicationException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 2096947190860250582L;

	/**
	 * Constructor.
	 */
	public AccessDenied() {
	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *   the message associated with the exception.
	 */
	public AccessDenied(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * 
	 * @param cause
	 *   the original exception cause.
	 */
	public AccessDenied(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *   the message associated with the exception.
	 * @param cause
	 *   the original exception cause.
	 */
	public AccessDenied(String message, Throwable cause) {
		super(message, cause);
	}
}
