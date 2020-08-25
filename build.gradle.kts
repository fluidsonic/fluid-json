import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.1.8"
}

fluidLibrary(name = "json", version = "1.1.1") {
	allModules {
		publishSingleTargetAsModule()
	}
}
