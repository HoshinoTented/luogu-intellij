import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
	java
	id("org.jetbrains.intellij") version "0.4.8"
	kotlin("jvm") version "1.3.41"
}

val isCI = System.getenv("CI").isNullOrBlank().not()

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

fun DependencyHandlerScope.luoguapi(module: String, version: String) = "com.github.HoshinoTented.LuoGuAPI:$module:$version"

dependencies {
	implementation(kotlin("stdlib"))
	implementation(luoguapi("luogu", "2efd9fcbb8"))
	implementation(luoguapi("problem", "2efd9fcbb8"))
}