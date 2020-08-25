import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (coding extension)") {
	targets {
		jvm {
			dependencies {
				api(project(":fluid-json-annotations"))
				api(project(":fluid-json-basic"))
				api(kotlin("reflect"))
			}
		}
	}
}
