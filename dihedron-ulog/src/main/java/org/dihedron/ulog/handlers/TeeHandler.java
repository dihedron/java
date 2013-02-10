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


import java.util.ArrayList;
import java.util.List;

import org.dihedron.ulog.Message;



/**
 * This handler splits the control flow in N, by forwarding
 * it in a loop to all registered sub-handlers.
 * 
 * @author Andrea Funto'
 */
public class TeeHandler implements MessageHandler {

	/**
	 * The handler's unique identifier.
	 */
	private final static String ID = "tee";
	
	/**
	 * The registered sub-handlers.
	 */
	private List<MessageHandler> handlers = new ArrayList<MessageHandler>();
	
	/**
	 * Constructor.
	 * 
	 * @param currentHandler
	 */
	public TeeHandler() {		
	}

	/**
	 * Constructor.
	 * 
	 * @param currentHandler
	 */
	public TeeHandler(MessageHandler... handlers) {
		for(MessageHandler handler : handlers) {
			this.handlers.add(handler);
		}
	}
	
	/**
	 * Adds a new handler to the registered set.
	 * 
	 * @param handler
	 *   the new handler.
	 * @return
	 *   the object itself, for chaining.
	 */
	public TeeHandler wrapAround(MessageHandler handler) {		
		assert(handlers != null);
//		System.out.println("TEE wrapping around " + handler.getClass().getSimpleName());
		this.handlers.add(handler);
		return this;
	}

	
	/**
	 * Adds one or more new handlers to the registered set.
	 * 
	 * @param handlers
	 *   the new handlers.
	 * @return
	 *   the object itself, for chaining.
	 */
	public TeeHandler wrapAround(MessageHandler... handlers) {		
		assert(handlers != null);
//		System.out.println("TEE: " + handlers.length + " elements");
		for(MessageHandler handler : handlers) {
//			System.out.println("TEE wrapping around " + handler.getClass().getSimpleName());
			this.handlers.add(handler);
		}
		return this;
	}
	

	/**
	 * Returns the handler's unique id.
	 * 
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
	public TeeHandler clone() throws CloneNotSupportedException  {
		TeeHandler clone = (TeeHandler)super.clone();
		clone.handlers = new ArrayList<MessageHandler>();
		for(MessageHandler handler : this.handlers) {
			clone.handlers.add(handler.clone());
		}
		return clone;
	}	
	
	/**
	 * Forwards the control flow to all registered sub-handlers. Before doing 
	 * so, it creates a clone of the input message and passes the copy on, in
	 * order to avoid unwanted interferences among registered handlers in different
	 * branches.
	 * 
	 * @see 
	 *   it.bankitalia.sisi.dsvaa.common.logging.writers.MessaheHandler#onMessage(org.dihedron.ulog.Message)
	 */
	@Override
	public void onMessage(Message message) {
//		System.out.println("TEE forwarding message to " + handlers.size());
		for(MessageHandler handler : handlers) {
//			System.out.println("resending message from " + this.getClass().getSimpleName() + " to " + handler.getClass().getSimpleName());
			Message clone = Message.clone(message);
			handler.onMessage(clone);
		}
	}
}
