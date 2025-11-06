package com.github.ityeri.comshop

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import kotlin.reflect.KClass

class ArgumentData<T>(val argumentType: ArgumentType<T>, val name: String) {
    fun <S> createBuilder(): RequiredArgumentBuilder<S, T> {
        return argument<S, T>(name, argumentType)
    }
}