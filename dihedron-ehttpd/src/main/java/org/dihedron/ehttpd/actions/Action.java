/**
 * Copyright (c) 2011, 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron embeddable web container ("e-httpd").
 *
 * "e-httpd" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "e-httpd" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "e-httpd". If not, see <http://www.gnu.org/licenses/>.
 */

package org.dihedron.ehttpd.actions;

import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.dihedron.ehttpd.exceptions.ServerException;

/**
 * Base interface for all actions; see the <code>execute()</code>
 * method for details about the general contract between the
 * action controller and the individual actions implementing this
 * interface.
 *  
 * @author Andrea Funto'
 */
public interface Action {
	
	/**
	 * Result returned in case of success.
	 */
	public final static String SUCCESS = "SUCCESS";
	
	/**
	 * Result returned in case of (expected) error or failure.
	 */
	public final static String ERROR = "ERROR";
	
	/**
	 * Result returned when more info is needed from the caller.
	 */
	public final static String INPUT = "INPUT";
	
	/**
	 * Result returned when the Action did everything (including
	 * returning the presentation data to the caller).
	 */
	public final static String COMPLETE = "COMPLETE";
	
	/**
	 * The actual nuts'n'bolts of the action. An action should execute 
	 * its task and return the appropriate result code. The general 
	 * contract is as follows:<ul>
	 * <li>Action.SUCCESS should be returned when the action performs 
	 * its tasks without errors.</li>
	 * <li>Action.COMPLETE should be returned when the action performed
	 * its tasks <b>AND</b> provided the output, so no further processing
	 * is needed for rendering the view</li>
	 * <li>Action.INPUT should be returned when the input data are incomplete 
	 * and the input view should be sent back to the user, e.g. to fill
	 * additional information into a form</li>
	 * <li>Action.ERROR should be returned when an error occurred that was 
	 * handled by the action and which should result in a custom error
	 * page being shown to the user.</li>
	 * </ul>
	 * Any error which cannot be handled gracefully by the action should
	 * result in an ApplicationException being thrown; the exception will
	 * bubble up to the "pseudo-servlet" layer and will end up in a 
	 * generic error page being shown to the user. This latter mechanism
	 * should be used sparingly, and only to address cases where the error
	 * is unexpected.
	 * The generic error page could show some informational, warning and
	 * error messages as reported by the action in the <code>Notifications
	 * </code> thread specific information repository.  
	 * @return
	 *   a result code.
	 * @throws ApplicationException
	 *   if an unexpected, unmanageable application error occurred.
	 * @throws ServerException
	 *   if an internal server error occurred.
	 * @see ApplicationException.bankitalia.uhttpd.notifications.Notifications
	 */
	public String execute() throws ApplicationException, ServerException;
}
