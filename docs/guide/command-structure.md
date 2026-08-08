# Command Structure

A comshop command is built from three building blocks — `requires`, `arguments`, `executes` — plus `then` for subcommands:

```kotlin
register("somecommand") {
    requires { ... }      // who can use the command
    arguments { ... }     // what arguments it takes
    executes { ... }      // what it does
}
```

## register vs command

* `register("name") { ... }` — defines the command **and registers** it.
* `command("name") { ... }` — only builds the definition and returns a `CommandBuilder`. Register it later:

```kotlin
val someCommand = command("somecommand") {
    requires { sender.isOp }
}

// later:
register(someCommand)
```

## requires

The `requires` block decides who is allowed to execute the command. It runs with a `SourceContext` receiver, so `sender`, `entity`, and `player` are available:

```kotlin
register("somecommand") {
    requires { sender.isOp }
}
```

Only operators can use the command above. A sender check can be as complex as you want:

```kotlin
register("somecommand") {
    requires {
        when (sender) {
            is Entity -> true
            else -> false
        }
    }
}
```

If the `requires` block is not specified, **all senders are allowed** by default (server console, non-op players, and entities).

!!! warning
    You cannot use the `requires` block more than twice. It will be overwritten by the last one.

## arguments

The `arguments` block declares the command's arguments. See [Defining Arguments](arguments.md) for the full syntax, overloads, and branching.

## executes

The `executes` block is the command's behavior. It must return a `CommandResult` — see [Results & Errors](results-and-errors.md) for details:

```kotlin
register("somecommand") {
    arguments {
        "someInt" named int().asArg
    }

    executes {
        val intValue: Int = "someInt" to Int::class
        sender.sendMessage("Value is $intValue")

        CommandResult.SUCCESS
    }
}
```

## Subcommands with then

Use `then` to create subcommands:

```kotlin
register("myteam") {
    then("new") {
        arguments { ... }
        executes { ... }
    }
    then("join") {
        arguments { ... }
        executes { ... }
    }
}
```

This supports commands like:

```
/myteam new sans-team red
/myteam join sans-team ityeri
```

A `then` block accepts the same blocks as `register`/`command` — so subcommands of subcommands are possible. A subcommand can also be reused programmatically:

```kotlin
val newTeamCommand = command("new") { ... }

register("myteam") {
    then(newTeamCommand)
}
```

## Command overloads

Using the `arguments` block several times creates a number of overloads for the command:

```kotlin
register("mytp") {
    arguments {
        "target" named entity().asArg
        "to" named entity().asArg
    }
    arguments {
        "target" named entity().asArg
    }

    executes {
        val target = "target" to Entity::class
        val to = "to" toOrNull Entity::class

        ...
        CommandResult.SUCCESS
    }
}
```

## What the structure looks like internally

Each command becomes a tree:

```
literal ("somecommand")
└── union of branches
    ├── subcommand 1 (literal → arguments → executes)
    ├── subcommand 2
    └── default branch: arguments → executes
```

The DSL hides this completely — see [Architecture](../internals/architecture.md) if you are curious.
