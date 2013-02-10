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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import org.dihedron.struts.jndi.JndiNameResolver;
import org.dihedron.struts.plugin.AutoBound;
import org.dihedron.struts.plugin.AutoBindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class representing the factory used to produce the object 
 * instances to be injected into Struts-managed action instances.
 * 
 * @author Andrea Funto'
 */
public class ResourceFactory {

	/**
	 * The SLF4J logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(ResourceFactory.class);
	
	/**
	 * The JNDI context used to lookup objects.
	 */
	private Context context = null;

	/**
	 * The optional set of dependency aliases.
	 */	
	private Map<String, String> aliasMapping;
	
	/**
	 * The JNDI name factory, if any.
	 */
	private JndiNameResolver resolver;
	
	/**
	 * A random number generator.
	 */
	private Random random = null;
	
	/**
	 * Constructor.
	 * 
	 * @param args
	 *   a map of JNDI initialisation properties; each application server
	 *   may have its own, if none is provided the default values from the
	 *   environment are used.
	 *   
	 * @throws AutoBindingException
	 *   if the JNDI naming context cannot be acquired.
	 */
	public ResourceFactory(Hashtable<String, String> args) throws AutoBindingException {
		
		try {
			if(args != null && !args.isEmpty()) {
				context = new InitialContext(args);
			} else {
				context = new InitialContext();
			}
		} catch (NamingException e) {
			throw new AutoBindingException("unable to acquire naming context", e);
		}
	}

	/**
	 * Sets the alias mappings; the mapping is optional.
	 * 
	 * @param aliasMapping
	 *   an optional map of aliases and their corresponding resource's binding info.
	 */
	public void setAliasMapping(Map<String, String> aliasMapping) {
		this.aliasMapping = aliasMapping;
	}

	/**
	 * Sets the optional EJB name resolver.
	 * 
	 * @param nameResolver
	 *   an optional name resolver, used to map EJBs onto their JNDI name;
	 *   the mapping is largely application-server-dependent. 
	 */
	public void setNameResolver(JndiNameResolver nameResolver) {
		this.resolver = nameResolver;
	}
	
	/**
	 * Retrieves the injected resource's type; if the type is
	 * declared in the annotation, the provided value is used;
	 * if the annotation has AUTO, then some inference is attempted 
	 * to recognise the possible dependency type.
	 * 
	 * @param object
	 *   the object being dependency-injected.
	 * @param annotation
	 *   the field annotation.
	 * @param field
	 *   the field itself.
	 * @return
	 *   the injected dependency type.
	 * @throws AutoBindingException
	 */
	public AutoBound.Type getDependencyType(Object object, AutoBound annotation, Field field) throws AutoBindingException {
		
		AutoBound.Type type = annotation.type();
		
		if(type == AutoBound.Type.AUTO) {
			Class<?> fieldType = field.getType();
			logger.trace("auto-detecting dependency type: '{}'", field.getType());
			if(fieldType == File.class) {				
				type = AutoBound.Type.FILE;
			} else if(fieldType == InputStream.class) {
				type = AutoBound.Type.RESOURCE;
			} else if(fieldType == Properties.class) {
				type = AutoBound.Type.PROPERTIES;
			} else if(fieldType == DataSource.class) {
				type = AutoBound.Type.DATASOURCE;
			} else if(fieldType == ConnectionFactory.class) {
				type = AutoBound.Type.JMS_CONN_FACTORY;
			} else if(fieldType == Queue.class) {
				type = AutoBound.Type.JMS_QUEUE;
			} else if(fieldType == Topic.class) {
				type = AutoBound.Type.JMS_TOPIC;
			} else if(fieldType == UserTransaction.class) {
				type = AutoBound.Type.USER_TRANSACTION;
			} else if(field.getType() == PersistenceContext.class) {
				type = AutoBound.Type.PERSISTENCE_CONTEXT;
			} else if(fieldType == String.class) {
				type = AutoBound.Type.STRING;
			} else if(fieldType == Integer.class || fieldType == Long.class) {
				type = AutoBound.Type.RANDOM;
			} else {
				if(fieldType.isAnnotationPresent(Local.class)) {
					type = AutoBound.Type.EJB;
				} else if(fieldType.isAnnotationPresent(Remote.class)) {
					type = AutoBound.Type.EJB;
//				} else if(fieldType.isAnnotationPresent(Resource.class)) {
//					if(fieldType == ConnectionFactory.class) {
//						type = AutoWired.Type.JMS_CONN_FACTORY;
//					} else if(fieldType == Queue.class) {
//						type = AutoWired.Type.JMS_QUEUE;
//					} else if(fieldType == Topic.class) {
//						type = AutoWired.Type.JMS_TOPIC;
//					}
				} else {
					type = AutoBound.Type.POJO;
				}
			}
		}
		logger.trace("auto-detected dependency type: '{}'", type);
		return type;
	}
	

	/**
	 * Determines the binding information for the given annotated field, by:<ol>
	 * <li>checking the annotation's information, else</li>
	 * <li>checking the anbnotation's alias, and trying to get its binding information, else</li>
	 * <li>trying to get binding information through the object and field name, else</li>
	 * <li>for EJBs only, trying to have its JNDI name resolved by the optionally registered
	 * name resolver</li>.
	 * If all fails, returns <code>null</code>.
	 *  
	 * @param object
	 *   the objected being dependency-injected.
	 * @param annotation
	 *   the annotation accompanying the injected field.
	 * @param field
	 *   the injected field itself. 
	 * @param type
	 *   the pre-computed dependency type.  
	 * @return
	 *   the binding information, or <code>null</code> if all fails.
	 * @throws AutoBindingException
	 */
	public String getDependencyInformation(Object object, AutoBound annotation, Field field, AutoBound.Type type) throws AutoBindingException {
		String information = null;
		do {
			logger.debug("checking for direct binding information...");
			information = annotation.value();
			if(information != null && information.trim().length() > 0) {
				logger.info("binding information readily available: '{}'", information);
				break;
			}
			
			logger.debug("checking for binding information in alias...");
			String alias = annotation.alias();
			if(alias != null && alias.trim().length() > 0 && aliasMapping != null && aliasMapping.containsKey(alias)) {				
				information = aliasMapping.get(alias);
				if(information != null && information.trim().length() > 0) {
					logger.info("binding information available as alias: '{}' with value: '{}'", alias, information);
					break;
				} else {
					logger.warn("unbound alias '{}' on field '{}' of class '{}'", new Object[] {alias, field, object.getClass().getSimpleName()});
				}
			}
			
			logger.debug("trying to determine binding information through field name");
			alias = object.getClass().getName() + "#" + field.getName();
			if(aliasMapping != null && aliasMapping.containsKey(alias)) {				
				information = aliasMapping.get(alias);
				if(information != null && information.trim().length() > 0) {
					logger.info("binding information available as alias '{}' with value '{}'", alias, information);
					break;
				}
			}
			
			if(type == AutoBound.Type.EJB && this.resolver != null) {
				logger.debug("trying to determine binding information through name resolver");
				information = resolver.resolveEjbJndiName(field);
				if(information != null && information.trim().length() > 0) {
					logger.info("binding information available through name resolution: '{}'", information);
					break;
				}
			}
			logger.warn("no binding information for field '{}' either", field);
			
		} while(false);
		
		return information;
	}

	/**
	 * Creates a new object instance, according to provided information and recipient field's
	 * type, to be injected into the field itself. If all information is provided, no inference
	 * is used, otherwise it resolrts to intelligent defaults as far as possible in order to
	 * find out the dependency type and its binding information.
	 * 
	 * @param object
	 *   the object into which the dependency is to be injected.
	 * @param annotation
	 *   the dependency companion annotation.
	 * @param field
	 *   the field to be injected.
	 * @return
	 *   the object to be injected.
	 * @throws AutoBindingException
	 */
	public Object makeDependency(Object object, AutoBound annotation, Field field) throws AutoBindingException {
		Object result = null;
		
		AutoBound.Type type = getDependencyType(object, annotation, field);		
		String information = getDependencyInformation(object, annotation, field, type);
		
		if(information == null && !annotation.lenient()) {
			throw new AutoBindingException("insuffienct information to bind field");
		}
		
		logger.trace("acquiring dependency '{}' of type '{}'", information, type);
		
		switch(type){		
		case POJO:				// plain old java object
			try {
				result = Class.forName(information).newInstance();
			} catch (ClassNotFoundException e) {
				logger.error("class '{}' not found", information, e);
				throw new AutoBindingException("class '" + information + "' not found", e);
			} catch (InstantiationException e) {
				logger.error("error instantiating object of class '{}'", information, e);
				throw new AutoBindingException("error instantiating object of class ' " + information + "'", e);
			} catch (IllegalAccessException e) {
				logger.error("illegal access to class '{}'", information, e);
				throw new AutoBindingException("illegal access to class '" + information + "'", e);
			}		
			break;
		case FILE:				// a file-system file
			result = new File(information);
			break;
		case STRING:			// a string
			result = new String(information);
			break;
		case RESOURCE:			// a jar resource, as an input stream
			File file = new File(information);
			if(file.exists() && file.isFile()) {
				try {
					result = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					logger.debug("resource '{}' not found on filesystem, looking it up in classpath", information);
				}
			}
			result = this.getClass().getClassLoader().getResourceAsStream(information);
			break;	
		case RANDOM:
			if(random == null) {
				random = new Random(); 
			}
			if(field.getType().equals(Integer.class)) {
				result = random.nextInt();
			} else if(field.getType().equals(Long.class)){
				result = random.nextLong();
			}
			break;
		case PROPERTIES:		// a Properties, loaded from file-system or JAR
			Properties properties = new Properties();
			InputStream stream = null;
			try {
				stream = this.getClass().getClassLoader().getResourceAsStream(information);
				logger.trace("properties file '{}' found as JAR resource", information);
				if(stream != null) {
				} else {
					logger.trace("properties file '{}' not found as JAR resource", information);
					stream = new FileInputStream(information);
				}
				properties.load(stream);
				result = properties;
			} catch (FileNotFoundException e) {
				logger.error("properties file '{}' not found on filesystem: '{}'", information, e.getMessage());
				if(!annotation.lenient()) {
					throw new AutoBindingException("file '" + information + "' not found on filesystem", e);
				}
			} catch (IOException e) {
				logger.error("error reading properties file '{}': {}", information, e.getMessage());
				if(!annotation.lenient()) {
					throw new AutoBindingException("error reading properties file '" + information + "'", e);
				}
			} finally {
				if(stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						logger.error("error closing input stream for properties file '{}': {}", information, e.getMessage());
					}
					stream = null;
				}
			}
			break;
		case DATASOURCE:			// JDBC datasource
		case EJB:					// local and remote EJB
		case JMS_QUEUE: 			// JMS queue
		case JMS_TOPIC: 			// JMS topic
		case JMS_CONN_FACTORY:		// JMS connection factory
		case USER_TRANSACTION:		// User transaction
		case PERSISTENCE_CONTEXT:		// Persistence context
			try {
				result = context.lookup(information);
			} catch (NamingException e) {
				logger.error("JNDI naming exception", e);
				if(!annotation.lenient()) {
					throw new AutoBindingException("error looking up object " + information, e);
				}
			}		
			break;
		default:
			throw new AutoBindingException("unsupported resource type: '" + type + "'");
		}
		return result;
	}
	
	/**
	 * Retrieves the auto-wired resource's lookup or 
	 * instantiation information.
	 * 
	 * @param annotation
	 *   the injected resource's annotation.
	 * @return
	 *   a string representing the lookup annotation.
	 * @throws AutoWiringException
	 *   if the information is not available.
	 */
//	private String getDependencyInformation(AutoWired annotation, AutoWired.Type type, Field field) throws AutoWiringException {
// 
//		// get info from the annotation itself
//		String information = annotation.value();
//		if(information != null && information.trim().length() > 0) {
//			if(aliasMapping != null && aliasMapping.containsKey(information)) {
//				information = aliasMapping.get(information);
//				return information;
//			}
//			if(type != AutoWired.Type.EJB) {
//				return information;
//			}
//		}
//		
//		// get info using the fully qualified class name
//		information = field.getType().getName();
//		if(information != null && information.trim().length() > 0) {
//			if(aliasMapping != null && aliasMapping.containsKey(information)) {
//				information = aliasMapping.get(information);
//				return information;
//			}
//		}
//
//		// get info from the class name
//		information = field.getType().getSimpleName();
//		if(information != null && information.trim().length() > 0) {
//			if(aliasMapping != null && aliasMapping.containsKey(information)) {
//				information = aliasMapping.get(information);
//				return information;
//			}
//		}
//		
//		// get info from the JNDI name factory
//		if(type == AutoWired.Type.EJB && resolver != null) {
//			information = resolver.resolveEjbJndiName(field);
//			return information;
//		}		
//		return null;
//	}
	

}
