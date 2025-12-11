package com.github.ityeri.comshop.dsl

import com.github.ityeri.comshop.argument.ArgumentChainNode
import com.github.ityeri.comshop.argument.ArgumentNode
import com.mojang.brigadier.arguments.StringArgumentType

class ArgumentChainDSL {
    internal val argumentChains: MutableList<List<ArgumentNode>> = mutableListOf()

    fun arguments(block: ArgumentListDSL.() -> Unit) {
        argumentChains.add(ArgumentListDSL().apply(block).arguments)
    }

    fun build(): ArgumentChainNode {
        return ArgumentChainNode(argumentChains)
    }
}