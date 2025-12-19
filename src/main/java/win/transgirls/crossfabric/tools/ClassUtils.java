package win.transgirls.crossfabric.tools;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ClassUtils {
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    public static Class<?> forName(String... classNames) {
        for (String className : classNames) {
            try {
                return Class.forName(className);
            } catch (Throwable ignored){
            }
        }

        throw new RuntimeException(String.format("ClassUtils.forName() did not find any class in list [%s]", ClassUtils.joinSeparator(classNames)));
    }

    public static MethodHandle firstVirtualOfName(Class<?> rootClass, MethodType descriptor, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                return lookup.findVirtual(rootClass, methodName, descriptor);
            } catch (Throwable ignored){
            }
        }

        throw new RuntimeException(String.format("ClassUtils.firstVirtualOfName() did not find any methods from class %s in list [%s]", rootClass.getName(), ClassUtils.joinSeparator(methodNames)));
    }

    public static MethodHandle firstSpecialOfName(Class<?> rootClass, MethodType descriptor, Class<?> special, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                return lookup.findSpecial(rootClass, methodName, descriptor, special);
            } catch (Throwable ignored){
            }
        }

        throw new RuntimeException(String.format("ClassUtils.firstSpecialOfName() did not find any methods from class %s in list [%s]", rootClass.getName(), ClassUtils.joinSeparator(methodNames)));
    }

    public static MethodHandle firstStaticMethodOfName(Class<?> rootClass, MethodType descriptor, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                return lookup.findStatic(rootClass, methodName, descriptor);
            } catch (Throwable ignored){
            }
        }

        throw new RuntimeException(String.format("ClassUtils.firstMethodOfName() did not find any methods from class %s in list [%s]", rootClass.getName(), ClassUtils.joinSeparator(methodNames)));
    }

    public static Field firstDeclaredFieldWithName(Class<?> rootClass, String... fieldNames) {
        for (String fieldName : fieldNames) {
            try {
                Field field = rootClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (Throwable ignored) {
            }
        }

        throw new RuntimeException(String.format("ClassUtils.firstFieldOfName() did not find any fields from class %s in list [%s]", rootClass.getName(), ClassUtils.joinSeparator(fieldNames)));
    }

    public static MethodHandle firstDeclaredMethodWithName(Class<?> rootClass, MethodType descriptor, Class<?> special, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                return lookup.findSpecial(rootClass, methodName, descriptor, special);
            } catch (Throwable ignored){
            }
        }

        throw new RuntimeException(String.format("ClassUtils.firstSpecialOfName() did not find any methods from class %s in list [%s]", rootClass.getName(), ClassUtils.joinSeparator(methodNames)));
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