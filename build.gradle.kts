import com.github.fluidsonic.fluid.library.*
import org.gradle.api.tasks.testing.logging.*

plugins {
	id("com.github.fluidsonic.fluid-library") version "0.9.10"
	jacoco
}

fluidJvmLibrary {
	name = "fluid-json"
	version = "0.9.20"
}


subprojects {
	apply<JacocoPlugin>()
	apply<JUnitTestSuitePlugin>()
	apply<TestingBasePlugin>()

	jacoco {
		toolVersion = "0.8.3"
	}

	tasks {
		val check by this

		withType<Test> {
			useJUnitPlatform {
				includeEngines("junit-jupiter")
			}

			testLogging {
				events(TestLogEvent.FAILED, TestLogEvent.SKIPPED)

				exceptionFormat = TestExceptionFormat.FULL
				showExceptions = true
				testLogging.showStandardStreams = true
			}
		}

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
		testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.0")

		testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.0")
		testRuntimeOnly("org.junit.platform:junit-platform-runner:1.4.0")
	}
}
