package win.transgirls.crossfabric.tools;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ClassUtils {
    public static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    public static Class<?> forName(String... classNames) {
        for (String className : classNames) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ignored) {
            }
        }

        throw new RuntimeException(String.format("ClassUtils.forName() did not find any class in list [%s]", ClassUtils.joinSeparator(classNames)));
    }

    public static MethodHandle firstVirtualOfName(Class<?> rootClass, MethodType descriptor, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                return lookup.findVirtual(rootClass, methodName, descriptor);
            } catch (NoSuchMethodException | IllegalAccessException ignored) {
            }
        }

        throw new RuntimeException(String.format("ClassUtils.firstVirtualOfName() did not find any methods from class %s in list [%s]", rootClass.getName(), ClassUtils.joinSeparator(methodNames)));
    }

    public static MethodHandle firstSpecialOfName(Class<?> rootClass, MethodType descriptor, Class<?> special, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                return lookup.findSpecial(rootClass, methodName, descriptor, special);
            } catch (NoSuchMethodException | IllegalAccessException ignored) {
            }
        }

        throw new RuntimeException(String.format("ClassUtils.firstSpecialOfName() did not find any methods from class %s in list [%s]", rootClass.getName(), ClassUtils.joinSeparator(methodNames)));
    }

    public static MethodHandle firstStaticMethodOfName(Class<?> rootClass, MethodType descriptor, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                return lookup.findStatic(rootClass, methodName, descriptor);
            } catch (NoSuchMethodException | IllegalAccessException ignored) {
            }
        }

        throw new RuntimeException(String.format("ClassUtils.firstStaticMethodOfName() did not find any methods from class %s in list [%s]", rootClass.getName(), ClassUtils.joinSeparator(methodNames)));
    }

    public static Field firstDeclaredFieldWithName(Class<?> rootClass, String... fieldNames) {
        for (String fieldName : fieldNames) {
            try {
                Field field = rootClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {
            }
        }

        throw new RuntimeException(String.format("ClassUtils.firstDeclaredFieldWithName() did not find any fields from class %s in list [%s]", rootClass.getName(), ClassUtils.joinSeparator(fieldNames)));
    }

    public static Field firstFieldWithName(Class<?> rootClass, String... fieldNames) {
        for (String fieldName : fieldNames) {
            try {
                Field field = rootClass.getField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {
            }
        }

        throw new RuntimeException(String.format("ClassUtils.firstFieldWithName() did not find any fields from class %s in list [%s]", rootClass.getName(), ClassUtils.joinSeparator(fieldNames)));
    }

    public static Method firstDeclaredMethodWithName(Class<?> rootClass, List<String> methodNames, Class<?>... paramTypes) {
        for (String methodName : methodNames) {
            try {
                return rootClass.getDeclaredMethod(methodName, paramTypes);
            } catch (NoSuchMethodException ignored) {
            }
        }

        throw new RuntimeException(String.format("ClassUtils.firstDeclaredMethodWithName() did not find any methods from class %s in list [%s]", rootClass.getName(), ClassUtils.joinSeparator(methodNames)));
    }

    public static MethodHandle unreflectFirstDeclaredMethodWithName(MethodHandles.Lookup caller, Class<?> rootClass, Class<?> special, List<String> methodNames, Class<?>... paramTypes) {
        Method method = firstDeclaredMethodWithName(rootClass, methodNames, paramTypes);

        try {
            method.setAccessible(true);
            return caller.unreflectSpecial(method, special);
        } catch (Throwable e) {
            throw new RuntimeException(String.format("ClassUtils.unreflectFirstDeclaredMethodWithName() could not unreflect method: %s", e));
        }
    }

    public static String joinSeparator(List<?> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).toString());
            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static String joinSeparator(Object[] list) {
        return joinSeparator(Arrays.asList(list));
    }
}