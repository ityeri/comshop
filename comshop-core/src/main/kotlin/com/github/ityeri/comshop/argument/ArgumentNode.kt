package com.github.ityeri.comshop.argument

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext

interface ArgumentNode {
    fun <S> connectArgumentBuilder(argumentBuilders: List<ArgumentBuilder<S, *>>): List<ArgumentBuilder<S, *>>
    fun <S> connectExecuteBlock(executeBlock: (CommandContext<S>) -> Int): List<ArgumentBuilder<S, *>>
}