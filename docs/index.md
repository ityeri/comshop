# comshop

comshop is a Kotlin DSL library for defining Minecraft commands on Paper servers, built on top of [Brigadier](https://github.com/Mojang/brigadier).

The goal is to let you write commands in a declarative, type-safe Kotlin DSL — without touching Brigadier nodes or version-specific Paper APIs directly:

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

## Highlights

- **Declarative DSL** — `requires` / `arguments` / `executes` / `then` building blocks
- **40 built-in argument types** — primitives, strings, entities, positions, world state, and registry types
- **Custom argument types** — defined as converters on top of native types
- **Custom suggestions** — with tooltips and QUOTED-string handling
- **Version-independent definitions** — swap the implementation module to support a different Paper version

## Quick links

| Page | Description |
|------|-------------|
| [Installation](getting-started/installation.md) | Add comshop to your plugin via JitPack |
| [Quickstart](getting-started/quickstart.md) | Define your first command end to end |
| [Version Compatibility](getting-started/compatibility.md) | Which Paper versions are supported |
| [Argument Types Reference](reference/argument-types.md) | All built-in argument types |
| [Examples](examples.md) | Complete copy-pasteable commands |

---

comshop is distributed under the [MIT License](https://github.com/ityeri/comshop/blob/develop/LICENSE).
