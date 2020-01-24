import io.fluidsonic.gradle.*
import org.jetbrains.kotlin.gradle.plugin.*

plugins {
	kotlin("kapt")
}

fluidJvmLibraryVariant(JvmTarget.jdk8) {
	description = "A JSON library written in pure Kotlin (annotation processor)"
}

dependencies {
	implementation(project(":fluid-json-annotations"))
	implementation(project(":fluid-json-coding"))

	implementation(kotlin("reflect"))
	implementation(fluid("meta", "0.9.15"))
	implementation(fluid("stdlib", "0.9.29")) {
		attributes {
			attribute(KotlinPlatformType.attribute, KotlinPlatformType.jvm)
			attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, Usage.JAVA_RUNTIME))
			attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
		}
	}
	implementation("com.google.auto:auto-common:0.10")
	implementation("com.google.auto.service:auto-service:1.0-rc6")
	implementation("com.squareup:kotlinpoet:1.5.0")

	kapt("com.google.auto.service:auto-service:1.0-rc6")

	testImplementation(fluid("compiler", "0.9.9"))
}
