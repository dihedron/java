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


/**
 * This interface represents the base class for all message and 
 * output stream manipulators, as well as for service components in 
 * the processing stack; the stack of <code>MessageHandler</code>s 
 * is responsible as a whole of the management, formatting, filtering 
 * and writing of the incoming logging messages.
 * 
 * @author Andrea Funto'
 */
public interface MessageHandler extends Cloneable {
	
	/**
	 * Returns the handler's unique identifier.
	 * 
	 * @return
	 *   the handler's identifier.
	 */
	String getId();
		
	/**
	 * Invoked whenever a new message is available for logging.
	 * 
	 * @param message
	 *   the message to be processed.
	 */
	void onMessage(Message message);
	
	/**
	 * Performs a deep copy of the object.
	 * 
	 * @return
	 *   a deep copy of the object.
	 */
	MessageHandler clone() throws CloneNotSupportedException ;
}
