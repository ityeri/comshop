import com.github.ityeri.comshop.builder.CommandDSL.Companion.command
import com.github.ityeri.comshop.CommandRegistrar
import com.mojang.brigadier.arguments.StringArgumentType
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import org.bukkit.plugin.java.JavaPlugin


class ComshopPlugin : JavaPlugin() {

    override fun onEnable() {
        CommandRegistrar.lifecycleRegister(this)

        val greetingCommand = command("greeting") {
            requires { source -> source.sender.isOp }

            arguments {
                "message" { StringArgumentType.word() }
            }

            executes { source ->
                val message = getArg("message", String::class)
                source.sender.sendMessage(message)

                0
            }

            then("player") {
                arguments {
                    "player" { ArgumentTypes.player() }
                    "message" { StringArgumentType.word() }
                }

                executes { source ->
                    val playerResolver = getArg("player", PlayerSelectorArgumentResolver::class)
                    val player = playerResolver.resolve(source).first()
                    val message = getArg("message", String::class)

                    player.sendMessage(message)

                    0
                }
            }
        }

        CommandRegistrar.register(greetingCommand)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}