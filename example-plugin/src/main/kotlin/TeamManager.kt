import com.github.ityeri.comshop.api.CommandResult
import com.github.ityeri.comshop.api.argument.StringType
import com.github.ityeri.comshop.api.exception.ComshopCommandException
import com.github.ityeri.comshop.command
import com.github.ityeri.comshop.customArgument
import com.github.ityeri.comshop.selectArgument
import com.github.ityeri.comshop.simpleSuggests
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player


val mm = MiniMessage.miniMessage()

object TeamManager {
    val teams: MutableList<Team> = mutableListOf(
        Team(
            "asdf",
            NamedTextColor.LIGHT_PURPLE,
            mutableListOf()
        )
    )

    fun findByName(name: String): Team? =
        teams.find({ it.name == name })
}

fun TeamArgumentType() = customArgument {
    native(quotedString())

    parses { nativeValue, source ->
        println(nativeValue)
        TeamManager.findByName(nativeValue) ?:
        throw ComshopCommandException("The team name not found")
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
            mm.deserialize(
                "Information about the team \"<$teamColorHex>${team.name}</$teamColorHex>\": "
            )
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
                sender.sendMessage(
                    mm.deserialize("<red>Team already exsists</red>")
                )
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
                mm.deserialize(
                    "Team \"<$teamColorHex>${newTeam.name}</$teamColorHex>\" is added"
                )
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
                    "Player \"<bold>${player.name}</bold>\" is added to team "
                            + "\"<$teamColorHex>${team.name}</$teamColorHex>\""
                )
            )
            CommandResult.SUCCESS
        }
    }
}
