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

/**
 * A policy that ORs its sub-policies.
 * 
 * @author Andrea Funto'
 */
public class Or extends CompoundPolicy {
	
	/**
	 * Default contructor.
	 */
	public Or() {
	}
	
	/**
	 * Constructor.
	 * 
	 * @param policies
	 *   the policies to be ORed together, in order.
	 */
	public Or(Policy... policies) {
		super(policies);
	}
	
	/**
	 * ORs policies together, and returns the compound result
	 * obtained by applying them one after the other to the given
	 * message; for performance reasons, as soon as a policy 
	 * accepts the message, the message is accepted (short-circuit).
	 * 
	 * @param message
	 *   the input message.
	 * @return
	 *   <code>true</code> if at least one policy is satisfied (OR), 
	 *   <code>false</code> if none satisfied.
	 * @see 
	 *   org.dihedron.ulog.policies.Policy#accept(org.dihedron.ulog.Message)
	 */
	@Override
	public boolean accept(Message message) {
		boolean accepted = false;
		int i = 0;
		while(!accepted && i < policies.size()) {
			accepted = accepted || policies.get(i++).accept(message);
		}
		return accepted;
	}
	
	/**
	 * Performs a deep copy of the object.
	 * 
	 * @return
	 *   a deep copy of the object.
	 */
	@Override
	public Or clone() throws CloneNotSupportedException {
		Or clone = (Or)super.clone();
		return clone;
	}	
	
}
