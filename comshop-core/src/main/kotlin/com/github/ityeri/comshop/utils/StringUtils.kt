package com.github.ityeri.comshop.utils

fun String.trimStartUnlessEmpty(): String {
    if (trimStart().isNotEmpty()) {
        return trimStart()
    } else {
        return this
    }
}
