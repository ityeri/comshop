# FAQ

## My command with only `requires` and `executes` does nothing

Known limitation. Commands that define **no arguments and no subcommands** are registered as inert literals: the `executes` block is dropped during the internal node-connection step.

Workarounds:

* Add at least one argument, or
* Move the logic into a subcommand (`then`), or
* Wait for this limitation to be fixed in a future version.

## I used `requires` twice — only the last one works

By design. The `requires` block is overwritten by the last definition. See [Command Structure](guide/command-structure.md).

## Can `executes` return other integers?

No. Brigadier allows arbitrary integer results, but comshop currently only supports `0`/`1` via `CommandResult.SUCCESS` / `CommandResult.FAILED`. See [Results & Errors](guide/results-and-errors.md).

## Why do I need two dependencies (`front` + `impl-...`)?

The DSL (`front`) and the registration implementation (`impl`) are separate artifacts so that the same command code can run on multiple Paper versions. The `impl` module is loaded by reflection at runtime. See [Installation](getting-started/installation.md).

## Is there async / CompletableFuture support?

Not yet. Async command execution is on the roadmap.

## `CommandWritingContext.remining` looks like a typo

It is — the property is named `remining` (for "remaining") and is kept as-is for compatibility. Use it exactly as written. See [Suggestions](guide/suggestions.md).

## What does the `arguments(builder)` overload do?

`arguments(builder: CommandBuilder)` adds a subcommand, same as `then(builder)`. It exists as a programmatic alternative to the `then` block.

## How do I add suggestions to a custom argument type?

Use the `suggests` block (or `simpleSuggests`) inside `customArgument { }`. A `suggests` block attached to an argument node overrides the type's own suggestions. See [Custom Arguments](guide/custom-arguments.md).

## The README shows syntax that does not compile

The README can lag behind development. The documentation site is the authoritative reference — if something here disagrees with the README, trust the docs (and consider opening an issue).
