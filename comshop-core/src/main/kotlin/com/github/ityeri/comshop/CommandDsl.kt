package com.github.ityeri.comshop

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.papermc.paper.command.brigadier.CommandSourceStack

class CommandDsl(val name: String) : CommandBuilder {
    private val arguments: MutableList<ArgumentData<*>> = mutableListOf()
    private val subCommands: MutableList<CommandBuilder> = mutableListOf()
    private var executor: ContextWrapper<CommandSourceStack>.(CommandSourceStack) -> Int =
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

    fun then(block: () -> CommandBuilder) {
        subCommands.add(block())
    }

    fun then(name: String, block: CommandDsl.() -> Unit) {
        val commandDsl = CommandDsl(name)
        subCommands.add(commandDsl)
        commandDsl.apply(block)
    }

    override fun build(): LiteralArgumentBuilder<CommandSourceStack> {
        val rootBuilder = literal<CommandSourceStack>(name)

        rootBuilder.requires { source -> permissionChecker(source) }

        for (subCommand in subCommands) {
            rootBuilder.then(subCommand.build())
        }

        // TODO 아무리 봐도 이 코드는 좀 아닌것 같음 아니다 꽤 괜찮을지도
        if (arguments.isEmpty()) {
            rootBuilder.executes { context ->
                ContextWrapper(context).run { executor(context.source) }
            }

        } else {
            var previousBuilder: ArgumentBuilder<CommandSourceStack, *>? = null
            var currentBuilder: ArgumentBuilder<CommandSourceStack, *>? = null

            for (argumentData in arguments.reversed()) {
                currentBuilder = argumentData.createBuilder<CommandSourceStack>()

                if (previousBuilder == null) {
                    previousBuilder = currentBuilder.executes { context ->
                        ContextWrapper(context).run { executor(context.source) }
                    }
                } else {
                    previousBuilder = currentBuilder.then(previousBuilder)
                }
            }

            rootBuilder.then(currentBuilder!!)
        }

        return rootBuilder
    }
}