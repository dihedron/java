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
package org.dihedron.ulog;

import java.util.concurrent.atomic.AtomicInteger;

import org.dihedron.ulog.strings.StringTokeniser;

/**
 * Creates new {@link Message} objects, by formatting input
 * text and substituting arguments.
 * 
 * @author Andrea Funto'
 */
public class MessageBuilder {
	
	/**
	 * The default depth in the call stack of the method that called
	 * the logging services; by default it is 3, but it may change when 
	 * ULOG is wrapped by the SLF4J API bridge.
	 */
	public final static int DEFAULT_STACK_DEPTH = 3;
		
	/**
	 * The depth of the calling method in the call stack; this is used to retrieve such
	 * information as method name, source file name, line number and class name where 
	 * the logging event occurred. 
	 */
	private final static AtomicInteger stackDepth = new AtomicInteger(DEFAULT_STACK_DEPTH);
	
	/**
	 * Returns the current call stack depth of the calling method, without
	 * blocking and without any need for synchronisation.
	 * 
	 * @return
	 *   the current call stack depth of the calling method.
	 */
	public final static int getStackDepth() {
		return stackDepth.get();
	}
	
	/**
	 * Sets a new value for the stack depth of the calling method.
	 *  
	 * @param newValue
	 *   the new value for the call stack depth of the calling method.
	 */
	public final static void setStackDepth(int newValue) {
		stackDepth.set(newValue);
	}
	
	/**
	 * The string tokeniser used to substitute '{}' place-holders
	 * with arguments. 
	 */
	private StringTokeniser tokeniser;
	
	/** 
	 * The buffer into which the new message will be stored.
	 */
	private StringBuilder builder;
	
	/**
	 * Constructor.
	 * 
	 * @param placeholder
	 *   the place-holder to text arguments; by default it is '{}'.
	 */
	public MessageBuilder(String placeholder) {
		tokeniser = new StringTokeniser(placeholder).setTrimSpaces(false);
		builder = new StringBuilder();
	}
	
	/**
	 * Factory method: this method creates a new <code>Message</code>, 
	 * given the level and the message text. In order to fill all
	 * relevant data, it walks the stack and retrieves run-time information.
	 * 
	 * @param level
	 *   the message level.
	 * @param exception
	 *   the optional nested exception.
	 * @param text
	 *   the message text.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 * @return
	 *   a new <code>Message</code> instance.
	 */
	public Message createMessage(Level level, Throwable exception, String text, Object... arguments) {
		StackTraceElement frame = new Throwable("").getStackTrace()[getStackDepth()];
		String classname = frame.getClassName().substring(frame.getClassName().lastIndexOf('.') + 1);	
		if(arguments != null && arguments.length > 0 && text.contains("{}")) {
			tokeniser.reset();
			builder.setLength(0);
			String [] tokens = tokeniser.tokenise(text);
			int i = 0;
			for(String token : tokens) {
				builder.append(token);
				if(arguments.length > i) {
					builder.append(arguments[i] != null ? arguments[i].toString() : "<null>");
					++i;
				}
			}
			text = builder.toString();
		}		
		return new Message(level, text, classname, frame.getMethodName(), frame.getFileName(), frame.getLineNumber(), exception);
	}


}
