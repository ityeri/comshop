package com.github.ityeri.comshop

import com.github.ityeri.comshop.argument.ArgumentChainNode
import com.github.ityeri.comshop.argument.ArgumentNode
import com.github.ityeri.comshop.argument.SingleArgumentNode
import com.mojang.brigadier.arguments.ArgumentType

class ArgumentDSL {
    val argumentChains: MutableList<List<ArgumentNode>> = mutableListOf()

    fun arguments(block: ArgumentRegistrar.() -> Unit) {
        argumentChains.add(ArgumentRegistrar().apply(block).arguments)
    }

    fun build(): ArgumentChainNode {
        return ArgumentChainNode(argumentChains)
    }
}



class ArgumentRegistrar {
    val arguments: MutableList<ArgumentNode> = mutableListOf()

    operator fun <T> String.invoke(block: () -> ArgumentType<T>) {
        arguments.add(SingleArgumentNode(block(), this))
    }

    fun then(block: ArgumentDSL.() -> Unit) {

    }
}