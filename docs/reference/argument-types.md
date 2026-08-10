# Argument Types Reference

comshop provides **40 built-in argument types** through factory functions. All of them are available inside `arguments` blocks, `unions`, and `customArgument` builders.

## Primitives

| Factory | Returns | Notes |
|---------|---------|-------|
| `boolean()` | `Boolean` | |
| `int(min, max)` | `Int` | Default range: `Int.MIN_VALUE`..`Int.MAX_VALUE` |
| `float(min, max)` | `Float` | |
| `double(min, max)` | `Double` | |
| `long(min, max)` | `Long` | |

## Strings

| Factory | Returns | Notes |
|---------|---------|-------|
| `string(type)` | `String` | `type: StringType` — `WORD`, `QUOTED`, or `GREEDY` |
| `word()` | `String` | Single word (no spaces) |
| `quotedString()` | `String` | Quoted string (spaces allowed inside quotes) |
| `greedyString()` | `String` | Everything after the argument, including spaces |

## Entities & Players

| Factory | Returns |
|---------|---------|
| `entity()` | `Entity` |
| `entities()` | `List<Entity>` |
| `player()` | `Player` |
| `players()` | `List<Player>` |
| `playerProfiles()` | `Collection<PlayerProfile>` |

## Positions & Rotation

| Factory | Returns | Notes |
|---------|---------|-------|
| `blockPosition()` | `BlockPosition` | Integer block coordinates |
| `finePosition(centerIntegers)` | `Location` | `centerIntegers` defaults to `false` |
| `rotation()` | `Rotation` | |

## World & State

| Factory | Returns |
|---------|---------|
| `blockState()` | `BlockState` |
| `itemStack()` | `ItemStack` |
| `itemPredicate()` | `ItemStackPredicate` |
| `namedColor()` | `NamedTextColor` |
| `component()` | `Component` |
| `style()` | `Style` |
| `signedMessage()` | `SignedMessageResolver` |
| `scoreboardDisplaySlot()` | `DisplaySlot` |
| `namespacedKey()` | `NamespacedKey` |
| `key()` | `Key` |
| `integerRange()` | `Range<Int>` |
| `doubleRange()` | `Range<Double>` |
| `world()` | `World` |
| `gameMode()` | `GameMode` |
| `heightMap()` | `HeightMap` |
| `uuid()` | `UUID` |
| `objectiveCriteria()` | `Criteria` |
| `entityAnchor()` | `LookAnchor` |
| `time(minTime)` | `Int` | `minTime` defaults to `0` |
| `templateMirror()` | `Mirror` |
| `templateRotation()` | `StructureRotation` |

## Registry Types

| Factory | Returns |
|---------|---------|
| `resource(registryKey)` | `T` — the registry entry (`registryKey: RegistryKey<T>`) |
| `resourceKey(registryKey)` | `TypedKey<T>` |

## Usage

```kotlin
arguments {
    "count" named int(0, 100).asArg
    "target" named player().asArg
    "position" named blockPosition().asArg
    "color" named namedColor().asArg
}
```

See [Defining Arguments](../guide/arguments.md) for the full syntax.
