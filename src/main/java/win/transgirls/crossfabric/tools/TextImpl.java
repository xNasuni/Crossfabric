package win.transgirls.crossfabric.tools;

import net.minecraft.text.MutableText;
import win.transgirls.crossfabric.Constants;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;

import java.util.Objects;

import net.minecraft.text.Text;

public class TextImpl {
    public static Text translatable(String key, Object... args) {
        Throwable e;
        Throwable e2;

        try { //----1.16.5->1.18.2
            Class<?> translatableTextClass = ClassUtils.forName("net.minecraft.network.chat.TranslatableComponent", "net.minecraft.class_2588", "net.minecraft.text.TranslatableText");
            Constructor<?> ctor = translatableTextClass.getConstructor(String.class, Object[].class);
            return (Text) Objects.requireNonNull(ctor.newInstance(key, args));
        } catch (Throwable _e) {
            e = _e;
            try { //----1.19->1.21.9
                Class<?> textClass = ClassUtils.forName("net.minecraft.network.chat.Component", "net.minecraft.class_2561", "net.minecraft.text.Text");
                Class<?> mutableTextClass = ClassUtils.forName("net.minecraft.network.chat.MutableComponent", "net.minecraft.class_5250", "net.minecraft.text.MutableText");
                MethodType methodType = MethodType.methodType(mutableTextClass, String.class, Object[].class);
                MethodHandle translatableMethod = ClassUtils.firstStaticMethodOfName(textClass, methodType, "translatable", "method_43469");
                return (Text) Objects.requireNonNull(translatableMethod.invoke(key, args));
            } catch (Throwable _e2) {
                e2 = _e2;
            }
        }

        throw new IllegalStateException(String.format("Translatable text couldn't be created for %s: \n%s\n%s", Constants.getMinecraftVersion(), e, e2));
    }

    public static Text of(String text) {
        Throwable e;

        try { //----1.16.5->1.21.9
            Class<?> textClass = ClassUtils.forName("net.minecraft.network.chat.Component", "net.minecraft.class_2561", "net.minecraft.text.Text");
            MethodType methodType = MethodType.methodType(textClass, String.class);
            MethodHandle ofMethod = ClassUtils.firstStaticMethodOfName(textClass, methodType, "nullToEmpty", "method_30163", "of");
            return (Text) Objects.requireNonNull(ofMethod.invoke(text));
        } catch (Throwable _e){
            e = _e;
        }

        throw new IllegalStateException(String.format("Literal text couldn't be created for %s: \n%s", Constants.getMinecraftVersion(), e));
    }
}