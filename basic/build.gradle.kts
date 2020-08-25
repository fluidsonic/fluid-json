import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (basic variant)") {
	targets {
		jvm {
			testDependencies {
				implementation(kotlin("reflect"))
			}
		}
	}
}
