package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.api.ComshopContext
import com.github.ityeri.comshop.impl.CommandFragment
import com.github.ityeri.comshop.impl.BrigadierNodeBuilder
import com.github.ityeri.comshop.impl.converter.argument.toBrigadierArgumentType
import com.github.ityeri.comshop.impl.optic.nodePTraversal
import com.github.ityeri.comshop.api.node.ComshopCommandNode
import com.github.ityeri.comshop.api.node.Node


fun toCommandFragmentNode(node: Node<ComshopCommandNode>): Node<CommandFragment> =
    nodePTraversal<ComshopCommandNode, CommandFragment>()
        .modify(
            node,
            { commandNode ->
                toCommandFragment(commandNode)
            }
        )

fun toCommandFragment(commandNode: ComshopCommandNode): CommandFragment =
    when (commandNode) {
        is ComshopCommandNode.LiteralCommandNode -> {
            CommandFragment.NodeBuilderFragment(
                BrigadierNodeBuilder.LiteralNodeBuilder(
                    commandNode.name, commandNode.requiresChecker
                )
            )
        }
        is ComshopCommandNode.ArgumentNode<*> -> {
            CommandFragment.NodeBuilderFragment(
                BrigadierNodeBuilder.ArgumentNodeBuilder(
                    commandNode.name,
                    argumentType = commandNode.argumentType.toBrigadierArgumentType(),
                    requiresChecker =  commandNode.requiresChecker,
                    suggestionProvider = commandNode.customSuggestionProvider?.let {
                        toBrigadierSuggestionProvider(it)
                    }
                )
            )
        }
        is ComshopCommandNode.ExecutionNode -> {
            CommandFragment.ExecutionFragment { ctx ->
                 commandNode.commandBlock(
                    object : ComshopContext {
                        override val source = ctx.source

                        override fun <T> getArgument(name: String, clazz: Class<T>): T {
                            return ctx.getArgument(name, clazz)
                        }
                    }
                ).toInt()
            }
        }
    }
