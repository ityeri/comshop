package com.github.ityeri.comshop

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.papermc.paper.command.brigadier.CommandSourceStack

class CommandDsl(val name: String) {
    val arguments: MutableList<ArgumentData<*>> = mutableListOf()
    val subCommands: MutableList<CommandDsl> = mutableListOf()
    var op = false
    private var executor: ContextWrapper<CommandSourceStack>.(CommandSourceStack) -> Int =
        { source -> 1 }

    fun then(name: String, block: CommandDsl.() -> Unit) {
        val commandDsl = CommandDsl(name)
        subCommands.add(commandDsl)
        commandDsl.apply(block)
    }

    fun arguments(block: ArgumentRegistrar.() -> Unit) {
        ArgumentRegistrar(arguments).apply(block)
    }

    fun executes(block: ContextWrapper<CommandSourceStack>.(CommandSourceStack) -> Int) {
        executor = block
    }

    fun build(): LiteralArgumentBuilder<CommandSourceStack> {
        val rootBuilder = literal<CommandSourceStack>(name)

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