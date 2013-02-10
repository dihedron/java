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


import java.util.ArrayList;
import java.util.List;

import org.dihedron.ulog.extensions.Extension;
import org.dihedron.ulog.extensions.ExtensionPoint;


/**
 * @author Andrea Funto'
 */
public abstract class CompoundPolicy implements Policy, ExtensionPoint {

	/**
	 * The policies to be operated upon.
	 */
	protected List<Policy> policies = new ArrayList<Policy>();
	
	/**
	 * Default constructor.
	 */
	protected CompoundPolicy() {
	}
	
	/**
	 * Constructor.
	 * 
	 * @param policies
	 *   the policies to be operated upon.
	 */
	protected CompoundPolicy(Policy... policies) {
		for(Policy policy: policies) {
			addExtension(policy);
		}
	}
	
	/**
	 * Adds one or more policies to the set to be operated upon.
	 * 
	 * @param extension
	 *   the policies to be operated upon.
	 */
	@Override
	public void addExtension(Extension extension) {
		if(policies != null && extension instanceof Policy) {
			this.policies.add((Policy)extension);
		}
	}
	
	/**
	 * Performs a deep copy of the object.
	 * 
	 * @return
	 *   a deep copy of the object.
	 */
	@Override
	public CompoundPolicy clone() throws CloneNotSupportedException {
		CompoundPolicy clone = (CompoundPolicy) super.clone();
		clone.policies = new ArrayList<Policy>();
		for(Policy policy : this.policies) {
			clone.policies.add(policy.clone());
		}
		return clone;
	}	
}
