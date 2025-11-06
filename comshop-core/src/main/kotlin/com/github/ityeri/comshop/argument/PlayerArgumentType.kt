package com.github.ityeri.comshop.argument

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

// TODO
private class PlayerArgumentType : CustomArgumentType.Converted<Player, String> {
    override fun <S : Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        for (player in Bukkit.getOnlinePlayers()) {
            val playerNameLowerCase = player.name.lowercase()
            if (playerNameLowerCase.startsWith(builder.remainingLowerCase)) {
                builder.suggest(player.name)
            }
        }

        return builder.buildFuture()
    }

    override fun convert(nativeType: String): Player {
        val player = Bukkit.getPlayerExact(nativeType)

        println("$nativeType, $player")

        if (player == null) {
            // TODO 서버에 보이는 예외처리 어캄? + CustomArgumentType 이해하고 이것도 레퍼 하나 만들어야 할듯;;
            throw Exception("No player was found")
        } else {
            return player
        }
    }

    override fun getNativeType(): ArgumentType<String> {
        return StringArgumentType.word()
    }
}