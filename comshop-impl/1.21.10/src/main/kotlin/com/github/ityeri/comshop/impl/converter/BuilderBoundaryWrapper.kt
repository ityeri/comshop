package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.impl.BuilderBoundary
import com.github.ityeri.comshop.impl.CommandFragment


fun CommandFragment.wrapBuilderBoundary(): BuilderBoundary =
    when (this) {
        is CommandFragment.NodeBuilderFragment -> {
            BuilderBoundary(listOf(builder), listOf(builder))
        }
        is CommandFragment.ExecutionFragment -> {
            BuilderBoundary(
                entries = emptyList(),
                exits = emptyList(),
                pendingCommand = command
            )
        }
    }
