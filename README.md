Example of a command that sends the given entity to the zero coordinate

```kotlin
register("sendtozero") {
    arguments {
        "entities" to entities()
    }

    executes {
        val entities: List<Entity> = get("entities")

        entities.forEach {
            it.teleport(
                Location(it.world, 0.0, 0.0, 0.0)
            )
        }

        CommandResult.SUCCESS
    }
}
```

# comshop
[![](https://jitpack.io/v/ityeri/comshop.svg)](https://jitpack.io/#ityeri/comshop)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---
comshop is a library for defining Minecraft commands using Kotlin DSL in Paper API 
(based on [Brigadier](https://github.com/Mojang/brigadier))

Supported & tested Paper API versions are below:

| Paper version | Used implementation module | Compatibility   |
|---------------|----------------------------|-----------------|
| 1.21.3        | `impl-1.21.10`             | 🔴 Incompatable |
| 1.21.4        | `impl-1.21.10`             | 🟢 Compatable   |
| 1.21.10       | `impl-1.21.10`             | 🟢 Compatable   |
| 26.1.2        | `impl-1.21.10`             | 🟢 Compatable   |

Support for the Paper version under 1.21.4 is planned for the future

# dependencies
comshop is distributing via JitPack

---

kotlin

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}
```

```kotlin
dependencies {
    implementation("com.github.ityeri.comshop:front:v2.0.0")
    implementation("com.github.ityeri.comshop:impl-1.21.10:v2.0.0")
}
```

---

groovy

```groovy
repositories {
  maven { url 'https://jitpack.io' }
}
```

```groovy
dependencies {
  implementation 'com.github.ityeri.comshop:front:v2.0.0'
  implementation 'com.github.ityeri.comshop:impl-1.21.10:v2.0.0'
}
```

> comshop has two parts:
> * `comshop-front` (`comshop-interface`) : The command definition DSL and structure
> * `comshop-impl:...` : Actual implementation for registering the command defined using comshop to the Paper server
> 
> More details in the **internal & compat** section in below

# usage

## before defining command... `initComshop`

You need to initialize the comshop before Paper plugin loading using `initComshop(plugin: JavaPlugin)`

```kotlin
import com.github.ityeri.comshop.initComshop

class ComshopExamplePlugin : JavaPlugin() {

    override fun onEnable() {
        initComshop(this)
    }
}
```

It should be done before the plugin is enabled

## basics of command DSL

```kotlin
register("somecommand") {
    requires { ... }
    arguments { ... }
    executes { ... }
}
```

The `register` is a top-level function for defining and registering the command directly. 
If you want to do only command definition, you can use the `command` function like this:

```kotlin
val someCommand = command("somecommand") { 
    requires { ... }
    arguments { ... }
    executes { ... }
}
```

The `command` function does nothing but returns the definition of command

## `requires` block
The `requires` block is defines who can use this command.
This block should return true or false, which represents whether the sender is qualified to execute the command

---

```kotlin
val someCommand = command("somecommand") {
    requires { sender.isOp }
}
```
only OPs are allowed

---

```kotlin
register("somecommand") {
    requires {
        when (sender) {
            is Entity -> {
                true
            }
            else -> {
                false
            }
        }
    }
}
```
only entities are allowed

---

If the `requires` block is not specified,
it allows all senders in default (including server console, non-op player, and general entity)

> NOTE : You cannot use the `requires` block more than twice. It will be overwritten by the last one

## `arguments` block
The arguments block is a space for defining arguments. 
You can define arguments using the `to` infix function that pairs a string and an argument type. 
Do not confuse with kotlin built-in function `to`!

```kotlin
register("somecommand") {
    arguments {
        "boolean" to boolean()
        "int" to int(-100, 100)
        "double" to double()
        "word" to word()
        "quotedString" to quotedString()
        "greedyString" to greedyString()
    }
}
```

For example, `"boolean" to boolean()` is means **"I gonna add the argument that is named to `boolean` as a boolean type!"**

In the `register` or `command` block, you can use an `arguments` block several times. 
This allows you to make a number of overloads for the command. 
Below is an example of command overload:

```kotlin
register("mytp") {
    arguments {
        "target" to entity()
        "to" to entity()
    }
    arguments {
        "target" to entity()
    }

    executes {
        val target = "target" to Entity::class
        val to = "to" toOrNull Entity::class

        ...
        
        CommandResult.SUCCESS
    }
}
```
> Full example is in the `example-plugin/src/main/kotlin/ComshopExamplePlugin.kt`

---
You can make an argument branching using the `unions`

```kotlin
register("somecommand") {
    arguments {
        "int" to int()

        unions {
            "boolean" to boolean()
            "double" to double()
            
            arguments {
                "entity" to entity()
                "color" to namedColor()
            }
        }

        "word" to word()
    }
}
```

You can use the above command like this:
```
/somecommand 10 false wasans
/somecommand 10 3.14 wasans

/somecommand 10 @s red wasans
```

## `executes` block
In the `executes` block, you can define a behavior of a command.
You can get a sender-related value like `sender`, `player`, or `entity`;
and arguments using the `to`, `toOrNull`, `get`, or `getOrNull` function in there

(Do not confuse the `to` infix function with kotlin built-in `to` function)

```kotlin
register("somecommand") {
    arguments {
        "someInt" to int()
    }
    
    executes {
        val sender: CommandSender = sender
        val player: Player? = player
        val entity: Entity? = entity

        val intValue: Int = "someInt" to Int::class
        val nullableInt: Int? = "someInt" toOrNull Int::class
        
        val intValue1: Int = get("someInt")
        val nullableInt1: Int? = getOrNull("someInt")
        
        CommandResult.SUCCESS
    }
}
```

---

If the command execution fails for any reason, it should throw a `ComshopCommandException`.
The exception message will be shown to the sender

```kotlin
import com.github.ityeri.comshop.api.exception.ComshopCommandException

executes {
    val playerName = "playerName" to String::class
    
    if (!playerList.contains(playerName)) {
        throw ComshopCommandException("Target player does not exist!")
    }
}
```

---
The `executes` block must return whether the command execution succeeded using `CommandResult`.
It's different from a command execution error.
For example, this Minecraft command returns a 1 (which is `CommandResult.SUCCESS` in comshop)
when any villager entities exist,
or returns a 0 (which is `CommandResult.FAILED` in comshop) when the villager does not exist

```
/execute as @e[type=minecraft:villager] at @s run kill @s
```

In brigadier system(which is comshop based on), you can return any integer in the `executes` block,
But comshop only supports returning 0 or 1 via `CommandResult` for now

## subcommands

You can make a subcommand that works like this:

```
/myteam new sans-team red
/myteam join sans-team ityeri
```

To make a subcommand, using the `then` block:

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
> Full example is in the `example-plugin/src/main/kotlin/TeamManager.kt`

You can write a `then` block, same as the `register` and `command` block.
Also, that means you can make a subcommand of a subcommand

## custom suggestions

You can customize the suggestion like this:

```kotlin
register("somecommand") {
    requires { sender.isOp }

    arguments {
        "color" to word()
            .suggests {
                suggest("red")
                suggest("green")
                suggest("blue")
                suggest("white")
            }
    }
}
```

It will suggest `red`, `green`, etc

![](./readme_assets/suggestion_demo.png)

## custom arguments

You can make your own custom argument using `ComshopCustomArgumentType`

Below is a simple example of the fruit argument type:

```kotlin
enum class Fruit {
    APPLE,
    BANANA,
    JAVASCRIPT,
    BLUE_BERRY
}

class FruitArgumentType :
    ComshopCustomArgumentType<Fruit, String>(NativeArgumentType.StringArgumentType(StringType.WORD)) {

    override fun parse(
        nativeValue: String,
        source: CommandSourceStack
    ): Fruit {
        try {
            return Fruit.valueOf(nativeValue.uppercase())
        } catch (_: IllegalArgumentException) {
            throw ComshopCommandException("Fruit name is wrong!")
        }
    }

    override fun suggest(
        writingContext: CommandWritingContext,
        source: CommandSourceStack
    ): Iterable<SuggestionElement> {
        return Fruit.entries.filter {
            val lowercaseFruitName = it.name.lowercase()
            lowercaseFruitName.startsWith(writingContext.reminingLower)
        }.map {
            SuggestionElement(it.name.lowercase())
        }
    }
}
```

As you can see, a custom argument type is always based on another `NativeArgumentType`.
So `ComshopCustomArgumentType` works like a converter.
When `StringArgumentType`, which is the native type of `FruitArgumentType`,
is parsed as a word, that value is passed to `FruitArgumentType` and converted into the `Fruit` enum by custom logic

If the `parsing` method fails for any reason, it should throw the exception: `ComshopCommandException`.
And the error message will be shown to the sender

![](./readme_assets/exception_demo.png)

When using the `FruitArgumentType`, just do the same as others:

```kotlin
arguments {
    "fruit" to FruitArgumentType()
}
```

> The custom argument feature is not fully completed.
> It does not support the suggestion custom **in argument block** for now.
> But a more convenient argument customization and support are planned for the future

# examples
You can check more examples in `example-plugin/src/main/kotlin/ComshopExamplePlugin.kt`

```kotlin
register("somecommand") {
    // Requirements of command sender is defining here
    requires { sender.isOp }

    // Arguments are defining here
    arguments {
        "boolean" to boolean()
        "int" to int(-100, 100)
        "double" to double()
        "word" to word()
        "quotedString" to quotedString()
        "greedyString" to greedyString()
    }

    // The behavior of command is defining here
    executes {
        // You can get argument using `to` infix method
        val booleanValue = "boolean" to Boolean::class
        // or using `get` method
        val intValue = get<Int>("int")
        // this function returns null when the argument name or type does not exist
        val doubleValue = "double" toOrNull Double::class

        sender.sendMessage("Boolean is $booleanValue")
        sender.sendMessage("Integer is $intValue")
        sender.sendMessage("Double is $doubleValue")
        sender.sendMessage("Word is ${"int" to String::class}")
        sender.sendMessage("Quoted string is ${"quotedString" to String::class}")
        sender.sendMessage("Greedy string is ${"greedyString" to String::class}")

        // You should return whether command is succeeded
        // More details in below
        CommandResult.SUCCESS
    }
}
```

# internal & compat

> This part is about the comshop's internal structure

comshop is composed of several core modules for compat of multiple Minecraft versions (Paper api versions):

* `comshop-front` (in artifact, name as `comshop:front`)
* `comshop-interface` (in artifact, name as `comshop:interface`)
* `comshop-impl`
  * `1.21.10` (in artifact, name as `comshop:front`)

---

The `comshop-front` module includes the command DSL and several top-level functions like `initComshop`.
You'll maybe use this module finally.
Internally, the `comshop-front` module imports an `AbstractCommandRegistrar` 
that is implemented by one of the `comshop-impl` modules

The `comshop-interface` is a unified interface for all Paper api versions.
It provides a common command definition structure, and a set of native-supported types

The `comshop-impl:<mc version>` is the actual implementation of the `comshop-interface`.
Usually, this module does the job of 
converting the comshop command structure into the brigadier node and then registers it

Currently, only `comshop-impl:1.21.10` exists, but more will be added
