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

package org.dihedron.ehttpd.i18n;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides basic support for a message
 * provider.
 * 
 * @author Andrea Funto'
 */
public class MapBasedMessageProvider implements MessageProvider {
	
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MapBasedMessageProvider.class);
	
	/**
	 * The message map.
	 */
	private Map<String, String> messages; 
	
	/**
	 * Constructor.
	 * 
	 * @param messages
	 *   a message map, containing message key and its value, possibly
	 *   with positional place-holders in the form '%1', '%2' (numbering
	 *   starting at 1).
	 */
	public MapBasedMessageProvider(Map<String, String> messages) {
		assert(messages != null);
		this.messages = messages;
	}

	/**
	 * Returns a bound message from the internal message
	 * map, possibly substituting positional variables according
	 * to values provided as input.
	 * 
	 * @param key
	 *   the message key
	 * @param args
	 *   the optional list of positional arguments.
	 * @return
	 *   the bound message.
	 * @see 
	 *   org.dihedron.i18n.MessageProvider#getMessage(java.lang.String, java.lang.String[])
	 */
	//@Override
	public String getMessage(String key, String... args) {
		String message = null;
		logger.debug("retrieving message with key '{}'...", key);
		if(messages.containsKey(key)) {
			 message = messages.get(key);
			if(args != null && args.length > 0) {
				for(int i = 0; i < args.length; ++i) {						
					message = message.replaceFirst("%" + (i + 1), args[i]);
				}
			}

		}
		return message;
	}

}
