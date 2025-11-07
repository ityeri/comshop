package com.github.ityeri.comshop

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack

interface CommandBuilder {
    fun build(): LiteralArgumentBuilder<CommandSourceStack>
}