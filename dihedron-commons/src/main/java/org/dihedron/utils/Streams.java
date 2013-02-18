/**
 * Copyright (c) 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron Common Utilities library ("Commons").
 *
 * "Commons" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "Commons" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "Commons". If not, see <http://www.gnu.org/licenses/>.
 */
package org.dihedron.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Funto'
 */
public class Streams {
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Streams.class);
	
	/**
	 * Default size of the internal memory buffer.
	 */
	public final static int DEFAULT_BUFFER_SIZE = 4096;
	
	/**
	 * Copies all the bytes it can read from the input stream into the output
	 * stream; input and output streams management (opening, flushing, closing
	 * are all up to the caller.
	 * 
	 * @param input
	 *   an open and ready-to-be-read input stream.
	 * @param output
	 *   an open output stream.
	 * @return
	 *   the total number of bytes copied.
	 * @throws IOException
	 */
	public static long copy(InputStream input, OutputStream output) throws IOException {
		long total = 0;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		
		assert(input != null && output != null);
		
		int read = 0;
		while((read = input.read(buffer)) > 0) {
			logger.trace("read {} bytes from input stream", read);
			total += read;
			output.write(buffer, 0, read);
		}
		logger.trace("total of {} bytes copied", total);
		return total;
	}
	
	/**
	 * Reads all data from the given input file.
	 * 
	 * @param file
	 *   the file to read from.
	 * @return
	 *   an array of bytes read from the input file.
	 * @throws IOException
	 */
	public static byte[] readFromFile(File file) throws IOException {
		byte [] data = null;
		assert(file != null);
		FileInputStream input = null;
		ByteArrayOutputStream output = null;

		try {
			input = new FileInputStream(file);
			output = new ByteArrayOutputStream();
			copy(input, output);
			data = output.toByteArray();					
		} finally {
			IOException ex = null;
			try {
				input.close();
			} catch(IOException e) {
				ex = e;
			}
			try {
				output.close();
			} catch(IOException e) {
				ex = e;
			}
			if(ex != null) {
				throw ex;
			}
		}		
		return data;
	}

	/**
	 * Reads all data from the given input file.
	 * 
	 * @param file
	 *   the path of the file to read from.
	 * @return
	 *   an array of bytes read from the input file.
	 * @throws IOException
	 */
	public static byte[] readFromFile(String filepath) throws IOException {
		return readFromFile(new File(filepath));
	}
	
	public static int readFromFile(File file, byte[] data) throws IOException {
		return readFromFile(file, data, 0, data.length);
	}

	public static int readFromFile(File file, byte[] data, int offset,	int length) throws IOException {
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
			return readFromStream(stream, data, offset, length);
		} catch (FileNotFoundException e) {
			logger.error("file '" + file.getCanonicalPath() + "' not found", e);
		} finally {
			if(stream != null) {
				stream.close();
			}
		}
		return -1;
	}
	
	
	public static void writeToFile(File file, byte[] data) throws IOException {
		writeToFile(file, data, 0, data.length);
	}

	public static void writeToFile(File file, byte[] data, int offset,	int length) throws IOException {
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
			writeToStream(stream, data, offset, length);
		} finally {
			if(stream != null) {
				stream.close();
			}
		}
	}

	public static void writeToStream(OutputStream stream, byte[] data) throws IOException {
		stream.write(data, 0, data.length);
	}

	public static void writeToStream(OutputStream stream, byte[] data, int offset, int length) throws IOException {
		stream.write(data, offset, length);
	}


	public static int readFromStream(InputStream stream, byte[] data)
			throws IOException {
		return readFromStream(stream, data, 0, data.length);
	}

	public static int readFromStream(InputStream stream, byte[] data, int offset, int length) throws IOException {
		int total = 0;
		int size = 0;
		while ((length - total) > 0) {
			size = stream.read(data, offset + total, length - total);
			total += size;
		}
		return total;
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is));
				int n;
				while ((n = reader.read()) != -1) {
					writer.write(n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}	
}
