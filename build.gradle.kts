import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
	java
	id("org.jetbrains.intellij") version "0.4.8"
	id("com.github.johnrengelman.shadow") version "5.1.0"
	kotlin("jvm") version "1.3.41"
}

val luoguVersion = "4e605d15e6"
val isCI = System.getenv("CI").isNullOrBlank().not()
fun luoguapi(module: String, version: String) = "com.github.HoshinoTented.LuoGuAPI:$module:$version"

group = "org.hoshino9"
version = "0.0.2"

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
}

(tasks["shadowJar"] as ShadowJar).apply {
	archiveClassifier.set("all")
}