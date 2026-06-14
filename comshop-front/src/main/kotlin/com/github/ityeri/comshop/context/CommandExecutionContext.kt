package com.github.ityeri.comshop.context

import com.github.ityeri.comshop.ComshopDsl
import com.github.ityeri.comshop.api.ComshopContext
import kotlin.reflect.KClass


@ComshopDsl
class CommandExecutionContext(val context: ComshopContext) : SourceContext(context.source) {
    infix fun <T : Any> String.to(clazz: KClass<T>): T =
        context.getArgument(this, clazz.java)

    infix fun <T : Any> String.toOrNull(clazz: KClass<T>): T? =
        try {
            context.getArgument(this, clazz.java)
        } catch (_: IllegalArgumentException) {
            null
        }
}
