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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is for testing purposes only: it 
 * returns a fake session identified and will
 * always validate the session.
 * 
 * @author Andrea Funto'
 */
public class FakeSessionGenerator implements SessionGenerator {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(FakeSessionGenerator.class);	

	/**
	 * Retrieves a fake session identifier.
	 * 
	 * @return
	 *   a fake session identifier.
	 * @see 
	 *   org.dihedron.ehttpd.session.SessionGenerator#getValue()
	 */
	//@Override
	public String getValue() {
		return "fakesession";
	}

	/**
	 * Always returns <code>true</code>, effectively validating all
	 * requests. This class is for testing purposes only, it should not
	 * be used in production systems.
	 * 
	 * @param sessionId
	 *   the other session identifier.
	 * @return
	 *   always <code>true</code>. 
	 * @see 
	 *   org.dihedron.ehttpd.session.SessionGenerator#isValid(java.lang.String)
	 */
	//@Override
	public boolean isValid(String sessionId) {
		logger.debug("setting " + sessionId + " as valid without any check");
		return true;
	}
}
