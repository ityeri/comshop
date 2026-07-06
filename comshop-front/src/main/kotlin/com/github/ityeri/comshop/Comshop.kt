package com.github.ityeri.comshop

import com.github.ityeri.comshop.builder.CommandBuilder
import org.bukkit.plugin.java.JavaPlugin


fun command(name: String, block: CommandBuilder.() -> Unit): CommandBuilder {
    return CommandBuilder(name).apply(block)
}

fun initComshop(plugin: JavaPlugin) {
    CommandRegistrar.init(plugin)
}

fun register(name: String, block: CommandBuilder.() -> Unit) {
    CommandRegistrar.register(
        CommandBuilder(name).apply(block).build()
    )
}
fun register(builder: CommandBuilder) {
    CommandRegistrar.register(builder.build())
}
