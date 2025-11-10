import com.github.ityeri.comshop.builder.LiteralCommandBuilder
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType

class ExampleCommand : LiteralCommandBuilder() {
    override val name: String = "example"

    init {
        arguments {
            "unsigned int" { IntegerArgumentType.integer(0) }
            "flag" { BoolArgumentType.bool() }
        }

        executes { source ->
            val integer = getArg("unsigned int", Int::class)
            val flag = getArg("flag", Boolean::class)
            source.sender.sendMessage("Unsinged integer: $integer, flag: $flag")
            0
        }
    }
}