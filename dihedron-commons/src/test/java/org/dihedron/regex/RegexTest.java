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
package org.dihedron.regex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

public class RegexTest {

	@Before
	public void setUp() throws Exception {
		BasicConfigurator.configure();
	}


	@Test
	public void test() {
		Regex regex = new Regex("^pipp\\d*\\.pdf");
		assertFalse(regex.matches("pippo1.pdf"));
		assertTrue(regex.matches("pipp1.pdf"));
		assertFalse(regex.matches("pipp1.pdff"));
		assertFalse(regex.matches("pippo.pdf"));
		assertTrue(regex.matches("pipp.pdf"));
		assertFalse(regex.matches("pluto.pdf"));
		
		Regex regex2 = new Regex("^pipp\\d*\\.pdf");
		assertFalse(regex2.matches("pippo1.pdf"));
		assertTrue(regex2.matches("pipp1.pdf"));
		assertFalse(regex2.matches("pipp1.pdff"));
		assertFalse(regex2.matches("pippo.pdf"));
		assertTrue(regex2.matches("pipp.pdf"));
		assertFalse(regex2.matches("pluto.pdf"));
		
	}
}
