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
import java.util.List;


/**
 * @author Andrea Funto'
 *
 */
public class Address {
	private String street;
	private String number;
	private String city;
	private String country;
	private List<String> phones;
	
	public Address(String street, String number, String city, String country) {
		this.street = street;
		this.number = number;
		this.city = city;
		this.country = country;
		phones = new ArrayList<String>();
	}
	
	public Address addPhone(String phone) {
		if(phone != null) {
			phones.add(phone);
		}
		return this;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the phones
	 */
	public List<String> getPhones() {
		return phones;
	}

	/**
	 * @param phones the phones to set
	 */
	public void setPhones(List<String> phones) {
		this.phones = phones;
	}
	
}

