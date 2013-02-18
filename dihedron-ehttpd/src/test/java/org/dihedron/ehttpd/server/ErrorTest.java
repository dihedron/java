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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Funto'
 */
public class ErrorTest {
	/**
	 * The logger.
	 */
	private final static Logger logger = LoggerFactory
			.getLogger(ErrorTest.class);

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Error error = new Error(Exception.class);
		assertTrue(error.isSuperClassOf(new IOException()));
		assertTrue(error.isSuperClassOf(new Exception()));
		assertFalse(error.isSuperClassOf(new Throwable()));

		assertTrue(error.isSuperClassOf(IOException.class));
		assertTrue(error.isSuperClassOf(Exception.class));
		assertFalse(error.isSuperClassOf(Throwable.class));
		//fail("Not yet implemented");
	}

}
