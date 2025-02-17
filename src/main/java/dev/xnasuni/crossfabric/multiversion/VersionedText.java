package dev.xnasuni.crossfabric.multiversion;

import dev.xnasuni.crossfabric.tools.TextImpl;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class VersionedText {
    public static Text of(String text) {
        return TextImpl.of(text);
    }

    public static Text of(String text, Object... fmt) {
        return TextImpl.of(String.format(text, fmt));
    }

    public static Text translatable(String key) {
        return TextImpl.translatable(key);
    }

    public static Text translatable(String key, Object... args) {
        return TextImpl.translatable(key, args);
    }
}