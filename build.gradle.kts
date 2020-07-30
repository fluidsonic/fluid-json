import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.1.2"
}

fluidLibrary(name = "json", version = "1.1.0") {
	allModules {
		publishSingleTargetAsModule()
	}
}
