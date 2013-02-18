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

package org.dihedron.ehttpd.actions;

import org.dihedron.ehttpd.server.Request;
import org.dihedron.ehttpd.server.Response;

/**
 * Base class for all actions needing awareness of the request and
 * response object and supporting validation.
 * 
 * @author Andrea Funto'
 */
public abstract class BaseAction 
	implements Action, ModelAware, RequestAware, ResponseAware  {
	
	//@Override
	public void setRequest(Request request) {
		this.request = request;
		
	}

	//@Override
	public void setResponse(Response response) {
		this.response = response;
	}
	
	//@Override
	public void setModel(Object model) {
		this.model = model;
	}
	
	/**
	 * The request object.
	 */
	protected Request request;
	
	/**
	 * The response object.
	 */
	protected Response response;
	
	/**
	 * The model object.
	 */
	protected Object model;
}
