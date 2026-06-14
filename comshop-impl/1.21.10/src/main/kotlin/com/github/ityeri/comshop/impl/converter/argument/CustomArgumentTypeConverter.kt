package com.github.ityeri.comshop.impl.converter.argument

import com.github.ityeri.comshop.api.argument.ComshopCustomArgumentType
import com.github.ityeri.comshop.api.exception.ComshopCommandException
import com.github.ityeri.comshop.impl.converter.toBrigadierSuggestionProvider
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import java.util.concurrent.CompletableFuture


fun <T : Any, N : Any> ComshopCustomArgumentType<T, N>.toBrigadierArgumentType(): ArgumentType<T> =
    object : CustomArgumentType<T, N> {

        val suggestionProvider = toBrigadierSuggestionProvider(this@toBrigadierArgumentType::suggest)

        override fun parse(reader: StringReader): T {
            throw NotImplementedError(
                "The ComshopCustomArgumentType does not support parsing without a source value"
            )
        }

        override fun <S : Any> parse(reader: StringReader, source: S): T =
            when (source) {
                is CommandSourceStack -> {
                    val convertedValue = nativeType.parse(reader, source)

                    try {
                        this@toBrigadierArgumentType.parse(convertedValue, source)
                    }
                    catch (e: ComshopCommandException) {
                        throw SimpleCommandExceptionType({ e.message }).create()
                    }
                    catch (e: CommandSyntaxException) {
                        throw IllegalStateException(
                            "Method ComshopCustomArgumentType.parse cannot throw CommandSyntaxException, "
                                    + "which belongs to brigadier. Use ComshopCommandException instead",
                            e
                        )
                    }
                }
                else -> {
                    throw NotImplementedError(
                        "The ComshopCustomArgumentType only support a CommandSourceStack as source value"
                    )
                }
            }

        override fun <S : Any> listSuggestions(
            context: CommandContext<S>,
            builder: SuggestionsBuilder
        ): CompletableFuture<Suggestions> {
            if (context.source !is CommandSourceStack) {
                throw NotImplementedError(
                    "The ComshopCustomArgumentType only support a CommandSourceStack as source value"
                )
            } else {
                @Suppress("UNCHECKED_CAST")
                return suggestionProvider.getSuggestions(context as CommandContext<CommandSourceStack>, builder)
            }
        }

        override fun getNativeType(): ArgumentType<N> =
            this@toBrigadierArgumentType.nativeArgumentType.toBrigadierArgumentType()
    }
