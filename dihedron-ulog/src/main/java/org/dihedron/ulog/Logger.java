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



/**
 * A LOG4J-like logger class. In order to use it, initialise it in 
 * the application main, like this:
 * <pre>
 * logger = Logger.initialiseWithDefaults(Level.DEBUG, ThisClass.class);
 * </pre>
 * and then create a static, final reference in each class where
 * logging occurs, as follows:
 * <pre>
 * public MyClass {
 *     private final static Logger logger = Logger.getLogger(MyClass.class);
 *     //...
 * }
 * </pre>
 * The object can then be used in any method of the class.<br> 
 * The Logger uses appenders to output its messages; by default only the
 * synchronous console appender is created, but you can configure the
 * Logger to use any appender, including high-performance asynchronous
 * ones.  
 * 
 * @author Andrea Funto'
 */
public class Logger  {
	
	/**
	 * Returns a new <code>Logger</code> instance.
	 * 
	 * @param clazz
	 *   unused, omit it altogether or use the class to which the
	 *   <code>Logger</code> will be associated, as you'd do in LOG4J.
	 * @return
	 *   a new <code>Logger</code>.
	 */
	public static Logger getLogger(Class<?> ... clazz) {
		return new Logger();
	}
	
	/**
	 * Default constructor.
	 */
	private Logger() {
	}
			 			
	/**
	 * Logs a message if the current logging level is at least TRACE.
	 * 
	 * @param message
	 *   the message.
	 */
	public void trace(String message) {
		Log.LOG.get().write(Level.TRACE, null, message);
	}

	/**
	 * Logs a message if the current logging level is at least TRACE.
	 * 
	 * @param message
	 *   the message.
	 * @param exception
	 *   an exception to be logged along with the message.
	 */
	public void trace(String message, Throwable exception) {
		Log.LOG.get().write(Level.TRACE, exception, message);
	}

	/**
	 * Logs a message if the current logging level is at least DEBUG.
	 * 
	 * @param message
	 *   the message.
	 */
	public void debug(String message) {
		Log.LOG.get().write(Level.DEBUG, null, message);
	}
	
	/**
	 * Logs a message if the current logging level is at least DEBUG.
	 * 
	 * @param message
	 *   the message.
	 * @param exception
	 *   an exception to be logged along with the message.
	 */
	public void debug(String message, Throwable exception) {
		Log.LOG.get().write(Level.DEBUG, exception, message);
	}

	/**
	 * Logs a message if the current logging level is at least INFO.
	 * 
	 * @param message
	 *   the message.
	 */
	public void info(String message) {
		Log.LOG.get().write(Level.INFO, null, message);	
	}
	
	/**
	 * Logs a message if the current logging level is at least INFO.
	 * 
	 * @param message
	 *   the message.
	 * @param exception
	 *   an exception to be logged along with the message.
	 */
	public void info(String message, Throwable exception) {
		Log.LOG.get().write(Level.INFO, exception, message);
	}
	
	/**
	 * Logs a message if the current logging level is at least WARN.
	 * 
	 * @param message
	 *   the message.
	 */
	public void warn(String message) {
		Log.LOG.get().write(Level.WARN, null, message);	}
	
	/**
	 * Logs a message if the current logging level is at least WARN.
	 * 
	 * @param message
	 *   the message.
	 * @param exception
	 *   an exception to be logged along with the message.
	 */
	public void warn(String message, Throwable exception) {
		Log.LOG.get().write(Level.WARN, exception, message);	}

	/**
	 * Logs a message if the current logging level is at least ERROR.
	 * 
	 * @param message
	 *   the message.
	 */
	public void error(String message) {
		Log.LOG.get().write(Level.ERROR, null, message);	}
	
	/**
	 * Logs a message if the current logging level is at least ERROR.
	 * 
	 * @param message
	 *   the message.
	 * @param exception
	 *   an exception to be logged along with the message.
	 */
	public void error(String message, Throwable exception) {
		Log.LOG.get().write(Level.ERROR, exception, message);	}	

	/**
	 * Logs a message if the current logging level is at least FATAL.
	 * 
	 * @param message
	 *   the message.
	 */
	public void fatal(String message) {
		Log.LOG.get().write(Level.FATAL, null, message);	
	}

	/**
	 * Logs a message if the current logging level is at least FATAL.
	 * 
	 * @param message
	 *   the message.
	 * @param exception
	 *   an exception to be logged along with the message.
	 */
	public void fatal(String message, Throwable exception) {
		Log.LOG.get().write(Level.FATAL, exception, message);
	}	
	
	/**
	 * Logs a message.
	 * 
	 * @param level
	 *   the message level; based on this parameter the logger may
	 *   decide whether to log or not the message.
	 * @param message
	 *   the message.
	 * @param exception
	 *   an exception to be logged along with the message.
	 */
	public void log(Level level, String message, Throwable exception) {
		Log.LOG.get().write(level, exception, message);
	}
}
