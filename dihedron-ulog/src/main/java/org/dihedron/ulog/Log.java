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

import org.dihedron.ulog.factories.LogFactory;
import org.dihedron.ulog.handlers.MessageHandler;


/**
 * A LOG4J-like logger class. In order to use it, initialise it in 
 * the application main, like this:
 * <pre>
 * logger = Logger.initialiseWithDefaults(Level.DEBUG, ThisClass.class);
 * </pre>
 * and then create a static, final reference in each class where
 * logging occurs, as follows:
 * <pre>
 * public MyClass {
 *     private final static Logger logger = Logger.getLogger(MyClass.class);
 *     //...
 * }
 * </pre>
 * The object can then be used in any method of the class.<br> 
 * The Logger uses appenders to output its messages; by default only the
 * synchronous console appender is created, but you can configure the
 * Logger to use any appender, including high-performance asynchronous
 * ones.  
 * 
 * @author Andrea Funto'
 */
public class Log implements Cloneable {
	
	/**
	 * The common, shared log factory object.
	 */
	private static LogFactory factory = null;
	
	/**
	 * The common log factory is initialised here, so that 
	 * configuration reading occurs only one at startup.
	 */
	static {
		try {
			String classname = System.getProperty("log.factory.class");
			if(classname == null || classname.trim().length() == 0) {
				classname = LogFactory.DEFAULT_FACTORY_CLASSNAME;
			}
			Class<?> clazz = Class.forName(classname);
			factory = (LogFactory)clazz.newInstance();
			factory.initialise();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The thread-local information about the current session.
	 */
	static final ThreadLocal<Log> LOG = new ThreadLocal<Log>() {
		
		@Override protected Log initialValue() {
			return factory.makeLog();		
		}
	};

	/** 
	 * Logger output handlers' chain. 
	 */
	private MessageHandler root;
	
	/**
	 * The logger message builder.
	 */
	private MessageBuilder builder;
	
	/**
	 * Default constructor.
	 */
	public Log() {
		builder = new MessageBuilder("{}"); 
	}
	
	/**
	 * Performs a deep copy of the object.
	 * 
	 * @return
	 *   a deep copy of the object.
	 */
	@Override
	public Log clone() throws CloneNotSupportedException {
		Log clone = (Log)super.clone();
		clone.root = this.root.clone();
		return clone;
	}
	
	/**
	 * 
	 * @param root
	 *   the root node of the handlers' chain.
	 */
	public void setRootHandler(MessageHandler root) {
		this.root = root;		
	}
		 			
	/**
	 * Logs a message if the current logging level is at least TRACE.
	 * 
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void trace(String message, Object... arguments) {
		LOG.get().write(Level.TRACE, null, message, arguments);
	}

	/**
	 * Logs a message if the current logging level is at least TRACE.
	 * 
	 * @param exception
	 *   an exception to be logged along with the message.
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void trace(Throwable exception, String message, Object... arguments) {
		LOG.get().write(Level.TRACE, null, message, arguments);
	}

	/**
	 * Logs a message if the current logging level is at least DEBUG.
	 * 
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void debug(String message, Object... arguments) {
		LOG.get().write(Level.DEBUG, null, message, arguments);
	}
	
	/**
	 * Logs a message if the current logging level is at least DEBUG.
	 * 
	 * @param exception
	 *   an exception to be logged along with the message.
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void debug(Throwable exception, String message, Object... arguments) {
		LOG.get().write(Level.DEBUG, null, message, arguments);
	}

	/**
	 * Logs a message if the current logging level is at least INFO.
	 * 
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void info(String message, Object... arguments) {
		LOG.get().write(Level.INFO, null, message, arguments);	
	}
	
	/**
	 * Logs a message if the current logging level is at least INFO.
	 * 
	 * @param exception
	 *   an exception to be logged along with the message.
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void info(Throwable exception, String message, Object... arguments) {
		LOG.get().write(Level.INFO, null, message, arguments);
	}
	
	/**
	 * Logs a message if the current logging level is at least WARN.
	 * 
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void warn(String message, Object... arguments) {
		LOG.get().write(Level.WARN, null, message, arguments);	
	}
	
	/**
	 * Logs a message if the current logging level is at least WARN.
	 * 
	 * @param exception
	 *   an exception to be logged along with the message.
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void warn(Throwable exception, String message, Object... arguments) {
		LOG.get().write(Level.WARN, exception, message, arguments);	
	}

	/**
	 * Logs a message if the current logging level is at least ERROR.
	 * 
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void error(String message, Object... arguments) {
		LOG.get().write(Level.ERROR, null, message, arguments);	
	}
	
	/**
	 * Logs a message if the current logging level is at least ERROR.
	 * 
	 * @param exception
	 *   an exception to be logged along with the message.
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void error(Throwable exception, String message, Object... arguments) {
		LOG.get().write(Level.ERROR, exception, message, arguments);	
	}	

	/**
	 * Logs a message if the current logging level is at least FATAL.
	 * 
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void fatal(String message, Object... arguments) {
		LOG.get().write(Level.FATAL, null, message, arguments);	
	}

	/**
	 * Logs a message if the current logging level is at least FATAL.
	 * 
	 * @param exception
	 *   an exception to be logged along with the message.
	 * @param message
	 *   the message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	public static void fatal(Throwable exception, String message, Object... arguments) {
		LOG.get().write(Level.FATAL, exception, message, arguments);
	}	

	
	/**
	 * Sends a new <code>Message</code> down the <code>MessageHandler</code>'s 
	 * pipeline. This method has package visibility because it can be invoked
	 * by the <code>Logger</code>'s methods to emulate the LOG4J interface.
	 * 
	 * @param level
	 *   the message logging level.
	 * @param exception
	 *   the exception, or <code>null</code> if none present.
	 * @param text
	 *   the unformatted message.
	 * @param arguments
	 *   an optional set of parameters that will replace '{}' place-holders
	 *   in the message.
	 */
	void write(Level level, Throwable exception, String text, Object... arguments) {
		Message message = LOG.get().builder.createMessage(level, exception, text, arguments);
		if(root != null) {
			root.onMessage(message);
		}
	}	
	
	/**
	 * This class represents the session-specific logging level.
	 * 
	 * @author Andrea Funto'
	 */
	public static class SessionInfo {
		
		/**
		 * The default logging level; by default traces are disabled, 
		 * otherwise they would enable logging for anyone despite the 
		 * value in the <code>GlobalPolicy</code>. 
		 */
		public static final  Level DEFAULT_LEVEL = Level.NONE;
		
		/**
		 * Thread-local session declaration and initialisation.
		 */
		public static final ThreadLocal<SessionInfo> SESSION = new ThreadLocal<SessionInfo>() {
			protected SessionInfo initialValue() {
				return new SessionInfo();
			}
		};
		
		/**
		 * The unique session identifier.
		 */
		private String id;
		
		/**
		 * The logger level
		 */
		private Level level;

		/**
		 * Default constructor.
		 */
		public SessionInfo() {
			this(null, DEFAULT_LEVEL);
		}

		/**
		 * Constructor.
		 * 
		 * @param id
		 *   the unique session identifier.
		 */
		public SessionInfo(String id) {
			this(id, DEFAULT_LEVEL);
		}

		/**
		 * Constructor.
		 * 
		 * @param id
		 *   the unique session identifier.
		 * @param level
		 *   the logging level.
		 */
		public SessionInfo(String id, Level level) {
			this.id = id;
			this.level = level;
		}
		
		/**
		 * Returns the session id.
		 * 
		 * @return
		 *   the session identifier.
		 */	
		public static String getId() {
			return SESSION.get().id;
		}
		
		/**
		 * Sets the session id.
		 * 
		 * @param id
		 *   the session identifier.
		 */	
		public static void setId(String id) {
			SESSION.get().id = id;
		}
		
		/**
		 * Returns the logging level.
		 * 
		 * @return
		 *   the session-specific logging level.
		 */	
		public static Level getLevel() {
			return SESSION.get().level;
		}
		
		/**
		 * Sets the logging level.
		 * 
		 * @param id
		 *   the session-specific logging level.
		 */	
		public static void setLevel(Level level) {
			SESSION.get().level = level;
		}
	}	
	
	/**
	 * This class represents the global logging level.
	 * 
	 * @author Andrea Funto'
	 */
	public static class GlobalInfo {
		
		/**
		 * The default, initial level for the global
		 * logging policy.
		 */
		public static final Level DEFAULT_LEVEL = Level.NONE;
		
		/**
		 * The global reference level; this is the value used
		 * for comparisons against message levels.
		 */
		private static Level level = DEFAULT_LEVEL;
		
		/**
		 * Returns the global level.
		 * 
		 * @return
		 *   the current value or the global level.
		 */
		public static Level getLevel() {
			return GlobalInfo.level;
		}
		
		/**
		 * Sets the global level.
		 * 
		 * @param level
		 *   the new value or the global level.
		 */
		public static void setLevel(Level level) {
			GlobalInfo.level = level;
		}
	}
}
