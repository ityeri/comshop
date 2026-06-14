package com.github.ityeri.comshop

import com.github.ityeri.comshop.api.entry.AbstractCommandRegistrar


object CommandRegistrar : AbstractCommandRegistrar by ComshopLoader.loadRegistrarImpl()