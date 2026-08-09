package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.api.ComshopContext
import com.github.ityeri.comshop.api.exception.ComshopCommandException
import com.github.ityeri.comshop.api.node.ComshopCommandNode
import com.github.ityeri.comshop.impl.BrigadierNodeBuilder
import com.github.ityeri.comshop.impl.BrigadierFragment
import com.github.ityeri.comshop.impl.converter.argument.toBrigadierArgumentType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType


fun ComshopCommandNode.toBrigadierFragment(): BrigadierFragment =
    when (this) {
        is ComshopCommandNode.LiteralCommandNode -> {
            BrigadierFragment.NodeBuilderFragment(
                BrigadierNodeBuilder.LiteralNodeBuilder(
                    name, requiresChecker
                )
            )
        }
        is ComshopCommandNode.ArgumentNode<*> -> {
            BrigadierFragment.NodeBuilderFragment(
                BrigadierNodeBuilder.ArgumentNodeBuilder(
                    name,
                    argumentType = argumentType.toBrigadierArgumentType(),
                    requiresChecker =  requiresChecker,
                    suggestionProvider = customSuggestionProvider?.let {
                        toBrigadierSuggestionProvider(it)
                    }
                )
            )
        }
        is ComshopCommandNode.ExecutionNode -> {
            BrigadierFragment.ExecutionFragment { context ->
                val comshopContext = object : ComshopContext {
                    override val source = context.source

                    override fun <T> getArgument(name: String, clazz: Class<T>): T {
                        return context.getArgument(name, clazz)
                    }
                }

                try {
                    commandBlock(comshopContext).toInt()
                }
                catch (e: ComshopCommandException) {
                    throw SimpleCommandExceptionType({ e.message }).create()
                }
                catch (e: CommandSyntaxException) {
                    throw IllegalStateException(
                        "Command execution block defined in comshop should not throw CommandSyntaxException, "
                                + "which belongs to brigadier. Use ComshopCommandException instead",
                        e
                    )
                }
            }
        }
    }
