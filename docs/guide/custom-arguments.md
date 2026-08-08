# Custom Arguments

A custom argument type is always built on top of a `NativeArgumentType`. It works like a **converter**: the native value is parsed first, then converted into your own type by custom logic.

## The interface

`ComshopCustomArgumentType<T, N>` has three members:

* `nativeArgumentType: NativeArgumentType<N>` — the underlying native type
* `parse(nativeValue: N, source: CommandSourceStack): T` — convert the native value into your type
* `suggest(writingContext: CommandWritingContext, source: CommandSourceStack): Iterable<SuggestionElement>` — provide suggestions

```kotlin
class FruitArgumentType :
    ComshopCustomArgumentType<Fruit, String>(NativeArgumentType.StringArgumentType(StringType.WORD)) {

    override fun parse(nativeValue: String, source: CommandSourceStack): Fruit {
        return Fruit.valueOf(nativeValue.uppercase())
    }

    override fun suggest(
        writingContext: CommandWritingContext,
        source: CommandSourceStack
    ): Iterable<SuggestionElement> {
        return Fruit.entries
            .filter { it.name.lowercase().startsWith(writingContext.reminingLower) }
            .map { SuggestionElement(it.name.lowercase()) }
    }
}
```

Use it like any other argument type:

```kotlin
arguments {
    "fruit" named FruitArgumentType().asArg
}
```

## The builder DSL

For most cases, `customArgument` is more convenient than implementing the interface:

```kotlin
fun TeamArgumentType() = customArgument {
    native(quotedString())

    parses { nativeValue, source ->
        TeamManager.findByName(nativeValue)
            ?: throw ComshopCommandException("The team name was not found")
    }

    simpleSuggests(StringType.QUOTED) {
        TeamManager.teams.map { it.name }
    }
}
```

The builder requires three blocks — missing ones throw an `IllegalStateException` with a hint:

* `native(...)` — the underlying native argument type (all factory functions from [Argument Types](../reference/argument-types.md) are available)
* `parses { nativeValue, source -> ... }` — conversion logic
* `suggests { ... }` — suggestion logic (or use `simpleSuggests`)

## Convenience functions

### selectArgument — pick from a list

Creates a string-based argument that must match one of the given elements (case-insensitive by default):

```kotlin
fun StringFruitArgumentType() = selectArgument(Fruit.entries.map { it.name })
```

Overloads accept a provider lambda or varargs:

```kotlin
selectArgument { getAvailableNames() }            // dynamic list
selectArgument("red", "green", "blue")            // fixed list
```

Options:

| Parameter | Default | Meaning |
|-----------|---------|---------|
| `stringType` | `WORD` | Underlying string type |
| `ignoreCase` | `true` | Case-insensitive matching |
| `suggestOnlyMatches` | `true` | Only suggest matching elements |
| `whenException` | throws | Custom error when the input does not match (`ComshopCommandException("Value \"...\" does not exist.")`) |

### enumArgument — enum as argument

Turns any enum into an argument type:

```kotlin
fun FruitArgumentType() = enumArgument(Fruit::class)
```

The `lowercase` parameter (default `true`) controls whether enum names are parsed and suggested in lowercase.

### simpleSuggests — common suggestion patterns

An extension on `CustomArgumentTypeBuilder` for the most common suggestion setups:

```kotlin
simpleSuggests(
    stringType = StringType.QUOTED,
    ignoreCase = false,
    displayOnlyMatches = true
) {
    listOf("red", "green", "blue")
}
```

* `stringType` — when `QUOTED`, suggestions are wrapped in quotes and matching strips the quotes
* `ignoreCase` — case-insensitive prefix matching
* `displayOnlyMatches` — filter suggestions by the current input

## Error handling in parse

If parsing fails, throw `ComshopCommandException` — the message is shown to the sender:

```kotlin
parses { nativeValue, source ->
    val fruit = Fruit.entries.find { it.name.equals(nativeValue, ignoreCase = true) }
        ?: throw ComshopCommandException("Fruit name is wrong!")
    fruit
}
```

Brigadier's `CommandSyntaxException` is **not** allowed inside custom argument logic — throwing it is treated as a programming error (see [Results & Errors](results-and-errors.md)).
