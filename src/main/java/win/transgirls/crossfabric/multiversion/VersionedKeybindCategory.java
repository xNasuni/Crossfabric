package win.transgirls.crossfabric.multiversion;

public class VersionedKeybindCategory {
    public final String modNamespace;
    public final String path;
    public Object instance;

    public VersionedKeybindCategory(String modNamespace, String path) {
        this.modNamespace = modNamespace;
        this.path = path;
    }
}