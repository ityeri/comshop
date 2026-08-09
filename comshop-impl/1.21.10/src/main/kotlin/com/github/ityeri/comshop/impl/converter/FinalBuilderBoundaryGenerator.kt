package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.api.node.ComshopCommandNode
import com.github.ityeri.comshop.api.node.Node
import com.github.ityeri.comshop.impl.BuilderBoundary
import com.github.ityeri.comshop.impl.optic.nodePTraversal


fun toFinalBuilderBoundary(node: Node<ComshopCommandNode>): BuilderBoundary =
        nodePTraversal<ComshopCommandNode, BuilderBoundary>()
            .modify(node) { node ->
                node
                    .toCommandFragment()
                    .wrapBuilderBoundary()
            }
            .connectBuilderBoundaries()
