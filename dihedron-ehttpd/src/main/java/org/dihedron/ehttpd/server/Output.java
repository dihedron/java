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

import java.io.OutputStream;

import org.dihedron.ehttpd.exceptions.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Output {
	
	private static Logger logger = LoggerFactory.getLogger(Output.class);
	
	public static final String NEWLINE = "\r\n";
	
	protected OutputStream stream = null;
		
	public Output(OutputStream stream) {
		this.stream = stream;
	}
	
	public abstract void writeStatus(Status status) throws ServerException;
	
	public abstract void writeHeader(String key, String... values) throws ServerException;
	
	public abstract void writeContent(String text) throws ServerException;
	
	public abstract void writeContent(byte[] data) throws ServerException;
	
	public abstract void flush() throws ServerException;
	
	public abstract void close() throws ServerException;
	
	protected byte[] formatHttpStatus(StringBuffer sb, Status status) {
		logger.trace("formatting HTTP status line: '{}'", status);		
		if(sb == null) {
			logger.trace("allocating new string buffer");
			sb = new StringBuffer();
		} else {
			logger.trace("reusing existing string buffer");
			sb.setLength(0);
		}
		// write out status first
		sb.append("HTTP/1.0").append(" ").append(status.toString()).append(NEWLINE);
		logger.debug("outputting:'{}' ", sb.toString().trim());
		return sb.toString().getBytes();		
	}
	
	protected byte[] formatHttpHeader(StringBuffer sb, String key, String [] values) {
		if(sb == null) {
			sb = new StringBuffer();
		} else {
			sb.setLength(0);
		}
		boolean first = true;
		sb.append(key);
		for(String value : values) {
			if(first) {
				sb.append(": ").append(value);
				first = false;
			} else { 
				sb.append(", ").append(value);
			}
		}			
		sb.append(NEWLINE);
		logger.debug("outputting: '{}'", sb.toString());
		return sb.toString().getBytes();		
	}
}
