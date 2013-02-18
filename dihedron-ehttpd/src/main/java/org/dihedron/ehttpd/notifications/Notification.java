/**
 * Copyright (c) 2011, 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron embeddable web container ("e-httpd").
 *
 * "e-httpd" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "e-httpd" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "e-httpd". If not, see <http://www.gnu.org/licenses/>.
 */
package org.dihedron.ehttpd.notifications;

/**
 * A class representing a notification message, produced by
 * the actions and passed on to the renderers. 
 * 
 * @author Andrea Funto'
 */
public class Notification {
	
	public enum Type {
		/**
		 * Informational notification.
		 */
		INFO,
		
		/**
		 * Warning notification.
		 */
		WARNING,
		
		/**
		 * Error notification.
		 */
		ERROR
	}
	
	/**
	 * Constructor.
	 * 
	 * @param type
	 *   the notification type.
	 * @param message
	 *   the notification message.
	 */
	public Notification(Type type, String message) {
		this.type = type;
		this.message = message;
	}

	/**
	 * Constructor.
	 * 
	 * @param type
	 *   the notification type.
	 * @param message
	 *   the notification message.
	 * @param arguments
	 *   any additional notification arguments.
	 */
	public Notification(Type type, String message, String... arguments) {
		this.type = type;
		this.message = message;
		if(arguments != null) {
			this.arguments = arguments;
		}
	}
	
	/**
	 * Retrieves the notification type.
	 * 
	 * @return
	 *   the notification type.
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Retrieves the notification message.
	 * 
	 * @return
	 *   the notification message.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Retrieves the (optional) additional notification
	 * arguments.
	 * 
	 * @return
	 *   any additional notification arguments.
	 */
	public String [] getArguments() {
		return arguments;
	}
	
	/**
	 * Checks whether the notification has any additional
	 * details.
	 * 
	 * @return
	 *   whether the notification has additional arguments.
	 */
	public boolean hasArguments() {
		return arguments != null && arguments.length > 0;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(type.name());
		sb.append(" - ").append(message);
		if(arguments != null) {
			sb.append( "{");
			for(String argument : arguments) {
				sb.append(" \"").append(argument).append("\"");
				if(argument != arguments[arguments.length - 1]) {
					sb.append(", ");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * The notification type.
	 */
	private Type type = Type.INFO;
	
	/**
	 * The notification message.
	 */
	private String message = null;
	
	/**
	 * Any (optional) notification arguments, such as
	 * referenced field in the GUI, etc.
	 */
	private String [] arguments = null;
}
