import com.github.ityeri.comshop.api.CommandResult
import com.github.ityeri.comshop.command
import com.github.ityeri.comshop.enumArgument
import com.github.ityeri.comshop.selectArgument

enum class Fruit {
    APPLE,
    BANANA,
    JAVASCRIPT,
    STRAWBERRY
}

fun FruitArgumentType() = enumArgument(Fruit::class)

fun StringFruitArgumentType() = selectArgument(Fruit.entries.map { it.name })

val fruitCommand = command("fruit") {
    arguments {
        "fruit" named FruitArgumentType().asArg
    }

    executes {
        val fruit = "fruit" to Fruit::class

        sender.sendMessage("You are choose the ${fruit}!")

        CommandResult.SUCCESS
    }
}

val stringFruitCommand = command("sfruit") {
    arguments {
        "fruit" named StringFruitArgumentType().asArg
    }

    executes {
        val fruit = "fruit" to String::class

        sender.sendMessage("You are choose the ${fruit}!")

        CommandResult.SUCCESS
    }
}
