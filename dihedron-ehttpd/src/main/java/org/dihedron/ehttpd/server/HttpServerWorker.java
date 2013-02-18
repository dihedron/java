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

import java.io.IOException;
import java.net.Socket;

import org.dihedron.ehttpd.exceptions.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The HTTP server workhorse, accepting requests and dispatching it to service classes.
 * 
 * @author Andrea Funto'
 */
public class HttpServerWorker implements Runnable {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(HttpServerWorker.class);
	
	/**
	 * The socket connected to the client.
	 */
	private Socket client = null;
	
	/**
	 * The internal dispatcher; this object decides where to route requests.
	 */
	private RequestDispatcher dispatcher = null;
	
	/**
	 * Constructor.
	 * 
	 * @param client
	 *   the client socket.
	 * @param dispatcher
	 *   the object that decides where to route requests.
	 */
	public HttpServerWorker(Socket client, RequestDispatcher dispatcher) {
		this.client = client;
		this.dispatcher = dispatcher;
	}
	
	public void run() {
		try {
	        logger.debug("servicing client {}:{}", client.getInetAddress(), client.getPort());
	        
	        // fill the handler context
	        HandlerContext.setRequestDiaspatcher(dispatcher);
	        
	        // prepare request by reading from the input 
	        // channel on the socket, then shut it down
	        Request request = prepareRequest(client);
	        
	        // prepare the response object, which will 
	        // write to the output channel of the socket
	        Response response = prepareResponse(client);
	        
	        
	        // dispatcher will lookup and call the appropriate handler
        	dispatcher.dispatch(request, response);
	        
        	// flush the response data to stream
	        response.flush();	        
	        
	        logger.debug("client serviced");
		} catch(ServerException e) {
			logger.error("error servicing client", e);
		} finally {
			try {
		        // close streams
				client.shutdownInput();
				client.shutdownOutput();

		        // close socket
				client.close();
			} catch(IOException e) {
				logger.error("I/O exception closing streams", e);
			}
		}
	}
	
	/**
	 * Prepares a new request by reading in HTTP request line, headers
	 * and getting a reference to the input stream.
	 * 
	 * @param client
	 *   the client socket.
	 * @return
	 *   a new <code>REquest</code> object.
	 * @throws ServerException
	 */
	private Request prepareRequest(Socket client) throws ServerException {
		try {
			Request request = new Request(client.getInputStream());
	        request.setRemoteAddr(client.getInetAddress().getHostAddress());
	        request.setRemoteHost(client.getInetAddress().getCanonicalHostName());
	        request.setRemotePort(client.getPort());
	        request.setLocalAddr(client.getLocalAddress().getHostAddress());
	        request.setLocalHost(client.getLocalAddress().getCanonicalHostName());
	        request.setLocalPort(client.getLocalPort());
	        return request;
		} catch(IOException e) {
			logger.error("error preparing request object", e);
			throw new ServerException("error preparing request object", e);
		}
	}
	
	/**
	 * Prepares a response object; the output stream will be managed by the
	 * server.
	 * 
	 * @param client
	 *   the client socket.
	 * @return
	 *   a <code>Response</code> object.
	 * @throws ServerException
	 */
	private Response prepareResponse(Socket client) throws ServerException {	
		try {
			Output output = new ManagedOutput(client.getOutputStream());		
			Response response = new Response(output);		
			return response;
		} catch(IOException e) {
			logger.error("error preparing response object", e);
			throw new ServerException("error preparing response object", e);
		}
	}
	
}
