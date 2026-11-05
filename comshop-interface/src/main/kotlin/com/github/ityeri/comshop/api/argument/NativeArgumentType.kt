package com.github.ityeri.comshop.api.argument

import com.destroystokyo.paper.profile.PlayerProfile
import com.google.common.collect.Range
import io.papermc.paper.command.brigadier.argument.SignedMessageResolver
import io.papermc.paper.command.brigadier.argument.predicate.ItemStackPredicate
import io.papermc.paper.entity.LookAnchor
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.*
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import java.util.*


sealed class NativeArgumentType<T : Any> : ComshopArgumentType<T> {
    // Primitive types
    // Primitive type class's name prefix follows the form: kotlin-side type name + Type suffix
    // ex: Int -> class IntType
    class BooleanType : NativeArgumentType<Boolean>()

    class IntType(
        val min: Int = Int.MIN_VALUE,
        val max: Int = Int.MAX_VALUE
    ) : NativeArgumentType<Int>()

    class FloatType(
        val min: Float = Float.MIN_VALUE,
        val max: Float = Float.MAX_VALUE
    ) : NativeArgumentType<Float>()

    class DoubleType(
        val min: Double = Double.MIN_VALUE,
        val max: Double = Double.MAX_VALUE
    ) : NativeArgumentType<Double>()

    class LongType(
        val min: Long = Long.MIN_VALUE,
        val max: Long = Long.MAX_VALUE
    ) : NativeArgumentType<Long>()

    class StringArgType( // Exceptional name to prevent conflict with `enum StringType`
        val type: StringType = StringType.WORD
    ) : NativeArgumentType<String>()


    // Paper types from io.papermc.paper.command.brigadier.argument.ArgumentTypes
    // Paper type class's name prefix follows origin method name in ArgumentTypes class
    // ex: ArgumentTypes.resourceKey() -> class ResourceKey (not `ResourceKeyArgumentType`)
    class Entity : NativeArgumentType<org.bukkit.entity.Entity>()
    class Entities : NativeArgumentType<List<org.bukkit.entity.Entity>>()
    class Player : NativeArgumentType<org.bukkit.entity.Player>()
    class Players : NativeArgumentType<List<org.bukkit.entity.Player>>()
    class PlayerProfiles : NativeArgumentType<Collection<PlayerProfile>>()

    class BlockPosition : NativeArgumentType<io.papermc.paper.math.BlockPosition>()
    class FinePosition(
        val centerIntegers: Boolean = false
    ) : NativeArgumentType<Location>()

    class Rotation : NativeArgumentType<io.papermc.paper.math.Rotation>()

    class BlockState : NativeArgumentType<org.bukkit.block.BlockState>()
    class ItemStack : NativeArgumentType<org.bukkit.inventory.ItemStack>()
    class ItemPredicate : NativeArgumentType<ItemStackPredicate>()

    class NamedColor : NativeArgumentType<NamedTextColor>()
    class Component : NativeArgumentType<net.kyori.adventure.text.Component>()
    class Style : NativeArgumentType<net.kyori.adventure.text.format.Style>()
    class SignedMessage : NativeArgumentType<SignedMessageResolver>()

    class ScoreboardDisplaySlot : NativeArgumentType<DisplaySlot>()
    class NamespacedKey : NativeArgumentType<org.bukkit.NamespacedKey>()
    class Key : NativeArgumentType<net.kyori.adventure.key.Key>()

    class IntegerRange : NativeArgumentType<Range<Int>>()
    class DoubleRange : NativeArgumentType<Range<Double>>()

    class World : NativeArgumentType<org.bukkit.World>()
    class GameMode : NativeArgumentType<org.bukkit.GameMode>()
    class HeightMap : NativeArgumentType<org.bukkit.HeightMap>()
    class Uuid : NativeArgumentType<UUID>()
    class ObjectiveCriteria : NativeArgumentType<Criteria>()
    class EntityAnchor : NativeArgumentType<LookAnchor>()

    class Time(
        val minTime: Int = 0
    ) : NativeArgumentType<Int>()

    class TemplateMirror : NativeArgumentType<Mirror>()
    class TemplateRotation : NativeArgumentType<StructureRotation>()

    class Resource<T : Any>(
        val registryKey: RegistryKey<T>
    ) : NativeArgumentType<T>()

    class ResourceKey<T : Any>(
        val registryKey: RegistryKey<T>
    ) : NativeArgumentType<TypedKey<T>>()
}
