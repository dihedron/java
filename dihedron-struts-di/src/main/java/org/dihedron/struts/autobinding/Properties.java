/**
 * Copyright (c) 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron Struts2 Dependency Injection Plugin ("Struts-DI").
 *
 * "Struts-DI" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "Struts-DI" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "Struts-DI". If not, see <http://www.gnu.org/licenses/>.
 */
package org.dihedron.struts.autobinding;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An alternative implementation of the classical <code>Properties</code>
 * class, providing the capability of loading from several sources, of
 * merging with another instance and of switching among different separator
 * character sequences.
 * 
 * @author Andrea Funto'
 */
public class Properties extends HashMap<String, String> {	
	/**
	 * Serial version ID. 
	 */
	private static final long serialVersionUID = 4356540255433454048L;
	
	/**
	 * The SL4J logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(Properties.class);
	
	/**
	 * The default character using for telling key and value apart.
	 */	
	public final static String DEFAULT_SEPARATOR = "=";
	
	/**
	 * The default comment start character sequence.
	 */
	public final static String LINE_COMMENT_START = "#";
	
	/**
	 * The character(s) indicating that a property is stored on 
	 * multiple lines, and that it continues on the following line.
	 */
	public final static String CONTINUE_ON_NEW_LINE = "\\"; 
	
	/**
	 * Constructor.
	 */
	public Properties() {
		super();
	}
	
	/**
	 * Constructor.
	 *
	 * @param values
	 *   a map of initial values.
	 */
	public Properties(Map<String, String> values) {
		super(values);
	}
	
	/**
	 * Loads the properties from an input file.
	 * 
	 * @param resourcename
	 *   the name of the file.
	 * @throws IOException
	 */
	public void load(String resourcename) throws IOException {
		load(resourcename, DEFAULT_SEPARATOR);
	}

	/**
	 * Loads the properties from an input file.
	 * 
	 * @param resourcename
	 *   the name of the resource, whether a file on the filesystem or
	 *   a resource in the JAR.
	 * @param separator
	 *   the separator character.
	 * @throws IOException
	 */
	public void load(String resourcename, String separator) throws IOException {
		File file = new File(resourcename);
		if(file.exists() && file.isFile()) {
			logger.trace("reading properties from file: {}", file.getCanonicalPath());
			load(file, separator);
		} else {
			logger.trace("reading properties from resource: {}", resourcename);
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourcename);
			try {
				load(stream, separator);
			} finally {
				if(stream != null) {
					stream.close();
				}
			}
		}
	}
	
	/**
	 * Loads the properties from an input file.
	 * 
	 * @param file
	 *   the input file.
	 * @throws IOException
	 */
	public void load(File file) throws IOException {
		load(file, DEFAULT_SEPARATOR);
	}

	/**
	 * Loads the properties from an input file.
	 * 
	 * @param file
	 *   the input file.
	 * @param separator
	 *   the separator character.
	 * @throws IOException
	 */
	public void load(File file, String separator) throws IOException {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			load(stream, separator);
		} finally {
			if(stream != null) {
				stream.close();
			}
		}
	}

	/**
	 * Loads the properties from an input stream.
	 * 
	 * @param stream
	 *   an input stream.
	 * @throws IOException
	 */
	public void load(InputStream stream) throws IOException {
		load(stream, DEFAULT_SEPARATOR);
	}
	
	/**
	 * Loads the properties from an input stream.
	 * 
	 * @param stream
	 *   an input stream.
	 * @param separator
	 *   the separator character.
	 * @throws IOException
	 */
	public void load(InputStream stream, String separator) throws IOException {
		DataInputStream in = null;
		try {
			boolean inMultiLine = false;
			in = new DataInputStream(stream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			StringBuilder line = new StringBuilder();
			String chunk;
			while ((chunk = br.readLine()) != null)   {
				chunk = chunk.trim();
				logger.trace("read chunk: '{}'", chunk);
				if(inMultiLine) {
					logger.trace("processing in multiline mode");
					if(chunk.startsWith(LINE_COMMENT_START)) {
						logger.trace("comment ending multiline mode");
						add(line.toString(), separator);
						line.setLength(0);
						inMultiLine = false;						
					} else {
						line.append(chunk);
						if(chunk.endsWith(CONTINUE_ON_NEW_LINE)) {							
							line.setLength(line.length() - CONTINUE_ON_NEW_LINE.length()); // remove trailing '\'
							logger.trace("adding chunk, line is now '{}'", line);
						} else {					
							logger.trace("exiting multiline mode");
							add(line.toString(), separator);
							line.setLength(0);
							inMultiLine = false;
						}
					}
				} else {
					if(chunk.startsWith(LINE_COMMENT_START)) {
						logger.trace("comment line, skipping");
					} else {
						if(chunk.endsWith(CONTINUE_ON_NEW_LINE)) {
							logger.trace("entering multiline mode");
							inMultiLine = true;
							line.append(chunk);
							line.setLength(line.length() - CONTINUE_ON_NEW_LINE.length());
						} else {
							logger.trace("adding single line property");
							add(chunk, separator);
						}
					}
				}
			}
		} finally {
			in.close();
 		}
	}
	
	/**
	 * Merges with another property set.
	 *  
	 * @param properties
	 *   the other property set.
	 */
	public void merge(Properties properties) {
		assert(properties != null);
		for(Entry<String, String> entry : properties.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Parses a line containing a key/value pair and adds it to the
	 * map.
	 * 
	 * @param property
	 *   a line containing a key/value pair separated by <code>separator</code>.
	 * @param separator
	 *   the character sequence dividing key from value.
	 * @return
	 *   <code>true</code> in case of success, <code>false</code> otherwise.
	 */
	public boolean add(String property, String separator) {
		logger.trace("parsing property '{}' using separator '{}'", property, separator);
		if(property.trim().length() > 0) {
			int index = property.indexOf(separator);
			if(index != -1) {
				String key = property.substring(0, index);
				String value = property.substring(index + separator.length());
				logger.trace("parsed as '{}' => '{}'", key, value);
				this.put(key, value);
				return true;
			}
		} else {
			logger.warn("empty property");
			return false;
		}
		logger.error("invalid property");
		return false;
	}
}
