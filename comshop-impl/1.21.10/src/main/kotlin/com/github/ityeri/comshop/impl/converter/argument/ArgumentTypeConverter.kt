package com.github.ityeri.comshop.impl.converter.argument

import com.github.ityeri.comshop.api.argument.ComshopArgumentType
import com.github.ityeri.comshop.api.argument.ComshopCustomArgumentType
import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.mojang.brigadier.arguments.ArgumentType


fun <T : Any> ComshopArgumentType<T>.toBrigadierArgumentType(): ArgumentType<T> =
    when (this) {
        is NativeArgumentType -> {
            this.nativeTypeToBrigadierArgumentType()
        }
        is ComshopCustomArgumentType<T, *> -> {
            this.customTypeToBrigadierArgumentType()
        }
        else -> {
            throw IllegalArgumentException("Unexpected ComshopArgumentTypes's subtype was passed")
        }
    }
