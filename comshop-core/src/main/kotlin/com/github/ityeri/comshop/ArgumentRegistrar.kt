package com.github.ityeri.comshop

import com.mojang.brigadier.arguments.ArgumentType

class ArgumentRegistrar(val arguments: MutableList<ArgumentData<*>>) {
    operator fun <T> String.invoke(block: () -> ArgumentType<T>) {
        arguments.add(ArgumentData(block(), this))
    }
}