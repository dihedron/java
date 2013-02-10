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


import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.dihedron.ulog.Message;

/**
 * @author Andrea Funto'
 */
public class SimpleFormatter extends WrapperHandler {

	/**
	 * The <code>Writer</code>'s unique id.
	 */
	private final static String ID = "formatter";
	
	/** 
	 * The default date format for the traces. 
	 */
	private static final String DEFAULT_FORMAT = "yyyyMMdd@HH:mm:ss.SSS";
	
	/**
	 * The custom date format.
	 */
	private String dateFormat = DEFAULT_FORMAT;
	
	/**
	 * The date formatter.
	 */
	private DateFormat formatter;

	/**
	 * Whether the stack trace should be printed out.
	 */
	private boolean printStackTrace = true;
		
	/**
	 * Default constructor.
	 */
	public SimpleFormatter() {
		super();
		this.printStackTrace = true;
		this.formatter = new SimpleDateFormat(dateFormat);
	}


	/**
	 * Constructor.
	 * 
	 * @param handler
	 *   the wrapped <code>MessageHandler</code>.
	 */
	public SimpleFormatter(MessageHandler handler) {
		super(handler);
		this.printStackTrace = true;
		this.formatter = new SimpleDateFormat(dateFormat);
	}

	/**
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
	public SimpleFormatter clone() throws CloneNotSupportedException  {
		SimpleFormatter clone = (SimpleFormatter)super.clone();
		//clone.setDateFormat(this.dateFormat);
		clone.formatter = (SimpleDateFormat)this.formatter.clone();
		return clone;
	}	
	
	/**
	 * Sets the date format, ad updates the formatter.
	 * @param value
	 */
	public void setDateFormat(String value) {
		if(value != null && value.trim().length() > 0) {
			this.dateFormat = value.trim();
			this.formatter = new SimpleDateFormat(this.dateFormat);
		}
	}
	
	/**
	 * Returns the current date format.
	 * 
	 * @return
	 *   the current date format.
	 */
	public String getDateFormat() {
		return this.dateFormat;
	}
	
	/**
	 * Enables the stack trace printout. <b>NOTE</b>: the output may easily 
	 * become very verbose and difficult to read: use sparingly.
	 */
	public void setPrintStackTrace(String value) {
		if(value!= null && value.trim().equalsIgnoreCase("false")) {
			this.printStackTrace = false;
		} else {
			this.printStackTrace = true;
		}
	}
		
	/**
	 * Checks whether the logging will include the stack trace
	 * in the event of an exception.
	 * 
	 * @return
	 *   <code>true</code> if the stack trace will be print, 
	 *   <code>false</code> otherwise.
	 */
	public boolean isPrintStackTrace() {
		return printStackTrace;
	}
	
	/**
	 * Formats the message, including all information regarding the class 
	 * and the source (filename and line number) where the logging was
	 * requested; if the stack trace printout is enabled it also formats
	 * the exception's stack trace, otherwise it simply writes the exception's
	 * message. When it's done, the message is modified in place and is ready to
	 * be passed on to the next <code>handler</code> in the chain.
	 * 
	 * @param message
	 *   the message being formatted.
	 */
	@Override
	public void onMessage(Message message) {
		StringBuilder sb = new StringBuilder();
		switch(message.getLevel()) {
		case TRACE: sb.append("[A] "); break;
		case DEBUG: sb.append("[D] "); break;
		case INFO:  sb.append("[I] "); break;
		case WARN:  sb.append("[W] "); break;
		case ERROR: sb.append("[E] "); break;
		case FATAL: sb.append("[F] "); break;
		}				
		sb.append(formatter.format(message.getTimestamp()));
		sb.append(" [").append(Thread.currentThread().getId()).append("] ");
		sb.append(message.getClassName()).append(".").append(message.getMethod()).append("() - ").append(message.getText());
		sb.append(" (").append(message.getFileName()).append(":").append(message.getLineNumber()).append(")");
		if(message.getException() != null && isPrintStackTrace()) {
			StringWriter sw = new StringWriter();
			PrintWriter writer = new PrintWriter(sw); 
			message.getException().printStackTrace(writer);
			writer.flush();
			sb.append("\nStack Trace:\n").append(sw.toString());
		}
		message.setText(sb.toString());
		forward(message);
	}
}
