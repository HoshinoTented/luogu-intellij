package org.hoshino9.luogu.intellij.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.hoshino9.luogu.intellij.actions.ui.UnlockUI
import org.hoshino9.luogu.intellij.lg
import org.hoshino9.luogu.intellij.tryIt
import javax.swing.JComponent
import javax.swing.JOptionPane

class UnlockUIImpl(type: String) : UnlockUI() {
	init {
		init()

		title = LuoguBundle.message("luogu.unlock.dialog.title", when (type) {
			"2fa" -> "Two Factor"
			"secret" -> "Password"

			else -> IllegalArgumentException(type)
		})
	}

	override fun doOKAction() {
		tryIt(mainPanel) {
			lg.unlock(unlockCode.text)

			super.doOKAction()
		}
	}

	override fun createCenterPanel(): JComponent? {
		return mainPanel
	}
}

class UnlockAction : AnAction(
		LuoguBundle.message("luogu.unlock.title"),
		LuoguBundle.message("luogu.unlock.description"), null
) {
	override fun actionPerformed(e: AnActionEvent) {
		lg.needUnlock?.let {
			UnlockUIImpl(it).show()
		} ?: JOptionPane.showMessageDialog(null, LuoguBundle.message("luogu.unlock.neednt"), LuoguBundle.message("luogu.error.title"), JOptionPane.ERROR_MESSAGE)
	}
}