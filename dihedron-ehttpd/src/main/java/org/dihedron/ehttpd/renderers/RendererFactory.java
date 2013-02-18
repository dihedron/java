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
package org.dihedron.ehttpd.renderers;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dihedron.ehttpd.actions.Action;
import org.dihedron.ehttpd.annotations.Outcome;
import org.dihedron.ehttpd.annotations.Outcomes;
import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class acts as a renderer factory, by inspecting the
 * action annotations and the outcome of the action's execution.
 * 
 * @author Andrea Funto'
 */
public class RendererFactory {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(RendererFactory.class);
	
	/**
	 * A map of renderer names to renderer classes.
	 */
	private Map<String, Class<? extends Renderer>> renderers;
	
	/**
	 * A default package prepended to renderer class names when
	 * there are not fully qualified with an explicit package; it 
	 * <b>must not</b> contain the trailing 'dot'.
	 */
	private String defaultPackage;
	
	/**
	 * Constructor.
	 */
	public RendererFactory() {
		this(null);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param defaultPackage
	 *   a package name (<b>not</b> including the trailing 'dot') where
	 *   renderers will be located when their class name is not fully
	 *   qualified by a package name of its own.
	 */
	public RendererFactory(String defaultPackage) {
		this.renderers =  Collections.synchronizedMap(new HashMap<String, Class<? extends Renderer>>());
		this.defaultPackage = defaultPackage;
	}	

	/**
	 * Creates a new renderer, using the annotated information in the action 
	 * and the outcome of its execution.
	 * 
	 * @param action
	 *   the action for which a renderer is to be identified.
	 * @param result
	 *   the action's execution result.
	 */
	@SuppressWarnings("unchecked")
	public Renderer makeRenderer(Action action, String result) throws ApplicationException {
		
		Renderer renderer = null;
		
		logger.debug("instantiating renderer for \"" + action.getClass() + "\" with result " + result + "\"");
		
		RendererInfo info = getRendererInfoForResult(action, result);
		
		if(info != null) {
		
			String name = info.getClassName();
			logger.debug("renderer class name: \"" + name + "\"");
			
			Class<? extends Renderer> clazz = null;
			
			if (!renderers.containsKey(name)) {
				logger.debug("renderer not in map yet: loading class...");
	
				try {
					clazz = (Class<? extends Renderer>) Class.forName(name);
					logger.debug("class loaded, adding to map...");
					renderers.put(name, clazz);
				} catch (Exception e) {
					logger.error("error loading class for renderer \"" + name + "\"", e);
					throw new ApplicationException("error loading class for renderer \"" + name + "\"", e);
				}
			}
			
			if (clazz == null) {
				logger.debug("renderer already in map");
				clazz = renderers.get(name);
			}
			
			try {
				logger.debug("instantiating renderer...");
				renderer = clazz.newInstance();
				
				String [] parameters = info.getParameters();				
				renderer.setParameters(parameters);				
				return renderer;
				
			} catch (Exception e) {
				logger.error("error creating action \"" + name + "\"", e);
				throw new ApplicationException("error creating action \"" + name + "\"", e);
			}
		} else {
			logger.error("no renderer specified");
		}
		return null;
	}
	
	/**
	 * Retrieves the renderer class name and parameters for the given 
	 * result, on the given action, based on the annotated info in the 
	 * Action class.
	 * 
	 * @param action
	 *   the action for which the renderer class name is to be found.
	 * @param result
	 *   the result of the action's execution.
	 * @return
	 *   the renderer info for the given action and result; if the 
	 *   class name in the annotation is not fully qualified with a
	 *   package name and the <code>defaultPackage</code> optional
	 *   parameter has been configured, the name is prepended with
	 *   the default package name.
	 */
	private RendererInfo getRendererInfoForResult(Action action, String result) {
		RendererInfo info = null;
		
		logger.debug("retrieving renderering information for " + result + " on action " + action.getClass());
		
		Outcomes annotation = action.getClass().getAnnotation(Outcomes.class);
		Outcome [] mappings = annotation.value();
		for (Outcome mapping : mappings) {
			logger.debug("checking mapping \"" + mapping.result() + "\"...");
			if(mapping.result().equals(result)) {
				String classname = null;
				Class<?> classref = mapping.classref();
				if(classref != Object.class) {
					classname = classref.getName();
					logger.debug("getting renderer type from class reference in annotation: \"" + classname + "\"...");
				} else {
					classname = mapping.classname();
					if(classname != null) {
						if(!classname.contains(".") && defaultPackage != null) {
							classname = defaultPackage + "." + classname;
						}
					}
					logger.debug("getting renderer type from class name in annotation: \"" + classname + "\"...");
				}
				info = new RendererInfo(classname);
				info.setParameters(mapping.parameters());
				break;
			}
		}

		logger.debug("returning info: \" " + info + "\"");
		
		return info;
	}
	/*
	private String getRendererClassForResult(Action action, Result result) {
		String className = null;
		
		logger.debug("retrieving renderering information for " + result);
		
		switch(result) {
		case SUCCESS:
			OnSuccess success = action.getClass().getAnnotation(OnSuccess.class);
			if(success != null) {
				className = success.value();
			}
			break;
		case INPUT:
			OnInput input = action.getClass().getAnnotation(OnInput.class);
			if(input != null) {
				className = input.value();
			}
			break;
		case ERROR:
			OnError error = action.getClass().getAnnotation(OnError.class);
			if(error != null) {
				className = error.value();
			}
			break;
		}
		
		if(className != null) {
			if(!className.contains(".") && defaultPackage != null) {
				className = defaultPackage + "." + className;
			}
		}
		
		logger.debug("returning class name: \" " + className + "\"");
		
		return className;
	}
	
	
	/**
	 * Retrieves the (optional) renderer parameters for the given result, 
	 * on the given action, based on the annotated info in the Action
	 * class.
	 * 
	 * @param action
	 *   the action for which the renderer class name is to be found.
	 * @param result
	 *   the result of the action's execution.
	 * @return
	 *   the renderer parameters for the given action and result, or null
	 *   if none found.
	 *
	private String[] getRendererParametersForResult(Action action, Result result) {
		String [] parameters = null;
		
		logger.debug("retrieving renderering information for " + result);
		
		switch(result) {
		case SUCCESS:
			OnSuccess success = action.getClass().getAnnotation(OnSuccess.class);
			if(success != null) {
				parameters = success.parameters();
			}
			break;
		case INPUT:
			OnInput input = action.getClass().getAnnotation(OnInput.class);
			if(input != null){
				parameters = input.parameters();
			}
			break;
		case ERROR:
			OnError error = action.getClass().getAnnotation(OnError.class);
			if(error != null) {
				parameters = error.parameters();
			}
			break;
		}
		return parameters;
	}
	*/
}
