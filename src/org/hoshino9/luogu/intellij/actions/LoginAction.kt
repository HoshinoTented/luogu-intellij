package org.hoshino9.luogu.intellij.actions

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.popup.JBPopupFactory
import org.hoshino9.luogu.IllegalAPIStatusCodeException
import org.hoshino9.luogu.IllegalStatusCodeException
import org.hoshino9.luogu.intellij.actions.ui.LoginUI
import org.hoshino9.luogu.intellij.lg
import org.hoshino9.luogu.intellij.tryIt
import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import javax.imageio.ImageIO
import javax.swing.Action
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JOptionPane
import kotlin.concurrent.thread

class LoginUIImpl(val project: Project) : LoginUI() {
	private val verifyCodeFile = File("${project.basePath}/.idea/verifyCode.jpg")
	private var isRequestingVerifyCode = false

	init {
		title = LuoguBundle.message("luogu.login.title")
		myOKAction = object : DialogWrapperAction(LuoguBundle.message("luogu.login")) {
			override fun doAction(e: ActionEvent?) {
				tryIt(mainPanel) {
					lg.login(account.text, String(password.password), verifyCodeInput.text)
					val name = lg.loggedUser.spacePage.username
					JOptionPane.showMessageDialog(mainPanel, LuoguBundle.message("luogu.loginwith", name), LuoguBundle.message("luogu.successful.title"), JOptionPane.INFORMATION_MESSAGE)
					close(DialogWrapper.OK_EXIT_CODE)


					ActionManager.getInstance()
							.getAction("org.hoshino9.luogu.intellij.actions.LoginAction")
							.templatePresentation
							.text = LuoguBundle.message("luogu.loginwith", name)
				}
			}
		}

		verifyCode.addMouseListener(object : MouseListener {
			override fun mouseClicked(e: MouseEvent?) {
				if (isRequestingVerifyCode) return

				updateVerifyCode()
			}

			override fun mouseReleased(e: MouseEvent?) {
			}

			override fun mouseEntered(e: MouseEvent?) {
			}

			override fun mouseExited(e: MouseEvent?) {
			}

			override fun mousePressed(e: MouseEvent?) {
			}

		})

		setResizable(false)

		init()

		updateVerifyCode()
	}

	override fun createCenterPanel(): JComponent? {
		return mainPanel
	}

	@Synchronized
	private fun updateVerifyCode() {
		lg.verifyCode(verifyCodeFile.outputStream())
		verifyCode.icon = ImageIcon(ImageIO.read(verifyCodeFile))
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