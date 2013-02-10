package org.dihedron.ulog;

import org.junit.Before;
import org.junit.Test;

public class LogTest {
	
	private static Logger logger = null;

	@Before
	public void setUp() throws Exception {
		Log.GlobalInfo.setLevel(Level.TRACE);
		Log.SessionInfo.setLevel(Level.TRACE);
		logger = Logger.getLogger();
	}

	@Test
	public void testLog() {
		
		// no exception, no arguments
		Log.trace("trace message");
		Log.debug("debug message");
		Log.info("info message");
		Log.warn("warn message");
		Log.error("error message");
		Log.fatal("fatal message");

		// no exception, with arguments
		Log.trace("trace message with arguments '{}' and '{}'", 1, 2);
		Log.debug("debug message with arguments '{}' and '{}'", 1, 2);
		Log.info("info message with arguments '{}' and '{}'", 1, 2);
		Log.warn("warn message with arguments '{}' and '{}'", 1, 2);
		Log.error("error message with arguments '{}' and '{}'", 1, 2);
		Log.fatal("fatal message with arguments '{}' and '{}'", 1, 2);
		
		Exception e = new Exception("test exception");
		
		// with exception, no arguments
		Log.trace(e, "trace message");
		Log.debug(e, "debug message");
		Log.info(e, "info message");
		Log.warn(e, "warn message");
		Log.error(e, "error message");
		Log.fatal(e, "fatal message");

		// with exception, with arguments		
		Log.trace(e, "trace message with arguments '{}' and '{}'", 1, 2);
		Log.debug(e, "debug message with arguments '{}' and '{}'", 1, 2);
		Log.info(e, "info message with arguments '{}' and '{}'", 1, 2);
		Log.warn(e, "warn message with arguments '{}' and '{}'", 1, 2);
		Log.error(e, "error message with arguments '{}' and '{}'", 1, 2);
		Log.fatal(e, "fatal message with arguments '{}' and '{}'", 1, 2);
	}

	@Test
	public void testLogger() {
		// old (i.e. log4j) style
		logger.trace("trace message with simple text");
		logger.debug("debug message with simple text");
		logger.info("info message with simple text");
		logger.warn("warn message with simple text");
		logger.error("error message with simple text");
		logger.fatal("fatal message with simple text");
		
		// new (SLF4J) style
		logger.trace("trace message with arguments '{}' and '{}'", 1, 2);
		logger.debug("debug message with arguments '{}' and '{}'", 1, 2);
		logger.info("info message with arguments '{}' and '{}'", 1, 2);
		logger.warn("warn message with arguments '{}' and '{}'", 1, 2);
		logger.error("error message with arguments '{}' and '{}'", 1, 2);
		logger.fatal("fatal message with arguments '{}' and '{}'", 1, 2);
	}
}
