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
package org.dihedron.ulog.factories;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dihedron.ulog.Log;
import org.dihedron.ulog.extensions.Extension;
import org.dihedron.ulog.extensions.ExtensionPoint;
import org.dihedron.ulog.handlers.MessageHandler;
import org.dihedron.ulog.handlers.WrapperHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Andrea Funto'
 */
public class XmlLogFactory implements LogFactory {

	/**
	 * The name of the user-provided XML configuration file.
	 */
	private static final String USER_ULOG_CONFIG_XML = "ulog-config.xml";
	
	/**
	 * The name of the built-in default XML configuration file.
	 */
	private static final String DEFAULT_ULOG_CONFIG_XML = "org/dihedron/ulog/default-ulog-config.xml";

	/**
	 * The input XML file.
	 */
	private File file;
	
	/**
	 * The <code>Log</code> archetype.
	 */
	private Log archetype;
	
	/**
	 * Default constructor.
	 */
	public XmlLogFactory() {
	}
	
	/**
	 * Constructor.
	 * 
	 * @param filename
	 *   the name of the XML file to parse.
	 */
	public XmlLogFactory(String filename) {
		this(new File(filename));
	}
	
	/**
	 * Constructor.
	 * 
	 * @param file
	 *   the XML file to parse.
	 */
	public XmlLogFactory(File file) {
		this.file = file;
	}
	
	/**
	 * Parses the input configuration and returns a DOM document.
	 * 
	 * @return
	 *   a DOM document.
	 * @throws FileNotFoundException 
	 */
	private InputStream getInputStream() throws FileNotFoundException {
		
		InputStream stream = null;
		
		do {
			// input file (to the object) wins over all
			if(file != null && file.exists() && file.isFile()) {
				stream = new FileInputStream(file);
			}
			
			if(stream != null) break;

			// user configuration as a file comes next
			File f = new File(USER_ULOG_CONFIG_XML);
			if(f.exists() && f.isFile()) {
				stream = new FileInputStream(f);
			}
			
			if(stream != null) break;
			
			// user configuration on the classpath
			stream = this.getClass().getClassLoader().getResourceAsStream(USER_ULOG_CONFIG_XML);
			
			if(stream != null) break;
			
			// resort to the built-in configuration file
			stream = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_ULOG_CONFIG_XML);
			
		} while(false);
		return stream;
	}
	
	@Override
	public void initialise() {	
		try {
			// acquire the input stream
			InputStream input = getInputStream();				
			
			// parses the input stream
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			SAXHandler handler = new SAXHandler();
			parser.parse(input, handler);
			// retrieve the Loge archetype
			archetype = handler.getLogger();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new <code>Log</code> after the settings in the user-provided 
	 * input file, in a user-provided <code>ulog-config.xml</code> under the 
	 * class path, or in the built-in <code>default-ulog-config.xml</code>.
	 * 
	 * @see 
	 *   org.dihedron.ulog.factories.LogFactory#makeLog()
	 */
	@Override
	public Log makeLog() {
		try {
			return archetype.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author Andrea Funto'
	 */
	public static class SAXHandler extends DefaultHandler {
		
		private enum Mode {
			IN_HANDLER,
			IN_EXTENSION,
			AT_REST
		};
			
		/**
		 * The name of the parameter.
		 */
		private String parameterName = null;
		
		/**
		 * A buffer to hold the parameter value.
		 */
		private StringBuilder parameterValue = new StringBuilder();
		
		/**
		 * The logger being configured.
		 */
		private Log log;
		
		/**
		 * The stack of message handlers encountered so far.
		 */
		private Stack<MessageHandler> handlersStack = new Stack<MessageHandler>();
		
		/**
		 * The stack of extensions encountered so far.
		 */
		private Stack<Extension> extensionsStack = new Stack<Extension>();

		/**
		 * The current processing mode.
		 */
		private Mode mode = Mode.AT_REST;

		/**
		 * Returns the <code>Log</code>; this method should be invoked only
		 * once the parsing has been accomplished, otherwise it may return 
		 * <code>null</code> or a partially configured <code>Log</code> object.
		 * 
		 * @return
		 *   the <code>Log</code> object.
		 */
		public Log getLogger() {
			return log;
		}
		
		/**
		 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
		 */
		@Override
		public void startDocument() throws SAXException {
//			System.out.println("document starting");
			
			super.startDocument();
			mode = Mode.AT_REST;
			parameterName = null;
			parameterValue.setLength(0);
			handlersStack.clear();
			extensionsStack.clear();			
			log = new Log();		
		}
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {	
//			System.out.println("----------------------------------------------------------------");
//			System.out.println("element starting: " + qName);
//			printStack("HANDLERS (before)", handlersStack);
//			printStack("EXTENSIONS (before)", extensionsStack);			
			if (qName.equalsIgnoreCase("handler")) {
//				System.out.println(" - handler: " + attributes.getValue("class"));
				mode = Mode.IN_HANDLER;
				try {
					String classname = attributes.getValue("class");			
					MessageHandler handler = (MessageHandler)Class.forName(classname).newInstance();
					if(!handlersStack.isEmpty()) {
//						System.out.println("   - pushing new handler " + attributes.getValue("class") + " onto stack");
						if(handlersStack.peek() instanceof WrapperHandler) {
							((WrapperHandler)handlersStack.peek()).wrapAround(handler);
						} else {
							// NOTE: we have to resort to reflection here because both WrapperHandler 
							// and TeeHandler have the wrapAround() method; TeeHandler could 
							// extend WrapperHandler, but then when we cast the stack emerging
							// element to MessageHandler and invoke wrapAround(), it is
							// MessageHandler's implementation that is picked up. Thus we
							// prefer to let TeeHandler implement the wrapAround() method without
							// extending WrapperHandler (which would be nonsense), and we implement
							// duck-typing here by looking up the method dynamically on the object
							Object object = handlersStack.peek();
							Method method = object.getClass().getDeclaredMethod("wrapAround", MessageHandler.class);
							method.invoke(object, handler);
						}
					} else {
						// first MessageHandler, set it into the logger
//						System.out.println("   - pushing 1st handler " + attributes.getValue("class") + " onto stack");
						log.setRootHandler(handler);
					}
					handlersStack.push(handler);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}			
			} else if (qName.equalsIgnoreCase("param")) {
//				System.out.println(" - param   : " + attributes.getValue("name"));
				parameterName = attributes.getValue("name");
			} else if (qName.equalsIgnoreCase("extension")) {
//				System.out.println(" - extension  : " + attributes.getValue("class"));
				mode = Mode.IN_EXTENSION;
				try {
					String classname = attributes.getValue("class");			
					Extension extension = (Extension)Class.forName(classname).newInstance();
//					System.out.println("   - pushing new extension " + attributes.getValue("class") + " onto stack");
					if(!extensionsStack.isEmpty()) {
//						System.out.println("   - registering new extension " + attributes.getValue("class") + " to stack emeging element");
						((ExtensionPoint)extensionsStack.peek()).addExtension(extension);
					}					
					extensionsStack.push(extension);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}			
			}
//			printStack("HANDLERS (after)", handlersStack);
//			printStack("EXTENSIONS (after)", extensionsStack);			
		}

		public void endElement(String uri, String localName, String qName) throws SAXException {
//			System.out.println("----------------------------------------------------------------");
//			System.out.println("element ending: " + qName);
//			printStack("HANDLERS (before)", handlersStack);
//			printStack("EXTENSIONS (before)", extensionsStack);
			if(qName.equals("handler")) {				
				handlersStack.pop();
//				System.out.println(" - popping handler from stack, new depth is " + handlersStack.size());
			} else if(qName.equals("extension")) {
//				System.out.println(" - popping extension from stack, new depth is " + (handlersStack.size() - 1));
				if(extensionsStack.size() == 1) {
//					System.out.println("   - registering extension onto current handler");
					((ExtensionPoint)handlersStack.peek()).addExtension(extensionsStack.peek());
				}
				extensionsStack.pop();
			} else if(qName.equals("param")) {				
				try {
					switch(mode) {
					case IN_HANDLER:
//						System.out.println(" - registering parameter " + parameterName + " onto emerging handler");
						setField(handlersStack.peek(), parameterName, parameterValue.toString());
						break;
					case IN_EXTENSION:
//						System.out.println(" - registering parameter " + parameterName + " onto emerging extension");
						setField(extensionsStack.peek(), parameterName, parameterValue.toString());
						break;
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				// clean up
				parameterName = null;
				parameterValue.setLength(0);
			}
//			printStack("HANDLERS (after)", handlersStack);
//			printStack("EEXTENSIONS (after)", extensionsStack);
			
		}

		public void characters(char ch[], int start, int length) throws SAXException {
			if(parameterName != null) {
				parameterValue.append(new String(Arrays.copyOfRange(ch, start, start + length)));
			}
		}

		@Override
		public void warning(SAXParseException spe) throws SAXException {
			System.err.println("warning: " + spe.getSystemId() + " at line " + spe.getLineNumber() + " reports: " + spe.getMessage());
		}

		@Override
		public void error(SAXParseException spe) throws SAXException {
			String message = "error: " + spe.getSystemId() + " at line " + spe.getLineNumber() + " reports: " + spe.getMessage();
			System.err.println(message);
			throw new SAXException(message);
		}

		@Override
		public void fatalError(SAXParseException spe) throws SAXException {
			String message = "fatal: " + spe.getSystemId() + " at line " + spe.getLineNumber() + " reports: " + spe.getMessage();
			System.err.println(message);
			throw new SAXException(message);
		}
		
		
		private void setField(Object object, String name, String value) 
				throws SecurityException, NoSuchMethodException, IllegalArgumentException, 
				IllegalAccessException, InvocationTargetException {
			String setter = "set" + name.toUpperCase().charAt(0) + name.substring(1);
//			System.out.println("invoking: " + setter + "(" + value + ") on " + object.getClass().getSimpleName());
			Method method = object.getClass().getDeclaredMethod(setter, String.class);
			method.invoke(object, value.trim());
		}
		
//		private static <T> void printStack(String name, Stack<T> stack) {
//			StringBuilder buffer = new StringBuilder(name);
//			buffer.append(" [size=").append(stack.size()).append("]: ");
//			for(T element : stack ) {
//				buffer.append(element.getClass().getSimpleName()).append(" > ");
//			}
//			System.out.println(buffer);
//		}
	}	
}
