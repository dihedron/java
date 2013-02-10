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
import org.slf4j.Logger;

/**
 * An implementation of {@link ILoggerFactory} returning a
 * {@link UlogLoggerAdapter} instance.
 * 
 * @author Andrea Funto'
 */
public class UlogLoggerFactory implements ILoggerFactory {

	/**
	 * @see org.slf4j.ILoggerFactory#getLogger(java.lang.String)
	 */
	@Override
	public Logger getLogger(String name) {
		return new UlogLoggerAdapter(org.dihedron.ulog.Logger.getLogger());		
	}
}
