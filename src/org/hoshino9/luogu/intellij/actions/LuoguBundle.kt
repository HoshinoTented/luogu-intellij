package org.hoshino9.luogu.intellij.actions

import com.intellij.CommonBundle
import org.jetbrains.annotations.PropertyKey
import java.util.ResourceBundle

object LuoguBundle {
	private const val BUNDLE = "org.hoshino9.luogu.intellij.luogu-bundle"
	private val bundle: ResourceBundle by lazy { ResourceBundle.getBundle(BUNDLE) }

	@JvmStatic
	fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
			CommonBundle.message(bundle, key, *params)
}