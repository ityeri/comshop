package com.github.ityeri.comshop.impl.converter.argument.nativ

import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.mojang.brigadier.arguments.ArgumentType


fun <T : Any> NativeArgumentType<T>.nativeToBrigadierArgumentType(): ArgumentType<T> =
    @Suppress("UNCHECKED_CAST")
    (when {
        this is NativeArgumentType.BooleanArgumentType -> {
            BooleanArgumentTypeConverter()
        }
        this is NativeArgumentType.IntArgumentType -> {
            IntArgumentTypeConverter()
        }
        this is NativeArgumentType.FloatArgumentType -> {
            FloatArgumentTypeConverter()
        }
        this is NativeArgumentType.DoubleArgumentType -> {
            DoubleArgumentTypeConverter()
        }
        this is NativeArgumentType.LongArgumentType -> {
            LongArgumentTypeConverter()
        }
        this is NativeArgumentType.StringArgumentType -> {
            StringArgumentTypeConverter()
        }
        else -> {
            TODO()
        }
    } as NativeArgumentTypeConverter<T, NativeArgumentType<T>>)
        .converter(this)
