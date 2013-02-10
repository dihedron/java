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
import org.dihedron.ulog.extensions.Extension;

/**
 * This is the common interface to all policy objects.
 * 
 * @author Andrea Funto'
 */
public interface Policy extends Cloneable, Extension {
	/**
	 * Checks whether a message should be accepted for logging,
	 * or be discarded. The concrete implementations work in 
	 * combination with a <code>PolicyEnforcer</code>.
	 * 
	 * @param message
	 *   the input message.
	 * @return
	 *   <code>true</code> if the message should be passed on to
	 *   the handler's chain, or <code>false</code> if it was not 
	 *   accepted and should therefore be discarded.
	 */
	boolean accept(Message message);
	
	/**
	 * Performs a deep copy of the object.
	 * 
	 * @return
	 *   a deep copy of the object.
	 */
	Policy clone() throws CloneNotSupportedException ;
}
