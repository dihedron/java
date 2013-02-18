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
import java.util.Set;

import org.dihedron.ehttpd.exceptions.ServerException;



/**
 * Maps the HTTP request, converting the raw bytes
 * into headers, cookies and request parameters.
 */
public class Request {
	
	/**
	 * The query object.
	 */
	protected HttpQuery query = null;
		
	/**
	 * The input stream associated with the request.
	 */
	protected InputStream stream = null;
	
	/**
	 * The remote host, at the client side.
	 */
	protected String remoteHost = null;
	
	/**
	 * The remote client's network address.
	 */
	protected String remoteAddr = null;
	
	/**
	 * The remote client's port.
	 */
	protected int remotePort = 0;
	
	/**
	 * The local host at the server.
	 */
	protected String localHost = null;
	
	/**
	 * The network address at the server that accepted the connection.
	 */
	protected String localAddr = null;
	
	/**
	 * The local connection port at the server.
	 */
	protected int localPort = 0;
	
	/**
	 * A store for parameters that must be passed along as by-products of the
	 * request handling.
	 */
	protected Map<String, Object> parameters = new HashMap<String, Object>();

	/**
	 * Constructor.
	 * 
	 * @param stream
	 *   the input stream.
	 * @throws IOException
	 */
	Request(InputStream stream) throws ServerException {
		
		this.stream = stream;
		
		// parse the request line
		query = new HttpQuery().parse(stream);
	}
	
	/**
	 * Sets a parameter in the request, for further down-stream handling.
	 * 
	 * @param key
	 *   the parameter name.
	 * @param value
	 *   an object representing the parameter value.
	 */
	public void setParameter(String key, Object value) {
		assert(key != null);
		if(value == null) {
			removeParameter(key);
		} else {
			parameters.put(key, value);
		}
	}
	
	/**
	 * Removes a parameter from the parameters map.
	 * 
	 * @param key
	 *   the parameter name.
	 */
	public void removeParameter(String key) {
		assert(key != null);
		parameters.remove(key);
	}
	
	/**
	 * Returns the set of all parameter names.
	 * 
	 * @return
	 *   the parameter names.
	 */
	public Set<String> getParameterKeys() {
		return parameters.keySet();
	}
	
	/**
	 * Returns the value of a parameter if available, <code>null</code> otherwise.
	 * 
	 * @param key
	 *   the name of the parameter.
	 * @return
	 *   the parameter value, or <code>null</code>.
	 */
	public Object getParameter(String key) {
		assert(key != null);
		return parameters.get(key);
	}
	
	
	void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	
	void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}
	
	void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}
	
	void setLocalHost(String localHost) {
		this.localHost = localHost;
	}

	void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}
	
	public String getRemoteHost() {
		return remoteHost;
	}	
	
	public int getRemotePort() {
		return remotePort;
	}
	
	public String getLocalAddr() {
		return localAddr;
	}
	
	public String getLocalHost() {
		return localHost;
	}	
	
	public int getLocalPort() {
		return localPort;
	}

	// TODO: not implemented
	public int getContentLength() {
		return -1;
	}
	
	// TODO: not implemented
	public String getContentType() {
		return null;
	}
	
	public InputStream getInputStream() {
		return stream;
	}
	
	public BufferedReader getReader() {
		return new BufferedReader(new InputStreamReader(stream));
	}
	
	public HttpQuery getHttpQuery() {
		return query;
	}
	
	/**
	 * This setter method is only visible within the package and is expected to
	 * be used only by the <code>RequestDispatcher forward()</code> method.
	 * It will replace the current request with one referencing the new target 
	 * and the GET method.
	 * 
	 * @param query
	 *   the new <code>HttpQuery</code> object.
	 */
	void setHttpQuery(HttpQuery query) {
		this.query = query;
	}
}
