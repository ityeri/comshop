import com.github.ityeri.comshop.api.CommandResult
import com.github.ityeri.comshop.api.CommandWritingContext
import com.github.ityeri.comshop.api.argument.ComshopCustomArgumentType
import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.github.ityeri.comshop.api.argument.SuggestionElement
import com.github.ityeri.comshop.api.exception.ComshopCommandException
import com.github.ityeri.comshop.initComshop
import com.github.ityeri.comshop.register
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Entity
import org.bukkit.plugin.java.JavaPlugin
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


class ComshopPlugin : JavaPlugin() {

    override fun onEnable() {
        initComshop(this)

        register("mytp") {
            requires { sender.isOp }

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
    }

    override fun onDisable() {
    }
}
