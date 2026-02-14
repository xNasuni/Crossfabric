package win.transgirls.crossfabric.multiversion;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import win.transgirls.crossfabric.tools.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class VersionedKeybind {
    public static KeyBinding create(String translationKey, InputUtil.Type type, int code, VersionedKeybindCategory category) {
        try {
            return createWithCategory(translationKey, type, code, category);
        } catch (Throwable e) {
            try {
                return createWithString(translationKey, type, code, category);
            } catch (Throwable e2) {
                String cast = null;
                if (e instanceof InvocationTargetException ite) {
                    cast = ite.getTargetException().toString();
                } else {
                    cast = e.toString();
                }

                throw new RuntimeException(String.format("Failed to create KeyBinding with either API version\n%s\n%s", cast, e2));
            }
        }
    }

    private static KeyBinding createWithCategory(String translationKey, InputUtil.Type type, int code, VersionedKeybindCategory category) throws Throwable {
        Class<?> categoryClass = ClassUtils.forName("net.minecraft.client.option.KeyBinding$Category", "net.minecraft.class_304$class_11900", "net.minecraft.client.KeyMapping$Category");
        Method createMethod = ClassUtils.firstDeclaredMethodWithName(categoryClass, List.of("create", "method_74698", "register"), Identifier.class);

        Object identifier = Identifier.of(category.modNamespace, category.path);
        Object categoryObj = category.instance != null ? category.instance : createMethod.invoke(null, identifier);

        if (category.instance == null) {
            category.instance = categoryObj;
        }

        Constructor<KeyBinding> constructor = KeyBinding.class.getConstructor(
                String.class,
                InputUtil.Type.class,
                int.class,
                categoryClass
        );

        return constructor.newInstance(translationKey, type, code, categoryObj);
    }

    private static KeyBinding createWithString(String translationKey, InputUtil.Type type, int code, VersionedKeybindCategory category) throws Exception {
        Constructor<KeyBinding> constructor = KeyBinding.class.getConstructor(
                String.class,
                InputUtil.Type.class,
                int.class,
                String.class
        );

        return constructor.newInstance(translationKey, type, code, category.path);
    }
}