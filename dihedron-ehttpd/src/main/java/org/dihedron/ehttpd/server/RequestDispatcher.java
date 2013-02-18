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
package org.dihedron.ehttpd.server;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dihedron.ehttpd.exceptions.AccessDenied;
import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.dihedron.ehttpd.exceptions.InvalidConfigurationException;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.exceptions.UnsupportedRequest;
import org.dihedron.ehttpd.server.handlers.Handler;
import org.dihedron.ehttpd.server.session.SessionGenerator;
import org.dihedron.regex.Regex;
import org.dihedron.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the dispatching logic: it has a map of
 * request format templates (expressed as regular expressions) and
 * request handlers; whenever a request comes in that matches one 
 * of the given patterns, the appropriate handler is invoked. If
 * no matching handler is encountered, the request is handed over 
 * to a "default handler".  
 * 
 * @author Andrea Funto'
 */
public class RequestDispatcher {
	
	/**
	 * An intermediate class representing a regular expression and its associated 
	 * handler; this class is used to provide an expressive syntax to handlers
	 * registration.
	 *  
	 * @author Andrea Funto'
	 */
	public class Dispatch {
		/**
		 * A back-reference to the RequestDispatcher this target refers to.
		 */
		private RequestDispatcher dispatcher;
		
		/**
		 * The regular expression representing the naming patterns to which the
		 * given handler will respond.
		 */
		private Regex regex;
		
		/**
		 * Constructor.
		 *
		 * @param regex
		 *   the regular expression to which the handler will respond.
		 * @param dispatcher
		 *   the dispatcher back-reference, for method chaining.
		 * @throws ApplicationException
		 *   if the input regular expression is empty or invalid.
		 */
		private Dispatch(String regex, RequestDispatcher dispatcher) throws ApplicationException {
			assert dispatcher != null : "dispatcher must never be null";
			if(!Strings.isValid(regex)) {
				throw new InvalidConfigurationException("invalid pattern for request handler");
			}
			this.dispatcher = dispatcher;
			this.regex = new Regex(regex);
		}
		
		/**
		 * Constructor.
		 *
		 * @param regex
		 *   the regular expression to which the handler will respond.
		 * @param dispatcher
		 *   the dispatcher back-reference, for method chaining.
		 * @throws ApplicationException
		 *   if the input regular expression is empty or invalid.
		 */
		private Dispatch(Regex regex, RequestDispatcher dispatcher) throws ApplicationException {
			assert dispatcher != null : "dispatcher must never be null";
			if(regex == null) {
				throw new InvalidConfigurationException("invalid regula expression for request handler");
			}
			this.dispatcher = dispatcher;
			this.regex = regex;
		}
		
		/**
		 * Registers a request handler for the given regular expression patter.
		 * 
		 * @param handler
		 *   the request handler to be registered.
		 * @return
		 *   the RequestDispatcher, for method chaining.
		 * @throws ApplicationException
		 */
		public RequestDispatcher through(Handler handler) throws ApplicationException {
			if(handler == null) {
				throw new InvalidConfigurationException("invalid handler for pattern " + regex.getRegex());
			}
			dispatcher.handlers.put(regex, handler);
			return dispatcher;
		}
	}
	
	/**
	 * An intermediate class representing an exception and its associated 
	 * URL; this class is used to provide an expressive syntax to error
	 * forwarding registration.
	 *  
	 * @author Andrea Funto'
	 */
	public class Forward {
		/**
		 * A back-reference to the RequestDispatcher this target refers to.
		 */
		private RequestDispatcher dispatcher;
		
		/**
		 * The class of the exception this forward instruction refers to.
		 */
		private Class<? extends Throwable> clazz;
		
		/**
		 * Constructor.
		 *
		 * @param clazz
		 *   the class of the exception this instruction will refer to.
		 * @param dispatcher
		 *   the dispatcher back-reference, for method chaining.
		 * @throws ApplicationException
		 *   if the input class is invalid.
		 */
		private Forward(Class<? extends Throwable> clazz, RequestDispatcher dispatcher) throws ApplicationException {
			assert dispatcher != null : "dispatcher must never be null";
			if(clazz == null) {
				throw new InvalidConfigurationException("invalid exception class for forward instruction");
			}
			this.dispatcher = dispatcher;
			this.clazz = clazz;
		}
				
		/**
		 * Registers an URL to be called when the given exception is raised.
		 * 
		 * @param url
		 *   the URL to be called when the given exception is raised.
		 * @return
		 *   the RequestDispatcher, for method chaining.
		 * @throws ApplicationException
		 */
		public RequestDispatcher goTo(String url) throws ApplicationException {
			if(url == null) {
				throw new InvalidConfigurationException("invalid URL for exception " + clazz.getSimpleName());
			}
			dispatcher.errors.put(new Error(clazz), url);
			return dispatcher;
		}
	}	

	/** 
	 * The logger. 
	 */
	private Logger logger = LoggerFactory.getLogger(RequestDispatcher.class);
	
	/** 
	 * The registered request handlers. 
	 */
	private SortedMap<Regex, Handler> handlers;

	/**
	 * The mapping between exceptions and the handlers (as targets) that are able
	 * to handle them.
	 */
	private SortedMap<Error, String> errors;
	
	/**
	 * The session identifier generator.
	 */
	private SessionGenerator sessionGenerator;
	
	/**
	 * Constructor.
	 * 
	 * @param generator
	 *   the generator of the session identifier.
	 */
	public RequestDispatcher(SessionGenerator generator) {
		handlers = Collections.synchronizedSortedMap(new TreeMap<Regex, Handler>());
		errors = Collections.synchronizedSortedMap(new TreeMap<Error, String>());
		sessionGenerator = generator;
	}
	
	/**
	 * Performs the first half of a handler registration; the second half is a
	 * call to Dispatch's through() method, in which the actual handler has to be
	 * provided. The call patterns is as follows:
	 * <pre>
	 * dispatcher.handle("(?:your/pattern{as}+you\\.like(?:it)").through(new MyHandler(...));
	 * </pre>
	 * 
	 * @param regex
	 *   the pattern for the incoming requests.
	 * @return
	 *   a Dispatch object, on which the through() method can be called to perform
	 *   the actual handler registration.
	 * @throws ApplicationException
	 */
	public Dispatch handle(String regex) throws ApplicationException {
		return new Dispatch(regex, this);
	}
	
	/**
	 * Performs the first half of a handler registration; the second half is a
	 * call to Dispatch's through() method, in which the actual handler has to be
	 * provided. The call patterns is as follows:
	 * <pre>
	 * dispatcher.handle("(?:your/pattern{as}+you\\.like(?:it)").through(new MyHandler(...));
	 * </pre>
	 * 
	 * @param regex
	 *   the regular expression for the incoming requests.
	 * @return
	 *   a Dispatch object, on which the through() method can be called to perform
	 *   the actual handler registration.
	 * @throws ApplicationException
	 */
	public Dispatch handle(Regex regex) throws ApplicationException {
		return new Dispatch(regex, this);
	}	
	
	
	/**
	 * Performs the first half of an exception handling registration; the second 
	 * half is a call to Forward's to() method, in which the actual URL has to be
	 * provided. The call patterns is as follows:
	 * <pre>
	 * dispatcher.on(MyException.class).goTo("/my/error/url.html");
	 * </pre>
	 * 
	 * @param clazz
	 *   the class of the exception to be handled through this URL.
	 * @return
	 *   a Forward object, on which the to() method can be called to perform
	 *   the actual URL registration.
	 * @throws ApplicationException
	 */
	public Forward on(Class<? extends Throwable> clazz) throws ApplicationException {
		return new Forward(clazz, this);
	}	
	
	/**
	 * Adds a request handler.
	 * 
	 * @param regex
	 *   the regular expression representing the request to which 
	 *   the given handler will answer.
	 * @param handler
	 *   the request handler.
	 * @return
	 *   the object itself, for method chaining.
	 *
	public RequestDispatcher addRequestHandler(Regex regex, Handler handler) {
		if(regex != null && handler != null) {
			handlers.put(regex, handler);
		}
		return this;
	}
	*/
	
	/**
	 * Adds a request handler.
	 * 
	 * @param regex
	 *   the regular expression representing the request to which 
	 *   the given handler will answer.
	 * @param handler
	 *   the request handler.
	 * @return
	 *   the object itself, for method chaining.
	 *
	public RequestDispatcher addRequestHandler(String regex, Handler handler) {
		if(regex != null && handler != null) {
			handlers.put(new Regex(regex), handler);
		}
		return this;
	}
	*/

	/**
	 * Adds mapping info between an exception and the target path handler that 
	 * will for a given exception to the stack of handlers of application errors.
	 * 
	 * @param clazz
	 *   the exception class.
	 * @param target
	 *   the target resource path. 
	 * @return
	 *   the object itself, for method chaining.
	 *
	public RequestDispatcher addErrorMapping(Class<?> clazz, String target) {
		errors.put(clazz, target);
		return this;
	}	
	*/	
		
	/**
	 * Retrieves the currently registered session id
	 * generator and verifier.
	 * 
	 * @return
	 *   the currently registered session id generator.
	 */
	public SessionGenerator getSessionGenerator() {
		return sessionGenerator;
	}
	
	/**
	 * Business logic method: if forwards request to the appropriate
	 * handler, or to the default handler if no registered handler 
	 * for the given request is found.
	 * 
	 * @param target
	 *   the requested resource path
	 * @param request
	 *   the client request.
	 * @param response
	 *   the response.
	 * @throws ServerException
	 */
	public void dispatch(Request request, Response response) throws ServerException {

        // get the path to the requested resource
        String target = request.getHttpQuery().getQueryString();
				
		logger.info("request target: '{}'", target);
		
		try {			
			// check if the resource comes from a trusted source
			if(!isValidSession(request)) {
				throw new AccessDenied("no valid session identifier found");
			}
			
			// look for the appropriate handler
			Handler handler = findHandler(target);
			handler.handle(request, response);

		} catch(ApplicationException e) {
			logger.error("application error, looking for appropriate error handler");
			//String forward = errors.get(e.getClass());
			String forward = null;
			for(Entry<Error, String> entry : errors.entrySet()) {
				if(entry.getKey().isSuperClassOf(e)) {
					forward = entry.getValue();
				}
			}			
			
			if(forward != null) {
				// store the exception among the request parameters
				request.setParameter(HttpSessionKeys.EXCEPTION, e);
				
				// let the appropriate handler handle the error
				forward(forward, request, response);
			} else{
				logger.error("no application error found, bubbling error to server");
				throw new ServerException("unhandled application error", e);
			}
		
		} catch(ServerException e) {
			logger.error("server error, looking for appropriate handler");
			String forward = errors.get(e.getClass());
			if(forward != null) {
				// store the exception among the request parameters
				request.setParameter(HttpSessionKeys.EXCEPTION, e);
				
				// let the appropriate handler handle the error
				forward(forward, request, response);
			} else {
				logger.error("no server exception mapping found, bubbling error ro controller");
				throw new ServerException("unhandled application error", e);
			}
		} catch(Exception e) {
			logger.error("error while handling error", e);
		}			
	}
	
	
	/**
	 * Business logic method: if forwards request to the appropriate handler, 
	 * using the HTTP GET method and altering the request by setting the new 
	 * target resource path.
	 * 
	 * @param target
	 *   the requested resource path.
	 * @param request
	 *   the client request.
	 * @param response
	 *   the response.
	 * @throws ServerException
	 */
	public void forward(String target, Request request, Response response) throws ServerException {
		HttpQuery query = request.getHttpQuery();
		query.setMethod(Method.GET);
		query.setQueryString(target);
		request.setHttpQuery(query);
		dispatch(request, response);
	}

	/**
	 * Business logic method: if forwards request to the appropriate
	 * handler, or to the default handler if no registered handler 
	 * for the given request is found.
	 * 
	 * @param target
	 *   the requested resource path
	 * @param request
	 *   the client request.
	 * @param response
	 *   the response.
	 * @throws ServerException
	 *
	public void dispatch(String target, Request request, Response response) throws ServerException {
				
		logger.info("request target: '{}'", target);
		
		try {			
			// this outer try block serves as a guard against server errors, and it 
			// also intercepts application errors that bubble up as server errors 
			// because they could not be handled within the application
			try {					
				// the inner try block guards against application errors
				try {
					
					// check if the resource comes from a trusted source
					if(!isValidSession(request)) {
						throw new AccessDenied("no valid session identifier found");
					}
					
					// look for the appropriate handler
					Handler handler = findHandler(target);
					handler.handle(request, response);

				} catch(ApplicationException e) {
					logger.error("application error, looking for appropriate error handler");
					boolean handled = false;
					for(ErrorHandler_____ eh : applicationErrorHandlers) {
						if(eh.canHandle(e)) {
							handled = true;
							logger.debug("dispatching to application error handler");
							eh.setException(e);
							eh.onGet(request, response);
							break;
						}
					}
					if(!handled) {
						logger.error("no application error found, bubbling error to server");
						throw new ServerException("unhandled application error", e);
					}
				}
			} catch(ServerException e) {
				logger.error("server error, looking for appropriate handler");
				for(ErrorHandler_____ eh : serverErrorHandlers) {
					if(eh.canHandle(e)) {
						eh.setException(e);
						eh.onGet(request, response);
						break;
					}
				}			
			}
		} catch(Exception e) {
			logger.error("error while handling error", e);
		}			
	}
	*/
	
	/**
	 * Checks whether the session identifier provided in the
	 * request (either as a request parameter or as a cookie) is
	 * valid.
	 * 
	 * @param request
	 *   the client request.
	 * @return
	 *   <code>true</code> if the request is valid, <code>false
	 *   </code> otherwise.
	 */
	private boolean isValidSession(Request request) {
		
		String cookie = request.getHttpQuery().getCookies().get("sessionid");
		String parameter = request.getHttpQuery().getParameter("sessionid");		
		return sessionGenerator.isValid(cookie) || sessionGenerator.isValid(parameter);
	}
	
	/**
	 * Looks up the appropriate request handler for the given resource; if none
	 * is found, an <code>UnsupportedRequest</code> exception is thrown.
	 * 
	 * @param target
	 *   a string representing the requested resource path.
	 * @return
	 *   the appropriate handler if found.
	 * @throws ApplicationException
	 */
	private Handler findHandler(String target) throws ApplicationException {
		
		logger.info("request target: '{}'", target);

		Handler handler = null;
		
		// look for the appropriate handler
//		for(Regex regex : handlers.keySet()) {			
//			if(regex.matches(target)){
//				logger.info("dispatching request to handler for \"" + regex + "\"");
//				handler = handlers.get(regex);
//				break;
//			}
//		}
		for(Entry<Regex, Handler> entry : handlers.entrySet()) {
			if(entry.getKey().matches(target)) {
				logger.info("dispatching request to handler for '{}'", entry.getKey());
				handler = entry.getValue();
				break;
			}
		}
		
		// check if a handler was found: if not, then the request cannot 
		// be possibly satisfied by the application 
		if(handler == null) {
			logger.info("dispatching request to default (application) handler");
			throw new UnsupportedRequest("no handler found for request");
		}
		
		return handler;
	}
	
	/**
	 * Looks up an application error handler for the given exception; if none is
	 * found, a <code>ServerException</code> exception is thrown and the error bubbles
	 * up to the server.
	 * 
	 * @param error
	 *   the application error to be handled.
	 * @return
	 *   
	 * @throws ServerException
	 *
	private ErrorHandler_____ findApplicationErrorHandler(Exception error) throws ServerException {
		if(error != null && error instanceof ApplicationException) {
			logger.error("application error, looking for appropriate error handler");
			for(ErrorHandler_____ handler : applicationErrorHandlers) {
				if(handler.canHandle(error)) {
					logger.debug("application error handler found");
					return handler;
				}
			}
			logger.error("no application error found, bubbling error to server");
			throw new ServerException("unhandled application error", error);			
		}
		return null;
	}

	private ErrorHandler_____ findServerErrorHandler(Exception error) throws ServerException {
		if(error != null && error instanceof ApplicationException) {
			logger.error("application error, looking for appropriate error handler");
			boolean handled = false;
			for(ErrorHandler_____ handler : serverErrorHandlers) {
				if(handler.canHandle(error)) {
					logger.debug("server error handler found");
					return handler;
				}
			}
			logger.error("no server error found");			
		}
		return null;
	}
	*/	
	
}
