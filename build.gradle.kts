import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
	java
	id("org.jetbrains.intellij") version "0.4.8"
	id("com.github.johnrengelman.shadow") version "5.1.0"
	kotlin("jvm") version "1.3.41"
}

group = "org.hoshino9"
version = "0.0.3"


val luoguVersion = "d21bf691c6"
val isCI = System.getenv("CI").isNullOrBlank().not()
fun luoguapi(module: String, version: String) = "com.github.HoshinoTented.LuoGuAPI:$module:$version"

sourceSets {
	main.configure {
		withConvention(KotlinSourceSet::class) {
			listOf(java, kotlin).forEach { it.srcDirs("src") }
		}

		resources.srcDir("resources")
	}
}

intellij {
	version = "2019.2"
}

repositories {
	if (isCI) jcenter() else {
		maven("https://maven.aliyun.com/repository/public")
	}

	maven("https://jitpack.io")
}


dependencies {
	compile(kotlin("stdlib"))
	compile(luoguapi("luogu", luoguVersion))
	compile(luoguapi("problem", luoguVersion))
	compile(luoguapi("record", luoguVersion))
	compile(luoguapi("paste", luoguVersion))
}

tasks.withType<PatchPluginXmlTask> {
	changeNotes(file("resources/META-INF/ChangeNotes.html").readText())
	pluginDescription(file("resources/META-INF/Description.html").readText())
	version(version)
	pluginId(project.name)
}