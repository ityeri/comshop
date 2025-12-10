package com.github.ityeri.comshop.argument

import com.mojang.brigadier.builder.ArgumentBuilder

class ArgumentChainNode(val argumentChains: List<List<ArgumentNode>>): ArgumentNode {
    override fun <S> connectArgumentBuilder(argumentBuilders: List<ArgumentBuilder<S, *>>): List<ArgumentBuilder<S, *>> {
        return argumentChains.map { argumentChain ->
            var lastArgumentNodes = argumentBuilders

            for (argumentNode in argumentChain)  {
                lastArgumentNodes = argumentNode.connectArgumentBuilder(lastArgumentNodes)
            }

            return lastArgumentNodes
        }
    }
}