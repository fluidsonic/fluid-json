import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "3.0.0"
}

fluidLibrary(name = "json", version = "2.0.0")

allprojects {
	repositories {
		mavenLocal()
	}
}
