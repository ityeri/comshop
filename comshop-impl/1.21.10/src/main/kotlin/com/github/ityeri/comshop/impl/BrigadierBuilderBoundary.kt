package com.github.ityeri.comshop.impl

import com.mojang.brigadier.Command
import io.papermc.paper.command.brigadier.CommandSourceStack


class BrigadierBuilderBoundary(
    val entries: Collection<BrigadierNodeBuilder>,
    val exits: Collection<BrigadierNodeBuilder>,
    val pendingCommand: Command<CommandSourceStack>? = null
) {
    val onlyExecutionFragment: Boolean
        get() = pendingCommand != null && exits.isEmpty()

    fun connectNext(boundary: BrigadierBuilderBoundary): BrigadierBuilderBoundary {
        exits.forEach { exitBuilder ->
            boundary.entries.forEach { entryBuilder ->
                exitBuilder.children.add(entryBuilder)
            }
        }

        if (boundary.pendingCommand != null) {
            exits.forEach { it.command = boundary.pendingCommand }
        }

        return BrigadierBuilderBoundary(
            entries,
            if (boundary.onlyExecutionFragment) exits else boundary.exits
        )
    }
}
