package com.github.ityeri.comshop

import com.github.ityeri.comshop.api.argument.ComshopCustomArgumentType
import com.github.ityeri.comshop.api.argument.StringType
import com.github.ityeri.comshop.api.exception.ComshopCommandException
import com.github.ityeri.comshop.builder.CustomArgumentTypeBuilder
import com.github.ityeri.comshop.builder.SuggestionBuilder
import kotlin.reflect.KClass


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
            displayingElements.filter {
                it.trim('"').startsWith(context.remaining.trim('"'), ignoreCase = ignoreCase)
            }
        } else {
            displayingElements
        }.forEach {
            suggest(it)
        }
    }
}

fun selectArgument(
    elementProvider: () -> Iterable<String>,
    stringType: StringType = StringType.WORD,
    ignoreCase: Boolean = true,
    suggestOnlyMatches: Boolean = true,
    whenException: (String) -> String = { userInput ->
        throw ComshopCommandException("Value \"${userInput}\" does not exist.")
    }
) = customArgument {
    native(string(stringType))

    parses { nativeValue, source ->
        val elements = elementProvider.invoke()
        val foundValue = elements.find {
            nativeValue.equals(it, ignoreCase = ignoreCase)
        }

        foundValue ?: whenException.invoke(nativeValue)
    }

    simpleSuggests(
        stringType,
        ignoreCase,
        suggestOnlyMatches
    ) { elementProvider.invoke() }
}

fun selectArgument(
    elements: Iterable<String>,
    stringType: StringType = StringType.WORD,
    ignoreCase: Boolean = true,
    suggestOnlyMatches: Boolean = true,
    whenException: (String) -> String = { userInput ->
        throw ComshopCommandException("Value \"${userInput}\" does not exist.")
    }
) = selectArgument(
    elementProvider = { elements },
    stringType = stringType,
    ignoreCase = ignoreCase,
    suggestOnlyMatches = suggestOnlyMatches,
    whenException = whenException
)

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
    clazz: KClass<E>,
    stringType: StringType = StringType.WORD,
    ignoreCase: Boolean = true,
    lowercase: Boolean = true,
    suggestOnlyMatches: Boolean = true,
    whenException: (String) -> Enum<E> = { userInput ->
        throw ComshopCommandException("Option \"${userInput}\" does not exist.")
    }
) = customArgument {
    val elements = clazz.java.enumConstants.toList()

    native(string(stringType))

    parses { nativeValue, source ->
        val foundValue = elements.find {
            nativeValue.equals(
                if (lowercase) it.name.lowercase()
                else it.name,
                ignoreCase = ignoreCase
            )
        }

        foundValue ?: whenException.invoke(nativeValue)
    }

    simpleSuggests(
        stringType,
        ignoreCase,
        suggestOnlyMatches
    ) {
        if (lowercase) {
            elements.map { it.name.lowercase() }
        } else {
            elements.map { it.name }
        }
    }
}
