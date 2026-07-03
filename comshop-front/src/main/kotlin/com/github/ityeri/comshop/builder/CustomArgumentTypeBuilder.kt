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
                this@CustomArgumentTypeBuilder.nativeArgumentType!!

            override fun parse(nativeValue: N, source: CommandSourceStack): T =
                parser!!.invoke(nativeValue, source)

            override fun suggest(
                writingContext: CommandWritingContext,
                source: CommandSourceStack
            ): Iterable<SuggestionElement> =
                SuggestionBuilder(source, writingContext).apply(suggestionProvider!!).build()
        }
}
