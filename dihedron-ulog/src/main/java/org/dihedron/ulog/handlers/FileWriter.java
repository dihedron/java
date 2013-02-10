/**
 * Copyright (c) 2012, 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the uLog library ("uLog").
 *
 * "uLog" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "uLog" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "uLog". If not, see <http://www.gnu.org/licenses/>.
 */

package org.dihedron.ulog.handlers;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * This class implements the <code>MessageHandler</code>
 * resposible of writing the message to a file.
 * 
 * @author Andrea Funto'
 */
public class FileWriter extends StreamWriter {
	
	/**
	 * The default name of the output logfile.
	 */
	public final static String DEFAULT_LOGFILE = "log.txt";
	
	/**
	 * The default name of the file writer.
	 */
	public static final String ID = "file";

	/**
	 * Default contructor.
	 */
	public FileWriter() {		
	}
	
	/**
	 * Constructor.
	 * 
	 * @param filename
	 *   the name of the output file.
	 * @throws FileNotFoundException 
	 */
	public FileWriter(String filename){
		this(new File(filename));
	}
	
	/**
	 * Constructor.
	 * 
	 * @param file
	 *   the output file.
	 * @throws FileNotFoundException 
	 */
	public FileWriter(File file) {
		setFile(file);
	}
	
	/**
	 * Sets the name of the output file.
	 * 
	 * @param filename
	 *   the name of the output file.
	 */
	public void setFile(String filename) {
		this.setFile(new File(filename));
	}
	
	/**
	 * Sets the output file.
	 * 
	 * @param file
	 *   the output file.
	 */
	public void setFile(File file) {
		try {
			FileOutputStream stream = new FileOutputStream(file);
			super.setOutputStream(stream);
		} catch (FileNotFoundException e) {
			System.err.println("error opening file output stream");
			e.printStackTrace();
		}		
	}
	
	/**
	 * Returns the default name of the file writer.
	 * 
	 * @return
	 *   the default name of the file writer.
	 */
	public String getId() {
		return ID;
	}
	
	/**
	 * Performs a deep copy of the object.
	 * 
	 * @return
	 *   a deep copy of the object.
	 */
	@Override
	public FileWriter clone() throws CloneNotSupportedException {
		FileWriter clone = (FileWriter)super.clone();
		return clone;
	}
	
}
