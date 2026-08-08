# Reading Arguments

## Sender-related values

Inside `requires`, `executes`, and `suggests` blocks you have a `SourceContext` receiver:

| Property | Type | Meaning |
|----------|------|---------|
| `sender` | `CommandSender` | The command sender (console, player, or other entity) |
| `entity` | `Entity?` | The executing entity (`source.executor`) |
| `player` | `Player?` | The sender as a player, or `null` |

```kotlin
executes {
    val sender: CommandSender = sender
    val player: Player? = player
    val entity: Entity? = entity
    ...
}
```

## Reading argument values

Inside `executes`, four functions read argument values:

| Function | Returns | Behavior on missing argument / type mismatch |
|----------|---------|---------------------------------------------|
| `"name" to KClass` (infix) | `T` | throws `IllegalArgumentException` |
| `"name" toOrNull KClass` (infix) | `T?` | returns `null` |
| `get<T>("name")` (reified) | `T` | throws `IllegalArgumentException` |
| `getOrNull<T>("name")` (reified) | `T?` | returns `null` |

```kotlin
register("somecommand") {
    arguments {
        "someInt" named int().asArg
    }

    executes {
        val intValue: Int = "someInt" to Int::class
        val nullableInt: Int? = "someInt" toOrNull Int::class

        val intValue1: Int = get("someInt")
        val nullableInt1: Int? = getOrNull("someInt")

        CommandResult.SUCCESS
    }
}
```

!!! tip
    The type you read with must match the argument type's generic type. For example, `entities()` maps to `List<Entity>`:

    ```kotlin
    arguments {
        "entities" named entities().asArg
    }

    executes {
        val entities: List<Entity> = get("entities")
        ...
    }
    ```
