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
package org.dihedron.struts.autobinding;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.naming.InitialContext;

import org.dihedron.struts.jndi.JndiNameResolver;
import org.dihedron.struts.plugin.AutoBindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds the <code>ResourceFactory</code>, the <code>ResourceBinder</code>
 * and (if information is available) the <code>ResourceNameFactory</code>,
 * and wires them together.
 * 
 * @author Andrea Funto'
 */
public class ResourceBinderBuilder {			
	/**
	 * The SLF4J logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(ResourceBinderBuilder.class);
	
	/**
	 * Whether the dependency should be cast to the recipient
	 * object's field type before assignment; this helps ensure
	 * that the proper type of dependency is injected.
	 */
	private boolean checkType = false;
	
	/**
	 * An optional properties file containing the dependency aliases
	 * and their corresponding fully qualified JNDI names. 
	 */
	private String aliasBindings;
	
	/**
	 * The fully qualified class name of the JNDI name factory, if any.
	 */
	private String jndiNameResolverClass;
	
	/**
	 * The optional full class name of the initial context factory.
	 */
	private String initialContextFactory; 
	
	/**
	 * The optional URL of the JNDI provider.
	 */
	private String jndiProviderUrl; 
			
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
		checkType = (value != null) && (value.equalsIgnoreCase("true"));
		logger.trace("setting check type to {}", (checkType ? "enabled" : "disabled"));
	}
	
	/**
	 * Sets the initial context factory.
	 *
	 * @param value 
	 *   the initial context factory to set.
	 */
	public void setInitialContextFactory(String value) {
		logger.trace("setting initial context factory to '{}'", value);
		this.initialContextFactory = value;
	}

	/**
	 * Sets the JNDI name factory class name.
	 *
	 * @param value 
	 *   the JNDI name factory class name.
	 */
	public void setJndiNameResolverClass(String value) {
		logger.trace("setting JNDI name resolver class to: '{}'", value);
		this.jndiNameResolverClass = value;
	}
	
	/**
	 * Sets the JNDI provider URL.
	 *
	 * @param value 
	 *   the JNDI provider URL to set.
	 */
	public void setJndiProviderUrl(String value) {
		logger.trace("setting JNDI provider url to: '{}'", value);
		this.jndiProviderUrl = value;
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
		logger.trace("setting alias bindings to: '{}'", value);
		this.aliasBindings = value;		
	}

	/**
	 * Initialises the resource binder.
	 * 
	 * @return 
	 *   the resource binder.
	 *   
	 */

	public ResourceBinder createResourceAutoBinder() {
		logger.trace("creating resource autobinder");
		
		ResourceBinder binder = null;
		
		// initialise the binding factory; this is the real workhorse
		// behind the binder.
		try {

			// if JNDI parameters are provided, use them
			Hashtable<String, String> args = new Hashtable<String, String>();
			if(jndiProviderUrl != null && jndiProviderUrl.trim().length() > 0) {
				args.put(InitialContext.PROVIDER_URL, jndiProviderUrl.trim());
			}
			if(this.initialContextFactory != null && initialContextFactory.trim().length() > 0) {
				args.put(InitialContext.INITIAL_CONTEXT_FACTORY, initialContextFactory.trim());
			}

			ResourceFactory resourceFactory = new ResourceFactory(args);

			// provide the JNDI name resolver
			if(jndiNameResolverClass != null && jndiNameResolverClass.trim().length() > 0) {
				JndiNameResolver jndiNameResolver = (JndiNameResolver)Class.forName(jndiNameResolverClass.trim()).newInstance();
				resourceFactory.setNameResolver(jndiNameResolver);
				logger.info("JNDI name resolver of type '{}' ready", jndiNameResolverClass);
			}			

			// provide the dependency mapping, if declared 
			if(aliasBindings != null && aliasBindings.trim().length() > 0) {
				Properties properties = new Properties();
				properties.load(this.getClass().getClassLoader().getResourceAsStream(aliasBindings));
				logger.info("---------------- autobinder mappings ----------------");
				for(Entry<String, String> property : properties.entrySet()) {
					logger.info(" * '{}' mapped to '{}'", property.getKey(), property.getValue());
				}
				logger.info("-----------------------------------------------------");
				resourceFactory.setAliasMapping(properties);
				logger.info("alias mapping based on '{}' ready", aliasBindings);
			}
			
			// now create the binder
			binder = new ResourceBinder(resourceFactory);			
			
		} catch (AutoBindingException e) {
			logger.error("error acquiring dependency factory", e);
		} catch (IOException e) {
			logger.error("error loading dependecy aliases {}", aliasBindings, e);
		} catch (IllegalAccessException e) {
			logger.error("illegal class access exception", e);
		} catch (InstantiationException e) {
			logger.error("object instantiation error", e);
		} catch (ClassNotFoundException e) {
			logger.error("class not found", e);
		}	
		 
		logger.info("autobinder ready");
		return binder;
	}
}
