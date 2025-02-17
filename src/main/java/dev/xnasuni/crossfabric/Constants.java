package dev.xnasuni.crossfabric;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {
    public static final Logger LOGGER = LogManager.getLogger("Crossfabric");

    public static String getMinecraftVersion() {
        return FabricLoader.getInstance().getModContainer("minecraft").orElseThrow(() -> new RuntimeException("FabricLoader.getInstance().getModContainer(\"minecraft\") is null")).getMetadata().getVersion().getFriendlyString();
    }
}