# Examples

Complete, copy-pasteable commands. These are the same examples provided by the `example-plugin` module in the repository.

## mytp — overloads & optional arguments

```kotlin
register("mytp") {
    requires { sender.isOp }

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

        val from = if (to != null) {
            target
        } else {
            if (entity != null) {
                entity!!
            } else {
                sender.sendMessage("Only entity can use this command!")
                return@executes CommandResult.FAILED
            }
        }

        val destination = to ?: target

        from.teleport(destination.location)

        CommandResult.SUCCESS
    }
}
```

## sendtozero — list argument

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

## fruit — enum & select arguments

```kotlin
enum class Fruit {
    APPLE,
    BANANA,
    JAVASCRIPT,
    STRAWBERRY
}

// enum-based argument
fun FruitArgumentType() = enumArgument(Fruit::class)

// selection-based argument (string)
fun StringFruitArgumentType() = selectArgument(Fruit.entries.map { it.name })

val fruitCommand = command("fruit") {
    arguments {
        "fruit" named FruitArgumentType().asArg
    }

    executes {
        val fruit = "fruit" to Fruit::class
        sender.sendMessage("You are choose the ${fruit}!")

        CommandResult.SUCCESS
    }
}

val stringFruitCommand = command("sfruit") {
    arguments {
        "fruit" named StringFruitArgumentType().asArg
    }

    executes {
        val fruit = "fruit" to String::class
        sender.sendMessage("You are choose the ${fruit}!")

        CommandResult.SUCCESS
    }
}
```

## myteam — custom argument & subcommands

```kotlin
val mm = MiniMessage.miniMessage()

object TeamManager {
    val teams: MutableList<Team> = mutableListOf(
        Team("asdf", NamedTextColor.LIGHT_PURPLE, mutableListOf())
    )

    fun findByName(name: String): Team? =
        teams.find({ it.name == name })
}

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

data class Team(
    val name: String,
    val color: NamedTextColor,
    val members: MutableList<Player>
)

val teamCommand = command("myteam") {
    requires { sender.isOp }

    arguments {
        "team" named TeamArgumentType().asArg
    }

    executes {
        val team = "team" to Team::class
        val teamColorHex = team.color.asHexString()

        sender.sendMessage(
            mm.deserialize("Information about the team \"<$teamColorHex>${team.name}</$teamColorHex>\": ")
        )
        sender.sendMessage(
            mm.deserialize("| ${team.members.size} players are joined to this team")
        )

        CommandResult.SUCCESS
    }

    then("new") {
        arguments {
            "name" named quotedString().asArg
            "color" named namedColor().asArg
        }

        executes {
            val existsTeam = TeamManager.findByName("name" to String::class)

            if (existsTeam != null) {
                sender.sendMessage(mm.deserialize("<red>Team already exsists</red>"))
                return@executes CommandResult.FAILED
            }

            val newTeam = Team(
                "name" to String::class,
                "color" to NamedTextColor::class,
                mutableListOf()
            )

            TeamManager.teams.add(newTeam)

            val teamColorHex = newTeam.color.asHexString()

            sender.sendMessage(
                mm.deserialize("Team \"<$teamColorHex>${newTeam.name}</$teamColorHex>\" is added")
            )

            CommandResult.SUCCESS
        }
    }

    then("join") {
        arguments {
            "team" named TeamArgumentType().asArg
            "player" named player().asArg
        }

        executes {
            val team = "team" to Team::class
            val player = "player" to Player::class

            team.members.add(player)

            val teamColorHex = team.color.asHexString()

            sender.sendMessage(
                mm.deserialize(
                    "Player \"<bold>${player.name}</bold>\" is added to team " +
                        "\"<$teamColorHex>${team.name}</$teamColorHex>\""
                )
            )

            CommandResult.SUCCESS
        }
    }
}
```

The commands above are registered in `onEnable`:

```kotlin
override fun onEnable() {
    initComshop(this)

    register(teamCommand)
    register(fruitCommand)
    register(stringFruitCommand)

    register("mytp") { ... }
    register("sendtozero") { ... }
}
```
