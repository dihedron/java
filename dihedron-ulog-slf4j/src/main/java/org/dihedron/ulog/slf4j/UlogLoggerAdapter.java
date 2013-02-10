/**
 * Copyright (c) 2012, 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the uLog library ("uLog") SLF4J binding.
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
package org.dihedron.ulog.slf4j;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

/**
 * A wrapper over {@link org.dihedron.ulog.Logger} conforming to the 
 * {@link Logger} interface.
 * 
 * @author Andrea Funto'
 */
public final class UlogLoggerAdapter extends MarkerIgnoringBase implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 8561667284797975523L;

	/**
	 * The wrapped ULOG logger.
	 */
	final transient org.dihedron.ulog.Logger logger;

	/**
	 * Constructor; it has package access to that only LoggerAdapterFactory
	 * has access to it.
	 * 
	 * @param logger
	 *   the wrapper ULOG logger.
	 */
	UlogLoggerAdapter(org.dihedron.ulog.Logger logger) {
		this.logger = logger;
	}

	/**
	 * Returns if this logger instance enabled for the TRACE level.
	 * 
	 * @return 
	 *   always <code>true</code>, as there is not way of telling without 
	 *   invoking the full chain (level filtering is delegated to policies in
	 *   the logging stack).
	 */
	@Override
	public boolean isTraceEnabled() {
		return true;
	}	
	
	/**
	 * Log a message object at level TRACE.
	 * 
	 * @param message
	 *   the message object to be logged.
	 */
	@Override
	public void trace(String message) {
		logger.trace(message);
	}

	/**
	 * Log a message at level TRACE according to the specified format and
	 * argument.
	 * 
	 * @param format
	 *   the format string.
	 * @param arg
	 *   the argument.
	 */
	@Override
	public void trace(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		logger.trace(ft.getMessage());
	}

	/**
	 * Log a message at level TRACE according to the specified format and
	 * arguments.
	 * 
	 * @param format
	 *   the format string.
	 * @param arg1
	 *   the first argument.
	 * @param arg2
	 *   the second argument.
	 */
	@Override
	public void trace(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		logger.trace(ft.getMessage());
	}

	/**
	 * Log a message at level TRACE according to the specified format and
	 * arguments.
	 * 
	 * @param format
	 *   the format string.
	 * @param arguments
	 *   an array of arguments.
	 */
	@Override
	public void trace(String format, Object[] arguments) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
		logger.trace(ft.getMessage());
	}

	/**
	 * Log an exception (throwable) at level TRACE with an accompanying message.
	 * 
	 * @param message
	 *   the message accompanying the exception.
	 * @param exception
	 *   the exception (throwable) to log.
	 */
	@Override
	public void trace(String message, Throwable exception) {
		logger.trace(message, exception);
	}

	/**
	 * Returns if this logger instance enabled for the DEBUG level.
	 * 
	 * @return 
	 *   always <code>true</code>, as there is not way of telling without 
	 *   invoking the full chain (level filtering is delegated to policies in
	 *   the logging stack).
	 */
	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	/**
	 * Log a message object at level DEBUG.
	 * 
	 * @param message
	 *   the message object to be logged.
	 */
	@Override
	public void debug(String message) {
		logger.debug(message);
	}

	/**
	 * Log a message at level DEBUG according to the specified format and
	 * argument.
	 * 
	 * @param format
	 *   the format string.
	 * @param arg
	 *   the argument.
	 */
	@Override
	public void debug(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		logger.debug(ft.getMessage());
	}

	/**
	 * Log a message at level DEBUG according to the specified format and
	 * arguments.
	 * 
	 * @param format
	 *   the format string.
	 * @param arg1
	 *   the first argument.
	 * @param arg2
	 *   the second argument.
	 */
	@Override
	public void debug(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		logger.debug(ft.getMessage());
	}

	/**
	 * Log a message at level DEBUG according to the specified format and
	 * arguments.
	 * 
	 * @param format
	 *   the format string.
	 * @param arguments
	 *   an array of arguments.
	 */
	@Override
	public void debug(String format, Object[] arguments) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
		logger.debug(ft.getMessage());
	}

	/**
	 * Log an exception (throwable) at level DEBUG with an accompanying message.
	 * 
	 * @param message
	 *   the message accompanying the exception.
	 * @param exception
	 *   the exception (throwable) to log.
	 */
	@Override
	public void debug(String message, Throwable exception) {
		logger.debug(message, exception);
	}

	/**
	 * Returns whether this logger instance enabled for the INFO level.
	 * 
	 * @return 
	 *   always <code>true</code>, as there is not way of telling without 
	 *   invoking the full chain (level filtering is delegated to policies in
	 *   the logging stack).
	 */
	@Override
	public boolean isInfoEnabled() {
		return true;
	}

	/**
	 * Log a message object at the INFO level.
	 * 
	 * @param message
	 *   the message object to be logged.
	 */
	@Override
	public void info(String message) {
		logger.info(message);
	}

	/**
	 * Log a message at level INFO according to the specified format and
	 * argument.
	 * 
	 * @param format
	 *   the format string.
	 * @param arg
	 *   the argument.
	 */
	@Override
	public void info(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		logger.info(ft.getMessage());
	}

	/**
	 * Log a message at the INFO level according to the specified format and
	 * arguments.
	 * 
	 * @param format
	 *   the format string.
	 * @param arg1
	 *   the first argument.
	 * @param arg2
	 *   the second argument.
	 */
	@Override
	public void info(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		logger.info(ft.getMessage());
	}

	/**
	 * Log a message at level INFO according to the specified format and
	 * arguments.
	 * 
	 * @param format
	 *   the format string.
	 * @param argArray
	 *   an array of arguments.
	 */
	@Override
	public void info(String format, Object[] argArray) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
		logger.info(ft.getMessage());
	}

	/**
	 * Log an exception (throwable) at the INFO level with an accompanying
	 * message.
	 * 
	 * @param message
	 *   the message accompanying the exception.
	 * @param exception
	 *   the exception (throwable) to log.
	 */
	@Override
	public void info(String message, Throwable exception) {
		logger.info(message, exception);
	}

	/**
	 * Returns whether this logger instance enabled for the INFO level.
	 * 
	 * @return 
	 *   always <code>true</code>, as there is not way of telling without 
	 *   invoking the full chain (level filtering is delegated to policies in
	 *   the logging stack).
	 */
	@Override
	public boolean isWarnEnabled() {
		return true;
	}

	/**
	 * Log a message object at the WARN level.
	 * 
	 * @param message
	 *   the message object to be logged.
	 */
	@Override
	public void warn(String message) {
		logger.warn(message);
	}

	/**
	 * Log a message at the WARN level according to the specified format and
	 * argument.
	 * 
	 * @param format
	 *   the format string.
	 * @param arg
	 *   the argument.
	 */
	@Override
	public void warn(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		logger.warn(ft.getMessage());
	}

	/**
	 * Log a message at the WARN level according to the specified format and
	 * arguments.
	 * 
	 * @param format
	 *   the format string.
	 * @param arg1
	 *   the first argument.
	 * @param arg2
	 *   the second argument.
	 */
	@Override
	public void warn(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		logger.warn(ft.getMessage());
	}

	/**
	 * Log a message at level WARN according to the specified format and
	 * arguments.
	 * 
	 * @param format
	 *   the format string.
	 * @param arguments
	 *   an array of arguments.
	 */
	@Override
	public void warn(String format, Object[] arguments) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
		logger.warn(ft.getMessage());
	}

	/**
	 * Log an exception (throwable) at the WARN level with an accompanying
	 * message.
	 * 
	 * @param message
	 *   the message accompanying the exception.
	 * @param exception
	 *   the exception (throwable) to log.
	 */
	@Override
	public void warn(String message, Throwable exception) {
		logger.warn(message, exception);
	}

	/**
	 * Returns whether this logger instance enabled for the INFO level.
	 * 
	 * @return 
	 *   always <code>true</code>, as there is not way of telling without 
	 *   invoking the full chain (level filtering is delegated to policies in
	 *   the logging stack).
	 */
	@Override
	public boolean isErrorEnabled() {
		return true;
	}

	/**
	 * Log a message object at the ERROR level.
	 * 
	 * @param message
	 *   the message object to be logged.
	 */
	@Override
	public void error(String message) {
		logger.error(message);
	}

	/**
	 * Log a message at the ERROR level according to the specified format and
	 * argument.
	 * 
	 * @param format
	 *   the format string.
	 * @param arg
	 *   the argument.
	 */
	@Override
	public void error(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		logger.error(ft.getMessage());
	}

	/**
	 * Log a message at the ERROR level according to the specified format and
	 * arguments.
	 * 
	 * @param format
	 *   the format string.
	 * @param arg1
	 *   the first argument.
	 * @param arg2
	 *   the second argument.
	 */
	@Override
	public void error(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		logger.error(ft.getMessage());
	}

	/**
	 * Log a message at level ERROR according to the specified format and
	 * arguments.
	 * 
	 * @param format
	 *   the format string.
	 * @param argArray
	 *   an array of arguments.
	 */
	@Override
	public void error(String format, Object[] argArray) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
		logger.error(ft.getMessage());
	}

	/**
	 * Log an exception (throwable) at the ERROR level with an accompanying
	 * message.
	 * 
	 * @param message
	 *   the message accompanying the exception.
	 * @param exception
	 *   the exception (throwable) to log.
	 */
	@Override
	public void error(String message, Throwable exception) {		
		logger.error(message, exception);
	}
}
