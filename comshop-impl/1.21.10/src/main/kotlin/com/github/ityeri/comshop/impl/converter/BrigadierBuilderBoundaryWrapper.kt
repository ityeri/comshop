package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.impl.BrigadierBuilderBoundary
import com.github.ityeri.comshop.impl.BrigadierFragment


fun BrigadierFragment.toBrigadierBuilderBoundary(): BrigadierBuilderBoundary =
    when (this) {
        is BrigadierFragment.NodeBuilderFragment -> {
            BrigadierBuilderBoundary(listOf(builder), listOf(builder))
        }
        is BrigadierFragment.ExecutionFragment -> {
            BrigadierBuilderBoundary(
                entries = emptyList(),
                exits = emptyList(),
                pendingCommand = command
            )
        }
    }
