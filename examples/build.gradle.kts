import com.github.fluidsonic.fluid.library.*

fluidLibraryVariant {
	jdk = JDK.v1_8
	publishing = false
}

dependencies {
	implementation(project(":fluid-json-coding-jdk8"))
}
