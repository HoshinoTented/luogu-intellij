import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
	java
	id("org.jetbrains.intellij") version "0.4.15"
	id("com.github.johnrengelman.shadow") version "5.2.0"
	kotlin("jvm") version "1.3.61"
}

val luoguVersion = "0.0.8b1"
group = "org.hoshino9"
version = "0.0.4"

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
	val ideaPath: String? by extra

	if (ideaPath != null) {
		localPath = ideaPath
	} else version = "2019.3"
}

repositories {
	if (isCI) jcenter() else {
		maven("https://maven.aliyun.com/repository/public")
	}

	maven("https://jitpack.io")
}


dependencies {
	implementation(kotlin("stdlib"))
	implementation(luoguapi("core", luoguVersion))
	implementation(luoguapi("problem", luoguVersion))
	implementation(luoguapi("record", luoguVersion))
	implementation(luoguapi("paste", luoguVersion))

	implementation("org.slf4j:slf4j-simple:1.7.30")
}

tasks.withType<PatchPluginXmlTask> {
	changeNotes(file("resources/META-INF/ChangeNotes.html").readText())
	pluginDescription(file("resources/META-INF/Description.html").readText())
	version(version)
	pluginId(project.name)
}