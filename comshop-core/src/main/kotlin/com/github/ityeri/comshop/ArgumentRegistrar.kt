package com.github.ityeri.comshop

import com.github.ityeri.comshop.argument.SingleArgumentData
import com.mojang.brigadier.arguments.ArgumentType

class ArgumentRegistrar(val arguments: MutableList<SingleArgumentData<*>>) {
    operator fun <T> String.invoke(block: () -> ArgumentType<T>) {
        arguments.add(SingleArgumentData(block(), this))
    }
}