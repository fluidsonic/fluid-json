import com.github.fluidsonic.fluid.library.*
import org.jetbrains.kotlin.gradle.plugin.*

plugins {
	kotlin("kapt")
}

fluidJvmLibraryVariant {
	description = "A JSON library written in pure Kotlin (annotation processor)"
	jdk = JvmTarget.jdk8
}

dependencies {
	implementation(project(":fluid-json-annotations"))
	implementation(project(":fluid-json-coding"))

	implementation(kotlin("reflect"))
	implementation(fluid("meta-jvm", "0.9.11"))
	implementation(fluid("stdlib", "0.9.25")) {
		attributes {
			attribute(KotlinPlatformType.attribute, KotlinPlatformType.jvm)
			attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, Usage.JAVA_RUNTIME))
			attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
		}
	}
	implementation("com.google.auto:auto-common:0.10")
	implementation("com.google.auto.service:auto-service:1.0-rc6")
	implementation("com.squareup:kotlinpoet:1.4.0")

	kapt("com.google.auto.service:auto-service:1.0-rc6")

	testImplementation(fluid("compiler", "0.9.6"))
}

configurations {
	all {
		// https://youtrack.jetbrains.com/issue/KT-31641
		// https://youtrack.jetbrains.com/issue/KT-33206

		if (name.contains("kapt"))
			attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, Usage.JAVA_RUNTIME))
	}
}

