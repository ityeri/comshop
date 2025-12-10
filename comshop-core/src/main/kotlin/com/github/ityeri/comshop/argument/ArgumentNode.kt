package com.github.ityeri.comshop.argument

import com.mojang.brigadier.builder.ArgumentBuilder

interface ArgumentNode {
    fun <S> connectArgumentBuilder(argumentBuilders: List<ArgumentBuilder<S, *>>): List<ArgumentBuilder<S, *>>
}