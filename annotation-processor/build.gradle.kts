import com.github.fluidsonic.fluid.library.*
import org.jetbrains.kotlin.gradle.plugin.*

plugins {
	kotlin("kapt")
}

fluidJvmLibraryVariant {
	description = "A JSON library written in pure Kotlin (annotation processor)"
	jdk = JDK.v1_8
}

dependencies {
	implementation(project(":fluid-json-annotations"))
	implementation(project(":fluid-json-coding"))

	implementation(kotlin("reflect"))
	implementation(fluid("meta-jvm", "0.9.7"))
	implementation(fluid("stdlib", "0.9.23"))
	implementation("com.google.auto:auto-common:0.10")
	implementation("com.google.auto.service:auto-service:1.0-rc5")
	implementation("com.squareup:kotlinpoet:1.3.0")

	kapt("com.google.auto.service:auto-service:1.0-rc5")

	testImplementation(fluid("compiler", "0.9.4"))
}
