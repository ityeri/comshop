package com.github.ityeri.comshop.api

enum class CommandResult {
    SUCCESS,
    FAILED
    ;

    fun toInt(): Int =
        if (this == CommandResult.SUCCESS) 1
        else 0
}
