package org.hoshino9.luogu.intellij.actions

import com.google.gson.JsonParser
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import okhttp3.WebSocket
import org.hoshino9.luogu.intellij.actions.ui.RecordUI
import org.hoshino9.luogu.intellij.actions.ui.SubmitUI
import org.hoshino9.luogu.intellij.checkLogin
import org.hoshino9.luogu.intellij.gson
import org.hoshino9.luogu.intellij.lg
import org.hoshino9.luogu.intellij.tryIt
import org.hoshino9.luogu.record.Record
import org.hoshino9.luogu.record.Solution
import org.hoshino9.luogu.record.postSolution
import java.awt.event.ActionEvent
import javax.swing.ButtonGroup
import javax.swing.JComponent
import javax.swing.JOptionPane
import javax.swing.JScrollPane

class RecordUIImpl(val record: Record) : RecordUI() {
	private var socket: WebSocket? = null

	init {
		connect()

		scrollPane.apply {
			horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
			verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
		}

		infoText.apply {
			lineWrap = true
			isEditable = false
		}

		title = (LuoguBundle.message("luogu.record.title", record.rid))

		init()
	}

	private fun connect() {
		socket = this@RecordUIImpl.record.listen(lg) { socket, msg ->
			if ("status_push" in msg) updateInfo(msg)
		}
	}

	private fun closeSocket() {
		socket?.close(1000, null)
	}

	private fun updateInfo(info: String) {
		this.infoText.text = gson.toJson(JsonParser().parse(info))
	}

	override fun doOKAction() {
		closeSocket()
		super.doOKAction()
	}

	override fun doCancelAction() {
		closeSocket()
		super.doCancelAction()
	}

	override fun createCenterPanel(): JComponent? {
		return mainPanel
	}
}

class SubmitUIImpl(val file: VirtualFile, val editor: Editor, val project: Project) : SubmitUI() {
	companion object {
		val regexp = Regex("[A-Z]+[1-9][0-9]+")
	}

	private val buttonGroup = ButtonGroup()

	private val problemId: String
		get() {
			return if (buttonGroup.selection == useFileName.model) file.nameWithoutExtension else {
				customName.text
			}
		}

	private val currentLanguage: Solution.Language
		get() {
			return when (file.extension) {
				"c", "h" -> Solution.Language.C
				"cpp", "hpp", "cc" -> Solution.Language.Cpp
				"py" -> Solution.Language.Python3
				"java" -> Solution.Language.Java8
				"js" -> Solution.Language.NodeJs
				"rb" -> Solution.Language.Ruby
				"go" -> Solution.Language.Go
				"rs" -> Solution.Language.Rust
				"php" -> Solution.Language.PHP7
				"cs" -> Solution.Language.CS
				"vb" -> Solution.Language.VB
				"hs" -> Solution.Language.Haskell
				"kt" -> Solution.Language.KotlinJVM
				"scala" -> Solution.Language.Scala
				"pl" -> Solution.Language.Perl5

				else -> Solution.Language.Auto
			}
		}

	init {
		title = LuoguBundle.message("luogu.submit.title")

		useFileName.apply {
			addActionListener { _ ->
				customName.isEnabled = false
			}
		}

		useCustomName.apply {
			addActionListener { _ ->
				customName.isEnabled = true
			}
		}

		language.apply {
			Solution.Language.values().forEach(::addItem)

			selectedIndex = Solution.Language.values().indexOf(currentLanguage)
		}

		buttonGroup.add(useFileName)
		buttonGroup.add(useCustomName)

		(if (regexp.matches(file.nameWithoutExtension)) {
			useFileName
		} else useCustomName).apply {
			isSelected = true
			actionListeners.forEach {
				it.actionPerformed(null)
			}
		}

		myOKAction = object : DialogWrapperAction(LuoguBundle.message("luogu.submit.submit")) {
			override fun doAction(e: ActionEvent?) {
				tryIt(mainPanel) {
					lg.checkLogin()

					val solution = Solution(problemId, Solution.Language.values()[language.selectedIndex], editor.document.text)
					val record = lg.loggedUser.postSolution(solution)

					JOptionPane.showMessageDialog(mainPanel, LuoguBundle.message("luogu.submit.success", record.rid), LuoguBundle.message("luogu.success.title"), JOptionPane.INFORMATION_MESSAGE)

					close(DialogWrapper.OK_EXIT_CODE)
					RecordUIImpl(record).show()
				}
			}
		}

		init()
	}

	override fun createCenterPanel(): JComponent? {
		return mainPanel
	}
}

class SubmitAction : AnAction() {
	private fun doAction(file: VirtualFile, editor: Editor, project: Project) {
		SubmitUIImpl(file, editor, project).show()
	}

	override fun actionPerformed(e: AnActionEvent) {
		val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
		val editor = e.getData(CommonDataKeys.EDITOR) ?: return
		val project = e.project ?: return

		doAction(file, editor, project)
	}
}