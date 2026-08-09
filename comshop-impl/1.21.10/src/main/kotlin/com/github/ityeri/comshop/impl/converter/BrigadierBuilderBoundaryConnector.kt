package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.api.node.Node
import com.github.ityeri.comshop.impl.BrigadierBuilderBoundary


fun Node<BrigadierBuilderBoundary>.connectBuilderBoundaries(): BrigadierBuilderBoundary =
    when (this) {
        is Node.SingleNode -> {
            value
        }
        is Node.UnionNode -> {
            val boundaries = nodes.map { it.connectBuilderBoundaries() }

            BrigadierBuilderBoundary(
                boundaries.flatMap { it.entries },
                boundaries.flatMap { it.exits },
                pendingCommand = boundaries.singleOrNull { it.pendingCommand != null }?.pendingCommand
            )
        }
        is Node.ChainNode -> {
            connectBoundaryChain(
                nodes.map { it.connectBuilderBoundaries() }
            )
        }
    }

fun connectBoundaryChain(
    boundaries: List<BrigadierBuilderBoundary>
): BrigadierBuilderBoundary =
    if (boundaries.isEmpty()) {
        BrigadierBuilderBoundary(listOf(), listOf())
    } else if (boundaries.size == 1) {
        boundaries.first()
    } else {
        val headBoundary = boundaries.first()
        val bodyBoundary = connectBoundaryChain(boundaries.subList(1, boundaries.size))

        headBoundary.connectNext(bodyBoundary)
    }
