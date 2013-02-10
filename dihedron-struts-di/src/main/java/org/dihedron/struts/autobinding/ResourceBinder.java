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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.dihedron.struts.plugin.AutoBound;
import org.dihedron.struts.plugin.AutoBindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for scanning the Action, finding 
 * annotated fields and injecting them with the values provided
 * by the factory class. 
 * 
 * @author Andrea Funto'
 */
public class ResourceBinder {
		
	/**
	 * The SLF4J logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(ResourceBinder.class);
	
	/**
	 * The factory object used to create/lookup and inject dependencies.
	 */
	private ResourceFactory resourceFactory;

	/**
	 * Whether the dependency should be cast to the recipient
	 * object's field type before assignment; this helps ensure
	 * that the proper type of dependency is injected.
	 */
	private boolean checkDependencyType = false;
	
	/**
	 * Constructor.
	 * 
	 * @param resourceFactory
	 *   the resource factory: it is responsible of creating the
	 *   new resources to be injected.
	 */
	public ResourceBinder(ResourceFactory resourceFactory) {
		this.resourceFactory = resourceFactory;
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
	public void setCheckDependencyType(boolean value) {
		this.checkDependencyType = value;
		logger.trace("setting check type to: {}", (checkDependencyType ? "enabled" : "disabled"));
	}

	/**
	 * Scans the object for annotated fields, then tries to inject them
	 * according to...
	 * 
	 * @param recipient
	 *   the object whose annotated fields will be injected.
	 * @return
	 *   the object itself, with bound anottated fields.
	 * @throws AutoBindingException
	 */
	public Object bind(Object recipient) throws AutoBindingException {
		if(recipient == null) {
			logger.error("null recipient object");
			throw new AutoBindingException("invalid recipient object");
		}
		
		Field[] fields = recipient.getClass().getDeclaredFields();
		for (Field field : fields) {
			Annotation annotation = field.getAnnotation(AutoBound.class);
			if(annotation != null){			    
				String information = ((AutoBound)annotation).value();
				String alias = ((AutoBound)annotation).alias();
				logger.debug("binding field '{}' ({}){}", new Object[]{ field.getName(), field.getType().getSimpleName(), 
						(information.length() > 0 ? " to " + information : (alias.length() > 0 ? alias : "")) });
			    Object resource = resourceFactory.makeDependency(recipient, (AutoBound)annotation, field);
			    if(resource == null && ((AutoBound)annotation).lenient()) {
			    	logger.warn("error binding field '{}'", field.getName());
			    	return recipient;
			    }
			    bind(recipient, field, resource);
			    logger.debug("resource bound");
			}					
		}
		return recipient;
	}
		
	private void bind(Object recipient, Field field, Object value) throws AutoBindingException {
		boolean wasAccessible = field.isAccessible(); 		
		field.setAccessible(true); // make field accessible if it was not
		
		try {
			if(checkDependencyType){ 
				// check type compatibility between value and recipient field				
				field.set(recipient, field.getType().cast(value));
			} else { 
				field.set(recipient, value);
			}
		} catch (IllegalArgumentException e) {
			logger.error("illegal argument binding field '{}'", field.getName(), e);
			throw new AutoBindingException("illegal argument binding field '" + field.getName() + "'", e);
		} catch (IllegalAccessException e) {
			logger.error("illegal access binding field '{}'", field.getName(), e);
			throw new AutoBindingException("illegal access binding field '" + field.getName() + "'", e);
		}		
		field.setAccessible(wasAccessible); // restore access criteria
	}	
}
