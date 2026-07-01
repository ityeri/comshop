import com.github.ityeri.comshop.api.CommandResult
import com.github.ityeri.comshop.initComshop
import com.github.ityeri.comshop.register
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.java.JavaPlugin


class ComshopExamplePlugin : JavaPlugin() {

    override fun onEnable() {
        initComshop(this)

        register(teamCommand)

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
    }

    override fun onDisable() {
    }
}
