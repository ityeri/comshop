import com.github.ityeri.comshop.builder.LiteralCommandBuilder
import com.mojang.brigadier.arguments.IntegerArgumentType

class ExampleCommand : LiteralCommandBuilder() {
    override val name: String = "example"

    init {
        arguments {
            "unsigned int" { IntegerArgumentType.integer(0) }
        }

        executes { source ->
            val integer = getArg("unsigned int", Int::class)
            source.sender.sendMessage("You typed unsigned integer: $integer")
            0
        }
    }
}