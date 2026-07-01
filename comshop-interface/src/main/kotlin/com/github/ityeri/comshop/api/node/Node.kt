package com.github.ityeri.comshop.api.node

sealed class Node<out T> {
    class SingleNode<T>(val value: T) : Node<T>()
    class UnionNode<T>(val nodes: Iterable<Node<T>>) : Node<T>() {
        constructor(vararg nodes: Node<T>): this(nodes.toList())
    }
    class ChainNode<T>(val nodes: Iterable<Node<T>>) : Node<T>() {
        constructor(vararg nodes: Node<T>): this(nodes.toList())
    }
}
