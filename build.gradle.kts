import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "3.0.0"
}

fluidLibrary(name = "json", version = "2.0.0")

allprojects {
	repositories {
		mavenLocal()

		val githubToken = System.getenv("GITHUB_PACKAGES_AUTH_TOKEN") ?: findProperty("GITHUB_PACKAGES_AUTH_TOKEN") as String?
		if (githubToken != null)
			maven("https://maven.pkg.github.com/fluidsonic/fluid-meta") {
				credentials {
					username = "unused"
					password = githubToken
				}
			}
	}
}
