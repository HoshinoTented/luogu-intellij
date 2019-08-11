package org.hoshino9.luogu.intellij.ui

import com.intellij.openapi.project.Project
import org.hoshino9.luogu.intellij.actions.LuoguBundle
import org.hoshino9.luogu.intellij.lg
import org.hoshino9.luogu.intellij.ui.ui.VerifyUI
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JComponent

data class VerifyCode(var code: String)

class VerifyUIImpl(val project: Project, val code: VerifyCode) : VerifyUI() {
	private val verifyCodeFile = File("${project.basePath}/.idea/verifyCode.jpg")

	init {
		verifyCode.addMouseListener(object : MouseListener {
			override fun mouseClicked(e: MouseEvent?) {
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

		updateVerifyCode()

		title = LuoguBundle.message("luogu.verify.title")
		setResizable(false)

		init()
	}

	@Synchronized
	private fun updateVerifyCode() {
		lg.verifyCode(verifyCodeFile.outputStream())
		verifyCode.icon = ImageIcon(ImageIO.read(verifyCodeFile))
	}

	override fun doOKAction() {
		code.code = verifyCodeText.text

		super.doOKAction()
	}

	override fun createCenterPanel(): JComponent? {
		return mainPanel
	}
}

fun verifyCode(project: Project): String? {
	val code = VerifyCode("")

	return if (VerifyUIImpl(project, code).showAndGet()) {
		code.code
	} else null
}