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
 * This class resolves names of EJBs on JBoss, given the field where the EJB reference 
 * should be injected.<br>The EJB JNDI naming scheme is largely application-server-dependent, 
 * so we need a class to resolve names automatically in order to ease the user's task. This
 * class attempts an educated guess by assuming that the beans' names comply with the ordinary 
 * naming convention, so that <code>MyBean</code>'s remote interface is called 
 * </code>MyBean<u>Remote</u></code> and its local interface is <code>MyBean<u>Local</u></code>.<br>  
 * JBoss JNDI names (on version 7) adhere to the
 * <code>java:module/&lt;bean-name&gt;!&lt;fully-qualified-interface-name&gt;</code> pattern;
 * an example of one such name is <code>java:module/MyBean!org.dihedron.template.ejb.MyBeanRemote</code>
 * (JNDI binding of <code>MyBean</code>'s remote interface). 
 * 
 * @author Andrea Funto'
 *
 */
public class JBossJndiNameResolver implements JndiNameResolver {

	/**
	 * The SLF4J logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(JBossJndiNameResolver.class);
	
	/**
	 * @see org.dihedron.struts.jndi.JndiNameFactory#resolveJndiName(org.dihedron.struts.autowiring.AutoWired, org.dihedron.struts.autowiring.AutoWired.Type, java.lang.reflect.Field)
	 */
	@Override
	public String resolveEjbJndiName(Field field) {
		StringBuilder name = new StringBuilder();
		Class<?> clazz = field.getType();
		Remote remote = clazz.getAnnotation(Remote.class);
		Local local = clazz.getAnnotation(Local.class);
		if(remote != null || field.getType().getSimpleName().endsWith("Remote")) {
			logger.debug("resolving name for remote EJB '{}'", field.getType().getSimpleName());
			// java:module/ExampleBean!org.dihedron.template.ejb.ExampleBeanRemote
			String bean = field.getType().getSimpleName().replaceAll("Remote", "");
			name.append("java:module/").append(bean).append("!").append(field.getType().getName());
			logger.debug("resolved EJB: '{}'", name.toString());
		} else if(local != null || field.getType().getSimpleName().endsWith("Local")) {
			logger.debug("resolving name for local EJB '{}'", field.getType().getSimpleName());
			// java:module/ExampleBean!org.dihedron.template.ejb.ExampleBeanLocal
			String bean = field.getType().getSimpleName().replaceAll("Local", "");
			name.append("java:module/").append(bean).append("!").append(field.getType().getName());
			logger.debug("resolved EJB: '{}'", name.toString());
		}
		return name.length() > 0 ? name.toString() : null;
	}
}
