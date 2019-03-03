import com.github.fluidsonic.fluid.library.*

plugins {
	kotlin("kapt")
}

fluidLibraryVariant {
	description = "A JSON library written in pure Kotlin (annotation processor)"
	jdk = JDK.v1_8
}

dependencies {
	implementation(project(":fluid-json-annotations"))
	implementation(project(":fluid-json-coding"))

	implementation(kotlin("reflect"))
	implementation(fluid("meta-jvm", "0.9.5"))
	implementation(fluid("stdlib-jdk8", "0.9.1"))
	implementation("com.google.auto:auto-common:0.10")
	implementation("com.google.auto.service:auto-service:1.0-rc4")
	implementation("com.squareup:kotlinpoet:1.1.0")

	kapt("com.google.auto.service:auto-service:1.0-rc4")

	testImplementation(fluid("compiler", "0.9.3"))
}
