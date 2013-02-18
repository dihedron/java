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
package org.dihedron.ehttpd.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.utils.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a managed way of reordering output data
 * in order to comply with the HTTP standard: it sends status 
 * information first, headers next, and data last.
 * 
 * @author Andrea Funto'
 */
public class ManagedOutput extends Output {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(ManagedOutput.class);
	
	protected ByteArrayOutputStream buffer = null;
	
	protected Status status = Status.STATUS_200;
	
	protected Map<String, String[]> headers = null;
	
	/**
	 * Indicates whether the output has already been flushed.
	 */
	protected boolean flushed = false;
	
	/**
	 * Constructor.
	 * 
	 * @param stream
	 *   the response stream (built on the
	 *   client socket).
	 */
	public ManagedOutput(OutputStream stream) {
		super(stream);
		this.buffer = new ByteArrayOutputStream();
		this.headers = new HashMap<String, String[]>();
		logger.debug("request output will be automatically managed");
	}

	public void writeStatus(Status status) {
		this.status = status;
	}

	/**
	 * @see it.bankitalia.sisi.dsvaa.httpd.OutputStrategy#writeHeader(java.lang.String, java.lang.String[])
	 */
	@Override
	public void writeHeader(String key, String... values) {
		if(key != null && values != null) {
			headers.put(key, values);
		}
	}

	/**
	 * @see it.bankitalia.sisi.dsvaa.httpd.OutputStrategy#writeContent(java.lang.String)
	 */
	@Override
	public void writeContent(String text) throws ServerException {
		if(text != null) {
			logger.debug("appending '{}' to content", text);
			try {
				buffer.write(text.getBytes());
			} catch(IOException e) {
				throw new ServerException("I/O error writing text to client");
			}
		}
	}

	/* (non-Javadoc)
	 * @see it.bankitalia.sisi.dsvaa.httpd.OutputStrategy#writeContent(byte[])
	 */
	@Override
	public void writeContent(byte[] data) throws ServerException {
		if(data != null) {
			try {
				buffer.write(data);
			} catch(IOException e) {
				throw new ServerException("I/O error writing data to client");
			}
		}
	}
	
	/**
	 * Flushes data to the client stream.
	 * 
	 * @see org.dihedron.ehttpd.server.Output#flush()
	 */
	@Override
	public void flush() throws ServerException {
		
		if(flushed) {
			logger.debug("output already flushed");
		} else {
			logger.debug("flushing managed output");		
		
			try {
			
				StringBuffer sb = new StringBuffer();
		
				// write out status first
				stream.write(formatHttpStatus(sb, status));
		
				// write headers next
				for(Entry<String, String[]> header : headers.entrySet()) {
					byte [] data = null;
					// content length is automatically calculated on the buffered data
					if(header.getKey().equals("Content-Length")) {
						sb.setLength(0);
						sb.append("Content-Length: ").append(buffer.size()).append(NEWLINE);
						data = sb.toString().getBytes();			
					} else {
						data = formatHttpHeader(sb, header.getKey(), header.getValue());
					}
					stream.write(data);
				}
				// append header terminator (NEWLINE on its own)
				stream.write(NEWLINE.getBytes());
				
				// write content
				logger.debug("outputting {} bytes of content", buffer.size());
				Streams.copy(new ByteArrayInputStream(buffer.toByteArray()), stream);
				logger.debug("content written to stream");
				
				stream.flush();
				
				// this response should not be resent, should a 
				// second call to this method erroneously occur
				flushed = true;
				
			} catch(IOException e) {
				throw new ServerException("I/O error flushing stream", e);
			}
		}
	}
	
	/**
	 * Flushes the output data stream, and closes it.
	 */
	public void close() throws ServerException {
		try {
			stream.flush();
			stream.close();
		} catch(IOException e) {
			throw new ServerException("I/O error closing stream");
		}
	}
}

	