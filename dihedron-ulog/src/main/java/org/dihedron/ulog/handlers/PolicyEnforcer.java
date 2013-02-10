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
import org.dihedron.ulog.extensions.Extension;
import org.dihedron.ulog.extensions.ExtensionPoint;
import org.dihedron.ulog.policies.Policy;

/**
 * @author Andrea Funto'
 */
public class PolicyEnforcer extends WrapperHandler implements ExtensionPoint {

	/**
	 * The handler's unique id.
	 */
	private static final String ID = "policy";
	
	/**
	 * The policy to be enforced.
	 */
	private Policy policy;
	
	/**
	 * Default constructor.
	 */
	public PolicyEnforcer() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param policy
	 *   the policy to be enforced.
	 */
	public PolicyEnforcer(Policy policy) {
		super();
		this.policy = policy;
	}

	/**
	 * Constructor.
	 * 
	 * @param handler
	 *   the wrapped handler.
	 */
	public PolicyEnforcer(MessageHandler handler) {
		super(handler);
	}

	/**
	 * Constructor.
	 * 
	 * @param policy
	 *   the policy to be enforced.
	 * @param handler
	 *   the wrapped handler.
	 */
	public PolicyEnforcer(Policy policy, MessageHandler handler) {
		super(handler);
		this.policy = policy;
	}

	/**
	 * Returns the handler's unique id.
	 * 
	 * @see org.dihedron.ulog.handlers.MessageHandler#getId()
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
	public PolicyEnforcer clone() throws CloneNotSupportedException  {
		PolicyEnforcer clone = (PolicyEnforcer)super.clone();
		clone.policy = this.policy.clone();
		return clone;
	}	
	
	/**
	 * Adds an extension to the set.
	 * 
	 * @param extension
	 *   the extension to add.
	 */
	@Override
	public void addExtension(Extension extension) {
		if(extension instanceof Policy) {
			this.policy = (Policy)extension;
		}
	}

	/* (non-Javadoc)
	 * @see it.bankitalia.sisi.dsvaa.common.logging.handlers.TextMessageHandler#onTextMessage(it.bankitalia.sisi.dsvaa.common.logging.messages.TextMessage)
	 */
	@Override
	public void onMessage(Message message) {
		if(policy.accept(message)) {
//			System.out.println("message accepted: " + message.getText());
			forward(message);
//		} else {
//			System.out.println("message refused: " + message.getText());
		}
	}
}
