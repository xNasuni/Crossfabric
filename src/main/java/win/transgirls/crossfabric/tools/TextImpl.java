package win.transgirls.crossfabric.tools;

import win.transgirls.crossfabric.Constants;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;

import java.util.Objects;

import net.minecraft.text.Text;

public class TextImpl {
    public static Text translatable(String key, Object... args) {
        try { //----1.16.5->1.19.3
            Class<?> translatableTextClass = ClassUtils.forName("net.minecraft.network.chat.TranslatableComponent", "net.minecraft.class_2588", "net.minecraft.text.TranslatableText");
            Constructor<?> ctor = translatableTextClass.getConstructor(String.class, Object[].class);
            return (Text) Objects.requireNonNull(ctor.newInstance(key, args));
        } catch (Throwable ignored) {
            try { //----1.19.4->1.21.9
                Class<?> translatableTextClass = ClassUtils.forName("net.minecraft.network.chat.TranslatableComponent", "net.minecraft.class_2588", "net.minecraft.text.TranslatableText");
                Constructor<?> ctor = translatableTextClass.getConstructor(String.class, String.class, Object[].class);
                return (Text) Objects.requireNonNull(ctor.newInstance(key, key, args));
            } catch (Throwable ignored2) {
            }
        }

        throw new IllegalStateException(String.format("Translatable text couldn't be created for %s", Constants.getMinecraftVersion()));
    }

    public static Text of(String text) {
        try { //----1.16.5->1.21.9
            Class<?> textClass = ClassUtils.forName("net.minecraft.network.chat.Component", "net.minecraft.class_2561", "net.minecraft.text.Text");
            MethodType methodType = MethodType.methodType(textClass, String.class);
            MethodHandle ofMethod = ClassUtils.firstStaticMethodOfName(textClass, methodType, "nullToEmpty", "method_30163", "of");
            return (Text) Objects.requireNonNull(ofMethod.invoke(text));
        } catch (Throwable ignored){
        }

        throw new IllegalStateException(String.format("Literal text couldn't be created for %s", Constants.getMinecraftVersion()));
    }
}