import com.github.fluidsonic.fluid.library.*

plugins {
	kotlin("kapt")
}

fluidJvmLibraryVariant {
	jdk = JDK.v1_8
	publishing = false
}

dependencies {
	implementation(project(":fluid-json-coding-jdk8"))
	implementation(project(":fluid-json-annotations"))

	kapt(project(":fluid-json-annotation-processor"))
}

configurations.getByName("kapt") {
	attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, "java-runtime"))
}
