package com.github.ityeri.comshop.dsl

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack

interface CommandBuilder {
    fun createBuilder(): LiteralArgumentBuilder<CommandSourceStack>
}