import org.gradle.api.internal.HasConvention
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.com.intellij.util.indexing.ID.findByName
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension


group = "com.github.fluidsonic"
version = "0.0.1"

plugins {
	kotlin("jvm", "1.1.51")
	jacoco
	`java-library`
	id("com.github.ben-manes.versions") version "0.15.0"
	id("org.junit.platform.gradle.plugin") version "1.0.1"
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8

	sourceSets.getByName("main").apply {
		kotlin.srcDirs("Sources")
		resources.srcDirs("Resources")
	}
	sourceSets.getByName("test").apply {
		kotlin.srcDirs("Tests/Sources")
		resources.srcDirs("Tests/Resources")
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
	implementation(kotlin("stdlib", "1.1.51"))
	implementation("org.apiguardian:apiguardian-api:1.0.0")

	testImplementation("org.jetbrains.spek:spek-api:1.1.5")
	testRuntime("org.jetbrains.spek:spek-junit-platform-engine:1.1.5")
	testRuntime("org.junit.platform:junit-platform-runner:${junitPlatform.platformVersion}")
}

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
