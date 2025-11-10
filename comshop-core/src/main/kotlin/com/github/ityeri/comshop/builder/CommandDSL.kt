package com.github.ityeri.comshop.builder

import com.github.ityeri.comshop.ArgumentData
import com.github.ityeri.comshop.ArgumentRegistrar
import com.github.ityeri.comshop.ContextWrapper
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.papermc.paper.command.brigadier.CommandSourceStack

class CommandDSL(override val name: String) : LiteralCommandBuilder() {
    companion object {
        fun command(name: String, block: CommandDSL.() -> Unit = {}): CommandDSL {
            val commandDsl = CommandDSL(name)
            commandDsl.apply(block)
            return commandDsl
        }
    }

    fun then(name: String, block: CommandDSL.() -> Unit) {
        val commandDsl = CommandDSL(name)
        subCommands.add(commandDsl)
        commandDsl.apply(block)
    }
}