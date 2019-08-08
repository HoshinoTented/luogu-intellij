package org.hoshino9.luogu.intellij.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import org.hoshino9.luogu.IllegalStatusCodeException
import org.hoshino9.luogu.intellij.actions.ui.SubmitUI
import org.hoshino9.luogu.intellij.lg
import org.hoshino9.luogu.intellij.tryIt
import org.hoshino9.luogu.record.Solution
import org.hoshino9.luogu.record.postSolution
import java.awt.event.ActionEvent
import javax.swing.ButtonGroup
import javax.swing.JComponent
import javax.swing.JOptionPane

class SubmitUIImpl(val file: VirtualFile, val editor: Editor) : SubmitUI() {
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

		(if (regexp.matches(file.nameWithoutExtension)) {
			useFileName
		} else useCustomName).isSelected = true

		buttonGroup.add(useFileName)
		buttonGroup.add(useCustomName)

		myOKAction = object : DialogWrapperAction(LuoguBundle.message("luogu.submit.submit")) {
			override fun doAction(e: ActionEvent?) {
				tryIt(mainPanel) {
					if (lg.isLogged) {
						val record = lg.loggedUser.postSolution(Solution(problemId, Solution.Language.values()[language.selectedIndex], editor.document.text))
						JOptionPane.showMessageDialog(mainPanel, LuoguBundle.message("luogu.submit.successful", record.rid), LuoguBundle.message("luogu.successful.title"), JOptionPane.INFORMATION_MESSAGE)
						close(DialogWrapper.OK_EXIT_CODE)
					} else {
						throw IllegalStatusCodeException(403, "No login")
					}
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
	private fun doAction(file: VirtualFile, editor: Editor) {
		SubmitUIImpl(file, editor).show()
	}

	override fun actionPerformed(e: AnActionEvent) {
		val file = e.getData(PlatformDataKeys.VIRTUAL_FILE) ?: return
		val editor = e.getData(CommonDataKeys.EDITOR) ?: return

		doAction(file, editor)
	}
}