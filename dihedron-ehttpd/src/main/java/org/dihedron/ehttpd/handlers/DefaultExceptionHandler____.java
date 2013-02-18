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
package org.dihedron.ehttpd.handlers;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.server.HttpSessionKeys;
import org.dihedron.ehttpd.server.Method;
import org.dihedron.ehttpd.server.Request;
import org.dihedron.ehttpd.server.Response;
import org.dihedron.ehttpd.server.Status;
import org.dihedron.ehttpd.server.handlers.BaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Funto'
 */
public class DefaultExceptionHandler____ extends BaseHandler {
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler____.class);

	/**
	 * Default constructor.
	 */
	public DefaultExceptionHandler____() {
	}

	/**
	 * Constructor.
	 * 
	 * @param methods
	 *   the list of supported HTTP methods.
	 */
	public DefaultExceptionHandler____(Method... methods) {
		super(methods);
	}
	
	/**
	 * Called when an HTTP GET request is being served.
	 * 
	 * @param request
	 *   an object containing the HTTP request parameters.
	 * @param response
	 *   the response object.
	 * @throws ServerException
	 *   if the server experiences an error.
	 * @throws ApplicationException
	 *   if an application error is triggered.
	 */
	public void onGet(Request request, Response response) throws ServerException {
		logger.trace("default handling for exceptions");
		
		Exception exception = (Exception)request.getParameter(HttpSessionKeys.EXCEPTION); 
		
		if(exception instanceof ServerException) {
			response.setStatus(Status.STATUS_500);
		} else if(exception instanceof ApplicationException) {
			response.setStatus(Status.STATUS_400);
		}
		
		response.addContent("<html>");
		response.addContent("<head>");
		response.addContent("<link rel=\"stylesheet\" href=\"uhttpd.css\" type=\"text/css\" />");

		if(exception instanceof ServerException) {
			response.addContent("<title>500 Internal Server Error</title>");
		} else if(exception instanceof ApplicationException) {
			response.addContent("<title>400 Application Error</title>");
		}
		response.addContent("</head>");
		response.addContent("<body>");

		if(exception instanceof ServerException) {
			response.addContent("<h2>500: Internal Server Error</h2><br>");
			response.addContent("Sorry, the server has experienced an unexpected internal error. This is usually due to a misconfiguration, a temporary lack of resources, or a bug.<br>");
		} else if(exception instanceof ApplicationException) {
			response.addContent("<h2>400: Application Error</h2><br>");			
			response.addContent("Sorry, the application has experienced an unexpected error. This is usually due to a bug in the application.<br>");
		}
		
		if(exception != null) {
			response.addContent("The following error was reported:");	
		
			response.addContent("<div>");
			response.addContent("<pre>");
	
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter writer = new PrintWriter(baos);		
			exception.printStackTrace(writer);
		
			writer.flush();
			response.addContent(baos.toByteArray());
		
			response.addContent("</pre>");
			response.addContent("</div>");
		}
		response.addContent("</body>");
		response.addContent("</html>");		
	}	
}
