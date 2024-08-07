package nl.odalitadevelopments.menus.nms.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public final class Reflections {

    public static <T> FieldAccessor<T> getField(Class<?> clazz, String name, Class<T> fieldType) {
        return getField(clazz, name, fieldType, 0);
    }

    public static <T> FieldAccessor<T> getField(Class<?> clazz, Class<T> fieldType, int index) {
        return getField(clazz, null, fieldType, index);
    }

    private static <T> FieldAccessor<T> getField(Class<?> clazz, String name, Class<T> fieldType, int index) {
        for (Field field : clazz.getDeclaredFields()) {
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);
                return new FieldAccessor<T>() {
                    public T get(Object target) {
                        try {
                            return (T) field.get(target);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    public void set(Object target, Object value) {
                        try {
                            field.set(target, value);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    public boolean hasField(Object target) {
                        return field.getDeclaringClass().isAssignableFrom(target.getClass());
                    }
                };
            }
        }
        if (clazz.getSuperclass() != null) return getField(clazz.getSuperclass(), name, fieldType, index);
        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }

    public static <T> FieldAccessor<T> getField(Class<?> clazz, Class<?> returnType, String... fieldNames) {
        Field field;

        if (clazz != null) {
            try {
                for (String fieldName : fieldNames) {
                    field = clazz.getDeclaredField(fieldName);
                    if (returnType == null || returnType.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);

                        Field finalField = field;
                        return new FieldAccessor<T>() {
                            public T get(Object target) {
                                try {
                                    return (T) finalField.get(target);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException("Cannot access reflection.", e);
                                }
                            }

                            public void set(Object target, Object value) {
                                try {
                                    finalField.set(target, value);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException("Cannot access reflection.", e);
                                }
                            }

                            public boolean hasField(Object target) {
                                return finalField.getDeclaringClass().isAssignableFrom(target.getClass());
                            }
                        };
                    }
                }
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException("Cannot find field with names " + Arrays.toString(fieldNames));
    }

    public static MethodInvoker getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        return getTypedMethod(clazz, methodName, null, params);
    }

    public static Method getMethodSimply(Class<?> clazz, String method) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(method)) return m;
        }
        return null;
    }

    public static MethodInvoker getTypedMethod(Class<?> clazz, String methodName, Class<?> returnType, Class<?>... params) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (((methodName == null || method.getName().equals(methodName)) && returnType == null) || (method.getReturnType().equals(returnType) && Arrays.equals(
                    method.getParameterTypes(), params
            ))) {
                method.setAccessible(true);
                return (target, arguments) -> {
                    try {
                        return method.invoke(target, arguments);
                    } catch (Exception e) {
                        throw new RuntimeException("Cannot invoke method " + method, e);
                    }
                };
            }
        }
        if (clazz.getSuperclass() != null) return getMethod(clazz.getSuperclass(), methodName, params);
        throw new IllegalStateException(String.format("Unable to find method %s (%s).", methodName, List.of(params)));
    }

    public interface MethodInvoker {
        Object invoke(Object param1Object, Object... param1VarArgs);
    }

    public interface FieldAccessor<T> {
        T get(Object param1Object);

        void set(Object param1Object1, Object param1Object2);

        boolean hasField(Object param1Object);
    }
}