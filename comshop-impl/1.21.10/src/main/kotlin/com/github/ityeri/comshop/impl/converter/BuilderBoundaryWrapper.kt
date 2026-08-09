package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.api.node.Node
import com.github.ityeri.comshop.impl.BuilderBoundary
import com.github.ityeri.comshop.impl.CommandFragment
import com.github.ityeri.comshop.impl.optic.nodePTraversal


fun toBuilderBoundaryWrappedNode(node: Node<CommandFragment>): Node<BuilderBoundary> =
    nodePTraversal<CommandFragment, BuilderBoundary>()
        .modify(
            node,
            { commandFragment ->
                toBuilderBoundary(commandFragment)
            }
        )

fun toBuilderBoundary(commandFragment: CommandFragment): BuilderBoundary =
    when (commandFragment) {
        is CommandFragment.NodeBuilderFragment -> {
            BuilderBoundary(listOf(commandFragment.builder), listOf(commandFragment.builder))
        }
        is CommandFragment.ExecutionFragment -> {
            BuilderBoundary(
                entries = emptyList(),
                exits = emptyList(),
                pendingCommand = commandFragment.command
            )
        }
    }
