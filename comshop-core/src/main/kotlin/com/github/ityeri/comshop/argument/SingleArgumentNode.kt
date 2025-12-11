package com.github.ityeri.comshop.argument

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext

class SingleArgumentNode<T>(val argumentType: ArgumentType<T>, val name: String): ArgumentNode {
    override fun <S> connectArgumentBuilder(argumentBuilders: List<ArgumentBuilder<S, *>>):
            List<ArgumentBuilder<S, *>> {

        val builder = RequiredArgumentBuilder.argument<S, T>(name, argumentType)
        for (otherBuilder in argumentBuilders) {
            builder.then(otherBuilder)
        }

        return listOf(builder)
    }

    override fun <S> connectExecuteBlock(executeBlock: (CommandContext<S>) -> Int): List<ArgumentBuilder<S, *>> {
        val builder = RequiredArgumentBuilder.argument<S, T>(name, argumentType)
        return listOf(
            builder.executes(executeBlock)
        )
    }
}