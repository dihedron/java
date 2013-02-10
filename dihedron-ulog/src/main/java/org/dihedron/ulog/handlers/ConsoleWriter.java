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


import java.io.PrintStream;

import org.dihedron.ulog.Message;

/**
 * This class implements the handler taht writes message to
 * the system console.
 * 
 * @author Andrea Funto'
 */
public class ConsoleWriter implements MessageHandler {
	
	private enum NewLine {
		WINDOWS("\r\n"),
		UNIX("\n"),
		MAC("\r"),
		NONE("");
		
		private NewLine(String chars) {
			this.chars = chars;
		}
		
		@Override
		public String toString() {
			return chars;
		}
		
		private String chars;
	}
	
	/**
	 * The unique identifier of the console writer.
	 */
	public static final String ID = "console";
	
	/**
	 * The ordinary output stream.
	 */
	private PrintStream output = System.out;
	
	/**
	 * The error stream.
	 */
	private PrintStream error = System.out;
	
	/**
	 * The new line character sequence.
	 */
	private NewLine newLine = NewLine.WINDOWS;
	
	/**
	 * Constructor.
	 */
	public ConsoleWriter() {
		super();
	}

	/**
	 * Overrides the output <code>PrintStream</code>: by default 
	 * this is set to <code>System.out</code>.
	 * 
	 * @param streamName
	 *   the overriding output <code>PrintStream</code>.
	 */
	public void setOutputStream(String streamName) {
		if(streamName != null){
			if(streamName.trim().equalsIgnoreCase("stdout")) {
				setOutputStream(System.out);
			} else if(streamName.trim().equalsIgnoreCase("stderr")) {
				setOutputStream(System.err);
			} 
		}
	}
	
	/**
	 * Overrides the error <code>PrintStream</code>; by default 
	 * this is set to <code>System.err</code>, use this method to 
	 * set it to <code>System.err</code> if necessary.
	 * 
	 * @param streamName
	 *   the overriding error stream name.
	 */
	public void setErrorStream(String streamName) {
		if(streamName != null){
			if(streamName.trim().equalsIgnoreCase("stdout")) {
				setOutputStream(System.out);
			} else if(streamName.trim().equalsIgnoreCase("stderr")) {
				setOutputStream(System.err);
			} 
		}
	}
	
	/**
	 * Sets whether the writer should append a newline to the end of 
	 * the string; this is necessary to provide proper formatting
	 * depending on the platform. 
	 * 
	 * @param value
	 *   there are 4 supported values: <code>WINDOWS</code>, 
	 *   <code>UNIX</code>, <code>MAC</code> and <code>NONE</code>. 
	 */
	public void setNewLine(String value) {
		if(value != null) {
			if(value.equalsIgnoreCase("WINDOWS")) {
				newLine = NewLine.WINDOWS;
			} else if(value.equalsIgnoreCase("UNIX")) {
				newLine = NewLine.UNIX;
			} else if(value.equalsIgnoreCase("MAC")) {
				newLine = NewLine.MAC;
			} else {
				newLine = NewLine.NONE;
			}
		}
	}
	
	/**
	 * Overrides the output <code>PrintStream</code>: by default 
	 * this is set to <code>System.out</code>.
	 * 
	 * @param output
	 *   the overriding output <code>PrintStream</code>.
	 */
	public void setOutputStream(PrintStream output) {
		this.output = output;
	}
	
	/**
	 * Overrides the error <code>PrintStream</code>; by default 
	 * this is set to <code>System.err</code>, use this method to 
	 * set it to <code>System.err</code> if necessary.
	 * 
	 * @param error
	 *   the overriding error <code>PrintStream</code>.
	 */
	public void setErrorStream(PrintStream error) {
		this.error = error;
	}
	
	/**
	 * Returns the writer's default name.
	 * 
	 * @return
	 *   the writer's default name.
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
	public ConsoleWriter clone() throws CloneNotSupportedException {
		ConsoleWriter clone = (ConsoleWriter)super.clone();
		clone.setErrorStream(this.error);
		clone.setOutputStream(this.output);
		clone.newLine = this.newLine;
		return clone;
	}
	
	/**
	 * Writes the message to console, to one of the registered output 
	 * streams. By default messages below WARN (included) are written to 
	 * standard output, whereas more severe messages are output to
	 * standard error. These settings can be overridden by specifying
	 * the output and error streams explicitly; nothing prevents 
	 * both channels to be pointed to the same stream.
	 * Note: different streams can often lead to interlaced messages 
	 * on the console, where lower priority messages get intermixed with 
	 * higher severity ones: specifying the same stream for both can make
	 * reading easier as messages will be output according to their
	 * natural time-based flow.
	 * 
	 * @param message
	 *   the message.
	 */
	@Override
	public void onMessage(Message message) {
		switch(message.getLevel()){
		case TRACE: 
		case DEBUG: 
		case INFO: 
		case WARN:
			output.print(message.getText().replaceAll("\\r\\n", newLine.toString()) + newLine);
			/*
			if(printNewLine) {
				output.println(message.getText());
			} else {
				output.print(message.getText());
			}
			*/
			break;
		case ERROR: 
		case FATAL:
			error.print(message.getText().replaceAll("\\r\\n", newLine.toString()) + newLine);
			/*
			if(printNewLine) {				
				error.println(message.getText());
			} else {
				error.print(message.getText());
			}
			*/
			break;
		}
	}
}
