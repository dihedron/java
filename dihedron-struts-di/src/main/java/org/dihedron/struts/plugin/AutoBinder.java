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

package org.dihedron.struts.plugin;

import org.dihedron.struts.autobinding.ResourceBinder;
import org.dihedron.struts.autobinding.ResourceBinderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * The Struts2 action inspector plugin; it acts as a front
 * end to the injector, adapting the functionality to the
 * Struts2 requirements. 
 * 
 * @author Andrea Funto'
 */
public class AutoBinder implements Interceptor {
	
	/**
	 * The SLF4J logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(AutoBinder.class);
	
	/**
	 * Serial version id.
	 */
	private static final long serialVersionUID = 8404033124464221712L;
	
	/**
	 * The builder that will create the <code>ResourceBinder</code>
	 * and its companion objects (<code>ResourceFactory</code> and
	 * <code>JndiNameResolver</code>).
	 */
	private ResourceBinderBuilder builder = new ResourceBinderBuilder();
	
	/**
	 * The actual resource binder.
	 */
	private ResourceBinder binder;
	
	/**
	 * Default constructor.
	 */
	public AutoBinder() {
		logger.trace("creating autobinding plugin");
	}
	
	/**
	 * Sets the initial context factory.
	 *
	 * @param value 
	 *   the initial context factory to set.
	 */
	public void setInitialContextFactory(String value) {
		logger.debug("setting initialContextFactory to '{}'", value);
		builder.setInitialContextFactory(value);
	}

	/**
	 * Sets the flag that specifies whether the type of injected
	 * resources should be checked against the type of the
	 * recipient object's field.
	 * 
	 * @param value
	 *   if 'true' the type will be checked; any other value will
	 *   cause the flag to be set to false;
	 */
	public void setCheckType(String value) {
		logger.debug("setting checkType to '{}'", value);
		builder.setCheckType(value);
	}

	/**
	 * Sets the JNDI name resolver class name.
	 *
	 * @param value 
	 *   the JNDI name resolver class name.
	 */
	public void setJndiNameResolverClass(String value) {
		logger.debug("setting jndiNameResolverClass to '{}'", value);
		builder.setJndiNameResolverClass(value);
	}
	
	/**
	 * Sets the JNDI provider URL.
	 *
	 * @param value 
	 *   the JNDI provider URL to set.
	 */
	public void setJndiProviderUrl(String value) {
		builder.setJndiProviderUrl(value);
	}
	
	/**
	 * Sets the address of the file containing the mapping between
	 * dependency aliases and corresponding fully-qualified JNDI lookup
	 * names.
	 * 
	 * @param value
	 *   the name of the file (as a resource).
	 */
	public void setAliasBindings(String value) {
		builder.setAliasBindings(value);
	}

	/**
	 * Initialises the auto-binder.
	 * 
	 * @see 
	 *   com.opensymphony.xwork2.interceptor.Interceptor#init()
	 */
	@Override
	public void init() {
		logger.info("initialising the injector");
		binder = builder.createResourceAutoBinder();	
	}

	/** 
	 * Cleans up the injector.
	 * 
	 * @see 
	 *   com.opensymphony.xwork2.interceptor.Interceptor#destroy()
	 */
	@Override
	public void destroy() {
		logger.info("destroying the injector");
	}

	/**
	 * Intercepts Structs2 calls and operates on the action, by 
	 * injecting its annotated fields. This interceptor does not
	 * tamper with the Action's return value and does not divert
	 * the regular Action's work flow.
	 * 
	 * @return
	 *   the invocation's result, unchanged.
	 * @see 
	 *   com.opensymphony.xwork2.interceptor.Interceptor#intercept(com.opensymphony.xwork2.ActionInvocation)
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		long startTime = System.currentTimeMillis();
		Object action = invocation.getAction();
		action = binder.bind(action);
		String result = invocation.invoke();
		long executionTime = System.currentTimeMillis() - startTime;
		logger.trace("action invocation took {} ms", executionTime);
		return result;
	}
}
