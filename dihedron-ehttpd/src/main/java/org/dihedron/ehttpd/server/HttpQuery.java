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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.dihedron.ehttpd.exceptions.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an HTTP request; instances of the class are created from
 * the first line in aHTTP request, which typically runs as follows:<code>
 * GET /my/path/to/the/resource.php?param1=value1&param2=&param3=value3 HTTP/1.1
 * </code>
 * After the parsing the request is split into:<ul>
 * <li><b>method</b>: in this case would be "GET"</li>
 * <li><b>path</b>: in this case would be "/my/path/to/the/"</li>
 * <li><b>resource</b>: in this case would be "resource.php"</li>
 * <li><b>parameters</b>: in this case it would be a map of keys and values,
 * containing param1, param2 (empty) and param3</li>
 * <li><b>protocol</b>: in this case would be "HTTP/1.1"</li>
 * </ul>.
 * 
 * @author Andrea Funto'
 */
public class HttpQuery {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(HttpQuery.class);
	
	/**
	 * The request method (GET, POST, DELETE, PUT).
	 */
	private Method method = null;
	
	/**
	 * The query string, including resource name and (optional) parameters.
	 */
	private String queryString;
	
	/**
	 * The path to the resource.
	 */
	private String path;
	
	/**
	 * The resource.
	 */
	private String resource;
	
	/**
	 * The schema part (HTTP...).
	 */
	private String protocol = null;
	
	/**
	 * A map of parameters, defined by name and value.
	 */
	private Map<String, String> parameters = null;
	
	/**
	 * The request headers (including the Cookies in raw form).
	 */
	protected Map<String, String> headers = null;
	
	/**
	 * A map of (name, value) containing all the cookies
	 * coming from the browser.
	 */
	protected Map<String, String> cookies = null;
	
	/**
	 * Constructor.
	 */
	public HttpQuery() {		
	}
	
	public HttpQuery parse(InputStream input) throws ServerException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			// parse the first line in the request
			parseRequestLine(reader);
			
			// now parse additional headers
			parseHeaders(reader);
			
			// last, parse cookies
			parseCookies();
			
		} catch (IOException e) {
			logger.error("error reading request from input stream", e);
			throw new ServerException("error reading request from stream", e);
		}
		return this;
	}

	/**
	 * Returns the protocol.
	 * 
	 * @return
	 *   the protocol.
	 */
	public String getProtocol() {
		return protocol;
	}
	
	/**
	 * Sets a new value for the protocol; this is only used by the 
	 * <code>RequestDispatcher/code> to override values coming from the client.
	 * 
	 * @param protocol
	 *   the new value for the protocol.
	 */
	void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	/**
	 * Returns the method.
	 * 
	 * @return
	 *   the method.
	 */
	public Method getMethod() {
		return method;
	}
	
	/**
	 * Sets a new value for the HTTP method; this is only used by the 
	 * <code>RequestDispatcher/code> to override values coming from the client.
	 * 
	 * @param method
	 *   the new value for the method.
	 */
	void setMethod(Method method) {
		this.method = method;
	}
		
	/**
	 * Returns the query string, including the parameters.
	 * 
	 * @return
	 *   the query string, including the parameters.
	 */
	public String getQueryString() {
		return queryString;
	}
	
	/**
	 * Sets a new value for the query string and for the path, resource and
	 * parameters, as a by-product; this method is only used by the 
	 * <code>RequestDispatcher/code> to override values coming from the client.
	 * 
	 * @param query
	 *   the new value for the query string.
	 */
	void setQueryString(String query) {
		parseQueryString(query);
	}
	
	/**
	 * Returns the path to the resource.
	 * 
	 * @return
	 *   the path to the resource.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Returns the name of the resource, with no path.
	 * 
	 * @return
	 *   the name of the resource, without path.
	 */
	public String getResource() {
		return resource;
	}
	
	/**
	 * Returns whether there are parameters to the request.
	 * 
	 * @return
	 *   whether there are parameters to the request.
	 */
	public boolean hasParameters() {
		return parameters != null && parameters.size() > 0;
	}
	
	/**
	 * Returns a set containing the names of the
	 * parameters. 
	 * 
	 * @return
	 *   the names of the parameters.
	 */
	public Set<String> getParameterKeys() {
		if(parameters != null) {
			return parameters.keySet();
		}
		return null;
	}
	
	/**
	 * Returns a parameter value, given its name.
	 * 
	 * @param key
	 *   the name of the parameter.
	 * @return
	 *   the value of the parameter.
	 */
	public String getParameter(String key) {
		if(key != null && parameters != null) {
			return parameters.get(key);
		}
		return null;
	}

	/**
	 * Returns a copy of the parameters.
	 * 
	 * @return
	 *   a Map with a copy of the request parameters.
	 */
	public Map<String, String> getParameters() {
		Map<String, String> result = new HashMap<String, String>();
		result.putAll(parameters);
		
		return result;
	}

	/**
	 * Returns the request headers.
	 * 
	 * @return
	 *   the request headers.
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Returns the cookies.
	 * 
	 * @return
	 *   the request cookies.
	 */
	public Map<String, String> getCookies() {
		return cookies;
	}
	
	/**
	 * Converts the query back into its URL.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(queryString);
		if(parameters != null) {
			boolean first = true;
			
			for(Entry<String,String> entry : parameters.entrySet()) {
				if(first) {
					sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
					first = false;
				} else {
					sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * Parses a request, as per the HTTP standard,
	 * and translates it into the object's internal state.
	 * 
	 *
	 * @param line
	 *   a line of text containing the URL.
	 * @return
	 *   the object itself, for method chaining.
	 */
	private HttpQuery parseRequestLine(BufferedReader input) throws IOException {
				
		String line = input.readLine();
		
		logger.debug("analysing first request line: '" + line + "'");

		parameters = new HashMap<String, String>();
		
		logger.trace("analysing line: '{}'", line);
		if(line != null) {
			StringTokenizer tokeniser = new StringTokenizer(line);
			method = Method.makeMethod(tokeniser.nextToken());
			String query = tokeniser.nextToken();
			protocol = tokeniser.nextToken();
			
			parseQueryString(query);
			
			logger.info("protocol     : '{}'", protocol);
			logger.info("http method  : '{}'", method);
			logger.info("query string : '{}'", queryString);
			logger.info("parameters   : ");
			
			if(parameters != null) {
				
				for (Entry<String, String> parameter : parameters.entrySet()) {
					logger.info(" - '{}':='{}'", parameter.getKey(), parameter.getValue());
				}
			} else {
				logger.info(" - none");
			}
		}
		return this;
	}

	/**
	 * Parses a query string, tokenising it into path, resource and parameters.
	 * 
	 * @param query
	 *   the query string, as a relative URL
	 */
	private void parseQueryString(String query) {

		if(query != null) {
			logger.trace("analysing query: '{}'", query);
			int index = query.indexOf("?");
			if(index == -1) {
				queryString = query;
				logger.trace("query string: '{}', no parameters", queryString);
			} else {
				queryString = query.substring(0, index);
				logger.trace("query string: '{}', with parameters", queryString);
				query = query.substring(index + 1).trim();
				
				// analyse parameters
				logger.trace("parameters: '{}'", query);
				if(query.length() > 0) {										
					StringTokenizer tokeniser = new StringTokenizer(query, "&");
					while(tokeniser.hasMoreElements()) {
						String parameter = tokeniser.nextToken();
						logger.trace("analysing parameter: '{}'", parameter);
						index = parameter.indexOf('=');
						if(index != -1) {
							// parameter is of the form "key=value"							
							String key = parameter.substring(0, index);
							String value = parameter.substring(index + 1);
							logger.trace("'{}':='{}'", key, value);
							parameters.put(key, value);
						} else {
							// parameter has only the key
							logger.trace("'{}':= '<null>'", parameter);
							parameters.put(parameter, "");								
						}
					}
				}
			}
			
			index = queryString.lastIndexOf("/");
			if(index != -1) {
				path = queryString.substring(0, index);
				resource = queryString.substring(index + 1);
			} else {
				resource = queryString;
			}				
		}
	}
	
	/**
	 * Parses the request headers.
	 * @param input
	 * @return
	 *   the object itself, for method chaining.
	 * @throws IOException
	 */
	private HttpQuery parseHeaders(BufferedReader input) throws IOException {
		// parse the headers
		logger.debug("parsing headers");
		
		headers = new HashMap<String, String>();
		
		while (input.ready()) {
			// read the HTTP complete HTTP Query			
			String line = input.readLine();
			logger.debug("parsing header: '" + line + "'");
			int index = line.indexOf(":");
			if(index != -1) {
				String header = line.substring(0, index).trim();
				String value = line.substring(index + 1).trim();
				headers.put(header, value);
				logger.debug("adding header: '" + header + "' := '" + value + "'");
			} else {
				if(line.trim().length() == 0) {
					logger.debug("end of headers found");
				} else {
					logger.warn("invalid header encountered");
				}
				break;
			}			
		}
		return this;
	}
	
	/**
	 * Parses the <code>Cookie</code> header, identifying
	 * cookies and storing them into a map. This method <b>
	 * must</b> be called after the <code>parseHeaders()</code>
	 * since it relies on the headers map being already
	 * populated.
	 * 
	 * @return
	 *   the object itself, for method chaining.
	 */
	private HttpQuery parseCookies() {
		
		logger.debug("parsing cookies");
		
		cookies = new HashMap<String, String>();
		
		String header = headers.get("Cookie");
		
		if(header != null && header.length() > 0) {			
			logger.debug("cookie: \"" + header + "\"");
			StringTokenizer tokeniser = new StringTokenizer(header, ";");
			while(tokeniser.hasMoreTokens()) {
				String token = tokeniser.nextToken();
				logger.debug("token: \"" + token + "\"");
				int index = token.indexOf('=');
				if(index != -1) {
					// cookie is of the form "key=value"							
					String key = token.substring(0, index);
					String value = token.substring(index + 1);
					if(key != null && value != null) {
						logger.debug("adding cookie: '" + key + "' := '" + value + "'");
						cookies.put(key.trim(), value.trim());
					}
				}
			}
		}
		return this;
	}	
}
