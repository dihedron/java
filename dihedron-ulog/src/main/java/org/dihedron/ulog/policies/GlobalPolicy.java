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

import org.dihedron.ulog.Level;
import org.dihedron.ulog.Message;
import org.dihedron.ulog.Log.GlobalInfo;

/**
 * Applies a filtering policy based on the message level, 
 * comparing it against a thread-specific level as per the
 * constructor parameter.
 * 
 * @author Andrea Funto'
 */
public class GlobalPolicy implements Policy {
	
	/**
	 * Constructor.
	 */
	public GlobalPolicy() {		
	}
	
	/**
	 * Constructor.
	 * 
	 * @param level
	 *   the new global reference level.
	 */
	public GlobalPolicy(Level level) {
		GlobalInfo.setLevel(level);
		
	}
	
	/**
	 * Sets the global level.
	 * 
	 * @param level
	 *   the new value for the global level.
	 */
	public void setLevel(String level) {
//		System.out.println("setting global level to " + Level.valueOf(level.trim().toUpperCase()));
		GlobalInfo.setLevel(Level.valueOf(level.trim().toUpperCase()));
	}
	
	/**
	 * Checks if the message level is at least that of the
	 * reference value provided in the constructor.
	 * 
	 * @see 
	 *   org.dihedron.ulog.policies.Policy#accept(org.dihedron.ulog.Message)
	 */
	@Override
	public boolean accept(Message message) {
//		System.out.println("[global] comparing message's " + message.getLevel() + " against global " + GlobalInfo.getLevel() + ": " + (GlobalInfo.getLevel().compareTo(message.getLevel()) <= 0));
		return (GlobalInfo.getLevel().compareTo(message.getLevel()) <= 0);
	}
	
	/**
	 * Performs a deep copy of the object.
	 * 
	 * @return
	 *   a deep copy of the object.
	 */
	@Override
	public GlobalPolicy clone() throws CloneNotSupportedException {
		GlobalPolicy clone = (GlobalPolicy)super.clone();
		return clone;
	}		
}
