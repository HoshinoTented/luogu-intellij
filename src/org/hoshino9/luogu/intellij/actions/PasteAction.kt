package org.hoshino9.luogu.intellij.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import org.hoshino9.luogu.intellij.actions.ui.PasteUI
import org.hoshino9.luogu.intellij.checkLogin
import org.hoshino9.luogu.intellij.lg
import org.hoshino9.luogu.intellij.tryIt
import org.hoshino9.luogu.paste.postPaste
import java.awt.event.ActionEvent
import javax.swing.JComponent
import javax.swing.JOptionPane

class PasteUIImpl(val file: VirtualFile, val editor: Editor) : PasteUI() {
	val documentText: String
		get() {
			return if (file.extension == "md") editor.document.text else """```${file.extension}
${editor.document.text}
```"""
		}

	init {
		myOKAction = object : DialogWrapperAction(LuoguBundle.message("luogu.paste.submit")) {
			override fun doAction(e: ActionEvent?) {
				tryIt(mainPanel) {
					lg.checkLogin()

					val id = lg.loggedUser.postPaste(documentText, makePublic.isSelected)
					JOptionPane.showMessageDialog(mainPanel, LuoguBundle.message("luogu.paste.success", id), LuoguBundle.message("luogu.success.title"), JOptionPane.INFORMATION_MESSAGE)
					close(DialogWrapper.OK_EXIT_CODE)
				}
			}
		}

		title = LuoguBundle.message("luogu.paste.title")

		init()
	}

	override fun createCenterPanel(): JComponent? {
		return mainPanel
	}
}

class PasteAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.getData(CommonDataKeys.EDITOR) ?: return
		val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

		PasteUIImpl(file, editor).show()
	}
}