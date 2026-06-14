import com.github.ityeri.comshop.api.CommandResult
import com.github.ityeri.comshop.api.CommandWritingContext
import com.github.ityeri.comshop.api.argument.ComshopCustomArgumentType
import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.github.ityeri.comshop.api.argument.StringType
import com.github.ityeri.comshop.api.argument.SuggestionElement
import com.github.ityeri.comshop.api.exception.ComshopCommandException
import com.github.ityeri.comshop.command
import io.papermc.paper.command.brigadier.CommandSourceStack
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

class TeamArgumentType :
    ComshopCustomArgumentType<Team, String>(NativeArgumentType.StringArgumentType(StringType.QUOTED)) {
    override fun parse(
        nativeValue: String,
        source: CommandSourceStack
    ): Team =
        TeamManager.findByName(nativeValue) ?: throw ComshopCommandException("Team is not found")

    override fun suggest(
        writingContext: CommandWritingContext,
        source: CommandSourceStack
    ): Iterable<SuggestionElement> =
        TeamManager.teams.map {
            SuggestionElement("\"${it.name}\"")
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
        "team" to TeamArgumentType()
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
            "name" to quotedString()
            "color" to namedColor()
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
            "team" to TeamArgumentType()
            "player" to player()
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
