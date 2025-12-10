package com.github.ityeri.comshop

import com.github.ityeri.comshop.argument.SingleArgumentNode
import com.mojang.brigadier.arguments.ArgumentType

class ArgumentRegistrar(val arguments: MutableList<SingleArgumentNode<*>>) {
    operator fun <T> String.invoke(block: () -> ArgumentType<T>) {
        arguments.add(SingleArgumentNode(block(), this))
    }
}