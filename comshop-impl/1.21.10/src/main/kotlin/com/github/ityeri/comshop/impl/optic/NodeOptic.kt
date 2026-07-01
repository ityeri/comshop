package com.github.ityeri.comshop.impl.optic

import arrow.optics.PLens
import arrow.optics.PTraversal
import com.github.ityeri.comshop.api.node.Node


fun <A, B> nodeCastingPLens(): PLens<
        Node<A>,
        Node<B>,
        Choice3<Node.SingleNode<A>, Node.UnionNode<A>, Node.ChainNode<A>>,
        Choice3<Node.SingleNode<B>, Node.UnionNode<B>, Node.ChainNode<B>>,
        > = PLens(
    get = { node ->
        when (node) {
            is Node.SingleNode<A> -> {
                Choice3.First(node)
            }
            is Node.UnionNode<A> -> {
                Choice3.Second(node)
            }
            is Node.ChainNode<A> -> {
                Choice3.Third(node)
            }
        }
    },
    set = { _, newValue ->
        newValue.fold(
            ifFirst = { singleNodeB -> singleNodeB },
            ifSecond = { unionNodeB -> unionNodeB },
            ifThird = { chainNodeB -> chainNodeB }
        )
    }
)


fun <A, B> singleNodePLens(): PLens<
        Node.SingleNode<A>,
        Node.SingleNode<B>,
        A,
        B
        > = PLens(
    get = { node -> node.value },
    set = { _, newValue -> Node.SingleNode(newValue) }
)

fun <A, B> unionNodePTraversal(): PTraversal<
        Node.UnionNode<A>,
        Node.UnionNode<B>,
        Node<A>,
        Node<B>
> = PTraversal { source, map ->
    Node.UnionNode(
        source.nodes.map { map(it) }
    )
}

fun <A, B> chainNodePTraversal(): PTraversal<
        Node.ChainNode<A>,
        Node.ChainNode<B>,
        Node<A>,
        Node<B>
        > = PTraversal { source, map ->
    Node.ChainNode(
        source.nodes.map { map(it) }
    )
}


fun <A, B> nodePTraversal(): PTraversal<Node<A>, Node<B>, A, B> =
    nodeCastingPLens<A, B>() + split3(
        traversal1 = singleNodePLens(),
        traversal2 = unionNodePTraversal<A, B>() + PTraversal { source, mapFunc ->
            nodePTraversal<A, B>().modify(source, mapFunc)
        },
        traversal3 = chainNodePTraversal<A, B>() + PTraversal { source, mapFunc ->
            nodePTraversal<A, B>().modify(source, mapFunc)
        }
    )
