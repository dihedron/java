/**
 * Copyright (c) 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron Struts2 Dependency Injection Plugin ("Struts-DI").
 *
 * "Struts-DI" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "Struts-DI" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "Struts-DI". If not, see <http://www.gnu.org/licenses/>.
 */

package org.dihedron.struts.jndi;

import java.lang.reflect.Field;

/**
 * This is the base interface for all classes implementing the mechanism
 * to intelligently generate the EJB binding name for an EJB of which
 * only the local/remote interface class is known.
 * 
 * @author Andrea Funto'
 */
public interface JndiNameResolver {
	/**
	 * Creates an application server-specific JNDI name for the given EJB.
	 * 
	 * @param field
	 *   the EJB (as a Reflection field) whose JNDI name is to be created.
	 * @return
	 *   the JNDI name for the given EJB.
	 */
	String resolveEjbJndiName(Field field);
}
