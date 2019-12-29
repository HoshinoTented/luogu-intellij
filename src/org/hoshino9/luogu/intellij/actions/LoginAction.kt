package org.hoshino9.luogu.intellij.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import org.hoshino9.luogu.intellij.actions.ui.LoginUI
import org.hoshino9.luogu.intellij.lg
import org.hoshino9.luogu.intellij.tryIt
import org.hoshino9.luogu.intellij.ui.verifyCode
import org.hoshino9.luogu.user.currentUser
import java.awt.event.ActionEvent
import javax.swing.JComponent
import javax.swing.JOptionPane

class LoginUIImpl(val event: AnActionEvent) : LoginUI() {
	val project = event.project !!

	init {
		title = LuoguBundle.message("luogu.login.title")
		myOKAction = object : DialogWrapperAction(LuoguBundle.message("luogu.login")) {
			override fun doAction(e: ActionEvent?) {
				tryIt(mainPanel) {
					val code = verifyCode(project)
					if (code != null) {
						lg.login(account.text, String(password.password), code)

						lg.needUnlock?.let {
							UnlockUIImpl(it).show()
						}

						val user = lg.currentUser.user
						val title = LuoguBundle.message("luogu.login.loginwith", user.name, user.uid)

						event.presentation.text = title

						JOptionPane.showMessageDialog(mainPanel, title, LuoguBundle.message("luogu.success.title"), JOptionPane.INFORMATION_MESSAGE)
						close(DialogWrapper.OK_EXIT_CODE)
					}
				}
			}
		}

		setResizable(false)

		init()
	}

	override fun createCenterPanel(): JComponent? {
		return mainPanel
	}
}

class LoginAction : AnAction(
		LuoguBundle.message("luogu.login.title"),
		LuoguBundle.message("luogu.login.description"), null
) {
	override fun actionPerformed(e: AnActionEvent) {
		if (lg.isLogged) {
			tryIt(null) {
				lg.logout()

				JOptionPane.showMessageDialog(null, "Logged out!", LuoguBundle.message("luogu.success.title"), JOptionPane.INFORMATION_MESSAGE)
				e.presentation.text = LuoguBundle.message("luogu.login.title")
			}
		} else {
			e.project ?: return
			LoginUIImpl(e).show()
		}
	}
}