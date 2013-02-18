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

import java.io.UnsupportedEncodingException;

import org.dihedron.ehttpd.exceptions.ServerException;


/**
 * This class abstracts the response to the client. It mimics
 * the "servlet API", and provides access to the output stream.
 * 
 * @author Andrea Funto'
 */
public class Response {
	
	/**
	 * The underlying output channel.
	 */
	private Output output;
	
	/**
	 * The HTTP status.
	 */
	private Status status = Status.STATUS_200;
		
	/**
	 * Constructor.
	 * 
	 * @param output
	 *   the underlying output channel.
	 */
	public Response(Output output) {
		this.output = output;
	}

	/**
	 * Returns the underlying output channel.
	 * 
	 * @return
	 *   the underlying output channel.
	 */
	public Output getOutput() {
		return output;
	}
	
	/**
	 * Sets the HTTP status.
	 * 
	 * @param status
	 *   the HTTP status.
	 * @throws ServerException
	 *   if the status cannot be written to the output channel.
	 */
	public void setStatus(Status status) throws ServerException {
		this.status = status;
		output.writeStatus(status);
	}
	
	/**
	 * Retrieve the current HTTP status.
	 * 
	 * @return
	 *   the current HTTP status.
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * Replaces the given cookie with what is provided.
	 * All cookies set by the server are session and <code>
	 * httponly</code> cookies: the cookie looks like the
	 * following:<pre>Set-Cookie: name=value; httponly</pre>
	 * 
	 * @param name
	 *   the name of the cookie
	 * @param value
	 *   the cookie value
	 * @throws ServerException
	 */
	public void setCookie(String name, String value) throws ServerException {
		output.writeHeader("Set-Cookie", name + "=" + value + "; httponly");
	}
	
	/**
	 * Sets an HTTP header.
	 * 
	 * @param key
	 *   the header name.
	 * @param values
	 *   the new value for the header.
	 * @throws ServerException
	 *   if the header cannot be written to the output channel.
	 */
	public void setHeader(String key, String... values) throws ServerException {
		output.writeHeader(key, values);
	}
	
	/**
	 * Adds content to the output channel; the string is added 
	 * as a byte array, in the host default encoding.
	 * 
	 * @param text
	 *   the text to be added to the output channel.
	 * @throws ServerException
	 *   if the text cannot be written to the output channel.
	 */
	public void addContent(String text) throws ServerException {
		output.writeContent(text);
	}
	
	/**
	 * Adds content to the output channel; the string is added 
	 * as a byte array, in the given default encoding.
	 * 
	 * @param text
	 *   the text to be added to the output channel.
	 * @param encoding
	 *   the preferred encoding.
	 * @throws ServerException
	 *   if the text cannot be written to the output channel, or 
	 *   the given encoding is unsupported.
	 */
	public void addContent(String text, String encoding) throws ServerException {
		try {
			output.writeContent(text.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			throw new ServerException("unsupported encoding: '" + encoding + "'", e);
		}
	}
	
	/**
	 * Adds content to the output channel, as raw data.
	 * 
	 * @param data
	 *   the raw data to be written to the output.
	 * @throws ServerException
	 *   if the data cannot be written to the output channel.
	 */
	public void addContent(byte [] data) throws ServerException {
		output.writeContent(data);
	}
	
	/**
	 * Flushes the data to the output channel.
	 * 
	 * @throws ServerException
	 *   if the data cannot be flushed to the output channel.
	 */
	public void flush() throws ServerException {
		output.flush();
	}
}
