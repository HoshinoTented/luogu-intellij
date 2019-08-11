package org.hoshino9.luogu.intellij.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import org.hoshino9.luogu.intellij.actions.ui.LoginUI
import org.hoshino9.luogu.intellij.lg
import org.hoshino9.luogu.intellij.tryIt
import org.hoshino9.luogu.intellij.ui.VerifyCode
import org.hoshino9.luogu.intellij.ui.VerifyUIImpl
import org.hoshino9.luogu.intellij.ui.verifyCode
import java.awt.event.ActionEvent
import javax.swing.JComponent
import javax.swing.JOptionPane

class LoginUIImpl(val project: Project) : LoginUI() {
	init {
		title = LuoguBundle.message("luogu.login.title")
		myOKAction = object : DialogWrapperAction(LuoguBundle.message("luogu.login")) {
			override fun doAction(e: ActionEvent?) {
				tryIt(mainPanel) {
					val code = verifyCode(project)
					if (code != null) {
						lg.login(account.text, String(password.password), code)

						val user = lg.loggedUser

						JOptionPane.showMessageDialog(mainPanel, LuoguBundle.message("luogu.login.loginwith", user.spacePage.username, user.uid), LuoguBundle.message("luogu.successful.title"), JOptionPane.INFORMATION_MESSAGE)
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

class LoginAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		LoginUIImpl(e.project ?: return).show()
	}

	override fun update(e: AnActionEvent) {
		e.presentation.isEnabledAndVisible = e.project != null
	}
}