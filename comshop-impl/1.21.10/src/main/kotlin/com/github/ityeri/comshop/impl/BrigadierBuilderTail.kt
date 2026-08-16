package com.github.ityeri.comshop.impl

import com.mojang.brigadier.Command
import io.papermc.paper.command.brigadier.CommandSourceStack


sealed class BrigadierBuilderTail {
    class BuilderTail(
        val entries: Collection<BrigadierNodeBuilder>
    ) : BrigadierBuilderTail() {
        constructor (entry: BrigadierNodeBuilder) : this (entries = listOf(entry))
    }
    class CommandTail(
        val command: Command<CommandSourceStack>
    ) : BrigadierBuilderTail()
}
