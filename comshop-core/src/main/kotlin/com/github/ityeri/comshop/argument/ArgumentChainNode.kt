package com.github.ityeri.comshop.argument

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext

class ArgumentChainNode(
    val argumentChains: List<List<ArgumentNode>>
): ArgumentNode {
    override fun <S> connectArgumentBuilder(argumentBuilders: List<ArgumentBuilder<S, *>>): List<ArgumentBuilder<S, *>> {
        return argumentChains.map { argumentChain ->
            var lastArgumentNodes: List<ArgumentBuilder<S, *>> = argumentBuilders

            for (argumentNode in argumentChain.reversed())  {
                lastArgumentNodes = argumentNode.connectArgumentBuilder(lastArgumentNodes)
            }

            return lastArgumentNodes
        }
    }

    override fun <S> connectExecuteBlock(executeBlock: (CommandContext<S>) -> Int): List<ArgumentBuilder<S, *>> {
        return argumentChains.map { argumentChain ->
            var lastArgumentNodes: List<ArgumentBuilder<S, *>> =
                argumentChain.last().connectExecuteBlock(executeBlock)


            for (argumentNode in argumentChain.subList(0, argumentChain.size - 1).reversed())  {
                lastArgumentNodes = argumentNode.connectArgumentBuilder(lastArgumentNodes)
            }

            return lastArgumentNodes
        }
    }
}