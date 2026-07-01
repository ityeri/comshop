package com.github.ityeri.comshop

import com.github.ityeri.comshop.api.entry.AbstractCommandRegistrar

object ComshopLoader {
    var registrarClassPath: String = "com.github.ityeri.comshop.impl.CommandRegistrarImpl"

    fun loadRegistrarImpl(): AbstractCommandRegistrar =
        try {
            val clazz = Class.forName(registrarClassPath)
            clazz.getDeclaredConstructor().newInstance() as AbstractCommandRegistrar
        } catch (_: ClassNotFoundException) {
            throw ClassNotFoundException(
                "The registrarClassPath is invalid or cannot find loader class. Is the comshop-impl dependency added?"
            )
        }
}
