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
package org.dihedron.strings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Andrea Funto'
 */
public class StringsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.dihedron.strings.Strings#isValid(java.lang.String)}.
	 */
	@Test
	public void testIsValid() {
		assertTrue(Strings.isValid(" valid "));
		assertTrue(Strings.isValid(" _ "));
		assertFalse(Strings.isValid("    "));
		assertFalse(Strings.isValid("\t"));
		assertFalse(Strings.isValid(null));
	}

	/**
	 * Test method for {@link org.dihedron.strings.Strings#trim(java.lang.String)}.
	 */
	@Test
	public void testTrim() {
		assertTrue(Strings.trim(" string ").equals("string"));
		assertTrue(Strings.trim(null) == null);
	}

	/**
	 * Test method for {@link org.dihedron.strings.Strings#concatenate(java.lang.String[])}.
	 */
	@Test
	public void testConcatenate() {
		assertTrue(Strings.concatenate("hello", "world", null, "!").equals("helloworld!"));
	}

	/**
	 * Test method for {@link org.dihedron.strings.Strings#firstValidOf(java.lang.String[])}.
	 */
	@Test
	public void testFirstValidOf() {
		assertTrue(Strings.firstValidOf(" ", "\t", null, "first", "second").equals("first"));
	}

}
