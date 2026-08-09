package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.api.node.ComshopCommandNode
import com.github.ityeri.comshop.api.node.Node
import com.github.ityeri.comshop.impl.BuilderBoundary
import com.github.ityeri.comshop.impl.optic.nodePTraversal


fun toSingleBuilderBoundary(node: Node<ComshopCommandNode>): BuilderBoundary =
    connectBuilderBoundaries(
        nodePTraversal<ComshopCommandNode, BuilderBoundary>()
            .modify(node) { node ->
                toBuilderBoundary(
                    toCommandFragment(node)
                )
            }
    )
