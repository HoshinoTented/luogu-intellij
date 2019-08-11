package org.hoshino9.luogu.intellij

import com.intellij.openapi.project.Project
import org.hoshino9.luogu.IllegalStatusCodeException
import org.hoshino9.luogu.LuoGu
import org.hoshino9.luogu.intellij.actions.LuoguBundle
import org.hoshino9.luogu.intellij.ui.VerifyCode
import org.hoshino9.luogu.intellij.ui.VerifyUIImpl
import java.awt.Component
import javax.swing.JOptionPane

inline fun tryIt(parent: Component, block: () -> Unit) {
	try {
		block()
	} catch (e: IllegalStatusCodeException) {
		JOptionPane.showMessageDialog(parent, e.message, LuoguBundle.message("luogu.error.title"), JOptionPane.ERROR_MESSAGE)
	}
}

fun LuoGu.checkLogin() {
	if (! isLogged) throw IllegalStatusCodeException(403, "No login")
}