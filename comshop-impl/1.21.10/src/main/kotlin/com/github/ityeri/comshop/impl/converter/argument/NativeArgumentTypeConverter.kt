package com.github.ityeri.comshop.impl.converter.argument

import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.github.ityeri.comshop.api.argument.StringType
import com.mojang.brigadier.arguments.*


class NativeArgumentTypeConverter<T : Any>(
    val converter: (original: NativeArgumentType<T>) -> ArgumentType<T>
)
