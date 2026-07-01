package com.github.ityeri.comshop.impl

import com.mojang.brigadier.Command
import io.papermc.paper.command.brigadier.CommandSourceStack


sealed class CommandFragment {
    class NodeBuilderFragment(val builder: BrigadierNodeBuilder) : CommandFragment()
    class ExecutionFragment(val command: Command<CommandSourceStack>) : CommandFragment()
}
