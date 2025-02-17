# Crossfabric
<img src="/assets/logo.png" align="right" width="180px">

Crossfabric is a library that mods can use to integrate multiversion easily!

>[!IMPORTANT]
> Crossfabric is currently in **beta** and only supports **client-side Fabric mods** for now.  
> Server-side support is planned for a future update.

## 😖 How do I even use this???
You can add the following to your `build.gradle` to include Crossfabric in your mod:


```gradle
repositories {
    maven { url = "https://api.modrinth.com/maven" }
}

dependencies {
    modImplementation "maven.modrinth:crossfabric:0.1.0"
}
```

The [wiki](https://github.com/xNasuni/Crossfabric/wiki) contains information on the tools crossfabric provides.