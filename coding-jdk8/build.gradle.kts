import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (JDK8+ coding extension)") {
	targets {
		jvmJdk8 {
			dependencies {
				api(project(":fluid-json-coding"))
			}
		}
	}
}
