package io.github.xtherk.bytehook.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Reflection utilities.
 *
 * @author Matt Coley
 * @author xDark
 */
public final class ReflectUtil {
	/**
	 * Deny all constructions.
	 */
	private ReflectUtil() {
	}

	/**
	 * @param declaringClass
	 * 		Class that declares the field.
	 * @param name
	 * 		Name of the field.
	 *
	 * @return {@link Field} object for the specified field in the class.
	 *
	 * @throws NoSuchFieldException
	 * 		When a field with the specified name is not found.
	 */
	public static Field getDeclaredField(Class<?> declaringClass, String name) throws NoSuchFieldException {
		Field field = declaringClass.getDeclaredField(name);
		field.setAccessible(true);
		return field;
	}

	/**
	 * @param declaringClass
	 * 		Class that declares the method.
	 * @param name
	 * 		Name of the method.
	 * @param args
	 * 		Argument types of the method.
	 *
	 * @return {@link Method} object for the specified field in the class.
	 *
	 * @throws NoSuchMethodException
	 * 		When a method with the specified name and argument specification is not found.
	 */
	public static Method getDeclaredMethod(Class<?> declaringClass, String name, Class<?>... args)
			throws NoSuchMethodException {
		Method method = declaringClass.getDeclaredMethod(name, args);
		method.setAccessible(true);
		return method;
	}

	/**
	 * @param type
	 * 		Class to construct.
	 * @param argTypes
	 * 		Argument types.
	 * @param args
	 * 		Argument values.
	 * @param <T>
	 * 		Assumed class type.
	 *
	 * @return New instance of class.
	 */
	public static <T> T quietNew(Class<T> type, Class<?>[] argTypes, Object[] args) {
		try {
			Constructor<T> constructor = type.getDeclaredConstructor(argTypes);
			constructor.setAccessible(true);
			return constructor.newInstance(args);
		} catch (ReflectiveOperationException ex) {
			throw new IllegalStateException("Constructor failure: " + type.getName(), ex);
		}
	}

	/**
	 * @param type
	 * 		Class the method is defined in.
	 * @param instance
	 * 		Instance to invoke in, or {@code null} for static.
	 * @param name
	 * 		Method name.
	 * @param argTypes
	 * 		Argument types.
	 * @param args
	 * 		Argument values.
	 * @param <T>
	 * 		Assumed class type.
	 *
	 * @return Return value of invoke.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T quietInvoke(Class<?> type, Object instance, String name, Class<?>[] argTypes, Object[] args) {
		try {
			Method method = type.getDeclaredMethod(name, argTypes);
			method.setAccessible(true);
			return (T) method.invoke(instance, args);
		} catch (ReflectiveOperationException ex) {
			throw new IllegalStateException("Invoke failure: " + type.getName(), ex);
		}
	}

	/**
	 * @param field
	 * 		Field to get from.
	 * @param instance
	 * 		Instance of the class.
	 *
	 * @return Field value in the instance.
	 *
	 * @throws IllegalAccessException
	 * 		When the field could not be accessed.
	 */
	private static Object get(Field field, Object instance) throws IllegalAccessException {
		field.setAccessible(true);
		return field.get(instance);
	}

	/**
	 * @param field
	 * 		Field to set.
	 * @param instance
	 * 		Instance of the class.
	 * @param value
	 * 		Value to set.
	 *
	 * @throws IllegalAccessException
	 * 		When the field could not be accessed.
	 */
	private static void set(Field field, Object instance, Object value) throws IllegalAccessException {
		field.setAccessible(true);
		field.set(instance, value);
	}
}
