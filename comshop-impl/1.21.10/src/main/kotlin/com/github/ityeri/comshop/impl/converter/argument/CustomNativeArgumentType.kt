package com.github.ityeri.comshop.impl.converter.argument

import com.mojang.brigadier.arguments.ArgumentType
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.argument.CustomArgumentType


class CustomNativeArgumentType<T : Any, N : Any>(
    val brigadierNativeType: ArgumentType<N>,
    val converter: (nativeValue: N, source: CommandSourceStack) -> T
) : CustomArgumentType.Converted<T, N> {
    override fun convert(nativeType: N): T {
        throw IllegalStateException(
            "Comshop does not supports a argument parsing without source value input. "
                    + "Are you using a unsupported paper api version?"
        )
    }
    override fun <S : Any> convert(nativeType: N, source: S): T =
        when (source) {
            is CommandSourceStack -> {
                converter(nativeType, source)
            }
            else -> {
                throw IllegalArgumentException(
                    "Comshop only supports CommandSourceStack as a source value type"
                )
            }
        }

    override fun getNativeType(): ArgumentType<N> = brigadierNativeType
}
