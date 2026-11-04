package com.github.ityeri.comshop.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import java.util.function.Predicate


class BrigadierNodeBuilder(
    val innerNode: TaillessBrigadierNode,
    val children: Iterable<BrigadierNodeBuilder>,
    val command: Command<CommandSourceStack>? = null
) {
    fun build(): CommandNode<CommandSourceStack> =
        when (innerNode) {
            is TaillessBrigadierNode.Literal ->
                LiteralCommandNode(
                    innerNode.literal,
                    command,
                    innerNode.requiresChecker,
                    null,
                    null,
                    false
                ).apply {
                    this@BrigadierNodeBuilder.children.forEach {
                        addChild(it.build())
                    }
                }
            is TaillessBrigadierNode.Argument ->
                ArgumentCommandNode(
                    innerNode.name,
                    innerNode.argumentType,
                    command,
                    innerNode.requiresChecker,
                    null,
                    null,
                    false,
                    innerNode.customSuggestions
                ).apply {
                    this@BrigadierNodeBuilder.children.forEach {
                        addChild(it.build())
                    }
                }
        }
}

sealed class TaillessBrigadierNode {
    abstract val requiresChecker: Predicate<CommandSourceStack>?

    data class Literal(
        val literal: String,
        override val requiresChecker: Predicate<CommandSourceStack>?
    ) : TaillessBrigadierNode()
    data class Argument(
        val name: String,
        val argumentType: ArgumentType<*>,
        val customSuggestions: SuggestionProvider<CommandSourceStack>?,
        override val requiresChecker: Predicate<CommandSourceStack>
    ) : TaillessBrigadierNode()
}
