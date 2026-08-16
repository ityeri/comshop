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
                    BrigadierBuilderTail.BuilderTail(
                        BrigadierNodeBuilder(
                            commandNode.toTaillessBrigadierNode(),
                            children = if (tail is BrigadierBuilderTail.BuilderTail) tail.entries else emptyList(),
                            command = if (tail is BrigadierBuilderTail.CommandTail) tail.command else null
                        )
                    )
                is ComshopCommandNode.ArgumentNode<*> ->
                    BrigadierBuilderTail.BuilderTail(
                        BrigadierNodeBuilder(
                            commandNode.toTaillessBrigadierNode(),
                            children = if (tail is BrigadierBuilderTail.BuilderTail) tail.entries else emptyList(),
                            command = if (tail is BrigadierBuilderTail.CommandTail) tail.command else null
                        )
                    )
                is ComshopCommandNode.ExecutionNode ->
                    BrigadierBuilderTail.CommandTail(commandNode.commandBlock.toBrigadierCommand())
            }
        is Node.UnionNode -> {
            val tails = nodes.map {
                it.createTailWith(tail)
            }
            val foundCommandBoundary = tails.singleOrNull { it is BrigadierBuilderTail.CommandTail }
                    as BrigadierBuilderTail.CommandTail?

            if (foundCommandBoundary != null) {
                BrigadierBuilderTail.CommandTail(foundCommandBoundary.command)
            } else {
                BrigadierBuilderTail.BuilderTail(
                    tails.flatMap { (it as BrigadierBuilderTail.BuilderTail).entries }
                )
            }
        }
        is Node.ChainNode -> {
            nodes.foldRight(nodes.first().createTailWith(tail)) { commandNode, tail ->
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
