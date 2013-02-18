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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Funto'
 */
public class RandomSessionGenerator implements SessionGenerator {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(RandomSessionGenerator.class);
	
	/**
	 * The session id for this object.
	 */
	private String sessionId;
	
	public RandomSessionGenerator() {		
		String hostname;
		String now = new SimpleDateFormat("yyyyMMdd@HH:mm:ss").format(Calendar.getInstance().getTime());
		String random;
		try {			
			hostname = InetAddress.getLocalHost().getHostName();			
		} catch (UnknownHostException e) {
			logger.error("error determining local host name, using default");
			hostname = "unknownhost";
		}
		
		try {
			random = Long.toString(SecureRandom.getInstance("SHA1PRNG").nextLong());
		} catch (NoSuchAlgorithmException e) {
			logger.error("error calculating secure random, using unsecure version");
			random = Long.toString(new Random(System.currentTimeMillis()).nextLong());
		}
		
		sessionId = hostname + "+" + now + "+" + Thread.currentThread().getId() + "+" + random;			
		logger.info("session id: \"" + sessionId + "\"");		
	}
	

	/**
	 * Retrieves the session id for the current
	 * session.
	 * 
	 * @return
	 *   the session identifier for this object.
	 * @see 
	 *   org.dihedron.ehttpd.session.SessionGenerator#getValue()
	 */
	//@Override
	public String getValue() {
		return sessionId;
	}

	/**
	 * Checks whether the given session identifier matches
	 * that of this object.
	 * 
	 * @param sessionId
	 *   the other session identifier.
	 * @return
	 *   whether the session identifier is to be considered valid. 
	 * @see 
	 *   org.dihedron.ehttpd.session.SessionGenerator#isValid(java.lang.String)
	 */
	//@Override
	public boolean isValid(String sessionId) {
		logger.debug("checking " + sessionId + " against current session " + this.sessionId);
		return sessionId != null && sessionId.equalsIgnoreCase(this.sessionId);
	}
}
