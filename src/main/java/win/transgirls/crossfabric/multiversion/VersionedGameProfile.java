package win.transgirls.crossfabric.multiversion;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class VersionedGameProfile {
    public static String nameof(GameProfile profile) {
        try {
            return (String) profile.getClass().getDeclaredMethod("name").invoke(profile);
        } catch (Throwable e) {
            return profile.getName();
        }
    }

    public static UUID uuidof(GameProfile profile) {
        try {
            return (UUID) profile.getClass().getDeclaredMethod("id").invoke(profile);
        } catch (Throwable e) {
            return profile.getId();
        }
    }
}