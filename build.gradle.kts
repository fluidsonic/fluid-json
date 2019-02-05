import com.github.fluidsonic.fluid.library.*
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	id("com.github.fluidsonic.fluid-library") version "0.9.0"
	jacoco
}

fluidLibrary {
	name = "fluid-json"
	version = "0.9.12"
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
		testImplementation("ch.tutteli.atrium:atrium-cc-en_GB-robstoll:0.7.0")
		testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")

		testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.2")
		testRuntimeOnly("org.junit.platform:junit-platform-runner:1.3.2")
	}
}

