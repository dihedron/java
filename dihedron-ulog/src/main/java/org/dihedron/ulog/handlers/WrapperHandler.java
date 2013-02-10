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
 * This class implements the base functionalities
 * for all filtering handlers.
 * 
 * @author Andrea Funto'
 */
public abstract class WrapperHandler implements MessageHandler {
	
	/**
	 * The wrapped handler.
	 */
	protected MessageHandler handler = null; 
	
	/**
	 * Constructor.
	 */
	public WrapperHandler() {
	}

	/**
	 * Constructor.
	 * 
	 * @param handler
	 *   the wrapped handler.
	 */
	public WrapperHandler(MessageHandler handler) {
		this.handler = handler;
	}

	/**
	 * Sets the <code>MessageHandler</code>.
	 * 
	 * @param handler
	 *   the wrapped handler.
	 * @return
	 *   the object itself, for chaining.
	 */
	public WrapperHandler wrapAround(MessageHandler handler) {
		this.handler = handler;
		return this;
	}
	
	/**
	 * Performs a deep copy of the object.
	 * 
	 * @return
	 *   a deep copy of the object.
	 */
	@Override
	public WrapperHandler clone() throws CloneNotSupportedException  {
		WrapperHandler clone = (WrapperHandler)super.clone();
		clone.handler = this.handler.clone();
		return clone;
	}	
	
	/**
	 * This method is invoked prior to forwarding control to
	 * the filtered <code>MessageHandler</code>. If the method
	 * returns <code>null</code>, the execution path is aborted
	 * thus resulting in the message being discarded. Otherwise
	 * the method can act upon the message itself and modify its
	 * contents in place, then return it. All implementing
	 * sub-classes can extend the filter behaviour by acting on 
	 * this method.
	 *  
	 * @param message
	 *   the incoming message.
	 * @return
	 *   the (possibly modified) message itself if the processing
	 *   can go on, code>null</code> if it should be aborted.
	 *
	protected abstract TextMessage filter(TextMessage message);
	*/
	
	/**
	 * This implements the interface's method by checking if the
	 * message should be filtered, and getting a (possibly modified)
	 * copy of the same, then forwarding control to the wrapped
	 * handler. See <code>filter()</code> for details.
	 */
	protected void forward(Message message) {
		if(message != null) {
//			System.out.println("forwarding message from " + this.getClass().getSimpleName() + " to " + handler.getClass().getSimpleName());
			handler.onMessage(message);
		}
		// otherwise message is discarded
	}
}
