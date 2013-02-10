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
package org.dihedron.ulog;


import java.util.Date;

/**
 * A class representing a logger message.
 * 
 * @author Andrea Funto'
 */
public class Message {
		
	
	
	/**
	 * Makes a verbatim copy of the given message.
	 * 
	 * @param message
	 *   the message to be copied byte-by-byte.
	 * @return
	 *   a clone of the original message.
	 */
	public static Message clone(Message message) {
		assert(message != null);
		return new Message(message);
	}
	
	/**
	 * The message level.
	 */
	private Level level;
	
	/**
	 * The message's time-stamp.
	 */
	private Date timestamp;
			
	/**
	 * The message text.
	 */
	private String text;
	
	/**
	 * The name of the class.
	 */
	private String classname;
	
	/**
	 * The method where the logging occurs.
	 */
	private String method;
	
	/**
	 * The source file where the logging message is located.
	 */
	private String filename;
	
	/**
	 * The line number within the source file.
	 */
	private int lineno;
	
	/**
	 * The optional wrapped exception.
	 */
	private Throwable exception;
	
	/**
	 * Private constructor.
	 * 
	 * @param other
	 *   the message to be cloned.
	 */
	Message(Message other) {
		this.level = other.level;
		this.timestamp = other.timestamp;
		this.text = other.text;
		this.classname = other.classname;
		this.method = other.method;
		this.filename = other.filename;
		this.lineno = other.lineno;
		this.exception = other.exception;
	}
	
	/**
	 * Private constructor.
	 * 
	 * @param level
	 *   the message level.
	 * @param text
	 *   the message text.
	 * @param classname
	 *   the name of the class.
	 * @param method
	 *   the name of the method.
	 * @param filename
	 *   the name of the source file.
	 * @param lineno
	 *   the line number within the source file.
	 * @param exception
	 *   the optional wrapped exception.
	 */
	Message(Level level, String text, String classname, String method, String filename, int lineno, Throwable... exception) {
		this.level = level;
		this.timestamp = new Date();
		this.text = text;
		this.classname = classname;
		this.method = method;
		this.filename = filename;
		this.lineno = lineno;
		this.exception = (exception != null && exception.length > 0) ? exception[0] : null;
	}

	/**
	 * Returns the message level.
	 *
	 * @return 
	 *   the message level.
	 */
	public Level getLevel() {
		return level;
	}
	
	/**
	 * Returns the message's time-stamp.
	 * 
	 * @return
	 *   the message's time-stamp.
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Returns the message text.
	 *
	 * @return 
	 *   the message text.
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Replaces the message text.
	 *
	 * @param text 
	 *   the message text.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the name of the class that is logging the
	 * message.
	 *
	 * @return 
	 *   the name of the class that is logging.
	 */
	public String getClassName() {
		return classname;
	}

	/**
	 * Returns the method requesting a log message.
	 *
	 * @return 
	 *   the method.
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Returns the source file name.
	 *
	 * @return 
	 *   the name of the source file.
	 */
	public String getFileName() {
		return filename;
	}

	/**
	 * Returns the line number where the logging occurs.
	 *
	 * @return 
	 *   the line number in the source file.
	 */
	public int getLineNumber() {
		return lineno;
	}
	
	/**
	 * Returns the optional exception.
	 * 
	 * @return
	 *   the optional exception.
	 */
	public Throwable getException() {
		return exception;
	}
}
