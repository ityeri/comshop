package com.github.ityeri.comshop.api.argument

import com.destroystokyo.paper.profile.PlayerProfile
import com.google.common.collect.Range
import io.papermc.paper.command.brigadier.argument.SignedMessageResolver
import io.papermc.paper.command.brigadier.argument.predicate.ItemStackPredicate
import io.papermc.paper.entity.LookAnchor
import io.papermc.paper.math.BlockPosition
import io.papermc.paper.math.Rotation
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import org.bukkit.*
import org.bukkit.block.BlockState
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import java.util.*


sealed class NativeArgumentType<T : Any> : ComshopArgumentType<T> {
    // ArgumentType class's name prefix follows target generic type's class name (kotlin-side name first)


    // Primitive types
    class BooleanArgumentType : NativeArgumentType<Boolean>()

    class IntArgumentType(
        val min: Int = Int.MIN_VALUE,
        val max: Int = Int.MAX_VALUE
    ) : NativeArgumentType<Int>()

    class FloatArgumentType(
        val min: Float = Float.MIN_VALUE,
        val max: Float = Float.MAX_VALUE
    ) : NativeArgumentType<Float>()

    class DoubleArgumentType(
        val min: Double = Double.MIN_VALUE,
        val max: Double = Double.MAX_VALUE
    ) : NativeArgumentType<Double>()

    class LongArgumentType(
        val min: Long = Long.MIN_VALUE,
        val max: Long = Long.MAX_VALUE
    ) : NativeArgumentType<Long>()

    class StringArgumentType(
        val type: StringType = StringType.WORD
    ) : NativeArgumentType<String>()


    // Paper types from io.papermc.paper.command.brigadier.argument.ArgumentTypes
    class EntityArgumentType : NativeArgumentType<Entity>()
    class EntitiesArgumentType : NativeArgumentType<List<Entity>>()
    class PlayerArgumentType : NativeArgumentType<Player>()
    class PlayersArgumentType : NativeArgumentType<List<Player>>()
    class PlayerProfilesArgumentType : NativeArgumentType<Collection<PlayerProfile>>()

    class BlockPositionArgumentType : NativeArgumentType<BlockPosition>()
    class FinePositionArgumentType(
        val centerIntegers: Boolean = false
    ) : NativeArgumentType<Location>()
    class RotationArgumentType : NativeArgumentType<Rotation>()

    class BlockStateArgumentType : NativeArgumentType<BlockState>()
    class ItemStackArgumentType : NativeArgumentType<ItemStack>()
    class ItemPredicateArgumentType : NativeArgumentType<ItemStackPredicate>()

    class NamedColorArgumentType : NativeArgumentType<NamedTextColor>()
    class ComponentArgumentType : NativeArgumentType<Component>()
    class StyleArgumentType : NativeArgumentType<Style>()
    class SignedMessageArgumentType : NativeArgumentType<SignedMessageResolver>()

    class ScoreboardDisplaySlotArgumentType : NativeArgumentType<DisplaySlot>()
    class NamespacedKeyArgumentType : NativeArgumentType<NamespacedKey>()
    class KeyArgumentType : NativeArgumentType<Key>()

    class IntegerRangeArgumentType : NativeArgumentType<Range<Int>>()
    class DoubleRangeArgumentType : NativeArgumentType<Range<Double>>()

    class WorldArgumentType : NativeArgumentType<World>()
    class GameModeArgumentType : NativeArgumentType<GameMode>()
    class HeightMapArgumentType : NativeArgumentType<HeightMap>()
    class UuidArgumentType : NativeArgumentType<UUID>()
    class ObjectiveCriteriaArgumentType : NativeArgumentType<Criteria>()
    class EntityAnchorArgumentType : NativeArgumentType<LookAnchor>()

    class TimeArgumentType(
        val minTime: Int = 0
    ) : NativeArgumentType<Int>()

    class TemplateMirrorArgumentType : NativeArgumentType<Mirror>()
    class TemplateRotationArgumentType : NativeArgumentType<StructureRotation>()

    class ResourceArgumentType<T : Any>(
        val registryKey: RegistryKey<T>
    ) : NativeArgumentType<T>()

    class ResourceKeyArgumentType<T : Any>(
        val registryKey: RegistryKey<T>
    ) : NativeArgumentType<TypedKey<T>>()
}
