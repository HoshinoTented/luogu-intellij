import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
	java
	id("org.jetbrains.intellij") version "0.4.8"
	kotlin("jvm") version "1.3.41"
}

val luoguVersion = "adc35b396f"
val isCI = System.getenv("CI").isNullOrBlank().not()
fun DependencyHandlerScope.luoguapi(module: String, version: String) = "com.github.HoshinoTented.LuoGuAPI:$module:$version"

group = "org.hoshino9"
version = "0.0.1"

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
	implementation(kotlin("stdlib"))
	implementation(luoguapi("luogu", luoguVersion))
	implementation(luoguapi("problem", luoguVersion))
}