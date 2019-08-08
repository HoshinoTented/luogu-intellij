import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
	java
	id("org.jetbrains.intellij") version "0.4.8"
	kotlin("jvm") version "1.3.41"
}

val luoguVersion = "4e605d15e6"
val isCI = System.getenv("CI").isNullOrBlank().not()
fun DependencyHandler.luoguapi(module: String, version: String) = "com.github.HoshinoTented.LuoGuAPI:$module:$version"

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

val dependenciesJar = task<Jar>("dependenciesJar") {
	from(configurations.getByName("compile").map { if (it.isDirectory) it else zipTree(it) })
}

artifacts {
	add("archives", dependenciesJar)
}