package com.github.ityeri.comshop

import com.mojang.brigadier.context.CommandContext
import kotlin.reflect.KClass

class ContextWrapper<S>(val context: CommandContext<S>) {
    fun <T : Any> getArg(name: String, clazz: KClass<T>): T {
        return context.getArgument<T>(name, clazz.java)
    }
}