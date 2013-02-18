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

import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.server.Request;
import org.dihedron.ehttpd.server.Response;
import org.dihedron.ehttpd.server.Status;
import org.dihedron.ehttpd.server.handlers.BaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a standard error page in case of ApplicationExceptions.
 * 
 * @author Andrea Funto'
 */
public class MissingResourceHandler____ extends BaseHandler {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(MissingResourceHandler____.class);
	
	/**
	 * Constructor.
	 */
	public MissingResourceHandler____() {
		logger.debug("instantiating \"page not found\" handler");
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
		
		response.setStatus(Status.STATUS_404);
		response.setHeader("Content-Type", "text/html");
		
		response.addContent("<html>");
		response.addContent("<head>");
		response.addContent("<link rel=\"stylesheet\" href=\"uhttpd.css\" type=\"text/css\" />");

		response.addContent("<title>404 Page Not Found</title>");
		response.addContent("</head>");
		response.addContent("<body><h2 align=\"center\">404: Page Not Found</h2><br/>");

		response.addContent("Sorry, the resource you're looking for (<em>" + request.getHttpQuery() + "</em>) could not ");
		response.addContent("be located on this server; checking the request URL or the parameters may help you fix this ");
		response.addContent("problem. If the problem persists, contact the system administrator. ");
		
		response.addContent("<p>");
		
		response.addContent("<h3 align=\"center\">Request headers</h3><br/>");
		response.addContent("<table align=\"center\" class=\"reference\">");
		response.addContent("<thead><tr><th>Name</th><th>Value</th></tr></thead><tbody>");		
		for(String header : request.getHttpQuery().getHeaders().keySet()) {
			response.addContent("<tr>");
			response.addContent("<td align=\"center\">" + header + "</td>");
			response.addContent("<td align=\"center\">" + request.getHttpQuery().getHeaders().get(header) + "</td>");
			response.addContent("</tr>");
		}
		response.addContent("</tbody></table>");
		
		response.addContent("<p>");
		
		if(request.getHttpQuery().hasParameters()) {
			response.addContent("<h3 align=\"center\">Request parameters</h3><br/>");
			response.addContent("<table align=\"center\" class=\"reference\">");
			response.addContent("<thead><tr><th>Name</th><th>Value</th></tr></thead><tbody>");		
			for(String parameter : request.getHttpQuery().getParameterKeys()) {
				response.addContent("<tr>");
				response.addContent("<td align=\"center\">" + parameter + "</td>");
				response.addContent("<td align=\"center\">" + request.getHttpQuery().getParameter(parameter) + "</td>");
				response.addContent("</tr>");
			}
			response.addContent("</tbody></table>");
		}		
		
		response.addContent("</body></html>");				
	}}
