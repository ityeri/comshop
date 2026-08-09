package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.impl.BuilderBoundary
import com.github.ityeri.comshop.impl.BrigadierFragment


fun BrigadierFragment.wrapBuilderBoundary(): BuilderBoundary =
    when (this) {
        is BrigadierFragment.NodeBuilderFragment -> {
            BuilderBoundary(listOf(builder), listOf(builder))
        }
        is BrigadierFragment.ExecutionFragment -> {
            BuilderBoundary(
                entries = emptyList(),
                exits = emptyList(),
                pendingCommand = command
            )
        }
    }
