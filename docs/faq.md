# FAQ

---
## I used `requires` twice — only the last one works

By design. The `requires` block is overwritten by the last definition. See [Command Structure](guide/command-structure.md).

---
## Can `executes` return other integers?

No. Brigadier allows arbitrary integer results, but comshop currently only supports `0`/`1` via `CommandResult.SUCCESS` / `CommandResult.FAILED`. See [Results & Errors](guide/results-and-errors.md).

---
## Why do I need two dependencies (`front` + `impl-...`)?

The DSL (`front`) and the registration implementation (`impl`) are separate artifacts so that the same command code can run on multiple Paper versions. The `impl` module is loaded by reflection at runtime. See [Installation](getting-started/installation.md).

---
## Is there async / CompletableFuture support?

Not yet. Async command execution is on the roadmap.

---
## How do I add suggestions to a custom argument type?

Use the `suggests` block (or `simpleSuggests`) inside `customArgument { }`. A `suggests` block attached to an argument node overrides the type's own suggestions. See [Custom Arguments](guide/custom-arguments.md).
