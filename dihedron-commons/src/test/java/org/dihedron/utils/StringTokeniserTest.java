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
package org.dihedron.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Andrea Funto'
 */
public class StringTokeniserTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.dihedron.utils.StringTokeniser#tokenise(java.lang.String)}.
	 */
	@Test
	public void testTokenise() {
		StringTokeniser tokeniser = new StringTokeniser(";");
		tokeniser.setTrimSpaces(true);
		String[] strings = tokeniser.tokenise("test01;test02; test03;test04\t;   test05  ; test06  ");
		assertTrue(strings.length==6);
		assertTrue(strings[0].equals("test01"));
		assertTrue(strings[1].equals("test02"));
		assertTrue(strings[2].equals("test03"));
		assertTrue(strings[3].equals("test04"));
		assertTrue(strings[4].equals("test05"));
		assertTrue(strings[5].equals("test06"));

		tokeniser.setTrimSpaces(false);
		strings = tokeniser.tokenise("test01;test02; test03;test04\t;   test05  ; test06  ");
		assertTrue(strings.length==6);
		assertTrue(strings[0].equals("test01"));
		assertTrue(strings[1].equals("test02"));
		assertTrue(strings[2].equals(" test03"));
		assertTrue(strings[3].equals("test04\t"));
		assertTrue(strings[4].equals("   test05  "));
		assertTrue(strings[5].equals(" test06  "));

		tokeniser = new StringTokeniser("--");
		tokeniser.setTrimSpaces(false);
		strings = tokeniser.tokenise("test01--test02-- test03--test04\t --   test05  -- test06  --");
		assertTrue(strings.length==6);
		assertTrue(strings[0].equals("test01"));
		assertTrue(strings[1].equals("test02"));
		assertTrue(strings[2].equals(" test03"));
		assertTrue(strings[3].equals("test04\t "));
		assertTrue(strings[4].equals("   test05  "));
		assertTrue(strings[5].equals(" test06  "));
		
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.dihedron.utils.StringTokeniser#hasNext()}.
	 */
	@Test
	public void testHasNext() {
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.dihedron.utils.StringTokeniser#next()}.
	 */
	@Test
	public void testNext() {
//		fail("Not yet implemented");
	}

}
