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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.dihedron.ehttpd.annotations.In;
import org.dihedron.ehttpd.annotations.Out;
import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.i18n.Localised;
import org.dihedron.ehttpd.i18n.MessageProvider;
import org.dihedron.ehttpd.notifications.Notifications;
import org.dihedron.ehttpd.renderers.Renderer;
import org.dihedron.ehttpd.renderers.RendererFactory;
import org.dihedron.ehttpd.server.HttpQuery;
import org.dihedron.ehttpd.server.Request;
import org.dihedron.ehttpd.server.Response;
import org.dihedron.ehttpd.server.handlers.BaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a "struts"-like controller.
 * 
 * @author Andrea Funto'
 */
public class ActionController extends BaseHandler implements Localised {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(ActionController.class);
	
	/**
	 * The actions context; all actions will start with this part of the
	 * path, and the action name will follow, such as in <pre>
	 * 	/actions/MyAction?param1=value1&param2=value2
	 * </pre> where "/actions" is the context, MyAction is the action name
	 * and the remainder are the request parameters. 
	 */
	private String rootActionPath;
	
	/**
	 * The actions' factory, which creates new actions given their name.
	 */
	private ActionFactory actionFactory;

	/**
	 * The renderers' factory, which creates new renderers given their name.
	 */
	private RendererFactory rendererFactory;
	
	/**
	 * The (optional) model, for <code>ModelAware</code> actions.
	 */
	private Object model = null;
	
	/**
	 * The (optional) message provider, for handling of localised 
	 * messages in the renderers.
	 */
	private MessageProvider messageProvider = null;

	/**
	 * Constructor.
	 * 
	 * @param rootActionPath
	 *   the root context for actions.
	 * @param actionPackage
	 *   the package where actions are to be located.
	 */
	public ActionController(String rootActionPath, String actionPackage) {
		this.rootActionPath = rootActionPath;
		this.actionFactory = new ActionFactory(actionPackage);
		this.rendererFactory = new RendererFactory();
		logger.info("creating action request handler for path \"" + rootActionPath + "\" based on package \"" + actionPackage + "\"");
	}

	/**
	 * Constructor.
	 * 
	 * @param rootActionPath
	 *   the root context for actions.
	 * @param actionPackage
	 *   the package where actions are to be located.
	 * @param rendererPackage
	 *   the package where renderers are to be located, when they are
	 *   not explicitly fully qualified with a package name of their own.
	 */
	public ActionController(String rootActionPath, String actionPackage, String rendererPackage) {
		this.rootActionPath = rootActionPath;
		this.actionFactory = new ActionFactory(actionPackage);
		this.rendererFactory = new RendererFactory(rendererPackage);
		logger.info("creating action request handler for path \"" + rootActionPath + "\" based on package \"" + actionPackage + "\"");
	}	

	/**
	 * Constructor.
	 * 
	 * @param rootActionPath
	 *   the root context for actions.
	 * @param actionPackage
	 *   the package where actions are to be located.
	 * @param rendererPackage
	 *   the package where renderers are to be located, when they are
	 *   not explicitly fully qualified with a package name of their own.
	 */
	public ActionController(String rootActionPath, String actionPackage, String rendererPackage, Object model) {
		this.rootActionPath = rootActionPath;
		this.actionFactory = new ActionFactory(actionPackage);
		this.rendererFactory = new RendererFactory(rendererPackage);
		this.model = model;
		logger.info("creating action request handler for path \"" + rootActionPath + "\" based on package \"" + actionPackage + "\"");
	}	
	
	/**
	 * Sets the (optional) model into the action
	 * controller; the model is injected into
	 * <code>modelAware</code> actions.
	 * 
	 * @param model
	 *   the model.
	 */
	public void setModel(Object model) {
		this.model = model;
	}
	
	/**
	 * Sets the optional message provider. The reference
	 * is passed on to the renderers implementing the 
	 * <code>Localised</code> interface.
	 * 
	 * @param provider 
	 *   a class implementing the <code>MessageProvider</code>
	 *   interface.
	 */
	public void setMessageProvider(MessageProvider provider) {
		this.messageProvider = provider;
	}
	
	/**
	 * This method performs the real activity of the action 
	 * controller.
	 * 
	 * @param request
	 *   the request object.
	 * @param response 
	 *   the response object.
	 * @throws ServerException
	 *   whenever an internal server error occurs.
	 * @throws ApplicationException
	 *   when the action has no way of handling an application error
	 *   the ordinary way; the exception will bubble up to the 
	 *   "pseudo-servlet" layer and will be handled there. 
	 */
	protected void service(Request request, Response response) throws ServerException, ApplicationException {
		
		HttpQuery query = request.getHttpQuery();
		
		String actionName = parseActionName(query.getQueryString());
		
		logger.debug("servicing action \"" + actionName + "\"");
		
		// create the action
		Action action = actionFactory.makeAction(actionName);
			
		// clear the notifications thread-local storage, 
		// if it is not null
		Notifications.getInstance().clear();
			
		// inject the input from the HTTP query parameters
		injectInputs(action, request);

		// inject dependencies if needed
		if(action instanceof RequestAware) {
			((RequestAware)action).setRequest(request);
		}
		if(action instanceof ResponseAware) {
			((ResponseAware)action).setResponse(response);
		}
		if(action instanceof ModelAware) {
			((ModelAware)action).setModel(model);
		}
				
		// validate, if needed and supported
		String result = null;
		Map<String, Object> outputs = new HashMap<String, Object>();
		if(action instanceof ValidationAware) {
			boolean valid = ((ValidationAware)action).validate();
			if(!valid) {
				// NOTE: if validation fails, control goes back to the
				// input view; in this case, if possible, all input 
				// values should be resent to the view so it can choose 
				// what to do with them, whether to send them back to the
				// user or throw then into the bin; this means that the
				// "outputs" are in reality retrieved from the input
				// fields, as no actual action execution has occurred!
				extractInputs(action, outputs);
				logger.warn("invalid input, forwarding to input");
				result = Action.INPUT;				
			}
		}
		
		if(result != Action.INPUT) {
			result = action.execute();
			extractOutputs(action, outputs);
		}

		if(result != Action.COMPLETE) {
			// now get the renderer and send the response back to the user
			Renderer renderer = rendererFactory.makeRenderer(action, result);
			if(renderer instanceof Localised) {
				((Localised)renderer).setMessageProvider(messageProvider);
			}
			renderer.setVariables(outputs);		
			renderer.render(response);
		}
		// clear the notifications; this is not called
		// if an exception is thrown, but the thread local 
		// storage is cleaned at the beginning of the method
		// anyway (the storage might be null...)
		Notifications.getInstance().clear();
	}

	/**
	 * Parse query string to find the action name.
	 * 
	 * @param queryString
	 *   the input query string.
	 * @return 
	 *   the action name.
	 */
	private String parseActionName(String queryString) {
		logger.debug("retrieving action name from \"" + queryString + "\", root context is \"" + rootActionPath + "\"");
		int idx = queryString.indexOf(rootActionPath);
		if(idx == -1) {
			logger.error("invalid action root context in request");
			return null;
		}
		//idx = queryString.indexOf("action");
		String buffer = queryString.substring(idx + rootActionPath.length());
		logger.debug("action name (so far): \"" + buffer + "\"");
		if(buffer.startsWith("/")) {
			buffer = buffer.substring(1);
		}
		logger.debug("action name (so far): \"" + buffer + "\"");
		return buffer;
	}
	
	/**
	 * Injects the input values from the request into the object.
	 *  
	 * @param action
	 *   the action being injected.
	 * @param request
	 *   the request holding the parameters.
	 * @throws ApplicationException
	 */
	private void injectInputs(Action action, Request request) throws ApplicationException {
		Field [] fields = action.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			try {
				In in = field.getAnnotation(In.class);
				if(in != null) {
					String fieldName = field.getName();
					String parameter = in.value();
					boolean mandatory = in.required();
					String value = null;
					
					if(parameter.equals("")) {
						parameter = fieldName;						
					}
					logger.debug("field \"" + fieldName + "\" mapped to parameter \"" + parameter + 
							"\" (parameter is " + (mandatory ? "mandatory)" : "not mandatory)"));					
					
					value = request.getHttpQuery().getParameter(parameter);
					if(mandatory && value == null) {
						logger.error("mandatory parameter \"" + parameter + "\" has no value specified");
						throw new ApplicationException("mandatory parameter \"" + parameter + "\" has no value specified");
					}
					
					if(!field.isAccessible()) {
						field.setAccessible(true);
						field.set(action, value);
						field.setAccessible(false);
					} else {
						field.set(action, value);
					}
	
				} else {
					logger.debug("field \"" + field.getName() + "\" has no input annotation");
				}			
			} catch (IllegalAccessException e) {
				logger.error("illegal access setting field " + field.getName(), e);
				throw new ApplicationException("error setting field " + field.getName(), e);
			} catch (IllegalArgumentException e) {
				logger.error("ilegal argument setting field " + field.getName(), e);
				throw new ApplicationException("error setting field " + field.getName(), e);
			}
		}
	}
		
	/**
	 * Extracts output from the action's annotated fields.
	 * 
	 * @param action
	 *   the action being inspected.
	 * @param outputs
	 *   a map that will contain the name of the parameters and the 
	 *   corresponding value.
	 * @throws ApplicationException
	 */
	private void extractOutputs(Action action, Map<String, Object> outputs) throws ApplicationException {
		Field [] fields = action.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			try {
				logger.debug("analysing field \"" + field.getName() + "\"...");
				Out out = field.getAnnotation(Out.class);
				if(out != null) {
					String fieldName = field.getName();
					String parameter = out.value();
					Object value = null;
					
					if(parameter.equals("")) {
						parameter = fieldName;						
					}
					logger.debug("field \"" + fieldName + "\" mapped to output \"" + parameter  + "\"");					
					
					if(!field.isAccessible()) {
						field.setAccessible(true);
						value = field.get(action);
						field.setAccessible(false);
					} else {
						value = field.get(action);
					}
					logger.debug("adding parameter \"" + parameter + "\" to output, value is \"" + value + "\"");
					outputs.put(parameter, value);
	
				} else {
					logger.debug("field \"" + field.getName() + "\" has no output annotation");
				}			
			} catch (IllegalAccessException e) {
				logger.error("illegal access setting field " + field.getName(), e);
				throw new ApplicationException("error setting field " + field.getName(), e);
			} catch (IllegalArgumentException e) {
				logger.error("ilegal argument setting field " + field.getName(), e);
				throw new ApplicationException("error setting field " + field.getName(), e);
			}
		}
	}
	
	/**
	 * Extracts input fields from the action's <code>In</code> annotated fields.
	 * 
	 * @param action
	 *   the action being inspected.
	 * @param inputs
	 *   a map that will contain the name of the input fields and the 
	 *   corresponding value.
	 * @throws ApplicationException
	 */
	private void extractInputs(Action action, Map<String, Object> inputs) throws ApplicationException {
		Field [] fields = action.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			try {
				logger.debug("analysing field \"" + field.getName() + "\"...");
				In in = field.getAnnotation(In.class);
				if(in != null) {
					String key = field.getName();
					Object value = null;
					if(!field.isAccessible()) {
						field.setAccessible(true);
						value = field.get(action);
						field.setAccessible(true);
					} else {
						value = field.get(action);
					}
					logger.debug("field \"" + key + "\" mapped back to input with value \"" + value  + "\"");
					inputs.put(field.getName(), field.get(action));
				} else {
					logger.debug("field \"" + field.getName() + "\" has no input annotation");
				}			
			} catch (IllegalAccessException e) {
				logger.error("illegal access setting field " + field.getName(), e);
				throw new ApplicationException("error setting field " + field.getName(), e);
			} catch (IllegalArgumentException e) {
				logger.error("ilegal argument setting field " + field.getName(), e);
				throw new ApplicationException("error setting field " + field.getName(), e);
			}
		}
	}
}
