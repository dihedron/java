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

package org.dihedron.ehttpd.handlers.request;

import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.server.Request;
import org.dihedron.ehttpd.server.Response;
import org.dihedron.ehttpd.server.Status;
import org.dihedron.ehttpd.server.handlers.BaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Funto'
 */
public class AuthorizationExceptionHandler extends BaseHandler {
	
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationExceptionHandler.class);
	
	/**
	 * Provides a standard error message, reporting the internal
	 * authorisation exception and its stack trace.
	 * 
	 *  @param request
	 *    the request object.
	 *  @param response
	 *    the response object.
	 */	
	protected void service(Request request, Response response) throws ServerException, ApplicationException {
		logger.debug("default authorization handler servicing request...");
		
		response.setStatus(Status.STATUS_401);
		
		response.addContent("<html>");
		response.addContent("<head>");
		response.addContent("<link rel=\"stylesheet\" href=\"digisign.css\" type=\"text/css\" />");

		response.addContent("<title>401: Unauthorized</title>");
		response.addContent("</head>");
		response.addContent("<body><h2 align=\"center\">401: Unauthorized</h2><br/>");

		response.addContent("Sorry, you don't appear to be authorized to access this server. This incident has been reported.");
		response.addContent("</body></html>");				
	}
}
