package com.github.ityeri.comshop.impl

import com.github.ityeri.comshop.api.entry.AbstractCommandRegistrar
import com.github.ityeri.comshop.api.node.ComshopCommandNode
import com.github.ityeri.comshop.api.node.Node
import com.github.ityeri.comshop.impl.converter.toFinalBuilderBoundary
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin


class CommandRegistrarImpl : AbstractCommandRegistrar {
    val nodes: MutableList<Node<ComshopCommandNode>> = mutableListOf()

    override fun init(plugin: JavaPlugin) {
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val commands = event.registrar()

            for (node in nodes) {
                val builderBoundary = toFinalBuilderBoundary(node)

                if (builderBoundary.entries.size != 1) {
                    throw IllegalArgumentException("??")
                }

                commands.register(
                    builderBoundary.entries.first().build() as LiteralCommandNode<CommandSourceStack>
                )
            }
        }
    }

    override fun register(node: Node<ComshopCommandNode>) {
        nodes.add(node)
    }
}
