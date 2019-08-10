package org.hoshino9.luogu.intellij

import com.google.gson.GsonBuilder
import org.hoshino9.luogu.LuoGu

val lg = LuoGu()
val gson = GsonBuilder()
		.setPrettyPrinting()
		.create()