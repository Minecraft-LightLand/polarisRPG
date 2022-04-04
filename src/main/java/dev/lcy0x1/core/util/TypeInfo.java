package dev.lcy0x1.core.util;

import java.lang.reflect.*;

public class TypeInfo {

	public static TypeInfo of(Class<?> cls) {
		return new TypeInfo(cls, null);
	}

	public static TypeInfo of(Field field) {
		return new TypeInfo(field.getType(), field.getGenericType());
	}

	private static TypeInfo of(Type type) {
		if (type instanceof Class) {
			Class<?> cls = (Class<?>) type;
			return new TypeInfo(cls, null);
		}
		if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() instanceof Class) {
			return new TypeInfo((Class<?>) ((ParameterizedType) type).getRawType(), type);
		}
		if (type instanceof GenericArrayType) {
			GenericArrayType array = (GenericArrayType) type;
			TypeInfo sub = of(array.getGenericComponentType());
			return new TypeInfo(Array.newInstance(sub.cls, 0).getClass(), array);
		}
		throw new IllegalStateException("type parameter cannot be converted to class. Generic Type: " + type + ", class: " + type.getClass());
	}

	private final Class<?> cls;
	private final Type type;

	private TypeInfo(Class<?> cls, Type type) {
		this.cls = cls;
		this.type = type;
	}

	public Class<?> getAsClass() {
		return cls;
	}

	public TypeInfo getComponentType() {
		Type com = null;
		if (type instanceof GenericArrayType) {
			GenericArrayType array = (GenericArrayType) type;
			com = array.getGenericComponentType();
		}
		return new TypeInfo(cls.getComponentType(), com);
	}

	public TypeInfo getGenericType(int i) {
		if (type instanceof ParameterizedType) {
			Type[] types = ((ParameterizedType) type).getActualTypeArguments();
			if (types.length <= i)
				throw new IllegalArgumentException("generic type " + type + "has " + types.length + " fields, accessing index " + i);
			return TypeInfo.of(types[i]);
		}
		throw new IllegalStateException("type parameter is missing. Type: " + type + ", Class: " + cls);
	}

	public Object newInstance() throws Exception {
		return cls.getConstructor().newInstance();
	}

	public boolean isArray() {
		return cls.isArray();
	}
}
