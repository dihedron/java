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

import org.dihedron.ehttpd.exceptions.AccessDenied;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.server.Request;
import org.dihedron.ehttpd.server.Response;
import org.dihedron.ehttpd.server.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a standard error page in case of ApplicationExceptions.
 * 
 * @author Andrea Funto'
 */
public class AccessDeniedHandler____ extends ErrorHandler_____ {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(AccessDeniedHandler____.class);
	
	/**
	 * The application exception.
	 */
	private Exception exception;
	
	/**
	 * Constructor.
	 */
	public AccessDeniedHandler____() {
		
	}
	
	/**
	 * @see org.dihedron.ehttpd.handlers.error.ErrorHandler_____#canHandle(java.lang.Exception)
	 */
	@Override
	public boolean canHandle(Exception exception) {
		return exception != null && exception instanceof AccessDenied;
	}		

	/**
	 * Provides a standard error message, reporting the internal
	 * application exception and its stack trace.
	 * 
	 *  @param request
	 *    the request object.
	 *  @param response
	 *    the response object.
	 */
	protected void service(Request request, Response response) throws ServerException {
		logger.debug("default handler servicing request");
		
		response.setStatus(Status.STATUS_400);
		
		response.addContent("<html>");
		response.addContent("<head>");
		response.addContent("<link rel=\"stylesheet\" href=\"uhttpd.css\" type=\"text/css\" />");

		response.addContent("<title>400 Application Error</title>");
		response.addContent("</head>");
		response.addContent("<body><h2 align=\"center\">401: Unauthorised</h2><br>");

		response.addContent("Sorry, you don't appear to be authorised to access the requested resource. This incident has been reported.");
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
		
		/*
		Notifications tls = Notifications.getInstance();
		List<Notification> notifications = tls.getAll();
		if(notifications != null && notifications.size() > 0) {
			response.addContent("The following notifications may be relevant:<p>");
			response.addContent("<table>");
			response.addContent("<th><td>Type</td><td>Message</td><td>Arguments</td></th>");
			
			for (Notification notification : notifications) {
				response.addContent("<tr>");
				response.addContent("<td>");
				response.addContent(notification.getType().toString());
				response.addContent("</td>");
				response.addContent("<td>");
				response.addContent(notification.getMessage());
				response.addContent("</td>");
				response.addContent("<td>");
				String [] args = notification.getArguments();
				if(args != null) {
					StringBuilder sb = new StringBuilder();
					for (String arg : args) {
						sb.append(arg).append(", ");
					}
					response.addContent(sb.toString().substring(0, sb.toString().length() - 1));
				} else {
					response.addContent("-");
				}
				response.addContent("</td>");
				response.addContent("</tr>");								
			}
			response.addContent("</table>");
			
		}
		response.addContent("</div>");
		*/
		response.addContent("</body></html>");			
	}
}
