package com.github.ityeri.comshop.context

import com.github.ityeri.comshop.ComshopDsl
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player


@ComshopDsl
open class SourceContext(val source: CommandSourceStack) {
    val sender: CommandSender
        get() = source.sender
    val entity: Entity?
        get() = source.executor
    val player: Player?
        get() = source.sender as? Player
}
