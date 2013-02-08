/**
 * Copyright (c) 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Object Graph Traversal Language framework ("OGTL").
 *
 * "OGTL" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "OGTL" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "OGTL". If not, see <http://www.gnu.org/licenses/>.
 */

package org.dihedron.ogtl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.dihedron.ogtl.test.Address;
import org.dihedron.ogtl.test.Email;
import org.dihedron.ogtl.test.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author d093154
 *
 */
public class ExpressionTest {
	
	private final static Logger logger = LoggerFactory.getLogger(ExpressionTest.class);
	
	private static Person person;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		person = new Person("Name", "Surname");
		person
			.addAddress(new Address("v.le Mani dal Naso", "3a", "Rome", "Italy")
				.addPhone("+001.555.123456")
				.addPhone("+0039.06.12345678"))
			.addAddress(new Address("v.le Terrone di Quà", "3b", "Berghem", "Padania")
				.addPhone("+001.555.654321")
				.addPhone("+0039.06.87654321"))
			.addEmail("work", new Email("name.surname@work.com"))
			.addEmail("personal", new Email("name.surname@personal.com"))
			.addEmail("jogging", new Email("name.surname@jogging.com"))
			.addEmail("amusement", new Email("name.surname@amusement.com"));		
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIndexed() {	
		logger.debug("================ START TEST ================");
		Expression expression;
		try {
			expression = new ExpressionCompiler().compile("addresses[1].street");
			String street = (String)expression.apply(person);
			assertTrue(street.equals("v.le Terrone di Quà"));
		} catch (LexerException e) {
			e.printStackTrace();
			fail("test did not complete");
		} catch (Exception e) {
			e.printStackTrace();
			fail("test did not complete");
		}
	}

	@Test
	public void testListsOfLists() {
		logger.debug("================ START TEST ================");
		Expression expression;
		try {
			expression = new ExpressionCompiler().compile("addresses.phones");
			@SuppressWarnings("unchecked") List<String> phones = (List<String>)expression.apply(person);
			assertTrue(phones.size() == 4);
			assertTrue(phones.get(0).equals("+001.555.123456"));
			assertTrue(phones.get(1).equals("+0039.06.12345678"));
			assertTrue(phones.get(2).equals("+001.555.654321"));
			assertTrue(phones.get(3).equals("+0039.06.87654321"));
		} catch (LexerException e) {
			e.printStackTrace();
			fail("test did not complete");
		} catch (Exception e) {
			e.printStackTrace();
			fail("test did not complete");
		}
	}
	
	@Test
	public void testLists() {
		logger.debug("================ START TEST ================");
		Expression expression;
		try {
			expression = new ExpressionCompiler().compile("addresses.number");
			@SuppressWarnings("unchecked") List<String> streets = (List<String>)expression.apply(person);
			assertTrue(streets.size() == 2);
			assertTrue(streets.get(0).equals("3a"));
			assertTrue(streets.get(1).equals("3b"));
		} catch (LexerException e) {
			e.printStackTrace();
			fail("test did not complete");
		} catch (Exception e) {
			e.printStackTrace();
			fail("test did not complete");
		}
	}

	// (field1.field2['field3']["field4"].array.4.list[3]).getAnagrafica(pippo)
	@Test
	public void testMap() {
		logger.debug("================ START TEST ================");
		Expression expression;
		try {
			expression = new ExpressionCompiler().compile("emails['work'].toString()");
			String mailto = (String)expression.apply(person);
			assertTrue(mailto.equals("mailto:name.surname@work.com"));
		} catch (LexerException e) {
			e.printStackTrace();
			fail("test did not complete");
		} catch (Exception e) {
			e.printStackTrace();
			fail("test did not complete");
		}		
	}	
}
