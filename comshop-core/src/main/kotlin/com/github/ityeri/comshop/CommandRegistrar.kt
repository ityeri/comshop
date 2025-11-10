package com.github.ityeri.comshop

import com.github.ityeri.comshop.builder.CommandBuilder
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin

object CommandRegistrar {
    private val builders: MutableList<CommandBuilder> = mutableListOf()

    fun register(builder: CommandBuilder) {
        builders.add(builder)
    }

    fun lifecycleRegister(plugin: JavaPlugin) {
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { commands ->
            for (builder in builders) {
                val argumentBuilder = builder.createBuilder()
                commands.registrar().register(argumentBuilder.build())
            }
        }
    }
}