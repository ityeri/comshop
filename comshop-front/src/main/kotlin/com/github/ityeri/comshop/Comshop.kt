package com.github.ityeri.comshop

import com.github.ityeri.comshop.api.argument.ComshopCustomArgumentType
import com.github.ityeri.comshop.api.argument.StringType
import com.github.ityeri.comshop.api.exception.ComshopCommandException
import com.github.ityeri.comshop.builder.CommandBuilder
import com.github.ityeri.comshop.builder.CustomArgumentTypeBuilder
import org.bukkit.plugin.java.JavaPlugin


fun command(name: String, block: CommandBuilder.() -> Unit): CommandBuilder {
    return CommandBuilder(name).apply(block)
}

fun initComshop(plugin: JavaPlugin) {
    CommandRegistrar.init(plugin)
}

fun register(name: String, block: CommandBuilder.() -> Unit) {
    CommandRegistrar.register(
        CommandBuilder(name).apply(block).build()
    )
}
fun register(builder: CommandBuilder) {
    CommandRegistrar.register(builder.build())
}

fun <T : Any, N : Any> customArgument(
    block: CustomArgumentTypeBuilder<T, N>.() -> Unit
): ComshopCustomArgumentType<T, N> =
    CustomArgumentTypeBuilder<T, N>().apply(block).build()

fun selectArgument(
    vararg element: String,
    stringType: StringType = StringType.WORD,
    ignoreCase: Boolean = true,
    displayOnlyMatches: Boolean = true,
    whenException: (String) -> String = { userInput ->
        throw ComshopCommandException("Value \"${userInput}\" does not exist.")
    }
) = customArgument {
    val elements = element.toList()

    native(string(stringType))

    parses { nativeValue, source ->
        val findValue = (if (ignoreCase) elements.map { it.lowercase() } else elements)
            .find {
                (if (ignoreCase) nativeValue.lowercase() else nativeValue) == it
            }

        findValue ?: whenException.invoke(nativeValue)
    }

    suggests {
        val displayingElements = if (stringType == StringType.QUOTED) {
            elements.map { "\"" + it + "\"" }
        } else {
            elements
        }

        if (displayOnlyMatches) {
            displayingElements.filter {
                it.removePrefix("\"").startsWith(context.remining, ignoreCase = true)
            }
        } else {
            displayingElements
        }.forEach {
            suggest(it)
        }
    }
}
