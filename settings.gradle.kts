rootProject.name = "luogu-intellij"

pluginManagement {
	repositories {
		if (System.getenv("CI").isNullOrBlank()) maven("https://maven.aliyun.com/repository/gradle-plugin")
		gradlePluginPortal()
	}
}