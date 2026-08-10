package com.github.ityeri.comshop.impl

import com.mojang.brigadier.Command
import io.papermc.paper.command.brigadier.CommandSourceStack


sealed class BrigadierFragment {
    class NodeBuilderFragment(val builder: BrigadierNodeBuilder) : BrigadierFragment()
    class ExecutionFragment(val command: Command<CommandSourceStack>) : BrigadierFragment()
}
