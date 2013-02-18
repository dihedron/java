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
package org.dihedron.ehttpd.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Funto'
 */
public enum Method {
	
	/**
	 * The GET HTTP method.
	 */
    GET ("GET"),
    
    /**
     * The POST HTTP method.
     */
    POST("POST"),
    
    /**
     * The PUT HTTP method.
     */
    PUT("PUT"),
    
    /**
     * The DELETE HTTP method.
     */
    DELETE("DELETE"),
    
    /**
     * The OPTIONS HTTP method.
     */
    OPTIONS("OPTIONS"),
    
    /**
     * The TRACE HTTP method.
     */
    TRACE("TRACE"),

    /**
     * The HEAD HTTP method.
     */
    HEAD("HEAD");
	
    /**
     * The logger.
     */
	private static Logger logger = LoggerFactory.getLogger(Method.class);

	/**
	 * A string representing the HTTP method.
	 */
	private String method; 
	
	/**
	 * Returns the appropriate HTTP method given its string representation.
	 *  
	 * @param method
	 *   a string representing the HTTP method.
	 * @return
	 *   a member of the enumeration, or null.
	 */
	public static Method makeMethod(String method) {
		logger.debug("creating method object for '" + method + "'");
		if(method != null && method.length() > 0) {
			if(method.trim().equals("GET")) {
				logger.debug("method is 'GET'");
				return GET;
			} else if(method.trim().equals("POST")) {
				logger.debug("method is 'POST'");
				return POST;
			} else if(method.trim().equals("PUT")) {
				logger.debug("method is 'PUT'");
				return PUT;
			} else if(method.trim().equals("DELETE")) {
				logger.debug("method is 'DELETE'");
				return DELETE;
			} else if(method.trim().equals("HEAD")) {
				logger.debug("method is 'HEAD'");
				return HEAD;
			} else if(method.trim().equals("TRACE")) {
				logger.debug("method is 'TRACE'");
				return TRACE;
			} else if(method.trim().equals("OPTIONS")) {
				logger.debug("method is 'OPTIONS'");
				return OPTIONS;
			}
		}
		logger.error("unsupported method");
		return null;
	}
	
	/**
	 * Private constructor.
	 * 
	 * @param method
	 *   a string representing the method.
	 */
	private Method(String method) {
		this.method = method;
	}
	
	/**
	 * Returns a string representation of the HTTP method.
	 * 
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return method;
	}
	
	/**
	 * An array of all HTTP methods.
	 */
	public static final Method[] DEFAULT_SUPPORTED_METHODS = { GET, POST, PUT, DELETE };
}
