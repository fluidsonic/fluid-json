import com.github.fluidsonic.fluid.library.*

plugins {
	kotlin("kapt")
}

fluidLibraryVariant {
	jdk = JDK.v1_8
	publishing = false
}

dependencies {
	implementation(project(":fluid-json-coding-jdk8"))
	implementation(project(":fluid-json-annotations"))

	kapt(project(":fluid-json-annotation-processor"))
}
