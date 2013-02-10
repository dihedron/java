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

import org.dihedron.ulog.Log;
import org.dihedron.ulog.handlers.ConsoleWriter;
import org.dihedron.ulog.handlers.FileWriter;
import org.dihedron.ulog.handlers.MessageHandler;
import org.dihedron.ulog.handlers.PolicyEnforcer;
import org.dihedron.ulog.handlers.SessionFormatter;
import org.dihedron.ulog.handlers.SimpleFormatter;
import org.dihedron.ulog.handlers.TeeHandler;
import org.dihedron.ulog.policies.GlobalPolicy;
import org.dihedron.ulog.policies.Or;
import org.dihedron.ulog.policies.Policy;
import org.dihedron.ulog.policies.SessionPolicy;


/**
 * Creates and initialises a Logger. 
 * 
 * @author Andrea Funto'
 */
public class DefaultLogFactory implements LogFactory {
	
	/**
	 * The <code>Log</code> archetype; all thread-specific instances will
	 * be cloned starting from this.
	 */
	private Log archetype = null;
	
	/**
	 * Initialises the <code>Log</code> archetype.
	 */
	@Override
	public void initialise() {
		Policy policies = 
				new Or(
					new SessionPolicy(),
					new GlobalPolicy()	
				);
		
		MessageHandler handlers = 
				new PolicyEnforcer(policies).wrapAround(
					new SimpleFormatter().wrapAround(
						new SessionFormatter().wrapAround(
							new TeeHandler().wrapAround(
									//new AsynchronousHandler(
											new ConsoleWriter(),
									//	),
									//new AsynchronousHandler(
											new FileWriter(FileWriter.DEFAULT_LOGFILE)
									//	)
								)
							)
						)
					);
		archetype = new Log();
		archetype.setRootHandler(handlers);		
	}
	
	/**
	 * Returns a cloned instance of the <code>Log</code>, for
	 * thread safety.
	 * 
	 * @return
	 *   a thread-specific instance of the <code>log</code>.
	 */
	public Log makeLog() {
		try {
			return archetype.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}


}