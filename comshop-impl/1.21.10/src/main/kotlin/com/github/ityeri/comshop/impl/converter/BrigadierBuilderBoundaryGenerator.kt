package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.api.node.ComshopCommandNode
import com.github.ityeri.comshop.api.node.Node
import com.github.ityeri.comshop.impl.BrigadierBuilderBoundary
import com.github.ityeri.comshop.impl.optic.nodePTraversal


fun toFinalBuilderBoundary(node: Node<ComshopCommandNode>): BrigadierBuilderBoundary =
    nodePTraversal<ComshopCommandNode, BrigadierBuilderBoundary>()
        .modify(node) { node ->
            node
                .toBrigadierFragment()
                .toBrigadierBuilderBoundary()
        }
        .connectBuilderBoundaries()
