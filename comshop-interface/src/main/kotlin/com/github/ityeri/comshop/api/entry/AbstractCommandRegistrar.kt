package com.github.ityeri.comshop.api.entry

import com.github.ityeri.comshop.api.node.Node
import com.github.ityeri.comshop.api.node.ComshopCommandNode
import org.bukkit.plugin.java.JavaPlugin

interface AbstractCommandRegistrar {
    fun init(plugin: JavaPlugin)
    fun register(node: Node<ComshopCommandNode>)
}
