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
package org.dihedron.ehttpd.server.handlers.impl;

import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.dihedron.ehttpd.exceptions.ResourceNotFoundException;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.server.Request;
import org.dihedron.ehttpd.server.Response;
import org.dihedron.ehttpd.server.handlers.Handler;
import org.dihedron.ehttpd.server.resources.StaticResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class supports serving of static resources (like CSS 
 * style sheets, static HTML pages etc.); it supports only 
 * GET requests, and throws on other verbs.
 * 
 * @author Andrea Funto'
 */
public class ResourceRequestHandler implements Handler {
	
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ResourceRequestHandler.class);
	
	/**
	 * Static resource provider.
	 */
	private StaticResource resource;
	
	/**
	 * Constructor.
	 * 
	 * @param resource
	 *   a static resource provider, which is in charge of
	 *   providing a MIME-type description of the resource
	 *   and the resource data, as a raw byte array.
	 */
	public ResourceRequestHandler(StaticResource resource) {
		this.resource = resource;
	}

	/**
	 * Serves a static resource to the client.
	 * 
	 * @see org.dihedron.ehttpd.server.handlers.Handler#onGet(it.bankitalia.sisi.dsvaa.httpd.Request, it.bankitalia.sisi.dsvaa.httpd.Response)
	 */
	@Override
	public void handle(Request request, Response response) throws ApplicationException, ServerException {
		String type = resource.getContentType();
		byte [] data = resource.getData();
		
		if (data != null) {
			response.setHeader("Content-Type", type);
			response.addContent(data);
		} else {
			logger.error("resource not found, throwing exception");
			throw new ResourceNotFoundException();
		}
	}
}
