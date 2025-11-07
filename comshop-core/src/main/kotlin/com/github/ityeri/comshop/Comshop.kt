package com.github.ityeri.comshop

import com.mojang.brigadier.arguments.IntegerArgumentType.getInteger
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin

fun command(name: String, block: CommandDSL.() -> Unit): CommandDSL {
    val commandDsl = CommandDSL(name)
    commandDsl.apply(block)
    return commandDsl
}

fun register(commandDsl: CommandDSL, plugin: JavaPlugin) {
    plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS) { commands ->
        val argumentBuilder = commandDsl.build()

        // TEST
        val testArgumentBuilder = literal<CommandSourceStack>("foo")
            .then(
                argument<CommandSourceStack, Int>("bar", integer())
                    .executes({ c ->
                        println("Bar is " + getInteger(c, "bar"))
                        1
                    })
            )
            .executes({ c ->
                println("Called foo with no arguments")
                1
            })

        commands.registrar().register(argumentBuilder.build())
        commands.registrar().register(
            testArgumentBuilder.build()
        )
    }
}