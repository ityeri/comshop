package com.github.ityeri.comshop.builder

import com.github.ityeri.comshop.ComshopDsl
import com.github.ityeri.comshop.api.CommandWritingContext
import com.github.ityeri.comshop.api.argument.ComshopCustomArgumentType
import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.github.ityeri.comshop.api.argument.SuggestionElement
import io.papermc.paper.command.brigadier.CommandSourceStack


@ComshopDsl
class CustomArgumentTypeBuilder<T : Any, N : Any> : NativeArgumentTypeFactory() {
    protected var nativeArgumentType: NativeArgumentType<N>? = null
    protected var parser: ((nativeValue: N, source: CommandSourceStack) -> T)? = null
    protected var suggestionProvider: SuggestionProvider? = null

    fun native(nativeArgumentType: NativeArgumentType<N>) {
        this.nativeArgumentType = nativeArgumentType
    }
    fun parses(block: (nativeValue: N, source: CommandSourceStack) -> T) {
        parser = block
    }
    fun suggests(block: SuggestionProvider) {
        suggestionProvider = block
    }

    fun build(): ComshopCustomArgumentType<T, N> =
        object : ComshopCustomArgumentType<T, N> {
            override val nativeArgumentType: NativeArgumentType<N> =
                this@CustomArgumentTypeBuilder.nativeArgumentType ?:
                throw IllegalStateException(
                    "You did not specify the nativeArgumentType." +
                            "Please add the native function in argument definition"
                )

            override fun parse(nativeValue: N, source: CommandSourceStack): T =
                parser?.invoke(nativeValue, source) ?:
                throw IllegalStateException(
                    "You did not specify the parses block. Please add the parses block"
                )

            override fun suggest(
                writingContext: CommandWritingContext,
                source: CommandSourceStack
            ): Iterable<SuggestionElement> =
                SuggestionBuilder(source, writingContext).apply(
                    suggestionProvider ?:
                    throw IllegalStateException(
                        "You did not specify the suggests block. Please add the suggests block"
                    )
                ).build()
        }
}
