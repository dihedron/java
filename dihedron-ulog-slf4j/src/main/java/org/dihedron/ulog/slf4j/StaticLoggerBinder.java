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

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * The binding of the {@link LoggerFactory} class with an actual instance of
 * {@link ILoggerFactory} is performed using information returned by this class.
 * 
 * @author Andrea Funto'
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {

	/**
	 * The unique instance of this class.
	 */
	private static StaticLoggerBinder singleton = null;
	
	static {
		singleton = new StaticLoggerBinder();
	}

	/**
	 * Return the singleton of this class.
	 * 
	 * @return 
	 *   the StaticLoggerBinder singleton.
	 */
	public static final StaticLoggerBinder getSingleton() {
		return singleton;
	}

	/**
	 * Declare the version of the SLF4J API this implementation is compiled
	 * against. The value of this field is usually modified with each release.
	 * To avoid constant folding by the compiler, this field must <b>not</b> be 
	 * final.
	 */
	public static String REQUESTED_API_VERSION = "1.6"; // !final

	/**
	 * The ILoggerFactory instance returned by the {@link #getLoggerFactory}
	 * method should always be the same object
	 */
	private final ILoggerFactory loggerFactory;

	/**
	 * Constructor; initialises the logger factory.
	 */
	private StaticLoggerBinder() {
		loggerFactory = new UlogLoggerFactory();
	}

	/**
	 * Retrieves the single instance of logger factory for ULOG.
	 * 
	 * @return
	 *   the ULOG logger factory.
	 */
	@Override
	public ILoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

	/**
	 * Returns the name of the class implementing the ULOG
	 * logger factory.
	 * 
	 * @return 
	 *   the name of the ULOG logger factory class.
	 */
	public String getLoggerFactoryClassStr() {
		return UlogLoggerFactory.class.getName();
	}
}
