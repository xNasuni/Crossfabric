package win.transgirls.crossfabric.tools;

import win.transgirls.crossfabric.Constants;
import net.minecraft.text.Text;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.Objects;

import static win.transgirls.crossfabric.Constants.LOGGER;

public class TextImpl {
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    public static Text translatable(String key, Object... args) {
        try {
            Class<?> textClass = ClassUtils.forName("net.minecraft.class_2561", "net.minecraft.text.Text");
            Class<?> mutableTextClass = ClassUtils.forName("net.minecraft.class_5250", "net.minecraft.text.MutableText");
            MethodType methodType = MethodType.methodType(mutableTextClass, String.class, Object[].class);
            MethodHandle translatableMethod = ClassUtils.firstStaticMethodOfName(textClass, methodType, "method_43469", "translatable");
            return (Text) Objects.requireNonNull(translatableMethod.invoke(key, args));
        } catch (Throwable e) {
            try {
                Class<?> translatableTextClass = ClassUtils.forName("net.minecraft.class_2588", "net.minecraft.text.TranslatableText", "net.minecraft.text.TranslatableTextContent");
                Constructor<?> translatableTextCtor = translatableTextClass.getConstructor(String.class, Object[].class);

                return (Text) Objects.requireNonNull(translatableTextCtor.newInstance(key, args));
            } catch (Throwable e2) {
                LOGGER.error("Couldn't create translatable text for {}", key, e2);
            }
        }

        throw new IllegalStateException(String.format("Text.translatable(key, args) class couldn't be found for %s", Constants.getMinecraftVersion()));
    }
    public static Text of(String text) {
        try {
            Class<?> textClass = ClassUtils.forName("net.minecraft.class_2561", "net.minecraft.text.Text");
            Class<?> mutableTextClass = ClassUtils.forName("net.minecraft.class_5250", "net.minecraft.text.MutableText");
            MethodType methodType = MethodType.methodType(mutableTextClass, String.class);
            MethodHandle literalMethod = ClassUtils.firstStaticMethodOfName(textClass, methodType, "method_43470", "literal");
            return (Text) Objects.requireNonNull(literalMethod.invoke(text));
        } catch (Throwable e) {
            try {
                Class<?> textClass = ClassUtils.forName("net.minecraft.class_2561", "net.minecraft.text.Text");
                MethodType methodType = MethodType.methodType(textClass, String.class);
                MethodHandle ofMethod = ClassUtils.firstStaticMethodOfName(textClass, methodType, "method_30163", "of");
                return (Text) Objects.requireNonNull(ofMethod.invoke(text));
            } catch (Throwable e2) {
                try {
                    Class<?> literalTextClass = ClassUtils.forName("net.minecraft.class_8828$class_2585", "net.minecraft.text.LiteralText");
                    Constructor<?> ctor = literalTextClass.getConstructor(String.class);
                    return (Text) Objects.requireNonNull(ctor.newInstance(text));
                } catch (Throwable e3) {
                    LOGGER.error("Couldn't create literal text for {}", text, e3);
                }
            }
        }

        throw new IllegalStateException(String.format("Text.of class couldn't be found for %s", Constants.getMinecraftVersion()));
    }
}