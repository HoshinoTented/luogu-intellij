package org.hoshino9.luogu.intellij

import com.google.gson.JsonParser
import io.ktor.client.features.ClientRequestException
import kotlinx.coroutines.runBlocking
import org.hoshino9.luogu.LuoGu
import org.hoshino9.luogu.intellij.actions.LuoguBundle
import org.hoshino9.luogu.utils.strData
import java.awt.Component
import javax.swing.JOptionPane

fun tryIt(parent: Component?, block: suspend () -> Unit) {
	runBlocking {
		val err = try {
			block()
		} catch (e: ClientRequestException) {
			val json = JsonParser().parse(e.response.strData()).asJsonObject
			json["errorMessage"].asString
		} catch (e: IllegalStateException) {
			e.message
		}

		if (err is String) {
			JOptionPane.showMessageDialog(parent, err, LuoguBundle.message("luogu.error.title"), JOptionPane.ERROR_MESSAGE)
		}
	}
}

fun LuoGu.checkLogin() {
	if (! isLogged) throw IllegalStateException("No login")
}