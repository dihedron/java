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

import static org.junit.Assert.fail;

import org.apache.log4j.BasicConfigurator;
import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.server.handlers.impl.ResourceRequestHandler;
import org.dihedron.ehttpd.server.resources.FileStaticResource;
import org.dihedron.ehttpd.server.session.FakeSessionGenerator;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerTest {
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(HttpServerTest.class);

	@Before
	public void setUp() throws Exception {
		//BasicConfigurator.configure();
	}
	
	@Test
	public void testHttpServerIntRequestDispatcher() {
		
		try {
			logger.debug("starting the server");
			RequestDispatcher dispatcher = new RequestDispatcher(new FakeSessionGenerator())
				.handle("\\/log4j\\.properties").through(new ResourceRequestHandler(new FileStaticResource("text/plain", "/home/andrea/Desktop/workspace_git/java/dihedron-ehttpd/src/test/resources/log4j.properties")))
				.on(Exception.class).goTo("/home/andrea/Downloads/error.html");
					//.addRequestHandler("\\/errors\\/.*Exception\\.do", )
					//.addDefaultApplicationErrorHandler()
					//.addApplicationErrorHandler(new AccessDeniedHandler____())
					//.addRequestHandler("\\/html-5-icon\\.png", new ResourceRequestHandler(new FileStaticResource("image/png", "/home/andrea/Downloads/html-5-icon.png")))
//					.addRequestHandler("\\/log4j\\.xml", new ResourceRequestHandler(new FileStaticResource("text/xml", "/log4j.xml")))
//					.addErrorMapping(Exception.class, "/home/andrea/Downloads/error.html");
				
			HttpServer server = new HttpServer(9080, dispatcher);
			server.run();
		} catch (ServerException e) {
			fail("server raised an exception");
		} catch (ApplicationException e) {
			fail("application raised an exception");
		}
	}

}
