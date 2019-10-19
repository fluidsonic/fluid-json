import io.fluidsonic.gradle.*

plugins {
	kotlin("kapt")
}

fluidJvmLibraryVariant(JvmTarget.jdk8) {
	publishing = false
}

dependencies {
	implementation(project(":fluid-json-coding-jdk8"))
	implementation(project(":fluid-json-annotations"))

	kapt(project(":fluid-json-annotation-processor"))
}
