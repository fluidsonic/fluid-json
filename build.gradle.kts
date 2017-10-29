import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.internal.HasConvention
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.JavaExec
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.cli.jvm.config.JavaSourceRoot
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension


group = "com.github.fluidsonic"
version = "0.0.1"

plugins {
	kotlin("jvm") version "1.1.51"
	jacoco
	`java-library`
	`maven-publish`
	id("com.jfrog.bintray") version "1.7.3"
	id("com.github.ben-manes.versions") version "0.17.0"
	id("org.junit.platform.gradle.plugin") version "1.0.1"
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8

	sourceSets {
		"examples" {
			kotlin.srcDirs("Examples")
		}

		"main" {
			kotlin.srcDirs("Sources")
			resources.srcDirs("Resources")
		}

		"test" {
			kotlin.srcDirs("Tests/Sources")
			resources.srcDirs("Tests/Resources")
		}
	}
}

junitPlatform {
	platformVersion = "1.0.1"

	filters {
		engines {
			include("spek")
		}
	}
}

tasks {
	withType<KotlinCompile> {
		sourceCompatibility = "1.8"
		targetCompatibility = "1.8"

		kotlinOptions.jvmTarget = "1.8"
	}
}

afterEvaluate {
	val junitPlatformTest: JavaExec by tasks

	jacoco {
		applyTo(junitPlatformTest)
	}

	task<JacocoReport>("${junitPlatformTest.name}Report") {
		executionData(junitPlatformTest)

		val sourceSet = java.sourceSets["main"]
		sourceDirectories = files(sourceSet.allJava.srcDirs)
		classDirectories = files(sourceSet.output)

		reports {
			xml.destination = file("$buildDir/reports/jacoco.xml")
			xml.isEnabled = true
			html.isEnabled = false
		}

		tasks["check"].dependsOn(this)
	}
}

dependencies {
	api(kotlin("reflect", "1.1.51"))
	api(kotlin("stdlib", "1.1.51"))

	testImplementation("com.winterbe:expekt:0.5.0")
	testImplementation("org.jetbrains.spek:spek-subject-extension:1.1.5")
	testRuntime("org.jetbrains.spek:spek-junit-platform-engine:1.1.5")
	testRuntime("org.junit.platform:junit-platform-runner:${junitPlatform.platformVersion}")

	"examplesImplementation"(java.sourceSets["main"].output)
}

configurations["examplesImplementation"].extendsFrom(configurations["api"])
configurations.all {
	resolutionStrategy.apply {
		force("org.jetbrains.kotlin:kotlin-reflect:1.1.51")
		force("org.jetbrains.kotlin:kotlin-stdlib:1.1.51")

		failOnVersionConflict()
	}
}

repositories {
	jcenter()
	mavenCentral()
}


// extension for configuration
fun JUnitPlatformExtension.filters(setup: FiltersExtension.() -> Unit) {
	when (this) {
		is ExtensionAware -> extensions.getByType(FiltersExtension::class.java).setup()
		else -> throw Exception("${this::class} must be an instance of ExtensionAware")
	}
}

fun FiltersExtension.engines(setup: EnginesExtension.() -> Unit) {
	when (this) {
		is ExtensionAware -> extensions.getByType(EnginesExtension::class.java).setup()
		else -> throw Exception("${this::class} must be an instance of ExtensionAware")
	}
}

val SourceSet.kotlin get() = (this as HasConvention).convention.getPlugin<KotlinSourceSet>().kotlin


// publishing

val sourcesJar = task<Jar>("sourcesJars") {
	dependsOn += "classes"
	classifier = "sources"

	from(java.sourceSets.getByName("main").allSource)
}

configure<BintrayExtension> {
	user = findProperty("bintrayUser") as String?
	key = findProperty("bintrayApiKey") as String?

	setPublications("default")

	pkg.apply {
		repo = "maven"
		name = "fluid-json"
		publicDownloadNumbers = false
		publish = true
		vcsUrl = "https://github.com/fluidsonic/fluid-json.git"
		websiteUrl = "https://github.com/fluidsonic/fluid-json"

		setLicenses("MIT")

		version.apply {
			name = project.version as String?
			vcsTag = project.version as String?
		}
	}
}

configure<PublishingExtension> {
	publications {
		create<MavenPublication>("default") {
			artifactId = "fluid-json"

			from(components.getByName("java"))

			artifact(sourcesJar)
		}
	}
}
