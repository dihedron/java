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
package org.dihedron.ulog.factories;

import org.dihedron.ulog.Log;

/**
 * @author Andrea Funto'
 */
public interface LogFactory {
	
	public static final String DEFAULT_FACTORY_CLASSNAME = "org.dihedron.ulog.factories.XmlLogFactory";
	
	/**
	 * Initialises the log factory, e.g. by reading the configuration
	 * and instantiating a template object or an object builder that 
	 * will then be able to create instances, one per thread, as needed.
	 */
	public void initialise();
	
	/**
	 * Creates a new <code>Log</code>, loading the factory configuration
	 * from the file whose path is stored in the input parameter.
	 * 
	 * @return
	 *   a <code>Log</code> instance.
	 */
	public Log makeLog();
}
