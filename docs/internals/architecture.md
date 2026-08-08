# Architecture

This page describes comshop's internal structure. You only need this if you are contributing to comshop or curious about how it works.

## Module layout

comshop is split into three core modules to support multiple Minecraft (Paper) versions:

| Module | Artifact | Responsibility |
|--------|----------|----------------|
| `comshop-interface` | `interface` | Version-independent API: node model, argument types, contexts, the `AbstractCommandRegistrar` SPI |
| `comshop-front` | `front` | The user-facing DSL: `register`/`command`, builders, argument factories |
| `comshop-impl:<version>` | `impl-<version>` | Version-specific implementation: converts comshop nodes into Brigadier nodes and registers them |

Users depend on `front` + one `impl` module. `front` never links against `impl` at compile time — the implementation is loaded via reflection (see below).

## The node model

All command definitions are built as trees of two sealed hierarchies:

```kotlin
sealed class Node<out T> {
    class SingleNode<T>(val value: T) : Node<T>()
    class UnionNode<T>(val nodes: Iterable<Node<T>>) : Node<T>()          // branching
    class ChainNode<T>(val nodes: Iterable<Node<T>>) : Node<T>()          // sequencing
}

sealed class ComshopCommandNode {
    class LiteralCommandNode(val name: String, val requiresChecker: ...)
    class ArgumentNode<T : Any>(val name: String, val argumentType: ComshopArgumentType<T>, ...)
    class ExecutionNode(val commandBlock: (ComshopContext) -> CommandResult)
}
```

A command built by `CommandBuilder.build()` has this shape:

```
ChainNode(
    SingleNode(LiteralCommandNode),          // the command's literal
    UnionNode(                                // branches
        subcommand trees...,
        ChainNode(                             // default branch
            UnionNode(argument nodes...),      // the arguments
            SingleNode(ExecutionNode)
        )
    )
)
```

## Argument types

* `NativeArgumentType<T>` — sealed hierarchy wrapping Brigadier/Paper argument types (40 types)
* `ComshopCustomArgumentType<T, N>` — user-defined converters over a native type

## Runtime loading

`comshop-front` does not know the implementation at compile time:

```kotlin
object ComshopLoader {
    var registrarClassPath: String = "com.github.ityeri.comshop.impl.CommandRegistrarImpl"

    fun loadRegistrarImpl(): AbstractCommandRegistrar =
        try {
            Class.forName(registrarClassPath).getDeclaredConstructor().newInstance()
                as AbstractCommandRegistrar
        } catch (_: ClassNotFoundException) {
            throw ClassNotFoundException(
                "The registrarClassPath is invalid or cannot find loader class. Is the comshop-impl dependency added?"
            )
        }
}

object CommandRegistrar : AbstractCommandRegistrar by ComshopLoader.loadRegistrarImpl()
```

`AbstractCommandRegistrar` (in `comshop-interface`) is the SPI every implementation provides:

```kotlin
interface AbstractCommandRegistrar {
    fun init(plugin: JavaPlugin)
    fun register(node: Node<ComshopCommandNode>)
}
```

## The conversion pipeline (inside an impl module)

`CommandRegistrarImpl` collects nodes via `register(node)`, then on Paper's `LifecycleEvents.COMMANDS` event converts and registers each one:

1. **toCommandFragmentNode** — maps `Node<ComshopCommandNode>` → `Node<CommandFragment>` using an optics traversal (`nodePTraversal`, built with Arrow Optics `PTraversal`/`PLens` and a custom `Choice3`/`split3` combinator).
2. **CommandFragment** — intermediate representation:

   ```kotlin
   sealed class CommandFragment {
       class NodeBuilderFragment(val builder: BrigadierNodeBuilder) : CommandFragment()
       class ExecutionFragment(val command: Command<CommandSourceStack>) : CommandFragment()
   }
   ```

3. **connectCommandFragments** — links fragments into a Brigadier tree via `BuilderBoundary`:

   ```kotlin
   class BuilderBoundary(
       val entries: Collection<BrigadierNodeBuilder>,
       val exits: Collection<BrigadierNodeBuilder>,
       val pendingCommand: Command<CommandSourceStack>? = null
   )
   ```

   * `SingleNode` → a boundary with one entry/exit
   * `UnionNode` → boundaries merged by flattening entries/exits
   * `ChainNode` → boundaries connected with `connectNext` (exits of one feed the entries of the next; a trailing `ExecutionFragment` becomes the pending command)
4. **Argument conversion** — `NativeArgumentType` → Brigadier `ArgumentType` via `nativeTypeToBrigadierArgumentType`. Paper types that resolve lazily (entity selectors, positions, ranges) are wrapped in `CustomNativeArgumentType`, a `CustomArgumentType.Converted` that resolves against the `CommandSourceStack` during parsing.
5. **Custom arguments** — `ComshopCustomArgumentType` → Paper's `CustomArgumentType` wrapper, delegating parse/suggest to the user's implementation.
6. **Suggestions** — comshop suggestion lambdas are converted to Brigadier `SuggestionProvider` (`toBrigadierSuggestionProvider`), with tooltips serialized via `MessageComponentSerializer`.

## Exception mapping

* `ComshopCommandException` (user-facing) → Brigadier `SimpleCommandExceptionType` — the message is shown to the sender
* `CommandSyntaxException` thrown from user blocks → wrapped in `IllegalStateException` with a warning (it belongs to Brigadier, not comshop)

## Adding a new Paper version

See [Adding a Version](adding-a-version.md).
