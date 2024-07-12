import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "2.0.0"
}

fluidLibrary(name = "json", version = "1.5.0")

allprojects {
	repositories {
		mavenLocal()
	}
}
