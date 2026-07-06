package com.github.ityeri.comshop

import com.github.ityeri.comshop.api.argument.ComshopCustomArgumentType
import com.github.ityeri.comshop.api.argument.StringType
import com.github.ityeri.comshop.api.exception.ComshopCommandException
import com.github.ityeri.comshop.builder.CustomArgumentTypeBuilder
import com.github.ityeri.comshop.builder.SuggestionBuilder


fun <T : Any, N : Any> customArgument(
    block: CustomArgumentTypeBuilder<T, N>.() -> Unit
): ComshopCustomArgumentType<T, N> =
    CustomArgumentTypeBuilder<T, N>().apply(block).build()

fun <T : Any, N : Any> CustomArgumentTypeBuilder<T, N>.simpleSuggests(
    stringType: StringType = StringType.WORD,
    ignoreCase: Boolean = false,
    displayOnlyMatches: Boolean = true,
    block: SuggestionBuilder.() -> Iterable<String>
) {
    suggests {
        val elements = this.run(block)

        val displayingElements = if (stringType == StringType.QUOTED) {
            elements.map { "\"" + it + "\"" }
        } else {
            elements
        }

        if (displayOnlyMatches) {
            elements.filter {
                it.startsWith(context.remining, ignoreCase = ignoreCase)
            }
        } else {
            displayingElements
        }.forEach {
            suggest(it)
        }
    }
}

fun selectArgument(
    elements: Iterable<String>,
    stringType: StringType = StringType.WORD,
    ignoreCase: Boolean = true,
    suggestOnlyMatches: Boolean = true,
    whenException: (String) -> String = { userInput ->
        throw ComshopCommandException("Value \"${userInput}\" does not exist.")
    }
) = customArgument {
    native(string(stringType))

    parses { nativeValue, source ->
        val foundValue = elements.find {
            nativeValue.equals(it, ignoreCase = ignoreCase)
        }

        foundValue ?: whenException.invoke(nativeValue)
    }

    simpleSuggests(
        stringType,
        ignoreCase,
        suggestOnlyMatches
    ) { elements }
}

fun selectArgument(
    vararg element: String,
    stringType: StringType = StringType.WORD,
    ignoreCase: Boolean = true,
    suggestOnlyMatches: Boolean = true,
    whenException: (String) -> String = { userInput ->
        throw ComshopCommandException("Value \"${userInput}\" does not exist.")
    }
) = selectArgument(
    elements = element.toList(),
    stringType = stringType,
    ignoreCase = ignoreCase,
    suggestOnlyMatches = suggestOnlyMatches,
    whenException = whenException
)

fun <E : Enum<E>> enumArgument(
    clazz: Class<Enum<E>>,
    stringType: StringType = StringType.WORD,
    ignoreCase: Boolean,
    suggestLower: Boolean,
    suggestOnlyMatches: Boolean = true,
    whenException: (String) -> Enum<E> = { userInput ->
        throw ComshopCommandException("Value \"${userInput}\" does not exist.")
    }
) = customArgument {
    val elements = clazz.enumConstants.toList()

    native(string(stringType))

    parses { nativeValue, source ->
        val foundValue = elements.find {
            nativeValue.equals(it.name, ignoreCase = ignoreCase)
        }

        foundValue ?: whenException.invoke(nativeValue)
    }

    simpleSuggests(
        stringType,
        ignoreCase,
        suggestOnlyMatches
    ) {
        if (suggestLower) {
            elements.map { it.name.lowercase() }
        } else {
            elements.map { it.name }
        }
    }
}
