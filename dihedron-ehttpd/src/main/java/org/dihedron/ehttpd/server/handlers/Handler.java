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

package org.dihedron.ehttpd.server.handlers;


import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.server.Request;
import org.dihedron.ehttpd.server.Response;

/**
 * Interface to be implemented by all classes dedicated to serve user requests.
 * 
 * @author Andrea Funto'
 */
public interface Handler {
	
	/**
	 * All subclasses must implement this method to support request handling.
	 * 
	 * @param request
	 *   an object wrapping the request.
	 * @param response
	 *   an object wrapping the response.
	 * @throws ServerException
	 *   if an internal server error occurs.
	 * @throws ApplicationException
	 *   if an application error or misconfiguration occurs.
	 */	
	void handle(Request request, Response response) throws ApplicationException, ServerException;

}
