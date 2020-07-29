import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (JDK8+ coding extension)") {
	targets {
		jvm {
			dependencies {
				api(project(":fluid-json-coding"))
			}
		}
	}
}
