package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.api.node.Node
import com.github.ityeri.comshop.impl.BuilderBoundary


fun connectBuilderBoundaries(node: Node<BuilderBoundary>): BuilderBoundary =
    when (node) {
        is Node.SingleNode -> {
            node.value
        }
        is Node.UnionNode -> {
            val boundaries = node.nodes.map { connectBuilderBoundaries(it) }

            BuilderBoundary(
                boundaries.flatMap { it.entries },
                boundaries.flatMap { it.exits },
                pendingCommand = boundaries.singleOrNull { it.pendingCommand != null }?.pendingCommand
            )
        }
        is Node.ChainNode -> {
            connectBoundaryChain(
                node.nodes.map { connectBuilderBoundaries(it) }
            )
        }
    }

fun connectBoundaryChain(
    boundaries: List<BuilderBoundary>
): BuilderBoundary =
    if (boundaries.isEmpty()) {
        BuilderBoundary(listOf(), listOf())
    } else if (boundaries.size == 1) {
        boundaries.first()
    } else {
        val headBoundary = boundaries.first()
        val bodyBoundary = connectBoundaryChain(boundaries.subList(1, boundaries.size))

        headBoundary.connectNext(bodyBoundary)
    }
