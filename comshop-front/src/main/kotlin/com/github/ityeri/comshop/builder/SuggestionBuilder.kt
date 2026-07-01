package com.github.ityeri.comshop.builder

import com.github.ityeri.comshop.context.SourceContext
import com.github.ityeri.comshop.ComshopDsl
import com.github.ityeri.comshop.api.CommandWritingContext
import com.github.ityeri.comshop.api.argument.SuggestionElement
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component


@ComshopDsl
class SuggestionBuilder(
    source: CommandSourceStack,
    val context: CommandWritingContext,
    protected val suggestions: MutableList<SuggestionElement> = mutableListOf()
) : SourceContext(source) {
    fun suggest(element: SuggestionElement) {
        suggestions.add(element)
    }
    fun suggest(text: String, tooltipMessage: Component? = null) {
        suggestions.add(SuggestionElement(text, tooltipMessage))
    }

    fun build(): Iterable<SuggestionElement> = suggestions
}
