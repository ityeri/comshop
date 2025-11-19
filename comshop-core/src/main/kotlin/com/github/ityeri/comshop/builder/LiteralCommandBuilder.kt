package com.github.ityeri.comshop.builder

import com.github.ityeri.comshop.ArgumentData
import com.github.ityeri.comshop.ArgumentRegistrar
import com.github.ityeri.comshop.ContextWrapper
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack

abstract class LiteralCommandBuilder() : CommandBuilder {
    abstract val name: String
    protected val arguments: MutableList<ArgumentData<*>> = mutableListOf()
    protected val subCommands: MutableList<CommandBuilder> = mutableListOf()
    protected var executor: ContextWrapper<CommandSourceStack>.(CommandSourceStack) -> Int =
        { source -> 1 }
    private var permissionChecker: (source: CommandSourceStack) -> Boolean = { true }

    fun requires(block: (source: CommandSourceStack) -> Boolean) {
        permissionChecker = block
    }

    fun arguments(block: ArgumentRegistrar.() -> Unit) {
        ArgumentRegistrar(arguments).apply(block)
    }

    fun executes(block: ContextWrapper<CommandSourceStack>.(source: CommandSourceStack) -> Int) {
        executor = block
    }

    fun <T: CommandBuilder> then(builder: T, block: T.() -> Unit = {}) {
        builder.apply(block)
        subCommands.add(builder)
    }


    override fun createBuilder(): LiteralArgumentBuilder<CommandSourceStack> {
        val rootBuilder = literal<CommandSourceStack>(name)

        rootBuilder.requires { source -> permissionChecker(source) }

        for (subCommand in subCommands) {
            rootBuilder.then(subCommand.createBuilder())
        }

        val executeBlock: (CommandContext<CommandSourceStack>) -> Int = { context ->
            ContextWrapper(context).run { executor(context.source) }
        }

        // TODO 아무리 봐도 이 코드는 좀 아닌것 같음 아니다 꽤 괜찮을지도
        if (arguments.isEmpty()) {
            rootBuilder.executes(executeBlock)

        } else {
            val argumentBuilders: List<ArgumentBuilder<CommandSourceStack, *>> =
                arguments.map { it.createBuilder() }
            rootBuilder.then(
                connectArgumentBuilders(argumentBuilders, executeBlock)
            )
        }

        return rootBuilder
    }
}

fun <S> connectArgumentBuilders(
    builders: List<ArgumentBuilder<S, *>>, executeBlock: (CommandContext<S>) -> Int
): ArgumentBuilder<S, *> {
    if (builders.size == 1) {
        return builders[0].executes(executeBlock)
    }

    return builders[0].then(
        connectArgumentBuilders(
            builders.subList(1, builders.size), executeBlock
        )
    )
}