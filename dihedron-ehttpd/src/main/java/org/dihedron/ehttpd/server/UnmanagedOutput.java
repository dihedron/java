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

import java.io.IOException;
import java.io.OutputStream;

import org.dihedron.ehttpd.exceptions.InvalidState;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andrea
 *
 */
public class UnmanagedOutput extends Output {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(UnmanagedOutput.class);
	
	/**
	 * An enumeration representing internal state.
	 * 
	 * @author Andrea Funto'
	 */
	private enum State {
		IN_HTTPSTATUS,
		IN_HEADERS,
		IN_CONTENT
	}
	
	private State state = State.IN_HTTPSTATUS; 
	
	public UnmanagedOutput(OutputStream stream) {
		super(stream);
	}
	
	@Override
	public void writeStatus(Status status) throws ServerException {
		if(state != State.IN_HTTPSTATUS) {
			throw new InvalidState();
		}
		try {
			byte [] data = formatHttpStatus(null, status);
			stream.write(data);
			state = State.IN_HEADERS;
		} catch (IOException e) {
			throw new ServerException("I/O error writing HTTP status to client", e);
		}
				
	}
	
	/**
	 * @see it.bankitalia.sisi.dsvaa.httpd.OutputPolicy#writeHeader(java.lang.String, java.lang.String[])
	 */
	@Override
	public void writeHeader(String key, String... values) throws ServerException {
			if(state == State.IN_HTTPSTATUS) {
				writeStatus(Status.STATUS_200);
			}		
			if(state == State.IN_HEADERS) {
				byte [] data = formatHttpHeader(null, key, values);
				try {
					stream.write(data);
				} catch (IOException e) {
					throw new ServerException("I/O error writing HTTP header to client", e);
				}
			} else {
				// invalid status!
				logger.error("trying to write header after content!");
				throw new InvalidState();
			}
	}
	

	/**
	 * @see it.bankitalia.sisi.dsvaa.httpd.OutputPolicy#writeContent(java.lang.String)
	 */
	@Override
	public void writeContent(String text) throws ServerException {
		if(text != null) {
			writeContent(text.getBytes());
		}
	}

	/**
	 * @see it.bankitalia.sisi.dsvaa.httpd.OutputPolicy#writeContent(byte[])
	 */
	@Override
	public void writeContent(byte[] data) throws ServerException {
		
		if(state == State.IN_HTTPSTATUS) {
			writeStatus(Status.STATUS_200);
		}		
		try {
			if(state == State.IN_HEADERS) {
				// append final 0x0D 0x0A
				stream.write(NEWLINE.getBytes());
				state = State.IN_CONTENT;
			}
			
			if(state == State.IN_CONTENT) {
				stream.write(data);
			}
		} catch (IOException e) {
			throw new ServerException("I/O error writing content to client", e);
		}
	}	
	
	/**
	 * @see org.dihedron.ehttpd.server.Output#flush()
	 */
	@Override
	public void flush() throws ServerException {
		try {
			stream.flush();
		} catch (IOException e) {
			throw new ServerException("I/O error flushing stream", e);
		}
	}
	
	/**
	 * Flushes the output data stream, and closes it.
	 */
	public void close() throws ServerException {
		try {
			stream.flush();
			stream.close();
		} catch (IOException e) {
			throw new ServerException("I/O error closing stream", e);
		}
	}	
}
