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
 * The possible values for the logging level; in ALL
 * mode the logging is the most verbose; the other values
 * make logging output scarcer and scarcer.
 *
 * @author Andrea Funto'
 */
public enum Level {
	/**
	 * Most detailed messages: these include method entry and exit,
	 * dump of all input parameters etc.
	 */
	TRACE,		 
	/**
	 * Debugging messages: these messages should make it possible
	 * to debug a problem without writing extra-detailed information. 
	 */
	DEBUG,
	/**
	 * Informational messages: messages of these kind should make 
	 * it possible to follow the execution path.
	 */
	INFO,
	/**
	 * Mild error messages, data inconsistencies, data that is not
	 * exactly as expected but may not harm the ordinary execution.
	 */
	WARN, 
	/**
	 * Recoverable error: something went wrong, but the application
	 * may still be able to refuse its services gracefully.
	 */
	ERROR,
	/**
	 * Fatal error: these messages are recorded when an unrecoverable 
	 * error occurs; usually the application will be terminated.
	 */
	FATAL,
	/**
	 * No logging at all.
	 */
	NONE
}
