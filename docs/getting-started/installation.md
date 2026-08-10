# Installation

comshop is distributed via [JitPack](https://jitpack.io/#ityeri/comshop).

## Dependencies

Add the JitPack repository and **two** dependencies to your plugin:

### Kotlin DSL

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.ityeri.comshop:front:v2.1.0")
    implementation("com.github.ityeri.comshop:impl-1.21.10:v2.1.0")
}
```

### Groovy

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ityeri.comshop:front:v2.1.0'
    implementation 'com.github.ityeri.comshop:impl-1.21.10:v2.1.0'
}
```

!!! note "Why two artifacts?"
    comshop is split into two parts:

    * `front` — the command definition DSL and command structure
    * `impl-<version>` — the actual implementation that registers commands on a specific Paper version

    You always need both. The `impl` module is loaded at runtime via reflection; if it is missing, comshop throws a `ClassNotFoundException` with a helpful message.

## Initialize

Call `initComshop` with your plugin instance **before the plugin is enabled**:

```kotlin
import com.github.ityeri.comshop.initComshop

class MyPlugin : JavaPlugin() {

    override fun onEnable() {
        initComshop(this)
    }
}
```

!!! warning
    `initComshop` must be called before the plugin is enabled. It registers the internal command registrar with Paper's lifecycle events, so commands defined after it will be picked up.

## Requirements

* Kotlin 2.x (comshop itself is compiled with Kotlin 2.3.0)
* JDK 21 or later (bytecode targets Java 21)
* Paper 1.21.4 or newer — see [Version Compatibility](compatibility.md)
