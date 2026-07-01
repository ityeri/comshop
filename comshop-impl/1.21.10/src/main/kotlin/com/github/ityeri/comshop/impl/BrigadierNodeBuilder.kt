package com.github.ityeri.comshop.impl

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import java.util.function.Predicate


sealed class BrigadierNodeBuilder {
    val children: MutableList<BrigadierNodeBuilder> = mutableListOf()
    var command: Command<CommandSourceStack>? = null
    abstract val requiresChecker: Predicate<CommandSourceStack>?

    abstract fun build(): CommandNode<CommandSourceStack>

    class LiteralNodeBuilder(
        val literal: String,
        override val requiresChecker: Predicate<CommandSourceStack>? = null
    ) : BrigadierNodeBuilder() {
        override fun build(): CommandNode<CommandSourceStack> {
            val node = LiteralCommandNode(
                literal,
                command,
                requiresChecker,
                null,
                null,
                false
            )

            children.forEach { node.addChild(it.build()) }

            return node
        }
    }

    class ArgumentNodeBuilder<T>(
        val name: String,
        val argumentType: ArgumentType<T>,
        override val requiresChecker: Predicate<CommandSourceStack>? = null,
        val suggestionProvider: SuggestionProvider<CommandSourceStack>? = null
    ) : BrigadierNodeBuilder() {
        override fun build(): CommandNode<CommandSourceStack> {
            val node = ArgumentCommandNode(
                name,
                argumentType,
                command,
                requiresChecker,
                null,
                null,
                false,
                suggestionProvider // TODO can compose function in here? yep.
            )

            children.forEach { node.addChild(it.build()) }

            return node
        }
    }
}
