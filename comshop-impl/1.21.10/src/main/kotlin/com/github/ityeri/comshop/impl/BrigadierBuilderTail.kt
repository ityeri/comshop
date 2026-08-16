package com.github.ityeri.comshop.impl

import com.mojang.brigadier.Command
import io.papermc.paper.command.brigadier.CommandSourceStack


data class BrigadierBuilderTail(
    val entries: Collection<BrigadierNodeBuilder> = emptyList(),
    val command: Command<CommandSourceStack>? = null
) {
    constructor(entry: BrigadierNodeBuilder) : this(listOf(entry))
}
