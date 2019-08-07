package org.hoshino9.luogu.intellij.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.popup.JBPopupFactory
import org.hoshino9.luogu.intellij.actions.ui.LoginUI

class LoginUIImpl : LoginUI() {
	init {

	}
}

class LoginAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val factory = JBPopupFactory
				.getInstance()
				.createComponentPopupBuilder(LoginUIImpl(), null)
				.setMovable(true)
				.createPopup()

		factory.show(JBPopupFactory.getInstance().guessBestPopupLocation(e.dataContext))
	}
}