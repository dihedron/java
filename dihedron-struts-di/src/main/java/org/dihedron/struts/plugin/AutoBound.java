/**
 * Copyright (c) 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron Struts2 Dependency Injection Plugin ("Struts-DI").
 *
 * "Struts-DI" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "Struts-DI" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "Struts-DI". If not, see <http://www.gnu.org/licenses/>.
 */

package org.dihedron.struts.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation represents the information needed to retrieve 
 * the resource to inject; the annotation can be used to provide
 * binding information in several ways, or simply to indicate that
 * the field should be automatically bound using intelligent 
 * defaults:<ol>
 * <li>if the <code>value</code> field is provided, then it is used 
 * to look up the resource or to instantiate it;</li>
 * <li>if the <code>alias</code> is provided, it is used to look up
 * any binding information in the injector's mapping</li>
 * <li>if neither <code>value</code> not <code>alias</code> are 
 * provided, an attempt is made to find a binding in the mapping
 * by looking it up under &lt;full class name&gt;#&lt;field name&gt;</li>
 * <li>last, if none has succeeded so far, the naming factory for
 * the given type is invoked; these factories can be registered 
 * through the injector's configuration parameters.</li></ol> 
 * 
 * @author Andrea Funto'
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoBound {
	
	/**
	 * The field type.
	 */
	public enum Type { 
		/** o
		 * Auto-detected. 
		 */
		AUTO,
		/**
		 * Plain old java object, with default constructor.
		 */
		POJO,
		/**
		 * A file system <code>file</code> object.
		 */
		FILE,
		/**
		 * A string.
		 */
		STRING,
		/**
		 * A random long value.
		 */
		RANDOM,
		/**
		 * A resource (JPG, BMP, etc.) loaded from the file system or
		 * the <code>CLASSPATH</code> as an InputStream.
		 */
		RESOURCE,
		/**
		 * A <code>Properties</code> object, pre-loaded from the file
		 * system or from a resource in the <code>CLASSPATH</code>.
		 */
		PROPERTIES,
		/**
		 * A JDBC data source.
		 */
		DATASOURCE,
		/**
		 * An EJB local or remote interface.
		 */
		EJB,
		/**
		 * A JMS queue.
		 */
		JMS_QUEUE,
		/**
		 * A JMS topic.
		 */
		JMS_TOPIC,
		/**
		 * A JMS connection factory.
		 */
		JMS_CONN_FACTORY,
		/**
		 * A JPA <code>UserTransaction</code> object.
		 */
		USER_TRANSACTION,
		/**
		 * A JPA <code>PersistenceContext</code> object.
		 */
		PERSISTENCE_CONTEXT	// JPA Persistence Context		
	};
	
	/**
	 * Substantial information useful to locate and instantiate/acquire
	 * the resource. This information depends on the resource type:<ul>
	 * <li>for <b>POJOs</b>, it is the class name</li>
	 * <li>for <b>Files</b>, it is the file path</li>
	 * <li>for <b>Random</b> numbers, it is the random seed</li>
	 * <li>for <b>Resources</b>, it is the resource path, first checked
	 * against the file system and then in the <code>CLASSPATH</code></li>
	 * <li>for <b>Properties</b>, it is the path to the properties file,
	 * first checked on the file system and then looked up in the <code>
	 * CLASSPATH</code></li> 
	 * <li>for <b>DataSources</b> it is the JNDI name</li>
	 * <li>for <b>EJB local/remote interfaces</b>, it is the JNDI name</li>
	 * <li>for <b>JMS queues</b>, it is the JNDI name</li>
	 * <li>for <b>JMS topics</b>, it is the JNDI name</li>
	 * <li>for <b>JMS connection factories</b>, it is the JNDI name</li>
	 * <li>for <b>User Transactions</b>, it is the JNDI name</li>
	 * <li>for <b>Persistence Context</b>, it is the JNDI name</li>
 	 * <li>for <b>Strings</b>, it is the string value, nonsensical as it 
 	 * may seem!</li></ul>
	 * 
	 * @return
	 *   information helpful to immediately retrieve the resource.
	 */
	String value() default "";
	
	/**
	 * The resource alias; this information should be provided as an
	 * alternative to the <code>value</code>; this allows for the
	 * injection of the same resource into multiple objects. The actual
	 * resource information will be looked up inside the injector's
	 * mapping file by this key.
	 * 
	 * @return
	 *   the resource alias.
	 */
	String alias() default "";
	
	/**
	 * Whether the injector should ignore binding errors 
	 * (be lenient) and leave the property unbound, or fail 
	 * spectacularly with an Exception.
	 * 
	 * @return
	 *   <code>true</code> if the binding should be tolerant
	 *   to configuration errors and ignore binding errors,
	 *   <code>false</code> if any error should result in an
	 *   Exception.  
	 */
	boolean lenient() default true;
	
	/**
	 * The injected dependency type.
	 * 
	 * @return
	 *   a value specifying the type of resource.
	 */
	Type type() default Type.AUTO;
}
