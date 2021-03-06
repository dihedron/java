/**
 * Copyright (c) 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron Common Utilities library ("Commons").
 *
 * "Commons" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "Commons" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "Commons". If not, see <http://www.gnu.org/licenses/>.
 */
package org.dihedron.reflection;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Andrea Funto'
 */
public class ReflectorTest {
	/**
	 * The logger.
	 */
//	private final static Logger logger = LoggerFactory.getLogger(ReflectorTest.class);

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.dihedron.reflection.Reflector#isSubClassOf(java.lang.Class)}.
	 */
	@Test
	public void testIsSubClassOf() {
		Map<String, String> map = new TreeMap<String, String>();
		Reflector reflector = new Reflector().applyTo(map);
		assertTrue(reflector.isSubClassOf(Map.class));
	}

}
