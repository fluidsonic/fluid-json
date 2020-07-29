import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (basic variant)") {
	targets {
		jvmJdk7 {
			testDependencies {
				implementation(kotlin("reflect"))
			}
		}
	}
}
