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
package org.dihedron.ehttpd.renderers;


import java.util.Map;

import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.server.Response;

/**
 * This interface is the base for all view handlers (resources).
 * 
 * @author Andrea Funto'
 */
public interface Renderer {
	
	/**
	 * Sets the (optional) parameters for the renderer
	 * class.
	 * 
	 * @param parameters
	 *   the (optional) parameters.
	 */
	void setParameters(String [] parameters);
	
	/**
	 * Sets the variables, as retrieved from the action.
	 * 
	 * @param variables
	 *   the variables retrieved from the action.
	 */
	void setVariables(Map<String, Object> variables);
	
	/**
	 * Renders the contents of the response to the client.
	 * 
	 * @param response
	 *   the channel to the client.
	 * @throws ApplicationException
	 * @throws ServerException
	 */
	void render(Response response) throws ApplicationException, ServerException;
}
