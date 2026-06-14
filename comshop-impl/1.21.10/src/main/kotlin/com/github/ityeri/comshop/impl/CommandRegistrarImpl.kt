package com.github.ityeri.comshop.impl

import com.github.ityeri.comshop.api.entry.AbstractCommandRegistrar
import com.github.ityeri.comshop.impl.converter.connectCommandFragments
import com.github.ityeri.comshop.impl.converter.toCommandFragmentNode
import com.github.ityeri.comshop.api.node.ComshopCommandNode
import com.github.ityeri.comshop.api.node.Node
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
                val convertedNode = toCommandFragmentNode(node)
                val boundary = connectCommandFragments(convertedNode)

                if (boundary.entries.size != 1) {
                    throw IllegalArgumentException("??")
                }

                commands.register(boundary.entries.first().build() as LiteralCommandNode<CommandSourceStack>)
            }
        }
    }

    override fun register(node: Node<ComshopCommandNode>) {
        nodes.add(node)
    }
}
