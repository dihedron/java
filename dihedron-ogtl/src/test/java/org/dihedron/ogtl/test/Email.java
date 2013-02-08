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

/**
 * @author Andrea Funto'
 */
public class Email {
	
	private String email;
	
	public Email(String email) {
		this.email = email;
	}
	
	/**
	 * 
	 * @return 
	 *   the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email 
	 *   the email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}



	public String toString() {
		return "mailto:" + email;
	}
}