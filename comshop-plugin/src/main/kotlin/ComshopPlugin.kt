import com.github.ityeri.comshop.command
import com.github.ityeri.comshop.register
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import org.bukkit.plugin.java.JavaPlugin


class ComshopPlugin : JavaPlugin() {

    override fun onEnable() {
        val command = command("comshop") {
            op = false // Not implemented yet

            arguments {
                "int" { IntegerArgumentType.integer() }
                "word" { StringArgumentType.word() }
                "bool" { BoolArgumentType.bool() }
            }

            executes { source ->
                val int = getArg("int", Int::class)
                val word = getArg("word1", String::class)
                val bool = getArg("bool", Boolean::class)
                source.sender.sendMessage("int: $int, word: $word, bool: $bool")

                0
            }

            then("tell") {
                arguments {
                    "player" { ArgumentTypes.player() }
                }

                executes { source ->
                    val player = getArg("player", PlayerSelectorArgumentResolver::class)
                        .resolve(source).first()

                    player.sendMessage("Someone want to talking with you!")

                    0
                }
            }

            then("op-only") {
                op = true // Not implemented yet

                executes { source ->
                    source.sender.sendMessage("You are used op-only command! good...")
                    0
                }

                then("wasans") {
                    executes { source ->
                        source.sender.sendMessage("wa papyrus")
                        0
                    }
                }
            }
        }

        register(command, this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}