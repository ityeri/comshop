package com.github.ityeri.comshop.api.node

import com.github.ityeri.comshop.api.CommandResult
import com.github.ityeri.comshop.api.CommandWritingContext
import com.github.ityeri.comshop.api.ComshopContext
import com.github.ityeri.comshop.api.argument.ComshopArgumentType
import com.github.ityeri.comshop.api.argument.SuggestionElement
import io.papermc.paper.command.brigadier.CommandSourceStack


typealias CustomSuggestionProvider = (CommandWritingContext, CommandSourceStack) -> Iterable<SuggestionElement>

sealed class ComshopCommandNode {
    class LiteralCommandNode(
        val name: String,
        val requiresChecker: (CommandSourceStack) -> Boolean = { true },
    ) : ComshopCommandNode()

    class ArgumentNode<T : Any>(
        val name: String,
        val argumentType: ComshopArgumentType<T>,
        val requiresChecker: (CommandSourceStack) -> Boolean = { _ -> true },
        val customSuggestionProvider: CustomSuggestionProvider? = null
    ) : ComshopCommandNode()

    class ExecutionNode(
        val commandBlock: (ComshopContext) -> CommandResult
    ) : ComshopCommandNode()
}
