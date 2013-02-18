/**
 * Copyright (c) 2011, 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron embeddable web container ("e-httpd").
 *
 * "e-httpd" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "e-httpd" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "e-httpd". If not, see <http://www.gnu.org/licenses/>.
 */
package org.dihedron.ehttpd.actions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class acts as a caching action factory.
 * 
 * @author Andrea Funto'
 */
public class ActionFactory {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(ActionFactory.class);
	
	/**
	 * A map of action names to action classes.
	 */
	private Map<String, Class<? extends Action>> actions;
	
	/**
	 * The package in which the action classes are expected to
	 * be located; new class instances are taken from this package.
	 */
	private String actionPackage;
	
	/**
	 * The (optional) action that handles application errors.
	 *
	private Action errorAction = null;
	*/

	/**
	 * Constructor.
	 * 
	 * @param actionPackage
	 *   the base package for the action classes.
	 */
	public ActionFactory(String actionPackage) {
		actions =  Collections.synchronizedMap(new HashMap<String, Class<? extends Action>>());
		this.actionPackage = actionPackage;
	}
	
	/**
	 * Registers an action  that will take care of handling
	 * errors.
	 * 
	 * @param action
	 *   the error handling action; if <code>null</code> any
	 *   active error handling actions will be removed.
	 *
	public void registerErrorHandlingAction(Action action) {
		if(action != null) {
			logger.info("registering new error handling action");			
		} else {
			logger.info("deregistering error handling action");
		}
		errorAction = action;
	}
	
	/**
	 * Retrieves the currently active error handling action,
	 * if any is registered.
	 * 
	 * @return
	 *   the error handling action, or <code>null</code> if none
	 *   is registered.
	 *
	public Action getErrorHandlingAction() {
		return errorAction;
	}
	*/
	
	/**
	 * Creates a new action, using  the cached class reference or
	 * instantiating a new one.
	 * 
	 * @param name
	 *   the name of the action.
	 */
	@SuppressWarnings("unchecked")
	public Action makeAction(String name) throws ApplicationException {
		
		logger.debug("instantiating action \"" + name + "\"");
		
		Class<? extends Action> clazz = null;
		
		if (!actions.containsKey(name)) {
			logger.debug("action not in map yet: loading class...");

			try {
				clazz = (Class<? extends Action>) Class.forName(actionPackage + "." + name);
				logger.debug("class loaded, adding to map...");
				actions.put(name, clazz);
			} catch (Exception e) {
				logger.error("error loading class for action \"" + name + "\" from package \"" + actionPackage + "\"", e);
				throw new ApplicationException("error loading class for action \"" + name + "\" from package \"" + actionPackage + "\"", e);
			}
		}
		
		if (clazz == null) {
			logger.debug("class already in map");
			clazz = actions.get(name);
		}
		
		try {
			logger.debug("instantiating action...");
			return clazz.newInstance();
		} catch (Exception e) {
			logger.error("error creating action \"" + name + "\"", e);
			throw new ApplicationException("error creating action \"" + name + "\"", e);
		}
	}
}
