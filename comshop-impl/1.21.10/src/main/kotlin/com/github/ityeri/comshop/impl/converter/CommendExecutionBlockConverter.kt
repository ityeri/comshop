package com.github.ityeri.comshop.impl.converter

import com.github.ityeri.comshop.api.ComshopContext
import com.github.ityeri.comshop.api.exception.ComshopCommandException
import com.github.ityeri.comshop.api.node.ComshopCommandBlock
import com.mojang.brigadier.Command
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import io.papermc.paper.command.brigadier.CommandSourceStack


fun ComshopCommandBlock.toBrigadierCommand(): Command<CommandSourceStack> = { context ->
    val comshopContext = object : ComshopContext {
        override val source = context.source

        override fun <T> getArgument(name: String, clazz: Class<T>): T {
            return context.getArgument(name, clazz)
        }
    }

    try {
        this.invoke(comshopContext).toInt()
    } catch (e: ComshopCommandException) {
        throw SimpleCommandExceptionType({ e.message }).create()
    } catch (e: CommandSyntaxException) {
        throw IllegalStateException(
            "Command execution block defined in comshop should not throw CommandSyntaxException, "
                    + "which belongs to brigadier. Use ComshopCommandException instead",
            e
        )
    }
}
