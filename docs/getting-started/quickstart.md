# Quickstart

This page walks through defining your first command end to end.

## 1. Initialize comshop

In your plugin's `onEnable`, call `initComshop`:

```kotlin
class MyPlugin : JavaPlugin() {

    override fun onEnable() {
        initComshop(this)
    }
}
```

## 2. Register your first command

Use the top-level `register` function:

```kotlin
import com.github.ityeri.comshop.register
import com.github.ityeri.comshop.api.CommandResult

register("hello") {
    executes {
        sender.sendMessage("Hello, world!")
        CommandResult.SUCCESS
    }
}
```

## 3. Add arguments

Arguments are declared with the `"name" named type().asArg` syntax inside an `arguments` block:

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

## 4. Restrict who can use it

The `requires` block returns whether the sender is allowed to execute the command:

```kotlin
register("banme") {
    requires { sender.isOp }

    executes {
        sender.sendMessage("You are an operator!")
        CommandResult.SUCCESS
    }
}
```

## 5. Build and run

* Add the dependencies from [Installation](installation.md).
* Build your plugin and drop the jar into your server's `plugins/` folder.
* The `example-plugin` module in the repository also provides a ready-to-run test setup (`gradle runServer`).

## What's next

* [Command Structure](../guide/command-structure.md) — `requires`, `arguments`, `executes`, subcommands
* [Defining Arguments](../guide/arguments.md) — overloads and branching
* [Custom Arguments](../guide/custom-arguments.md) — your own argument types
* [Examples](../examples.md) — complete commands
