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

/**
 * Contains information relevant for creating a new renderer;
 * it is provided in the action-to-renderer mapping in the
 * action class annotation.
 * 
 * @author Andrea Funto'
 *
 */
public class RendererInfo {
	
	/**
	 * The name of the renderer class.
	 */
	private String className;
	
	/**
	 * The renderer parameters, as specified in the
	 * action annotation.
	 */
	private String [] parameters;
	
	/**
	 * Constructor.
	 * 
	 * @param className
	 *   the name of the renderer class.
	 */
	public RendererInfo(String className) {
		setClassName(className);
	}

	/**
	 * Constructor.
	 * 
	 * @param className
	 *   the name of the renderer class.
	 * @param parameters
	 *   the parameters, as specified in the action annotation.
	 */
	public RendererInfo(String className, String [] parameters) {
		setClassName(className);
		setParameters(parameters);
	}

	/**
	 * Retrieves the class name of the renderer.
	 * 
	 * @return
	 *   the class name of the renderer.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Sets the class name of the renderer.
	 * 
	 * @param className
	 *   the class name of the renderer.
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Retrieves the parameters, as specified in the action
	 * annotation.
	 * 
	 * @return
	 *   the parameters, as specified in the action annotation.
	 */
	public String[] getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters, as specified in the action annotation.
	 * 
	 * @param parameters
	 *   the parameters, as specified in the action annotation.
	 */
	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Returns a string representation of the object.
	 * 
	 *  @return
	 *    a string representation of the object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(className);
		if(parameters != null) {
			boolean first = true;
			sb.append( "{ " );
			for (String parameter : parameters) {
				if(first) {
					first = false;
					sb.append(parameter);
				} else {
					sb.append(", ").append(parameter);
				}
			}
			sb.append( " }" );
		}
		return sb.toString();
	}
}
