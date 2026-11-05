package com.github.ityeri.comshop.builder

import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.github.ityeri.comshop.api.argument.StringType
import io.papermc.paper.registry.RegistryKey


open class NativeArgumentTypeFactory {
    // Primitive types
    fun boolean() = NativeArgumentType.BooleanType()

    fun int(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE) =
        NativeArgumentType.IntType(min, max)

    fun float(min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE) =
        NativeArgumentType.FloatType(min, max)

    fun double(min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE) =
        NativeArgumentType.DoubleType(min, max)

    fun long(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE) =
        NativeArgumentType.LongType(min, max)

    // beeeeeeeecause Some unknown compile bug
    fun string(type: StringType): NativeArgumentType<String> = NativeArgumentType.StringArgType(type)
    fun word() = NativeArgumentType.StringArgType(StringType.WORD)
    fun quotedString() = NativeArgumentType.StringArgType(StringType.QUOTED)
    fun greedyString() = NativeArgumentType.StringArgType(StringType.GREEDY)

    // Paper types from io.papermc.paper.command.brigadier.argument.ArgumentTypes
    fun entity() = NativeArgumentType.Entity()
    fun entities() = NativeArgumentType.Entities()
    fun player() = NativeArgumentType.Player()
    fun players() = NativeArgumentType.Players()
    fun playerProfiles() = NativeArgumentType.PlayerProfiles()

    fun blockPosition() = NativeArgumentType.BlockPosition()
    fun finePosition(centerIntegers: Boolean = false) =
        NativeArgumentType.FinePosition(centerIntegers)

    fun rotation() = NativeArgumentType.Rotation()

    fun blockState() = NativeArgumentType.BlockState()
    fun itemStack() = NativeArgumentType.ItemStack()
    fun itemPredicate() = NativeArgumentType.ItemPredicate()

    fun namedColor() = NativeArgumentType.NamedColor()
    fun component() = NativeArgumentType.Component()
    fun style() = NativeArgumentType.Style()
    fun signedMessage() = NativeArgumentType.SignedMessage()

    fun scoreboardDisplaySlot() = NativeArgumentType.ScoreboardDisplaySlot()
    fun namespacedKey() = NativeArgumentType.NamespacedKey()
    fun key() = NativeArgumentType.Key()

    fun integerRange() = NativeArgumentType.IntegerRange()
    fun doubleRange() = NativeArgumentType.DoubleRange()

    fun world() = NativeArgumentType.World()
    fun gameMode() = NativeArgumentType.GameMode()
    fun heightMap() = NativeArgumentType.HeightMap()
    fun uuid() = NativeArgumentType.Uuid()
    fun objectiveCriteria() = NativeArgumentType.ObjectiveCriteria()
    fun entityAnchor() = NativeArgumentType.EntityAnchor()

    fun time(minTime: Int = 0) = NativeArgumentType.Time(minTime)

    fun templateMirror() = NativeArgumentType.TemplateMirror()
    fun templateRotation() = NativeArgumentType.TemplateRotation()

    fun <T : Any> resource(registryKey: RegistryKey<T>) =
        NativeArgumentType.Resource(registryKey)

    fun <T : Any> resourceKey(registryKey: RegistryKey<T>) =
        NativeArgumentType.ResourceKey(registryKey)
}
