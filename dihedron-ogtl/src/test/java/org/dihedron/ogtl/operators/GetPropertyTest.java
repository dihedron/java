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

package org.dihedron.ogtl.operators;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Map;

import org.dihedron.ogtl.Expression;
import org.dihedron.ogtl.ExpressionCompiler;
import org.dihedron.ogtl.LexerException;
import org.dihedron.ogtl.test.Address;
import org.dihedron.ogtl.test.Email;
import org.dihedron.ogtl.test.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Andrea Funto'
 */
public class GetPropertyTest {

	private Person person;
	
	private ExpressionCompiler compiler; 

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
		
		compiler = new ExpressionCompiler();
		
	}
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.dihedron.ogtl.operators.GetProperty#apply(java.lang.Object)}.
	 */
	@Test
	public void testGetField() {
		
		try {
			Expression expression = compiler.compile("name");
			String name = (String) expression.apply(person);
			assertTrue(name.equals("Name"));
			expression = compiler.compile("surname");
			String surname = (String) expression.apply(person);
			assertTrue(surname.equals("Surname"));
		} catch (LexerException e) {			
			e.printStackTrace();
			fail("test did not complete");
		} catch (Exception e) {
			e.printStackTrace();
			fail("test did not complete");
		}
	}
	
	/**
	 * Test method for {@link org.dihedron.ogtl.operators.GetProperty#apply(java.lang.Object)}.
	 */
	@Test
	public void testGetMethod() {
		
		try {
			Expression expression = compiler.compile("getSurname");
			Invocation invocation = (Invocation) expression.apply(person);
			Method method = invocation.getMethod();
			assertTrue(method.getName().equals("getSurname"));
		} catch (LexerException e) {			
			e.printStackTrace();
			fail("test did not complete");
		} catch (Exception e) {
			e.printStackTrace();
			fail("test did not complete");
		}
	}
	
	/**
	 * Test method for {@link org.dihedron.ogtl.operators.GetProperty#apply(java.lang.Object)}.
	 */
	@Test
	public void testGetArrayElement() {
		
		try {
			Expression expression = compiler.compile("addresses");
			Address [] addresses = (Address [])expression.apply(person);
			assertTrue(addresses.length == 2);
		} catch (LexerException e) {			
			e.printStackTrace();
			fail("test did not complete");
		} catch (Exception e) {
			e.printStackTrace();
			fail("test did not complete");
		}
	}
	
	/**
	 * Test method for {@link org.dihedron.ogtl.operators.GetProperty#apply(java.lang.Object)}.
	 */
	@Test
	public void testGetMap() {
		
		try {
			Expression expression = compiler.compile("emails");
			@SuppressWarnings("unchecked") Map<String, Email> emails = (Map<String, Email>)expression.apply(person);
			assertTrue(emails.size() == 4);
		} catch (LexerException e) {			
			e.printStackTrace();
			fail("test did not complete");
		} catch (Exception e) {
			e.printStackTrace();
			fail("test did not complete");
		}
	}	

	/**
	 * Test method for {@link org.dihedron.ogtl.operators.GetProperty#toString()}.
	 */
	@Test
	public void testToString() {
		Operator operator = new GetProperty("property");
		String string = operator.toString();
		assertTrue(string.equals(".property"));
	}

}
