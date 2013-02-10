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
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void trace(String message, Object... arguments) {
		Log.LOG.get().write(Level.TRACE, null, message, arguments);
	}

	/**
	 * Logs a message if the current logging level is at least TRACE; this
	 * method signature resembles that of SLF4J methods.
	 *
	 * @param exception
	 *   the exception whose stack trace is to be printed out.
	 * @param message
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void trace(Throwable exception, String message, Object... arguments) {
		Log.LOG.get().write(Level.TRACE, exception, message, arguments);
	}

	/**
	 * Logs a message if the current logging level is at least TRACE; this
	 * method signature resembles that of LOG4J methods.
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
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void debug(String message, Object... arguments) {
		Log.LOG.get().write(Level.DEBUG, null, message, arguments);
	}
	
	/**
	 * Logs a message if the current logging level is at least DEBUG; this
	 * method signature resembles that of SLF4J methods.
	 *
	 * @param exception
	 *   the exception whose stack trace is to be printed out.
	 * @param message
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void debug(Throwable exception, String message, Object... arguments) {
		Log.LOG.get().write(Level.DEBUG, exception, message, arguments);
	}

	/**
	 * Logs a message if the current logging level is at least DEBUG; this
	 * method signature resembles that of LOG4J methods
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
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void info(String message, Object... arguments) {
		Log.LOG.get().write(Level.INFO, null, message, arguments);	
	}
	
	/**
	 * Logs a message if the current logging level is at least INFO; this
	 * method signature resembles that of SLF4J methods.
	 *
	 * @param exception
	 *   the exception whose stack trace is to be printed out.
	 * @param message
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void info(Throwable exception, String message, Object... arguments) {
		Log.LOG.get().write(Level.INFO, exception, message, arguments);
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
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void warn(String message, Object... arguments) {
		Log.LOG.get().write(Level.WARN, null, message, arguments);	
	}
	
	/**
	 * Logs a message if the current logging level is at least WARN; this
	 * method signature resembles that of SLF4J methods.
	 *
	 * @param exception
	 *   the exception whose stack trace is to be printed out.
	 * @param message
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void warn(Throwable exception, String message, Object... arguments) {
		Log.LOG.get().write(Level.WARN, exception, message, arguments);
	}

	/**
	 * Logs a message if the current logging level is at least WARN.
	 * 
	 * @param message
	 *   the message.
	 * @param exception
	 *   an exception to be logged along with the message.
	 */
	public void warn(String message, Throwable exception) {
		Log.LOG.get().write(Level.WARN, exception, message);	
	}

	/**
	 * Logs a message if the current logging level is at least ERROR.
	 * 
	 * @param message
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void error(String message, Object... arguments) {
		Log.LOG.get().write(Level.ERROR, null, message, arguments);	
	}
	
	/**
	 * Logs a message if the current logging level is at least ERROR; this
	 * method signature resembles that of SLF4J methods.
	 *
	 * @param exception
	 *   the exception whose stack trace is to be printed out.
	 * @param message
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void error(Throwable exception, String message, Object... arguments) {
		Log.LOG.get().write(Level.ERROR, exception, message, arguments);
	}

	/**
	 * Logs a message if the current logging level is at least ERROR.
	 * 
	 * @param message
	 *   the message.
	 * @param exception
	 *   an exception to be logged along with the message.
	 */
	public void error(String message, Throwable exception) {
		Log.LOG.get().write(Level.ERROR, exception, message);	
	}	

	/**
	 * Logs a message if the current logging level is at least FATAL.
	 * 
	 * @param message
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void fatal(String message, Object... arguments) {
		Log.LOG.get().write(Level.FATAL, null, message, arguments);	
	}

	/**
	 * Logs a message if the current logging level is at least FATAL; this
	 * method signature resembles that of SLF4J methods.
	 *
	 * @param exception
	 *   the exception whose stack trace is to be printed out.
	 * @param message
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void fatal(Throwable exception, String message, Object... arguments) {
		Log.LOG.get().write(Level.FATAL, exception, message, arguments);
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
	 * Logs a message if the current logging level lower that that specifies.
	 *
	 * @param level
	 *   the message level.
	 * @param exception
	 *   the exception whose stack trace is to be printed out.
	 * @param message
	 *   the message; if the arguments are specified, their place-holders should
	 *   be indicated with two curly brackets, as in SLF4J ('{}'); if no
	 *   arguments are specifies the method expects the message to be pre-formatted,
	 *   the way you would do in LOG4J, e.g. by concatenating strings and objects
	 *   before calling the method. Note that the former approach is faster and
	 *   results in the creation of fewer temporary objects; moreover if the
	 *   message is not to be written out (e.g. because its level is below
	 *   threshold), in the former case no formatting occurs, whereas in the
	 *   latter, LOG4J-style, the message formatting is done up front and the 
	 *   message construction price is paid always. 
	 * @param arguments
	 *   [optional] arguments list; they will be replaced to '{}' in the 
	 *   message template, only if the message is to be written out.
	 */
	public void log(Level level, Throwable exception, String message, Object... arguments) {
		Log.LOG.get().write(level, exception, message, arguments);
	}
}
