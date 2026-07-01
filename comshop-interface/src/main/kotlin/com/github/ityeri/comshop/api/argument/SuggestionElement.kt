package com.github.ityeri.comshop.api.argument

import net.kyori.adventure.text.Component

data class SuggestionElement(val text: String, val tooltipMessage: Component? = null)