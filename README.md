<p align="center">
  <img src="docs/assets/favicon.svg" alt="comshop" width="128">
</p>

<h1 align="center">comshop</h1>

<p align="center">
  <b>A Kotlin DSL for defining Minecraft commands on Paper servers</b><br>
  Declarative, type-safe, version-independent — built on top of <a href="https://github.com/Mojang/brigadier">Brigadier</a>.
</p>

<p align="center">
  <a href="https://jitpack.io/#ityeri/comshop"><img src="https://jitpack.io/v/ityeri/comshop.svg" alt="JitPack"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License: MIT"></a>
  <a href="https://ityeri.github.io/comshop/"><img src="https://img.shields.io/badge/docs-ityeri.github.io%2Fcomshop-blue.svg" alt="Documentation"></a>
</p>

---

## What is comshop?

comshop is a library for defining Minecraft commands in Kotlin, made for the [Paper API](https://papermc.io/software/paper). Instead of hand-building Brigadier node trees — literals, arguments, requirement predicates, executors, suggestion providers — you write declarative DSL blocks:

```kotlin
register("sendtozero") {
    arguments {
        "entities" named entities().asArg
    }

    executes {
        val entities: List<Entity> = get("entities")

        entities.forEach {
            it.teleport(Location(it.world, 0.0, 0.0, 0.0))
        }

        CommandResult.SUCCESS
    }
}
```

## Features

- **Minimal boilerplate** — what takes a dozen+ lines of raw Brigadier fits in a few DSL blocks
- **Declarative DSL** — `requires` / `arguments` / `executes` / `then`, command overloads, and `unions` branching
- **40 built-in argument types** — primitives, strings, entities, positions, world state, registry types
- **Custom argument types** — defined as converters on top of native types (`customArgument`, `selectArgument`, `enumArgument`)
- **Custom suggestions** — with tooltips and QUOTED-string handling
- **Version-independent** — swap the `impl` module to support a different Paper version; your command code never changes

## Documentation

The full documentation lives at **[https://ityeri.github.io/comshop/](https://ityeri.github.io/comshop/)**:

- [Installation](https://ityeri.github.io/comshop/getting-started/installation/)
- [Quickstart](https://ityeri.github.io/comshop/getting-started/quickstart/)
- [Argument Types Reference](https://ityeri.github.io/comshop/reference/argument-types/)
- [Examples](https://ityeri.github.io/comshop/examples/)

## Installation

comshop is distributed via [JitPack](https://jitpack.io/#ityeri/comshop). Add the repository and **two** dependencies — the DSL (`front`) and the implementation for your Paper version (`impl-<version>`):

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.ityeri.comshop:front:v2.1.0")
    implementation("com.github.ityeri.comshop:impl-1.21.10:v2.1.0")
}
```

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ityeri.comshop:front:v2.1.0'
    implementation 'com.github.ityeri.comshop:impl-1.21.10:v2.1.0'
}
```

Initialize comshop before your plugin is enabled:

```kotlin
class MyPlugin : JavaPlugin() {
    override fun onEnable() {
        initComshop(this)
    }
}
```

## Supported Paper versions

| Paper version | Implementation module | Compatibility |
|---------------|-----------------------|---------------|
| 1.21.3        | `impl-1.21.10`         | ❌ Incompatible |
| 1.21.4        | `impl-1.21.10`         | ✅ Compatible |
| 1.21.10       | `impl-1.21.10`         | ✅ Compatible |
| 26.1.2        | `impl-1.21.10`         | ✅ Compatible |
| 26.2          | `impl-1.21.10`         | ✅ Compatible |

Support for Paper versions below 1.21.4 is planned.

## Examples

Complete, copy-pasteable commands are in the [examples documentation](https://ityeri.github.io/comshop/examples/) and in the [`example-plugin`](example-plugin/) module.

## License

[MIT](LICENSE)
