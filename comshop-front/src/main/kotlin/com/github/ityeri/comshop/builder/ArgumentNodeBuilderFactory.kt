package com.github.ityeri.comshop.builder

import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.github.ityeri.comshop.api.argument.StringType
import io.papermc.paper.registry.RegistryKey


open class ArgumentNodeBuilderFactory {
    fun <T : Any> fromNativeType(
        argumentType: NativeArgumentType<T>
    ): ArgumentStructureBuilder.SingleNodeBuilder<T> =
        ArgumentStructureBuilder.SingleNodeBuilder(
            null, argumentType
        )

    // Primitive types
    fun boolean() = fromNativeType(NativeArgumentType.BooleanArgumentType())

    fun int(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE) =
        fromNativeType(NativeArgumentType.IntArgumentType(min, max))

    fun float(min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE) =
        fromNativeType(NativeArgumentType.FloatArgumentType(min, max))

    fun double(min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE) =
        fromNativeType(NativeArgumentType.DoubleArgumentType(min, max))

    fun long(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE) =
        fromNativeType(NativeArgumentType.LongArgumentType(min, max))

    fun string(type: StringType) = fromNativeType(NativeArgumentType.StringArgumentType(type))
    fun word() = fromNativeType(NativeArgumentType.StringArgumentType(StringType.WORD))
    fun quotedString() = fromNativeType(NativeArgumentType.StringArgumentType(StringType.QUOTED))
    fun greedyString() = fromNativeType(NativeArgumentType.StringArgumentType(StringType.GREEDY))

    // Paper types from io.papermc.paper.command.brigadier.argument.ArgumentTypes
    fun entity() = fromNativeType(NativeArgumentType.EntityArgumentType())
    fun entities() = fromNativeType(NativeArgumentType.EntitiesArgumentType())
    fun player() = fromNativeType(NativeArgumentType.PlayerArgumentType())
    fun players() = fromNativeType(NativeArgumentType.PlayersArgumentType())
    fun playerProfiles() = fromNativeType(NativeArgumentType.PlayerProfilesArgumentType())

    fun blockPosition() = fromNativeType(NativeArgumentType.BlockPositionArgumentType())
    fun finePosition(centerIntegers: Boolean = false) =
        fromNativeType(NativeArgumentType.FinePositionArgumentType(centerIntegers))
    fun rotation() = fromNativeType(NativeArgumentType.RotationArgumentType())

    fun blockState() = fromNativeType(NativeArgumentType.BlockStateArgumentType())
    fun itemStack() = fromNativeType(NativeArgumentType.ItemStackArgumentType())
    fun itemPredicate() = fromNativeType(NativeArgumentType.ItemPredicateArgumentType())

    fun namedColor() = fromNativeType(NativeArgumentType.NamedColorArgumentType())
    fun component() = fromNativeType(NativeArgumentType.ComponentArgumentType())
    fun style() = fromNativeType(NativeArgumentType.StyleArgumentType())
    fun signedMessage() = fromNativeType(NativeArgumentType.SignedMessageArgumentType())

    fun scoreboardDisplaySlot() = fromNativeType(NativeArgumentType.ScoreboardDisplaySlotArgumentType())
    fun namespacedKey() = fromNativeType(NativeArgumentType.NamespacedKeyArgumentType())
    fun key() = fromNativeType(NativeArgumentType.KeyArgumentType())

    fun integerRange() = fromNativeType(NativeArgumentType.IntegerRangeArgumentType())
    fun doubleRange() = fromNativeType(NativeArgumentType.DoubleRangeArgumentType())

    fun world() = fromNativeType(NativeArgumentType.WorldArgumentType())
    fun gameMode() = fromNativeType(NativeArgumentType.GameModeArgumentType())
    fun heightMap() = fromNativeType(NativeArgumentType.HeightMapArgumentType())
    fun uuid() = fromNativeType(NativeArgumentType.UuidArgumentType())
    fun objectiveCriteria() = fromNativeType(NativeArgumentType.ObjectiveCriteriaArgumentType())
    fun entityAnchor() = fromNativeType(NativeArgumentType.EntityAnchorArgumentType())

    fun time(minTime: Int = 0) = fromNativeType(NativeArgumentType.TimeArgumentType(minTime))

    fun templateMirror() = fromNativeType(NativeArgumentType.TemplateMirrorArgumentType())
    fun templateRotation() = fromNativeType(NativeArgumentType.TemplateRotationArgumentType())

    fun <T : Any> resource(registryKey: RegistryKey<T>) =
        fromNativeType(NativeArgumentType.ResourceArgumentType(registryKey))

    fun <T : Any> resourceKey(registryKey: RegistryKey<T>) =
        fromNativeType(NativeArgumentType.ResourceKeyArgumentType(registryKey))
}
