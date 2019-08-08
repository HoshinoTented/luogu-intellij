package org.hoshino9.luogu.intellij

import org.hoshino9.luogu.IllegalAPIStatusCodeException
import org.hoshino9.luogu.IllegalStatusCodeException
import org.hoshino9.luogu.intellij.actions.LuoguBundle
import java.awt.Component
import javax.swing.JOptionPane

inline fun tryIt(parent: Component, block: () -> Unit) {
	try {
		block()
	} catch (e: IllegalStatusCodeException) {
		JOptionPane.showMessageDialog(parent, e.message, LuoguBundle.message("luogu.error.title"), JOptionPane.ERROR_MESSAGE)
	} catch (e: IllegalAPIStatusCodeException) {
		JOptionPane.showMessageDialog(parent, e.message, LuoguBundle.message("luogu.error.title"), JOptionPane.ERROR_MESSAGE)
	}
}