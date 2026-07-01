package com.github.ityeri.comshop.builder

import com.github.ityeri.comshop.builder.ArgumentStructureBuilder.ChainStructureBuilder
import com.github.ityeri.comshop.context.CommandExecutionContext
import com.github.ityeri.comshop.context.SourceContext
import com.github.ityeri.comshop.ComshopDsl
import com.github.ityeri.comshop.api.CommandResult
import com.github.ityeri.comshop.api.node.ComshopCommandNode
import com.github.ityeri.comshop.api.node.Node


@ComshopDsl
class CommandBuilder(val name: String) {
    protected val argumentStructureBuilder = ArgumentStructureBuilder.UnionStructureBuilder()
    protected var requiresChecker: SourceContext.() -> Boolean = { true }
    protected var executor: CommandExecutionContext.() -> CommandResult = { CommandResult.FAILED }
    protected var subCommandBuilders: MutableList<CommandBuilder> = mutableListOf()

    fun requires(requiresChecker: SourceContext.() -> Boolean) {
        this.requiresChecker = requiresChecker
    }

    fun arguments(builder: CommandBuilder) {
        subCommandBuilders.add(builder)
    }
    fun arguments(block: ChainStructureBuilder.() -> Unit) {
        argumentStructureBuilder.arguments(block)
    }

    fun executes(block: CommandExecutionContext.() -> CommandResult) {
        executor = block
    }

    fun then(builder: CommandBuilder) {
        subCommandBuilders.add(builder)
    }
    fun then(name: String, block: CommandBuilder.() -> Unit) {
        subCommandBuilders.add(
            CommandBuilder(name).apply(block)
        )
    }

    fun build(): Node.ChainNode<ComshopCommandNode> =
        Node.ChainNode(
            Node.SingleNode(
                ComshopCommandNode.LiteralCommandNode(
                    name = name,
                    requiresChecker = { SourceContext(it).run(requiresChecker) }
                ),
            ),
            Node.UnionNode(
                *subCommandBuilders.map { it.build() }.toTypedArray(),
                Node.ChainNode(
                    argumentStructureBuilder.build(),
                    Node.SingleNode(
                        ComshopCommandNode.ExecutionNode(
                            commandBlock = { CommandExecutionContext(it).run(executor) }
                        )
                    )
                )
            )
        )
}
