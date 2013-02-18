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
package org.dihedron.ehttpd.server.resources;


/**
 * Provides MIME-type information and raw data for an
 * in-memory static resource.
 * 
 * @author Andrea Funto'
 */
public class InMemoryStaticResource implements StaticResource {
	
	/**
	 * The content type.
	 */
	private String mimeType;
	
	/**
	 * The resource raw data.
	 */
	private byte [] data;
	
	/**
	 * Constructor.
	 *
	 * @param mimeType
	 *   the data content type.
	 * @param data
	 *   the raw data.
	 */
	public InMemoryStaticResource(String mimeType, byte [] data) {
		this.mimeType = mimeType;
		this.data = data;
	}

	/**
	 * Retrieves the data MIME type.
	 * 
	 * @see org.dihedron.ehttpd.resources.StaticResource#getContentType()
	 */
	public String getContentType() {
		return mimeType;
	}

	/**
	 * Retrieves the raw data.
	 * 
	 * @see org.dihedron.ehttpd.resources.StaticResource#getData()
	 */
	public byte[] getData() {		
		return data;
	}
}
