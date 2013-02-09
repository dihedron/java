/**
 * Copyright (c) 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron Common Utilities library ("Commons").
 *
 * "Commons" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "Commons" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "Commons". If not, see <http://www.gnu.org/licenses/>.
 */
package org.dihedron.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Base exception for all library exceptions.
 * 
 * @author Andrea Funto'
 */
public class DihedronException extends Exception {

	/**
	 * Serial version id.
	 */
	private static final long serialVersionUID = 6015535629699736178L;

	/**
	 * Default constructor.
	 */
	public DihedronException() {
	}

	/**
	 * Constructor.
	 *
	 * @param message
	 *   the exception message.
	 */
	public DihedronException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 *
	 * @param cause
	 *   the root cause of this exception.
	 */
	public DihedronException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor.
	 *
	 * @param message
	 *   the exception message.
	 * @param cause
	 *   the root cause of this exception.
	 */
	public DihedronException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Returns the exception's stack trace as a string.
	 * 
	 * @return
	 *   the exception's stack trace as a String.
	 */
	public String asStackTraceString() {
		StringWriter writer = new StringWriter();
		PrintWriter printer = new PrintWriter(writer);
		this.printStackTrace(printer);
		return writer.toString(); 
	}
}
