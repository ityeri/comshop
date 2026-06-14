package com.github.ityeri.comshop.impl.converter.argument.nativ

import com.github.ityeri.comshop.api.argument.NativeArgumentType
import com.github.ityeri.comshop.api.argument.StringType
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType


class BooleanArgumentTypeConverter :
    NativeArgumentTypeConverter<Boolean, NativeArgumentType.BooleanArgumentType>({ original ->
        BoolArgumentType.bool()
    })
class IntArgumentTypeConverter :
    NativeArgumentTypeConverter<Int, NativeArgumentType.IntArgumentType>({ original ->
        IntegerArgumentType.integer(original.min, original.max)
    })
class FloatArgumentTypeConverter :
    NativeArgumentTypeConverter<Float, NativeArgumentType.FloatArgumentType>({ original ->
        FloatArgumentType.floatArg(original.min, original.max)
    })
class DoubleArgumentTypeConverter :
    NativeArgumentTypeConverter<Double, NativeArgumentType.DoubleArgumentType>({ original ->
        DoubleArgumentType.doubleArg(original.min, original.max)
    })
class LongArgumentTypeConverter :
    NativeArgumentTypeConverter<Long, NativeArgumentType.LongArgumentType>({ original ->
        LongArgumentType.longArg(original.min, original.max)
    })
class StringArgumentTypeConverter :
    NativeArgumentTypeConverter<String, NativeArgumentType.StringArgumentType>({ original ->
        when (original.type) {
            StringType.WORD -> StringArgumentType.word()
            StringType.QUOTED -> StringArgumentType.string()
            StringType.GREEDY -> StringArgumentType.greedyString()
        }
    })
