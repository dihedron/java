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


import java.io.OutputStream;
import java.io.PrintWriter;

import org.dihedron.ulog.Message;

/**
 * Base class for for stream-based <code>MessageHandler</code>s.
 * 
 * @author Andrea Funto'
 */
public class StreamWriter implements MessageHandler {
	/**
	 * The default name of the stream writer.
	 */
	public static final String ID	= "stream";
		
	/**
	 * The output print writer.
	 */
	private PrintWriter writer = null;
	
	/**
	 * Default constructor; this method does not initialise the
	 * output stream and writer and assumes that they will be
	 * populated later via a call to <code>setOutputStream()</code>.
	 */
	public StreamWriter() {		
	}
	
	/**
	 * Constructor.
	 * 
	 * @param stream
	 *   the output stream.
	 */
	public StreamWriter(OutputStream stream) {
		setOutputStream(stream);
	}
	
	/**
	 * This method is reserved for sub-classing writers that
	 * want to delay the initialisation of the output stream.
	 * When constructed via the default constructor, the writer
	 * is unusable until the stream is properly set up. By using
	 * this method, sub-classing writers can avoid throwing
	 * exceptions when opening or initialising the output stream,
	 * since they can carefully craft the process in order to catch 
	 * and handle exceptions. Clients should not be exposed to
	 * exceptions since at this point (logging initialisation) 
	 * there might be no way to let the user know about or record in
	 * any usable way the error.
	 * 
	 * @param stream
	 *   the initialised output stream.
	 */
	protected void setOutputStream(OutputStream stream) {
		assert(stream != null);
		this.writer = new PrintWriter(stream);
	}

	/**
	 * Returns the stream writer's unique id.
	 * 
	 * @see 
	 *   org.dihedron.ulog.handlers.MessageHandler#getId()
	 */
	@Override
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
	public StreamWriter clone() throws CloneNotSupportedException  {
		StreamWriter clone = (StreamWriter)super.clone();
		// this is shared, no way to duplicate
		clone.writer = this.writer;
		return clone;
	}	

	/**
	 * Writes the message out to the stream.
	 * 
	 * @see 
	 *   org.dihedron.ulog.handlers.MessageHandler#onMessage(org.dihedron.ulog.Message)
	 */
	@Override
	public void onMessage(Message message) {
		writer.println(message.getText());
		writer.flush();
	}
}
