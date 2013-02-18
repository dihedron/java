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
 * This is the base class for internal, server
 * runtime errors; these errors are not related
 * to application logic faults or invalid requests,
 * which should be handled differently.
 * 
 * @author Andrea Funto'
 */
public class ServerException extends Exception {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -3461399622344219196L;

	/**
	 * Constructor.
	 */
	public ServerException() {
	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *   the message associated with the exception.
	 */
	public ServerException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cause
	 *   the original exception cause.
	 */
	public ServerException(Throwable cause) {
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
	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
