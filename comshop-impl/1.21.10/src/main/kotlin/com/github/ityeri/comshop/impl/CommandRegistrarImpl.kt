package com.github.ityeri.comshop.impl

import com.github.ityeri.comshop.api.entry.AbstractCommandRegistrar
import com.github.ityeri.comshop.api.node.ComshopCommandNode
import com.github.ityeri.comshop.api.node.Node
import com.github.ityeri.comshop.impl.converter.createTailWith
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
                val builderTail = node.createTailWith(BrigadierBuilderTail.BuilderTail(emptyList()))

                when (builderTail) {
                    is BrigadierBuilderTail.BuilderTail ->
                        if (builderTail.entries.size != 1)
                            throw IllegalStateException(
                                "The final builder tail must reduce to exactly one entry (the command's root literal), "
                                        + "but ${builderTail.entries.size} entries were found. "
                                        + "This is most likely an internal error in the comshop node conversion pipeline"
                            )
                        else commands.register(
                            builderTail.entries.first().build() as LiteralCommandNode<CommandSourceStack>
                        )
                    is BrigadierBuilderTail.CommandTail ->
                        throw IllegalStateException(
                            "The final builder tail must reduce to BuilderTail "
                                    + "but CommandTail were found. "
                                    + "This is most likely an internal error in the comshop node conversion pipeline"
                        )
                }
            }
        }
    }

    override fun register(node: Node<ComshopCommandNode>) {
        nodes.add(node)
    }
}
