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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.dihedron.utils.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class retrieves a resource from the JAR, using the
 * class loader methods, and returns it as a stream.
 * 
 * @author Andrea Funto'
 */
public class JarStaticResource implements StaticResource {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(JarStaticResource.class);
	
	/** A class in the same package as the resource. */
	private Class<?> clazz = null;
	
	/** The name of the resource. */
	private String resource = null;
	
	/** The MIME type of the resource. */
	private String contentType = null;
	
	/**
	 * Constructor.
	 * 
	 * @param resource
	 *   the name of the resource.
	 * @param contentType
	 *   the MIME type of the resource (e.g. "text/html").  
	 */
	public JarStaticResource(String resource, String contentType) {
		this.resource = resource;
		this.contentType = contentType;
		logger.debug("retrieving resource \"" + resource + "\" with fully qualified name");
	}

	/**
	 * Constructor.
	 * 
	 * @param clazz
	 *   a class in the same package as the resource to be retrieved.
	 * @param resource
	 *   the name of the resource.
	 * @param contentType
	 *   the MIME type of the resource (e.g. "text/html").  
	 */
	public JarStaticResource(Class<?> clazz, String resource, String contentType) {
		this.clazz = clazz;
		this.resource = resource;
		this.contentType = contentType;
		logger.debug("retrieving resource \"" + resource + "\" from path of class \"" + clazz.getName() + "\"");
	}

	//@Override
	public byte[] getData() {
		try {
			InputStream input = null;
			if(clazz != null) {
				input = clazz.getResourceAsStream(resource);
			} else {
				input = getClass().getClassLoader().getResourceAsStream(resource);
			}
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			Streams.copy(input, output);
			return output.toByteArray();
		} catch(IOException e) {
			logger.error("error reading resource \"" + resource + "\" data");
		}
		return null;
	}

	//@Override
	public String getContentType() {
		return contentType;
	}
}
