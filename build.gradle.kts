import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.0.6"
	jacoco
}

fluidJvmLibrary(name = "json", version = "1.0.0")


subprojects {
	apply<JacocoPlugin>()

	jacoco {
		toolVersion = "0.8.3"
	}

	tasks {
		val check by this

		withType<JacocoReport> {
			reports {
				xml.isEnabled = true
				html.isEnabled = false
			}

			check.dependsOn(this)
		}
	}

	dependencies {
		testImplementation(kotlin("reflect"))
		testImplementation("ch.tutteli.atrium:atrium-cc-en_GB-robstoll:0.8.0")
	}
}
