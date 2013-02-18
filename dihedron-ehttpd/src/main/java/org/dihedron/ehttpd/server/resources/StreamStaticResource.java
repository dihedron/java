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
 * Provides MIME-type information and raw data for a static resource represented 
 * as a stream.
 * 
 * @author Andrea Funto'
 */
public class StreamStaticResource implements StaticResource {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(StreamStaticResource.class);
	
	/**
	 * The content type.
	 */
	private String mimeType;
	
	/**
	 * The input stream from which the resource data will be read.
	 */
	private InputStream stream;
	
	/**
	 * The cached resource content.
	 */
	byte [] data;
	
	/**
	 * Constructor.
	 *
	 * @param mimeType
	 *   the data content type.
	 * @param stream
	 *   the input resource as a stream.
	 */
	public StreamStaticResource(String mimeType, InputStream stream) {
		this.mimeType = mimeType;
		this.stream = stream;
		logger.debug("creating resource of type \"" + mimeType + " on input stream");
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
	 * Reads the file from the file system and returns it.
	 * 
	 * @see org.dihedron.ehttpd.resources.StaticResource#getData()
	 */
	public byte[] getData() {
		if(data == null) {
			try {
				logger.debug("reading the resource from the stream");
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				Streams.copy(stream, output);
				data = output.toByteArray();
			} catch(IOException e) {
				logger.error("error reading from the input stream", e);
			}
		}
		return data;
	}
}
