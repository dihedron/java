/**
 * Copyright (c) 2012, 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the uLog library ("uLog").
 *
 * "uLog" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "uLog" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "uLog". If not, see <http://www.gnu.org/licenses/>.
 */
package org.dihedron.ulog.handlers;

import org.dihedron.ulog.Message;
import org.dihedron.ulog.Log.SessionInfo;

/**
 * This class implements a more advanced formatter, one which is
 * aware of user- or session-specific information in the thread
 * locale storage and takes it into account when formatting the 
 * messages.
 * 
 * @author Andrea Funto'
 */
public class SessionFormatter extends WrapperHandler {

	/**
	 * The <code>Writer</code>'s unique id.
	 */
	private final static String ID = "session-formatter";
	
	/**
	 * Default constructor.
	 */
	public SessionFormatter() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param handler
	 *   the wrapped handler.
	 *   
	 */
	public SessionFormatter(MessageHandler handler) {
		super(handler);
	}
	
	/**
	 * @see 
	 *   org.dihedron.ulog.handlers.MessageHandler#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}
	
	/**
	 * Performs a deep copy of the object.
	 * 
	 * @return
	 *   a deep copy of the object.
	 */
	@Override
	public SessionFormatter clone() throws CloneNotSupportedException  {
		SessionFormatter clone = (SessionFormatter)super.clone();
		return clone;
	}	
	
	/**
	 * If user-specific information is available in the thread
	 * local storage, it uses it to prepend to the message text
	 * the session id; otherwise it simply forwards the message 
	 * as is.  
	 */
	@Override
	public void onMessage(Message message) {
		if(SessionInfo.getId() != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("[session: ").append(SessionInfo.getId()).append("] ").append(message.getText());
			message.setText(sb.toString());			
		}
		forward(message);		
	}
}
