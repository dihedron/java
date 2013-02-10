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


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dihedron.ulog.Message;

/**
 * This class implements a mechanism to make logging
 * asynchronous on any wrapped handler; a message is posted
 * onto an internal queue, and is picked up by an internal
 * worker thread as soon as system resources are available.
 * 
 * @author Andrea Funto'
 */
public class AsynchronousHandler extends WrapperHandler implements Cloneable {
	
	/**
	 * Makes the processing of the wrapped <code>MessageHandler</code>
	 * asynchronous by executing it in a separate thread.
	 * 
	 * @author Andrea Funto'
	 */
	class TaskHandler implements Runnable {
		
		/**
		 * The log message.
		 */
		private Message message;
		
		/**
		 * The actual wrapped (synchronous) handler.
		 */
		private MessageHandler handler;
		
		/**
		 * Constructor.
		 * 
		 * @param message
		 *   the message.
		 * @param handler
		 *   the actual wrapped handler.
		 */
		TaskHandler(Message message, MessageHandler handler) {
			this.message = message;
			this.handler = handler;
		}

		/**
		 * Sends the message to the given handler. 
		 */
		public void run() {
			handler.onMessage(message);
		}
	}
	
	/**
	 * The asynchronous handler's own name. 
	 */
	private static final String ID = "asynch";	
	
	/**
	 * The internal executor service.
	 */
	private ExecutorService executor;
	
	/**
	 * Constructor.
	 */
	public AsynchronousHandler() {
		super();
		executor = Executors.newFixedThreadPool(1);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param handler
	 *   the wrapped handler.
	 */
	public AsynchronousHandler(MessageHandler handler) {
		super(handler);
		executor = Executors.newFixedThreadPool(1);
	}
	
	/**
	 * Sends the message out to the wrapped handler by posting the
	 * request into an internal queue.
	 * 
	 * @param message
	 *   the message.
	 */
	@Override
	public void onMessage(Message message) {
		executor.execute(new TaskHandler(message, handler));
	}

	/**
	 * Returns the handler name, as a concatenation of the
	 * "asynch-" label and the wrapped handler's own name.
	 * 
	 *  @return
	 *    the handler name (as a composition of stack elements).
	 */
	public String getId() {
		return ID + "-" + handler.getId();
	}
	
	/**
	 * Performs a deep copy of the object.
	 * 
	 * @return
	 *   a deep copy of the object.
	 */
	@Override
	public AsynchronousHandler clone() throws CloneNotSupportedException {
		AsynchronousHandler clone = (AsynchronousHandler)super.clone();
		clone.executor = Executors.newFixedThreadPool(1);
		clone.handler = this.handler.clone();
		return clone;
	}
}
