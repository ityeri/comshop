package com.github.ityeri.comshop.impl.converter.argument

import com.github.ityeri.comshop.api.argument.ComshopArgumentType
import com.github.ityeri.comshop.api.argument.ComshopCustomArgumentType
import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.github.ityeri.comshop.impl.converter.argument.nativ.nativeToBrigadierArgumentType
import com.mojang.brigadier.arguments.ArgumentType


fun <T : Any> ComshopArgumentType<T>.toBrigadierArgumentType(): ArgumentType<T> =
    when (this) {
        is NativeArgumentType -> {
            this.nativeToBrigadierArgumentType()
        }
        is ComshopCustomArgumentType<T, *> -> {
            this.toBrigadierArgumentType()
        }
        else -> {
            throw IllegalArgumentException("Unexpected ComshopArgumentTypes's subtype was passed")
        }
    }
