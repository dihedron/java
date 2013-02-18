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

package org.dihedron.ehttpd.exceptions;


/**
 * @author Andrea Funto'
 *
 */
public class InvalidState extends ServerException {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -7137211323377602343L;

	/**
	 * Constructor.
	 */
	public InvalidState() {
	}

	/**
	 * @param message
	 */
	public InvalidState(String message) {
		super(message);
	}
}
