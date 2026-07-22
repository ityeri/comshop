package com.github.ityeri.comshop.builder

import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.github.ityeri.comshop.api.argument.StringType
import io.papermc.paper.registry.RegistryKey


open class NativeArgumentTypeFactory {
    // Primitive types
    fun boolean() = NativeArgumentType.BooleanArgumentType()

    fun int(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE) =
        NativeArgumentType.IntArgumentType(min, max)

    fun float(min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE) =
        NativeArgumentType.FloatArgumentType(min, max)

    fun double(min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE) =
        NativeArgumentType.DoubleArgumentType(min, max)

    fun long(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE) =
        NativeArgumentType.LongArgumentType(min, max)

    // beeeeeeeecause Some unknown compile bug
    fun string(type: StringType): NativeArgumentType<String> = NativeArgumentType.StringArgumentType(type)
    fun word() = NativeArgumentType.StringArgumentType(StringType.WORD)
    fun quotedString() = NativeArgumentType.StringArgumentType(StringType.QUOTED)
    fun greedyString() = NativeArgumentType.StringArgumentType(StringType.GREEDY)

    // Paper types from io.papermc.paper.command.brigadier.argument.ArgumentTypes
    fun entity() = NativeArgumentType.EntityArgumentType()
    fun entities() = NativeArgumentType.EntitiesArgumentType()
    fun player() = NativeArgumentType.PlayerArgumentType()
    fun players() = NativeArgumentType.PlayersArgumentType()
    fun playerProfiles() = NativeArgumentType.PlayerProfilesArgumentType()

    fun blockPosition() = NativeArgumentType.BlockPositionArgumentType()
    fun finePosition(centerIntegers: Boolean = false) =
        NativeArgumentType.FinePositionArgumentType(centerIntegers)
    fun rotation() = NativeArgumentType.RotationArgumentType()

    fun blockState() = NativeArgumentType.BlockStateArgumentType()
    fun itemStack() = NativeArgumentType.ItemStackArgumentType()
    fun itemPredicate() = NativeArgumentType.ItemPredicateArgumentType()

    fun namedColor() = NativeArgumentType.NamedColorArgumentType()
    fun component() = NativeArgumentType.ComponentArgumentType()
    fun style() = NativeArgumentType.StyleArgumentType()
    fun signedMessage() = NativeArgumentType.SignedMessageArgumentType()

    fun scoreboardDisplaySlot() = NativeArgumentType.ScoreboardDisplaySlotArgumentType()
    fun namespacedKey() = NativeArgumentType.NamespacedKeyArgumentType()
    fun key() = NativeArgumentType.KeyArgumentType()

    fun integerRange() = NativeArgumentType.IntegerRangeArgumentType()
    fun doubleRange() = NativeArgumentType.DoubleRangeArgumentType()

    fun world() = NativeArgumentType.WorldArgumentType()
    fun gameMode() = NativeArgumentType.GameModeArgumentType()
    fun heightMap() = NativeArgumentType.HeightMapArgumentType()
    fun uuid() = NativeArgumentType.UuidArgumentType()
    fun objectiveCriteria() = NativeArgumentType.ObjectiveCriteriaArgumentType()
    fun entityAnchor() = NativeArgumentType.EntityAnchorArgumentType()

    fun time(minTime: Int = 0) = NativeArgumentType.TimeArgumentType(minTime)

    fun templateMirror() = NativeArgumentType.TemplateMirrorArgumentType()
    fun templateRotation() = NativeArgumentType.TemplateRotationArgumentType()

    fun <T : Any> resource(registryKey: RegistryKey<T>) =
        NativeArgumentType.ResourceArgumentType(registryKey)

    fun <T : Any> resourceKey(registryKey: RegistryKey<T>) =
        NativeArgumentType.ResourceKeyArgumentType(registryKey)
}
