package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.impl.BuilderBoundary
import com.github.ityeri.comshop.impl.CommandFragment


fun toBuilderBoundary(commandFragment: CommandFragment): BuilderBoundary =
    when (commandFragment) {
        is CommandFragment.NodeBuilderFragment -> {
            BuilderBoundary(listOf(commandFragment.builder), listOf(commandFragment.builder))
        }
        is CommandFragment.ExecutionFragment -> {
            BuilderBoundary(
                entries = emptyList(),
                exits = emptyList(),
                pendingCommand = commandFragment.command
            )
        }
    }
