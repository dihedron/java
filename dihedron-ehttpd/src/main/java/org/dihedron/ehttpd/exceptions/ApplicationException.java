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
 * This class represents the base for any exception thrown by the applications 
 * running in the server; it can be handled by the application, if it manages
 * to register an appropriate application error handler that declares the 
 * error exception subclass as "managed", otherwise it is handled by the error
 * handler of last resort, provided by the server.
 * 
 * @author Andrea Funto'
 */
public class ApplicationException extends Exception {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -4724877475019039670L;
	
	/**
	 * Constructor.
	 */
	public ApplicationException() {
	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *   the message associated with the error.
	 */
	public ApplicationException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * 
	 * @param cause
	 *   the original cause of this problem
	 */
	public ApplicationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *   the message associated with the error.
	 * @param cause
	 *   the original cause of this problem
	 */
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}
}
