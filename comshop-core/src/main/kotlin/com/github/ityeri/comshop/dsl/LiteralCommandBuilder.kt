package com.github.ityeri.comshop.dsl

import com.github.ityeri.comshop.ArgumentDSL
import com.github.ityeri.comshop.ContextWrapper
import com.github.ityeri.comshop.argument.ArgumentChainNode
import com.github.ityeri.comshop.argument.ArgumentNode
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack

abstract class LiteralCommandBuilder() : CommandBuilder {
    abstract val name: String
    protected val rootArgumentNode: ArgumentChainNode = ArgumentChainNode()
    protected val subCommands: MutableList<CommandBuilder> = mutableListOf()
    protected var executor: ContextWrapper<CommandSourceStack>.(CommandSourceStack) -> Int =
        { source -> 1 }
    private var permissionChecker: (source: CommandSourceStack) -> Boolean = { true }

    fun requires(block: (source: CommandSourceStack) -> Boolean) {
        permissionChecker = block
    }

    fun arguments(block: ArgumentDSL.() -> Unit) {
        val argumentRegistrar = ArgumentDSL(rootArgumentNode)
        argumentRegistrar.apply(block)
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
            for (builder in buildArgumentNodes<CommandSourceStack>(TODO(), executeBlock)) {
                rootBuilder.then(builder)
            }
        }

        return rootBuilder
    }
}


fun <S> buildArgumentNodes(
    nodes: List<ArgumentNode>, executeBlock: (CommandContext<S>) -> Int
): List<ArgumentBuilder<S, *>> {
    if (nodes.size == 1) {
        return nodes[0].connectExecuteBlock(executeBlock)
    }
    else {
        return nodes[0].connectArgumentBuilder(
            buildArgumentNodes(nodes.subList(1, nodes.size - 1), executeBlock)
        )
    }
}