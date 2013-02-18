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
package org.dihedron.ehttpd.notifications;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Notifications extends ThreadLocal<List<Notification>> {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Notifications.class);

	/**
	 * The per-thread instance.
	 */
	private static ThreadLocal<Notifications> tls = null;
	    
	/**
	 * Retrieves the thread-local instance of the Notifications
	 * container.
	 * 
	 * @return 
	 *   the thread-local instance.
	 */
	public static Notifications getInstance() {
		if (tls == null) {
			synchronized(Notifications.class) {
				if (tls == null) {
					System.out.println("creating thread-local instance");
					tls = new ThreadLocal<Notifications> () {
			            @Override protected Notifications initialValue() {
			                return new Notifications();
			            }
					};
				}
			}
		}		
		return tls.get();
	}	

	/**
	 * Private constructor; it allocates resources in
	 * the thread-local stack. 
	 */
	private Notifications() {
		notifications = new ArrayList<Notification>();
	}
	
	/**
	 * The list of notifications.
	 */
	private List<Notification> notifications;
	
	/**
	 * Adds a notification to the list for the current 
	 * thread.
	 * 
	 * @param notification
	 *   the notification to be added.
	 */
	public void add(Notification notification) {
		if(notification != null){
			logger.debug("adding notification " + notification.toString() + " to thread "
					+ getCurrentThreadId());
			notifications.add(notification);
		}
	}
	
	/**
	 * Retrieves the list of all notifications.
	 * 
	 * @return
	 *   the list of all notifications.
	 */
	public List<Notification> getAll() {
		logger.debug("retrieving list of notifications for thread " + getCurrentThreadId());
		return notifications;
	}
	
	/**
	 * Clears the list of registered notification for
	 * the current thread.
	 */
	public void clear() {
		logger.debug("clearing notifications for thread " + getCurrentThreadId());
		notifications.clear();
	}
	
	/**
	 * Utility method: returns the current thread id.
	 * 
	 * @return
	 *   the current thread id.
	 */
	private static long getCurrentThreadId() {
		return Thread.currentThread().getId();		
	}
	
	/**
	 * @param args
	 *
	public static void main(String[] args) {
		logger = Logger.initialiseWithDefaults(Level.INFO, Notifications.class);
		for(int i = 0; i < 10; ++i) {
			new Thread(new Runnable() {

				//@Override
				public void run() {
					Notifications notifications = Notifications.getInstance();
					notifications.add(new Notification(Type.INFO, "hallo from thread " + getCurrentThreadId()));
					if(getCurrentThreadId() % 2 == 0) {
						notifications.add(new Notification(Type.ERROR, "goodbye from thread " + getCurrentThreadId()));
					}
					
					List<Notification> list = notifications.getAll();
					for (Notification notification : list) {
						logger.info(" > notif: " + notification);
					}
				}
				
			}).start();
		}
	}
	*/
}