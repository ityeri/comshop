package com.github.ityeri.comshop.api

import io.papermc.paper.command.brigadier.CommandSourceStack

interface ComshopContext {
    val source: CommandSourceStack
    fun <T> getArgument(name: String, clazz: Class<T>): T
}
