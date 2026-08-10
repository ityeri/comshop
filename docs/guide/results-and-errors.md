# Results & Errors

## CommandResult

Every `executes` block must return a `CommandResult`:

* `CommandResult.SUCCESS` — the command succeeded (mapped to brigadier result `1`)
* `CommandResult.FAILED` — the command failed (mapped to brigadier result `0`)

```kotlin
executes {
    ...
    CommandResult.SUCCESS
}
```

!!! note
    Brigadier allows returning any integer from a command, but comshop currently only supports `0` and `1` via `CommandResult`.

The result is different from an execution error: a failed result does not show an error message. For example, the vanilla command

```
/execute as @e[type=minecraft:villager] at @s run kill @s
```

returns `1` when any villager exists, and `0` when none do. Use `CommandResult.FAILED` for the latter case, and exceptions for error messages.

## ComshopCommandException

For command execution failures that should **show a message to the sender**, throw `ComshopCommandException`:

```kotlin
import com.github.ityeri.comshop.api.exception.ComshopCommandException

executes {
    val playerName = "playerName" to String::class

    if (!playerList.contains(playerName)) {
        throw ComshopCommandException("Target player does not exist!")
    }

    CommandResult.SUCCESS
}
```

The exception message is shown directly to the sender.

The same rule applies inside custom argument parsing (`parses` blocks and `ComshopCustomArgumentType.parse`) — see [Custom Arguments](custom-arguments.md).

## What not to throw

Brigadier's `CommandSyntaxException` belongs to the internal implementation. Throwing it from comshop blocks is treated as a programming error and re-wrapped in an `IllegalStateException` with an explanatory message:

> *"Command execution block defined in comshop should not throw CommandSyntaxException, which belongs to brigadier. Use ComshopCommandException instead"*

Always use `ComshopCommandException` for user-facing errors.
