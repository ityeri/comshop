# Defining Arguments

## The named ... asArg syntax

Inside an `arguments` block, pair a name with an argument type using the `named` infix function and the `asArg` extension property:

```kotlin
register("somecommand") {
    arguments {
        "boolean" named boolean().asArg
        "int" named int(-100, 100).asArg
        "double" named double().asArg
        "word" named word().asArg
        "quotedString" named quotedString().asArg
        "greedyString" named greedyString().asArg
    }
}
```

Each line means: *"add an argument named `int` with an integer type"*. The name is used later to read the value inside `executes` (see [Reading Arguments](reading-arguments.md)).

!!! note
    Do not confuse this with Kotlin's built-in `to` function. Earlier versions of comshop used `"name" to type()`; that syntax was removed in favor of `named ... asArg`.

## Argument options

A single argument can be customized before it is added:

```kotlin
arguments {
    "color" named word()
        .suggests {
            suggest("red")
            suggest("green")
        }
        .requires { sender.isOp }
        .asArg
}
```

* `requires { }` — per-argument permission check (runs with a `SourceContext` receiver)
* `suggests { }` — custom suggestions, see [Suggestions](suggestions.md)

## Multiple arguments blocks: overloads

Writing several `arguments` blocks creates overloads. Each block becomes an alternative argument chain:

```kotlin
register("mytp") {
    arguments {
        "target" named entity().asArg
        "to" named entity().asArg
    }
    arguments {
        "target" named entity().asArg
    }

    executes { ... }
}
```

This makes both `/mytp <target> <to>` and `/mytp <target>` valid.

## Branching with unions

For branching *within* a single argument chain, use `unions`. Every branch of a union is an alternative:

```kotlin
register("somecommand") {
    arguments {
        "int" named int().asArg

        unions {
            "boolean" named boolean().asArg
            "double" named double().asArg

            arguments {
                "entity" named entity().asArg
                "color" named namedColor().asArg
            }
        }

        "word" named word().asArg
    }
}
```

The command above can be used like:

```
/somecommand 10 false wasans
/somecommand 10 3.14 wasans

/somecommand 10 @s red wasans
```

## Adding argument structures programmatically

Besides the `arguments { }` block, a complete argument structure can be built and injected with `argument(builder)`:

```kotlin
import com.github.ityeri.comshop.builder.ArgumentStructureBuilder.ChainStructureBuilder

val commonArguments = ChainStructureBuilder().apply {
    "x" named int().asArg
    "y" named double().asArg
}

register("somecommand") {
    argument(commonArguments)

    executes {
        val x: Int = "x" to Int::class
        ...
        CommandResult.SUCCESS
    }
}
```

`argument(builder)` takes any `ArgumentStructureBuilder` (single, chain, or union) and adds it to the command's argument alternatives. It is the programmatic counterpart of the `arguments { }` block — it is **not** a subcommand. Use `then(builder)` to add subcommands programmatically (see [Command Structure](command-structure.md)).

## All argument types

See the [Argument Types Reference](../reference/argument-types.md) for the complete list of built-in types.
