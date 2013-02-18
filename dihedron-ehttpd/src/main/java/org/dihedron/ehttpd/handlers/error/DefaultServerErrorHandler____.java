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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.server.Request;
import org.dihedron.ehttpd.server.Response;
import org.dihedron.ehttpd.server.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Funto'
 */
public class DefaultServerErrorHandler____ extends ErrorHandler_____ {
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(DefaultServerErrorHandler____.class);

	/**
	 * Always returns <code>true</code>, as this is the handler of last resort.
	 *  
	 * @see org.dihedron.ehttpd.handlers.error.ErrorHandler_____#canHandle(java.lang.Exception)
	 */
	@Override
	public boolean canHandle(Exception exception) {
		return true;
	}	
	
	/**
	 * @see org.dihedron.ehttpd.server.handlers.BaseHandler#service(org.dihedron.ehttpd.server.Request, org.dihedron.ehttpd.server.Response)
	 */
	@Override
	protected void service(Request request, Response response) throws ServerException, ApplicationException {
		logger.trace("default handling for server exception");
		response.setStatus(Status.STATUS_500);
		
		response.addContent("<html>");
		response.addContent("<head>");
		response.addContent("<link rel=\"stylesheet\" href=\"uhttpd.css\" type=\"text/css\" />");

		response.addContent("<title>500 Internal Server Error</title>");
		response.addContent("</head>");
		response.addContent("<body>");
		response.addContent("<h2>500: Internal Server Error</h2><br>");

		response.addContent("Sorry, the server has experienced an unexpected internal error. This is usually due to a misconfiguration, a temporary lack of resources or a bug.<br>");
		if(exception != null) {
			response.addContent("The following error was reported:<p>");	
		
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
		
	}
}
