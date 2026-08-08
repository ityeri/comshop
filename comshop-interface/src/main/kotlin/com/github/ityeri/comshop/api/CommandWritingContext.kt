package com.github.ityeri.comshop.api

class CommandWritingContext(val fullInput: String, val start: Int) {
    val remaining: String
        get() = fullInput.substring(start)
    val remainingLower: String
        get() = remaining.lowercase()
}
