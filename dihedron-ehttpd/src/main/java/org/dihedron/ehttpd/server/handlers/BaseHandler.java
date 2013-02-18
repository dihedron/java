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
import org.dihedron.ehttpd.server.Method;
import org.dihedron.ehttpd.server.Request;
import org.dihedron.ehttpd.server.Response;

/**
 * Abstract base class for all request handlers; it provides method-based
 * dispatching and a set of do-nothing methods for HTTP GETs, POSTs etc. 
 * Subclasses can re-implement these methods to provide business logic. 
 * 
 * @author Andrea Funto'
 */
public abstract class BaseHandler implements Handler {
	
	/**
	 * The list of supported <code>Method</code>s.
	 */
	private Method[] methods = Method.DEFAULT_SUPPORTED_METHODS;

	/**
	 * Constructor; by default subclasses using this constructor must be able to 
	 * service the default set of request methods (GET, POST, PUT, DELETE).
	 */	
	protected BaseHandler() {
	}
	
	/**
	 * Constructor.
	 * 
	 * @param methods
	 *   the list of supported methods; if null, the default set of methods 
	 *   is assumed to be supported.
	 */	
	protected BaseHandler(Method... methods) {
		if(methods != null) {
			this.methods = methods;
		}
	}
	
	/**
	 * A method implementing the <code>Handler</code> interface; this method 
	 * provides per-method routing: it calls actual implementations of the
	 * supported methods, and throws an <code>ApplicationException</code>
	 * if the method is not supported. 
	 * 
	 * @see org.dihedron.ehttpd.server.handlers.Handler#handle(org.dihedron.ehttpd.server.Request, org.dihedron.ehttpd.server.Response)
	 */
	public void handle(Request request, Response response) throws ApplicationException, ServerException {
		Method method = request.getHttpQuery().getMethod();
		
		if(!isSupportedMethod(method)) {
			throw new ApplicationException("unsupported method '" + method + "' for request '" + request.getHttpQuery() + "'");
		}
		
		// now dispatch control to the appropriate method, based on the HTTP method
		switch(method) {
		case GET:
			onGet(request, response);
			break;
		case POST:
			onPost(request, response);
			break;
		case PUT:
			onPut(request, response);
			break;
		case DELETE:
			onDelete(request, response);
			break;
		case HEAD:
			onHead(request, response);
			break;
		case OPTIONS:
			onOptions(request, response);
			break;
		case TRACE:
			onTrace(request, response);
			break;
		}
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
	public void onGet(Request request, Response response) throws ServerException, ApplicationException {
		// do nothing
	}
	
	/**
	 * Called when an HTTP POST request is being served.
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
	public void onPost(Request request, Response response) throws ServerException, ApplicationException {
		// do nothing
	}
	
	/**
	 * Called when an HTTP PUT request is being served.
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
	public void onPut(Request request, Response response) throws ServerException, ApplicationException {
		// do nothing
	}
	
	/**
	 * Called when an HTTP DELETE request is being served.
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
	public void onDelete(Request request, Response response) throws ServerException, ApplicationException {
		// do nothing
	}
	
	
	/**
	 * Called when an HTTP OPTIONS request is being served.
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
	public void onOptions(Request request, Response response) throws ServerException, ApplicationException {
		// do nothing
	}

	/**
	 * Called when an HTTP HEAD request is being served.
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
	public void onHead(Request request, Response response) throws ServerException, ApplicationException {
		// do nothing
	}
	
	/**
	 * Called when an HTTP TRACE request is being served.
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
	public void onTrace(Request request, Response response) throws ServerException, ApplicationException {
		// do nothing
	}

	/**
	 * Checks whether the given method is among those supported
	 * by this <code>Handler</code>.
	 * 
	 * @param method
	 *   the HTTP request method.
	 * @return
	 *   <code>true</code> if the method is supported, <code>false</code>
	 *   otherwise.
	 */
	private boolean isSupportedMethod(Method method) {
		for(Method m : methods) {
			if(m == method) {
				return true;
			}
		}
		return false;
	}
}
