package com.github.ityeri.comshop.impl.converter.argument

import com.destroystokyo.paper.profile.PlayerProfile
import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.github.ityeri.comshop.api.argument.StringType
import com.google.common.collect.Range
import com.mojang.brigadier.arguments.*
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.range.DoubleRangeProvider
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import org.bukkit.entity.Entity
import org.bukkit.entity.Player


@Suppress("UNCHECKED_CAST")
fun <T : Any> NativeArgumentType<T>.nativeTypeToBrigadierArgumentType(): ArgumentType<T> =
    when (this) {
        is NativeArgumentType.BooleanType -> {
            BoolArgumentType.bool()
        }

        is NativeArgumentType.IntType -> {
            IntegerArgumentType.integer(this.min, this.max)
        }

        is NativeArgumentType.FloatType -> {
            FloatArgumentType.floatArg(this.min, this.max)
        }

        is NativeArgumentType.DoubleType -> {
            DoubleArgumentType.doubleArg(this.min, this.max)
        }

        is NativeArgumentType.LongType -> {
            LongArgumentType.longArg(this.min, this.max)
        }

        is NativeArgumentType.StringArgType -> {
            when (this.type) {
                StringType.WORD -> StringArgumentType.word()
                StringType.QUOTED -> StringArgumentType.string()
                StringType.GREEDY -> StringArgumentType.greedyString()
            }
        }

        is NativeArgumentType.Entity -> {
            CustomNativeArgumentType<Entity, EntitySelectorArgumentResolver>(
                ArgumentTypes.entity()
            ) { nativeValue, source ->
                nativeValue.resolve(source).first()
            }
        }

        is NativeArgumentType.Entities -> {
            CustomNativeArgumentType<List<Entity>, EntitySelectorArgumentResolver>(
                ArgumentTypes.entities()
            ) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }

        is NativeArgumentType.Player -> {
            CustomNativeArgumentType<Player, PlayerSelectorArgumentResolver>(
                ArgumentTypes.player()
            ) { nativeValue, source ->
                nativeValue.resolve(source).first()
            }
        }

        is NativeArgumentType.Players -> {
            CustomNativeArgumentType<List<Player>, PlayerSelectorArgumentResolver>(
                ArgumentTypes.players()
            ) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }

        is NativeArgumentType.PlayerProfiles -> {
            CustomNativeArgumentType<Collection<PlayerProfile>, PlayerProfileListResolver>(
                ArgumentTypes.playerProfiles()
            ) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }

        is NativeArgumentType.BlockPosition -> {
            CustomNativeArgumentType(ArgumentTypes.blockPosition()) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }

        is NativeArgumentType.FinePosition -> {
            CustomNativeArgumentType(
                ArgumentTypes.finePosition(this.centerIntegers)
            ) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }

        is NativeArgumentType.Rotation -> {
            CustomNativeArgumentType(ArgumentTypes.rotation()) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }

        is NativeArgumentType.BlockState -> {
            ArgumentTypes.blockState()
        }

        is NativeArgumentType.ItemStack -> {
            ArgumentTypes.itemStack()
        }

        is NativeArgumentType.ItemPredicate -> {
            ArgumentTypes.itemPredicate()
        }

        is NativeArgumentType.NamedColor -> {
            ArgumentTypes.namedColor()
        }

        is NativeArgumentType.Component -> {
            ArgumentTypes.component()
        }

        is NativeArgumentType.Style -> {
            ArgumentTypes.style()
        }

        is NativeArgumentType.SignedMessage -> {
            ArgumentTypes.signedMessage()
        }

        is NativeArgumentType.ScoreboardDisplaySlot -> {
            ArgumentTypes.scoreboardDisplaySlot()
        }

        is NativeArgumentType.NamespacedKey -> {
            ArgumentTypes.namespacedKey()
        }

        is NativeArgumentType.Key -> {
            ArgumentTypes.key()
        }

        is NativeArgumentType.IntegerRange -> {
            CustomNativeArgumentType<Range<Int>, IntegerRangeProvider>(
                ArgumentTypes.integerRange()
            ) { nativeValue, source ->
                nativeValue.range()
            }
        }

        is NativeArgumentType.DoubleRange -> {
            CustomNativeArgumentType<Range<Double>, DoubleRangeProvider>(
                ArgumentTypes.doubleRange()
            ) { nativeValue, source ->
                nativeValue.range()
            }
        }

        is NativeArgumentType.World -> {
            ArgumentTypes.world()
        }

        is NativeArgumentType.GameMode -> {
            ArgumentTypes.gameMode()
        }

        is NativeArgumentType.HeightMap -> {
            ArgumentTypes.heightMap()
        }

        is NativeArgumentType.Uuid -> {
            ArgumentTypes.uuid()
        }

        is NativeArgumentType.ObjectiveCriteria -> {
            ArgumentTypes.objectiveCriteria()
        }

        is NativeArgumentType.EntityAnchor -> {
            ArgumentTypes.entityAnchor()
        }

        is NativeArgumentType.Time -> {
            ArgumentTypes.time(this.minTime)
        }

        is NativeArgumentType.TemplateMirror -> {
            ArgumentTypes.templateMirror()
        }

        is NativeArgumentType.TemplateRotation -> {
            ArgumentTypes.templateRotation()
        }

        is NativeArgumentType.Resource<*> -> {
            ArgumentTypes.resource(this.registryKey)
        }

        is NativeArgumentType.ResourceKey<*> -> {
            ArgumentTypes.resourceKey(this.registryKey)
        }
    } as ArgumentType<T>
