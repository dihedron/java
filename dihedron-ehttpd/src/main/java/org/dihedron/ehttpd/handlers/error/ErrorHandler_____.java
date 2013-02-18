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
package org.dihedron.ehttpd.handlers.error;

import org.dihedron.ehttpd.server.handlers.BaseHandler;

/**
 * The base class for server and application error handlers.
 * 
 * @author Andrea Funto'
 */
public abstract class ErrorHandler_____ extends BaseHandler {
	/**
	 * The exception.
	 */
	protected Exception exception;
	
	/**
	 * Subclasses must implement this method; it is called whenever the dispatcher
	 * is looking for some handler to treat an exception; the first to return
	 * <code>true</code> will handle the error and will stop the search. 
	 * 
	 * @param exception
	 *   the exception being handled.
	 * @return
	 *   <code>true</code> if the handler can treat the exception, <code>false</code>
	 *   otherwise. In the latter case, the search for a handler will continue.
	 */
	public abstract boolean canHandle(Exception exception);
	
	/**
	 * Sets the current exception for reporting.
	 * 
	 * @param exception
	 *   the exception being handled.
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}
}
