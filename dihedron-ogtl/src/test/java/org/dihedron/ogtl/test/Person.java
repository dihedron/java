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
package org.dihedron.ogtl.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Funto'
 */
public class Person {
	private String name;
	private String surname;
	private Address addresses[] = new Address[]{};
	private Map<String, Email> emails = new HashMap<String, Email>();
	
	public Person(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}
	
	public Person addAddress(Address address) {			
		if(address != null) {
			List<Address> list = new ArrayList<Address>();
			if(addresses != null) {
				for(int i = 0; i < addresses.length; ++i) {
					if(addresses[i] != null) list.add(addresses[i]);
				}
			}
			list.add(address);
			addresses = (Address[])list.toArray(addresses);				
		}
		return this;
	}
	
	public Person addEmail(String label, Email email) {
		if(label != null && email != null) {
			emails.put(label,  email);
		}
		return this;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the addresses
	 */
	public Address[] getAddresses() {
		return addresses;
	}

	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(Address[] addresses) {
		this.addresses = addresses;
	}

	/**
	 * @return the emails
	 */
	public Map<String, Email> getEmails() {
		return emails;
	}

	/**
	 * @param emails the emails to set
	 */
	public void setEmails(Map<String, Email> emails) {
		this.emails = emails;
	}
	
	
}