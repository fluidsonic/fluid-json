import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
	kotlin("jvm") apply false
	`java-library`
	jacoco
	id("com.github.ben-manes.versions") version "0.20.0"
}

tasks.withType<Wrapper> {
	distributionType = Wrapper.DistributionType.ALL
	gradleVersion = "5.0"
}

allprojects {
	group = "com.github.fluidsonic"
	version = "0.9.8"

	apply<KotlinPlatformJvmPlugin>()
	apply<JacocoPlugin>()
	apply<JavaLibraryPlugin>()

	sourceSets {
		getByName("main") {
			kotlin.srcDirs("sources")
			resources.srcDirs("resources")
		}

		getByName("test") {
			kotlin.srcDirs("tests/sources")
			resources.srcDirs("tests/resources")
		}
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_1_7
		targetCompatibility = JavaVersion.VERSION_1_7
	}

	jacoco {
		toolVersion = "0.8.2"
	}

	tasks {
		val check by this

		withType<KotlinCompile> {
			sourceCompatibility = "1.7"
			targetCompatibility = "1.7"

			kotlinOptions.freeCompilerArgs = listOf("-Xuse-experimental=kotlin.contracts.ExperimentalContracts")
			kotlinOptions.jvmTarget = "1.6"
		}

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
		api(kotlin("stdlib-jdk7"))

		//testImplementation("com.winterbe:expekt:0.5.0") {
		//	exclude(group = "org.jetbrains.kotlin")
		//}

		testImplementation("ch.tutteli.atrium:atrium-cc-en_GB-robstoll:0.7.0") {
			exclude(group = "org.jetbrains.kotlin")
		}
		testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")

		testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.2")
		testRuntimeOnly("org.junit.platform:junit-platform-runner:1.3.2") {
			exclude(group = "junit")
		}
	}

	configurations {
		all {
			resolutionStrategy {
				failOnVersionConflict()
			}
		}
	}

	repositories {
		mavenCentral()
		jcenter()
	}
}
