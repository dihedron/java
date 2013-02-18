/**
 * Copyright (c) 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron Common Utilities library ("Commons").
 *
 * "Commons" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "Commons" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "Commons". If not, see <http://www.gnu.org/licenses/>.
 */

package org.dihedron.reflection;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A helper class to access object properties and method through reflection.
 * 
 * @author Andrea Funto'
 */
public class Reflector {

	/**
	 * Default value for whether the field access should be through getter methods
	 * or by reading the object field directly (value: <code>false</code>, meaning 
	 * that the fields are read directly through reflection, bypassing their getter 
	 * methods).
	 */
	public final static boolean DEFAULT_USE_GETTER = false;
	
	/**
	 * Default value for whether the non-public fields and methods should be
	 * made available through the inspector (value: <code>true</code>, meaning
	 * that fields and methods will be accessed regardless of their being protected
	 * or private).
	 */
	public final static boolean DEFAULT_EXPOSE_PRIVATE_FIELDS = true;
	
	/**
	 * The logger
	 */
	private final static Logger logger = LoggerFactory.getLogger(Reflector.class);
	
	/**
	 * The object under inspection.
	 */
	private Object object;
	
	/**
	 * Whether fields should be accessed using their getter method.
	 */
	private boolean useGetter = DEFAULT_USE_GETTER;
	
	/**
	 * Whether private and protected fields and methods should be directly 
	 * accessed (if <code>true</code>) or the getter and setter methods should 
	 * be used instead (if <code>false</code>).
	 */
	private boolean exposePrivateFields = DEFAULT_EXPOSE_PRIVATE_FIELDS;
	
	/**
	 * Constructor.
	 */
	public Reflector() {
		this(DEFAULT_USE_GETTER);
	}

	/**
	 * Constructor.
	 * 
	 * @param useGetter
	 *   whether fields should be accessed only through their getter.
	 */
	public Reflector(boolean useGetter) {
		this(DEFAULT_USE_GETTER, DEFAULT_EXPOSE_PRIVATE_FIELDS);
	}	

	/**
	 * Constructor.
	 * 
	 * @param useGetter
	 *   whether fields should be accessed only through their getter.
	 * @param expose
	 *   whether private and protected fields and methods should be made available
	 *   through the inspector as if they were public, by means of on-the-fly
	 *   transparent un-protection.
	 */
	public Reflector(boolean useGetter, boolean expose) {
		this.useGetter = useGetter;
		this.exposePrivateFields = expose;
	}	
	
	/**
	 * Applies the object inspector to the given object.
	 * 
	 * @param object
	 *   the object under inspection.
	 * @return
	 *   the inspector itself, for method chaining.
	 */
	public Reflector applyTo(Object object) {
		assert object != null : "object to inspect must not be null";
		this.object = object;
		return this;
	}

	/**
	 * Returns if access to fields is restricted to getter method invocation.
	 * 
	 * @return
	 *   <code>true</code> if fields are to be read only by calling their getter 
	 *   method, <code>false</code> if their value can be read directly through 
	 *   reflection, actually bypassing their getter method.
	 */
	public boolean isUseGetter() {
		return useGetter;
	}

	/**
	 * Sets whether the inspector should go through a getter methdo invocation to
	 * access the object's fields.
	 * 
	 * @param useGetter
	 *   set this to <code>true</code> if fields are to be read only by calling 
	 *   their getter method, to <code>false</code> if their value can be read 
	 *   directly through reflection, actually bypassing their getter method.
	 */
	public void setUseGetter(boolean useGetter) {
		this.useGetter = useGetter;
	}

	/**
	 * Returns whether the inspector will make private and protected methods and 
	 * fields available as if they were public.
	 * 
	 * @return
	 *   <code>true</code> if the inspector will treat public and private/protected
	 *   methods and fields alike, <code>false</code> if private and protected fields
	 *   and methods will be kept so and not exposed through the inspector. 
	 */
	public boolean isExposePrivateFields() {
		return exposePrivateFields;
	}

	/**
	 * Sets whether the inspector will make private and protected fields and 
	 * methods available to callers.
	 * 
	 * @param expose
	 *   set this to <code>true</code> to gain access to protected and private 
	 *   fields and methods through the inspector, to <code>false</code> to
	 *   keep private things private.
	 */
	public void setExposePrivateFields(boolean expose) {
		this.exposePrivateFields = expose;
	}
	
	/**
	 * Returns whether the object under inspection is an array of objects, e.g if 
	 * applied to <code>int[]</code>, it will return <code>true</code>.
	 * 
	 * @return
	 *   whether the object under inspection is an array of objects.
	 */
	public boolean isArray() {
		return object.getClass().isArray();
	}
	
	/**
	 * Returns whether the object under inspection is an instance of a <code>
	 * List&lt;?&gt;</code>.
	 * 
	 * @return
	 *   whether the object under inspection is a <code>List</code>. 
	 */
	public boolean isList() {
		return object instanceof List<?>;
	}
	
	/**
	 * Returns whether the object under inspection is an instance of a <code>
	 * Map&lt;?, ?&gt;</code>.
	 * 
	 * @return
	 *    whether the object under inspection is a <code>Map</code>.
	 */
	public boolean isMap() {
		return object instanceof Map<?, ?>;
	}
	
	/**
	 * Returns the number of elements in the array or list object;
	 * 
	 * @return
	 *   the number of elements in the array or list object.
	 * @throws ReflectorException
	 *   if the object is not a list or an array.
	 */
	public int getArrayLength() throws ReflectorException {
		int length = 0;
		if(object.getClass().isArray()) {
			length = Array.getLength(object);
		} else if (object instanceof List<?>){
			length = ((List<?>)object).size();
		} else {
			throw new ReflectorException("object is not an array or a list");
		}
		return length;
	}	

	/**
	 * Retrieves the value of a field.
	 * 
	 * @param fieldName
	 *   the name of the field.
	 * @return
	 *   the field value.
	 * @throws ReflectorException
	 *   if the getter method invocation fails or the field cannot be accessed 
	 *   directly.
	 */
	public Object getFieldValue(String fieldName) throws ReflectorException {
		
		assert fieldName != null : "error: field name must not be null";
		try {
			Object result = null;
			String name = fieldName.trim();
			if(useGetter) {
				logger.trace("accessing value using getter");
				String methodName = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);			
				result = invoke(methodName);
			} else {
				logger.trace("accessing value through exposePrivateFields field reading");
				Field field = object.getClass().getDeclaredField(name);
				boolean needReprotect = false;
				if(exposePrivateFields) {
					needReprotect = unprotect(field);
				}
				result = field.get(object);
				if(needReprotect) {				
					protect(field);
				}			
			}
			return result;
		} catch (SecurityException e) {
			logger.error("security exception trying to access " + fieldName, e);
			throw new ReflectorException("security exception trying to access method '" + fieldName + "'", e);
		} catch (NoSuchFieldException e) {
			logger.error("method '" + fieldName + "' not found on object of class '" + object.getClass().getSimpleName() + "'", e);
			throw new ReflectorException("method '" + fieldName + "' not found on object of class '" + object.getClass().getSimpleName() + "'", e);
		} catch (IllegalArgumentException e) {
			logger.error("illegal argument accessing field '" + fieldName + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
			throw new ReflectorException("illegal argument accessing field '" + fieldName + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
		} catch (IllegalAccessException e) {
			logger.error("illegal access to field '" + fieldName + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
			throw new ReflectorException("illegal argument accessing field '" + fieldName + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
		}		
	}
	
	/**
	 * Returns a reference to the method corresponding to the given name.
	 * 
	 * @param methodName
	 *   the name of the method to be returned.
	 * @return
	 *   the method object, or null if not found.
	 * @throws ReflectorException
	 *   if the method cannot be found on the object or a security violation 
	 *   is detected.
	 */
	public Method getMethod(String methodName) throws ReflectorException {
		assert methodName != null : "error: method name must not be null";
		
		try {
			return object.getClass().getMethod(methodName);
		} catch (SecurityException e) {
			logger.error("security esxception trying to access " + methodName, e);
			throw new ReflectorException("security exception trying to access method '" + methodName + "'", e);
		} catch (NoSuchMethodException e) {
			logger.error("method '" + methodName + "' not found on object of class '" + object.getClass().getSimpleName() + "'", e);
			throw new ReflectorException("method '" + methodName + "' not found on object of class '" + object.getClass().getSimpleName() + "'", e);
		}
	}
	
	/**
	 * Sets the value of the field.
	 * 
	 * @param fieldName
	 *   the name of the field.
	 * @param value
	 *   the new value of the field.
	 * @throws ReflectorException
	 *   if the field cannot be accessed; see the exception message for details.
	 */
	public void setFieldValue(String fieldName, Object value) throws ReflectorException {
		assert fieldName != null : "error: field name must not be null";

		try {
			String name = fieldName.trim();
			if(useGetter) {
				logger.info("accessing value using setter");
				String methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
				invoke(methodName);
			} else {
				logger.info("accessing value through exposePrivateFields field reading");
				Field field = object.getClass().getDeclaredField(name);
				boolean needReprotect = false;
				if(exposePrivateFields) {
					needReprotect = unprotect(field);
				}
				field.set(object, value);
				if(needReprotect) {				
					protect(field);
				}			
			}
		} catch(NoSuchFieldException e) {
			logger.error("field '" + fieldName + "' not found on object of class '" + object.getClass().getSimpleName() + "'", e);
			throw new ReflectorException("field '" + fieldName + "' not found on object of class '" + object.getClass().getSimpleName() + "'", e);			
		} catch (IllegalArgumentException e) {
			logger.error("illegal argument accessing field '" + fieldName + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
			throw new ReflectorException("illegal argument accessing field '" + fieldName + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
		} catch (IllegalAccessException e) {
			logger.error("illegal access to field '" + fieldName + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
			throw new ReflectorException("illegal access to field '" + fieldName + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
		}
	}
	
	/**
	 * If the object is an array or a subclass of <code>List</code>, it retrieves
	 * the element at the given index.
	 * 
	 * @param index
	 *   the offset of the element to be retrieved; this value can be positive 
	 *   (and the offset is calculated from the start of the array or list), or
	 *   negative, in which case the offset is calculated according to the rule
	 *   that element -1 is the last, -2 is the one before the last, etc.
	 * @return
	 *   the element at the given index.
	 * @throws ReflectorException
	 *   if the index is not valid for the given object or the object is not an 
	 *   array or a list.
	 */
	public Object getElementAtIndex(int index) throws ReflectorException {		
		Object result = null;
		if(object.getClass().isArray()) {
			result = Array.get(object, translateArrayIndex(index, getArrayLength()));
		} else if (object instanceof List<?>){
			result = ((List<?>)object).get(translateArrayIndex(index, getArrayLength()));
		} else {
			throw new ReflectorException("object of class '" + object.getClass().getSimpleName() + "'is not an array or a list");
		}
		return result;
	}
	
	/**
	 * Sets the value of the n-th element in an array or a list.
	 * 
	 * @param index
	 *   the offset of the element to be set; this value can be positive 
	 *   (and the offset is calculated from the start of the array or list), or
	 *   negative, in which case the offset is calculated according to the rule
	 *   that element -1 is the last, -2 is the one before the last, etc.
	 * @throws Exception
	 *   if the object is not a list or an array.
	 */
	public void setElementAtIndex(int index, Object value) throws Exception {
		
		if(object.getClass().isArray()) {
			 Array.set(object, translateArrayIndex(index, getArrayLength()), value);
//		} else if (object instanceof List<?>){
//			((List<?>)object).set(index, value);
		} else {
			throw new Exception("object is not an array or a list");
		}		
	}
	

	
	/**
	 * If the object is a <code>Map</code> or a subclass, it retrieves the 
	 * element corresponding to the given key.
	 * 
	 * @param key
	 *   the key corresponding to the element to be retrieved.
	 * @return
	 *   the element corresponding to the given key, or <code>null</code> if none 
	 *   found.
	 * @throws ReflectorException
	 *   if the object is not <code>Map</code>.
	 */
	public Object getValueForKey(Object key) throws ReflectorException {		
		Object result = null;
		if(object instanceof Map) {
			result = ((Map<?, ?>)object).get(key);
		} else {
			throw new ReflectorException("object of class '" + object.getClass().getSimpleName() + "' is not a map");
		}
		return result;
	}

	/**
	 * If the object is a <code>Map</code> or a subclass, it sets the 
	 * element corresponding to the given key.
	 * 
	 * @param key
	 *   the key corresponding to the element to be retrieved.
	 * @param value
	 *   the value to be set.
	 * @throws ReflectorException
	 *   if the object is not <code>Map</code>.
	 */
	public void setValueForKey(Object key, Object value) throws ReflectorException {
		// TODO: to be implemented using type recognition on generics' types
	}
		
	/**
	 * Invokes a method on the object under inspection.
	 * 
	 * @param methodName
	 *   the name of the method.
	 * @param args
	 *   the optional method arguments.
	 * @return
	 *   the return value of the given method.
	 * @throws ReflectorException
	 *   if any of the intermediate reflection methods raises a problem during
	 *   the object access.
	 */
	public Object invoke(String methodName, Object... args) throws ReflectorException {
		assert methodName != null : "error: method name must not be null";
		Method method;
		try {
			method = object.getClass().getMethod(methodName);
			return invoke(method, args);
		} catch (SecurityException e) {
			logger.error("security esxception trying to access " + methodName, e);
			throw new ReflectorException("security exception trying to access method '" + methodName + "'", e);
		} catch (NoSuchMethodException e) {
			logger.error("method '" + methodName + "' not found on object of class '" + object.getClass().getSimpleName() + "'", e);
			throw new ReflectorException("method '" + methodName + "' not found on object of class '" + object.getClass().getSimpleName() + "'", e);
		}
		
	}
	
	/**
	 * Invokes a method on the object under inspection.
	 * 
	 * @param method
	 *   the method to invoke.
	 * @param args
	 *   the optional method arguments.
	 * @return
	 *   the return value of the given method.
	 * @throws ReflectorException
	 *   if any of the intermediate reflection methods raises a problem during
	 *   the object access.
	 */
	public Object invoke(Method method, Object... args) throws ReflectorException {
		assert method != null : "error: method must not be null";
		try {
			boolean needReprotect = false;
			if(exposePrivateFields) {
				needReprotect = unprotect(method);
			}
			Object result = method.invoke(object, args);			
		
			if(needReprotect) {
				protect(method);
			}
			return result;
		} catch (IllegalArgumentException e) {
			logger.error("illegal argument accessing method '" + method.getName() + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
			throw new ReflectorException("illegal argument accessing method '" + method.getName() + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
		} catch (IllegalAccessException e) {
			logger.error("illegal access to method '" + method.getName() + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
			throw new ReflectorException("illegal access to method '" + method.getName() + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
		} catch (InvocationTargetException e) {
			logger.error("invocation target exception for method '" + method.getName() + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
			throw new ReflectorException("invocation target exception for method '" + method.getName() + "' on object of class '" + object.getClass().getSimpleName() + "'", e);
		}
	}	
	
	/**
	 * Checks whether the given name corresponds to a field in the object.
	 * 
	 * @param fieldName
	 *   the name of the field.
	 * @return
	 *   whether the name corresponds to an existing object field.
	 * @throws Exception
	 */
	public boolean isField(String fieldName) {
		assert fieldName != null : "error: field name must not be null";		
		try {
			object.getClass().getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			return false;		
		}
		return true;
	}
	
	/**
	 * Checks whether the given name corresponds to a method in the class.
	 * 
	 * @param methodName
	 *   the name of the method.
	 * @return
	 *   whether the name corresponds to an existing class method.
	 * @throws Exception
	 */
	public boolean isMethod(String methodName) {
		assert methodName != null : "error: method name must not be null";
		try {
			object.getClass().getMethod(methodName);
		} catch (NoSuchMethodException e) {
			return false;		
		}
		return true;
	}
		
	/**
	 * Checks if the given field or method is protected or private, and if so
	 * makes it publicly accessible.
	 * 
	 * @param accessible
	 *   the field or method to be made public.
	 * @return
	 *   <code>true</code> if the field or method had to be modified in order to
	 *   be made accessible, <code>false</code> if no change was needed.
	 */
	public boolean unprotect(AccessibleObject accessible) {
		if(!accessible.isAccessible()) {
			accessible.setAccessible(true);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the given field or method is public, and if so makes it 
	 * unaccessible.
	 * 
	 * @param accessible
	 *   the field or method to be made private.
	 * @return
	 *   <code>true</code> if the field or method had to be modified in order to
	 *   be made private, <code>false</code> if no change was needed.
	 */
	public boolean protect(AccessibleObject accessible) {
		if(accessible.isAccessible()) {
			accessible.setAccessible(false);
			return true;
		}
		return false;
	}
	
	public boolean isSubClassOf(Class<?> clazz) {
		if(clazz == null) return false;
//		String classname = object.getClass().getName();
//		for(Class<?> clazz : obj.getClasses()) {
//			if(clazz.getName().equals(classname)) {
//				return true;
//			}
//		}
		try {
			object.getClass().asSubclass(clazz);
			return true;
		} catch(ClassCastException e) {
			return false;
		}
		
//		String classname = obj.getName();
//		for(Class<?> clazz : object.getClass().getClasses()) {
//			if(classname.equals(clazz.getName())) {
//				return true;
//			}
//		}
		//return false;
	}
	
	/**
	 * Utility method that translates an array index, either positive or negative, 
	 * into its positive representation. The method ensures that the index is within
	 * array bounds, then it translates negatiuve indexes to their positive 
	 * counterparts according to the simple rule that element at index -1 is the 
	 * last element in the array, index -2 is the one before the last, and so on.
	 *  
	 * @param index
	 *   the element offset within the array or the list.
	 * @param length
	 *   the length of the array or the list.
	 * @return
	 *   the actual (positive) index of the element.
	 */
	private int translateArrayIndex(int index, int length) {
		assert (index > 0 ? index < length : Math.abs(index) <= Math.abs(length)) : "index must be less than number of elements";
		
		if(!(index > 0 ? index < length : Math.abs(index) <= Math.abs(length))){
			logger.error("index is out of bounmds");
			throw new ArrayIndexOutOfBoundsException();
		}
		if(index < 0) {
			index = length + index;
		}
		return index;
	}
}
