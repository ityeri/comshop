package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.api.node.ComshopCommandNode
import com.github.ityeri.comshop.api.node.Node
import com.github.ityeri.comshop.impl.BrigadierBuilderTail
import com.github.ityeri.comshop.impl.BrigadierNodeBuilder
import com.github.ityeri.comshop.impl.TaillessBrigadierNode
import com.github.ityeri.comshop.impl.converter.argument.toBrigadierArgumentType


fun Node<ComshopCommandNode>.createTailWith(tail: BrigadierBuilderTail): BrigadierBuilderTail =
    when (this) {
        is Node.SingleNode ->
            when (val commandNode = value) {
                is ComshopCommandNode.LiteralCommandNode ->
                    BrigadierBuilderTail(
                        BrigadierNodeBuilder(
                            commandNode.toTaillessBrigadierNode(),
                            children = tail.entries,
                            command = tail.command
                        )
                    )

                is ComshopCommandNode.ArgumentNode<*> ->
                    BrigadierBuilderTail(
                        BrigadierNodeBuilder(
                            commandNode.toTaillessBrigadierNode(),
                            children = tail.entries,
                            command = tail.command
                        )
                    )

                is ComshopCommandNode.ExecutionNode ->
                    BrigadierBuilderTail(command = commandNode.commandBlock.toBrigadierCommand())
            }

        is Node.UnionNode -> {
            val tails = nodes.map {
                it.createTailWith(tail)
            }
            val foundCommand = tails.singleOrNull { it.command != null }?.command

            BrigadierBuilderTail(
                entries = tails.flatMap { it.entries },
                command = foundCommand
            )
        }

        is Node.ChainNode -> {
            nodes.foldRight(tail) { commandNode, tail ->
                commandNode.createTailWith(tail)
            }
        }
    }


fun ComshopCommandNode.LiteralCommandNode.toTaillessBrigadierNode(): TaillessBrigadierNode.Literal =
    TaillessBrigadierNode.Literal(
        literal = name,
        requiresChecker = requiresChecker
    )

fun ComshopCommandNode.ArgumentNode<*>.toTaillessBrigadierNode(): TaillessBrigadierNode =
    TaillessBrigadierNode.Argument(
        name = name,
        argumentType = argumentType.toBrigadierArgumentType(),
        customSuggestions = customSuggestionProvider?.toBrigadierProvider(),
        requiresChecker = requiresChecker
    )
