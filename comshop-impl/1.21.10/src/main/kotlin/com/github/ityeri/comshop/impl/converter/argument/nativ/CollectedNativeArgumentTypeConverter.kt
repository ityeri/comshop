package com.github.ityeri.comshop.impl.converter.argument.nativ

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
fun <T : Any> NativeArgumentType<T>.nativeToBrigadierArgumentType(): ArgumentType<T> =
    when (this) {
        is NativeArgumentType.BooleanArgumentType -> {
            BoolArgumentType.bool()
        }
        is NativeArgumentType.IntArgumentType -> {
            IntegerArgumentType.integer(this.min, this.max)
        }
        is NativeArgumentType.FloatArgumentType -> {
            FloatArgumentType.floatArg(this.min, this.max)
        }
        is NativeArgumentType.DoubleArgumentType -> {
            DoubleArgumentType.doubleArg(this.min, this.max)
        }
        is NativeArgumentType.LongArgumentType -> {
            LongArgumentType.longArg(this.min, this.max)
        }
        is NativeArgumentType.StringArgumentType -> {
            when (this.type) {
                StringType.WORD -> StringArgumentType.word()
                StringType.QUOTED -> StringArgumentType.string()
                StringType.GREEDY -> StringArgumentType.greedyString()
            }
        }
        is NativeArgumentType.EntityArgumentType -> {
            CustomNativeArgumentType(ArgumentTypes.entity()) { nativeValue, source ->
                nativeValue.resolve(source).first()
            }
        }
        is NativeArgumentType.EntitiesArgumentType -> {
            CustomNativeArgumentType<List<Entity>, EntitySelectorArgumentResolver>(
                ArgumentTypes.entity()
            ) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }
        is NativeArgumentType.PlayerArgumentType -> {
            CustomNativeArgumentType(ArgumentTypes.player()) { nativeValue, source ->
                nativeValue.resolve(source).first()
            }
        }
        is NativeArgumentType.PlayersArgumentType -> {
            CustomNativeArgumentType<List<Player>, PlayerSelectorArgumentResolver>(
                ArgumentTypes.player()
            ) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }
        is NativeArgumentType.PlayerProfilesArgumentType -> {
            CustomNativeArgumentType<Collection<PlayerProfile>, PlayerProfileListResolver>(
                ArgumentTypes.playerProfiles()
            ) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }
        is NativeArgumentType.BlockPositionArgumentType -> {
            CustomNativeArgumentType(ArgumentTypes.blockPosition()) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }
        is NativeArgumentType.FinePositionArgumentType -> {
            CustomNativeArgumentType(ArgumentTypes.finePosition(this.centerIntegers)) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }
        is NativeArgumentType.RotationArgumentType -> {
            CustomNativeArgumentType(ArgumentTypes.rotation()) { nativeValue, source ->
                nativeValue.resolve(source)
            }
        }
        is NativeArgumentType.BlockStateArgumentType -> {
            ArgumentTypes.blockState()
        }
        is NativeArgumentType.ItemStackArgumentType -> {
            ArgumentTypes.itemStack()
        }
        is NativeArgumentType.ItemPredicateArgumentType -> {
            ArgumentTypes.itemPredicate()
        }
        is NativeArgumentType.NamedColorArgumentType -> {
            ArgumentTypes.namedColor()
        }
        is NativeArgumentType.ComponentArgumentType -> {
            ArgumentTypes.component()
        }
        is NativeArgumentType.StyleArgumentType -> {
            ArgumentTypes.style()
        }
        is NativeArgumentType.SignedMessageArgumentType -> {
            CustomNativeArgumentType(ArgumentTypes.signedMessage()) { nativeValue, source ->
                nativeValue
            }
        }
        is NativeArgumentType.ScoreboardDisplaySlotArgumentType -> {
            ArgumentTypes.scoreboardDisplaySlot()
        }
        is NativeArgumentType.NamespacedKeyArgumentType -> {
            ArgumentTypes.namespacedKey()
        }
        is NativeArgumentType.KeyArgumentType -> {
            ArgumentTypes.key()
        }
        is NativeArgumentType.IntegerRangeArgumentType -> {
            CustomNativeArgumentType<Range<Int>, IntegerRangeProvider>(
                ArgumentTypes.integerRange()
            ) { nativeValue, source ->
                nativeValue.range()
            }
        }
        is NativeArgumentType.DoubleRangeArgumentType -> {
            CustomNativeArgumentType<Range<Double>, DoubleRangeProvider>(
                ArgumentTypes.doubleRange()
            ) { nativeValue, source ->
                nativeValue.range()
            }
        }
        is NativeArgumentType.WorldArgumentType -> {
            ArgumentTypes.world()
        }
        is NativeArgumentType.GameModeArgumentType -> {
            ArgumentTypes.gameMode()
        }
        is NativeArgumentType.HeightMapArgumentType -> {
            ArgumentTypes.heightMap()
        }
        is NativeArgumentType.UuidArgumentType -> {
            ArgumentTypes.uuid()
        }
        is NativeArgumentType.ObjectiveCriteriaArgumentType -> {
            ArgumentTypes.objectiveCriteria()
        }
        is NativeArgumentType.EntityAnchorArgumentType -> {
            ArgumentTypes.entityAnchor()
        }
        is NativeArgumentType.TimeArgumentType -> {
            ArgumentTypes.time(this.minTime)
        }
        is NativeArgumentType.TemplateMirrorArgumentType -> {
            ArgumentTypes.templateMirror()
        }
        is NativeArgumentType.TemplateRotationArgumentType -> {
            ArgumentTypes.templateRotation()
        }
        is NativeArgumentType.ResourceArgumentType<*> -> {
            ArgumentTypes.resource(this.registryKey)
        }
        is NativeArgumentType.ResourceKeyArgumentType<*> -> {
            ArgumentTypes.resourceKey(this.registryKey)
        }
    } as ArgumentType<T>
