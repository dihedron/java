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
package org.dihedron.ulog.extensions;

/**
 * Base interface for all classes implementing
 * an extension point, where <code>Extensions</code>
 * can be registered.
 * 
 * @author Andrea Funto'
 */
public interface ExtensionPoint {
	/**
	 * Adds an <code>Extension</code> to the extension
	 * point.
	 * 
	 * @param extension
	 *   the new extension.
	 */
	void addExtension(Extension extension);
}
