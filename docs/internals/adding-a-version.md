# Adding a Version

comshop supports multiple Paper versions by publishing one `impl` module per version. This page explains how to add a new one, using the existing `comshop-impl/1.21.10` module as a template.

## Steps

### 1. Create the module

Copy the structure of `comshop-impl/1.21.10`:

```
comshop-impl/
└── <mc version>/                  e.g. comshop-impl/1.21.4
    ├── build.gradle.kts
    └── src/main/kotlin/com/github/ityeri/comshop/impl/...
```

### 2. Register it in the build

`settings.gradle.kts`:

```kotlin
include("comshop-impl")
include("comshop-impl:1.21.10")   // ← add your version here, e.g. "comshop-impl:1.21.4"
```

### 3. Declare the Paper API dependency

`comshop-impl/<version>/build.gradle.kts`:

```kotlin
dependencies {
    api("io.papermc.paper:paper-api:<version>-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:<version>-R0.1-SNAPSHOT")
}
```

The artifact name (`impl-<version>`) is derived automatically from the module path by the root publishing configuration.

### 4. Implement the registrar

Implement `AbstractCommandRegistrar` (from `comshop-interface`):

* `init(plugin)` — register a `LifecycleEvents.COMMANDS` handler that converts and registers all collected nodes
* `register(node)` — collect command definition nodes

### 5. Convert nodes to Brigadier

Reuse the conversion pipeline from `comshop-impl/1.21.10`:

* `toFinalBuilderBoundary` — one optics traversal maps `Node<ComshopCommandNode>` → `BrigadierBuilderBoundary` via `toBrigadierFragment` / `toBrigadierBuilderBoundary` and connects them
* the final `BrigadierBuilderBoundary` reduces to the Brigadier `LiteralCommandNode` root
* `nativeTypeToBrigadierArgumentType` — the mapping between comshop's `NativeArgumentType`s and the target version's `ArgumentTypes` is the part most likely to differ between versions, so check each type against the target Paper API

### 6. Test

* Point the `example-plugin` at the new module (change `implementation(project(":comshop-impl:..."))` and the Paper version) and run the test server.
* Verify the full feature set: arguments, overloads, unions, subcommands, custom arguments, and suggestions.

### 7. Update compatibility info

* The [Version Compatibility](../getting-started/compatibility.md) page
* The compatibility table in the repository README

## What can change between versions

* `ArgumentTypes` factory names and signatures (Paper's `io.papermc.paper.command.brigadier.argument.ArgumentTypes`)
* Resolver types for selector/position arguments
* Lifecycle event APIs

The command DSL (`comshop-front`) and the node model (`comshop-interface`) should stay unchanged across versions.
