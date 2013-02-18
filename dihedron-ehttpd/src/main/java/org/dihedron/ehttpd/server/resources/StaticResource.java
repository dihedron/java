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
package org.dihedron.ehttpd.server.resources;

/**
 * This interface is implemented by classes providing
 * access to a static resource Content-Type and data.
 */
public interface StaticResource {
	
	/**
	 * Method to be implemented by subclasses to provide
	 * the data Content-Type.
	 * 
	 * @return
	 *   the resource Content-Type.
	 */
	String getContentType();
	
	/**
	 * Method to be implemented by subclasses to provide
	 * the resource data.
	 * 
	 * @return
	 *   the resource data.
	 */
	byte [] getData();	
}
