package com.github.ityeri.comshop.impl.converter.argument.nativ

import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.mojang.brigadier.arguments.ArgumentType


abstract class NativeArgumentTypeConverter<T : Any, A : NativeArgumentType<T>>(
    val converter: (original: A) -> ArgumentType<T>
)
