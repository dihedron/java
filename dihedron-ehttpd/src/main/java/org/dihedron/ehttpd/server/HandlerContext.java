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
 * The invocation context for handlers.
 * 
 * @author Andrea Funto'
 */
public class HandlerContext {
	
	/**
	 * The thread-local information about the current session.
	 */
	static final ThreadLocal<HandlerContext> CONTEXT = new ThreadLocal<HandlerContext>() {
		
		@Override protected HandlerContext initialValue() {
			return new HandlerContext();		
		}
	};	
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(HandlerContext.class);

	/**
	 * The request dispatcher.
	 */
	private RequestDispatcher dispatcher;
	
	/**
	 * Constructor. 
	 */
	private HandlerContext() {
		logger.debug("instantiating handler context for thread {}", Thread.currentThread().getId());
	}
	
	/**
	 * Returns the request dispatcher.
	 * 
	 * @return
	 *   the <code>RequestDispatcher</code>.
	 */
	public static RequestDispatcher getRequestDispatcher() {
		return CONTEXT.get().dispatcher;
	}
	
	/**
	 * Sets the <code>RequestDispatcher</code> reference.
	 * 
	 * @param dispatcher
	 *   the reference to the <code>RequestDispatcher</code>.
	 */
	public static void setRequestDiaspatcher(RequestDispatcher dispatcher) {
		CONTEXT.get().dispatcher = dispatcher;
	}
}
