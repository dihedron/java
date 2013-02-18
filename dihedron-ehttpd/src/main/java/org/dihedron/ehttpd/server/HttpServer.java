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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ClosedByInterruptException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

import org.dihedron.ehttpd.exceptions.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the basic server functionality,
 * listening on a user defined or random port, accepting
 * connections and passing them over to the higher layers 
 * in the protocol and application stack.
 * 
 *  @author Andrea Funto'
 */
public class HttpServer implements Runnable {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(HttpServer.class);
	
	/**
	 * The maximum number of attempts to be made when
	 * binding the server to a network port.
	 */
	private static final int MAX_BIND_ATTEMPTS = 10;
	
	/**
	 * Specifies the default behaviour for incoming connections
	 * acceptance: by default all connections are accepted, even
	 * when coming from a different host.
	 */
	public static final boolean DEFAULT_ALLOW_LOOPBACK_ONLY = false;
	
	/**
	 * Specifies the default behaviour with respect to SSL connections: 
	 * by default SSL connections are not enabled.
	 */
	public static final boolean DEFAULT_USE_SECURE_CONNECTIONS = false;
	
	/**
	 * Specifies the default behaviour with respect to delayed 
	 * server socket binding: by default the binding is delayed
	 * to the moment when the server thread starts (run() method).
	 */
	public static final boolean DEFAULT_USE_DELAYED_BINDING = true;
	
	/**
	 * Indicates that the server should pick the first available 
	 * port to listen on.
	 */
	public static final int AUTOMATIC_PORT_BINDING = 0;
	
	/**
	 * Indicates that there should be no limit to the number of
	 * independent requests being served: a new thread is spawned for
	 * each new request.
	 */
	public static final int UNLIMITED_POOL_SIZE = 0;
	
	/**
	 * The server socket, listening for incoming connections.
	 */
	private ServerSocket server = null;
	
	/**
	 * The controller, which dispatches requests to the proper
	 * registered handlers.
	 */
	private RequestDispatcher dispatcher;
	
	/**
	 * A boolean used to cancel the server execution.
	 */
	private boolean stopped = false;

	/**
	 * The server port; this can be assigned by the user and then
	 */
	private Integer port = new Integer(AUTOMATIC_PORT_BINDING);
	
	/**
	 * The thread pool.
	 */
	private ExecutorService executor = null;
		
	/**
	 * Whether the server should only accept requests coming
	 * from the local host.
	 */
	private boolean loopbackOnly = DEFAULT_ALLOW_LOOPBACK_ONLY;
	
	/**
	 * Whether the server should use an SSL socket for its
	 * HTTP traffic (HTTPS).
	 */
	private boolean secureConnections = DEFAULT_USE_SECURE_CONNECTIONS;
	
	/**
	 * Indicates that the server socket is already bound. 
	 */
	private boolean bound = !DEFAULT_USE_DELAYED_BINDING;
	
	/**
	 * The size of the thread pool; if <code>UNLIMITED_POOL_SIZE</code>
	 * is specified (default), a new thread is spawned upon each new request.
	 */
	private int poolSize = UNLIMITED_POOL_SIZE;
		
	/**
	 * Constructor; by default it accepts connections coming from
	 * all clients (not only the local host) and uses plain HTTP,
	 * unsecured connections; the server socket is not bound.
	 *
	 * @param port
	 *   the port to bind the server to, on which the server will
	 *   listen for incoming connections; if 0 is specified here,
	 *   the server will connect to the first available TCP port.
	 * @param dispatcher
	 *   the application controller, which is responsible of routing
	 *   requests to the appropriate, registered handlers.
	 */
	public HttpServer(int port, RequestDispatcher dispatcher) throws ServerException {
		this(port, dispatcher, DEFAULT_USE_DELAYED_BINDING);
	}

	/**
	 * Constructor; by default it accepts connections coming from
	 * all clients (not only the local host) and uses plain HTTP,
	 * unsecured connections.
	 *
	 * @param port
	 *   the port to bind the server to, on which the server will
	 *   listen for incoming connections; if 0 is specified here,
	 *   the server will connect to the first available TCP port.
	 * @param dispatcher
	 *   the application controller, which is responsible of routing
	 *   requests to the appropriate, registered handlers.
	 * @param delayBinding
	 *   when <code>true</code> the server socket binding process is 
	 *   delayed to the moment when the worker thread is started, otherwise
	 *   it's performed straight away.
	 */
	public HttpServer(int port, RequestDispatcher dispatcher, boolean delayBinding) throws ServerException {
		this.port = new Integer(port);
		this.dispatcher = dispatcher;
		this.loopbackOnly = DEFAULT_ALLOW_LOOPBACK_ONLY;
		this.secureConnections = DEFAULT_USE_SECURE_CONNECTIONS;
		this.poolSize = UNLIMITED_POOL_SIZE;
		
		if(!delayBinding) {
			logger.info("server starting...");
			server = createServerSocket();
			logger.info("server open for business...");
			bound = true;
		}
	}
	
	/**
	 * Retrieves the current dispatcher.
	 * 
	 * @return
	 *   the current dispatcher.
	 */
	public RequestDispatcher getRequestDispatcher() {
		return dispatcher;
	}

	/**
	 * Sets whether the server should accept incoming connections 
	 * only from the local host (loop-back network interface).
	 * 
	 * @param loopbackOnly
	 *   whether the server should accept incoming connections only
	 *   from the local host (loop-back network interface).
	 */   
	public void setAllowLoopbackOnly(boolean loopbackOnly) {
		this.loopbackOnly = loopbackOnly;
	}
	
	/**
	 * Retrieves whether the server should accept incoming connections 
	 * only from the local host (loop-back network interface).
	 * 
	 * @return
	 *   whether the server should accept incoming connections only
	 *   from the local host (loop-back network interface).
	 */   
	public boolean isAllowLoopbackOnly() {
		return loopbackOnly;
	}
	
	/**
	 * Sets whether the server should use a secured, SSL socket 
	 * for incoming connections; a keystore must have been defined
	 * as follows:<pre>
	 * /path/to/java 
	 * 		-Djavax.net.ssl.keyStore=mySrvKeystore 
	 * 		-Djavax.net.ssl.keyStorePassword=123456 
	 * 		MyServer
	 * </pre>
	 * 
	 * @param secureConnections
	 *   whether the server should use a secured, SSL socket 
	 *   for incoming connections.
	 */   
	public void setUseSecureConnections(boolean secureConnections) {
		this.secureConnections = secureConnections;
	}
	
	/**
	 * Retrieves whether the server should use a secured, SSL socket 
	 * for incoming connections.
	 * 
	 * @return
	 *   whether the server should use a secured, SSL socket 
	 *   for incoming connections.
	 */   
	public boolean isUseSecureConnections() {
		return secureConnections;
	}	
	
	/**
	 * Sets the port on which the server is listening.
	 * 
	 * @param port
	 *   the port on which the server is listening.  
	 */
	private void setPort(int port) {
		synchronized(this.port) {
			this.port = port;
		}
	}
	
	/**
	 * Retrieves the port on which the server is listening.
	 * 
	 * @return
	 *   the port on which the server is listening.
	 */
	public int getPort() {
		synchronized(port) {
			return port;
		}
	}
	
	/**
	 * Sets the pool size; if <code>UNLIMITED_POOL_SIZE</code>
	 * is specified, a new thread is spawned upon each new
	 * request, otherwise a thread pool is instantiated and used.
	 * 
	 * @param poolSize
	 *   the new size of the thread pool.
	 */
	public void setThreadPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
	
	/**
	 * Stops the server execution at the next incoming request.
	 */
	public void stop() {
		stopped = true;
	}

	/**
	 * Actual implementation of the server: accepts incoming connections,
	 * checks if they are legitimate, and if so hands control over to the
	 * dispatcher, in a separate thread.
	 */
	public void run() {
		if(!bound) {
			logger.info("server starting...");
			try {
				long startup = System.currentTimeMillis();
				server = createServerSocket();
				logger.info("server open to incoming requests (started in {} ms}", System.currentTimeMillis() - startup);
			} catch(ServerException e) {
				logger.error("error starting server", e);
				return;
			}
		}
		
		if(poolSize > UNLIMITED_POOL_SIZE) {
			logger.info("running the server with a thread pool with {} threads", poolSize);
			executor = Executors.newFixedThreadPool(poolSize);
		}

	    while(!stopped) {
		    try {
		        Socket client = server.accept();
		        InetAddress remote = client.getInetAddress();
		        logger.info("incoming connection from {}:{}", remote, client.getPort());
		        if(loopbackOnly && !remote.isLoopbackAddress()) {
		        	logger.warn("SECURITY: remote connection attempted from {}:{}", remote, client.getPort());
		        	client.close();
		        	continue;
		        }
		        
		        HttpServerWorker worker = new HttpServerWorker(client, dispatcher);
		        if(executor == null) {	
		        	logger.info("spawning a new thread to service the request");
		        	new Thread(worker).start();
		        } else {
		        	logger.info("passing the incoming request to the thread pool");
		        	executor.execute(worker);
		        }
		    } catch (ClosedByInterruptException e) {
		    	// this handles more gracefully a server shutdown
		    	if(Thread.currentThread().isInterrupted()) {
		    		logger.info("server is shutting down...");
		    		try {
						server.close();
					} catch (IOException ex) {
						logger.error("error closing server socket", ex);
					}
		    	}
		    } catch (IOException e) {
		        logger.error("accept failed on port " + getPort(), e);
		    }
	    }
	}
	
	/**
	 * Creates a server socket, in plain or secure mode (HTTPS),
	 * and binds it to the port provided in the <code>port</code> 
	 * field; populates the same field with the actual port to which 
	 * the server was bound.
	 * If the <code>port</code> field contains 0, this methods binds 
	 * the server to the first available TCP port (chosen at random); 
	 * if it contains a non-null value, it attempts to bind it to the 
	 * specific port, and if it fails proceeds with the next 
	 * <code>MAX_BIND_ATTEMPTS</code> following values. 
	 *  
	 * @throws ServerException
	 *   if it cannot bid the server to any port.
	 */
	private ServerSocket createServerSocket() throws ServerException {
		
		ServerSocketFactory factory;
		if(secureConnections) {
			factory = SSLServerSocketFactory.getDefault();
		} else {
			factory = ServerSocketFactory.getDefault();
		}
		
		ServerSocket server = null;
		if(getPort() == 0) {
			logger.debug("binding server to randomly chosen port...");
			try {
				server = factory.createServerSocket(0);			    
			    int port = server.getLocalPort();
			    setPort(port);
			    logger.info("server bound to port {}", getPort());
			} catch (IOException e) {
			    logger.error("error binding server to random port", e);
			    throw new ServerException("error binding server to random port", e);
			}
		} else {
			for(int i = 0; i < MAX_BIND_ATTEMPTS; ++i) {
				try {	
					logger.trace("trying to bind server to port {} [attempt no. {}]...", (getPort() + i), (i + 1));
					server = factory.createServerSocket(getPort() + i);			    
				    int port = server.getLocalPort();
				    setPort(port);
				    logger.info("server bound to port " + getPort());
				    break;
				} catch (IOException e) {
				    logger.error("error binding server to port " + (getPort() + i), e);
				    if(i == MAX_BIND_ATTEMPTS) {
				    	logger.error("maximum number of bind attempts reached, stopping server");
				    	throw new ServerException("maximum number of bind attempts reached", e);
				    }
				}
			}
		}
		return server;
	}
}
