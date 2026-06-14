package com.github.ityeri.comshop.builder

import com.github.ityeri.comshop.AnyArgumentNode
import com.github.ityeri.comshop.context.SourceContext
import com.github.ityeri.comshop.ComshopDsl
import com.github.ityeri.comshop.api.argument.ComshopArgumentType
import com.github.ityeri.comshop.api.node.ComshopCommandNode
import com.github.ityeri.comshop.api.node.Node


typealias CustomSuggestionProvider = SuggestionBuilder.() -> Unit

@ComshopDsl
sealed interface ArgumentStructureBuilder {
    fun build(): Node<AnyArgumentNode>


    @ComshopDsl
    class SingleNodeBuilder<T : Any>(
        val name: String? = null,
        val argumentType: ComshopArgumentType<T>,
        protected val requiresChecker: SourceContext.() -> Boolean = { true },
        protected val customSuggestionProvider: CustomSuggestionProvider? = null,
    ) : ArgumentStructureBuilder {
        fun named(name: String): SingleNodeBuilder<T> =
            SingleNodeBuilder(
                name,
                this.argumentType,
                this.requiresChecker,
                this.customSuggestionProvider
            )
        fun requires(requiresChecker: SourceContext.() -> Boolean): SingleNodeBuilder<T> =
            SingleNodeBuilder(
                this.name,
                this.argumentType,
                requiresChecker,
                this.customSuggestionProvider
            )
        fun suggests(customSuggestionProvider: SuggestionBuilder.() -> Unit): SingleNodeBuilder<T> =
            SingleNodeBuilder(
                this.name,
                this.argumentType,
                this.requiresChecker,
                customSuggestionProvider
            )

        override fun build(): Node<AnyArgumentNode> =
            Node.SingleNode(
                ComshopCommandNode.ArgumentNode(
                    name = name ?: throw IllegalStateException("The name field cannot be null while building"),
                    argumentType = argumentType,
                    requiresChecker = { source ->
                        SourceContext(source).run(requiresChecker)
                    },
                    customSuggestionProvider = customSuggestionProvider?.let { provider ->
                        { context, source ->
                            SuggestionBuilder(source, context).apply(provider).build()
                        }
                    }
                )
            )
    }

    @ComshopDsl
    abstract class IterableStructureBuilder(
        protected val subBuilders: MutableList<ArgumentStructureBuilder>
    ) : ArgumentStructureBuilder, ArgumentNodeBuilderFactory() {
        infix fun <T : Any> String.to(builder: SingleNodeBuilder<T>) {
            subBuilders.add(builder.named(this))
        }
    }

    @ComshopDsl
    class ChainStructureBuilder(
        subBuilders: MutableList<ArgumentStructureBuilder> = mutableListOf()
    ): IterableStructureBuilder(subBuilders) {
        fun unions(block: UnionStructureBuilder.() -> Unit) {
            subBuilders.add(UnionStructureBuilder().apply(block))
        }

        override fun build(): Node<AnyArgumentNode> =
            Node.ChainNode(
                subBuilders.map { it.build() }
            )
    }

    @ComshopDsl
    class UnionStructureBuilder(
        subBuilders: MutableList<ArgumentStructureBuilder> = mutableListOf()
    ): IterableStructureBuilder(subBuilders) {
        fun argument(builder: ArgumentStructureBuilder) {
            subBuilders.add(builder)
        }
        fun arguments(block: ChainStructureBuilder.() -> Unit) {
            subBuilders.add(ChainStructureBuilder().apply(block))
        }

        override fun build(): Node<AnyArgumentNode> =
            Node.UnionNode(
                subBuilders.map { it.build() }
            )
    }
}
