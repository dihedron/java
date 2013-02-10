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
package org.dihedron.ulog.policies;

import org.dihedron.ulog.Message;
import org.dihedron.ulog.Log.SessionInfo;

/**
 * @author Andrea Funto'
 */
public class SessionPolicy implements Policy {

	/* (non-Javadoc)
	 * @see it.bankitalia.sisi.dsvaa.common.logging.policies.Policy#accept(it.bankitalia.sisi.dsvaa.common.logging.messages.TextMessage)
	 */
	@Override
	public boolean accept(Message message) {
//		System.out.println("[session] comparing message's " + message.getLevel() + " against session " + SessionInfo.getLevel() + ": " + (SessionInfo.getLevel().compareTo(message.getLevel()) <= 0));
		return (SessionInfo.getLevel().compareTo(message.getLevel()) <= 0);
	}
	
	
	/**
	 * Performs a deep copy of the object.
	 * 
	 * @return
	 *   a deep copy of the object.
	 */
	@Override
	public SessionPolicy clone() throws CloneNotSupportedException {
		SessionPolicy clone = (SessionPolicy)super.clone();
		return clone;
	}		
}
