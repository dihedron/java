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

import javax.ejb.Local;
import javax.ejb.Remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Funto'
 */
public class WebSphereJndiNameResolver implements JndiNameResolver {
	
	/**
	 * The SLF4J logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(WebSphereJndiNameResolver.class);
	
	/**
	 * Creates the JNDI lookup string from the class of the EJB reference.
	 * 
	 * @param field
	 *   the EJB (as a Reflection field) whose JNDI name is to be created.
	 * 
	 * @see org.dihedron.struts.jndi.JndiNameResolver#resolveEjbJndiName(java.lang.reflect.Field)
	 */
	@Override
	public String resolveEjbJndiName(Field field) {		
		StringBuilder name = new StringBuilder();
		Class<?> clazz = field.getType();
		logger.info("contructing JNDI name for resource '{}'", clazz.getName());
		Remote remote = clazz.getAnnotation(Remote.class);
		Local local = clazz.getAnnotation(Local.class);
		if(remote != null || clazz.getSimpleName().endsWith("Remote")) {			
			name.append(clazz.getName());
			logger.info("resource is remote: '{}'", name.toString());
		} else if(local != null || clazz.getSimpleName().endsWith("Local")) {
			name.append("ejblocal:").append(clazz.getName());
			logger.info("resource is local: '{}'", name.toString());
		}
		return name.length() > 0 ? name.toString() : null;
	}
}
