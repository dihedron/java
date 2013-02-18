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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface is the base for all view handlers.
 * 
 * @author Andrea Funto'
 */
public abstract class AbstractRenderer implements Renderer {
	
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractRenderer.class);
	
	/**
	 * The map of variables to be used for rendering.
	 */
	protected Map<String, Object> variables;
	
	/**
	 * A map of parameters, as extracted from a list
	 * of strings in the form key=value.
	 */
	protected Map<String, String> parameters;
	
	/**
	 * Sets the (optional) parameters for the renderer
	 * class.
	 * 
	 * @param parameters
	 *   the (optional) parameters. 
	 */
	public void setParameters(String [] parameters) {
		this.parameters = new HashMap<String, String>();
		if(parameters != null) {
			for (String parameter : parameters) {				
				String key = null;
				String value = "";
				logger.debug("analysing parameter \"" + parameter + "\"");
				int idx = parameter.indexOf('=');
				if(idx != -1) { 
					key = parameter.substring(0, idx);
					value = parameter.substring(idx + 1);
				} else {
					key = parameter;
				}
				logger.debug("putting key: \"" + key + "\", value: \"" + value + "\"");
				this.parameters.put(key, value);
			}
		}
	}
	
	/**
	 * Sets the variables, as retrieved from the action.
	 * 
	 * @param variables
	 *   the variables retrieved from the action.
	 */
	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
}
