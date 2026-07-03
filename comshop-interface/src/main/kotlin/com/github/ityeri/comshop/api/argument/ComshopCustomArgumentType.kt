package com.github.ityeri.comshop.api.argument

import com.github.ityeri.comshop.api.CommandWritingContext
import io.papermc.paper.command.brigadier.CommandSourceStack


interface ComshopCustomArgumentType<T : Any, N : Any> : ComshopArgumentType<T> {
    val nativeArgumentType: NativeArgumentType<N>
    fun parse(nativeValue: N, source: CommandSourceStack): T
    fun suggest(writingContext: CommandWritingContext, source: CommandSourceStack): Iterable<SuggestionElement>
}
