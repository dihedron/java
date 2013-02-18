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
package org.dihedron.ehttpd.server.session;

/**
 * Represents the base functionalities of a Session
 * identifier generator and checker.
 * 
 * @author Andrea Funto'
 */
public interface SessionGenerator {
	/**
	 * Retrieves the session id for the current
	 * session.
	 * 
	 * @return
	 *   the session identifier for this object.
	 */	
	String getValue();
	
	/**
	 * Checks whether the given session identifier matches
	 * that of this object.
	 * 
	 * @param sessionId
	 *   the other session identifier.
	 * @return
	 *   whether the session identifier is to be considered valid.
	 */	
	boolean isValid(String sessionId);
}
